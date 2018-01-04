package com.honglu.future.ui.msg.mainmsg;

import android.text.TextUtils;
import android.view.View;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.ui.msg.circlemsg.CircleMsgActivity;
import com.honglu.future.ui.msg.praisemsg.PraiseMsgActivity;
import com.honglu.future.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 消息中心main
 * Created by zhuaibing on 2018/1/2
 */

public class MainMsgActivity extends BaseActivity<MainMsgPresenter> implements MainMsgContract.View {

    @BindView(R.id.v_msg_trade_red)
    View mMsgTradeRed;
    @BindView(R.id.v_msg_comment_red)
    View mMsgCommentRed;
    @BindView(R.id.v_msg_praise_red)
    View mMsgPraiseRed;
    @BindView(R.id.v_msg_system_red)
    View mMsgSystemRed;
    @BindView(R.id.refresh_view)
    SmartRefreshLayout mRefreshView;

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
        mTitle.setTitle(false, R.color.color_white, "消息中心");
        mRefreshView.setEnableLoadmore(false);
        mRefreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                mRefreshView.finishRefresh();
            }
        });
    }

    @OnClick({R.id.ll_msg_trade, R.id.ll_msg_system,R.id.ll_msg_comment,R.id.ll_msg_praise})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_msg_system://系统通知
                startActivity(SystemMsgActivity.class);
                break;
            case R.id.ll_msg_trade://交易提醒
                startActivity(TradeMsgActivity.class);
                break;
            case R.id.ll_msg_comment: //评论回复
                startActivity(CircleMsgActivity.class);
                break;
            case R.id.ll_msg_praise: //赞
                startActivity(PraiseMsgActivity.class);
                break;
        }
    }
}
