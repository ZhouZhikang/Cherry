package cherry.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cherry.cherry.R;
import cherry.fragment.Tab03Fragment;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by aqi on 15/7/18.
 */
public class PersonalActivity extends Activity implements View.OnClickListener {
    private RelativeLayout mMyname;
    private RelativeLayout mMypwd;
    private RelativeLayout mMyout;
    public static TextView mUsernameTv;
    public static TextView mUseridTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        mMyname = (RelativeLayout) findViewById(R.id.ll_name);
        mMypwd = (RelativeLayout) findViewById(R.id.ll_pwd);
        mMyout = (RelativeLayout) findViewById(R.id.ll_out);
        mUsernameTv = (TextView) findViewById(R.id.tv_username);
        mUseridTv = (TextView) findViewById(R.id.tv_userid);

        mMyname.setOnClickListener(this);
        mMypwd.setOnClickListener(this);
        mMyout.setOnClickListener(this);

        mUsernameTv.setText(StaticValue.sCurrentUser.getUsername());
        mUseridTv.setText(StaticValue.sCurrentUser.getUserid());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_name:
                if (StaticValue.sCurrentUser != null) {
                    Intent changeusername = new Intent(getApplicationContext(), ChangeUsernameActivity.class);
                    startActivity(changeusername);
                } else {
                    Toast.makeText(getApplicationContext(), "登陆后才能修改用户名", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_pwd:
                if (StaticValue.sCurrentUser != null) {
                    if(!StaticValue.sCurrentUser.getUserid().contains("#")) {
                        Intent changepwd = new Intent(getApplicationContext(), ChangePwdAcitivity.class);
                        startActivity(changepwd);
                    }else{
                        Toast.makeText(getApplicationContext(),"第三方登录不能修改密码",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "登陆后才能修改密码", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_out:
                if (StaticValue.sCurrentUser.getUserid().contains("#")) {
                    String[] list = StaticValue.sCurrentUser.getUserid().split("#");
                    cancleAuth(list[0]);
                }
                StaticValue.sCurrentUser = null;
                Tab03Fragment.mMyInfo.setText("登录");
                StaticValue.sTagGroup.clear();
                onBackPressed();
                break;

        }
    }

    public void cancleAuth(String platName) {
        Platform plat = null;
        switch (platName) {
            case "SinaWeibo":
                plat = ShareSDK.getPlatform(this, SinaWeibo.NAME);
                break;
            case "Wechat":
                plat = ShareSDK.getPlatform(this, Wechat.NAME);
                break;
            case "QQ":
                plat = ShareSDK.getPlatform(this, QZone.NAME);
        }
        plat.removeAccount(true);
        ShareSDK.removeCookieOnAuthorize(true);
    }


}
