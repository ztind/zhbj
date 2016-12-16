package com.zt.zhbj.config;
/**
 * Author: ZT on 2016/12/8.
 */
public class UrlConfig {
    //注意访问服务器获取数据时手机要连接网络，否则获取不到本地电脑Tomcat里的数据，ip为 无线局域网适配器 本地连接* 3:
    public static String HOST_IP="192.168.191.1";
    public static final String SERVICE_URL = "http://"+HOST_IP+":8080/zhbj";
    public static final String NEWS_URL = SERVICE_URL+"/categories.json";
    public static final String ZUTU_URL=SERVICE_URL+"/photos/photos_1.json";
}
