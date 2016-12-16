package com.zt.zhbj.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.zt.zhbj.R;
import com.zt.zhbj.utils.MySharePreUtis;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Splash闪屏页
 */
public class SplashActivity extends Activity {
    @Bind(R.id.splash_layout) RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initAnimation();

    }
    private void initAnimation() {
        //旋转动画
        RotateAnimation rotateAnimation = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);//旋转时间
        rotateAnimation.setFillAfter(true);//旋转后停止在最后,保持动画结束状态
        //缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);
        //渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);
        //动画集合来存储3个动画
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        //开启动画
        relativeLayout.startAnimation(animationSet);

        //动画监听
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
            //动画结束回调监听方法
            @Override
            public void onAnimationEnd(Animation animation) {
                enterActivity();
            }
        });
    }
    //判断用户是否是第一次启动应用（sp来存储用户的启动标记）。是则进入引导页，否进入主界面
    private void enterActivity() {
          Intent intent;
                if(MySharePreUtis.getIsFristEnter()){
                    MySharePreUtis.setIsFristEnter(false);
                    intent = new Intent(SplashActivity.this, GuideActivity.class);
                }else{
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }
        startActivity(intent);
        finish();
    }
}
