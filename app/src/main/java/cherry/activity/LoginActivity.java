package cherry.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.tools.utils.UIHandler;

import java.util.HashMap;

import cherry.action.LoginAction;
import cherry.action.ThirdPartyLoginAction;
import cherry.action.UserSource;
import cherry.action.model.ViewUser;
import cherry.action.util.ActionBase;
import cherry.action.util.ErrorMsg;
import cherry.action.util.Responser;
import cherry.cherry.R;
import cherry.fragment.Tab03Fragment;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.favorite.WechatFavorite;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by AppleOne on 2015/7/16.
 */
public class LoginActivity extends Activity implements Handler.Callback,

        View.OnClickListener, PlatformActionListener {

    private TextView register;
    private EditText userid;
    private EditText pwd;
    private Button submit;
    private ImageView qqlogin;
    private ImageView sinalogin;
    private String password, userId;
    private static final int MSG_USERID_FOUND = 1;
    private static final int MSG_LOGIN = 2;
    private static final int MSG_AUTH_CANCEL = 3;
    private static final int MSG_AUTH_ERROR = 4;
    private static final int MSG_AUTH_COMPLETE = 5;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StaticValue.sharedPreferences = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        ShareSDK.initSDK(this);
        initView();
        initEvent();
    }

    void initView() {
        userid = (EditText) findViewById(R.id.login_edtId);
        pwd = (EditText) findViewById(R.id.login_edtPwd);
        submit = (Button) findViewById(R.id.login_btnLogin);
        register = (TextView) findViewById(R.id.login_register);
        qqlogin = (ImageView) findViewById(R.id.login_qqLogin);
        sinalogin = (ImageView) findViewById(R.id.login_sinaLogin);


    }

    void initKey() {
        ShareSDK.initSDK(this, "AppKey");
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("Id", "1");
        hashMap.put("SortId", "1");
        hashMap.put("AppKey", "3045229302");
        hashMap.put("AppSecret", "9100bddfa1b95c066a12703c2ae9c6df");
        hashMap.put("RedirectUrl", "https://api.weibo.com/oauth2/default.html");
        hashMap.put("ShareByAppClient", "true");
        hashMap.put("Enable", "true");
        ShareSDK.setPlatformDevInfo(SinaWeibo.NAME, hashMap);

        ShareSDK.initSDK(this, "AppKey");
        HashMap<String, Object> qqzone = new HashMap<String, Object>();
        qqzone.put("Id", "3");
        qqzone.put("SortId", "3");
        qqzone.put("AppId", "1104697953");
        qqzone.put("AppKey", "9h8kGpyrHXPdVv8V");
        qqzone.put("ShareByAppClient", "true");
        qqzone.put("Enable", "true");
        ShareSDK.setPlatformDevInfo(QZone.NAME, qqzone);

        ShareSDK.initSDK(this, "AppKey");
        HashMap<String, Object> qq = new HashMap<String, Object>();
        qq.put("Id", "7");
        qq.put("SortId", "7");
        qq.put("AppId", "1104697953");
        qq.put("AppKey", "9h8kGpyrHXPdVv8V");
        qq.put("ShareByAppClient", "true");
        qq.put("Enable", "true");
        ShareSDK.setPlatformDevInfo(QQ.NAME, qq);

        ShareSDK.initSDK(this, "AppKey");
        HashMap<String, Object> wechat = new HashMap<String, Object>();
        wechat.put("Id", "4");
        wechat.put("SortId", "4");
        wechat.put("AppId", "wx17ffd24826543613");
        wechat.put("AppSecret", "95b07fd8aa9d576bc514a889f54fb713");
        wechat.put("BypassApproval", "false");
        wechat.put("Enable", "true");
        ShareSDK.setPlatformDevInfo(Wechat.NAME, wechat);

        ShareSDK.initSDK(this, "AppKey");
        HashMap<String, Object> wechatMoments = new HashMap<String, Object>();
        wechatMoments.put("Id", "5");
        wechatMoments.put("SortId", "5");
        wechatMoments.put("AppId", "wx17ffd24826543613");
        wechatMoments.put("AppSecret", "95b07fd8aa9d576bc514a889f54fb713");
        wechatMoments.put("BypassApproval", "false");
        wechatMoments.put("Enable", "true");
        ShareSDK.setPlatformDevInfo(WechatMoments.NAME, wechatMoments);

        ShareSDK.initSDK(this, "AppKey");
        HashMap<String, Object> wechatFavorite = new HashMap<String, Object>();
        wechatFavorite.put("Id", "6");
        wechatFavorite.put("SortId", "6");
        wechatFavorite.put("AppId", "wx17ffd24826543613");
        wechatFavorite.put("AppSecret", "95b07fd8aa9d576bc514a889f54fb713");
        wechatFavorite.put("Enable", "true");
        ShareSDK.setPlatformDevInfo(WechatFavorite.NAME, wechatFavorite);

    }

    void initEvent() {
        qqlogin.setOnClickListener(this);
        sinalogin.setOnClickListener(this);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get Password Edit View Value
                password = pwd.getText().toString();
                userId = userid.getText().toString();
                System.out.println(password + "  " + userId);
                ActionBase login = new LoginAction(userId, password);
                login.execute(new Responser() {
                    @Override
                    public void successfulResponse(Object param) {
                        SharedPreferences.Editor editor = StaticValue.sharedPreferences.edit();
                        editor.putString("USER_NAME", userId);
                        Log.i("ssssss", userId);
                        editor.putString("PASSWORD", password);
                        editor.commit();
                        StaticValue.sCurrentUser = (ViewUser) param;
                        Tab03Fragment.mMyInfo.setText(StaticValue.sCurrentUser.getUsername());
                        LoginActivity.this.finish();
                        //put userid in gobal

                    }

                    @Override
                    public void failedResponse(String error) {
                        String errordetail = null;
                        if (error.equals(ErrorMsg.UNKNOWN_ERROR)) {
                            errordetail = "居然有未知的错误 >.< 正在努力排查中……";
                        }else if(error.equals(ErrorMsg.WRONG_PASSWORD)){
                            errordetail="密码错误，请重新输入";
                        }else if(error.equals(ErrorMsg.USER_NOT_FOUND)){
                            errordetail="用户名不存在，请注册！";
                        }
                        Toast.makeText(getApplicationContext(),
                                errordetail, Toast.LENGTH_LONG).show();

                    }
                });


            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });


    }

    protected void onDestroy() {
        ShareSDK.stopSDK(this);
        super.onDestroy();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_qqLogin: {
                Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                authorize(qzone);
//                authorize(new QQ(this));
            }
            break;
            case R.id.login_sinaLogin: {
                authorize(new SinaWeibo(this));
            }
            break;
        }
    }

    private void authorize(Platform plat) {
        if (plat.isValid()) {
            String userId = plat.getDb().getUserId();
            if (!TextUtils.isEmpty(userId)) {
                UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
                login(plat, userId, null);
                return;
            }
        }
        plat.setPlatformActionListener(this);
        plat.SSOSetting(true);
        plat.showUser(null);
    }

    public void onComplete(Platform platform, int action,
                           HashMap<String, Object> res) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_COMPLETE, this);
            login(platform, platform.getDb().getUserId(), res);
        }
        System.out.println(res);
        System.out.println("------User Name ---------" + platform.getDb().getUserName());
        System.out.println("------User ID ---------" + platform.getDb().getUserId());
    }

    public void onError(Platform platform, int action, Throwable t) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, this);
        }
        t.printStackTrace();
    }

    public void onCancel(Platform platform, int action) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
        }
    }

    private void login(Platform plat, String userId, HashMap<String, Object> userInfo) {
        Message msg = new Message();
        msg.what = MSG_LOGIN;
        msg.obj = plat;
        UIHandler.sendMessage(msg, this);
    }

    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_USERID_FOUND: {
                Toast.makeText(this, R.string.userid_found, Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_LOGIN: {

                Platform user = (Platform) msg.obj;

                String text = getString(R.string.logining, user.getDb().getPlatformNname());

                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                System.out.println("---------------" + user.getDb().getPlatformNname());

                String platName = checkPlatName(user.getDb().getPlatformNname());
                ActionBase thridPartyLogin = new ThirdPartyLoginAction(user.getDb().getUserId(), user.getDb().getUserName(), platName);
                thridPartyLogin.execute(new Responser() {
                    @Override
                    public void successfulResponse(Object param) {
//                        Intent intent = new Intent(getApplicationContext(), Tab03Fragment.class);
//                        startActivity(intent);
                        StaticValue.sCurrentUser = (ViewUser) param;
                        Tab03Fragment.mMyInfo.setText(StaticValue.sCurrentUser.getUsername());
                        onBackPressed();
                    }

                    @Override
                    public void failedResponse(String error) {
                        Toast.makeText(getApplicationContext(),
                                error, Toast.LENGTH_LONG).show();

                    }
                });

//				Builder builder = new Builder(this);
//				builder.setTitle(R.string.if_register_needed);
//				builder.setMessage(R.string.after_auth);
//				builder.setPositiveButton(R.string.ok, null);
//				builder.create().show();
            }
            break;
            case MSG_AUTH_CANCEL: {
                Toast.makeText(this, R.string.auth_cancel, Toast.LENGTH_SHORT).show();
                System.out.println("-------MSG_AUTH_CANCEL--------");
            }
            break;
            case MSG_AUTH_ERROR: {
                Toast.makeText(this, R.string.auth_error, Toast.LENGTH_SHORT).show();
                System.out.println("-------MSG_AUTH_ERROR--------");
            }
            break;
            case MSG_AUTH_COMPLETE: {
                Toast.makeText(this, R.string.auth_complete, Toast.LENGTH_SHORT).show();
                System.out.println("--------MSG_AUTH_COMPLETE-------");
            }
            break;
        }
        return false;
    }


    public String checkPlatName(String platName) {
        if (platName.equals("SinaWeibo")) {
            return UserSource.SINA;
        } else if (platName.equals("QZone")) {
            return UserSource.TENCENT;
        } else
            return null;
    }

}
