package com.honglu.future.ui.circle.morecomment;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonNull;
import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.Constant;
import com.honglu.future.ui.circle.bean.CommentBean;
import com.honglu.future.ui.circle.bean.CommentBosAllBean;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;

/**
 * 更多回复
 * Created by zhuaibing on 2018/1/3
 */

public class MoreCommentActivity extends BaseActivity<MoreCommentPresenter> implements MoreCommentContract.View{
    public final static String COMMENTBEAN_KEY = "commentbean_key";
    public final static String CIRCLEID_KEY = "circleId";
    public final static String POST_USER_KEY = "postUserId";

    public final static int REPLYTYPE_1 = 1; //评论
    public final static int REPLYTYPE_2 = 2; //回复

    @BindView(R.id.refresh_view)
    SmartRefreshLayout mRefreshView;
    @BindView(R.id.lv_listView)
    ListView mListView;
    @BindView(R.id.et_input)
    EditText mInput;
    @BindView(R.id.tv_send)
    TextView mSend;

    private int mRows;
    private CommentBosAllBean mAllBean;
    private CommentBean mCommentBean;
    private MoreCommentAdapter mCommentAdapter;

    private String mCircleId;
    public String mPostUserId;



    @Override
    public int getLayoutId() {
        return R.layout.activity_more_comment;
    }


    @Override
    public void showLoading(String content) {
        if (!TextUtils.isEmpty(content)) {
            App.loadingContent(MoreCommentActivity.this, content);
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
        mCommentBean = (CommentBean) getIntent().getSerializableExtra(COMMENTBEAN_KEY);
        mCircleId = getIntent().getStringExtra(CIRCLEID_KEY);
        mPostUserId = getIntent().getStringExtra(POST_USER_KEY);
        mRefreshView.setEnableLoadmore(false);
        String title = mCommentBean !=null && mCommentBean.layComment !=null ? mCommentBean.layComment.count+"条回复" : "0条回复";
        mTitle.setTitle(false, R.color.color_white,title);

        if (mCommentBean !=null) {
            mAllBean = new CommentBosAllBean();
            mAllBean.avatarPic = mCommentBean.avatarPic;
            mAllBean.nickName = mCommentBean.nickName;
            mAllBean.createTime = mCommentBean.createTime;
            mAllBean.replyContent = mCommentBean.replyContent;
            mAllBean.circleReplyId = String.valueOf(mCommentBean.circleReplyId);
            mAllBean.fatherCircleReplyId = String.valueOf(mCommentBean.circleReplyId);
            mAllBean.layCircleReplyId = String.valueOf(mCommentBean.circleReplyId);
            mAllBean.replyUserId = mCommentBean.replyUserId;
            mAllBean.pType = mCommentBean.replyUserId;
        }
        mCommentAdapter = new MoreCommentAdapter(MoreCommentActivity.this,mAllBean);
        mListView.setAdapter(mCommentAdapter);

        mRefreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (mCommentBean !=null ){
                    mRows = 0;
                    mPresenter.getLayComment(SpUtil.getString(Constant.CACHE_TAG_UID),String.valueOf(mCommentBean.circleReplyId),mRows);
                }
                mRefreshView.finishRefresh();
            }
        });

        mRefreshView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (mCommentBean !=null){
                    mRows ++;
                    mPresenter.getLayComment(SpUtil.getString(Constant.CACHE_TAG_UID),String.valueOf(mCommentBean.circleReplyId),mRows);
                }
                mRefreshView.finishLoadmore();
            }
        });


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListView.getHeaderViewsCount() > 0 && position == 0){
                    return;
                }
                mAllBean  = (CommentBosAllBean) parent.getItemAtPosition(position);
                if (TextUtils.equals(mAllBean.pType,mAllBean.replyUserId)){
                    mInput.setHint("回复评论");
                }else {
                    mInput.setHint("回复:"+mAllBean.nickName);
                }
            }
        });


        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (mAllBean !=null){
                   String content = geInputText(mInput);
                   if (TextUtils.isEmpty(content)) {
                       ToastUtil.show(getString(R.string.content_no_null));
                       return;
                   }
                   mSend.setEnabled(false);
                   mPresenter.getCommentContent(SpUtil.getString(Constant.CACHE_TAG_UID), mCircleId, content, mAllBean.replyUserId,
                           REPLYTYPE_2, SpUtil.getString(Constant.CACHE_TAG_USERNAME), mPostUserId,mAllBean.fatherCircleReplyId,mAllBean.layCircleReplyId);
               }
            }
        });


        if (mCommentBean !=null){
            mPresenter.getLayComment(SpUtil.getString(Constant.CACHE_TAG_UID),String.valueOf(mCommentBean.circleReplyId),mRows);
        }
    }



    @Override
    public void getLayComment(List<CommentBosAllBean> list) {

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
                mCommentAdapter.notifyDataChanged(true,list);
            }else {
                mCommentAdapter.notifyDataChanged(false,list);
            }
        }
    }


    @Override
    public void getCommentContentError() {
         mSend.setEnabled(true);
    }

    @Override
    public void getCommentContent(JsonNull jsonNull, int replyType) {
        mSend.setEnabled(true);
        ToastUtil.show("回复成功");
        mRows = 0;
        mPresenter.getLayComment(SpUtil.getString(Constant.CACHE_TAG_UID),String.valueOf(mCommentBean.circleReplyId),mRows);
    }

    public String geInputText(EditText mInput) {
        return mInput.getText() != null && !TextUtils.isEmpty(mInput.getText().toString()) ? mInput.getText().toString().trim() : "";
    }
}
