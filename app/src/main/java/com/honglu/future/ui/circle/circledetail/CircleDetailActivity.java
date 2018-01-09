package com.honglu.future.ui.circle.circledetail;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.ConfigUtil;
import com.honglu.future.config.Constant;
import com.honglu.future.dialog.areward.ArewardDialog;
import com.honglu.future.events.BBSArewardEvent;
import com.honglu.future.events.BBSCommentContentEvent;
import com.honglu.future.events.BBSCommentEvent;
import com.honglu.future.events.BBSFlownEvent;
import com.honglu.future.events.BBSLayCommentEvent;
import com.honglu.future.events.BBSPraiseEvent;
import com.honglu.future.ui.circle.bean.CircleDetailBean;
import com.honglu.future.ui.circle.bean.CommentAllBean;
import com.honglu.future.ui.circle.bean.CommentBean;
import com.honglu.future.ui.circle.bean.CommentBosAllBean;
import com.honglu.future.ui.circle.bean.LayCommentBean;
import com.honglu.future.ui.circle.bean.LayCommentListBean;
import com.honglu.future.ui.circle.bean.PraiseListBean;
import com.honglu.future.ui.circle.circlemine.CircleMineActivity;
import com.honglu.future.ui.circle.morecomment.MoreCommentActivity;
import com.honglu.future.util.AndroidUtil;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.util.ShareUtils;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;
import com.honglu.future.widget.CircleImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 帖子详情
 * Created by zhuaibing on 2017/12/7
 */
@Route(path = "/circle/Detail")
public class CircleDetailActivity extends BaseActivity<CircleDetailPresenter> implements CircleDetailContract.View, View.OnClickListener {
    public final static String POST_USER_KEY = "postUserId";
    public final static String CIRCLEID_KEY = "circleId";
    public final static String CIRCLEREPLYID_KEY = "circleReplyId";

    public final static String COMMENT_ALL = "comment_all"; //全部
    public final static String COMMENT_AUTH = "comment_auth"; //楼主

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


    private CircleImageView mCivHead;
    private TextView mName;
    private TextView mUserLabel;
    private TextView mTime;
    private TextView mFollow;
    private TextView mContnet;
    private TextView mSeeZhuanTi;
    private LinearLayout mImgsLinear;
    private ImageView mImgSupport;
    private TextView mTextSupport;
    private LinearLayout mSupportLinear;
    private ImageView mImgAreward;
    private TextView mTextAreward;
    private TextView mComment;
    private View mCommentLine;
    private TextView mSeeOwner;
    private View mSeeOwnerLine;
    private View mFooterView;

    private List<CommentBean> mCommentList = new ArrayList<>();
    private List<CommentBean> mCommentAuthList = new ArrayList<>();
    private CircleDetailBean mCircleDetailBean;
    private CircleDetailAdapter mAdapter;
    private CircleDetailHelper mHelper;
    private ArewardDialog mArewardDialog;
    private String mCommentType = COMMENT_ALL;

    @Autowired(name = "postUserId")
    public String mPostUserId;
    @Autowired(name = "circleId")
    public String mCircleId;
    @Autowired(name = "circleReplyId")
    public String circleReplyId;

    private int mCommentRows = 0;
    private int mCommentAuthRows = 0;
    private boolean mCommentMore = false;
    private boolean mCommentAuthMore = false;

    private int mCommentCountAll = 0;
    private int mCommentCountAuth = 0;

    private boolean mIsBBSPraise = false;//标记当前页点赞
    private boolean mIsBBSFlown = false; //标记当前页
    private boolean mIsCommentAll = false; //false 标记下次tab切换时要请求接口
    private boolean mIsCommentAuth = false; //false 标记下次tab切换时要请求接口

    private String mBeReplyUserId; //被回复人id
    private String mFatherCircleReplyId; //对一级回复时主id;
    private String mLayCircleReplyId; //对层级回复时id(当为一级时 传mFatherCircleReplyId);
    private String mNickName;

