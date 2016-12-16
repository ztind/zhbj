package com.zt.zhbj.base.impl;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import com.lidroid.xutils.http.client.HttpRequest;
import com.zt.zhbj.base.BasePager;
import com.zt.zhbj.config.UrlConfig;

/**
 * Author: ZT on 2016/12/7.
 */
public class HomePager extends BasePager {
    public HomePager(Activity activity) {
        super(activity);
    }
    //在子类来填从布局内容
    @Override
    public void initData() {
        TextView textView = new TextView(mActivity);
        textView.setText("智慧北京");
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        frameLayout.addView(textView);
        title.setText("首页");
        imageButton.setVisibility(View.GONE);

    }
}
