package com.zt.zhbj.ui;

import android.app.Application;

import com.zt.zhbj.utils.MySharePreUtis;

import cn.sharesdk.framework.ShareSDK;

/**
 * Author: ZT on 2016/12/6.
 * 初始化配置信息
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //init sp
        MySharePreUtis.initShraePre(getApplicationContext());
        ShareSDK.initSDK(this);
    }
}
