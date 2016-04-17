package cherry.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsy.android.grid.StaggeredGridView;

import java.util.List;

import cherry.action.LatestNewsAction;
import cherry.action.SearchNewsAction;
import cherry.action.model.ViewNews;
import cherry.action.util.ActionBase;
import cherry.action.util.Responser;
import cherry.activity.NewsDetailActivity;
import cherry.adpter.GridAdpter;
import cherry.cherry.R;


/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class Tab02Fragment extends Fragment {

    private EditText mSearchText;
    private ImageButton mSearchBtn;
    private StaggeredGridView mGridView;
    private List<ViewNews> mList;
    private String mKeyWord;
    private Context mContext;
    private GridAdpter mAdpter;
    private ImageView mEditCancel;
    private SwipeRefreshLayout mSwipeLayout;
    private int lastItem;
    private static int count;
    private static int numberPage = 1;

    public Tab02Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mview = inflater.inflate(R.layout.fragment_tab02, container, false);
        mSearchText = (EditText) mview.findViewById(R.id.et_search);
        mSearchBtn = (ImageButton) mview.findViewById(R.id.search_button);
        mGridView = (StaggeredGridView) mview.findViewById(R.id.grid_view);
        mEditCancel=(ImageView)mview.findViewById(R.id.btn_clear_search_text);
        mSwipeLayout = (SwipeRefreshLayout) mview.findViewById(R.id.id_swipe_tab02);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_red_light,
                android.R.color.holo_green_light, android.R.color.holo_orange_light);
        mContext = this.getActivity();
        numberPage = 1;
        initEvent();
        GetNews();
        SearchNews();
        GetNewsDetails();

        return mview;
    }

    void initEvent() {
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH){
                    mKeyWord = mSearchText.getText().toString();
                    Log.i("查询关键词", mKeyWord);
                    ActionBase action = new SearchNewsAction(mKeyWord);
                    action.getPage(1).execute(new Responser() {
                        @Override
                        public void successfulResponse(Object param) {
                            mList = (List<ViewNews>) param;
//                        mGridView.setAdapter(new GridAdpter(getActivity(), mList));
                            mAdpter = new GridAdpter(getActivity(), mList);
                            mGridView.setAdapter(mAdpter);
                        }

                        @Override
                        public void failedResponse(String error) {
                            Log.e("searchNewsActionError", error);
                        }
                    });
                }
                return false;
            }
        });
        mSearchText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                   mEditCancel.setVisibility(View.VISIBLE);
                } else {
                    mEditCancel.setVisibility(View.GONE);
                }
            }
        });
        mEditCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchText.setText("");
                GetNews();
            }
        });
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        numberPage = 1;
                        GetNews();
                        mSwipeLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                Log.i("@@@@@@@@", String.valueOf(lastItem));
            }
        });

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    numberPage++;
                    GetNews();
                    break;
            }
        }

        ;
    };


    private void GetNews() {
        ActionBase action = new LatestNewsAction();
        action.getPage(numberPage).execute(new Responser() {
            @Override
            public void successfulResponse(Object param) {
                if(numberPage==1) {
                    mList = (List<ViewNews>) param;
                    count = mList.size();
                    mAdpter = new GridAdpter(getActivity(),mList);
                    mGridView.setAdapter(mAdpter);
                }
                else{
                    List<ViewNews> list=(List<ViewNews>) param;
                    mList.addAll(list);
                    mAdpter.notifyDataSetChanged();
                    count=mList.size();
                }

            }

            @Override
            public void failedResponse(String error) {
                Log.e("failedResponse", error);
            }
        });
    }

    private void SearchNews() {
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mKeyWord = mSearchText.getText().toString();
                Log.i("查询关键词", mKeyWord);
                ActionBase action = new SearchNewsAction(mKeyWord);
                action.getPage(1).execute(new Responser() {
                    @Override
                    public void successfulResponse(Object param) {
                        mList = (List<ViewNews>) param;
//                        mGridView.setAdapter(new GridAdpter(getActivity(), mList));
                        mAdpter = new GridAdpter(getActivity(),mList);
                        mGridView.setAdapter(mAdpter);
                    }

                    @Override
                    public void failedResponse(String error) {
                        Log.e("searchNewsActionError", error);
                    }
                });
            }
        });
    }

    public void GetNewsDetails() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity().getApplicationContext(), NewsDetailActivity.class);
                Bundle b = new Bundle();
                b.putCharSequence("url", mList.get(position).getPageUrl());
                b.putCharSequence("newsid",mList.get(position).getNewsid());
                b.putCharSequence("comment_url",mList.get(position).getCommentUrl());
                b.putCharSequence("newsname", mList.get(position).getTitle());
                b.putCharSequence("commentcount",mList.get(position).getCommentCount());
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

}