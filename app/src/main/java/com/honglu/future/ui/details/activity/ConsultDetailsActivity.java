package com.honglu.future.ui.details.activity;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.ui.details.contract.ConsultDetailsContract;
import com.honglu.future.ui.details.presenter.ConsultDetailsPresenter;

import butterknife.BindView;

/**
 * Created by hc on 2017/10/24.
 */

public class ConsultDetailsActivity extends BaseActivity<ConsultDetailsPresenter> implements ConsultDetailsContract.View {

    @BindView(R.id.webView_content)
    WebView mContentWv;

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
