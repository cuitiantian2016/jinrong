package com.honglu.future.ARouter;

import android.app.Activity;
import android.app.Service;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.Constant;
import com.honglu.future.events.EventBusConstant;
import com.honglu.future.events.MarketRefreshEvent;
import com.honglu.future.ui.market.adapter.AddHavedOptionalAdapter;
import com.honglu.future.ui.market.adapter.AllHavedOptionalAdapter;
import com.honglu.future.ui.market.bean.MarketnalysisBean;
import com.honglu.future.ui.market.contract.OptionalQuotesContract;
import com.honglu.future.ui.market.fragment.MarketFragment;
import com.honglu.future.ui.market.presenter.OptionalQuotesPresenter;
import com.honglu.future.util.SpUtil;
import com.honglu.future.widget.recycler.BaseRecyclerAdapter;
import com.honglu.future.widget.recycler.SpaceItemDecoration;
import com.honglu.future.widget.tab.CustomTabEntity;
import com.honglu.future.widget.tab.HorizontalTabLayout;
import com.honglu.future.widget.tab.SimpleOnTabSelectListener;
import com.honglu.future.widget.tab.TabEntity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import butterknife.BindView;

/**
 * Created by hefei on 2017/10/26.
 *
 */

public class SchemeFilterActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        直接通过ARouter处理外部Uri
        Uri uri = getIntent().getData();
        ARouter.getInstance().build(uri).navigation(this, new NavCallback() {
            @Override
            public void onArrival(Postcard postcard) {
                finish();
            }
        });
    }

}
