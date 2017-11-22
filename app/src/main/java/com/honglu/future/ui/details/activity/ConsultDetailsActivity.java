package com.honglu.future.ui.details.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.ConfigUtil;
import com.honglu.future.config.Constant;
import com.honglu.future.events.ClickPraiseEvent;
import com.honglu.future.ui.details.bean.ConsultDetailsBean;
import com.honglu.future.ui.details.contract.ConsultDetailsContract;
import com.honglu.future.ui.details.presenter.ConsultDetailsPresenter;
import com.honglu.future.ui.home.bean.HomeMessageItem;
import com.honglu.future.ui.login.activity.LoginActivity;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;
import com.honglu.future.widget.CircleImageView;

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

    @BindView(R.id.webView_content)
    WebView mContentWv;
    @BindView(R.id.image_head)
    ImageView mImageHead;
    @BindView(R.id.support_iv)
    ImageView mSupportIv;
    @BindView(R.id.tv_support)
    TextView mTvSupportNum;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.user_icon)
    CircleImageView mUserIcon;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_position)
    TextView mTvPosition;
    @BindView(R.id.tv_comment)
    TextView mTvComment;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.ly_likes_user)
    LinearLayout mLinearPraise;
    @BindView(R.id.ll_agree)
    LinearLayout mLlAgree;
    @BindView(R.id.pics_linear)
    LinearLayout mLlPics;
    private List<String> mPraiseUserList;
    private String informationId;
    private int praiseCounts;
    private int mPosition;

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
    public void showErrorMsg(String msg, String type) {}
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
        mTitle.setRightTitle(R.mipmap.ic_details_shape, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String url = App.getConfig().ACTIVITY_CENTER;
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);*/
            }
        });
        Intent intent = getIntent();
        if (intent!=null){
            HomeMessageItem item = (HomeMessageItem) intent.getSerializableExtra(KEY_MESSAGE_ITEM);
            if (item==null){
                return;
            }
            ImageUtil.display(item.homePic, mImageHead, R.mipmap.other_empty);
            ImageUtil.display(ConfigUtil.baseImageUserUrl+item.userAvatar, mUserIcon, R.mipmap.img_head);
            mTvTitle.setText(item.title);
            mPosition = item.position;
            mTvName.setText(item.nickname);
            mTvPosition.setText(item.userRole);
            mTvComment.setText(item.commentNum+ "条评论");
            praiseCounts = item.praiseCounts;
            mTvSupportNum.setText(praiseCounts+"人点赞");
            if (!TextUtils.isEmpty(item.showTime) && item.showTime.length() > 16) {
                mTvTime.setText(computingTime(item.showTime));
            }
            informationId = item.informationId;
            mPresenter.getMessageData(informationId);
            if (item.isPraise>0){
                mSupportIv.setImageResource(R.mipmap.icon_support_click);
            }else {
                mSupportIv.setImageResource(R.mipmap.icon_support_normal);
            }
        }
        mSupportIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uID = SpUtil.getString(Constant.CACHE_TAG_UID);
                if (TextUtils.isEmpty(uID)){
                    startActivity(new Intent(ConsultDetailsActivity.this, LoginActivity.class));
                    return;
                }
                mPresenter.praiseMessage(informationId,uID);
            }
        });
    }
    @Override
    public void bindData(ConsultDetailsBean bean) {
        //设置字体大小
        WebSettings settings = mContentWv.getSettings();
        settings.setSupportZoom(true);
        settings.setTextSize(WebSettings.TextSize.NORMAL);
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
        this.mPraiseUserList = praiseUserList;
        mLinearPraise.removeAllViews();
        if (praiseUserList != null) {
            if (praiseUserList.size()==0){
                mLlPics.setVisibility(View.GONE);
            }else {
                mLlPics.setVisibility(View.VISIBLE);
            }
            for (String praiseUser : praiseUserList) {
                CircleImageView headIV = new CircleImageView(this);
                int size = getResources().getDimensionPixelSize(R.dimen.dimen_36dp);
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
        mSupportIv.setImageResource(R.mipmap.icon_support_click);
        ClickPraiseEvent clickPraiseEvent = new ClickPraiseEvent();
        clickPraiseEvent.position = mPosition;
        EventBus.getDefault().post(clickPraiseEvent);
        mTvSupportNum.setText(praiseCounts+"人点赞");
        updatePraiseUser(praiseUserList);
    }
}
