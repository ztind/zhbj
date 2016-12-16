package com.zt.zhbj.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.zt.zhbj.R;
import com.zt.zhbj.base.BaseMenuDetailPager;
import com.zt.zhbj.base.BasePager;
import com.zt.zhbj.base.impl.GovPager;
import com.zt.zhbj.base.impl.HomePager;
import com.zt.zhbj.base.impl.NewsPager;
import com.zt.zhbj.base.impl.ServicePager;
import com.zt.zhbj.base.impl.SettingPager;
import com.zt.zhbj.base.impl.menu.DeatilNewsPager;
import com.zt.zhbj.ui.MainActivity;
import com.zt.zhbj.view.CustomerViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: ZT on 2016/12/7.
 */
public class MainFragment extends BaseFragment {
    private CustomerViewPager viewPager;
    private View view;
    private List<BasePager> list;
    @Override
    protected View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.fragment_main, null);
        return view;
    }
    @Override
    protected void initData() {
        viewPager = (CustomerViewPager) view.findViewById(R.id.main_viewPager);
        list = new ArrayList<>();
        list.add(new HomePager(mActivity));
        list.add(new NewsPager(mActivity));
        list.add(new ServicePager(mActivity));
        list.add(new GovPager(mActivity));
        list.add(new SettingPager(mActivity));
        viewPager.setAdapter(new MyVpAdapter());
        initKongjing();
    }
    //init 控件
    private void initKongjing() {
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGropu);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioClick(checkedId);
            }
        });
        //在vp的item加载时才加载item的数据
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                //确保每个item里的数据只加载一次：节约流量和性能
                if(position>0){
                    switch (position){
                        case 1:
                            if(a==0){
                                list.get(position).initData();
                                a = 1;
                            }
                            break;
                        case 2:
                            if(b==0){
                                list.get(position).initData();
                                b = 1;
                            }
                            break;
                        case 3:
                            if(c==0){
                                list.get(position).initData();
                                c = 1;
                            }
                            break;
                        case 4:
                            if(d==0){
                                list.get(position).initData();
                                d = 1;
                            }
                            break;
                    }
                }
                //禁用首页和设置页的侧滑菜单
                if(position==0 || position==list.size()-1){
                    setSlidingMenuEnable(false);
                }else{
                    setSlidingMenuEnable(true);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //程序启动设置默认加载vp里的第一个item数据【只加载一次】
        list.get(0).initData();
        //首页禁用侧滑菜单
        setSlidingMenuEnable(false);
    }
    private void radioClick(int id) {
        int position=0;
        switch (id){
            case R.id.rb_home:
                position = 0;
                break;
            case R.id.rb_news:
                position = 1;
                break;
            case R.id.rb_service:
                position = 2;
                break;
            case R.id.rb_gov:
                position = 3;
                break;
            case R.id.rb_setting:
                position = 4;
                break;
        }
        viewPager.setCurrentItem(position,false);//切换的同时去除平滑滑动效果

        //点击新闻中心，且为新闻的北京选项 则开启侧滑，其余则关闭，解决与vp的滑动冲突
        if(position==1){
            NewsPager newsPager = (NewsPager) list.get(1);
            if(newsPager!=null){
                    if(newsPager.sdmCurrentPosition==0){ //新闻中心的新闻页在判断
                        ArrayList<BaseMenuDetailPager> list = newsPager.baseMenuDetailPagerArrayList;
                        if(list!=null){
                            DeatilNewsPager deatilNewsPager= (DeatilNewsPager) list.get(0);
                            int cp = deatilNewsPager.currentPosition;
                            MainActivity mainAty = (MainActivity) mActivity;
                            SlidingMenu sdm = mainAty.getSlidingMenu();
                            if(cp!=0){
                                sdm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                            }
                        }
                    }
                }
            }
    }
    //vp的5个item是否已经加载过的标记【若加载过就不用再加载】
    private int a,b,c,d;

    public void setSlidingMenuEnable(boolean slidingMenuEnable) {
        MainActivity mainAct = (MainActivity) mActivity;
        SlidingMenu  sdm = mainAct.getSlidingMenu();
        if(slidingMenuEnable){
            sdm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//全屏监听侧滑
        }else {
            sdm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);//禁用侧滑
        }
    }

    class MyVpAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return list.size();
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
        //vp的缓存机制以下两个方法会不断的回调
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager item = list.get(position);
            View view = item.mRootView;//获取到个item的布局View
//            item.initData();      //加载vp里的item项时就加载数据[调用子类的方法实现],不在此使用加载数据，弃用：节约流量和性能
            container.addView(view);
            return view;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
    /**
     * 获取新闻中心对象，提供外部获取此对象的方法，在各自的类里维护自己应有的业务
     */
    public NewsPager getNewsPager(){
        return (NewsPager) list.get(1);
    }
}
