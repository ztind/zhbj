package com.zt.zhbj.base.impl.menu;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.zt.zhbj.base.BaseMenuDetailPager;

/**
 * Author: ZT on 2016/12/9.
 * 侧滑视图--专题
 */
public class DeatilZhuangtiPager extends BaseMenuDetailPager {
    public DeatilZhuangtiPager(Activity activity) {
        super(activity);
    }


    @Override
    public View initView(Activity activity) {
        TextView textView = new TextView(activity);
        textView.setText("Im 专题");
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(20);
        return textView;
    }

    @Override
    public void initData() {
        Log.v("TAG", "加载侧滑视图----专题");
    }
}
