package com.zt.zhbj.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Author: ZT on 2016/12/14.
 * 网络缓存
 */
public class NetCacheUtils {

    private ImageView imageView;
    private String imageUrl;
    private LocalCacheUtils localCacheUtils;
    private MemoryCacheUtils memoryCacheUtils;
    public NetCacheUtils(LocalCacheUtils localCacheUtils, MemoryCacheUtils memoryCacheUtils) {
        this.localCacheUtils = localCacheUtils;
        this.memoryCacheUtils = memoryCacheUtils;
    }

    public void getBitmapFromService(ImageView imageView, String url){
            //开启任务
        new MyAsyncTask().execute(imageView,url);//两参数都会传入到doInBackground方法里
    }
    /**
     * 使用AsyncTask来实现异步请求网络：AsyncTask(线程次+Handler的二次封装)
     * 三个泛型：
     *  第一个：doInBackground方法的可变参数类型
     *  第二个：onProgressUpdate方法的可变参数类型
     *  第三个：doInBackground的返回值类型和onPostExecute参数类型
     */
    class MyAsyncTask extends AsyncTask<Object,Integer,Bitmap>{
        //准备运行与UI线程
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.v("TAG", "--onPreExecute--");
        }
        //运行与子线程
        @Override
        protected Bitmap doInBackground(Object... params) {//params为Object[]数组
            Log.v("TAG", "--doInBackground--");
            imageView = (ImageView) params[0];
            imageUrl = (String) params[1];

            //下载图片
            Bitmap bitmap=download(imageUrl);

//            publishProgress(values);调用此方法刷新进度(调用此方法就会回掉onProgressUpdate方法)

            return bitmap;
        }
        //下载进度回调,运行与UI线程
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.v("TAG", "--values--"+values);
        }
        //doInBackground执行完成后执行此回调方法，运行与UI线程
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.v("TAG", "--onPostExecute--");
            //显示图片
            /**
             * 由于listview的缓存机制，导致一个item可能公用一个bitmap,故设置一个tag来解决此bug
             * (myBitmapUtils.display(vh.imageView,imageUrl)方法是在适配器的getView()方法里不断调用的)
             */
            if(bitmap!=null){
                String url = (String) imageView.getTag();

                if(url.equals(imageUrl)){
                    imageView.setImageBitmap(bitmap);
                    Log.v("TAG", "从网络加载图片啦");
                    //写入本地缓存
                    localCacheUtils.setCacheBitmap(url, bitmap);
                    //写入内存缓存
                    memoryCacheUtils.setMemroyCache(url,bitmap);
                }
            }
            super.onPostExecute(bitmap);
        }
    }

    /**
     *HttpURLConnection:使用Android原生的网络请求api来实现网络访问
     */
    private Bitmap download(String url) {

        imageView.setTag(url);//设置标记

        HttpURLConnection conn=null;
        InputStream is=null;
        BufferedInputStream bis = null;

        try {
            URL uuu = new URL(url);
            conn = (HttpURLConnection) uuu.openConnection();

            conn.setRequestMethod("GET");//请求方式
            conn.setConnectTimeout(5000);//连接超时
            conn.setReadTimeout(5000);   //读取超时

            conn.connect(); //链接

            int code = conn.getResponseCode();//返回码

            if(code== HttpURLConnection.HTTP_OK){ //200成功

                is = conn.getInputStream(); //获取数据(在流里)
                bis = new BufferedInputStream(is);//缓冲
                Bitmap bitmap = BitmapFactory.decodeStream(bis);

                return bitmap;

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //最后关流[注：输出流才有刷新]
            if (bis!=null){
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //关闭链接
            if(conn!=null){
                conn.disconnect();
            }
        }
        return null;
    }
}
