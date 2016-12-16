package com.zt.zhbj.cache;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

/**
 * Author: ZT on 2016/12/14.
 * 使用三级缓存实现图片加载缓存框架
 * 内存缓存：速度快，不浪费流量，优先加载
 * 本地缓存(Ssdcard)：速度快，不浪费流量，其次
 * 网络缓存：速度慢，浪费流量，最后
 */
public class MyBitmapUtils {
    private NetCacheUtils netCacheUtils;
    private MemoryCacheUtils memoryCacheUtils;
    private LocalCacheUtils localCacheUtils;
    //在构造方法里初始化网络缓存类对象和本地缓存类对象
    public MyBitmapUtils(){
        memoryCacheUtils = new MemoryCacheUtils();
        localCacheUtils = new LocalCacheUtils();
        netCacheUtils = new NetCacheUtils(localCacheUtils,memoryCacheUtils);
    }

    private String url;
    public void display(ImageView imageView,String url){
        this.url = url;
        //优先内存缓存，速度快，不浪费流量
        Bitmap bitmap = memoryCacheUtils.getMemoryCache(url);
        if(bitmap!=null){
            Log.v("TAG", "内存加载图片");
            imageView.setImageBitmap(bitmap);
            return;
        }
        //其次本地缓存，速度快，不浪费流量
        bitmap = localCacheUtils.getCacheBitmap(url);
        if(bitmap!=null){
            Log.v("TAG", "本地加载图片");
            imageView.setImageBitmap(bitmap);
            //写入内存缓存
            memoryCacheUtils.setMemroyCache(url,bitmap);
            return;
        }
        //最后网络缓存，速度慢，浪费流量
        netCacheUtils.getBitmapFromService(imageView,url);
    }

    public void setDefaultBackground(ImageView imageView,int resId){
        imageView.setBackgroundResource(resId);
    }
}
