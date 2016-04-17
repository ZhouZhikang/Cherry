package cherry.activity;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cherry.cherry.R;
import cherry.fragment.Tab01Fragment;
import cherry.fragment.Tab02Fragment;
import cherry.fragment.Tab03Fragment;


public class MainActivity extends FragmentActivity implements View.OnClickListener{
    private LinearLayout mTab01;
    private LinearLayout mTab02;
    private LinearLayout mTab03;
    private TextView mTextView01;
    private TextView mTextView02;
    private TextView mTextView03;
    private TextView mTextViewTitle;
    private Fragment mFragmentTab01;
    private Fragment mFragmentTab02;
    private Fragment mFragmentTab03;
    public FragmentManager fm=getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        setSelect(0);
    }

    void initView(){
        mTab01=(LinearLayout)findViewById(R.id.ll_tab01);
        mTab02=(LinearLayout)findViewById(R.id.ll_tab02);
        mTab03=(LinearLayout)findViewById(R.id.ll_tab03);
        mTextView01=(TextView)findViewById(R.id.tv_tab01);
        mTextView02=(TextView)findViewById(R.id.tv_tab02);
        mTextView03=(TextView)findViewById(R.id.tv_tab03);
        mTextViewTitle=(TextView)findViewById(R.id.tv_title);
        fm = getSupportFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        mFragmentTab01 = new Tab01Fragment();
        mFragmentTab02 = new Tab02Fragment();
        mFragmentTab03 = new Tab03Fragment();
        trans.add(R.id.id_fragment, mFragmentTab01);
        trans.add(R.id.id_fragment, mFragmentTab02);
        trans.add(R.id.id_fragment, mFragmentTab03);
        trans.commit();
    }

    void initEvent(){
        mTab01.setOnClickListener(this);
        mTab02.setOnClickListener(this);
        mTab03.setOnClickListener(this);
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

    private void setSelect(int i) {
        FragmentTransaction trans = fm.beginTransaction();
        switch (i) {
            case 0:
                trans.show(mFragmentTab01);
                trans.hide(mFragmentTab02);
                trans.hide(mFragmentTab03);
                mTab01.setBackgroundResource(R.color.deep_blue);
                mTab02.setBackgroundResource(R.color.umeng_fb_color_btn_normal);
                mTab03.setBackgroundResource(R.color.umeng_fb_color_btn_normal);

                break;
            case 1:
                trans.show(mFragmentTab02);
                trans.hide(mFragmentTab01);
                trans.hide(mFragmentTab03);
                mTab02.setBackgroundResource(R.color.deep_blue);
                mTab01.setBackgroundResource(R.color.umeng_fb_color_btn_normal);
                mTab03.setBackgroundResource(R.color.umeng_fb_color_btn_normal);
                break;
            case 2:
                trans.show(mFragmentTab03);
                trans.hide(mFragmentTab02);
                trans.hide(mFragmentTab01);
                mTab03.setBackgroundResource(R.color.deep_blue);
                mTab01.setBackgroundResource(R.color.umeng_fb_color_btn_normal);
                mTab02.setBackgroundResource(R.color.umeng_fb_color_btn_normal);
                break;
        }
        trans.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_tab01:
                setSelect(0);
                break;
            case R.id.ll_tab02:
                setSelect(1);
                break;
            case R.id.ll_tab03:
                setSelect(2);
                break;
        }
    }
}
