package cherry.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cherry.action.GetNewsAction;
import cherry.action.model.NewsSet;
import cherry.action.model.ViewNews;
import cherry.action.model.ViewTag;
import cherry.action.util.ActionBase;
import cherry.action.util.Responser;
import cherry.activity.NewsDetailActivity;
import cherry.activity.StaticValue;
import cherry.activity.TagActivity;
import cherry.adpter.Tab01ListAdpter;
import cherry.cherry.R;


/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class Tab01Fragment extends Fragment implements View.OnClickListener {
    private ImageButton mAdd;
    private static TextView mGroupName;
    private static ListView mMainList;
    private static List<ViewNews> mainList;
    public static StringBuffer mGroupNameStringBuffer= new StringBuffer();;
    private SwipeRefreshLayout mSwipeLayout;
    private static Tab01ListAdpter adpter;
    private int lastItem;
    private static int count;
    private static int numberPage = 1;
    private static Context mContext;


    public Tab01Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mview = inflater.inflate(R.layout.fragment_tab01, container, false);
        mAdd = (ImageButton) mview.findViewById(R.id.ib_add);
        mMainList = (ListView) mview.findViewById(R.id.tab01List_main);
        mGroupName = (TextView) mview.findViewById(R.id.tv_groupname);
        mContext = this.getActivity();
        mSwipeLayout = (SwipeRefreshLayout) mview.findViewById(R.id.id_swipe_ly);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeLayout.setSize(SwipeRefreshLayout.DEFAULT);
        mContext = getActivity();
        refresh();
        initEvent();
        return mview;
    }

    void initEvent() {
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        numberPage = 1;
                        refresh();
                        mSwipeLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        mAdd.setOnClickListener(this);
        mMainList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //下拉到空闲是，且最后一个item的数等于数据的总数时，进行更新
                if (lastItem == count && scrollState == this.SCROLL_STATE_IDLE) {
                    Log.i("TAG", "拉到最底部");

                    mHandler.sendEmptyMessage(0);

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                lastItem = firstVisibleItem + visibleItemCount;
            }
        });
        mMainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(mContext, NewsDetailActivity.class);
                Bundle b = new Bundle();
                b.putCharSequence("url", mainList.get(position).getPageUrl());
                b.putCharSequence("comment_url",mainList.get(position).getCommentUrl());
                b.putCharSequence("newsid", mainList.get(position).getNewsid());
                b.putCharSequence("newsname", mainList.get(position).getTitle());
                b.putCharSequence("commentcount",mainList.get(position).getCommentCount());
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    numberPage++;
                    getNews();

                    break;
            }
        }

        ;
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_add:
                StaticValue.isAll=false;
                if(mGroupNameStringBuffer.length()!=0) {
                    mGroupNameStringBuffer.delete(0, mGroupNameStringBuffer.length());
                }
                Intent intent = new Intent(this.getActivity(), TagActivity.class);
                startActivity(intent);
                break;

        }
    }

    private static void getNews() {
        List<ViewTag> taglist = new ArrayList<ViewTag>();
        if (StaticValue.sTagGroup != null) {
            for (ViewTag t : StaticValue.sTagGroup.values()) {
                taglist.add(new ViewTag(t.getTagid(), t.getTagname()));
                Log.i("xxxxx", t.getTagname());
            }

            ActionBase action = new GetNewsAction(taglist);
            action.getPage(numberPage).execute(new Responser() {
                @Override
                public void successfulResponse(Object param) {
                    NewsSet set = (NewsSet) param;
                    if (numberPage == 1) {
                        mainList = set.getMainList();
                        count = mainList.size();
                        adpter = new Tab01ListAdpter(mContext, mainList);
                        if (mainList.size() == 0) {
                            mainList = set.getRecommandList();
                            StaticValue.isAll=true;
                            mMainList.setAdapter(new Tab01ListAdpter(mContext, mainList));
                        } else {
                            mMainList.setAdapter(adpter);
                        }
                    } else {
                        mainList.addAll(set.getMainList());
                        adpter.notifyDataSetChanged();
                        count = mainList.size();
                    }
                    getMyFavoriteTags();
                    mGroupName.setText(mGroupNameStringBuffer);
                }

                @Override
                public void failedResponse(String error) {
                    Toast.makeText(mContext,"错误500：服务器出错",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private static void getMyFavoriteTags() {
        if (!StaticValue.sTagGroup.isEmpty()) {
            if (StaticValue.isAll ==false) {
                mGroupNameStringBuffer.delete(0,mGroupNameStringBuffer.length());
                for (String s : StaticValue.sTagGroup.keySet()) {
                    mGroupNameStringBuffer.append(s).append(",");
                }
                mGroupNameStringBuffer.deleteCharAt(mGroupNameStringBuffer.length() - 1);
            }
            else{
                mGroupNameStringBuffer.delete(0,mGroupNameStringBuffer.length());
                mGroupNameStringBuffer.append("未能找到完全匹配的新闻，推荐以下内容");
            }
        }
    }

    public static void refresh() {
        numberPage = 1;
        getNews();
    }

}
