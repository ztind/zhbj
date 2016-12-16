package com.zt.zhbj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zt.zhbj.R;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Author: ZT on 2016/12/11.
 * 自定义下拉刷新listview(onTouch)
 * 上拉加载更多()
 */
public class ZdyPullToRefreshListView extends ListView implements AbsListView.OnScrollListener{
    private int flage = -1; //标记第一次触摸viewpager[用户滑动vp区域的位置时，滑动事件被vp拦截，故在本类的onTouch方法里获取不到其坐标]
    private int downY;
    private int moveY;
    private int headerHeight;
    private static final int STATE_PULL_TO_REFRESH = 1;
    private static final int STATE_RELEASE_REFRESH = 2;
    private static final int STATE_REFRESHING = 3;
    private int curretnState = STATE_PULL_TO_REFRESH;//默认下拉刷新
    private View headerView;
    private static final int MAX_LA_JULI=40;//下拉最大距离/列表的上padding距离
    @Bind(R.id.refresh_jt)
    ImageView jtImage;
    @Bind(R.id.refresh_progressbar)
    ProgressBar progressBar;
    @Bind(R.id.refresh_text)
    TextView refreshText;
    @Bind(R.id.refresh_time)
    TextView refreshTime;
    private RotateAnimation roteUp;
    private RotateAnimation roteDown;
    private View footerView;
    private int footerViewHeight;