    @Override
    public void initPresenter() {
        mPresenter.init(CircleDetailActivity.this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_circle_detail;
    }

    public String getCommentType() {
        return mCommentType;
    }

    @Override
    public void showLoading(String content) {
        if (!TextUtils.isEmpty(content)) {
            App.loadingContent(CircleDetailActivity.this, content);
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

    //全部评论列表
    public void getCirleComment(int mCommentRows ,String circleReplyId){
        mPresenter.getCirleComment(SpUtil.getString(Constant.CACHE_TAG_UID), mCircleId, mPostUserId, mCommentRows, circleReplyId);
    }

    //只看楼主评论列表
    public void getCirleCommentAuth(int mCommentAuthRows){
        mPresenter.getCirleCommentAuth(SpUtil.getString(Constant.CACHE_TAG_UID), mCircleId, mPostUserId, mCommentAuthRows);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mArewardDialog !=null){
            mArewardDialog.onDestroy();
        }
        if (Util.isOnMainThread() && !this.isFinishing()) {
            Glide.with(this).pauseRequests();
        }
    }


    @Override
    public void loadData() {
        EventBus.getDefault().register(this);
        if (getIntent().hasExtra(POST_USER_KEY)) {
            mPostUserId = getIntent().getStringExtra(POST_USER_KEY);
        }
        if (getIntent().hasExtra(CIRCLEID_KEY)) {
            mCircleId = getIntent().getStringExtra(CIRCLEID_KEY);
        }
        if (getIntent().hasExtra(CIRCLEREPLYID_KEY)) { //目前协议跳转时需要这个值 其他时候传空
            circleReplyId = getIntent().getStringExtra(CIRCLEREPLYID_KEY);
        }
        mArewardDialog = new ArewardDialog(CircleDetailActivity.this);
        mHelper = new CircleDetailHelper(CircleDetailActivity.this);
        mTitle.setTitle(false, R.color.color_white, "详情");
        mTitle.setRightTitle(R.mipmap.ic_share, this);
        View headView = View.inflate(CircleDetailActivity.this, R.layout.layout_circle_detail_head, null);
        mFooterView = View.inflate(CircleDetailActivity.this, R.layout.layout_circle_detail_footer, null);
        mCivHead = (CircleImageView) headView.findViewById(R.id.civ_head);
        mName = (TextView) headView.findViewById(R.id.tv_name);
        mUserLabel = (TextView) headView.findViewById(R.id.tv_user_label);
        mTime = (TextView) headView.findViewById(R.id.tv_time);
        mFollow = (TextView) headView.findViewById(R.id.tv_follow);
        mContnet = (TextView) headView.findViewById(R.id.tv_contnet);
        mSeeZhuanTi = (TextView) headView.findViewById(R.id.tv_seezhuanti);
        mSeeZhuanTi.setVisibility(View.GONE);
        mImgsLinear = (LinearLayout) headView.findViewById(R.id.imgs_linear);
        mImgSupport = (ImageView) headView.findViewById(R.id.iv_support);
        mImgSupport.setEnabled(false);
        mTextSupport = (TextView) headView.findViewById(R.id.tv_support);
        mSupportLinear = (LinearLayout) headView.findViewById(R.id.support_linear);
        mImgAreward = (ImageView) headView.findViewById(R.id.iv_areward);
        mTextAreward = (TextView) headView.findViewById(R.id.tv_areward);
        mComment = (TextView) headView.findViewById(R.id.tv_comment);
        mCommentLine = headView.findViewById(R.id.v_comment_line);
        mSeeOwner = (TextView) headView.findViewById(R.id.tv_see_owner);
        mSeeOwnerLine = headView.findViewById(R.id.v_see_owner_line);

        mComment.setSelected(true);
        mCommentLine.setVisibility(View.VISIBLE);
        mSeeOwner.setSelected(false);
        mSeeOwnerLine.setVisibility(View.INVISIBLE);
        AndroidUtil.setEmojiFilter(mInput);

        mListView.addHeaderView(headView);
        mAdapter = new CircleDetailAdapter(CircleDetailActivity.this, mPostUserId);
        mListView.setAdapter(mAdapter);

        mCivHead.setOnClickListener(this);
        mFollow.setOnClickListener(this);
        mImgSupport.setOnClickListener(this);
        mComment.setOnClickListener(this);
        mSeeOwner.setOnClickListener(this);
        mSend.setOnClickListener(this);
        mSupportLinear.setOnClickListener(this);
        mImgAreward.setOnClickListener(this);

        mRefreshView.setEnableLoadmore(false);
        setListener();
        mPresenter.getDetailBean(SpUtil.getString(Constant.CACHE_TAG_UID), mCircleId);
        getCirleComment(mCommentRows, circleReplyId);
    }

    private void setListener() {
        //下拉加载
        mRefreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPresenter.getDetailBean(SpUtil.getString(Constant.CACHE_TAG_UID), mCircleId);
                if (COMMENT_ALL.equals(mCommentType)) {
                    mCommentRows = 0;
                    getCirleComment(mCommentRows, "");
                } else {
                    mCommentAuthRows = 0;
                    getCirleCommentAuth(mCommentAuthRows);
                }
                mRefreshView.finishRefresh();
            }
        });

