package com.honglu.future.ui.msg.mainmsg;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 系统消息详情
 * Created by hefei on 2018/1/2
 */

public class SystemMsgDetailActivity extends BaseActivity implements MainMsgContract.View{

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_content)
    TextView tv_content;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main_msg_system_detail;
    }


    @Override
    public void showLoading(String content) {
        if (!TextUtils.isEmpty(content)) {
            App.loadingContent(SystemMsgDetailActivity.this, content);
        }
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        if (!TextUtils.isEmpty(msg)) {
            ToastUtil.show(msg);
        }
    }


    @Override
    public void initPresenter() {
    }

    @Override
    public void loadData() {
        mTitle.setTitle(false, R.color.color_white,"详情");
    }

}
