package com.zt.zhbj.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zt.zhbj.R;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;


/**
 * Author: ZT on 2016/12/11.
 */
public class DetailNewsActivity extends Activity {
    @ViewInject(R.id.webview)
    WebView webView;
    @ViewInject(R.id.bar_back)
    ImageButton backImage;
    @ViewInject(R.id.bar_textSize)
    ImageButton textSize;
    @ViewInject(R.id.bar_share)
    ImageButton shareImage;
    @ViewInject(R.id.bar_linearLayout)
    LinearLayout linearLayout;
    @ViewInject(R.id.load_progressbar)
    ProgressBar progressBar;

    @ViewInject(R.id.title_title)
    TextView textView;
    @ViewInject(R.id.title_image)
    ImageButton menuImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_detailnewsactivity);
        ViewUtils.inject(this);
        menuImage.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        backImage.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
        initView();
    }

    private void initView() {
        String url = getIntent().getStringExtra("url");
        webView.loadUrl(url);
        WebSettings setting = webView.getSettings();
        setting.setBuiltInZoomControls(true);//显示缩放按钮
        setting.setUseWideViewPort(true);//支持缩放
        setting.setJavaScriptEnabled(true);//支持js功能(js实现动态网页，标签控制/请求服务器...)

        webView.setWebViewClient(new WebViewClient() {
            //开始加载网页
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                Log.v("TAG", "加载地址:" + url);
            }

            //网页加载结束
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                Log.v("TAG", "加载结束:" + url);
            }

            //所有的连接跳转都会回调此方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                /**
                 * view:当前的webview
                 * url：跳转链接
                 */
                webView.loadUrl(url);//连接跳转时，强制在当前webview加载
                Log.v("TAG", "跳转" + url);
                return true;
            }
        });
//        webView.goBack();返回上一张网页
//        webView.goForward();返回下一张网页

        //监听网页加载进度
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                //网页加载进度
                Log.v("TAG", "网页加载进度:" + newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                //获取加载的网页标题
                Log.v("TAG", "加载的网页标题:" + title);
            }
        });

    }

    @OnClick(R.id.bar_back)
    public void backClick(View view) {
        finish();
    }

    private int selectItem=2;  //当前选中的item的位置(默认选中正常字体)
    private int lastPosition=2;//上一次被选中item的位置
    private boolean isOk = true; //是否是却确定状态

    @OnClick(R.id.bar_textSize)
    public void textSizeClick(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("字体设置");
        String[] str = {"超大号字体", "大号字体", "正常字体","小号字体","超小号字体"};
        builder.setSingleChoiceItems(str, isOk == true ? selectItem : lastPosition, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectItem = which;
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isOk = true;
                lastPosition = selectItem;
                textSizeChange(selectItem);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isOk = false;
            }
        }).show();
    }

    private void textSizeChange(int selectItem) {
        WebSettings settings = webView.getSettings();
        WebSettings.TextSize textSize = WebSettings.TextSize.NORMAL;
        switch (selectItem){
            case 0:
                textSize = WebSettings.TextSize.LARGEST;
                break;
            case 1:
                textSize = WebSettings.TextSize.LARGER;
                break;
            case 2:
                textSize = WebSettings.TextSize.NORMAL;
                break;
            case 3:
                textSize = WebSettings.TextSize.SMALLER;
                break;
            case 4:
                textSize = WebSettings.TextSize.SMALLEST;
                break;
        }
        settings.setTextSize(textSize);
    }

    @OnClick(R.id.bar_share)
    public void shareImageClick(View view) {
        showShare();
    }
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();

        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }
}
