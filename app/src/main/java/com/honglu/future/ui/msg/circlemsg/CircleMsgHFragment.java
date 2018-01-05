package com.honglu.future.ui.msg.circlemsg;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonNull;
import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.config.Constant;
import com.honglu.future.ui.msg.bean.CircleMsgBean;
import com.honglu.future.ui.circle.circledetail.CircleDetailActivity;
import com.honglu.future.util.AndroidUtil;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.List;

import butterknife.BindView;

/**
 * 收到的回复
 * Created by zhuaibing on 2017/12/7
 */

public class CircleMsgHFragment extends BaseFragment<CircleMsgPresenter> implements CircleMsgContract.View{
    @BindView(R.id.refresh_view)
    SmartRefreshLayout mRefreshView;
    @BindView(R.id.lv_listView)
    ListView mListView;
    @BindView(R.id.et_input)
    EditText mInput;
    @BindView(R.id.tv_send)
    TextView mSend;
    @BindView(R.id.v_line)
    View mLine;
    @BindView(R.id.ll_input)
    LinearLayout mLLInput;
    @BindView(R.id.ll_empty_view)
    LinearLayout mEmptyView;
    @BindView(R.id.tv_hinttext)
    TextView mHintText;

    private InputMethodManager mInputMethodManager;
    private CircleMsgHFAdapter mAdapter;
    private int mRows = 0;
    private int mPosition;


    @Override
    public void initPresenter() {
       mPresenter.init(CircleMsgHFragment.this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_circle_msg;
    }

    @Override
    public void showLoading(String content) {
        if (!TextUtils.isEmpty(content)) {
            App.loadingContent(getActivity(), content);
        }
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        if (!TextUtils.isEmpty(msg)){
            ToastUtil.show(msg);
        }
    }

    @Override
    public void loadData() {
        mInputMethodManager = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        mAdapter = new CircleMsgHFAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mRefreshView.setEnableLoadmore(false);
        mLLInput.setVisibility(View.GONE);
        mLine.setVisibility(View.GONE);
        mListView.setEmptyView(mEmptyView);
        AndroidUtil.setEmojiFilter(mInput);
        mHintText.setText("暂未收到回复哦");
        mRefreshView.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mRows ++ ;
                mPresenter.getCircleRevert(mRows);
                mRefreshView.finishLoadmore();
            }
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mRows = 0;
                mPresenter.getCircleRevert(0);
                mRefreshView.finishRefresh();
            }
        });


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳转帖子详情
                CircleMsgBean circleMsgBean = (CircleMsgBean) parent.getItemAtPosition(position);
                Intent intent = new Intent(mContext, CircleDetailActivity.class);
                intent.putExtra(CircleDetailActivity.POST_USER_KEY,circleMsgBean.postUserId);
                intent.putExtra(CircleDetailActivity.CIRCLEID_KEY,String.valueOf(circleMsgBean.circleId));
                mContext.startActivity(intent);
            }
        });

        //回复
        mAdapter.setOnHuifuClickListener(new CircleMsgHFAdapter.OnHuifuClickListener() {
            @Override
            public void onHuifuClick(int position) {
                mLLInput.setVisibility(View.VISIBLE);
                mLine.setVisibility(View.VISIBLE);
                mPosition = position;
                CircleMsgBean circleMsgBean = mAdapter.getCircleBean(mPosition);
                if (circleMsgBean !=null){
                    mInput.setHint("回复:"+circleMsgBean.nickName);
                }
                mInput.setFocusable(true);
                mInput.setFocusableInTouchMode(true);
                mInput.requestFocus();
                mInputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        //发表
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contnet = mInput.getText().toString();
                if (TextUtils.isEmpty(contnet)){
                    ToastUtil.show("输入内容不能为空");
                    return;
                }
                CircleMsgBean circleMsgBean = mAdapter.getCircleBean(mPosition);
                if (circleMsgBean !=null){
                    mSend.setEnabled(false);
                    mPresenter.getCommentContent(SpUtil.getString(Constant.CACHE_TAG_UID),String.valueOf(circleMsgBean.circleId),contnet,String.valueOf(circleMsgBean.replyUserId),
                            2,SpUtil.getString(Constant.CACHE_TAG_USERNAME),circleMsgBean.postUserId,circleMsgBean.fatherCircleReplyId,circleMsgBean.layCircleReplyId);
                }
            }
        });

        mPresenter.getCircleRevert(0);
    }

    //清空
    public void getClearReply(){
        if (mPresenter !=null && mAdapter !=null && mAdapter.getData().size() > 0){
            mPresenter.getClearReply();
        }
    }

    @Override
    public void clearCircle() {
       ToastUtil.show("清空成功....");
       if (mAdapter !=null) {
           mAdapter.clearData();
       }
        mLLInput.setVisibility(View.GONE);
        mLine.setVisibility(View.GONE);
    }


    @Override
    public void circleMsgData(List<CircleMsgBean> list) {
        if (list == null){return;}
        if (list.size() >=10){
            if (!mRefreshView.isEnableLoadmore()){
                mRefreshView.setEnableLoadmore(true);
            }
        }else {
            if (mRefreshView.isEnableLoadmore()) {
                mRefreshView.setEnableLoadmore(false);
            }
        }
        if (mRows > 0){
            mAdapter.addData(list);
        }else {
            mAdapter.getData().clear();
            mAdapter.addData(list);
        }
    }


    //评论回复 error
    @Override
    public void getCommentContentError() {
        mSend.setEnabled(true);
    }

    //评论回复
    @Override
    public void getCommentContent(String circleReplyId, int replyType) {
        mSend.setEnabled(true);
        CircleMsgBean circleBean = mAdapter.getCircleBean(mPosition);
        mInput.setHint(R.string.circle_input_hint);
        mInput.setText("");
        if (circleBean !=null){
            Intent intent = new Intent(getActivity(), CircleDetailActivity.class);
            intent.putExtra(CircleDetailActivity.POST_USER_KEY,circleBean.postUserId);
            intent.putExtra(CircleDetailActivity.CIRCLEID_KEY,String.valueOf(circleBean.circleId));
            startActivity(intent);
            getActivity().finish();
        }
    }

}
