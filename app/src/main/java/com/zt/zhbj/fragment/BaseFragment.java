package com.zt.zhbj.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Author: ZT on 2016/12/7.
 */
public abstract class BaseFragment extends Fragment {
    protected Activity mActivity;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();//得到宿主Activity
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = initView(inflater);
        return view;
    }
    //当fragment和activity的布局全部初始化完毕，在onCreateView方法后调用,
    //注意onActivityCreated()方法在vp滑动的过程中也会不断的回调
    // 所以里面init控件的代码也要保证只执行一次，否则控制数据会不断的变化,用flage来标记回调次数
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//    }

    //在vp里onViewCreated方法只执行一次 可init控件
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }
    protected abstract View initView(LayoutInflater inflater);

    protected abstract void initData();
}
