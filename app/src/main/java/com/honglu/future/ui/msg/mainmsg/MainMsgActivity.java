package com.honglu.future.ui.msg.mainmsg;

import android.text.TextUtils;
import android.view.View;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.Constant;
import com.honglu.future.ui.msg.bean.HasUnreadMsgBean;
import com.honglu.future.ui.msg.circlemsg.CircleMsgActivity;
import com.honglu.future.ui.msg.praisemsg.PraiseMsgActivity;
import com.honglu.future.util.SpUtil;
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
                mPresenter.getHasUnreadMsg(SpUtil.getString(Constant.CACHE_TAG_UID));
                mRefreshView.finishRefresh();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getHasUnreadMsg(SpUtil.getString(Constant.CACHE_TAG_UID));
    }

    @OnClick({R.id.ll_msg_trade, R.id.ll_msg_system,R.id.ll_msg_comment,R.id.ll_msg_praise})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_msg_system://系统通知
                startActivity(SystemMsgActivity.class);
                mMsgSystemRed.setVisibility(View.INVISIBLE);
                break;
            case R.id.ll_msg_trade://交易提醒
                startActivity(TradeMsgActivity.class);
                mMsgTradeRed.setVisibility(View.INVISIBLE);
                break;
            case R.id.ll_msg_comment: //评论回复
                startActivity(CircleMsgActivity.class);
                mMsgCommentRed.setVisibility(View.INVISIBLE);
                break;
            case R.id.ll_msg_praise: //赞
                startActivity(PraiseMsgActivity.class);
                mMsgPraiseRed.setVisibility(View.INVISIBLE);
                break;
        }
    }




    @Override
    public void hasUnreadMsg(HasUnreadMsgBean bean) {
         if (bean !=null){

             //交易红点
             if (bean.transMsgNoticeFlag){
                 mMsgTradeRed.setVisibility(View.VISIBLE);
             }else {
                 mMsgTradeRed.setVisibility(View.INVISIBLE);
             }

             //评论回复红点
             if (bean.commentAndReplyMsgNoticeFlag){
                 mMsgCommentRed.setVisibility(View.VISIBLE);
             }else {
                 mMsgCommentRed.setVisibility(View.INVISIBLE);
             }

             //点赞红点
             if (bean.praiseMsgNoticeFlag){
                 mMsgPraiseRed.setVisibility(View.VISIBLE);
             }else {
                 mMsgPraiseRed.setVisibility(View.INVISIBLE);
             }

             //系统通知
             if (bean.sysMsgNoticeFlag){
                 mMsgSystemRed.setVisibility(View.VISIBLE);
             }else {
                 mMsgSystemRed.setVisibility(View.INVISIBLE);
             }
         }
    }
}
