package com.honglu.future.ui.home.fragment;
import android.widget.LinearLayout;
import com.honglu.future.R;
import com.honglu.future.base.BaseFragment;
import com.honglu.future.ui.home.viewmodel.BannerViewModel;
import com.honglu.future.ui.home.viewmodel.HomeBottomTabViewModel;
import com.honglu.future.ui.home.viewmodel.HomeMarketPriceViewModel;
import com.honglu.future.ui.home.viewmodel.HorizontalIconViewModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import butterknife.BindView;
/**********
 * 首页
 */
public class HomeFragment extends BaseFragment{
    @BindView(R.id.home_scroll_view)
    LinearLayout mScrollView; //跟布局
    @BindView(R.id.home_smart_view)
    SmartRefreshLayout mSmartRefreshView; //刷新

    public static HomeFragment homeFragment;

    public static HomeFragment getInstance() {
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        return homeFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_layout;
    }
    @Override
    public void initPresenter() {
    }

    @Override
    public void loadData() {
        initView();
    }
    /**
     *
     */
    private void initView() {
        BannerViewModel bannerViewModel = new BannerViewModel(getContext());
        HomeMarketPriceViewModel homeMarketPriceViewModel = new HomeMarketPriceViewModel(getContext());
        HorizontalIconViewModel horizontalIconViewModel = new HorizontalIconViewModel(getContext());
        HomeBottomTabViewModel homeBottomTabViewModel = new HomeBottomTabViewModel(getContext(),mSmartRefreshView);
        mScrollView.addView(bannerViewModel.mView);//添加banner
        mScrollView.addView(homeMarketPriceViewModel.mView);//添加banner
        mScrollView.addView(horizontalIconViewModel.mView);//添加banner
        mScrollView.addView(homeBottomTabViewModel.mView);//添加banner
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        homeFragment = null;
    }
}
