package com.honglu.future.dialog;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ScrollingView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.honglu.future.R;
import com.honglu.future.util.ToastUtil;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 通用提示fragment
 */

public class AlertFragmentDialog extends DialogFragment implements View.OnClickListener {
    public static String TAG = "AlertFragmentDialog";
    private static final String KEY_BUILDER = "KEY_BUILDER";
    TextView mTvTitle;
    TextView mTvContent;
    TextView mTvCancel;
    TextView mTvAccomplish;
    EditText mEtInput;
    ImageView mImage;

    private LeftClickCallBack mLeftCallBack;
    private RightClickCallBack mRightCallBack;
    private Builder builder;

    public void setLeftCallBack(LeftClickCallBack mLeftCallBack) {
        this.mLeftCallBack = mLeftCallBack;
    }

    public void setRightCallBack(RightClickCallBack mRightCallBack) {
        this.mRightCallBack = mRightCallBack;
    }

    private static AlertFragmentDialog builder(Builder builder) {
        AlertFragmentDialog dialog = new AlertFragmentDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_BUILDER, builder);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 这个判断很重要
        if (getDialog() == null) {
            setShowsDialog(false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Bundle arguments = getArguments();
        if (arguments != null) {
            builder = (Builder) arguments.getSerializable(KEY_BUILDER);
        }
        View view = null;
        if (builder != null) {
            if (builder.type == Builder.TYPE_NORMAL) {
                view = inflater.inflate(R.layout.dialog_alert, container, false);
                mTvContent = (TextView) view.findViewById(R.id.tv_content);
            } else if (builder.type == Builder.TYPE_INPUT) {
                view = inflater.inflate(R.layout.dialog_alert_input, container, false);
                mEtInput = (EditText) view.findViewById(R.id.et_input);
                mTvContent = (TextView) view.findViewById(R.id.tv_content);
            } else if (builder.type == Builder.TYPE_IMAGE) {
                view = inflater.inflate(R.layout.dialog_alert_top_image, container, false);
                mImage = (ImageView) view.findViewById(R.id.ic_image);
            }
            mTvTitle = (TextView) view.findViewById(R.id.tv_title);
            mTvAccomplish = (TextView) view.findViewById(R.id.tv_accomplish);
            mTvCancel = (TextView) view.findViewById(R.id.tv_cancel);
            mTvCancel.setOnClickListener(this);
            mTvAccomplish.setOnClickListener(this);
            setData();
        }
        return view;
    }

    private void initDialog() {
        getDialog().setCancelable(builder.isCancel);
        getDialog().getWindow().setGravity(Gravity.CENTER);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout((int) (dm.widthPixels * 0.84), getDialog().getWindow().getAttributes().height);
    }

    private void setData() {
        if (builder.type == Builder.TYPE_NORMAL || builder.type == Builder.TYPE_INPUT) {
            mTvTitle.setText(builder.title);
            mTvContent.setText(builder.content);
            if (builder.type == Builder.TYPE_INPUT) {
                String hint = "请输入";
                if (!TextUtils.isEmpty(builder.etHintText)) {
                    hint = builder.etHintText;
                }
                mEtInput.setHint(hint);
            }
        }
        if (builder.type == Builder.TYPE_IMAGE) {
            mTvTitle.setText(builder.title);
            mImage.setImageResource(builder.imageRes);
        }
        if (TextUtils.isEmpty(builder.leftBtnText)) {
            mTvCancel.setVisibility(View.GONE);
        } else {
            mTvCancel.setText(builder.leftBtnText);
        }
        if (TextUtils.isEmpty(builder.rightBtnText)) {
            mTvAccomplish.setVisibility(View.GONE);
        } else {
            mTvAccomplish.setText(builder.rightBtnText);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initDialog();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                if (mLeftCallBack != null) {
                    mLeftCallBack.dialogLeftBtnClick();
                }
                dismiss();
                break;
            case R.id.tv_accomplish:
                String inputStr = null;
                if (mRightCallBack != null) {
                    if (builder.type == Builder.TYPE_INPUT) {
                        inputStr = mEtInput.getText().toString();
                        if (TextUtils.isEmpty(inputStr)) {
                            return;
                        }

                    }
                    mRightCallBack.dialogRightBtnClick(inputStr);
                }
                dismiss();
                break;
        }
    }

    /**
     * 左边按钮点击回调
     */
    public interface LeftClickCallBack {
        void dialogLeftBtnClick();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        //修改commit方法为commitAllowingStateLoss
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }


    /**
     * 右边按钮点击回调
     */
    public interface RightClickCallBack {
        void dialogRightBtnClick(String inputString);
    }

    public static class Builder implements Serializable {
        /**
         * 交易市场的类型
         */
        public static final int TYPE_INPUT = 1001;//有输入的样式
        public static final int TYPE_IMAGE = 1002;//弹出窗image 。title
        public static final int TYPE_NORMAL = 1003;//正常弹出窗title, content

        @Retention(RetentionPolicy.SOURCE)
        @IntDef({
                TYPE_INPUT,
                TYPE_IMAGE,
                TYPE_NORMAL
        })
        public @interface BuilderType {
        }

        private FragmentActivity activity;
        private String title;
        private String etHintText;
        private String content;
        private String leftBtnText;
        private String rightBtnText;
        private LeftClickCallBack leftCallBack;
        private RightClickCallBack rightCallBack;
        private boolean isCancel = true;
        private int imageRes;
        private int type = TYPE_NORMAL;

        public Builder(FragmentActivity activity) {
            this.activity = activity;
        }

        public Builder setEtHintText(String etHintText) {
            this.etHintText = etHintText;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setImageRes(@DrawableRes int imageRes) {
            this.imageRes = imageRes;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setLeftBtnText(String leftBtnText) {
            this.leftBtnText = leftBtnText;
            return this;
        }

        public Builder setRightBtnText(String rightBtnText) {
            this.rightBtnText = rightBtnText;
            return this;
        }

        public Builder setLeftCallBack(LeftClickCallBack leftCallBack) {
            this.leftCallBack = leftCallBack;
            return this;
        }

        public Builder setRightCallBack(RightClickCallBack rightCallBack) {
            this.rightCallBack = rightCallBack;
            return this;
        }

        /**
         * 是否可取消 （默认为不可取消）
         *
         * @param cancel true为可取消
         * @return
         */
        public Builder setCancel(boolean cancel) {
            isCancel = cancel;
            return this;
        }

        /**
         * 新样式提供的方法调用
         *
         * @return
         */
        public AlertFragmentDialog create(@BuilderType int type) {
            this.type = type;
            AlertFragmentDialog dialogFragment = AlertFragmentDialog.builder(this);
            dialogFragment.setLeftCallBack(leftCallBack);
            dialogFragment.setRightCallBack(rightCallBack);
            dialogFragment.show(activity.getSupportFragmentManager(), dialogFragment.TAG);
            return dialogFragment;
        }

        public AlertFragmentDialog build() {
            return create(TYPE_NORMAL);
        }
    }
}
