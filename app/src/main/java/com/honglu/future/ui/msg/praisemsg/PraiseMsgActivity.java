package com.honglu.future.ui.msg.praisemsg;

import android.text.TextUtils;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.util.ToastUtil;

/**
 *  赞 -消息
 * Created by zhuaibing on 2018/1/2
 */

public class PraiseMsgActivity extends BaseActivity<PraiseMsgPresenter> implements PraiseMsgContract.View{
    @Override
    public int getLayoutId() {
        return R.layout.activity_praise_msg;
    }


    @Override
    public void showLoading(String content) {
        if (!TextUtils.isEmpty(content)) {
            App.loadingContent(PraiseMsgActivity.this, content);
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

    }
}
