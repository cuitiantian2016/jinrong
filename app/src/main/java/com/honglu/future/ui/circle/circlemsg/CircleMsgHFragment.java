package com.honglu.future.ui.circle.circlemsg;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.ui.circle.bean.CircleMsgBean;
import com.honglu.future.util.ToastUtil;
import com.honglu.future.widget.recycler.DividerItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.List;

import butterknife.BindView;

/**
 * 收到的评论
 * Created by zhuaibing on 2017/12/7
 */

public class CircleMsgHFragment extends BaseFragment<CircleMsgPresenter> implements CircleMsgContract.View{
    @BindView(R.id.refresh_view)
    SmartRefreshLayout mRefreshView;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.et_input)
    EditText mInput;
    @BindView(R.id.tv_send)
    TextView mSend;

    private CircleMsgHFAdapter mAdapter;
    private int mRows = 0;
    private int mReplyUserId = -1;
    private int mCircleId = -1;

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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new CircleMsgHFAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRefreshView.setEnableLoadmore(false);
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

        //回复
        mAdapter.setOnHuifuClickListener(new CircleMsgHFAdapter.OnHuifuClickListener() {
            @Override
            public void onHuifuClick(String name, int replyUserId, int circleId) {
                mReplyUserId = replyUserId;
                mCircleId = circleId;
                mInput.setHint("回复:"+name);
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
               if (mCircleId == -1 || mReplyUserId == -1){
                   CircleMsgBean circleBean = mAdapter.getCircleBean();
                   if (circleBean  !=null){

                   }
               }else {

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

}
