package com.zt.zhbj.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zt.zhbj.R;
import com.zt.zhbj.config.UrlConfig;
import com.zt.zhbj.entiry.ZutuBaen;
import com.zt.zhbj.cache.MyBitmapUtils;

import java.util.ArrayList;

/**
 * Author: ZT on 2016/12/14.
 */
public class ZutuListGridAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<ZutuBaen.ZutuNewsData> list;

    //    private BitmapUtils bitmapUtils;
    private MyBitmapUtils myBitmapUtils;
    public ZutuListGridAdapter(Context context, ArrayList<ZutuBaen.ZutuNewsData> list){
        this.context = context;
        this.list = list;
        myBitmapUtils = new MyBitmapUtils();
//        bitmapUtils = new BitmapUtils(context);
//        bitmapUtils.configDefaultLoadingImage(R.mipmap.news_pic_default);//默认显示图片
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ZutuBaen.ZutuNewsData getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if(convertView==null){
            vh = new ViewHolder();
            convertView = View.inflate(context, R.layout.zutu_list_item, null);
            vh.imageView = (ImageView) convertView.findViewById(R.id.zutu_image);
            vh.textView = (TextView) convertView.findViewById(R.id.zutu_text);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }
        ZutuBaen.ZutuNewsData item = list.get(position);
        String imageUrl = item.listimage;
        imageUrl = imageUrl.replace("10.0.2.2", UrlConfig.HOST_IP);
//        bitmapUtils.display(vh.imageView,imageUrl);

        myBitmapUtils.setDefaultBackground(vh.imageView,R.mipmap.news_pic_default);
        myBitmapUtils.display(vh.imageView,imageUrl);

        vh.textView.setText(item.title);
        return convertView;
    }
    class ViewHolder{
        private ImageView imageView;
        private TextView textView;
    }
}
