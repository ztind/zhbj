package com.zt.zhbj.base.impl;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.zt.zhbj.base.BasePager;

/**
 * Author: ZT on 2016/12/7.
 */
public class GovPager extends BasePager {
    public GovPager(Activity activity) {
        super(activity);
    }
    //在子类来填从布局内容
    @Override
    public void initData() {
        TextView textView = new TextView(mActivity);
        textView.setText("gov");
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        frameLayout.addView(textView);
        title.setText("政务");
        imageButton.setVisibility(View.VISIBLE);
    }
}
