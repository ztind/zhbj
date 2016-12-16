package com.zt.zhbj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zt.zhbj.R;
import com.zt.zhbj.fragment.BaseFragment;

import java.util.ArrayList;

/**
 * Author: ZT on 2016/12/9.
 */
public class LeftSlidMenuListAdapter extends BaseAdapter {
    private ArrayList<String> titleList;
    private LayoutInflater layoutInflater;
    public int currentPosition;
    public LeftSlidMenuListAdapter(Context context,ArrayList<String> titleList,int currentPosition){
        this.titleList = titleList;
        this.currentPosition = currentPosition;
        this.layoutInflater = layoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return titleList.size();
    }

    @Override
    public Object getItem(int position) {
        return titleList.get(position);
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
            convertView = layoutInflater.inflate(R.layout.left_menu_list_item, null);
            vh.title = (TextView) convertView.findViewById(R.id.slidmenu_title);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.title.setText(titleList.get(position));
        if(position==currentPosition){
            vh.title.setEnabled(true);
        }else{
            vh.title.setEnabled(false);
        }
        return convertView;
    }
    class ViewHolder{
        private TextView title;
    }
}
