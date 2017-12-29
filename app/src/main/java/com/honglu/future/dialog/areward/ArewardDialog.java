package com.honglu.future.dialog.areward;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonNull;
import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseDialog;
import com.honglu.future.config.ConfigUtil;
import com.honglu.future.config.Constant;
import com.honglu.future.events.BBSArewardEvent;
import com.honglu.future.ui.circle.bean.CircleDetailBean;
import com.honglu.future.ui.main.activity.WebViewActivity;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.util.KeyBordUtil;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;
import com.honglu.future.widget.CircleImageView;
import com.honglu.future.widget.ExpandableLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;

/**
 * 打赏
 * Created by zhuaibing on 2017/12/20
 */

public class ArewardDialog extends BaseDialog<ArewardPresenter> implements ArewardContract.View ,View.OnClickListener{
    public static final String AREWARD_USER_TYPE = "areward_user";//打赏用户类型
    public static final String AREWARD_CIRCLE_TYPE = "areward_circle";//打赏帖子类型
    private static final int SMALL_NIUBI = 6;
    private static final int SD_NIUBI = 66;
    private static final int BIG_NIUBI = 666;

    private TextView mTextIntegral; //牛币
    private ImageView mClose;
    private CircleImageView mHead;
    private TextView mArewardHint; //打赏提示
    private View mDefaultLayout;
    private View mInputLayout;

    private View mRlSmallLayout;
    private View mRlSdLayout;
    private View mRlBigLayout;
    private ImageView mImgSmall;
    private ImageView mImgSd;
    private ImageView mImgBig;

    private EditText mInput;
    private TextView mQieHuan;
    private TextView mAreward;

    private InputMethodManager  mInputMethodManager;
    private Integer mArewardScore = 0;//总积分
    private String mArewardType;//打赏类型
    private String mPostId; //帖子id
    private String mBeUserId; //被打赏用户id
    private String mName;//名字
    private String mHeadUrl;//头像
    private int mIntegralNum;
    private ArewardHintDialog mArewardHintDialog;


    public ArewardDialog(@NonNull Activity context) {
        super(context, R.style.areward_dialog_main);
    }

    @Override
    public void initPresenter() {
       mPresenter.init(ArewardDialog.this);
    }


    @Override
    public void showLoading(String content) {
        if (!TextUtils.isEmpty(content)) {
            App.loadingContent(mContext, content);
        }
    }

