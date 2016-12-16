package com.zt.zhbj.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.zt.zhbj.R;
import com.zt.zhbj.config.UrlConfig;
import com.zt.zhbj.entiry.TabNews;

import java.util.ArrayList;

/**
 * Author: ZT on 2016/12/10.
 */
public class TabNewsVpAdapter extends PagerAdapter {
    private ArrayList<TabNews.TabTopNews> list;
    private Context context;
    private BitmapUtils bitmapUtils;
    public TabNewsVpAdapter(Context context ,ArrayList<TabNews.TabTopNews> list){
        this.list = list;
        this.context = context;
        this.bitmapUtils = new BitmapUtils(context);
        bitmapUtils.configDefaultLoadingImage(R.mipmap.news_pic_default);//设置默认加载的图片
    }
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        //BitmapUtils来加载缓存图片(以url的md5为图片文件名称，缓存到mnt/sdcard/android/data/应用包名/cache里)
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);//xy自适应
        //获取图片url
        //http://10.0.2.2:8080/zhbj/10007/1452327318UU91.jpg
        String imageUrl = list.get(position).topimage;
        imageUrl = imageUrl.replace("10.0.2.2", UrlConfig.HOST_IP);//更改主机地址
        bitmapUtils.display(imageView,imageUrl);
        container.addView(imageView);
        return imageView;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
