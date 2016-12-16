package com.zt.zhbj.ui;

import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.zt.zhbj.R;
import com.zt.zhbj.fragment.LeftMenuFragment;
import com.zt.zhbj.fragment.MainFragment;

/**
 * Author: ZT on 2016/12/6.
 * jar:只存放java文件
 * 库(library)：存放Java文件和资源文件，并且添加到工程后可以访问其资源文件
 * 使用SlidingMenu类库实现侧滑菜单，AS里作为一个Module引入，添加即可
 * sdm属性：http://www.cnblogs.com/SharkBin/p/3665548.html
 * sx:相同特效的fragment/activity可以抽象一个父类。在各自的类里自己维护自己的业务方法，提供外部来调用
 *
 *
 *  * 网络请求封装:单列
 * xUtils:
 *  xUtils是基于Afinal开发的目前功能比较完善的一个Android开源框架，
 *  而afinal 是一个android的 orm 和 ioc 框架。而且封装了android中的httpClient
 *  故：在Android 6.0后Google移除了HttpClient的支持。所以出现：
 *      Error:(35, 65) 错误: 无法访问HttpRequestBase
 找不到org.apache.http.client.methods.HttpRequestBase的类文件
 解决方法：在gradle的Android里添加 useLibrary 'org.apache.http.legacy' 使xUtils兼容Android6.0系统
 *  http://blog.csdn.net/u010111008/article/details/50380928
 *
 */
public class MainActivity extends SlidingFragmentActivity {
    private static final String MAIN_FRAGMENT = "MAIN_FRAGMENT";
    public static final String LEFT_FRAGMENT = "LEFT_FRAGMENT";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//无标题栏
        setContentView(R.layout.activity_main);
        initSlidMenu();
        loadLeftAndMainLayout();
    }

    private void initSlidMenu() {
        setBehindContentView(R.layout.left_menu);
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setMode(SlidingMenu.LEFT);//左/右侧滑出
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//全屏触摸监听

        //设置滑动时菜单的是否淡入淡出
        slidingMenu.setFadeEnabled(true);
        //设置淡入淡出的比例
        slidingMenu.setFadeDegree(0.5f);
        //设置滑动时拖拽效果:即slidingmenu的遮盖滑出效果
        slidingMenu.setBehindScrollScale(0);

//        slidingMenu.setBehindOffset(200);//屏幕预留宽度

        //以200/480的屏幕预留宽度比例 * 不同屏幕的宽度：适应侧滑视图的视频【200:屏幕预留宽度 480:测试手机的屏幕宽度】
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();

        int ylWidth = width*200/480; // 注：150/480*width为零

        slidingMenu.setBehindOffset(ylWidth);
        Log.v("TAG", "预留宽度：" + ylWidth);
    }
    /**
     * 加载两个fragment
     * 主界面的布局实现可用：
     * 1，RelativeLayout+Fragment+RadioButton
     * 2，ViewPager+RadioButton
     * 此处采用的是方式2
     */
    private void loadLeftAndMainLayout() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ts = fm.beginTransaction();
        ts.replace(R.id.main_FrameLayout, new MainFragment(),MAIN_FRAGMENT);
        ts.replace(R.id.left_frameLayout, new LeftMenuFragment(),LEFT_FRAGMENT);
        ts.commit();
    }
    //获取侧滑Fragment对象，供外部调用
    public LeftMenuFragment getLeftMenuFragment(){
        FragmentManager fm = getSupportFragmentManager();
        LeftMenuFragment leftMenuFragment = (LeftMenuFragment) fm.findFragmentByTag(LEFT_FRAGMENT);
        return leftMenuFragment;
    }
    //获取MainFragment对象，供外部调用
    public MainFragment getMainFragment(){
        FragmentManager fm = getSupportFragmentManager();
        MainFragment mainFragment = (MainFragment) fm.findFragmentByTag(MAIN_FRAGMENT);
        return mainFragment;
    }
}
