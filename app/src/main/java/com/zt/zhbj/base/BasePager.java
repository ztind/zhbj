package com.zt.zhbj.base;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.zt.zhbj.R;
import com.zt.zhbj.ui.MainActivity;

/**
 * Author: ZT on 2016/12/7.
 * 头部toolbar+content内容布局
 */
public class BasePager {
    protected Activity mActivity;
    protected ImageButton imageButton;
    protected TextView title;
    protected FrameLayout frameLayout;
    public View mRootView;
    public ImageView changeImage;//网格/列表图标
    public BasePager(Activity activity){
        this.mActivity = activity;
        this.mRootView = initView();
    }
    //init View布局
    public View initView(){
        View view = View.inflate(mActivity, R.layout.content_layout, null);
        imageButton = (ImageButton) view.findViewById(R.id.title_image);
        title = (TextView) view.findViewById(R.id.title_title);
        changeImage = (ImageView) view.findViewById(R.id.change_image);
        frameLayout = (FrameLayout) view.findViewById(R.id.content_frameLayout);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity aty = (MainActivity) mActivity;
                SlidingMenu sdm = aty.getSlidingMenu();
                sdm.toggle();
            }
        });
        return view;
    }
    //init 数据
    public void initData(){}
}
