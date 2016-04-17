package cherry.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import cherry.action.UpdateUserAction;
import cherry.action.util.ActionBase;
import cherry.action.util.Responser;
import cherry.cherry.R;
import cherry.fragment.Tab03Fragment;

/**
 * Created by aqi on 15/7/18.
 */
public class ChangeUsernameActivity extends Activity {
    private EditText mUsernameEt;
    private Button mSaveBtn;
    private ImageButton mCleanBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);
        mUsernameEt = (EditText) findViewById(R.id.change_usernameet);
        mSaveBtn = (Button) findViewById(R.id.change_username_savebtn);
        mCleanBtn = (ImageButton) findViewById(R.id.btn_clear_username_text);

        mUsernameEt.setText(StaticValue.sCurrentUser.getUsername());
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mUsernameEt.getText().toString().trim().equals("") && mUsernameEt.getText().toString() != null) {
                    ActionBase action = new UpdateUserAction(StaticValue.sCurrentUser.getUserid(), mUsernameEt.getText().toString());
                    action.execute(new Responser() {
                        @Override
                        public void successfulResponse(Object param) {
                            Toast.makeText(getApplication(), param.toString(), Toast.LENGTH_SHORT);
                            StaticValue.sCurrentUser.setUsername(mUsernameEt.getText().toString());
                            Tab03Fragment.mMyInfo.setText(mUsernameEt.getText().toString());
                            PersonalActivity.mUsernameTv.setText(mUsernameEt.getText().toString());
                            onBackPressed();
                        }

                        @Override
                        public void failedResponse(String error) {
                            Log.e("changenameerror", error);
                            Toast.makeText(getApplication(), "修改用户名失败，请稍后再尝试", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getApplication(), "用户名不能为空！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mCleanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUsernameEt.setText("");
            }
        });

    }
}
