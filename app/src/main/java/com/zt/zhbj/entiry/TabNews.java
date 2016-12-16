package com.zt.zhbj.entiry;

import java.util.ArrayList;

/**
 * Author: ZT on 2016/12/10.
 * tab页信息封装类
 */
public class TabNews {
    public DataNews data;

    public class DataNews{
        public String more;
        public ArrayList<TabNewsInfo> news;
        public ArrayList<TabTopNews> topnews;

        @Override
        public String toString() {
            return "DataNews{" +
                    "more='" + more + '\'' +
                    ", news=" + news +
                    ", topnews=" + topnews +
                    '}';
        }
    }

    public class TabNewsInfo{
        public int id;
        public String listimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;

        @Override
        public String toString() {
            return "TabNewsInfo{" +
                    "type='" + type + '\'' +
                    ", id=" + id +
                    '}';
        }
    }

    public class TabTopNews {
        public int id;
        public String topimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;

        @Override
        public String toString() {
            return "TabTopNews{" +
                    "title='" + title + '\'' +
                    ", id=" + id +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TabNews{" +
                "data=" + data +
                '}';
    }
}
