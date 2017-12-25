package com.honglu.future.ui.live.player;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.util.SpUtil;

/**
 * Created by zq on 2017/12/25.
 */

public class LiveInfoFragment extends Fragment {
    private View mView;
    private TextView mTvLiveInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_live_info, null);
        mTvLiveInfo = (TextView) mView.findViewById(R.id.tv_live_info);
        return mView;
    }

    public void setLiveInfo(String text){
        mTvLiveInfo.setText(text);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
//            Log.i("testVideo","555555555555555");
//            mTvLiveInfo.setText(SpUtil.getString("getSpeakerInfo"));
        }
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}
