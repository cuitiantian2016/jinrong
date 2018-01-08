package com.honglu.future.ui.msg.praisemsg;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.Constant;
import com.honglu.future.ui.circle.circledetail.CircleDetailActivity;
import com.honglu.future.ui.msg.bean.PraiseMsgListBean;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;

/**
 *  赞 -消息
 * Created by zhuaibing on 2018/1/2
 */

public class PraiseMsgActivity extends BaseActivity<PraiseMsgPresenter> implements PraiseMsgContract.View,View.OnClickListener{
    @BindView(R.id.refresh_view)
    SmartRefreshLayout mRefreshView;
    @BindView(R.id.lv_listView)
    ListView mListView;
    @BindView(R.id.tv_emptyView)
    TextView mEmptyView;

    private PraiseMsgAdapter mPraiseMsgAdapter;
    private int mRows = 0;


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
       mPresenter.init(this);
    }

    @Override
    public void loadData() {
        mTitle.setTitle(false, R.color.color_white,"赞");
        mTitle.setRightTitle(R.color.color_979899,"清空",this);
        mRefreshView.setEnableLoadmore(false);
        mListView.setEmptyView(mEmptyView);
        mPraiseMsgAdapter = new PraiseMsgAdapter(PraiseMsgActivity.this);
        mListView.setAdapter(mPraiseMsgAdapter);

        mRefreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mRows = 0;
                mPresenter.getPraiseList(SpUtil.getString(Constant.CACHE_TAG_UID),mRows);
                mRefreshView.finishRefresh();
            }
        });

        mRefreshView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mRows ++;
                mPresenter.getPraiseList(SpUtil.getString(Constant.CACHE_TAG_UID),mRows);
                mRefreshView.finishLoadmore();
            }
        });


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PraiseMsgListBean praiseMsgListBean = (PraiseMsgListBean) parent.getItemAtPosition(position);
                Intent intent = new Intent(mContext, CircleDetailActivity.class);
                intent.putExtra(CircleDetailActivity.POST_USER_KEY,praiseMsgListBean.postUserId);
                intent.putExtra(CircleDetailActivity.CIRCLEID_KEY,String.valueOf(praiseMsgListBean.circleId));
                mContext.startActivity(intent);
            }
        });

        mPresenter.getPraiseList(SpUtil.getString(Constant.CACHE_TAG_UID),mRows);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_right://清空
                if (mPraiseMsgAdapter !=null && mPraiseMsgAdapter.getCount() > 0){
                    mPresenter.getClearPraiseMsg(SpUtil.getString(Constant.CACHE_TAG_UID));
                }
                break;
        }
    }


    @Override
    public void getPraiseList(List<PraiseMsgListBean> list) {
        if (list !=null && list.size() >= 10){
            if (!mRefreshView.isEnableLoadmore()){
                mRefreshView.setEnableLoadmore(true);
            }
        }else {
            if (mRefreshView.isEnableLoadmore()){
                mRefreshView.setEnableLoadmore(false);
            }
        }

        if (list !=null && list.size() > 0){
            if (mRows > 0){
                mPraiseMsgAdapter.notifyDataChanged(true,list);
            }else {
                mPraiseMsgAdapter.notifyDataChanged(false,list);
            }
        }
    }


    /**
     * 清空赞列表
     */
    @Override
    public void clearPraiseMsg() {
        ToastUtil.show("清空成功....");
        if (mPraiseMsgAdapter !=null){
            mPraiseMsgAdapter.clearData();
        }
    }
}
