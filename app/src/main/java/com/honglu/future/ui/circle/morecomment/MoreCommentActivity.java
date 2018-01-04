package com.honglu.future.ui.circle.morecomment;

import android.content.Context;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.ConfigUtil;
import com.honglu.future.config.Constant;
import com.honglu.future.events.BBSLayCommentEvent;
import com.honglu.future.ui.circle.bean.CommentBean;
import com.honglu.future.ui.circle.bean.CommentBosAllBean;
import com.honglu.future.util.AndroidUtil;
import com.honglu.future.util.ShareUtils;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

/**
 * 更多回复
 * Created by zhuaibing on 2018/1/3
 */

public class MoreCommentActivity extends BaseActivity<MoreCommentPresenter> implements MoreCommentContract.View,View.OnClickListener{
    public final static String COMMENTBOSALLBEAN_KEY = "commentbosallbean";
    public final static String CIRCLEID_KEY = "circleId";
    public final static String POST_USER_KEY = "postUserId";
    public final static String CONTENT_KEY = "content";

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

    private String mCircleReplyId;
    private String mCircleId;
    private String mPostUserId;
    private String mContnet;

    private CommentBosAllBean mAllBean;
    private InputMethodManager mInputMethodManager;
    private MoreCommentAdapter mCommentAdapter;
    private int mRows;
    private boolean mIsCommentRefresh = true;


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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void loadData() {
        mInputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        EventBus.getDefault().register(this);
        mAllBean = (CommentBosAllBean) getIntent().getSerializableExtra(COMMENTBOSALLBEAN_KEY);
        mCircleReplyId = mAllBean !=null ? mAllBean.circleReplyId : "";
        mCircleId = getIntent().getStringExtra(CIRCLEID_KEY);
        mPostUserId = getIntent().getStringExtra(POST_USER_KEY);
        mContnet = getIntent().getStringExtra(CONTENT_KEY);

        AndroidUtil.setEmojiFilter(mInput);
        mRefreshView.setEnableLoadmore(false);
        mTitle.setTitle(false, R.color.color_white,"回复详情");
        mTitle.setRightTitle(R.mipmap.ic_share,this);
        mCommentAdapter = new MoreCommentAdapter(MoreCommentActivity.this,mAllBean);
        mListView.setAdapter(mCommentAdapter);

      mRefreshView.setOnRefreshListener(new OnRefreshListener() {
          @Override
          public void onRefresh(RefreshLayout refreshlayout) {
              if (!TextUtils.isEmpty(mCircleReplyId)){
                  mRows = 0;
                  mPresenter.getLayComment(SpUtil.getString(Constant.CACHE_TAG_UID),String.valueOf(mCircleReplyId),mRows);
              }
              mRefreshView.finishRefresh();
          }
      });

        mRefreshView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (!TextUtils.isEmpty(mCircleReplyId)){
                    mRows ++;
                    mPresenter.getLayComment(SpUtil.getString(Constant.CACHE_TAG_UID),String.valueOf(mCircleReplyId),mRows);
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
                mInput.setFocusable(true);
                mInput.setFocusableInTouchMode(true);
                mInput.requestFocus();
                mInputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        mSend.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsCommentRefresh && !TextUtils.isEmpty(mCircleReplyId)){
            mPresenter.getLayComment(SpUtil.getString(Constant.CACHE_TAG_UID),mCircleReplyId,mRows);
            mIsCommentRefresh = false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_send:
                if (mAllBean !=null){
                    String content = geInputText(mInput);
                    if (TextUtils.isEmpty(content)) {
                        ToastUtil.show(getString(R.string.content_no_null));
                        return;
                    }
                    mSend.setEnabled(false);
                    mPresenter.getCommentContent(SpUtil.getString(Constant.CACHE_TAG_UID), mCircleId, content, mAllBean.replyUserId,
                            REPLYTYPE_2, SpUtil.getString(Constant.CACHE_TAG_USERNAME), mPostUserId,mCircleReplyId,mAllBean.layCircleReplyId);
                }
                break;
            case R.id.tv_right:
                if (!TextUtils.isEmpty(mContnet)){
                    String title = mContnet.length() >=23 ? mContnet.substring(0,22)+"..." : mContnet;
                    ShareUtils.getIntance().share(this, "", ConfigUtil.baseH5Url+"connector/oxstallShare?userId="+SpUtil.getString(Constant.CACHE_TAG_UID)+"&circleId="+mCircleId+"&postUserId="+mPostUserId, title, "投资达人喜欢的社区");
                }
                break;
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
    public void getCommentContent(String circleReplyId, int replyType) {
        mSend.setEnabled(true);
        if (!TextUtils.isEmpty(circleReplyId) && !TextUtils.isEmpty(mCircleReplyId)){
            String content = geInputText(mInput);
            EventBus.getDefault().post(new BBSLayCommentEvent(mAllBean.nickName,mCircleReplyId,circleReplyId,content));
        }
        mInput.setText("");
        if (mInputMethodManager.isActive()) {
            IBinder ibinder = mInput.getWindowToken();
            if (ibinder != null) {
                mInputMethodManager.hideSoftInputFromWindow(ibinder, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        mRows = 0;
        mPresenter.getLayComment(SpUtil.getString(Constant.CACHE_TAG_UID),mCircleReplyId,mRows);
        ToastUtil.show("回复成功");
    }

    public String geInputText(EditText mInput) {
        return mInput.getText() != null && !TextUtils.isEmpty(mInput.getText().toString()) ? mInput.getText().toString().trim() : "";
    }


    /**
     * 回复监听
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BBSLayCommentEvent event) {
       if ( TextUtils.isEmpty(mCircleReplyId) && TextUtils.equals(event.fatherCircleReplyId,mCircleReplyId)){
           mIsCommentRefresh = true;
       }
    }

}
