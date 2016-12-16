package com.zt.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Author: ZT on 2016/12/7.
 * 重写OnTouch方法，禁止vp的滑动
 */
public class CustomerViewPager extends ViewPager{
    public CustomerViewPager(Context context) {
        super(context);
    }

    public CustomerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;//不拦截此事件，事件传递给子item
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;//消费此事件。不做任何处理,从而实现对滑动事件的禁用
    }
}
