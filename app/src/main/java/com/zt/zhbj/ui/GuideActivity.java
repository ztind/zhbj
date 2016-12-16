package com.zt.zhbj.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.zt.zhbj.R;
import com.zt.zhbj.adapter.GuiderVpAdapter;
import com.zt.zhbj.utils.DensityUtils;

import java.util.ArrayList;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * Author: ZT on 2016/12/7.
 * 引导页
 */
public class GuideActivity extends Activity {
    @Bind(R.id.guide_viewpager)
    ViewPager viewPager;
    @Bind(R.id.dian_linear)
    LinearLayout linearLayout;
    @Bind(R.id.but_start)
    Button button;
    @Bind(R.id.red_point)
    ImageView redPoint;
    private int pointDistance;//两点距离
    private ArrayList<ImageView> imageViewArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        initData();
        GuiderVpAdapter adapter = new GuiderVpAdapter(this,imageViewArrayList);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //页面滑动过程中回调方法
                /**
                 * position:当前页位置
                 * positionOffset：移动百分比（要用到）
                 * positionOffsetPixels：滑动的具体像素
                 * 小点移动距离=移动百分比*两点之间的距离
                 */
                //通过设置小红点的marginLeft来使其移动点
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) redPoint.getLayoutParams();
                lp.leftMargin = (int) (positionOffset * pointDistance) + position * pointDistance;
                redPoint.setLayoutParams(lp);
            }
            @Override
            public void onPageSelected(int position) {
                //滑动到某个页面回调方法
                if(position==imageViewArrayList.size()-1){
                    button.setVisibility(View.VISIBLE);
                }else {
                    button.setVisibility(View.GONE);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                //页面状态改变回调方法
            }
        });
        //获取两点之间的距离，是在UI界面绘制完成后才能获取到(onMeasure,onLayout,onDraw-->在activity的onResume方法后执行)
        // 可以通过获取视图树添加观察监听器来监听onLayout()执行完
        //获取视图树
        redPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //onLayout方法执行完成后回调
                redPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);//移除监听,避免反复回调
                // redPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);//API>=16
                pointDistance = linearLayout.getChildAt(1).getLeft() - linearLayout.getChildAt(0).getLeft();
//                Log.v("TAG","距离：" + pointDistance);31
            }
        });
    }
    private void initData() {
        imageViewArrayList = new ArrayList<>();
        int[] imageRes = {R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};
        ImageView guideImage;
        ImageView pointImage;
        LinearLayout.LayoutParams lp;
        for (int i = 0; i < imageRes.length; i++) {
            //init 引导页
            guideImage = new ImageView(this);
            guideImage.setBackgroundResource(imageRes[i]);
            imageViewArrayList.add(guideImage);
            //init 点
            pointImage = new ImageView(this);
            pointImage.setBackgroundResource(R.drawable.point_gra);
            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            if(i>0){
                lp.leftMargin = DensityUtils.dip2pix(10,this);//转化为px，解决屏幕适配
            }
            pointImage.setLayoutParams(lp);
            linearLayout.addView(pointImage);
        }
    }
    @OnClick(R.id.but_start)
    public void clickStart(View view){
        startActivity(new Intent(GuideActivity.this, MainActivity.class));
        finish();
    }
}
