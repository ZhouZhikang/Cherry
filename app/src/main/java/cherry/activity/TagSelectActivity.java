package cherry.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cherry.action.AllTagsAction;
import cherry.action.GetTagsAction;
import cherry.action.UpdateTagsAction;
import cherry.action.model.ViewTag;
import cherry.action.util.ActionBase;
import cherry.action.util.Responser;
import cherry.cherry.R;
import cherry.db.CherryHelper;
import cherry.fragment.Tab01Fragment;
import cherry.widget.Tag;
import cherry.widget.TagListView;
import cherry.widget.TagView;

public class TagSelectActivity extends Activity {
    private TextView mFavorite;
    private TagListView mTagAll;
    private Context mContext;
    private CherryHelper mDB;
    private Map<String, Tag> mMap = new HashMap<String, Tag>();
    private List<Tag> mTags = new ArrayList<Tag>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_select);
        initView();
        initEvent();
    }

    void initView() {
        if (StaticValue.sCurrentUser != null)
            getUserTags();
        mTagAll = (TagListView) findViewById(R.id.tagview_all);
        setUpData();
        mFavorite = (TextView) findViewById(R.id.tag_done_all);
    }

    private void setUpData() {
        ActionBase action = new AllTagsAction();
        action.execute(new Responser() {
            @Override
            public void successfulResponse(Object param) {
                List<ViewTag> list = (List<ViewTag>) param;
                if (StaticValue.sCurrentUser == null) {
                    mDB = new CherryHelper(mContext);
                    StaticValue.mList = mDB.selectTag();
                }
                for (int i = 0; i < list.size(); i++) {
                    Tag tag = new Tag();
                    tag.setId(i);
                    tag.setChecked(false);
                    tag.setTitle(list.get(i).getTagname());
                    StaticValue.sGetAllTag.put(list.get(i).getTagname(), list.get(i));
                    mTags.add(tag);
                }
                mTagAll.setTags(mTags);
                if (StaticValue.mList != null) {
                    for (int j = 0; j < StaticValue.mList.size(); j++) {
                        for (int i = 0; i < mTagAll.getTags().size(); i++) {
                            if (StaticValue.mList.get(j).getTagname().equals(mTagAll.getTags().get(i).getTitle())) {
                                mTagAll.getTags().get(i).setChecked(true);
                                mTagAll.getViewByTag(mTagAll.getTags().get(i)).setBackgroundResource(R.drawable.tag_bg2);
                            }
                        }
                    }
                }
            }

            @Override
            public void failedResponse(String error) {

            }
        });
    }


    void initEvent() {
        mContext = this;
        if (StaticValue.sCurrentUser == null) {
            mDB = new CherryHelper(mContext);
            mTagAll.setOnTagClickListener(new TagListView.OnTagClickListener() {
                @Override
                public void onTagClick(TagView tagView, Tag tag) {
                    if (tag.isChecked() == false) {
                        mDB.insertTag(StaticValue.sGetAllTag.get(tagView.getText().toString()));
                        tagView.setBackgroundResource(R.drawable.tag_bg2);
                        tag.setChecked(true);
                    } else if (tag.isChecked() == true) {
                        mDB.deleteTag(StaticValue.sGetAllTag.get(tagView.getText().toString()).getTagid());
                        if(StaticValue.sTagGroup.containsKey(tagView.getText().toString())){
                            StaticValue.sTagGroup.remove(tagView.getText().toString());
                        }
                        tagView.setBackgroundResource(R.drawable.tag_bg);
                        tag.setChecked(false);
                    }

                }
            });
            mFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (StaticValue.sFromWhere.equals("welcome")) {
                        Intent intent = new Intent(TagSelectActivity.this, MainActivity.class);
                        startActivity(intent);
                        SharedPreferences.Editor editor = WelcomeActivity.sp.edit();
                        editor.putBoolean("hasSelected", true).commit();
                        List<ViewTag> list = mDB.selectTag();
                        for (int i = 0; i < list.size(); i++) {
                            StaticValue.sTagGroup.put(list.get(i).getTagname(), list.get(i));
                        }
                        finish();
                    } else if (StaticValue.sFromWhere.equals("my")) {
                        finish();
                    }
                }
            });
        } else {
            mTagAll.setOnTagClickListener(new TagListView.OnTagClickListener() {
                @Override
                public void onTagClick(TagView tagView, Tag tag) {
                    if (tag.isChecked() == false) {
                        StaticValue.updateMap.put(tagView.getText().toString(), StaticValue.sGetAllTag.get(tagView.getText().toString()));
                        tagView.setBackgroundResource(R.drawable.tag_bg2);
                        tag.setChecked(true);
                    } else if (tag.isChecked() == true) {
                        StaticValue.updateMap.remove(tagView.getText().toString());
                        if(StaticValue.sTagGroup.containsKey(tagView.getText().toString())){
                            StaticValue.sTagGroup.remove(tagView.getText().toString());
                        }
                        tagView.setBackgroundResource(R.drawable.tag_bg);
                        tag.setChecked(false);
                    }

                }
            });
            mFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<ViewTag> list = new ArrayList<ViewTag>();
                    for (ViewTag up : StaticValue.updateMap.values()) {
                        list.add(up);
                    }
                    StaticValue.mList = list;
                    ActionBase update = new UpdateTagsAction(StaticValue.sCurrentUser.getUserid(), list);
                    update.execute(new Responser() {
                        @Override
                        public void successfulResponse(Object param) {
                            finish();
                        }

                        @Override
                        public void failedResponse(String error) {
                            Toast.makeText(getApplicationContext(),
                                    error, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }

    }


    public static void getUserTags() {
        ActionBase getTag = new GetTagsAction(StaticValue.sCurrentUser.getUserid());
        getTag.execute(new Responser() {
            @Override
            public void successfulResponse(Object param) {
                StaticValue.mList = (List<ViewTag>) param;
                for (int i = 0; i < StaticValue.mList.size(); i++) {
                    Log.i("######", StaticValue.mList.get(i).getTagname());
                    StaticValue.updateMap.put(StaticValue.mList.get(i).getTagname(), StaticValue.mList.get(i));
                }
            }

            @Override
            public void failedResponse(String error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tag_select, menu);
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
