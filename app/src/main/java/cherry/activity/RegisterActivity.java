package cherry.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cherry.action.RegistAction;
import cherry.action.model.ViewUser;
import cherry.action.util.ActionBase;
import cherry.action.util.ErrorMsg;
import cherry.action.util.Responser;
import cherry.cherry.R;
import cherry.fragment.Tab03Fragment;

/**
 * Created by AppleOne on 2015/7/13.
 */
public class RegisterActivity extends Activity {

    private EditText userid;
    private EditText pwd;
    private EditText ComfirmPwd;
    private Button submit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initEvent();
    }

    void initView() {
        userid = (EditText) findViewById(R.id.register_userid);
        pwd = (EditText) findViewById(R.id.register_Pwd);
        ComfirmPwd = (EditText) findViewById(R.id.register_ConfirmPwd);
        submit = (Button) findViewById(R.id.register_submit);
    }

    void initEvent() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = userid.getText().toString();
                String password = pwd.getText().toString();
                // Get Password Edit View Value
                if (checkLogin()) {

                    ActionBase register = new RegistAction(userId, password);
                    register.execute(new Responser() {
                        @Override
                        public void successfulResponse(Object param) {
                            StaticValue.sCurrentUser=(ViewUser)param;
//                            Intent intent =new Intent(getApplicationContext(), Tab03Fragment.class);
//                            startActivity(intent);
                            Tab03Fragment.mMyInfo.setText(StaticValue.sCurrentUser.getUsername());
                            finish();
                            //将userid存入全局变量
                        }

                        @Override
                        public void failedResponse(String error) {
                            String errordetail = null;
                            if (error.equals(ErrorMsg.USERID_EXISTS)) {
                                errordetail = "用户名已存在！";
                            } else if (error.equals(ErrorMsg.UNKNOWN_ERROR)) {
                                errordetail = "居然有未知的错误 >.< 正在努力排查中……";
                            }
                            Toast.makeText(getApplicationContext(),
                                    errordetail, Toast.LENGTH_LONG).show();

                        }
                    });
                }

            }

        });

    }

    public boolean checkLogin() {
        if (checkPwd() && IsEmpty() && IsContiansStr()) {
            return true;
        } else {
            return false;
        }

    }

    public boolean checkPwd() {
        if ((pwd.getText().toString()).equals(ComfirmPwd.getText().toString())) {
            return true;
        } else {
            Toast.makeText(this, "重复输入密码错误！请重新输入！", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean IsContiansStr() {
        if(userid.getText().toString().contains("#")){
            Toast.makeText(this, "用户ID不得包含非法字符#", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
    }

    public boolean IsEmpty() {
        if (userid.getText().toString().trim().equals("")||userid.getText().toString()==null) {
            Toast.makeText(this, "用户ID不得为空！", Toast.LENGTH_SHORT).show();
            return false;
        } else if (pwd.getText().toString().trim().equals("")||pwd.getText().toString()==null) {
            Toast.makeText(this, "密码不得为空！", Toast.LENGTH_SHORT).show();
            return false;
        } else if (ComfirmPwd.getText().toString() == null||ComfirmPwd.getText().toString()==null) {
            Toast.makeText(this, "确认密码不得为空！", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

}
