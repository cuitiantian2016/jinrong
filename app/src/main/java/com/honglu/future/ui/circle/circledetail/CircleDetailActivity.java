package com.honglu.future.ui.circle.circledetail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.util.Util;
import com.google.gson.JsonNull;
import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.ConfigUtil;
import com.honglu.future.config.Constant;
import com.honglu.future.events.BBSCommentContentEvent;
import com.honglu.future.events.BBSFlownEvent;
import com.honglu.future.events.BBSPraiseEvent;
import com.honglu.future.ui.circle.bean.BBS;
import com.honglu.future.ui.circle.bean.CircleDetailBean;
import com.honglu.future.ui.circle.bean.CommentAllBean;
import com.honglu.future.ui.circle.bean.CommentBean;
import com.honglu.future.ui.circle.bean.PraiseListBean;
import com.honglu.future.ui.circle.praisesandreward.RewardDetailActivity;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.util.ShareUtils;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.TimeUtil;
import com.honglu.future.util.ToastUtil;
import com.honglu.future.widget.CircleImageView;
import com.honglu.future.widget.photo.FullScreenDisplayActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    private TextView mComment;
    private View mCommentLine;
    private TextView mSeeOwner;
    private View mSeeOwnerLine;

    private List<CommentBean> mCommentList = new ArrayList<>();
    private List<CommentBean> mCommentAuthList = new ArrayList<>();
    private CircleDetailBean mCircleDetailBean;
    private CircleDetailAdapter mAdapter;
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
    //点回复对应bean
    private CommentBean mCommentBean;
    private int mCommentCountAll = 0;
    private int mCommentCountAuth = 0;

    private boolean mIsBBSPraise = false;//标记当前页点赞
    private boolean mIsBBSFlown = false; //标记当前页

    @Override
    public void initPresenter() {
        mPresenter.init(CircleDetailActivity.this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_circle_detail;
    }

    public String getCommentType(){
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
        if (!TextUtils.isEmpty(msg)){
            ToastUtil.show(msg);
        }
    }
    @Override
    public void loadData() {
        EventBus.getDefault().register(this);
        if (getIntent().hasExtra(POST_USER_KEY)){
            mPostUserId = getIntent().getStringExtra(POST_USER_KEY);
        }
        if (getIntent().hasExtra(CIRCLEID_KEY)){
            mCircleId = getIntent().getStringExtra(CIRCLEID_KEY);
        }
        if (getIntent().hasExtra(CIRCLEREPLYID_KEY)){ //目前协议跳转时需要这个值 其他时候传空
            circleReplyId = getIntent().getStringExtra(CIRCLEREPLYID_KEY);
        }

        mTitle.setTitle(false, R.color.color_white, "详情");
        mTitle.setRightTitle(R.mipmap.ic_share, this);
        View headView = View.inflate(CircleDetailActivity.this, R.layout.layout_circle_detail_head, null);
        mCivHead = (CircleImageView) headView.findViewById(R.id.civ_head);
        mName = (TextView) headView.findViewById(R.id.tv_name);
        mUserLabel = (TextView) headView.findViewById(R.id.tv_user_label);
        mTime = (TextView) headView.findViewById(R.id.tv_time);
        mFollow = (TextView) headView.findViewById(R.id.tv_follow);
        mFollow.setEnabled(false);
        mContnet = (TextView) headView.findViewById(R.id.tv_contnet);
        mSeeZhuanTi = (TextView) headView.findViewById(R.id.tv_seezhuanti);
        mSeeZhuanTi.setVisibility(View.GONE);
        mImgsLinear = (LinearLayout) headView.findViewById(R.id.imgs_linear);
        mImgSupport = (ImageView) headView.findViewById(R.id.iv_support);
        mImgSupport.setEnabled(false);
        mTextSupport = (TextView) headView.findViewById(R.id.tv_support);
        mSupportLinear = (LinearLayout) headView.findViewById(R.id.support_linear);
        mComment = (TextView) headView.findViewById(R.id.tv_comment);
        mCommentLine = headView.findViewById(R.id.v_comment_line);
        mSeeOwner = (TextView) headView.findViewById(R.id.tv_see_owner);
        mSeeOwnerLine = headView.findViewById(R.id.v_see_owner_line);

        mComment.setSelected(true);
        mCommentLine.setVisibility(View.VISIBLE);
        mSeeOwner.setSelected(false);
        mSeeOwnerLine.setVisibility(View.INVISIBLE);

        mListView.addHeaderView(headView);
        mAdapter = new CircleDetailAdapter(CircleDetailActivity.this ,mPostUserId);
        mListView.setAdapter(mAdapter);

        mCivHead.setOnClickListener(this);
        mFollow.setOnClickListener(this);
        mImgSupport.setOnClickListener(this);
        mComment.setOnClickListener(this);
        mSeeOwner.setOnClickListener(this);
        mSend.setOnClickListener(this);

        mRefreshView.setEnableLoadmore(false);
        mRefreshView.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (COMMENT_ALL.equals(mCommentType)){
                    mCommentRows ++;
                    mPresenter.getCirleComment(SpUtil.getString(Constant.CACHE_TAG_UID),mCircleId,mPostUserId,mCommentRows,"");
                }else {
                    mCommentAuthRows ++;
                    mPresenter.getCirleCommentAuth(SpUtil.getString(Constant.CACHE_TAG_UID),mCircleId,mPostUserId,mCommentAuthRows);
                }
                mRefreshView.finishLoadmore();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPresenter.getDetailBean(SpUtil.getString(Constant.CACHE_TAG_UID),mCircleId);
                if (COMMENT_ALL.equals(mCommentType)){
                    mCommentRows = 0;
                    mPresenter.getCirleComment(SpUtil.getString(Constant.CACHE_TAG_UID),mCircleId,mPostUserId,mCommentRows,"");
                }else {
                    mCommentAuthRows = 0;
                    mPresenter.getCirleCommentAuth(SpUtil.getString(Constant.CACHE_TAG_UID),mCircleId,mPostUserId,mCommentAuthRows);
                }
                mRefreshView.finishRefresh();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {return;} //addHead
                mCommentBean = (CommentBean) parent.getItemAtPosition(position);
                mInput.setHint("回复："+mCommentBean.nickName);
            }
        });

        mPresenter.getDetailBean(SpUtil.getString(Constant.CACHE_TAG_UID),mCircleId);
        mPresenter.getCirleComment(SpUtil.getString(Constant.CACHE_TAG_UID),mCircleId,mPostUserId,mCommentRows,circleReplyId);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_send: //发表
                String content = mInput.getText().toString();
                if (TextUtils.isEmpty(content)){
                    ToastUtil.show("内容不能为空...");
                    return;
                }
                if (mCommentBean !=null){
                    mPresenter.getCommentContent(SpUtil.getString(Constant.CACHE_TAG_UID),mCircleId,content,mCommentBean.replyUserId,2,SpUtil.getString(Constant.CACHE_TAG_USERNAME),mPostUserId);
                }else {
                    mPresenter.getCommentContent(SpUtil.getString(Constant.CACHE_TAG_UID),mCircleId,content,mPostUserId,1,SpUtil.getString(Constant.CACHE_TAG_USERNAME),mPostUserId);
                }
                break;
            case R.id.tv_right://title 分享按钮
                ShareUtils.getIntance().share(this, "", "http://www.baidu.com", "帖子分享", "帖子分享内容");
                break;
            case R.id.civ_head: //头像

                break;
            case R.id.tv_follow: //关注
                 //1 关注  0 取消
                 if (mFollow.isSelected()){
                     mPresenter.getCirleFocus(mPostUserId,SpUtil.getString(Constant.CACHE_TAG_UID),"0");
                 }else {
                     mPresenter.getCirleFocus(mPostUserId,SpUtil.getString(Constant.CACHE_TAG_UID),"1");
                 }
                break;
            case R.id.iv_support: //点赞
                 mPresenter.getCirlePraise(mPostUserId,SpUtil.getString(Constant.CACHE_TAG_UID),true,mCircleId);
                break;
            case R.id.tv_comment: //全部评论
                if (COMMENT_ALL.equals(mCommentType)){
                   return;
                }
                mCommentBean = null;
                mInput.setHint(getString(R.string.circle_input_hint));
                mCommentType = COMMENT_ALL;
                mComment.setSelected(true);
                mCommentLine.setVisibility(View.VISIBLE);
                mSeeOwner.setSelected(false);
                mSeeOwnerLine.setVisibility(View.INVISIBLE);
                mRefreshView.setEnableLoadmore(mCommentMore);
                mAdapter.notifyDataChanged(getCommentType());
                if (mCommentList != null && mCommentList.size() > 0){
                    mAdapter.notifyDataChanged(false,getCommentType(),mCommentList);
                }else {
                    mPresenter.getCirleComment(SpUtil.getString(Constant.CACHE_TAG_UID),mCircleId,mPostUserId,mCommentRows,"");
                }
                break;
            case R.id.tv_see_owner: //只看楼主
                if (COMMENT_AUTH.equals(mCommentType)){
                    return;
                }
                mCommentBean = null;
                mInput.setHint(getString(R.string.circle_input_hint));
                mCommentType = COMMENT_AUTH;
                mComment.setSelected(false);
                mCommentLine.setVisibility(View.INVISIBLE);
                mSeeOwner.setSelected(true);
                mSeeOwnerLine.setVisibility(View.VISIBLE);
                mRefreshView.setEnableLoadmore(mCommentAuthMore);
                mAdapter.notifyDataChanged(getCommentType());
                if (mCommentAuthList !=null && mCommentAuthList.size() > 0){
                    mAdapter.notifyDataChanged(false,getCommentType(),mCommentAuthList);
                }else {
                    mPresenter.getCirleCommentAuth(SpUtil.getString(Constant.CACHE_TAG_UID),mCircleId,mPostUserId,mCommentAuthRows);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        if (Util.isOnMainThread() && !this.isFinishing()) {
            Glide.with(this).pauseRequests();
        }
    }


    //点赞头像
    private void updateUserHead(List<PraiseListBean> headList) {
        mSupportLinear.setVisibility(View.VISIBLE);
        mSupportLinear.removeAllViews();

        int size = getResources().getDimensionPixelSize(R.dimen.dimen_30dp);
        if (headList !=null && headList.size() > 0) {
            for (int i = 0; i < headList.size(); i++) {
                if (i == 4) {
                    break;
                }
                PraiseListBean praiseBean = headList.get(i);
                CircleImageView imgHead = new CircleImageView(this);
                ImageUtil.display(praiseBean.avatarPic, imgHead, R.mipmap.img_head);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
                params.rightMargin = size / 2;
                mSupportLinear.addView(imgHead, params);
            }
        }

        CircleImageView imgHead = new CircleImageView(this);
        imgHead.setImageResource(R.mipmap.ic_more);
        imgHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCircleDetailBean!=null&&mCircleDetailBean.circleIndexBo!=null)
                RewardDetailActivity.startRewardDetailActivity(CircleDetailActivity.this,mCircleId,mCircleDetailBean.circleIndexBo.isPraise);
            }
        });
        mSupportLinear.addView(imgHead, new LinearLayout.LayoutParams(size, size));
    }


    /**
     * 内容中的图片加载
     *
     * @param picList 图片 链接集合
     */
    private void initContentImage(final ArrayList<String> picList) {
        mImgsLinear.setVisibility(View.VISIBLE);
        mImgsLinear.removeAllViews();

        if (picList != null && picList.size() != 0) {
            for (final String imageStr : picList) {
                final ImageView imageView = new ImageView(this);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), FullScreenDisplayActivity.class);
                        Bundle b = new Bundle();
                        b.putStringArrayList("image_urls", picList);
                        b.putInt("position", picList.indexOf(imageStr));
                        intent.putExtras(b);
                        int[] location = new int[2];
                        view.getLocationOnScreen(location);
                        intent.putExtra("locationX", location[0]);
                        intent.putExtra("locationY", location[1]);
                        intent.putExtra("width", view.getWidth());
                        intent.putExtra("height", view.getHeight());
                        view.getContext().startActivity(intent);
                    }
                });
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                imageView.setImageResource(R.mipmap.other_empty);
                params.topMargin = DeviceUtils.dip2px(this, 4);
                mImgsLinear.addView(imageView);
                final int imageWidth = DeviceUtils.getScreenWidth(this) - DeviceUtils.dip2px(this, 30);
                String picurls = imageStr;
                Glide.with(this).load(picurls).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        int bitmapWidth = resource.getWidth();
                        int bitmapHeight = resource.getHeight();
                        int height = (int) ((float) imageWidth * (float) bitmapHeight / (float) bitmapWidth);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                        params.width = imageWidth;
                        params.height = height;
                        params.topMargin = DeviceUtils.dip2px(CircleDetailActivity.this, 4);
                        imageView.setImageBitmap(resource);
                        imageView.requestLayout();
                    }
                });
            }
        }
    }


    //帖子详情 - head 数据
    @Override
    public void getDetailBean(CircleDetailBean bean) {
       if (bean == null || isDestroyed()){return;}
        mCircleDetailBean = bean;
       if (bean.circleIndexBo !=null){
           //头像
           ImageUtil.display( bean.circleIndexBo.avatarPic, mCivHead, R.mipmap.img_head);
           //名字
           setText(mName,bean.circleIndexBo.nickName);
           //管理员
           setText(mUserLabel,bean.circleIndexBo.userRole);
           //时间
           setText(mTime,bean.circleIndexBo.createTime);

           //判断是否是自己 / 关注
           if (!TextUtils.isEmpty(bean.circleIndexBo.postUserId)
                   && bean.circleIndexBo.postUserId.equals(SpUtil.getString(Constant.CACHE_TAG_UID))){
               mFollow.setVisibility(View.INVISIBLE);
           }else {
               mFollow.setVisibility(View.VISIBLE);
               //是否关注
               if (bean.circleIndexBo.isFocus()){
                   mFollow.setSelected(true);
                   mFollow.setText("已关注");
                   mFollow.setEnabled(true);
               }else {
                   mFollow.setSelected(false);
                   mFollow.setText("+ 关注");
                   mFollow.setEnabled(true);
               }
           }

           //内容
           setText(mContnet,bean.circleIndexBo.content);
           //图片
           initContentImage(bean.circleIndexBo.images);
           //是否点赞
           if (bean.circleIndexBo.isPraise()){
               mImgSupport.setImageResource(R.mipmap.icon_support_click);
               mImgSupport.setEnabled(false);
           }else {
               mImgSupport.setImageResource(R.mipmap.icon_support_normal);
               mImgSupport.setEnabled(true);
           }
           //点赞数量
           setText(mTextSupport,bean.circleIndexBo.praiseCount);
           //点赞头像
           updateUserHead(bean.praiseList);
       }
    }

    //全部
    @Override
    public void getCirleComment(CommentAllBean bean) {
         if (bean == null || bean.commentBosAll == null){return;}
        mCommentCountAll = bean.commentCountAll;
        mCommentCountAuth = bean.commentCountAuth;
        mComment.setText("全部评论 "+mCommentCountAll);
        mSeeOwner.setText("只看楼主 "+mCommentCountAuth);
        mCommentRows = bean.rows;
        if (bean.commentBosAll.size() >=10){
            if (!mRefreshView.isEnableLoadmore()){
                mCommentMore = true;
                mRefreshView.setEnableLoadmore(true);
            }
        }else {
            if (mRefreshView.isEnableLoadmore()) {
                mCommentMore = false;
                mRefreshView.setEnableLoadmore(false);
            }
        }
        if (mCommentRows > 0){
            mCommentList.addAll(bean.commentBosAll);
            mAdapter.notifyDataChanged(true,getCommentType(),bean.commentBosAll);
        }else {
            mCommentList.clear();
            mCommentList.addAll(bean.commentBosAll);
            mAdapter.notifyDataChanged(false,getCommentType(),bean.commentBosAll);
        }
    }

    //楼主
    @Override
    public void getCirleCommentAuth(List<CommentBean> list) {
        if (list == null){return;}
        if (list.size() >=10){
            if (!mRefreshView.isEnableLoadmore()){
                mCommentAuthMore = true;
                mRefreshView.setEnableLoadmore(true);
            }
        }else {
            if (mRefreshView.isEnableLoadmore()) {
                mCommentAuthMore = false;
                mRefreshView.setEnableLoadmore(false);
            }
        }
        if (mCommentAuthRows > 0){
            mCommentAuthList.addAll(list);
            mAdapter.notifyDataChanged(true,getCommentType(),list);
        }else {
            mCommentAuthList.clear();
            mCommentAuthList.addAll(list);
            mAdapter.notifyDataChanged(false,getCommentType(),list);
        }
    }


    //关注
    @Override
    public void getCirleFocus(JsonNull jsonNull) {
        if (mFollow.isSelected()){
            mFollow.setSelected(false);
            mFollow.setText("已关注");
        }else {
            mFollow.setSelected(true);
            mFollow.setText("+ 关注");
        }
        //关注 BBSClassifyFragment
        mIsBBSFlown = true;
        BBSFlownEvent bbsFlownEvent = new BBSFlownEvent();
        bbsFlownEvent.uid = mPostUserId;
        bbsFlownEvent.follow = mFollow.isSelected() ? "1" : "0";
        EventBus.getDefault().post(bbsFlownEvent);
    }

    //点赞
    @Override
    public void getCirlePraise(JsonNull jsonNull) {
        mImgSupport.setImageResource(R.mipmap.icon_support_click);
        int mPraiseNum = getTextNum(mTextSupport)+1;
        mTextSupport.setText(String.valueOf(mPraiseNum));
        mImgSupport.setEnabled(false);
        updatePraiseList();
        //点赞 BBSClassifyFragment

        mIsBBSPraise = true;//标记当前点赞
        BBSPraiseEvent bbsPraiseEvent = new BBSPraiseEvent();
        bbsPraiseEvent.topic_id = mCircleId;
        bbsPraiseEvent.praiseNum = String.valueOf(mPraiseNum);
        EventBus.getDefault().post(bbsPraiseEvent);
    }

    //评论回复
    @Override
    public void getCommentContent(JsonNull jsonNull,int replyType) {
        String content = mInput.getText().toString();
        CommentBean commentBean = new CommentBean();
        if (mCommentBean  !=null){
            commentBean.replyUserId = SpUtil.getString(Constant.CACHE_TAG_UID);//回复人id
            commentBean.nickName = SpUtil.getString(Constant.CACHE_TAG_USERNAME);//回复人名字
            commentBean.avatarPic =  ConfigUtil.baseImageUserUrl + SpUtil.getString(Constant.CACHE_USER_AVATAR); //回复人头像
            commentBean.beReplyNickName = mCommentBean.nickName; //被回复人名字
            commentBean.beReplyAvatarPic = mCommentBean.avatarPic;//被回复人头像
            commentBean.replyType = String.valueOf(replyType);
            commentBean.createTime = TimeUtil.getCurrentDate(String.valueOf(System.currentTimeMillis()));
            commentBean.replyContent = content;
        }else {
            commentBean.replyUserId = SpUtil.getString(Constant.CACHE_TAG_UID);//回复人id
            commentBean.nickName = SpUtil.getString(Constant.CACHE_TAG_USERNAME);//回复人名字
            commentBean.avatarPic =  ConfigUtil.baseImageUserUrl + SpUtil.getString(Constant.CACHE_USER_AVATAR); //回复人头像
            commentBean.replyType = String.valueOf(replyType);
            commentBean.createTime = TimeUtil.getCurrentDate(String.valueOf(System.currentTimeMillis()));
            commentBean.replyContent = content;
            commentBean.createTime = TimeUtil.getCurrentDate(String.valueOf(System.currentTimeMillis()));
            if (mCircleDetailBean !=null && mCircleDetailBean.circleIndexBo !=null){
                commentBean.beReplyNickName = mCircleDetailBean.circleIndexBo.nickName; //被回复人名字
                commentBean.beReplyAvatarPic = mCircleDetailBean.circleIndexBo.avatarPic;//被回复人头像
            }
        }
        mCommentBean = null;
        mInput.setText("");
        mInput.setHint(getString(R.string.circle_input_hint));
        if (mAdapter !=null){
            if (COMMENT_ALL.equals(getCommentType())){
                mCommentList.add(0,commentBean);
                mCommentCountAll++;
                mComment.setText("全部评论 "+mCommentCountAll);
            }else {
                mCommentAuthList.add(0,commentBean);
                mCommentCountAuth++;
                mSeeOwner.setText("只看楼主 "+mCommentCountAuth);
            }
            mAdapter.addCommentBean(commentBean);
        }
        //评论 BBSClassifyFragment
        BBSCommentContentEvent bbsCommentContentEvent = new BBSCommentContentEvent();
        bbsCommentContentEvent.top_id = mCircleId;
        bbsCommentContentEvent.replyContent = content;
        bbsCommentContentEvent.replyNickName = SpUtil.getString(Constant.CACHE_TAG_USERNAME);
        EventBus.getDefault().post(bbsCommentContentEvent);
    }

    private void setText(TextView view , String text){
        if (!TextUtils.isEmpty(text)){
            view.setText(text);
        }else {
            view.setText("");
        }
    }
    private int getTextNum(TextView view){
        return view.getText() !=null && !TextUtils.isEmpty(view.getText().toString()) ? Integer.parseInt(view.getText().toString()) : 0;
    }


    /**
     * 点赞的事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BBSPraiseEvent event) {
        if (mIsBBSPraise){
            mIsBBSPraise = false; //true 表示当前页点赞
            return;
        }
        if (mCircleDetailBean!=null&&event.topic_id.equals(mCircleId)){
            mCircleDetailBean.circleIndexBo.isPraise = "1";
            //是否点赞
            if (mCircleDetailBean.circleIndexBo.isPraise()){
                mImgSupport.setImageResource(R.mipmap.icon_support_click);
                mImgSupport.setEnabled(false);
            }else {
                mImgSupport.setImageResource(R.mipmap.icon_support_normal);
                mImgSupport.setEnabled(true);
            }
            if (TextUtils.isEmpty(event.praiseNum)){
                mCircleDetailBean.circleIndexBo.praiseCount = Integer.parseInt(mCircleDetailBean.circleIndexBo.praiseCount)+1+"";
            }else {
                mCircleDetailBean.circleIndexBo.praiseCount = event.praiseNum;
            }
            //点赞数量
            setText(mTextSupport,mCircleDetailBean.circleIndexBo.praiseCount);
            updatePraiseList();
        }
    }
    /**
     * 更新点赞头像
     */
    private void updatePraiseList(){
        if (mCircleDetailBean !=null && mCircleDetailBean.praiseList !=null && mCircleDetailBean.praiseList.size() > 0){
            PraiseListBean praiseListBean = new PraiseListBean();
            praiseListBean.avatarPic = ConfigUtil.baseImageUserUrl + SpUtil.getString(Constant.CACHE_USER_AVATAR);
            praiseListBean.userId = SpUtil.getString(Constant.CACHE_TAG_UID);
            mCircleDetailBean.praiseList.add(0,praiseListBean);
            updateUserHead(mCircleDetailBean.praiseList);
        }else {
            List<PraiseListBean> praiseList = new ArrayList<>();
            PraiseListBean praiseListBean = new PraiseListBean();
            praiseListBean.avatarPic = ConfigUtil.baseImageUserUrl + SpUtil.getString(Constant.CACHE_USER_AVATAR);
            praiseListBean.userId = SpUtil.getString(Constant.CACHE_TAG_UID);
            praiseList.add(praiseListBean);
            updateUserHead(praiseList);
        }
    }
    /**
     * 监听关注
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BBSFlownEvent event) {
        if (mIsBBSFlown){
            mIsBBSFlown = false; // true 表示当前关注
            return;
        }
        if (mCircleDetailBean!=null&&event.uid.equals(mCircleDetailBean.circleIndexBo.postUserId)){
            mCircleDetailBean.circleIndexBo.isFocus =event.follow;
            //是否关注
            if (mCircleDetailBean.circleIndexBo.isFocus()){
                mFollow.setSelected(true);
                mFollow.setText("已关注");
                mFollow.setEnabled(true);
            }else {
                mFollow.setSelected(false);
                mFollow.setText("+ 关注");
                mFollow.setEnabled(true);
            }
        }
    }
}
