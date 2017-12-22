package com.honglu.future.dialog.areward;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonNull;
import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseDialog;
import com.honglu.future.config.Constant;
import com.honglu.future.ui.circle.bean.CircleDetailBean;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;
import com.honglu.future.widget.CircleImageView;

import java.util.Collections;
import java.util.List;

/**
 * 打赏
 * Created by zhuaibing on 2017/12/20
 */

public class ArewardDialog extends BaseDialog<ArewardPresenter> implements ArewardContract.View,View.OnClickListener{
    public static final String AREWARD_USER_TYPE = "areward_user";//打赏用户类型
    public static final String AREWARD_CIRCLE_TYPE = "areward_circle";//打赏帖子类型

    private View mNormalLayout;
    private TextView mNormalIntegral;
    private TextView mNormalCustom;
    private CircleImageView mNormalHead;
    private View mNormalCustomLayout;
    private EditText mNormalInput;
    private TextView mNormalAreward;

    private View mSuccessLayout;
    private TextView mSuccessHint;
    private TextView mSuccessAgain;
    private TextView mSuccessKnow;

    private View mFailLayout;
    private TextView mFailJump;

    private InputMethodManager  mInputMethodManager;
    private Integer mArewardScore = 0;//总积分
    private String mArewardType;//打赏类型
    private String mPostId; //帖子id
    private String mBeUserId; //被打赏用户id
    private String mName;//名字

    public ArewardDialog(@NonNull Activity context) {
        super(context, R.style.areward_dialog);
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
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_areward_main, null);
        setContentView(view);
        getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        mInputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        mNormalLayout = findViewById(R.id.ll_normal_layout);
        mNormalIntegral = (TextView) findViewById(R.id.tv_normal_integral);
        mNormalCustom = (TextView) findViewById(R.id.tv_normal_custom);
        mNormalHead = (CircleImageView) findViewById(R.id.civ_normal_head);
        mNormalCustomLayout = findViewById(R.id.ll_normal_custom_layout);
        mNormalInput = (EditText) findViewById(R.id.et_normal_input);
        mNormalAreward = (TextView) findViewById(R.id.tv_normal_areward);

        mSuccessLayout = findViewById(R.id.ll_success_layout);
        mSuccessHint = (TextView) findViewById(R.id.tv_success_hint);
        mSuccessAgain = (TextView) findViewById(R.id.tv_success_again);
        mSuccessKnow = (TextView) findViewById(R.id.tv_success_know);

        mFailLayout = findViewById(R.id.ll_fail_layout);
        mFailJump = (TextView) findViewById(R.id.tv_fail_jump);


        mNormalAreward.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.tv_normal_areward: //打赏

              if (TextUtils.equals(AREWARD_USER_TYPE,mArewardType)){
                 mPresenter.getReward(SpUtil.getString(Constant.CACHE_TAG_UID),"",mBeUserId ,1);
              }else {
                  mPresenter.getReward(SpUtil.getString(Constant.CACHE_TAG_UID),mPostId,mBeUserId ,1);
              }
              break;
      }
    }

    private void initViewState(){
        mNormalLayout.setVisibility(View.VISIBLE);
        mNormalCustomLayout.setVisibility(View.GONE);
        mSuccessLayout.setVisibility(View.GONE);
        mFailLayout.setVisibility(View.GONE);
    }


    /**
     * 对用户打赏
     * @param arewardType 打赏类型
     * @param beUserId   被打赏人id
     * @param name 被打赏人名字
     */
    public void arewardUser(String arewardType,String beUserId ,String name){
        this.mArewardType = arewardType;
        this.mBeUserId = beUserId;
        this.mPostId = "";
        this.mName = name;
        show();
        mPresenter.getArewardScore(SpUtil.getString(Constant.CACHE_TAG_UID));
    }

    /**
     * 对帖子打赏
     * @param arewardType 打赏类型
     * @param beUserId 被打赏人id
     * @param postId   帖子id
     * @param name 被打赏人名字
     */
    public void arewardCircle(String arewardType,String beUserId ,String postId ,String name){
        this.mArewardType = arewardType;
        this.mBeUserId = beUserId;
        this.mPostId = postId;
        this.mName= name;
        show();
        mPresenter.getArewardScore(SpUtil.getString(Constant.CACHE_TAG_UID));
    }


    //获取积分
    @Override
    public void getArewardScore(Integer arewardScore) {
         this.mArewardScore = arewardScore;
         mNormalIntegral.setText(String.valueOf(arewardScore));
    }

    //打赏
    @Override
    public void getReward(JsonNull jsonNull) {
       ToastUtil.show("打赏成功......");
    }
}
