package com.zt.zhbj.utils;

import android.content.Context;

/**
 * Author: ZT on 2016/12/15.
 */
public class DensityUtils {
    public static int dip2pix(float dp, Context context){
        float density = context.getResources().getDisplayMetrics().density;//获取屏幕密度
        int px = (int) (dp * density+0.5f);//4.9->4 4.1->4   +0.5f是四舍五入
        return px;
    }
    public static float pix2dip(float px,Context context){
        float density = context.getResources().getDisplayMetrics().density;
        float dp = px / density;
        return dp;
    }
}
