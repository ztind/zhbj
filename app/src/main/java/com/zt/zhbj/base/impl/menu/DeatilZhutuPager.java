package com.zt.zhbj.base.impl.menu;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.zt.zhbj.R;
import com.zt.zhbj.adapter.ZutuListGridAdapter;
import com.zt.zhbj.base.BaseMenuDetailPager;
import com.zt.zhbj.config.UrlConfig;
import com.zt.zhbj.entiry.ZutuBaen;
import com.zt.zhbj.utils.MySharePreUtis;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Author: ZT on 2016/12/9.
 * 侧滑视图--组图
 */
public class DeatilZhutuPager extends BaseMenuDetailPager implements View.OnClickListener {
    private Activity mActivity;
    private ZutuListGridAdapter adapter;
    @Bind(R.id.zutu_listview)
    ListView listView;
    @Bind(R.id.zutu_gridview)
    GridView gridView;
    private ImageView changeImage;
    public DeatilZhutuPager(Activity activity, ImageView changeImage) {
        super(activity);
        this.mActivity = activity;
        this.changeImage = changeImage;
        changeImage.setOnClickListener(this);
    }
    @Override
    public View initView(Activity activity) {
        View view = View.inflate(activity, R.layout.layout_zhutu, null);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void initData() {
        Log.v("TAG", "加载侧滑视图----组图");
        String cacheData = MySharePreUtis.getCacheDataByUrl(UrlConfig.ZUTU_URL);
        if(!TextUtils.isEmpty(cacheData)){
            parseData(cacheData);//读缓存
        }else{
            getDataFromService();//网络加载数据
        }
    }
    private void getDataFromService() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, UrlConfig.ZUTU_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String resultJson = responseInfo.result;
                parseData(resultJson);
                //缓存数据到sp
                MySharePreUtis.setCacheDataByUrl(UrlConfig.ZUTU_URL,resultJson);
            }
            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                Log.v("TAG", s);
                Toast.makeText(mActivity, "数据获取失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //gson解析json
    private void parseData(String resultJson){
        Gson gson = new Gson();
        ZutuBaen zutuData = gson.fromJson(resultJson, ZutuBaen.class);

        if(zutuData!=null){
            ArrayList<ZutuBaen.ZutuNewsData> list = zutuData.data.news;
            adapter = new ZutuListGridAdapter(mActivity, list);
            listView.setAdapter(adapter);
            gridView.setAdapter(adapter);
        }

    }
    private boolean isShowList=true;
    @Override
    public void onClick(View v) {
        if (isShowList){
            listView.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            changeImage.setBackgroundResource(R.mipmap.icon_pic_list_type);
        }else{
            gridView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            changeImage.setBackgroundResource(R.mipmap.icon_pic_grid_type);
        }
        isShowList = !isShowList;
    }
}
