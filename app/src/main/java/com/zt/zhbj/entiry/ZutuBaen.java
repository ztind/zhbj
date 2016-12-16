package com.zt.zhbj.entiry;

import java.util.ArrayList;

/**
 * Author: ZT on 2016/12/14.
 * 组图数据对象封装类
 */
public class ZutuBaen {
    public ZutuData data;
    public class ZutuData{
        public ArrayList<ZutuNewsData> news;
    }
    public class ZutuNewsData{
        public String listimage;
        public String title;
    }
}
