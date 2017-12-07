package com.honglu.future.ui.usercenter.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.base.PermissionsListener;
import com.honglu.future.config.ConfigUtil;
import com.honglu.future.config.Constant;
import com.honglu.future.dialog.AlertFragmentDialog;
import com.honglu.future.events.LogoutEvent;
import com.honglu.future.events.RefreshUIEvent;
import com.honglu.future.events.UIBaseEvent;
import com.honglu.future.ui.login.activity.ResetPwdActivity;
import com.honglu.future.ui.main.activity.MainActivity;
import com.honglu.future.ui.usercenter.contract.ModifyUserContract;
import com.honglu.future.ui.usercenter.presenter.ModifyUserPresenter;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.util.MediaStoreUtils;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ViewUtil;
import com.honglu.future.widget.CircleImageView;
import com.honglu.future.widget.popupwind.BottomPopupWindow;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.honglu.future.util.ToastUtil.showToast;

/**
 * Created by zq on 2017/10/25.
 */

public class ModifyUserActivity extends BaseActivity<ModifyUserPresenter> implements
        ModifyUserContract.View {
    @BindView(R.id.tv_back)
    ImageView mIvBack;
    @BindView(R.id.rl_modify_name)
    RelativeLayout mModifyName;
    @BindView(R.id.rl_reset_pwd)
    RelativeLayout mResetPwd;
    @BindView(R.id.tv_nickname)
    TextView mNickname;
    @BindView(R.id.iv_avatar)
    CircleImageView mIvAvatar;
    @BindView(R.id.tv_mobile)
    TextView mTvMobile;
    private int mRequestType = 0; //0相册存储权限 1 相机存储权限
    public static final int PHOTO_PICKED_WITH_ALBUM = 10000;
    public static final int PHOTO_PICKED_WITH_CAMERA = 10001;
    private BottomPopupWindow mPopupWindow;

    @Override
    public int getLayoutId() {
        return R.layout.activity_modify_user;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        EventBus.getDefault().register(this);
        initViews();
    }

    private void initViews() {
        mIvBack.setVisibility(View.VISIBLE);
        mTitle.setTitle(false, R.color.white, "账户管理");
        if (!TextUtils.isEmpty(SpUtil.getString(Constant.CACHE_TAG_USERNAME))) {
            mNickname.setText(SpUtil.getString(Constant.CACHE_TAG_USERNAME));
        }
        mTvMobile.setText(SpUtil.getString(Constant.CACHE_TAG_MOBILE));
        ImageUtil.display(ConfigUtil.baseImageUserUrl + SpUtil.getString(Constant.CACHE_USER_AVATAR), mIvAvatar, R.mipmap.img_head);
    }

    @OnClick({R.id.rl_modify_avatar, R.id.tv_back, R.id.rl_modify_name, R.id.rl_reset_pwd, R.id.btn_logout})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_modify_avatar:
                //takeCamera();
                clickTab("wode_account_touxiang","账户管理_头像");
                showTipWindow(v);
                break;
            case R.id.tv_back:
                finish();
                break;
            case R.id.rl_modify_name:
                startActivity(ModifyNicknameActivity.class);
                break;
            case R.id.rl_reset_pwd:
                clickTab("wode_account_chongzhimima","账户管理_重置密码");
                startActivity(ResetPwdActivity.class);
                break;
            case R.id.btn_logout:
                clickTab("wode_account_tuichu","账户管理_退出登录");
                if(DeviceUtils.isFastDoubleClick()){
                    return;
                }
                new AlertFragmentDialog.Builder(mActivity).setContent("确认退出登录吗？")
                        .setRightBtnText("确定")
                        .setLeftBtnText("取消")
                        .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                            @Override
                            public void dialogRightBtnClick(String string) {
                                EventBus.getDefault().post(new LogoutEvent(getApplicationContext(), 1));
                            }
                        }).build();
                break;
        }
    }

    private void showTipWindow(View view) {
        View layout = LayoutInflater.from(mActivity).inflate(R.layout.pic_selecter_popup_window, null);
        showTipBottomWindow(view, layout);
        ViewUtil.backgroundAlpha(mActivity, .5f);
    }

    private void showTipBottomWindow(View view, View layout) {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            return;
        }
        mPopupWindow = new BottomPopupWindow(mActivity,
                view, layout);
        //添加按键事件监听
        setButtonListeners(layout);
        //添加pop窗口关闭事件，主要是实现关闭时改变背景的透明度
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (mPopupWindow == null || !mPopupWindow.isShowing()) {
                    ViewUtil.backgroundAlpha(mActivity, 1f);
                }
            }
        });
    }

    private void setButtonListeners(View view) {
        TextView tvCamera = (TextView) view.findViewById(R.id.tv_camera);
        tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPopupWindow != null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                    takeCamera();
                }
            }
        });

        TextView tvSelect = (TextView) view.findViewById(R.id.tv_select);
        tvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPopupWindow != null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                    loadPicture();
                }
            }
        });

        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPopupWindow != null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
            }
        });
    }

    /**
     * 加载本地相册
     */
    private void loadPicture() {
        mRequestType = 0;
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestPermissions);
    }

    private void startLoadPicture() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_PICKED_WITH_ALBUM);
    }


    /**
     * 调用相机拍照
     */
    private void takeCamera() {
        mRequestType = 1;
        requestPermissions(new String[]{Manifest.permission.CAMERA}, requestPermissions);
    }

    private PermissionsListener requestPermissions = new PermissionsListener() {
        @Override
        public void onGranted() {
            if (mRequestType == 1) {
                startTakePhoto();
            } else if (mRequestType == 0) {
                startLoadPicture();
            }
        }

        @Override
        public void onDenied(List<String> deniedPermissions, boolean isNeverAsk) {
            for (String denied : deniedPermissions) {
                if (denied.equals(Manifest.permission.CAMERA)) {
                    showToast(getString(R.string.please_open_permission, getString(R.string.camera)));
                } else if (denied.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showToast(getString(R.string.please_open_permission, getString(R.string.storage)));
                }
            }
        }
    };

    private void startTakePhoto() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            try {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, PHOTO_PICKED_WITH_CAMERA);
            } catch (Throwable e) {
                showToast(getString(R.string.please_open_permission, getString(R.string.storage)));
            }
        } else {
            showToast(getString(R.string.sd_card_not_use));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String filePath;
        switch (requestCode) {
            case PHOTO_PICKED_WITH_ALBUM:
                if (data != null) {
                    Uri uri = data.getData();
                    filePath = MediaStoreUtils.getCapturePathFromPicture(this, uri);
                    if (!TextUtils.isEmpty(filePath)) {
                        cropImage(filePath);
                    }
                }
                break;
            case PHOTO_PICKED_WITH_CAMERA:
                if (data != null) {
                    filePath = MediaStoreUtils.getCapturePathFromCamera(this, data);
                    if (!TextUtils.isEmpty(filePath)) {
                        cropImage(filePath);
                    }
                }
                break;
        }
    }

    private void cropImage(final String filePath) {
        Luban.get(this).load(new File(filePath))                     //传人要压缩的图片
                .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                .setCompressListener(new OnCompressListener() {
                    //设置回调
                    @Override
                    public void onStart() {
                        App.loadingContent(ModifyUserActivity.this, "");
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        mPresenter.updateUserAvatar(file.getPath());
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过去出现问题时调用
                    }
                }).launch();    //启动压缩
    }

    @Override
    public void showLoading(String content) {
        if (!TextUtils.isEmpty(content)) {
            App.loadingContent(this, content);
        }
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        showToast(msg);
    }

    @Override
    public void updateUserAvatarSuccess(String imgUrl) {
        SpUtil.putString(Constant.CACHE_USER_AVATAR, imgUrl);
        ImageUtil.display(ConfigUtil.baseImageUserUrl + imgUrl, mIvAvatar, R.mipmap.img_head);
        EventBus.getDefault().post(new RefreshUIEvent(UIBaseEvent.EVENT_UPDATE_AVATAR));
        showToast("头像上传成功");
    }

    /***********
     * eventBus 监听
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UIBaseEvent event) {
        if (event instanceof RefreshUIEvent) {
            int code = ((RefreshUIEvent) event).getType();
            if (code == UIBaseEvent.EVENT_UPDATE_NICK_NAME) {//修改昵称
                mNickname.setText(SpUtil.getString(Constant.CACHE_TAG_USERNAME));
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 埋点
     * @param value1
     * @param value2
     */
    private void clickTab(String value1 , String value2){
        MobclickAgent.onEvent(mContext,value1, value2);
    }
}
