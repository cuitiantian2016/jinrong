package com.honglu.future.ui.usercenter.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.Constant;
import com.honglu.future.events.RefreshUIEvent;
import com.honglu.future.events.UIBaseEvent;
import com.honglu.future.ui.usercenter.contract.ModifyNicknameContract;
import com.honglu.future.ui.usercenter.presenter.ModifyNicknamePresenter;
import com.honglu.future.util.SpUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static com.honglu.future.util.ToastUtil.showToast;

/**
 * Created by zq on 2017/10/31.
 */

public class ModifyNicknameActivity extends BaseActivity<ModifyNicknamePresenter> implements
        ModifyNicknameContract.View {
    @BindView(R.id.tv_back)
    ImageView mIvBack;
    @BindView(R.id.et_nickname)
    EditText mNickName;

    @Override
    public void showLoading(String content) {
        if (!TextUtils.isEmpty(content)) {
            App.loadingContent(this, content);
        }
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        showToast(msg);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_modify_nickname;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        initViews();
    }

    private void initViews() {
        mIvBack.setVisibility(View.VISIBLE);
        mTitle.setTitle(false, R.color.white, "修改昵称");
        if (!TextUtils.isEmpty(SpUtil.getString(Constant.CACHE_TAG_USERNAME))) {
            mNickName.setText(SpUtil.getString(Constant.CACHE_TAG_USERNAME));
        }
    }

    @OnClick({R.id.tv_back, R.id.btn_save})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.btn_save:
                if (TextUtils.isEmpty(mNickName.getText().toString().trim())) {
                    showToast("昵称不能为空");
                    return;
                }
                mPresenter.updateNickName(mNickName.getText().toString(), SpUtil.getString(Constant.CACHE_TAG_UID));
                break;
        }
    }

    @Override
    public void updateNickNameSuccess() {
        showToast("修改昵称成功");
        SpUtil.putString(Constant.CACHE_TAG_USERNAME, mNickName.getText().toString());
        EventBus.getDefault().post(new RefreshUIEvent(UIBaseEvent.EVENT_UPDATE_NICK_NAME));
    }

}
