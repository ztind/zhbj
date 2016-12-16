package com.zt.zhbj.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.zt.zhbj.R;
import com.zt.zhbj.adapter.LeftSlidMenuListAdapter;
import com.zt.zhbj.base.impl.NewsPager;
import com.zt.zhbj.entiry.NewsJson;
import com.zt.zhbj.ui.MainActivity;

import java.util.ArrayList;

/**
 * Author: ZT on 2016/12/7.
 */
public class LeftMenuFragment extends BaseFragment {
    private ListView listView;
    private View view;
    private LeftSlidMenuListAdapter adapter;
    @Override
    protected View initView(LayoutInflater inflater) {
         view = inflater.inflate(R.layout.fragment_left, null);
        return view;
    }
    @Override
    protected void initData() {
        listView = (ListView) view.findViewById(R.id.left_lsitview);
    }
    private ArrayList<String> titleList=new ArrayList<>();
    //加载布局数据
    public void loadMentLayoutTextData(ArrayList<NewsJson.NewsDate> data){
        for (int i=0;i<data.size();i++){
            String title = data.get(i).title;
            titleList.add(title);
        }
        if(titleList!=null && titleList.size()>0){
            adapter = new LeftSlidMenuListAdapter(mActivity, titleList,0);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    adapter.currentPosition = position;
                    adapter.notifyDataSetChanged();
                    //关闭侧滑视图
                    closeSlidmenu(position);
                }
            });
        }
    }

    private void closeSlidmenu(int position) {
        MainActivity aty = (MainActivity) mActivity;
        SlidingMenu sdm = aty.getSlidingMenu();
        sdm.toggle();
        //根据不同的positon在新闻中心页[Newspager]切换不同的视图
        MainFragment mainFragment = ((MainActivity) mActivity).getMainFragment();
        NewsPager newsPager = mainFragment.getNewsPager();
        newsPager.changeNewsContentView(position);

    }


}
