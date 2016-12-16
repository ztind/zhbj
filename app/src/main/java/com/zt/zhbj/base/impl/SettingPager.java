package com.zt.zhbj.base.impl;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.zt.zhbj.base.BasePager;

/**
 * Author: ZT on 2016/12/7.
 */
public class SettingPager extends BasePager {
    public SettingPager(Activity activity) {
        super(activity);
    }
    //在子类来填从布局内容
    @Override
    public void initData() {
        TextView textView = new TextView(mActivity);
        textView.setText("setting");
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        frameLayout.addView(textView);
        title.setText("设置");
        imageButton.setVisibility(View.GONE);
    }
}