    //new 对象是执行
    public ZdyPullToRefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }

    //加载无style属性的该xml布局时
    public ZdyPullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }

    //加载含有style属性的该xml布局时
    public ZdyPullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }

    //初始化刷新的头布局
    private void initHeaderView() {
        headerView = View.inflate(getContext(), R.layout.refresh_list_header, null);
        //设置paddingTop默认隐藏刷新头视图
        headerView.measure(0, 0);
        headerHeight = headerView.getMeasuredHeight();
        headerView.setPadding(0, -headerHeight, 0, 0);
        this.addHeaderView(headerView);
        ButterKnife.bind(this, headerView);
        setUpdataTextTime();
        //init 动画
        imageAnimation();
        //初始化OnScrollListener监听接口
        this.setOnScrollListener(this);
    }
    //初始化listview角布局
    private void initFooterView(){
        footerView = View.inflate(getContext(), R.layout.listview_footer, null);
        footerView.measure(0,0);
        footerViewHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0,-footerViewHeight,0,0);
        this.addFooterView(footerView);
    }
    //箭头的旋转动画
    private void imageAnimation(){
        roteUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        roteUp.setDuration(300);
        roteUp.setFillAfter(true);//停留在最后一个幁动画

        roteDown = new RotateAnimation(-180,0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        roteDown.setDuration(300);
        roteDown.setFillAfter(true);//停留在最后一个幁动画
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (flage == -1) {
                    downY = (int) ev.getY();//重新获取x坐标
                    flage = 0; //改变标记，防止不断调用
                }

                moveY = (int) ev.getY();
                //滑动偏移量
                int dy = moveY - downY;

                //正在刷新状态时listview是全部显示，此时(下滑)可以设置listview的paddingTop值来控制刷新刷新状态时的最大滑动量，真强用户体验。当然也可以不用设置
                if (curretnState == STATE_REFRESHING) {
                    //下滑状态且为显示头视图时
                    if (moveY > downY && getFirstVisiblePosition() == 0) {
                        if (dy <= MAX_LA_JULI) {
                            this.setPadding(0, dy, 0, 0);
                        } else {
                            this.setPadding(0, 0, 0, 0);
                        }
                        return true;//消费此事件
                    }
                    //上滑/上滑没有显示头视图，交友父类去完成(正常状态)super.onTouchEvent(ev)
                    return super.onTouchEvent(ev);
                }

                //头视图的上paddig值
                int paddingTop = dy - headerHeight;
                //有偏移量且当前listview显示在最顶部，时才进行头视图的显示
                if (dy > 0 && getFirstVisiblePosition() == 0) {

                    //设置paddingtop的下拉最大距离，当然可以不用设置
                    if (paddingTop < MAX_LA_JULI) {
                        headerView.setPadding(0, paddingTop, 0, 0);
                    }

                    if (paddingTop >= 0 && curretnState != STATE_RELEASE_REFRESH) { //释放刷新
                        curretnState = STATE_RELEASE_REFRESH;
                        Log.v("TAG", "释放刷新");
                        updateHeadeView();
                    } else if (paddingTop < 0 && curretnState != STATE_PULL_TO_REFRESH) {  //下拉刷新【执行不到】
                        curretnState = STATE_PULL_TO_REFRESH;
                        Log.v("TAG", "下拉刷新");
                        updateHeadeView();
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                flage = -1;//手指抬起后还原标记
                //手指抬起
                if (curretnState == STATE_RELEASE_REFRESH) {
                    headerView.setPadding(0, 0, 0, 0);
                    curretnState = STATE_REFRESHING;//正在刷新状态
                    updateHeadeView();
                }else if(curretnState==STATE_PULL_TO_REFRESH){
                    headerView.setPadding(0,-headerHeight, 0, 0);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }
    //更新头视图
    private void updateHeadeView() {
        switch (curretnState) {
            case STATE_PULL_TO_REFRESH://下拉刷新
                jtImage.startAnimation(roteDown);
                jtImage.setVisibility(VISIBLE);
                progressBar.setVisibility(INVISIBLE);
                refreshText.setText("下拉刷新");
                break;
            case STATE_RELEASE_REFRESH://释放刷新
                jtImage.startAnimation(roteUp);
                jtImage.setVisibility(VISIBLE);
                progressBar.setVisibility(INVISIBLE);
                refreshText.setText("释放刷新");
                break;
            case STATE_REFRESHING://正在刷新
                jtImage.clearAnimation();//清除动画才能隐藏
                jtImage.setVisibility(INVISIBLE);
                progressBar.setVisibility(VISIBLE);
                refreshText.setText("正在刷新...");
                //通知listview刷新加载数据
                if(refreshListener!=null){
                    refreshListener.refresh();
                }
                break;
        }
    }

    //刷新监听接口
    public interface RefreshListener{
        void refresh();

        void loadData();
    }
    private RefreshListener refreshListener;
    public void setRefreshListener(RefreshListener refreshListener){
        this.refreshListener = refreshListener;
    }
    //刷新完成隐藏刷新视图
    public void completeRefresh(boolean success){
        headerView.setPadding(0,-headerHeight,0,0);
        curretnState=STATE_PULL_TO_REFRESH;
        refreshText.setText("下拉刷新");
        jtImage.setVisibility(VISIBLE);
        progressBar.setVisibility(INVISIBLE);
        if(success){
            setUpdataTextTime();//更新文本刷新时间
        }
    }
    //获取当前系统时间
    private void setUpdataTextTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        refreshTime.setText("最后刷新时间："+sdf.format(new Date()));
    }

    /**
     * 上拉加载更多的滑动回调方法
     */
    private boolean isLoadModel;
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(isLoadModel){  //数据在加载就无需再加载啦
            return;
        }
        if(scrollState==SCROLL_STATE_IDLE){//界面空闲
            int lastVisiblePosition = getLastVisiblePosition();
            int lastPosition = getCount()-1;
            //判断当前列表显示的item是否是列表最后一个item,且为非加载模式(防止不断调用)
            if(lastVisiblePosition>=lastPosition && !isLoadModel){
                footerView.setPadding(0,0,0,0);
                setSelection(getCount());//将listview显示在最后一个item上，从而加载更多会直接显示出来无需手动滑出

                if(refreshListener!=null){
                    isLoadModel = true;
                    //通知UI，加载数据
                    refreshListener.loadData();
                }
            }
        }
    }
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
    /**
     * 上拉加载完成
     */
    public void completeLoad(){
        footerView.setPadding(0,-footerViewHeight,0,0);
        isLoadModel = false;
    }
}
