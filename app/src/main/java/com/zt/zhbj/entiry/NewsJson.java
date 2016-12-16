package com.zt.zhbj.entiry;

import java.util.ArrayList;

/**
 * Author: ZT on 2016/12/8.
 * Gson解析Json对象的实体类
 * 使用Gson来解析json要求封装的实体类对象的属性要和json里的一模一样
 */
public class NewsJson {
    public ArrayList<NewsDate> data;
    public ArrayList<Integer> extend;
    public int retcode;

    public class NewsDate{
        public ArrayList<NewsChildrenData> children;
        public int id;
        public String title;
        public int type;
        public String url;

        @Override
        public String toString() {
            return "NewsDate{" +
                    "title='" + title + '\'' +
                    ", children=" + children +
                    '}';
        }
    }
    public class NewsChildrenData{
        public int id;
        public String title;
        public int type;
        public String url;

        @Override
        public String toString() {
            return "NewsChildrenData{" +
                    "title='" + title + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "NewsJson{" +
                "data=" + data +
                ", extend=" + extend +
                ", retcode=" + retcode +
                '}';
    }
}
