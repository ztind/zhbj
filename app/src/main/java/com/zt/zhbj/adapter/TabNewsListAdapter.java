package com.zt.zhbj.adapter;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.zt.zhbj.R;
import com.zt.zhbj.config.UrlConfig;
import com.zt.zhbj.entiry.TabNews;
import com.zt.zhbj.utils.MySharePreUtis;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Author: ZT on 2016/12/10.
 */
public class TabNewsListAdapter extends BaseAdapter {
    private ArrayList<TabNews.TabNewsInfo> tabNewsInfoArrayList;
    private LayoutInflater layoutInflater;
    private BitmapUtils bitmapUtils;

    public TabNewsListAdapter(Context context, ArrayList<TabNews.TabNewsInfo> tabNewsInfoArrayList){
        this.tabNewsInfoArrayList = tabNewsInfoArrayList;
        this.layoutInflater = LayoutInflater.from(context);
        this.bitmapUtils = new BitmapUtils(context);
    }
    @Override
    public int getCount() {
        return tabNewsInfoArrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return tabNewsInfoArrayList.get(position);
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
            convertView = layoutInflater.inflate(R.layout.tab_listview, null);
            vh.imageView = (ImageView) convertView.findViewById(R.id.news_image);
            vh.textContent = (TextView) convertView.findViewById(R.id.news_content);
            vh.textData = (TextView) convertView.findViewById(R.id.news_updata);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        TabNews.TabNewsInfo tabNewsInfo = tabNewsInfoArrayList.get(position);
        String imageUrl = tabNewsInfo.listimage;
        imageUrl = imageUrl.replace("10.0.2.2", UrlConfig.HOST_IP);
        bitmapUtils.display(vh.imageView,imageUrl);
        vh.textContent.setText(tabNewsInfo.title);
        vh.textData.setText(tabNewsInfo.pubdate);
        //根据本地sp里保存的id值来判断是否该新闻是否已读
        if(MySharePreUtis.getRead_ids().contains(tabNewsInfo.id+"")){
            vh.textContent.setTextColor(Color.GRAY);
        }else{
            vh.textContent.setTextColor(Color.BLACK);
        }
        return convertView;
    }
    class ViewHolder{
        private ImageView imageView;
        private TextView textContent, textData;
    }
}
