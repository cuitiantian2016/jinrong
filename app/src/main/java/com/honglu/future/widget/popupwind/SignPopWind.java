package com.honglu.future.widget.popupwind;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.config.Constant;
import com.honglu.future.dialog.TradeTipDialog;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.http.RxHelper;
import com.honglu.future.ui.circle.bean.SignBean;
import com.honglu.future.ui.circle.circlemain.CircleMainFragment;
import com.honglu.future.ui.trade.bean.HoldPositionBean;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ViewUtil;
import com.honglu.future.widget.CircleSignView;

import java.util.List;

/**
 * Created by hefei on 2017/10/27
 * 签到的弹窗
 */

public class SignPopWind extends PopupWindow {
    private final SignAdapter signAdapter;

    public SignPopWind(final Context context) {
        View rootView = View.inflate(context, R.layout.popupwind_sign_layout, null);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        GridView gridView = (GridView) rootView.findViewById(R.id.sign_grad);

        signAdapter = new SignAdapter(this,new Handler());
        gridView.setAdapter(signAdapter);
        ColorDrawable drawable = new ColorDrawable(context.getResources().getColor(android.R.color.transparent));
        setBackgroundDrawable(drawable);
        setOutsideTouchable(false);
        setFocusable(true);
        setAnimationStyle(R.style.position_popupwind);
        setContentView(rootView);
        setOutsideTouchable(false);
        getSigleData();
    }

    /**
     * 获取签到数据
     */
    public void getSigleData(){
        if (!App.getConfig().getLoginStatus()){
            return;
        }
        String string = SpUtil.getString(Constant.CACHE_TAG_MOBILE);
        //访问接口
        HttpManager.getApi().getSignData(string).compose(RxHelper.<SignBean>handleSimplyResult()).subscribe(new HttpSubscriber<SignBean>() {
            @Override
            protected void _onNext(SignBean signBean) {
                super._onNext(signBean);
                boolean isSign = signBean.isIsSign();
                List<SignBean.SignListBean> signList = signBean.getSignList();
                int signCount = signBean.count;
                for (int i = 0 ; i < signList.size() ; i++){
                    if (i < signCount){
                        SignBean.SignListBean bean = signList.get(i);
                        bean.setSign(true);
                    }else if (i == signCount&&!isSign){
                        SignBean.SignListBean bean = signList.get(i);
                        bean.setSignClick(true);
                    }else if (signCount == 0&&isSign){
                        SignBean.SignListBean bean = signList.get(i);
                        bean.setSign(true);
                    }
                }
                signAdapter.refreshPhotos(signList);
            }
            @Override
            protected void _onError(String message) {
                super._onError(message);
            }
        });
    }


    public void showPopupWind(View view) {
        // if (Build.VERSION.SDK_INT >= 24) {
        //    Rect rect = new Rect();
        //    view.getGlobalVisibleRect(rect);
        //    int h = view.getResources().getDisplayMetrics().heightPixels - rect.bottom;
        //    setHeight(h);
        //}
       showAsDropDown(view);
    }
}
