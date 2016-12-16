package com.zt.zhbj.base;

import android.app.Activity;
import android.view.View;

/**
 * Author: ZT on 2016/12/9.
 * 侧滑视图点击切换view视图的父类封装
 */
public abstract class BaseMenuDetailPager {
    public View mRootView;//根视图
    public BaseMenuDetailPager(Activity activity){
        mRootView = initView(activity);
    }
    //初始化布局view，子类来返回各自的布局view，必须实现
    public abstract View initView(Activity activity);
    //初始化布局view的数据
    public void initData() {}
}