        //上拉加载
        mRefreshView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (COMMENT_ALL.equals(mCommentType)) {
                    mCommentRows++;
                    getCirleComment(mCommentRows, "");
                } else {
                    mCommentAuthRows++;
                    getCirleCommentAuth(mCommentAuthRows);
                }
                mRefreshView.finishLoadmore();
            }
        });

        mArewardDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mHelper.hideSoftInputFromWindow(mInput);
            }
        });
    }


    private void setCommentDataInit(){
        mBeReplyUserId = "";
        mFatherCircleReplyId = "";
        mLayCircleReplyId = "";
    }

    private boolean isCommentData(){
        return !TextUtils.isEmpty(mBeReplyUserId) && !TextUtils.isEmpty(mFatherCircleReplyId) && !TextUtils.isEmpty(mLayCircleReplyId) ? true : false;
    }

    public void onItemClick(String name,String replyUserId,String fatherCircleReplyId,String layCircleReplyId){
        if (COMMENT_AUTH.equals(getCommentType())) {
            setCommentDataInit();
            mInput.setHint(getString(R.string.circle_input_hint));
        }else {
            mNickName = name;
            mBeReplyUserId = replyUserId;
            mFatherCircleReplyId = fatherCircleReplyId;
            mLayCircleReplyId = layCircleReplyId;
            mInput.setHint("回复：" + name);
        }
        mHelper.toggleSoftInput(mInput);
    }

    /**
     * 跳转到更多回复
     * @param mCommentBean
     */
    public void startMoreCommentActivity(CommentBean mCommentBean){
        CommentBosAllBean mAllBean = new CommentBosAllBean();
        mAllBean.avatarPic = mCommentBean.avatarPic;
        mAllBean.nickName = mCommentBean.nickName;
        mAllBean.createTime = mCommentBean.createTime;
        mAllBean.replyContent = mCommentBean.replyContent;
        mAllBean.circleReplyId = String.valueOf(mCommentBean.circleReplyId);
        mAllBean.fatherCircleReplyId = String.valueOf(mCommentBean.circleReplyId);
        mAllBean.layCircleReplyId = String.valueOf(mCommentBean.circleReplyId);
        mAllBean.replyUserId = mCommentBean.replyUserId;
        mAllBean.pType = mCommentBean.replyUserId;

        Intent intent = new Intent(mActivity, MoreCommentActivity.class);
        intent.putExtra(MoreCommentActivity.COMMENTBOSALLBEAN_KEY,mAllBean);
        intent.putExtra(MoreCommentActivity.CIRCLEID_KEY,mCircleId);
        intent.putExtra(MoreCommentActivity.POST_USER_KEY,mPostUserId);
        intent.putExtra(MoreCommentActivity.CONTENT_KEY,mHelper.getText(mContnet));
        mActivity.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_send: //发表
                String content = mHelper.geInputText(mInput);
                if (TextUtils.isEmpty(content)) {
                    ToastUtil.show(getString(R.string.content_no_null));
                    return;
                }
                mSend.setEnabled(false);
                if (COMMENT_AUTH.equals(getCommentType())) {
                    mPresenter.getCommentContent(SpUtil.getString(Constant.CACHE_TAG_UID), mCircleId, content, mPostUserId, REPLYTYPE_1, SpUtil.getString(Constant.CACHE_TAG_USERNAME), mPostUserId,"","", "");
                } else {
                    if (isCommentData()) {
                        mPresenter.getCommentContent(SpUtil.getString(Constant.CACHE_TAG_UID), mCircleId, content, mBeReplyUserId, REPLYTYPE_2, SpUtil.getString(Constant.CACHE_TAG_USERNAME), mPostUserId,mFatherCircleReplyId,mLayCircleReplyId,mNickName);
                    } else {
                        mPresenter.getCommentContent(SpUtil.getString(Constant.CACHE_TAG_UID), mCircleId, content, mPostUserId, REPLYTYPE_1, SpUtil.getString(Constant.CACHE_TAG_USERNAME), mPostUserId,"","" ,"");
                    }
                }
                break;
            case R.id.tv_right://title 分享按钮
                String contentStr = mHelper.getText(mContnet);
                if (!TextUtils.isEmpty(contentStr)){
                    String title = contentStr.length() >=23 ? contentStr.substring(0,22)+"..." : contentStr;
                    ShareUtils.getIntance().share(this, "", ConfigUtil.baseH5Url+"connector/oxstallShare?userId="+SpUtil.getString(Constant.CACHE_TAG_UID)+"&circleId="+mCircleId+"&postUserId="+mPostUserId, title, "投资达人喜欢的社区");
                }
                break;
            case R.id.civ_head: //头像
                if (mCircleDetailBean != null && mCircleDetailBean.circleIndexBo != null) {
                    Intent intent = new Intent(mContext, CircleMineActivity.class);
                    intent.putExtra("userId", mPostUserId);
                    intent.putExtra("imgHead", mCircleDetailBean.circleIndexBo.avatarPic);
                    intent.putExtra("nickName", mCircleDetailBean.circleIndexBo.nickName);
                    startActivity(intent);
                }
                break;
            case R.id.tv_follow: //关注
                if (mFollow.isSelected()) {//1 关注  0 取消
                    mPresenter.getCirleFocus(mPostUserId, SpUtil.getString(Constant.CACHE_TAG_UID), "0");
                } else {
                    mPresenter.getCirleFocus(mPostUserId, SpUtil.getString(Constant.CACHE_TAG_UID), "1");
                }
                break;
            case R.id.iv_support: //点赞
                mPresenter.getCirlePraise(mPostUserId, SpUtil.getString(Constant.CACHE_TAG_UID), true, mCircleId);
                break;
            case R.id.support_linear:
                setCommentDataInit();
                mInput.setHint(getString(R.string.circle_input_hint));
                break;
            case R.id.iv_areward://打赏
                if (mCircleDetailBean !=null && mCircleDetailBean.circleIndexBo !=null){
                    if (TextUtils.equals(SpUtil.getString(Constant.CACHE_TAG_UID),mPostUserId)){
                        ToastUtil.show("自己不能打赏自己");
                    }else {
                        mArewardDialog.arewardCircle(ArewardDialog.AREWARD_CIRCLE_TYPE,mPostUserId,mCircleId,mCircleDetailBean.circleIndexBo.nickName,mCircleDetailBean.circleIndexBo.avatarPic);
                    }
                }
                break;
            case R.id.tv_comment: //全部评论
                if (COMMENT_ALL.equals(mCommentType)) {
                    return;
                }
                setCommentDataInit();
                mInput.setHint(getString(R.string.circle_input_hint));
                mCommentType = COMMENT_ALL;
                mComment.setSelected(true);
                mCommentLine.setVisibility(View.VISIBLE);
                mSeeOwner.setSelected(false);
                mSeeOwnerLine.setVisibility(View.INVISIBLE);
                mRefreshView.setEnableLoadmore(mCommentMore);
                mAdapter.notifyDataChanged(false, getCommentType(), mCommentList);

                boolean mIsEmpty = mCommentList !=null && mCommentList.size() > 0 ? false : true;
                mHelper.setEmptyView(mIsEmpty,mFooterView,mListView);
                if (!mIsCommentAll) {
                    mCommentRows = 0;
                    getCirleComment(mCommentRows, "");
                }
                break;
            case R.id.tv_see_owner: //只看楼主
                if (COMMENT_AUTH.equals(mCommentType)) {
                    return;
                }
                setCommentDataInit();
                mInput.setHint(getString(R.string.circle_input_hint));
                mCommentType = COMMENT_AUTH;
                mComment.setSelected(false);
                mCommentLine.setVisibility(View.INVISIBLE);
                mSeeOwner.setSelected(true);
                mSeeOwnerLine.setVisibility(View.VISIBLE);
                mRefreshView.setEnableLoadmore(mCommentAuthMore);
                mAdapter.notifyDataChanged(false, getCommentType(), mCommentAuthList);

                boolean mIsEmptyAuth = mCommentAuthList !=null && mCommentAuthList.size() > 0 ? false : true;
                mHelper.setEmptyView(mIsEmptyAuth,mFooterView,mListView);
                if (!mIsCommentAuth) {
                    mCommentAuthRows = 0;
                    getCirleCommentAuth(mCommentAuthRows);
                }
                break;
        }
    }


    //帖子详情 - head 数据
    @Override
    public void getDetailBean(CircleDetailBean bean) {
        if (bean == null || isDestroyed()) {
            return;
        }
        mCircleDetailBean = bean;
        if (bean.circleIndexBo == null) {
            return;
        }
        //头像
        ImageUtil.display(bean.circleIndexBo.avatarPic, mCivHead, R.mipmap.img_head);
        //名字
        mHelper.setText(mName, bean.circleIndexBo.nickName);
        //管理员
        mHelper.setText(mUserLabel, bean.circleIndexBo.userRole);
        //时间
        mHelper.setText(mTime, bean.circleIndexBo.createTime);

        //判断是否是自己 / 关注
        if (!TextUtils.isEmpty(bean.circleIndexBo.postUserId)&& bean.circleIndexBo.postUserId.equals(SpUtil.getString(Constant.CACHE_TAG_UID))) {
            mFollow.setVisibility(View.INVISIBLE);
        } else {
            mFollow.setVisibility(View.VISIBLE);
            if (bean.circleIndexBo.isFocus()) {
                mFollow.setSelected(true);
            } else {
                mFollow.setSelected(false);
            }
        }

        //内容
        mHelper.setText(mContnet, bean.circleIndexBo.content);
        //图片
        mHelper.initContentImage(mImgsLinear,bean.circleIndexBo.images);
        //是否点赞
        if (bean.circleIndexBo.isPraise()) {
            mImgSupport.setImageResource(R.mipmap.icon_support_click);
            mImgSupport.setEnabled(false);
        } else {
            mImgSupport.setImageResource(R.mipmap.icon_support_normal);
            mImgSupport.setEnabled(true);
        }
        //点赞数量
        mHelper.setText(mTextSupport,String.format(getString(R.string.support_num),bean.circleIndexBo.praiseCount));
        //点赞头像
        mHelper.updateUserHead(mSupportLinear,bean.circleIndexBo.isPraise,mCircleId,bean.praiseList);

        mImgAreward.setImageResource(bean.circleIndexBo.exceptional? R.mipmap.icon_areward_suc :  R.mipmap.icon_areward);
        mHelper.setText(mTextAreward,bean.circleIndexBo.exceptionalCount+"次打赏");
    }

    //全部
    @Override
    public void getCirleComment(CommentAllBean bean) {
        if (bean == null || bean.commentBosAll == null) { return;}
        this.mIsCommentAll = true;
        this.mCommentCountAll = bean.commentCountAll;
        this.mCommentCountAuth = bean.commentCountAuth;
        this. mCommentRows = bean.rows;
        if (mCommentCountAll <= 0){
            mComment.setText(getString(R.string.comment_all_0));
        }else {
            mComment.setText(String.format(getString(R.string.comment_all),mCommentCountAll));
        }
        if (mCommentCountAuth <=0){
            mSeeOwner.setText(getString(R.string.comment_auth_0));
        }else {
            mSeeOwner.setText(String.format(getString(R.string.comment_auth),mCommentCountAuth));
        }

        if (bean.commentBosAll.size() >= 10) {
            if (!mRefreshView.isEnableLoadmore()) {
                this.mCommentMore = true;
                 mRefreshView.setEnableLoadmore(true);
            }
        } else {
            if (mRefreshView.isEnableLoadmore()) {
                this.mCommentMore = false;
                 mRefreshView.setEnableLoadmore(false);
            }
        }
        if (mCommentRows > 0) {
            mCommentList.addAll(bean.commentBosAll);
            if (COMMENT_ALL.equals(getCommentType())) {
                mAdapter.notifyDataChanged(true, getCommentType(), bean.commentBosAll);
                boolean mIsEmpty = mCommentList != null && mCommentList.size() > 0 ? false : true;
                mHelper.setEmptyView(mIsEmpty, mFooterView, mListView);
            }
        } else {
            mCommentList.clear();
            mCommentList.addAll(bean.commentBosAll);
            if (COMMENT_ALL.equals(getCommentType())){
                mAdapter.notifyDataChanged(false, getCommentType(), bean.commentBosAll);
                boolean mIsEmpty = mCommentList !=null && mCommentList.size() > 0 ? false : true;
                mHelper.setEmptyView(mIsEmpty,mFooterView,mListView);
            }
        }
    }

    //楼主
    @Override
    public void getCirleCommentAuth(List<CommentBean> list) {
        if (list == null) { return; }
        this.mIsCommentAuth = true;

        if (list.size() >= 10) {
            if (!mRefreshView.isEnableLoadmore()) {
                this.mCommentAuthMore = true;
                mRefreshView.setEnableLoadmore(true);
            }
        } else {
            if (mRefreshView.isEnableLoadmore()) {
                this.mCommentAuthMore = false;
                mRefreshView.setEnableLoadmore(false);
            }
        }
        if (mCommentAuthRows > 0) {
            mCommentAuthList.addAll(list);
            if (COMMENT_AUTH.equals(getCommentType())){
                mAdapter.notifyDataChanged(true, getCommentType(), list);
                boolean mIsEmpty = mCommentAuthList !=null && mCommentAuthList.size() > 0 ? false : true;
                mHelper.setEmptyView(mIsEmpty,mFooterView,mListView);
            }
        } else {
            mCommentAuthList.clear();
            mCommentAuthList.addAll(list);
            if (COMMENT_AUTH.equals(getCommentType())) {
                mAdapter.notifyDataChanged(false, getCommentType(), list);
                boolean mIsEmpty = mCommentAuthList != null && mCommentAuthList.size() > 0 ? false : true;
                mHelper.setEmptyView(mIsEmpty, mFooterView, mListView);
            }
        }
    }


    //关注
    @Override
    public void getCirleFocus(JsonNull jsonNull) {
        if (mFollow.isSelected()) {
            mFollow.setSelected(false);
        } else {
            mFollow.setSelected(true);
        }
        //关注 BBSClassifyFragment
        mIsBBSFlown = true;
        BBSFlownEvent bbsFlownEvent = new BBSFlownEvent();
        bbsFlownEvent.uid = mPostUserId;
        bbsFlownEvent.follow = mFollow.isSelected() ? "1" : "0";
        if(bbsFlownEvent.follow.equals("0")){
            ToastUtil.show("取消关注成功");
        } else{
            ToastUtil.show("关注成功");
        }
        EventBus.getDefault().post(bbsFlownEvent);
    }

    //点赞
    @Override
    public void getCirlePraise(JsonNull jsonNull) {
        mImgSupport.setImageResource(R.mipmap.icon_support_click);
        if ( mCircleDetailBean !=null &&  mCircleDetailBean.circleIndexBo !=null){
            mCircleDetailBean.circleIndexBo.isPraise = "1";
        }
        int mPraiseNum = mCircleDetailBean !=null && mCircleDetailBean.circleIndexBo !=null ? mCircleDetailBean.circleIndexBo.praiseCount + 1 : 1;
        mTextSupport.setText(String.format(getString(R.string.support_num),mPraiseNum));
        mImgSupport.setEnabled(false);
        updatePraiseList();
        //点赞 BBSClassifyFragment

        mIsBBSPraise = true;//标记当前点赞
        BBSPraiseEvent bbsPraiseEvent = new BBSPraiseEvent();
        bbsPraiseEvent.topic_id = mCircleId;
        bbsPraiseEvent.praiseNum = String.valueOf(mPraiseNum);
        EventBus.getDefault().post(bbsPraiseEvent);
    }

    //评论回复错误时进入
    @Override
    public void getCommentContentError() {
        mSend.setEnabled(true);
    }

    //评论回复
    @Override
    public void getCommentContent(String nickName,String fatherCircleReplyId,String circleReplyId, int replyType) {
        mSend.setEnabled(true);
        mCommentCountAll++;
        String content =mHelper.geInputText(mInput);
        mInput.setText(getString(R.string.content_null));
        mComment.setText(String.format(getString(R.string.comment_all),mCommentCountAll));
        mHelper.hideSoftInputFromWindow(mInput);

        //判断是否楼主发的评论/回复
        if (SpUtil.getString(Constant.CACHE_TAG_UID).equals(mPostUserId)) {
            mCommentCountAuth++;
            mSeeOwner.setText(String.format(getString(R.string.comment_auth),mCommentCountAuth));
        }

        //1评论 2 回复
        if (REPLYTYPE_1 == replyType){
            BBSCommentContentEvent bbsCommentContentEvent = new BBSCommentContentEvent();
            bbsCommentContentEvent.top_id = mCircleId;
            bbsCommentContentEvent.replyContent = content;
            bbsCommentContentEvent.replyNickName = SpUtil.getString(Constant.CACHE_TAG_USERNAME);
            EventBus.getDefault().post(bbsCommentContentEvent);
        }else {
            BBSCommentEvent bbsCommentEvent = new BBSCommentEvent();
            bbsCommentEvent.commentNum = String.valueOf(mCommentCountAll);
            bbsCommentEvent.topic_id = mCircleId;
            EventBus.getDefault().post(bbsCommentEvent);
        }

        if (COMMENT_AUTH.equals(getCommentType())) {
            mIsCommentAll = false;
            //如果是楼主发的评论就刷新只看楼主接口
            if (SpUtil.getString(Constant.CACHE_TAG_UID).equals(mPostUserId)) {
                mCommentAuthRows = 0;
                getCirleCommentAuth(mCommentAuthRows);
            }
        } else {
            //如果是楼主发的评论就表示下次tab切换时要调用接口
            if (SpUtil.getString(Constant.CACHE_TAG_UID).equals(mPostUserId)) {
                mIsCommentAuth = false;
            }

            //回复不重新调用接口，评论重新调用接口
            if (replyType == REPLYTYPE_1){
                mCommentRows = 0;
                getCirleComment(mCommentRows, "");
            }else {
                layCommentContnet(nickName,fatherCircleReplyId,circleReplyId,content);
            }
        }

        if (replyType == REPLYTYPE_1){
            ToastUtil.show("评论成功");
        }else {
            ToastUtil.show("回复成功");
        }
    }


    /**
     * 刷新回复
     * @param content
     */
    private void  layCommentContnet(String nickName,String fatherCircleReplyId,String circleReplyId,String content){
        if (mAdapter !=null && mAdapter.getCount() > 0){
        for (CommentBean commentBean : mAdapter.getList()) {
            if (commentBean.circleReplyId == Integer.parseInt(fatherCircleReplyId)) {
                LayCommentBean layComment = commentBean.getLayComment();
                layComment.count = layComment.count + 1;
                List<LayCommentListBean> layList = layComment.getLayList();
                if (layList.size() > 2){
                   layList = layList.subList(0,2);
                }
                LayCommentListBean commentListBean = new LayCommentListBean();
                commentListBean.avatarPic = ConfigUtil.baseImageUserUrl + SpUtil.getString(Constant.CACHE_USER_AVATAR);
                commentListBean.nickName = SpUtil.getString(Constant.CACHE_TAG_USERNAME);
                commentListBean.beReplyNickName = nickName;
                commentListBean.replyContent = content;
                commentListBean.replyUserId = SpUtil.getString(Constant.CACHE_TAG_UID);
                commentListBean.fatherCircleReplyId = String.valueOf(commentBean.circleReplyId);
                commentListBean.layCircleReplyId = circleReplyId;
                commentListBean.circleReplyId = circleReplyId;
                layList.add(0, commentListBean);
                layComment.layList = layList;
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
        }
    }


    /**
     * 点赞的事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BBSPraiseEvent event) {
        if (mIsBBSPraise) {
            mIsBBSPraise = false; //true 表示当前页点赞
            return;
        }
        if (mCircleDetailBean == null || mCircleDetailBean.circleIndexBo == null){return;}

        if (event.topic_id.equals(mCircleId)){
             mCircleDetailBean.circleIndexBo.isPraise = "1";
            //是否点赞
            if (mCircleDetailBean.circleIndexBo.isPraise()) {
                mImgSupport.setImageResource(R.mipmap.icon_support_click);
                mImgSupport.setEnabled(false);
            } else {
                mImgSupport.setImageResource(R.mipmap.icon_support_normal);
                mImgSupport.setEnabled(true);
            }
            if (TextUtils.isEmpty(event.praiseNum)) {
                mCircleDetailBean.circleIndexBo.praiseCount = mCircleDetailBean.circleIndexBo.praiseCount + 1;
            } else {
                mCircleDetailBean.circleIndexBo.praiseCount = Integer.parseInt(event.praiseNum);
            }
            //点赞数量
            mHelper.setText(mTextSupport,String.format(getString(R.string.support_num),mCircleDetailBean.circleIndexBo.praiseCount));
            updatePraiseList();
        }
    }

    /**
     * 更新点赞头像
     */
    private void updatePraiseList() {
        if (mCircleDetailBean != null && mCircleDetailBean.praiseList != null && mCircleDetailBean.praiseList.size() > 0) {
            PraiseListBean praiseListBean = new PraiseListBean();
            praiseListBean.avatarPic = ConfigUtil.baseImageUserUrl + SpUtil.getString(Constant.CACHE_USER_AVATAR);
            praiseListBean.userId = SpUtil.getString(Constant.CACHE_TAG_UID);
            mCircleDetailBean.praiseList.add(0, praiseListBean);
            String attention = mCircleDetailBean.circleIndexBo !=null ? mCircleDetailBean.circleIndexBo.isPraise : "";
            mHelper.updateUserHead(mSupportLinear,attention,mCircleId,mCircleDetailBean.praiseList);
        } else {
            List<PraiseListBean> praiseList = new ArrayList<>();
            PraiseListBean praiseListBean = new PraiseListBean();
            praiseListBean.avatarPic = ConfigUtil.baseImageUserUrl + SpUtil.getString(Constant.CACHE_USER_AVATAR);
            praiseListBean.userId = SpUtil.getString(Constant.CACHE_TAG_UID);
            praiseList.add(praiseListBean);
            String attention = mCircleDetailBean!=null && mCircleDetailBean.circleIndexBo !=null ? mCircleDetailBean.circleIndexBo.isPraise : "";
            mHelper.updateUserHead(mSupportLinear,attention,mCircleId,praiseList);
        }
    }

    /**
     * 监听关注
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BBSFlownEvent event) {
        if (mIsBBSFlown) {
            mIsBBSFlown = false; // true 表示当前关注
            return;
        }
        if (mCircleDetailBean != null && mCircleDetailBean.circleIndexBo !=null && event.uid.equals(mCircleDetailBean.circleIndexBo.postUserId)) {
            mCircleDetailBean.circleIndexBo.isFocus = event.follow;
            //是否关注
            if (mCircleDetailBean.circleIndexBo.isFocus()) {
                mFollow.setSelected(true);
            } else {
                mFollow.setSelected(false);
            }
        }
    }

    /**
     * 监听打赏
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BBSArewardEvent event) {
       if (mCircleDetailBean !=null && mCircleDetailBean.circleIndexBo !=null && TextUtils.equals(mCircleId,event.circleId)){
           mCircleDetailBean.circleIndexBo.exceptional = true;
           mCircleDetailBean.circleIndexBo.exceptionalCount = mCircleDetailBean.circleIndexBo.exceptionalCount + 1;
           mImgAreward.setImageResource(R.mipmap.icon_areward_suc);
           mHelper.setText(mTextAreward,mCircleDetailBean.circleIndexBo.exceptionalCount+"次打赏");
       }
    }

    /**
     * 回复监听
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BBSLayCommentEvent event) {
         if (mAdapter !=null && mAdapter.getCount() > 0){
             if (!TextUtils.isEmpty(event.fatherCircleReplyId)) {
                 mCommentCountAll++;
                 mComment.setText(String.format(getString(R.string.comment_all),mCommentCountAll));
                 //判断是否楼主发的评论/回复
                 if (SpUtil.getString(Constant.CACHE_TAG_UID).equals(mPostUserId)) {
                     mCommentCountAuth++;
                     mSeeOwner.setText(String.format(getString(R.string.comment_auth),mCommentCountAuth));
                     mIsCommentAuth = false;
                 }
                 layCommentContnet(event.nickName,event.fatherCircleReplyId,event.circleReplyId,event.content);
             }
         }
    }
}
