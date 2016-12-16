package com.zt.zhbj.utils;

/**
 * Author: ZT on 2016/12/9.
 * 缓存类(封装了sp)
 * 缓存可存储到：sp ：url为键  json内容为值
 *              sqlite：声明两个字段 一个为键 一个为值
 *              file：(以url(MD5)作为文件名，缓存的数据作为文件内容)
 */
public class CacheUtils
{
     public static String readCache(String url){
         String conent = MySharePreUtis.getCacheDataByUrl(url);
         return conent;
     }
    public static void writeCache(String url,String values){
        MySharePreUtis.setCacheDataByUrl(url,values);
    }

}