    @Override
    public void stopLoading() {
        super.stopLoading();
        App.hideLoading();
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        if (!TextUtils.isEmpty(msg))
            ToastUtil.show(msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_areward_main);
        Window mWindow = this.getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        WindowManager manage = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        params.width = manage.getDefaultDisplay().getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        mWindow.setAttributes(params);

        mInputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        mArewardHintDialog = new ArewardHintDialog(mContext);
        mTextIntegral = (TextView) findViewById(R.id.dtv_integral);
        mClose = (ImageView) findViewById(R.id.iv_close);
        mHead = (CircleImageView) findViewById(R.id.civ_head);
        mArewardHint = (TextView) findViewById(R.id.tv_areward_hint);

        mDefaultLayout = findViewById(R.id.ll_default_layout);
        mRlSmallLayout = findViewById(R.id.rl_small_layout);
        mRlSdLayout = findViewById(R.id.rl_sd_layout);
        mRlBigLayout = findViewById(R.id.rl_big_layout);
        mImgSmall = (ImageView) findViewById(R.id.iv_small);
        mImgSd = (ImageView) findViewById(R.id.iv_sd);
        mImgBig = (ImageView) findViewById(R.id.iv_big);

        mInputLayout = findViewById(R.id.ll_input_layout);
        mInput = (EditText) findViewById(R.id.et_input);

        mQieHuan = (TextView) findViewById(R.id.tv_qiehuan);
        mAreward = (TextView) findViewById(R.id.tv_areward);
        setTextFonts(mInput);

        initState();
        mTextIntegral.setOnClickListener(this);
        mClose.setOnClickListener(this);
        mQieHuan.setOnClickListener(this);
        mAreward.setOnClickListener(this);
        mRlSmallLayout.setOnClickListener(this);
        mRlSdLayout.setOnClickListener(this);
        mRlBigLayout.setOnClickListener(this);
        mArewardHintDialog.setOnArewardHintClickListener(new ArewardHintDialog.OnArewardHintClickListener() {
            @Override
            public void onCcomplish(String showType) {
                if (TextUtils.equals(ArewardHintDialog.AREWARD_HINT,showType)){

                }else {
                    initState();
                }
            }
            @Override
            public void onCancel(String showType) {
                if (TextUtils.equals(ArewardHintDialog.AREWARD_HINT,showType)){

                }else {
                    dismiss();
                }
            }
        });

        mInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                int textNum = getTextNum(mInput);
                if (textNum > 1000){
                    mInput.setText("1000");
                    mInput.setSelection(4);
                }
            }
        });
    }

    private void initState(){
        mDefaultLayout.setVisibility(View.VISIBLE);
        mInputLayout.setVisibility(View.GONE);
        mIntegralNum = SD_NIUBI;
        mInput.setText("");
        mImgSmall.setVisibility(View.INVISIBLE);
        mImgSd.setVisibility(View.VISIBLE);
        mImgBig.setVisibility(View.INVISIBLE);
        mQieHuan.setText(R.string.custom_text);
        if (mInputMethodManager.isActive()){
            IBinder ibinder = mInput.getWindowToken();
            if (ibinder != null) {
                mInputMethodManager.hideSoftInputFromWindow(ibinder, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        initState();
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.dtv_integral: //积分
               Intent intentShopMall = new Intent(mContext, WebViewActivity.class);
               intentShopMall.putExtra("url", ConfigUtil.SHOP_MALL);
               intentShopMall.putExtra("title", "牛币商城");
               intentShopMall.putExtra("is_tool_bar", false);
               mContext.startActivity(intentShopMall);
               break;
           case R.id.iv_close: //关闭
                dismiss();
               break;
           case R.id.tv_qiehuan: //默认切换
                if (mDefaultLayout.getVisibility() == View.VISIBLE){
                    mDefaultLayout.setVisibility(View.GONE);
                    mInputLayout.setVisibility(View.VISIBLE);
                    mInput.setText("");
                    mInput.setFocusable(true);
                    mInput.setFocusableInTouchMode(true);
                    mInput.requestFocus();
                    mQieHuan.setText(R.string.default_text);
                    mInputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }else {
                    mDefaultLayout.setVisibility(View.VISIBLE);
                    mInputLayout.setVisibility(View.GONE);
                    mQieHuan.setText(R.string.custom_text);
                    if (mInputMethodManager.isActive()){
                        IBinder ibinder = mInput.getWindowToken();
                        if (ibinder != null) {
                            mInputMethodManager.hideSoftInputFromWindow(ibinder, InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }
                }
               break;
           case R.id.tv_areward: //打赏
               if(DeviceUtils.isFastDoubleClick()){
                   return;
               }
               int integralNum = mDefaultLayout.getVisibility() == View.VISIBLE ? mIntegralNum : getTextNum(mInput);
               if (integralNum <= 0){ToastUtil.show("打赏牛币必须大于0");return;}

               if (integralNum > 1000){ToastUtil.show("打赏牛币不能大于1000");return;}

               if (mArewardScore < integralNum){
                   mArewardHintDialog.showArewardHint(ArewardHintDialog.AREWARD_HINT,R.mipmap.icon_hint,"牛币不足","更多赚取牛币的方法敬请期待");
               }else {
                   if (AREWARD_USER_TYPE.equals(mArewardType)) { //对用户进行打赏
                       mPresenter.getReward(SpUtil.getString(Constant.CACHE_TAG_UID), mPostId, mBeUserId, integralNum,1);
                   } else { //对帖子进行打赏
                       mPresenter.getReward(SpUtil.getString(Constant.CACHE_TAG_UID), mPostId, mBeUserId, integralNum,2);
                   }
               }
               break;
           case R.id.rl_small_layout: //6
               if (mIntegralNum == SMALL_NIUBI && mImgSmall.getVisibility() == View.VISIBLE){ return;}
               this.mIntegralNum = SMALL_NIUBI;
               mImgSmall.setVisibility(View.VISIBLE);
               mImgSd.setVisibility(View.INVISIBLE);
               mImgBig.setVisibility(View.INVISIBLE);
               break;
           case R.id.rl_sd_layout: //66
               if (mIntegralNum == SD_NIUBI && mImgSd.getVisibility() == View.VISIBLE){ return;}
               this.mIntegralNum = SD_NIUBI;
               mImgSmall.setVisibility(View.INVISIBLE);
               mImgSd.setVisibility(View.VISIBLE);
               mImgBig.setVisibility(View.INVISIBLE);
               break;
           case R.id.rl_big_layout: //666
               if (mIntegralNum == BIG_NIUBI && mImgBig.getVisibility() == View.VISIBLE){ return;}
               this.mIntegralNum = BIG_NIUBI;
               mImgSmall.setVisibility(View.INVISIBLE);
               mImgSd.setVisibility(View.INVISIBLE);
               mImgBig.setVisibility(View.VISIBLE);
              break;
       }
    }


    /**
     * 对用户打赏
     * @param arewardType 打赏类型
     * @param beUserId   被打赏人id
     * @param name 被打赏人名字
     */
    public void arewardUser(String arewardType,String beUserId ,String name,String headUrl){
        areward(arewardType,beUserId,"",name,headUrl);
    }

    /**
     * 对帖子打赏
     * @param arewardType 打赏类型
     * @param beUserId 被打赏人id
     * @param postId   帖子id
     * @param name 被打赏人名字
     */
    public void arewardCircle(String arewardType,String beUserId ,String postId ,String name,String headUrl){
        areward(arewardType,beUserId,postId,name,headUrl);
    }

    private void areward(String arewardType,String beUserId ,String postId ,String name,String headUrl){
        this.mArewardType = arewardType;
        this.mBeUserId = beUserId;
        this.mPostId = postId;
        this.mName= name;
        this.mHeadUrl = headUrl;
        this.mIntegralNum = SD_NIUBI;
        show();
        ImageUtil.display(mHeadUrl, mHead, R.mipmap.img_head);
        mArewardHint.setText(String.format(mContext.getString(R.string.areward_niubi,name)));

        mPresenter.getArewardScore(SpUtil.getString(Constant.CACHE_TAG_UID));
    }

    //获取积分
    @Override
    public void getArewardScore(Integer arewardScore) {
         this.mArewardScore = arewardScore;
        mTextIntegral.setText(String.valueOf(arewardScore));
    }

    //打赏
    @Override
    public void getReward(int score) {
        this.mArewardScore = mArewardScore - score;
        mTextIntegral.setText(String.valueOf(mArewardScore));
        Spannable spannableContent = getSpannableContent(mName + "已经收到您打赏的", String.valueOf(score), "牛币");
        mArewardHintDialog.showArewardSuccess(ArewardHintDialog.AREWARD_SUCCESS,R.mipmap.success_tixian,"打赏成功",spannableContent);
        if (TextUtils.equals(AREWARD_CIRCLE_TYPE,mArewardType)){
            BBSArewardEvent bbsArewardEvent = new BBSArewardEvent();
            bbsArewardEvent.arewardNum = score;
            bbsArewardEvent.beUserId = mBeUserId;
            bbsArewardEvent.circleId = mPostId;
            EventBus.getDefault().post(bbsArewardEvent);
        }else {
            if (mListener !=null){
                mListener.onArewardSuccess(mBeUserId,score);
            }
        }
    }


    public void setOnArewardSuccessListener(OnArewardSuccessListener listener){
        this.mListener = listener;
    }
    private OnArewardSuccessListener mListener;
    public interface OnArewardSuccessListener{
        void onArewardSuccess(String beUserId,int score);
    }



    private int getTextNum(EditText editText){

        return editText.getText() !=null && !TextUtils.isEmpty(editText.getText().toString()) ? Integer.parseInt(editText.getText().toString()) : 0;
    }

    private int getTextLength(EditText editText){
        return editText.getText() !=null && !TextUtils.isEmpty(editText.getText().toString()) ? editText.getText().toString().length() : 0;
    }

    private int getLength(String text){
        return TextUtils.isEmpty(text) ? 0 : text.length();
    }


    private Spannable getSpannableContent(String text1 , String text2 , String text3){
        int text1Start = 0;
        int text1End = getLength(text1);

        int text2Start = text1End;
        int text2End = text1End + getLength(text2);

        int text3Start = text2End;
        int text3End = text2End + getLength(text3);

        Spannable spannable = new SpannableString(text1+text2+text3);
        spannable.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_979899)), text1Start, text1End,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannable.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_333333)),text2Start, text2End,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannable.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_979899)), text3Start,text3End ,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    //设置字体样式
    private void setTextFonts(TextView textView) {
        //得到AssetManager
        AssetManager mgr = getContext().getAssets();
        //根据路径得到Typeface
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/DIN-Medium.ttf");
        //设置字体
        textView.setTypeface(tf);
    }
}
