package cherry.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import cherry.action.GetTagsAction;
import cherry.action.LoginAction;
import cherry.action.model.ViewTag;
import cherry.action.model.ViewUser;
import cherry.action.util.ActionBase;
import cherry.action.util.Responser;
import cherry.cherry.R;
import cherry.db.CherryHelper;
import cherry.fragment.Tab03Fragment;


public class WelcomeActivity extends Activity {
    public static SharedPreferences sp;
    private CherryHelper mDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mDB = new CherryHelper(this);
        sp = this.getSharedPreferences("Tag", Context.MODE_PRIVATE);
        StaticValue.sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        if (StaticValue.sharedPreferences != null) {
            Log.i("GGGGGGGGGG", "这里被执行了");
            ActionBase login = new LoginAction(StaticValue.sharedPreferences.getString("USER_NAME", ""), StaticValue.sharedPreferences.getString("PASSWORD", ""));
            login.execute(new Responser() {
                @Override
                public void successfulResponse(Object param) {
                    Log.i("GGGGGGGGGG", "这里被执行了");
                    StaticValue.sCurrentUser = (ViewUser) param;
                    List<ViewTag> list = mDB.selectChosenTagByUserid(StaticValue.sCurrentUser.getUserid());
                    for (int i = 0; i < list.size(); i++) {
                        StaticValue.sTagGroup.put(list.get(i).getTagname(), list.get(i));
                    }
                }

                @Override
                public void failedResponse(String error) {
                    Log.i("GGGGGGGGGG", "这里被执行了");
                    List<ViewTag> list = mDB.selectChosenTagByUserid("##");
                    for (int i = 0; i < list.size(); i++) {
                        StaticValue.sTagGroup.put(list.get(i).getTagname(), list.get(i));
                    }
                }
            });
        }
//        else {
//            Log.i("GGGGGGGGGG", "这里被执行了");
//            List<ViewTag> list = mDB.selectChosenTagByUserid("##");
//            for (int i = 0; i < list.size(); i++) {
//                StaticValue.sTagGroup.put(list.get(i).getTagname(), list.get(i));
//            }
//        }

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (sp == null) {
                    SharedPreferences.Editor ed = sp.edit();
                    ed.putBoolean("hasSelected", false).commit();
                }
                if (sp.getBoolean("hasSelected", false) == false) {
                    Intent intent = new Intent(WelcomeActivity.this, TagSelectActivity.class);
                    StaticValue.sFromWhere = "welcome";
                    startActivity(intent);
                    WelcomeActivity.this.finish();
                } else if (sp.getBoolean("hasSelected", true) == true) {
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    WelcomeActivity.this.finish();
                }
            }
        }, 3000);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
