package com.zt.zhbj.base.impl.menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;
import com.zt.zhbj.R;

import com.zt.zhbj.adapter.TabNewsListAdapter;
import com.zt.zhbj.adapter.TabNewsVpAdapter;
import com.zt.zhbj.base.BaseMenuDetailPager;
import com.zt.zhbj.config.UrlConfig;
import com.zt.zhbj.entiry.NewsJson;
import com.zt.zhbj.entiry.TabNews;
import com.zt.zhbj.ui.DetailNewsActivity;
import com.zt.zhbj.ui.MainActivity;
import com.zt.zhbj.utils.CacheUtils;
import com.zt.zhbj.utils.MySharePreUtis;
import com.zt.zhbj.view.ZdyPullToRefreshListView;
import com.zt.zhbj.view.ZdyTabViewPager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Author: ZT on 2016/12/10.
 * 侧滑新闻页里的每个item布局view的封装类。此处不采用Fragment来封装
 */
public class DetailNewsVpItemPager extends BaseMenuDetailPager {

    private NewsJson.NewsChildrenData newsChildrenData;
    private String mUrl;//获取数据URL

    @ViewInject(R.id.tab_viewpager)
    ZdyTabViewPager viewPager;
    @ViewInject(R.id.tab_listview)
    ZdyPullToRefreshListView listView;
    @ViewInject(R.id.tab_vp_title)
    TextView tab_vp_title;
    @ViewInject(R.id.circle_indicator)
    CirclePageIndicator circlePageIndicator;


//    @Bind(R.id.tab_viewpager)
//    ZdyTabViewPager viewPager;
//    @Bind(R.id.tab_listview)
//    ListView listView;
//    @Bind(R.id.tab_vp_title)
//    TextView tab_vp_title;
//    @Bind(R.id.circle_indicator)
//    CirclePageIndicator circlePageIndicator;

    private Activity mActivity;
    private TabNewsListAdapter tabNewsListAdapter;
    private String mMoreUrl;//下一页连接
    private ArrayList<TabNews.TabNewsInfo> mTabListData;//tab页数据集合


    public DetailNewsVpItemPager(Activity activity, NewsJson.NewsChildrenData newsChildrenData) {
        super(activity);
        this.newsChildrenData = newsChildrenData;
        this.mUrl = UrlConfig.SERVICE_URL + newsChildrenData.url;
    }
    @Override
    public View initView(Activity activity) {
        //加载xml布局
        View view = View.inflate(activity, R.layout.detailnewsvpitempager_layout, null);
        ViewUtils.inject(this,view);
//        ButterKnife.bind(this,view);
        this.mActivity = activity;
        //vp添加作为listview的头布局，实现跟随列表滑动功能
        final View list_header = View.inflate(mActivity, R.layout.ltab_istview_header, null);
        ViewUtils.inject(this,list_header);//注入view和事件[ButterKinfe此处绑定有问题，绑定的控件必须在此view里，此处使用ViewUtils]
//        ButterKnife.bind(this,list_header);
        listView.addHeaderView(list_header);
        //初始化刷新回调接口
        listView.setRefreshListener(new ZdyPullToRefreshListView.RefreshListener() {
            @Override
            public void refresh() {
                //下拉刷新时，重新请求网络加载数据(注意url别传错了)
                requestService(HttpRequest.HttpMethod.GET,mUrl);

                //或 new thread 模拟请求网络加载数据
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(3000);
//                            mActivity.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(mActivity, "刷新成功!", Toast.LENGTH_SHORT).show();
//                                    listView.completeRefresh(true);//隐藏刷新视图
//                                }
//                            });
//                        } catch (InterruptedException e) {
//                            listView.completeRefresh(false);//隐藏刷新视图
//                            e.printStackTrace();
//                        };
//                    }
//                }).start();
            }

            @Override
            public void loadData() {
                Log.v("TAG","上拉加载数据啦..." );
                //加载更多数据
                if(mMoreUrl!=null){
                    //有下一页数据
                    loadMoreDataFormService(HttpRequest.HttpMethod.GET,mMoreUrl);
                }else{
                    //没有下一页数据
                    Toast.makeText(mActivity, "没有更多数据啦", Toast.LENGTH_SHORT).show();
                    listView.completeLoad();//隐藏加载更多 脚布局
                }
                //模拟加载更多数据
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(3000);
//                            mActivity.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    listView.completeLoad();
//                                }
//                            });
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
            }
        });
        return view;
    }

