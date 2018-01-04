package com.honglu.future.ui.msg.mainmsg;

import android.text.TextUtils;
import android.view.View;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.util.ToastUtil;

import butterknife.OnClick;

/**
 * 消息中心main
 * Created by zhuaibing on 2018/1/2
 */

public class MainMsgActivity extends BaseActivity<MainMsgPresenter> implements MainMsgContract.View{


    @Override
    public int getLayoutId() {
        return R.layout.activity_main_msg;
    }


    @Override
    public void showLoading(String content) {
        if (!TextUtils.isEmpty(content)) {
            App.loadingContent(MainMsgActivity.this, content);
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
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        mTitle.setTitle(false, R.color.color_white,"消息中心");
    }

    @OnClick({R.id.rl_msg_trade, R.id.rl_msg_system})
    public void onClick(View v){
        int id = v.getId();
        if (R.id.rl_msg_system== id){
            startActivity(SystemMsgActivity.class);
        }else if (R.id.rl_msg_trade == id){
            startActivity(TradeMsgActivity.class);
        }
    }
}
