package cherry.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import cherry.cherry.R;

/**
 * Created by aqi on 15/7/19.
 */
public class AllCommentAcivity extends Activity {
    private WebView mWebView;
    private ImageButton mBack;
    private String mCommentUrl;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_comment);
        mWebView = (WebView) findViewById(R.id.comment_webview);
        mBack=(ImageButton)findViewById(R.id.ib_allcomment_back);
        //设置WebView属性，能够执行Javascript脚本
        mWebView.getSettings().setJavaScriptEnabled(true);
        //加载需要显示的网页
        mCommentUrl = (String) getIntent().getExtras().getCharSequence("comment_url");
        mWebView.loadUrl(mCommentUrl);
        //设置Web视图
        mWebView.setWebViewClient(new HelloWebViewClient());
        initEvent();
    }

    void initEvent(){
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    //Web视图
    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
