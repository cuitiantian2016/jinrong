package com.honglu.future.ui.details.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.ConfigUtil;
import com.honglu.future.ui.details.contract.ConsultDetailsContract;
import com.honglu.future.ui.details.presenter.ConsultDetailsPresenter;
import com.honglu.future.ui.home.bean.HomeMessageItem;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.widget.CircleImageView;

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

    public static void startConsultDetailsActivity(HomeMessageItem item, Context context){
        Intent intent = new Intent(context,ConsultDetailsActivity.class);
        intent.putExtra(KEY_MESSAGE_ITEM,item);
        context.startActivity(intent);

    }

    @Override
    public void showLoading(String content) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorMsg(String msg, String type) {

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
            ImageUtil.display(ConfigUtil.baseImageUserUrl+item.userAvatar, mUserIcon, R.mipmap.iv_no_image);
            mTvTitle.setText(item.title);
            mTvName.setText(item.nickname);
            mTvPosition.setText(item.userRole);
            mTvComment.setText(item.commentNum+ "条评论");
            if (!TextUtils.isEmpty(item.createTime) && item.createTime.length() > 16) {
                mTvTime.setText(computingTime(item.createTime));
            }
        }
        WebSettings settings = mContentWv.getSettings();
        settings.setSupportZoom(true);
        settings.setTextSize(WebSettings.TextSize.NORMAL);
        // 设置加载进来的页面自适应手机屏幕
        //settings.setUseWideViewPort(true);
        //mContentTv.getSettings().setLoadWithOverviewMode(true);

        mContentWv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);//适应内容大小
        mContentWv.loadDataWithBaseURL(null, "这里展示的是H5内容这里展示的是H5内容这里展示的是H5内容这里展示的是H5内容这里展示的是H5内容这里展示的是H5内容这里展示的是H5内容这里展示的是H5内容这里展示的是H5内容这里展示的是H5内容这里展示的是H5内容这里展示的是H5内容这里展示的是H5内容这里展示的是H5内容这里展示的是H5内容这里" +
                "展示的是H5内容这里展示的是H5内容这里展示的是H5内容这里展示的是H5内容这里展示的是H5内容这里展示的是H5内容这里展示的是H5内容这里展示的是H5内容这" +
                "里展示的是H5内容这里展示的是H5内容这里展示的是H5内容这里展示的是H5内容这里展示的是H5内容" +
                "这里展示的是H5内容这里展示的是H5内容这里展示的是H5内容这里展示的是H5内容这里展示的是H5内容", "text/html", "utf-8", null);

    }
}
