package com.honglu.future.ui.details.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.ConfigUtil;
import com.honglu.future.config.Constant;
import com.honglu.future.events.ClickPraiseEvent;
import com.honglu.future.events.CommentEvent;
import com.honglu.future.ui.details.bean.ConsultDetailsBean;
import com.honglu.future.ui.details.bean.InformationCommentBean;
import com.honglu.future.ui.details.contract.ConsultDetailsContract;
import com.honglu.future.ui.details.presenter.ConsultDetailsPresenter;
import com.honglu.future.ui.details.presenter.TopicAdapter;
import com.honglu.future.ui.home.bean.HomeMessageItem;
import com.honglu.future.ui.login.activity.LoginActivity;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;
import com.honglu.future.util.ViewUtil;
import com.honglu.future.widget.CircleImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

import butterknife.BindView;

import static com.honglu.future.ui.home.HomeTabViewUtil.NewsCloumnViewUtils.computingTime;

/**
 * Created by hc on 2017/10/24.
 */

public class ConsultDetailsActivity extends BaseActivity<ConsultDetailsPresenter> implements ConsultDetailsContract.View {
    private static final String KEY_MESSAGE_ITEM = "KEY_MESSAGE_ITEM";
    WebView mContentWv;
    ImageView mImageHead;
    ImageView mSupportIv;
    TextView mTvSupportNum;
    TextView mTvTitle;
    CircleImageView mUserIcon;
    TextView mTvName;
    TextView mTvPosition;
    TextView mTvComment;
    TextView mTvTime ,pinglun_num;
    LinearLayout mLinearPraise;
    LinearLayout mLlAgree;
    LinearLayout mLlPics;
    @BindView(R.id.pullTo_refresh_view)
    SmartRefreshLayout mRefreshView;
    @BindView(R.id.list_view)
    ListView mListView;
    @BindView(R.id.reply_edit)
    EditText mInputEdit;
    @BindView(R.id.btn_publish)
    TextView mPublishBtn;
    private String informationId;
    private int praiseCounts;
    private int commentNum;
    private int mPosition;
    private TopicAdapter mTopicAdapter;
    private boolean isRefrsh;
    public static void startConsultDetailsActivity(HomeMessageItem item, Context context){
        Intent intent = new Intent(context,ConsultDetailsActivity.class);
        intent.putExtra(KEY_MESSAGE_ITEM,item);
        context.startActivity(intent);

    }
    @Override
    public void showLoading(String content) {}
    @Override
    public void stopLoading() {}
    @Override
    public void showErrorMsg(String msg, String type) {
        mRefreshView.finishRefresh();
    }
    @Override
    public int getLayoutId() {
        return R.layout.acticity_consult_details;
    }
    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }
    @Override
    public void loadData() {
        initView();
    }
    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }
    private void initView() {
        mTitle.setTitle(true, R.color.trans, "");
        LinearLayout headerView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.news_detail_header, null);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogin()) {
                    mInputEdit.setHint("说点什么吧~");
                    mInputEdit.setText("");
                    mStrReplyPostmanId = "";
                }
            }
        };
        headerView.setOnClickListener(onClickListener);
        mInputEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLogin();
            }
        });
        mContentWv = (WebView) headerView.findViewById(R.id.webView_content);
        mImageHead = (ImageView) headerView.findViewById(R.id.image_head);
        mSupportIv = (ImageView) headerView.findViewById(R.id.support_iv);
        mTvSupportNum = (TextView) headerView.findViewById(R.id.tv_support);
        mTvTitle = (TextView) headerView.findViewById(R.id.tv_title);
        mUserIcon = (CircleImageView) headerView.findViewById(R.id.user_icon);
        mTvName = (TextView) headerView.findViewById(R.id.tv_name);
        mTvPosition = (TextView) headerView.findViewById(R.id.tv_position);
        mTvComment = (TextView) headerView.findViewById(R.id.tv_comment);
        mTvTime = (TextView) headerView.findViewById(R.id.tv_time);
        pinglun_num = (TextView) headerView.findViewById(R.id.pinglun_num);
        mLinearPraise = (LinearLayout) headerView.findViewById(R.id.ly_likes_user);
        mLlAgree = (LinearLayout) headerView.findViewById(R.id.ll_agree);
        mLlPics = (LinearLayout) headerView.findViewById(R.id.pics_linear);
        //表情过滤
        ViewUtil.setEmojiFilter(mInputEdit);
        mInputEdit.setMovementMethod(ScrollingMovementMethod.getInstance());
        mListView.addHeaderView(headerView);
        mTopicAdapter = new TopicAdapter();
        mListView.setAdapter(mTopicAdapter);
        //设置监听
        setListener();
        Intent intent = getIntent();
        if (intent!=null){
            HomeMessageItem item = (HomeMessageItem) intent.getSerializableExtra(KEY_MESSAGE_ITEM);
            if (item ==null){
                return;
            }
            ImageUtil.display(item.homePic, mImageHead, R.mipmap.other_empty);
            ImageUtil.display(ConfigUtil.baseImageUserUrl+ item.userAvatar, mUserIcon, R.mipmap.img_head);
            mTvTitle.setText(item.title);
            mPosition = item.position;
            mTvName.setText(item.nickname);
            mTopicAdapter.setNickName(item.nickname);
            mTvPosition.setText(item.userRole);
            commentNum = item.commentNum;
            mTvComment.setText(commentNum+ "条评论");
            pinglun_num.setText(""+commentNum);
            praiseCounts = item.praiseCounts;
            mTvSupportNum.setText(praiseCounts+"人点赞");
            if (!TextUtils.isEmpty(item.showTime) && item.showTime.length() > 16) {
                mTvTime.setText(computingTime(item.showTime));
            }
            informationId = item.informationId;
            mPresenter.getMessageData(informationId);
            if (item.isPraise>0){
                mSupportIv.setImageResource(R.mipmap.icon_support_normal);
            }else {
                mSupportIv.setImageResource(R.mipmap.icon_support_click);
            }
        }
        mSupportIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uID = SpUtil.getString(Constant.CACHE_TAG_UID);
                if (isLogin()){
                    mPresenter.praiseMessage(informationId,uID);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String uID = SpUtil.getString(Constant.CACHE_TAG_UID);
        if (!TextUtils.isEmpty(uID)){
            mInputEdit.setFocusable(true);
            mInputEdit.setFocusableInTouchMode(true);
            mInputEdit.requestFocus();
        }
    }

    private boolean isLogin(){
        String uID = SpUtil.getString(Constant.CACHE_TAG_UID);
        if (TextUtils.isEmpty(uID)){
            startActivity(new Intent(ConsultDetailsActivity.this, LoginActivity.class));
            return false;
        }
        return true;
    }
    private String mStrReplyPostmanId = "";
    private void setListener() {
        mRefreshView.setEnableLoadmore(false);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InformationCommentBean bbs = (InformationCommentBean) parent.getItemAtPosition(position);
                if (bbs != null) {
                    if(!isLogin()){
                        return;
                    }
                    mInputEdit.setFocusable(true);
                    mInputEdit.setFocusableInTouchMode(true);
                    mInputEdit.requestFocus();
                    mInputEdit.setText("");
                    String frontStr = "回复:" + bbs.getNickname() + ":";
                    mInputEdit.setHint(frontStr);
                    InputMethodManager inputManager =
                            (InputMethodManager) mInputEdit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(mInputEdit, 0);
                    if (!TextUtils.isEmpty(bbs.getPostmanId())) {
                        mStrReplyPostmanId = bbs.getPostmanId();
                    } else {
                        mStrReplyPostmanId = "";
                    }
                } else {
                    mInputEdit.setHint("说点什么吧~");
                    mInputEdit.setText("");
                    mStrReplyPostmanId = "";
                }
            }
        });

        //发表评论
        mPublishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLogin()){
                    return;
                }
                if (DeviceUtils.isFastDoubleClick()) {
                    return;
                }
                if (!TextUtils.isEmpty(mInputEdit.getText().toString().trim())) {
                    String content = mInputEdit.getText().toString();
                  mPresenter.commentMessage(SpUtil.getString(Constant.CACHE_TAG_UID),informationId,mStrReplyPostmanId,content);
                } else {
                    ToastUtil.show("请填写评论内容");
                }
            }
        });
        mRefreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPresenter.getReplyList(informationId);
                isRefrsh = true;
            }
        });
    }

    @Override
    public void bindData(ConsultDetailsBean bean) {
        isRefrsh = true;
        mPresenter.getReplyList(informationId);
        //设置字体大小
        WebSettings settings = mContentWv.getSettings();
        settings.setSupportZoom(true);
        settings.setTextZoom (110);
        // 设置加载进来的页面自适应手机屏幕
        //settings.setUseWideViewPort(true);
        //mContentTv.getSettings().setLoadWithOverviewMode(true);
        if (!mIsWebLoaded) {
            mContentWv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);//适应内容大小
            mContentWv.loadDataWithBaseURL(null, getNewContent(bean.content), "text/html", "utf-8", null);
            mIsWebLoaded = true;
        }
        //设置图片
        initContentImage(bean.picList);
        //更新点赞头像列表
        updatePraiseUser(bean.praiseAvatars);
    }
    /**
     * 内容中的图片加载
     *
     * @param picList 图片 链接集合
     */
    private void initContentImage(List<String> picList) {
        mLlPics.setVisibility(View.VISIBLE);
        mLlPics.removeAllViews();
        if (picList != null && picList.size() != 0) {
            for (String imageStr : picList) {
                final ImageView imageView = new ImageView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                imageView.setImageResource(R.mipmap.other_empty);
                params.topMargin = DeviceUtils.dip2px(this, 4);
                mLlPics.addView(imageView);
                final int imageWidth = DeviceUtils.getScreenWidth(this) - DeviceUtils.dip2px(this, 24);
                Glide.with(this).load(imageStr).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        int bitmapWidth = resource.getWidth();
                        int bitmapHeight = resource.getHeight();
                        int height = (int) ((float) imageWidth * (float) bitmapHeight / (float) bitmapWidth);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                        params.width = imageWidth;
                        params.height = height;
                        params.topMargin = DeviceUtils.dip2px(ConsultDetailsActivity.this, 4);
                        imageView.setImageBitmap(resource);
                        imageView.requestLayout();
                    }
                });
            }
        }
    }
    /**
     * 更新点赞列表
     *
     * @param praiseUserList
     */
    private void updatePraiseUser(List<String> praiseUserList) {
        mLinearPraise.removeAllViews();
        if (praiseUserList != null) {
            if (praiseUserList.size()==0){
                mLlPics.setVisibility(View.GONE);
            }else {
                mLlPics.setVisibility(View.VISIBLE);
            }
            for (String praiseUser : praiseUserList) {
                CircleImageView headIV = new CircleImageView(this);
                int size = getResources().getDimensionPixelSize(R.dimen.dimen_30dp);
                mLinearPraise.addView(headIV, new LinearLayout.LayoutParams(size, size));
                ImageUtil.display(ConfigUtil.baseImageUserUrl + praiseUser, headIV, R.mipmap.img_head);
                //最多显示5个
                if (mLinearPraise.getChildCount() >= 5)
                    break;
            }
        }
    }
    private boolean mIsWebLoaded;
    /**
     * 解析加载h5代码
     * @param html
     * @return
     */
    public static String getNewContent(String html) {
        Document doc = Jsoup.parse(html);
        Elements elements = doc.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width", "70%").attr("height", "auto");
        }
        return doc.toString();
    }
    @Override
    public void praiseSuccess(List<String> praiseUserList) {
        ToastUtil.show("点赞成功");
        ++praiseCounts;
        mSupportIv.setImageResource(R.mipmap.icon_support_normal);
        ClickPraiseEvent clickPraiseEvent = new ClickPraiseEvent();
        clickPraiseEvent.position = mPosition;
        EventBus.getDefault().post(clickPraiseEvent);
        mTvSupportNum.setText(praiseCounts+"人点赞");
        updatePraiseUser(praiseUserList);
    }

    @Override
    public void commentSuccess() {
        ++commentNum;
        mTvComment.setText(commentNum+ "条评论");
        pinglun_num.setText(commentNum+ "");
        CommentEvent clickPraiseEvent = new CommentEvent();
        clickPraiseEvent.position = mPosition;
        EventBus.getDefault().post(clickPraiseEvent);
        isRefrsh = false;
        mPresenter.getReplyList(informationId);
    }

    @Override
    public void bindReplyList(final List<InformationCommentBean> list) {
        mRefreshView.finishRefresh(0,true);
        mInputEdit.setText("");
        //加载评论列表
        mTopicAdapter.setDatas(list);
        if (!isRefrsh){
            //滚到第一条
            mListView.setSelection(1);
        }
        //隐藏小键盘
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mInputEdit.getWindowToken(), 0);

    }
}
