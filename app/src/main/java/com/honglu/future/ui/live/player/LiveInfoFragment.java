package com.honglu.future.ui.live.player;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.ui.live.LiveListBean;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.widget.CircleImageView;

/**
 * Created by zq on 2017/12/25.
 */

public class LiveInfoFragment extends Fragment {
    private View mView;
    private CircleImageView head;
    private TextView name;
    private TextView teacherInfo;
    private TextView liveInfo;
    private LiveListBean liveBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_live_info, null);
        head = (CircleImageView) mView.findViewById(R.id.iv_teacher_head);
        name = (TextView) mView.findViewById(R.id.tv_teacher_name);
        teacherInfo = (TextView) mView.findViewById(R.id.tv_teacher_content);
        liveInfo = (TextView) mView.findViewById(R.id.tv_live_content);
        return mView;
    }

    public void setLiveInfo(LiveListBean bean) {
        liveBean = bean;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (liveBean != null) {
                ImageUtil.display(liveBean.liveTeacherICon, head, R.mipmap.img_head);
                name.setText(liveBean.liveTeacher);
                teacherInfo.setText(liveBean.liveTeacherDes);
                liveInfo.setText(liveBean.liveDes);
            }
        }
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}
