package com.zt.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zt.zhbj.base.impl.menu.DetailNewsVpItemPager;

/**
 * Author: ZT on 2016/12/10.
 *
 * ZdyTabViewPager为图片滑动页
 *
 */
public class ZdyTabViewPager extends ViewPager  {
    private int downX;
    private int downY;

    public ZdyTabViewPager(Context context) {
        super(context);
        init();
    }

    public ZdyTabViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init(){

    }
    //事件分发
    /**
     *  1，上下滑动父类拦截(自己+listview列表的滑动)
     * 2，向左滑动到最后一页 父类(vp)拦截
     * 3，向右滑动到第一页 父类(vp)拦截
     * 4,左右滑动,x坐标的滑动量(绝对值)大于y坐标的滑动量(绝对值)，else 上下滑动
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        getParent().requestDisallowInterceptTouchEvent(true);//请求父控件不拦截事件

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();
                int dx = moveX - downX;
                int dy = moveY - downY;
                if(Math.abs(dx)>Math.abs(dy)){ //左右滑动
                   if(dx>0){//向右滑
                       if(this.getCurrentItem()==0){
                           getParent().requestDisallowInterceptTouchEvent(false);//父控件拦截
                       }
                   }else { //左滑
                       if(this.getCurrentItem()==getAdapter().getCount()-1){
                           getParent().requestDisallowInterceptTouchEvent(false);//父控件拦截
                       }
                   }
                }else {
                    //上下滑动
                    getParent().requestDisallowInterceptTouchEvent(false);//父类拦截
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
