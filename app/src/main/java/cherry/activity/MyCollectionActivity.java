package cherry.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cherry.action.GetCollectionsAction;
import cherry.action.model.ViewNews;
import cherry.action.util.ActionBase;
import cherry.action.util.Responser;
import cherry.adpter.Tab01ListAdpter;
import cherry.cherry.R;

/**
 * Created by aqi on 15/7/18.
 */
public class MyCollectionActivity extends Activity{
    private ListView mList;
    private LinearLayout mWarning;
    private List<ViewNews> mNewslist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycollection);
        mList = (ListView) findViewById(R.id.collection_list);
        mWarning=(LinearLayout)findViewById(R.id.ll_warning);
        getCollection();
        initEvent();
    }

    private void getCollection(){
            ActionBase action = new GetCollectionsAction(StaticValue.sCurrentUser.getUserid());
            action.getPage(1).execute(new Responser() {
                @Override
                public void successfulResponse(Object param) {
                     mNewslist = (List<ViewNews>) param;
                    if(mNewslist.size()==0){
                        mWarning.setVisibility(View.VISIBLE);
                    }
                    else {
                        mList.setAdapter(new Tab01ListAdpter(getApplicationContext(), mNewslist));
                        mWarning.setVisibility(View.GONE);
                    }
                }

                @Override
                public void failedResponse(String error) {

                }
            });
    }

    private void initEvent(){
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), NewsDetailActivity.class);
                Bundle b = new Bundle();
                b.putCharSequence("url", mNewslist.get(position).getPageUrl());
                b.putCharSequence("comment_url",mNewslist.get(position).getCommentUrl());
                b.putCharSequence("comment_url",mNewslist.get(position).getCommentUrl());
                b.putCharSequence("newsid", mNewslist.get(position).getNewsid());
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

}
