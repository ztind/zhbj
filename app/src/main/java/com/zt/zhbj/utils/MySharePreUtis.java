package com.zt.zhbj.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Author: ZT on 2016/12/6.
 * sp数据信息存储类
 *  特点：
 *      1，根据特定的键存储对于的值，以xml文件的形式存储在data/data/应用包名/share_prefs目录下
 *      2，根据相同的键再次赋值，每次edit.commit();都会将上次存储的数据覆盖掉
 */
public class MySharePreUtis {
    private static SharedPreferences sp;
    private static SharedPreferences.Editor edit;
    private static boolean isFristEnter;//用户是否是第一次启动应用的标记
    private static String cacheDataByUrl;//缓存数据的的URL标记
    private static String read_ids;   //标记已读未读，sp存储该条新闻的id

    public static void initShraePre(Context context){
        sp = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
        edit = sp.edit();
    }

    public static boolean getIsFristEnter() {
        return sp.getBoolean("isfirstenter",true);
    }

    public static void setIsFristEnter(boolean isFristEnter) {
        edit.putBoolean("isfirstenter", isFristEnter);
        edit.commit();
    }

    //根据不同的URL获取缓存的json数据
    public static String getCacheDataByUrl(String cacheDataByUrl) {
        return sp.getString(cacheDataByUrl,null);//null默认无缓存
    }
    //根据不同的URL缓存不同的json数据
    public static void setCacheDataByUrl(String cacheDataByUrl,String cacheJsonValuse) {
        edit.putString(cacheDataByUrl, cacheJsonValuse);
        edit.commit();
    }
    public static String getRead_ids() {
        return sp.getString("read_ids", "");//默认为""
    }
    public static void setRead_ids(int id) {
        //此处做id的叠加以逗号分隔 1101,1102,1202,2032
        String ids = getRead_ids() + id + ",";
        edit.putString("read_ids", ids);
        edit.commit();
    }

}
