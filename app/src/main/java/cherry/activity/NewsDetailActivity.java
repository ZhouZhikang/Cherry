package cherry.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import cherry.action.AddCollectionAction;
import cherry.action.CommentAction;
import cherry.action.RemoveCollectionAction;
import cherry.action.util.ActionBase;
import cherry.action.util.Responser;
import cherry.cherry.R;
import cherry.db.CherryHelper;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by aqi on 15/7/13.
 */
public class NewsDetailActivity extends Activity {
    private String mUrl;
    private String mCommentUrl;
    private String mNewsid;
    private String mNewsName;
    private WebView mWebView;
    private TextView mSend;
    private Button mSeaComment;
    private ImageButton mBack;
    private ImageButton mBtnAddColletion;
    private ImageButton mBtnShare;
    private EditText mEtComment;
    private CherryHelper helper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsdetail);
        mWebView = (WebView) findViewById(R.id.news_webview);
        mEtComment = (EditText) findViewById(R.id.news_et_comment);
        mBtnShare=(ImageButton)findViewById(R.id.ib_share);
        mSeaComment = (Button) findViewById(R.id.sea_comment);
        mSend=(TextView)findViewById(R.id.tv_send);
        mBtnAddColletion = (ImageButton) findViewById(R.id.news_add_collection);
        mBack=(ImageButton)findViewById(R.id.ib_back);
        //设置WebView属性，能够执行Javascript脚本
        mWebView.getSettings().setJavaScriptEnabled(true);
        //加载需要显示的网页
        mUrl = (String) getIntent().getExtras().getCharSequence("url");
        mCommentUrl = (String) getIntent().getExtras().getCharSequence("comment_url");
        mNewsid = (String) getIntent().getExtras().getCharSequence("newsid");
        mNewsName = (String) getIntent().getExtras().getCharSequence("newsname");
        String count=(String)getIntent().getExtras().getCharSequence("commentcount");
        mWebView.loadUrl(mUrl);
        //设置Web视图
        mWebView.setWebViewClient(new HelloWebViewClient());

        initEvent();

    }

    void initEvent(){
        InitIcon();
        AddCommentListener();
        AddCollection();
        AddShare();
        SeaAllComment();
        mEtComment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mBtnAddColletion.setVisibility(View.GONE);
                    mBtnShare.setVisibility(View.GONE);
                    mSend.setVisibility(View.VISIBLE);
                } else {
                    mBtnAddColletion.setVisibility(View.VISIBLE);
                    mBtnShare.setVisibility(View.VISIBLE);
                    mSend.setVisibility(View.GONE);
                    mEtComment.setText("");
                }
            }
        });
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment();
                mEtComment.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void InitIcon(){
        if(StaticValue.sCurrentUser != null){
            if(!isCollection((StaticValue.sCurrentUser.getUserid()),mNewsid)){
                mBtnAddColletion.setBackgroundResource(R.mipmap.ic_star_border_black);
            }else {
                mBtnAddColletion.setBackgroundResource(R.mipmap.ic_star_black);
            }
        }
    }

    private void AddShare(){
        mBtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("https://github.com/M-CATS");
        // text是分享文本，所有平台都需要这个字段
//        oks.setText("我是分享文本");
        oks.setText("新闻标题"+mNewsName);
        // url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl("http://sharesdk.cn");
        oks.setUrl(mUrl);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl("http://sharesdk.cn");
        oks.setSiteUrl(mUrl);

// 启动分享GUI
        oks.show(this);
    }

    private void AddCollection() {

        mBtnAddColletion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (StaticValue.sCurrentUser != null ) {

                    if(!isCollection(StaticValue.sCurrentUser.getUserid(),mNewsid)) {

                        ActionBase action = new AddCollectionAction(StaticValue.sCurrentUser.getUserid(), mNewsid);
                        action.execute(new Responser() {
                            @Override
                            public void successfulResponse(Object param) {
                                helper.insertCollection(StaticValue.sCurrentUser.getUserid(), mNewsid);
                                mBtnAddColletion.setBackgroundResource(R.mipmap.ic_star_black);
                                Toast.makeText(getApplicationContext(), "收藏成功！", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void failedResponse(String error) {
                                Toast.makeText(getApplicationContext(), "收藏失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{

                        ActionBase action = new RemoveCollectionAction(StaticValue.sCurrentUser.getUserid(),mNewsid);
                        action.execute(new Responser() {
                            @Override
                            public void successfulResponse(Object param) {
                                helper.deleteCollection(StaticValue.sCurrentUser.getUserid(), mNewsid);
                                mBtnAddColletion.setBackgroundResource(R.mipmap.ic_star_border_black);
                                Toast.makeText(getApplicationContext(), "取消收藏成功！", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void failedResponse(String error) {
                                Toast.makeText(getApplicationContext(), "取消收藏失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    private boolean isCollection(String userid, String newsid){
        helper = new CherryHelper(getApplicationContext());
        if(helper.isExistCollection(userid,newsid)){
            return true;
        }else {
            return false;
        }
    }


    private void AddCommentListener() {
        mEtComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.e("textxxxxxxxxxxxxxxxxxx", mEtComment.getText().toString().trim()+","+mNewsid+","+mEtComment.getText().toString());
                if (actionId== EditorInfo.IME_ACTION_SEND ){

                    addComment();
                    return true;
                }
                return false;
            }
        });

    }

    private void addComment(){
        if(StaticValue.sCurrentUser != null) {
            if (!mEtComment.getText().toString().trim().equals("") && mEtComment.getText().toString() != null) {
                ActionBase action = new CommentAction(StaticValue.sCurrentUser.getUserid(), mNewsid, mEtComment.getText().toString());
                action.execute(new Responser() {
                    @Override
                    public void successfulResponse(Object param) {
                        Toast.makeText(getApplicationContext(), "评论发表成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void failedResponse(String error) {
                        Toast.makeText(getApplicationContext(), "评论发表失败", Toast.LENGTH_SHORT).show();
                        Log.e("comment_error", error);
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "评论为空，不能发表哦！", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getApplicationContext(),"请先登录!",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);

        }
    }

    private void SeaAllComment(){
        mSeaComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), AllCommentAcivity.class);
                Bundle b = new Bundle();
                b.putCharSequence("comment_url", mCommentUrl);
                intent.putExtras(b);
                startActivity(intent);

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
