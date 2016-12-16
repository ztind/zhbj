package com.zt.zhbj.base.impl.menu;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;
import com.viewpagerindicator.TitlePageIndicator;
import com.zt.zhbj.R;
import com.zt.zhbj.base.BaseMenuDetailPager;
import com.zt.zhbj.entiry.NewsJson;
import com.zt.zhbj.ui.MainActivity;
import java.util.ArrayList;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author: ZT on 2016/12/9.
 * 侧滑视图--新闻
 * 布局：ViewPagerIndicator[JakeWharton大神开源之作]+ViewPager(里面的item用同一个类来封装布局view)
 * question:重写ViewpagerIndicator的 dispatchTouchEvent()方法请求sdm不要拦截滑动事件,其他页面进行切换时还需做标记判断设置
 * 而：vp与sdm的滑动冲突可以在vp的滑动回调方法里打开或关闭sdm即可
 *
 */
public class DeatilNewsPager extends BaseMenuDetailPager {
    @Bind(R.id.detailnewsvpitemtpager_viewpager)
    ViewPager viewPager;
    @Bind(R.id.tabPagerIndicator)
    TabPageIndicator tabPageIndicator;
    @Bind(R.id.next_pager_image)
    ImageView imageButton_nextPager;

    public int currentPosition;
    private Activity activity;
    private ArrayList<NewsJson.NewsChildrenData> childrenDatas;
    private ArrayList<DetailNewsVpItemPager> detailNewsVpItemPagerArrayList;

    public DeatilNewsPager(Activity activity, ArrayList<NewsJson.NewsChildrenData> children) {
        super(activity);
        this.childrenDatas = children;
    }

    @Override
    public View initView(Activity activity) {
        this.activity = activity;
        View view = View.inflate(activity, R.layout.layout_detailnewsvpitemtpager, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        Log.v("TAG", "加载侧滑视图----新闻");
        //根据服务器端获取的item来加载页面数
        detailNewsVpItemPagerArrayList = new ArrayList<>();
        DetailNewsVpItemPager detailNewsVpItemPager;
        //根据服务器端返回的数据结果，加载tab页数
        for (int i = 0; i < childrenDatas.size(); i++) {
            detailNewsVpItemPager = new DetailNewsVpItemPager(activity, childrenDatas.get(i));
            detailNewsVpItemPagerArrayList.add(detailNewsVpItemPager);
        }
        //为vp绑定数据
        viewPager.setAdapter(new VpAdapter());
        //监听vp的滑动监听
        MainActivity mainAty = (MainActivity) activity;
        final SlidingMenu sdm = mainAty.getSlidingMenu();

        //设置vp的指示器***
        tabPageIndicator.setViewPager(viewPager);
        tabPageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                //第一页开启侧滑，其他也则关闭侧滑
                if (position == 0) {
                    sdm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                } else {
                    sdm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
                }
                currentPosition = position;
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class VpAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return detailNewsVpItemPagerArrayList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //【依据vp的加载缓存机制，以下两个方法会不断的回掉。若vp包含的是fragment则回掉的是onCreateView()方法,销毁onDestroyView（）】
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            DetailNewsVpItemPager item = detailNewsVpItemPagerArrayList.get(position);
            View view = item.mRootView;
            container.addView(view);
            //加载数据***
            item.initData();
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return childrenDatas.get(position).title;
        }
    }

    @OnClick(R.id.next_pager_image)
    public void nextPagerClick(View view) {
        int currentPosition = viewPager.getCurrentItem()+1;
        if(currentPosition<detailNewsVpItemPagerArrayList.size()){
            tabPageIndicator.setCurrentItem(currentPosition);//tabPageIndicator已经自动帮我们解决了角标越界问题
        }
    }

}
