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

public class MainPointFragment extends Fragment {
    private View mView;
    private TextView mTvMainText;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_main_point, null);
        mTvMainText = (TextView) mView.findViewById(R.id.main_text);
        return mView;
    }

    public void setMainPoint(String text){
        mTvMainText.setText(text);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
//            Log.i("testVideo","44444444444");
//            mTvMainText.setText(SpUtil.getString("getDescription"));
        }
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}
