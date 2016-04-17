package cherry.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cherry.action.AllTagsAction;
import cherry.action.GetTagsAction;
import cherry.action.model.ViewTag;
import cherry.action.util.ActionBase;
import cherry.action.util.Responser;
import cherry.cherry.R;
import cherry.db.CherryHelper;
import cherry.fragment.Tab01Fragment;
import cherry.fragment.Tab03Fragment;
import cherry.widget.Tag;
import cherry.widget.TagListView;
import cherry.widget.TagView;

public class TagActivity extends Activity {
    private TextView mTagDone;
    private Button mButtonSet;
    private TagListView mTagListView;
    private TagListView mMyTag;
    private Context mContext;
    private CherryHelper mDB;
    private TextView mInput;
    private Button mBtnTagManage;
    private List<Tag> mTags = new ArrayList<Tag>();
    private List<Tag> mMyTagList = new ArrayList<Tag>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_tag_activity);
        initView();
        initEvent();
    }

    void initView() {
        mTagListView = (TagListView) findViewById(R.id.tagview);
        mBtnTagManage=(Button)findViewById(R.id.btn_manager_tag);
        if (StaticValue.sCurrentUser == null) {
            setUpData();
        } else {
            getUserTags();
        }
        mTagDone = (TextView) findViewById(R.id.tag_done);
    }

    void initEvent() {
        mContext = this;
        mDB = new CherryHelper(mContext);
        mTagListView.setOnTagClickListener(new TagListView.OnTagClickListener() {
            @Override
            public void onTagClick(TagView tagView, Tag tag) {
                if (tag.isChecked() == false) {
                    StaticValue.sTagGroup.put(tagView.getText().toString(), StaticValue.sGetAllTag.get(tagView.getText().toString()));
                    if (StaticValue.sCurrentUser != null)
                        mDB.insertChosenTag(StaticValue.sGetAllTag.get(tagView.getText().toString()), StaticValue.sCurrentUser.getUserid());
                    else
                        mDB.insertChosenTag(StaticValue.sGetAllTag.get(tagView.getText().toString()), "##");
                    tagView.setBackgroundResource(R.drawable.tag_bg2);
                    tag.setChecked(true);
                } else if (tag.isChecked() == true) {
                    StaticValue.sTagGroup.remove(tagView.getText().toString());
                    if (StaticValue.sCurrentUser != null)
                        mDB.deleteChosenTag(StaticValue.sGetAllTag.get(tagView.getText().toString()).getTagid(), StaticValue.sCurrentUser.getUserid());
                    else
                        mDB.deleteChosenTag(StaticValue.sGetAllTag.get(tagView.getText().toString()).getTagid(), "##");
                    tagView.setBackgroundResource(R.drawable.tag_bg);
                    tag.setChecked(false);
                }
            }
        });
        mTagDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StaticValue.sTagGroup.size() == 0)
                    Toast.makeText(mContext, "请至少选择一个标签", Toast.LENGTH_SHORT).show();
                else {
                    Tab01Fragment.refresh();
                    finish();
                }
            }
        });
        mBtnTagManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,TagSelectActivity.class);
                startActivity(intent);
            }
        });

    }

    public void getUserTags() {
        ActionBase getTag = new GetTagsAction(StaticValue.sCurrentUser.getUserid());
        getTag.execute(new Responser() {
            @Override
            public void successfulResponse(Object param) {
                StaticValue.mList = (List<ViewTag>) param;
                List<ViewTag> list = StaticValue.mList;
                for (int i = 0; i < list.size(); i++) {
                    Tag tag = new Tag();
                    tag.setId(i);
                    tag.setChecked(false);
                    tag.setTitle(list.get(i).getTagname());
                    StaticValue.sGetAllTag.put(list.get(i).getTagname(), list.get(i));
                    mTags.add(tag);
                }
                mTagListView.setTags(mTags);
                for (int i = 0; i < mTagListView.getTags().size(); i++) {
                    if (!StaticValue.sTagGroup.isEmpty()) {
                        if (StaticValue.sTagGroup.get(mTagListView.getTags().get(i).getTitle().toString()) != null) {
                            mTagListView.getTags().get(i).setChecked(true);
                            mTagListView.getViewByTag(mTagListView.getTags().get(i)).setBackgroundResource(R.drawable.tag_bg2);
                        }
                    }
                }
            }

            @Override
            public void failedResponse(String error) {

            }
        });
    }


    private void setUpData() {
        mContext = this;
        mDB = new CherryHelper(mContext);
        List<ViewTag> list = mDB.selectTag();
        for (int i = 0; i < list.size(); i++) {
            Tag tag = new Tag();
            tag.setId(i);
            if (StaticValue.sTagGroup.get(list.get(i).getTagname()) != null) {
                tag.setChecked(true);
            } else {
                tag.setChecked(false);
            }
            tag.setTitle(list.get(i).getTagname());
            StaticValue.sGetAllTag.put(list.get(i).getTagname(), list.get(i));
            mTags.add(tag);
        }
        mTagListView.setTags(mTags);
        for (int i = 0; i < mTagListView.getTags().size(); i++) {
            if (!StaticValue.sTagGroup.isEmpty()) {
                if (StaticValue.sTagGroup.get(mTagListView.getTags().get(i).getTitle().toString()) != null) {
                    mTagListView.getViewByTag(mTagListView.getTags().get(i)).setBackgroundResource(R.drawable.tag_bg2);
                }
            }
        }
    }
}
