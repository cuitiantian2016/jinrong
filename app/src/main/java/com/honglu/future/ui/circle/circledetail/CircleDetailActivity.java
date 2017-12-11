package com.honglu.future.ui.circle.circledetail;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.util.Util;
import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.util.ShareUtils;
import com.honglu.future.util.TimeUtil;
import com.honglu.future.util.ToastUtil;
import com.honglu.future.widget.CircleImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 帖子详情
 * Created by zhuaibing on 2017/12/7
 */

public class CircleDetailActivity extends BaseActivity<CircleDetailPresenter> implements CircleDetailContract.View, View.OnClickListener {

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

    private CircleDetailAdapter mAdapter;

    @Override
    public void initPresenter() {
        mPresenter.init(CircleDetailActivity.this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_circle_detail;
    }

    @Override
    public void loadData() {
        mTitle.setTitle(false, R.color.color_white, "详情");
        mTitle.setRightTitle(R.mipmap.ic_share, this);
        View headView = View.inflate(CircleDetailActivity.this, R.layout.layout_circle_detail_head, null);
        mCivHead = (CircleImageView) headView.findViewById(R.id.civ_head);
        mName = (TextView) headView.findViewById(R.id.tv_name);
        mUserLabel = (TextView) headView.findViewById(R.id.tv_user_label);
        mTime = (TextView) headView.findViewById(R.id.tv_time);
        mFollow = (TextView) headView.findViewById(R.id.tv_follow);
        mContnet = (TextView) headView.findViewById(R.id.tv_contnet);
        mSeeZhuanTi = (TextView) headView.findViewById(R.id.tv_seezhuanti);
        mImgsLinear = (LinearLayout) headView.findViewById(R.id.imgs_linear);
        mImgSupport = (ImageView) headView.findViewById(R.id.iv_support);
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
        mAdapter = new CircleDetailAdapter(CircleDetailActivity.this);
        mListView.setAdapter(mAdapter);

        mCivHead.setOnClickListener(this);
        mFollow.setOnClickListener(this);
        mImgSupport.setOnClickListener(this);
        mComment.setOnClickListener(this);
        mSeeOwner.setOnClickListener(this);

        mRefreshView.setEnableLoadmore(false);
        mRefreshView.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                Log.d("wahcc","====onLoadmore====");
                mRefreshView.finishLoadmore();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mRefreshView.finishRefresh();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return;
                } //addHead

                String str = (String) parent.getItemAtPosition(position);
                ToastUtil.show("===" + str);
            }
        });


        List<String> mList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mList.add("test" + i);
        }
        mAdapter.notifyDataChanged(false, mList);

        String imgUrl = "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2086662279,3458359640&fm=27&gp=0.jpg";

        ImageUtil.display(imgUrl, mCivHead, R.mipmap.img_head);
        mName.setText("zab");
        mUserLabel.setText("分析师");
        mContnet.setText("铁矿石期货1801合约9月大幅回落后，于10月12日创下本轮回调新低");
        mTextSupport.setText("11人点赞");

        mTime.setText(TimeUtil.formatData(TimeUtil.dateFormatHHmm_MMdd, System.currentTimeMillis()));
        List<String> picList = new ArrayList<>();
        picList.add(imgUrl);
        picList.add(imgUrl);
        picList.add(imgUrl);
        picList.add(imgUrl);
        picList.add(imgUrl);
        initContentImage(picList);
        updateUserHead(picList);
        mRefreshView.setEnableLoadmore(true);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right://title 分享按钮
                ShareUtils.getIntance().share(this, "", "http://www.baidu.com", "帖子分享", "帖子分享内容");
                break;
            case R.id.civ_head: //头像

                break;
            case R.id.tv_follow: //关注

                break;
            case R.id.iv_support: //点赞

                break;
            case R.id.tv_comment: //全部评论
                mComment.setSelected(true);
                mCommentLine.setVisibility(View.VISIBLE);
                mSeeOwner.setSelected(false);
                mSeeOwnerLine.setVisibility(View.INVISIBLE);
                break;
            case R.id.tv_see_owner: //只看楼主
                mComment.setSelected(false);
                mCommentLine.setVisibility(View.INVISIBLE);
                mSeeOwner.setSelected(true);
                mSeeOwnerLine.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Util.isOnMainThread() && !this.isFinishing()) {
            Glide.with(this).pauseRequests();
        }
    }


    //点赞头像
    private void updateUserHead(List<String> headList) {
        mSupportLinear.setVisibility(View.VISIBLE);
        mSupportLinear.removeAllViews();
        if (headList == null || headList.size() <= 0) {
            return;
        }

        int size = getResources().getDimensionPixelSize(R.dimen.dimen_30dp);
        for (int i = 0; i < headList.size(); i++) {
            if (i == 4) {
                break;
            }
            String headUrl = headList.get(i);
            CircleImageView imgHead = new CircleImageView(this);
            ImageUtil.display(headUrl, imgHead, R.mipmap.img_head);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
            params.rightMargin = size / 2;
            mSupportLinear.addView(imgHead, params);
        }

        CircleImageView imgHead = new CircleImageView(this);
        imgHead.setImageResource(R.mipmap.ic_more);
        imgHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show("-----更多");
            }
        });
        mSupportLinear.addView(imgHead, new LinearLayout.LayoutParams(size, size));
    }


    /**
     * 内容中的图片加载
     *
     * @param picList 图片 链接集合
     */
    private void initContentImage(List<String> picList) {
        mImgsLinear.setVisibility(View.VISIBLE);
        mImgsLinear.removeAllViews();
        if (picList != null && picList.size() != 0) {
            for (String imageStr : picList) {
                final ImageView imageView = new ImageView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                imageView.setImageResource(R.mipmap.other_empty);
                params.topMargin = DeviceUtils.dip2px(this, 4);
                mImgsLinear.addView(imageView);
                final int imageWidth = DeviceUtils.getScreenWidth(this) - DeviceUtils.dip2px(this, 30);
                String picurls = imageStr;
                Log.d("picurls=======", "" + picurls);
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
}