    @Override
    public void initData() { //因为做了缓存策略故反复调用此方法 也不消耗用户流量
        Log.v("TAG", "加载tab数据:"+newsChildrenData.title);
        /**
         * 缓存策略
         */
        String cahaeJsonData = CacheUtils.readCache(mUrl);
        if(!TextUtils.isEmpty(cahaeJsonData)){
            //读缓存
            Log.v("TAG", "【读缓存】");
            gsonJiexJson(cahaeJsonData,false);
        }else {
            //加载网络数据
            requestService(HttpRequest.HttpMethod.GET,mUrl);
        }
    }
    /**
     * 请求服务器获取more更多数据【上拉加载更多的数据】【注:上拉加载更多数据不做缓存】
     */
    private void loadMoreDataFormService(HttpRequest.HttpMethod httpMethod,final String mMoreUrl){
        //加载更多数据
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(httpMethod,mMoreUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String resultJson = responseInfo.result;
                if(resultJson!=null){
                    Log.v("TAG", "【从服务器获取more更多数据】："+newsChildrenData.title);
                    gsonJiexJson(resultJson,true);//加载更多模式
                    listView.completeLoad();//隐藏加载更多 脚布局
                }
            }
            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();//使用异常对象e打印异常信息
                Log.v("TAG","从服务器获取more更多数据失败,2言因："+s);
                listView.completeLoad();//隐藏加载更多 脚布局
            }
        });
    }
    /**
     * 请求service返回Json数据
     * @return
     */
    public  void requestService(HttpRequest.HttpMethod httpMethod, final String url){

            HttpUtils httpUtils = new HttpUtils();
            httpUtils.send(httpMethod,url, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    String resultJson = responseInfo.result;
                    if(resultJson!=null){
                        Log.v("TAG", "【从服务器获取数据】："+newsChildrenData.title);
                        //缓存sp
                        CacheUtils.writeCache(url,resultJson);
                        gsonJiexJson(resultJson,false);//非加载更多模式
                    }else{
                        Log.v("TAG", "resultJson-->null");
                    }
                    listView.completeRefresh(true);//隐藏刷新视图
                }
                @Override
                public void onFailure(HttpException e, String s) {
                    e.printStackTrace();//使用异常对象e打印异常信息
                    Log.v("TAG","请求失败,2言因："+s);
                    listView.completeRefresh(false);//隐藏刷新视图
                }
            });
    }
    private TabNewsVpAdapter tabNewsVpAdapter;
    private void gsonJiexJson(String resultJson,boolean isMore) {
        if(resultJson!=null){
            Gson gson = new Gson();
            TabNews tabNews = gson.fromJson(resultJson, TabNews.class);//解析json数据
            String moreUrl = tabNews.data.more;
            if(!TextUtils.isEmpty(moreUrl)){
                mMoreUrl = UrlConfig.SERVICE_URL + moreUrl;
            }else{
                mMoreUrl = null;
            }

            if(!isMore){
                //填充UI数据
                showDataToUI(tabNews);
            }else{
                //加载更多的数据直接追加到listview的尾部
                ArrayList<TabNews.TabNewsInfo> tabNewsInfosList = tabNews.data.news;
                mTabListData.addAll(tabNewsInfosList);//集合里添加集合
                tabNewsListAdapter.notifyDataSetChanged();//刷新列表
            }
        }
    }
    private int vpItemCount;
    private void showDataToUI(final TabNews tabNews){
        vpItemCount = tabNews.data.topnews.size();
        tabNewsVpAdapter = new TabNewsVpAdapter(mActivity, tabNews.data.topnews);
        viewPager.setAdapter(tabNewsVpAdapter);
        circlePageIndicator.setViewPager(viewPager);
        circlePageIndicator.setSnap(true);//设置快照 true小点不滑动，false为切换时小点跟随滑动
        //给指示器设置滑动监听器
        circlePageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                tab_vp_title.setText(tabNews.data.topnews.get(position).title);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //更新第一个头条新闻标题
        tab_vp_title.setText(tabNews.data.topnews.get(0).title);
        //默认选中第一页【解决：页面销毁后重新初始化时，indicator任然保留上次圆点位置的bug】
        circlePageIndicator.onPageSelected(0);
        //加载listView列表数据
        laodLaitViewUI(tabNews);

        //保证初始化时自加载一次(除非本类被销毁了)防止该vp里的item销毁时下次加载时/下拉刷新时UI又发送一次消息，从而造成vp的item的滑动不断加快的bug
        if(mHandler==null){
            mHandler=new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if(msg.what==100){
                        int currentVpPosiotion = viewPager.getCurrentItem();
                        if(currentVpPosiotion==vpItemCount-1){
                            currentVpPosiotion = -1;
                        }
                        viewPager.setCurrentItem(++currentVpPosiotion);
                        mHandler.sendEmptyMessageDelayed(100, 3000);//3s自动轮播
                    }
                }
            };
            mHandler.sendEmptyMessageDelayed(100, 3000);//3s自动轮播

            //设置用户按住vp里的item时图片不切换，抬起才切换【按住时移除所有的消息】vp的触摸监听
            viewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            mHandler.removeCallbacksAndMessages(null);//移除所有消息
                            Log.v("TAG", "-down-");
                            startX = (int) event.getX();
                            startY = (int) event.getY();
                            break;
                        case MotionEvent.ACTION_UP:
                            mHandler.sendEmptyMessageDelayed(100, 3000);//3s自动轮播
                            Log.v("TAG", "-up-");
                            //用户滑动时，不应该Tost,故计算用户的手指滑动是否不为水平滑动[若将item的点击事件写在适配器的instance方法里则，viewPagersetOnTouchListener()里的这3个方法就不会回调啦]
                            endX = (int) event.getX();
                            endY = (int) event.getY();
                            int dx = endX - startX;
                            int dy = endY - startY;
                            //若dx>dx 水平方向滑动 else 竖直.
                            int jdzDx = Math.abs(dx);//手指水平滑动距离的绝对值
                            if(jdzDx==0 || jdzDx<2 ){ //滑动量可自动设置此处设置为2 [上下滑动走ACTION_CANCEL事件了]
                                Toast.makeText(mActivity, tabNews.data.topnews.get(viewPager.getCurrentItem()).title, Toast.LENGTH_SHORT).show();
                            }
                            Log.v("TAG", dx + "------" + dy);
                            break;
                        case MotionEvent.ACTION_CANCEL: //但viewpager被按下后，直接滑动listview，导致ACTION_UP抬起事件无法响应，但会走此事件
                            mHandler.sendEmptyMessageDelayed(100, 3000);//3s自动轮播
                            Log.v("TAG", "-cancel-");
                            break;
                    }
                    return false;
                }
            });
        }
    }
    int startX;
    int startY;
    int endX;
    int endY;
    private Handler mHandler;

    private void laodLaitViewUI(TabNews tabNews) {
        //获取数据集合
        mTabListData = tabNews.data.news;

        if(mTabListData!=null && mTabListData.size()>0){
            tabNewsListAdapter = new TabNewsListAdapter(mActivity,mTabListData);
            listView.setAdapter(tabNewsListAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    /**
                     * parent:listview
                     * view:当前被点击的item布局view
                     * position:当前被点击的listview Item的位置
                     * id:item id
                     */
                    if(position==0 || position==1 || position==listView.getCount()-1){
                        return;//两个头视图和脚视图,点击不执行任何操作，即刻return
                    }
                    int headerCount = listView.getHeaderViewsCount();//获取listview头布局的个数
                    position = position - headerCount;//因为本listview包含了2个头布局，所以第一个item的position为2，而此处要减-2
                    TabNews.TabNewsInfo item = (TabNews.TabNewsInfo) tabNewsListAdapter.getItem(position);
                    //标记已读未读，sp存储该条新闻的id[已读字体颜色变灰]
                    int newsId = item.id;
                    if(!MySharePreUtis.getRead_ids().contains(newsId+"")){//sp里没有包含此id，才添加，防止用户多次点导致多次添加
                        MySharePreUtis.setRead_ids(newsId);
                        //更新字体颜色
                        TextView tvConrtent = (TextView) view.findViewById(R.id.news_content);
                        tvConrtent.setTextColor(Color.GRAY);
//                        tabNewsListAdapter.notifyDataSetChanged();//全局刷新，浪费性能
                    }
                    //进入新闻详情页
                    startDetailNewsAty(item.url);
                }
            });
        }
    }
    private void startDetailNewsAty(String url){
        url = url.replace("10.0.2.2", UrlConfig.HOST_IP);
        Intent intent = new Intent(mActivity, DetailNewsActivity.class);
        intent.putExtra("url", url);
        mActivity.startActivity(intent);
    }
}
