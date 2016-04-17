package cherry.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import cherry.action.UpdatePwdAction;
import cherry.action.util.ActionBase;
import cherry.action.util.Responser;
import cherry.cherry.R;

/**
 * Created by aqi on 15/7/18.
 */
public class ChangePwdAcitivity extends Activity {
    private EditText mOldpwdEt;
    private EditText mNewpwdEt1;
    private EditText mNewpwdEt2;
    private ImageButton mCleanOldpwdBtn;
    private ImageButton mCleanNewpwdBtn1;
    private ImageButton mCleanNewpwdBtn2;
    private Button mChangeBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        mOldpwdEt = (EditText) findViewById(R.id.change_oldpwd);
        mNewpwdEt1 = (EditText) findViewById(R.id.change_newpwd1);
        mNewpwdEt2 = (EditText) findViewById(R.id.change_newpwd2);
        mCleanOldpwdBtn = (ImageButton) findViewById(R.id.btn_clear_oldpwd_text);
        mCleanNewpwdBtn1 = (ImageButton) findViewById(R.id.btn_clear_newpwd1_text);
        mCleanNewpwdBtn2 = (ImageButton) findViewById(R.id.btn_clear_newpwd2_text);
        mChangeBtn = (Button) findViewById(R.id.change_pwd_savebtn);

        mCleanOldpwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOldpwdEt.setText("");
            }
        });

        mCleanNewpwdBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNewpwdEt1.setText("");
            }
        });

        mCleanNewpwdBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNewpwdEt2.setText("");
            }
        });

        mChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSame()) {
                    if (!mNewpwdEt1.getText().toString().trim().equals("") && mNewpwdEt1.getText().toString() != null) {
                        ActionBase action = new UpdatePwdAction(StaticValue.sCurrentUser.getUserid(), mOldpwdEt.getText().toString(), mNewpwdEt1.getText().toString());
                        action.execute(new Responser() {
                            @Override
                            public void successfulResponse(Object param) {
                                if (param.toString().equals("err_pwd")) {
                                    Toast.makeText(getApplicationContext(), "原密码错误！", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getApplicationContext(), "修改密码成功!", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }
                            }

                            @Override
                            public void failedResponse(String error) {
                                Toast.makeText(getApplicationContext(), "修改密码失败!", Toast.LENGTH_SHORT).show();
                                Log.e("changepwderror", error);
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "密码不能为空！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "两次输入密码不一样！", Toast.LENGTH_SHORT).show();
                }

            }


        });


    }

    private boolean isSame() {
        if (mNewpwdEt1.getText().toString().equals(mNewpwdEt2.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }
}
