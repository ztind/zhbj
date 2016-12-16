package com.zt.zhbj.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Author: ZT on 2016/12/15.
 * 本地缓存
 */
public class LocalCacheUtils {
    //缓存文件的sd卡路径
    private static final String CACHE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/zhbj_cache";
    //写缓存
    public void setCacheBitmap(String url, Bitmap bitmap){
        File dirFile = new File(CACHE_DIR);

        if(!dirFile.exists() || !dirFile.isDirectory()){ //文件夹路径不存在,不是一个文件路径(一个文件)
            dirFile.mkdirs();//创建文件夹
        }

        //以url的MD5值作为存储的文件名
        try {
            String fileName = MD5Encoder.encode(url);
            File file = new File(CACHE_DIR, fileName);//创建图片文件

            //compress压缩，压紧。参数：[图片格式，压缩比例0-100，输出流]
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));//将bitmap压入文件

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //读缓存
    public Bitmap getCacheBitmap(String url){
        try {
            String fileName = MD5Encoder.encode(url);
            File cacheFile = new File(CACHE_DIR,fileName);
            if(cacheFile.exists()){
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(cacheFile));//将图片文件以流的方式读入到程序里
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
