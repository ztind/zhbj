package com.zt.zhbj.cache;


import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;


/**
 * Author: ZT on 2016/12/15.
 * 内存缓存
 * 内存里加载里加载图片时要考虑内存泄露问题，内存泄露多了就可能oom
 * app一运行将一个类，成员加载进入内存，app退出类销毁
 * 1,内存溢出:
     不管android设备总内存是多大, 都只给每个app分配一定内存大小, 16M, 一旦超出16M就内存溢出了
 2,
 引用:
 - 默认强引用, 垃圾回收器不会回收
 - 软引用, 垃圾回收器会考虑回收 SoftReference
 - 弱引用, 垃圾回收器更会考虑回收 WeakReference
 - 虚引用, 垃圾回收器最优先回收 PhantomReference

 3:“因为从 Android 2.3 (API Level 9)开始，垃圾回收器会更倾向于回收持有软引用或弱引用的对象，这让软引用和弱引用变得不再可靠。”
    故：Google建议使用LruCache来实现内存的回收管理

 */
public class MemoryCacheUtils {
//1    private HashMap<String, Bitmap> myHashMap = new HashMap<>();

//2    private HashMap<String, SoftReference<Bitmap>> hashMap = new HashMap<>();

    //3，使用最近最少使用算法来优化图片的缓存和java的垃圾回收
    private LruCache<String, Bitmap> lruCache;//API>=12,故使用导入v4包来兼容 compile 'com.android.support:support-v4:25.0.1'

    public MemoryCacheUtils() {
        long maxMemorySize = Runtime.getRuntime().maxMemory();//获取app所分配到的最大内存 单位:字节B 1B = 8bit(一字节=8位) 1024B=1KB
        //使用最大可用内存值的1/8作为缓存的大小。
        lruCache = new LruCache<String, Bitmap>((int) (maxMemorySize / 8)) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //重写此方法来衡量每张图片的大小，默认返回图片数量。
//                int bitmapSize = value.getByteCount();
                int bitmapSize = value.getRowBytes() * value.getHeight();//计算图片大小
                return bitmapSize;
            }
        };
        Log.v("TAG", "最大内存大小："+maxMemorySize);
    }

    public void setMemroyCache(String url,Bitmap bitmap){
//        myHashMap.put(url, bitmap);

//        SoftReference<Bitmap> softReference = new SoftReference(bitmap);//使用软引用将bitmap包装起来
//        hashMap.put(url, softReference);

        lruCache.put(url, bitmap);
    }
    public Bitmap getMemoryCache(String url){
//        Bitmap bitmap = myHashMap.get(url);
//        if(bitmap!=null){
//            return bitmap;
//        }

//        SoftReference<Bitmap> soft = hashMap.get(url);
//        if(soft!=null){
//            Bitmap bitmap = soft.get();
//            return bitmap;
//        }
        Bitmap bitmap = lruCache.get(url);
        return bitmap;
    }
}
