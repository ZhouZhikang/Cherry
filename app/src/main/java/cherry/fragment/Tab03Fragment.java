package cherry.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cherry.activity.LoginActivity;
import cherry.activity.MyCollectionActivity;
import cherry.activity.PersonalActivity;
import cherry.activity.StaticValue;
import cherry.activity.TagSelectActivity;
import cherry.cherry.R;


/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class Tab03Fragment extends Fragment implements View.OnClickListener{
    private LinearLayout mMyTags;
    private LinearLayout mMyCollections;
    private ImageView mLoginButton;
    public static TextView mMyInfo;


    public Tab03Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mview= inflater.inflate(R.layout.fragment_tab03, container, false);
        mMyTags=(LinearLayout)mview.findViewById(R.id.ll_custom_tag);
        mMyCollections = (LinearLayout) mview.findViewById(R.id.ll_favorite);
        mLoginButton=(ImageView)mview.findViewById(R.id.iv_login_img);
        mMyInfo=(TextView)mview.findViewById(R.id.tv_myinfo);
        if(StaticValue.sCurrentUser!=null){
            mMyInfo.setText(StaticValue.sCurrentUser.getUsername());
        }
        mMyTags.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
        mMyCollections.setOnClickListener(this);
        return mview;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_custom_tag:
                Intent intent=new Intent(this.getActivity(), TagSelectActivity.class);
                StaticValue.sFromWhere="my";
                startActivity(intent);
                break;
            case R.id.ll_favorite:
                if(StaticValue.sCurrentUser != null) {
                    Intent favorite = new Intent(this.getActivity(), MyCollectionActivity.class);
                    startActivity(favorite);
                }else {
                    Toast.makeText(getActivity(), "登陆后才能查看收藏", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_login_img:
                if(StaticValue.sCurrentUser == null) {
                    Intent login = new Intent(this.getActivity(), LoginActivity.class);
                    startActivity(login);
                }else{
                    Intent info = new Intent(this.getActivity(), PersonalActivity.class);
                    startActivity(info);
                }
                break;
        }

    }


}
