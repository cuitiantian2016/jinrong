package com.honglu.future.ui.circle.circledetail;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
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

public class CircleDetailActivity extends BaseActivity<CircleDetailPresenter> implements CircleDetailContract.View,View.OnClickListener{

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
    private TextView mTime;
    private TextView mFollow;
    private TextView mContnet;
    private LinearLayout mImgsLinear;
    private ImageView mImgSupport;
    private TextView mTextSupport;
    private LinearLayout mSupportLinear;
    private TextView mComment;
    private TextView mCommentNum;
    private TextView mSeeOwner;
    private TextView mSeeOwnerNum;

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
        mTitle.setTitle(false, R.color.color_white,"详情");
        View headView = View.inflate(CircleDetailActivity.this,R.layout.layout_circle_detail_head,null);
        mCivHead = (CircleImageView) headView.findViewById(R.id.civ_head);
        mName = (TextView) headView.findViewById(R.id.tv_name);
        mTime = (TextView) headView.findViewById(R.id.tv_time);
        mFollow = (TextView) headView.findViewById(R.id.tv_follow);
        mContnet = (TextView) headView.findViewById(R.id.tv_contnet);
        mImgsLinear = (LinearLayout) headView.findViewById(R.id.imgs_linear);
        mImgSupport = (ImageView) headView.findViewById(R.id.iv_support);
        mTextSupport = (TextView) headView.findViewById(R.id.tv_support);
        mSupportLinear = (LinearLayout) headView.findViewById(R.id.support_linear);
        mComment = (TextView) headView.findViewById(R.id.tv_comment);
        mCommentNum = (TextView) headView.findViewById(R.id.tv_comment_num);
        mSeeOwner = (TextView) headView.findViewById(R.id.tv_see_owner);
        mSeeOwnerNum = (TextView) headView.findViewById(R.id.tv_see_owner_num);

        mListView.addHeaderView(headView);
        mAdapter = new CircleDetailAdapter(CircleDetailActivity.this);
        mListView.setAdapter(mAdapter);

        List<String> mList = new ArrayList<>();
        for (int i = 0 ; i < 20 ; i ++){
            mList.add("test"+i);
        }
        mAdapter.notifyDataChanged(false,mList);

        mCivHead.setOnClickListener(this);
        mFollow.setOnClickListener(this);
        mImgSupport.setOnClickListener(this);
        mComment.setOnClickListener(this);
        mSeeOwner.setOnClickListener(this);
        mRefreshView.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

                mRefreshView.finishLoadmore();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                mRefreshView.finishRefresh();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.civ_head: //头像

                break;
            case R.id.tv_follow: //关注

                break;
            case R.id.iv_support: //点赞

                break;
            case R.id.tv_comment: //全部评论

                break;
            case R.id.tv_see_owner: //只看楼主

                break;
        }
    }
}
