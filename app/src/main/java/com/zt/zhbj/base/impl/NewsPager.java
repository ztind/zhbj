package com.zt.zhbj.base.impl;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.SpanWatcher;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.zt.zhbj.base.BaseMenuDetailPager;
import com.zt.zhbj.base.BasePager;
import com.zt.zhbj.base.impl.menu.DeatilHudongPager;
import com.zt.zhbj.base.impl.menu.DeatilNewsPager;
import com.zt.zhbj.base.impl.menu.DeatilZhuangtiPager;
import com.zt.zhbj.base.impl.menu.DeatilZhutuPager;
import com.zt.zhbj.config.UrlConfig;
import com.zt.zhbj.entiry.NewsJson;
import com.zt.zhbj.fragment.LeftMenuFragment;
import com.zt.zhbj.ui.MainActivity;
import com.zt.zhbj.utils.CacheUtils;

import java.util.ArrayList;

/**
 * Author: ZT on 2016/12/7.
 */
public class NewsPager extends BasePager {
    public ArrayList<BaseMenuDetailPager> baseMenuDetailPagerArrayList;
    public int sdmCurrentPosition;
    public NewsPager(Activity activity) {
        super(activity);
    }
    //在子类来填从布局内容
    @Override
    public void initData() {
        title.setText("新闻中心");
        imageButton.setVisibility(View.VISIBLE);
        /**
         * 判断本地是否有缓存，有则从缓存里读取，否则从服务器端获取数据
         * 此处采用sp来缓存数据
         */
        if(!TextUtils.isEmpty(CacheUtils.readCache(UrlConfig.NEWS_URL))){ //读缓存
            String cacheJson = CacheUtils.readCache(UrlConfig.NEWS_URL);
            gsonJiexJson(cacheJson);
            Log.v("TAG", "【读缓存】");
        }else{      // 网络请求获取数据
            requestService(HttpRequest.HttpMethod.GET,UrlConfig.NEWS_URL);
        }

    }
    /**
     * 请求service返回Json数据
     * @return
     */
    public  void requestService(HttpRequest.HttpMethod httpMethod,String url){
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(httpMethod,url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String resultJson = responseInfo.result;
                if(resultJson!=null){
                    //缓存sp
                    CacheUtils.writeCache(UrlConfig.NEWS_URL,resultJson);
                    gsonJiexJson(resultJson);
                    Log.v("TAG", "【从服务器获取数据】");
                }else{
                    Log.v("TAG", "resultJson-->null");
                }
            }
            @Override
            public void onFailure(HttpException e, String s) {
                  e.printStackTrace();//使用异常对象e打印异常信息
                Log.v("TAG","请求失败,1言因："+s);
            }
        });
    }
    //解析json
    private void gsonJiexJson(String json){
        //Gson来解析Json数据
        Gson gson = new Gson();
        NewsJson newsJson = gson.fromJson(json, NewsJson.class);
        //填充UI
        /**
         * 在新闻中心页加载侧滑布局的内容
         */
        MainActivity mainAty = (MainActivity) mActivity;
        LeftMenuFragment leftFragment = mainAty.getLeftMenuFragment();
        leftFragment.loadMentLayoutTextData(newsJson.data);//加载侧滑视图布局里的数据

        /**
         * 加载4个侧滑菜单视图view布局
         */
        baseMenuDetailPagerArrayList = new ArrayList<>();
        baseMenuDetailPagerArrayList.add(new DeatilNewsPager(mActivity,newsJson.data.get(0).children));
        baseMenuDetailPagerArrayList.add(new DeatilZhuangtiPager(mActivity));
        baseMenuDetailPagerArrayList.add(new DeatilZhutuPager(mActivity,changeImage));
        baseMenuDetailPagerArrayList.add(new DeatilHudongPager(mActivity));

        /**
         * 默认加载 新闻页视图布局和数据加载填充
         */
        DeatilNewsPager newPager = (DeatilNewsPager) baseMenuDetailPagerArrayList.get(0);
        //添加view
        frameLayout.addView(newPager.mRootView);
        //数据填充
        newPager.initData();
    }

    /**
     * 根据用户在侧滑菜单里点击不同类别的选项切换不同的视图【点击侧滑item，会不断调用此方法，故在加载数据时应该
     * 设置加载一次/每次点击加载也可以。】
     *
     * @param position
     */
    private int A,B,C,D;
    public void changeNewsContentView(int position){
        //保证点击侧滑item加载一次view数据，其余返回填充了数据的view
        switch (position){
            case 0:
                    viewHavedData(position);
                break;
            case 1:
                if(B==0){
                    viewAndLoadData(position);
                    B = 1;
                }else{
                    viewHavedData(position);
                }
                break;
            case 2:
                if(C==0){
                    viewAndLoadData(position);
                    C = 1;
                }else {
                    viewHavedData(position);
                }
                break;
            case 3:
                if(D==0){
                    viewAndLoadData(position);
                    D = 1;
                }else {
                    viewHavedData(position);
                }
                break;
        }
        sdmCurrentPosition = position;
    }
    //加载视图和数据
    private void viewAndLoadData(int position){
        changeImage(position);
        BaseMenuDetailPager pager = baseMenuDetailPagerArrayList.get(position);
        //移除FrameLayout的view
        frameLayout.removeAllViews();
        //将view布局添加到FrameLayout里
        frameLayout.addView(pager.mRootView);
        //加载数据，填充数据到view
        pager.initData();

        //新闻中心-新闻vp滑动时：侧滑关闭判断
        sdmColseOpen(position);
    }
    //加载已经填充了数据的视图
    private void viewHavedData(int position){
        changeImage(position);
        Log.v("TAG", "返回视图：" + position);
        BaseMenuDetailPager pager = baseMenuDetailPagerArrayList.get(position);
        //移除FrameLayout的view
        frameLayout.removeAllViews();
        //将view布局添加到FrameLayout里
        frameLayout.addView(pager.mRootView);

        //新闻中心-新闻vp滑动时：侧滑关闭判断
        sdmColseOpen(position);
    }

    private void sdmColseOpen(int position){
        DeatilNewsPager dd= (DeatilNewsPager) baseMenuDetailPagerArrayList.get(0);
        MainActivity mainAty = (MainActivity) mActivity;
        SlidingMenu sdm = mainAty.getSlidingMenu();
        if(position==0){
            if(dd.currentPosition==0){
                sdm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
            }else {
                sdm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
            }
        }else{
            sdm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }
    }
    //更改图标
    private void changeImage(int position){
        BaseMenuDetailPager baseMenuDetailPager = baseMenuDetailPagerArrayList.get(position);

        if (baseMenuDetailPager instanceof DeatilZhutuPager){ //此对象是否是DeatilZhutuPager的实列
                changeImage.setVisibility(View.VISIBLE);
        }else {
                changeImage.setVisibility(View.GONE);
        }
    }

}
