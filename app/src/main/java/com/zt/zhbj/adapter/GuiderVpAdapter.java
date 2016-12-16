package com.zt.zhbj.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zt.zhbj.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: ZT on 2016/12/7.
 */
public class GuiderVpAdapter extends PagerAdapter {
    private Context context;
    private List<ImageView> imageViewList;
    public GuiderVpAdapter(Context context,List<ImageView> list){
        this.context = context;
        this.imageViewList = list;
    }
    @Override
    public int getCount() {
        return imageViewList.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
    //【依据vp的加载缓存机制，以下两个方法会不断的回掉。若vp包含的是fragment则回掉的是onCreateView()方法,销毁onDestroyView（）】
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = imageViewList.get(position);
        container.addView(imageView);
        return imageView;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
