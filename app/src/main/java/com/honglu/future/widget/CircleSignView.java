package com.honglu.future.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonNull;
import com.honglu.future.R;
import com.honglu.future.config.Constant;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.http.RxHelper;
import com.honglu.future.ui.circle.bean.SignBean;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;


/**
 * Created by zhuaibing on 2017/9/29
 */

public class CircleSignView extends LinearLayout {
    private int mPosition;
    private int mCount;
    private TextView mDay;
    private ImageView mSignHint;
    private ImageView mSignHintMoney;
    private View mIcSignView;
    private ImageView mIcsign;
    private ImageView mSlsign;
    private TextView mIntegral;
    private View rootSignView;
    private View mSignmc;
    Context context;



    public CircleSignView(@NonNull Context context, int position, int count, OnSignClickListener OnSignClickListener) {
        super(context);
        this.mPosition = position;
        this.mCount = count;
        this.context = context;
        this.OnSignClickListener = OnSignClickListener;
        initView();
    }

    public CircleSignView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleSignView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_sign, this);

        LinearLayout mContentView = (LinearLayout) findViewById(R.id.ll_content);
        mDay = (TextView) findViewById(R.id.tv_day);
        mSignHint = (ImageView) findViewById(R.id.iv_signsHint);
        mSignHintMoney = (ImageView) findViewById(R.id.iv_signsHint_money);
        mIcSignView = findViewById(R.id.rl_icsign);
        mIcsign = (ImageView) findViewById(R.id.iv_icsign);
        mSlsign = (ImageView) findViewById(R.id.iv_slsign);
        mIntegral = (TextView) findViewById(R.id.tv_integral);
        rootSignView = findViewById(R.id.rl_signView);
        mSignmc = findViewById(R.id.v_signmc);
        mSignmc.setAlpha(0.7F);
        mSignmc.setVisibility(INVISIBLE);

        int margin = getResources().getDimensionPixelSize(R.dimen.dimen_3_5);
        int margin10 = getResources().getDimensionPixelSize(R.dimen.dimen_10dp);
        int widgetWidth = (DeviceUtils.getScreenWidth(getContext()) - margin * mCount) / (mCount - 1); //每个item 宽度

        //content Height and margin
        int contentHeight = widgetWidth + (int)(widgetWidth * 0.3) - margin * 2 - getResources().getDimensionPixelSize(R.dimen.dimen_5dp);
        //蒙层
        FrameLayout.LayoutParams mcParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, contentHeight + margin10 * 2);
        mSignmc.setLayoutParams(mcParams);

        FrameLayout.LayoutParams contentParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, contentHeight);
        contentParams.leftMargin = margin;
        contentParams.rightMargin = margin;
        contentParams.topMargin = margin10;
        contentParams.bottomMargin = margin10;
        mContentView.setLayoutParams(contentParams);

        //rootView  Width - Height - margin
        LayoutParams param = new LayoutParams(widgetWidth, FrameLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        if (mPosition == 0){
            param.leftMargin = margin;
        }else if (mPosition == mCount-1){
            param.rightMargin = margin;
        }
        this.setLayoutParams(param);
    }




    public void setSignData(final SignBean.SignListBean bean) {
        mDay.setText(getContext().getString(R.string.the_first_few_days, bean.getParamNameOne()));
        mIntegral.setText(getContext().getString(R.string.integral_s, bean.getParamValueOne()));

        if (bean.isSignClick()) {//当前item 能签到
            mDay.setVisibility(GONE);
            mSignmc.setVisibility(INVISIBLE);//蒙层
            mSignHintMoney.setVisibility(GONE);//金钱堆
            mSignHint.setVisibility(VISIBLE); //签到
            mIcSignView.setVisibility(GONE);//签到成功layout
            rootSignView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!DeviceUtils.isFastDoubleClick()&&!bean.isSign()) {
                        saveSingInfo(bean);
                    }
                }
            });

        } else if (bean.isSign()) {
            //当前item 已签到
            mDay.setVisibility(VISIBLE);
            mSignmc.setVisibility(INVISIBLE);//蒙层
            mSignHintMoney.setVisibility(GONE);//金钱堆
            mSignHint.setVisibility(GONE);//签到
            mIcSignView.setVisibility(VISIBLE); //签到成功layout
        } else {
            //当前item没签到
            mDay.setVisibility(VISIBLE);
            if (mPosition == mCount -1){
                mSignmc.setVisibility(INVISIBLE);//蒙层
                mSignHintMoney.setVisibility(VISIBLE); //金钱堆
                mSignHintMoney.setImageResource(R.mipmap.ic_sign_money);
                mSignHint.setVisibility(GONE);//签到
                mIcSignView.setVisibility(GONE); //签到成功layout
            }else {
                mSignmc.setVisibility(VISIBLE);//蒙层
                mSignHintMoney.setVisibility(GONE);//金钱堆
                mSignHint.setVisibility(GONE);//签到
                mIcSignView.setVisibility(VISIBLE); //签到成功layout
                mSlsign.setVisibility(GONE);
            }
        }
    }


    //签到成功 开启动画
    private void startViewAnimation() {
        final Animation signAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_sign_scale_in);
        final Animation slSignAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_sign_scale_in);
        signAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                mSlsign.setVisibility(VISIBLE);
                mSlsign.startAnimation(slSignAnimation);
            }
        });

        slSignAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                if (OnSignClickListener != null) {
                    OnSignClickListener.OnSignClick();
                }
            }
        });

        mDay.setVisibility(VISIBLE);
        mSignmc.setVisibility(INVISIBLE);//蒙层
        mSignHintMoney.setVisibility(GONE);//金钱堆
        mSignHint.setVisibility(GONE);//签到
        mIcSignView.setVisibility(VISIBLE); //签到成功layout
        mIcsign.setVisibility(VISIBLE);
        mSlsign.setVisibility(GONE);
        mIcsign.startAnimation(signAnimation);
    }


    //签到http
    private void saveSingInfo(final SignBean.SignListBean bean) {
        HttpManager.getApi().saveSignData(SpUtil.getString(Constant.CACHE_TAG_MOBILE),SpUtil.getString(Constant.CACHE_TAG_UID),mPosition+1).compose(RxHelper.<JsonNull>handleSimplyResult())
                .subscribe(new HttpSubscriber<JsonNull>() {
                    @Override
                    protected void _onError(String message) {
                        super._onError(message);
                        ToastUtil.show(message);
                    }

                    @Override
                    protected void _onNext(JsonNull jsonNull) {
                        super._onNext(jsonNull);
                        bean.setSignClick(false);
                        bean.setSign(true);
                        //当前item 已签到
                        ToastUtil.show("签到成功");
                        mDay.setVisibility(VISIBLE);
                        mSignmc.setVisibility(INVISIBLE);//蒙层
                        mSignHintMoney.setVisibility(GONE);//金钱堆
                        mSignHint.setVisibility(GONE);//签到
                        mIcSignView.setVisibility(VISIBLE); //签到成功layout
                        startViewAnimation();
                    }
                });
//        Map<String, String> map = new HashMap<>();
//        map.put("mobile", AndroidUtil.getPhone(getContext()));
//        map.put("token", AndroidUtil.getToken(getContext()));
//        //当前点击的天数
//        map.put("consDays", (mPosition+1)+"");
//        new HttpRequest(new Callback() {
//            @Override
//            public void onErrorResponse() {
//                mLoadingDialog.dismiss();
//                ToastUtils.show(baseFragment.getActivity(),R.string.net_connect_error, Toast.LENGTH_SHORT);
//            }
//
//            @Override
//            public void onResponse(String response) {
//                mLoadingDialog.dismiss();
//                Gson gson = new Gson();
//                try {
//                    SingInfoBean bean = gson.fromJson(response, SingInfoBean.class);
//                    if (bean !=null && !TextUtils.isEmpty(bean.code) && bean.code.equals("200")){
//                        startViewAnimation();
//                    }else {
//                        String msg = bean !=null && !TextUtils.isEmpty(bean.message) ? bean.message : "签到失败";
//                        ToastUtils.show(msg, Toast.LENGTH_SHORT);
//                    }
//                } catch (JsonSyntaxException e) {
//                }
//            }
//        }, baseFragment).postWithOutToken(GET_SAVE_SING_INFO, map);
    }



    private OnSignClickListener OnSignClickListener;

    public interface OnSignClickListener {
        void OnSignClick();
    }
}
