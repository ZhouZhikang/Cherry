package cherry.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cherry.action.CommentAction;
import cherry.action.util.ActionBase;
import cherry.action.util.Responser;
import cherry.cherry.R;

/**
 * Created by aqi on 15/7/16.
 */
public class CommentActivity extends Activity{
    private EditText mCommentText;
    private Button mAddBtn;
    private String mUserid;
    private String mNewsid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcomment);
        mCommentText = (EditText)findViewById(R.id.comment_edittext);
        mAddBtn = (Button) findViewById(R.id.add_btn);

        mUserid = (String)getIntent().getExtras().getCharSequence("userid");
        mNewsid = (String)getIntent().getExtras().getCharSequence("newsid");
        AddComment();
    }

    private void AddComment(){
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionBase action = new CommentAction(mUserid,mNewsid,mCommentText.getText().toString());
                action.execute(new Responser() {
                    @Override
                    public void successfulResponse(Object param) {
                        Toast.makeText(getApplicationContext(), "评论发表成功", Toast.LENGTH_SHORT).show();
                        
                        onBackPressed();

                    }

                    @Override
                    public void failedResponse(String error) {
                        Toast.makeText(getApplicationContext(),"评论发表失败",Toast.LENGTH_SHORT).show();
                        Log.e("comment_error",error);
                    }
                });
            }
        });
    }


}
