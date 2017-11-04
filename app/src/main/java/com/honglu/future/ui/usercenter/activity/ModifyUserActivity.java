package com.honglu.future.ui.usercenter.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.Constant;
import com.honglu.future.ui.login.activity.ResetPwdActivity;
import com.honglu.future.ui.usercenter.contract.ModifyUserContract;
import com.honglu.future.ui.usercenter.presenter.ModifyUserPresenter;
import com.honglu.future.util.MediaStoreUtils;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;
import com.honglu.future.util.ViewUtil;
import com.honglu.future.widget.popupwind.BottomPopupWindow;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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
    private int mRequestType = 0; //0相册存储权限 1 相机存储权限
    public static final int PHOTO_PICKED_WITH_ALBUM = 10000;
    public static final int PHOTO_PICKED_WITH_CAMERA = 10001;
    private File mFile;
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
        initViews();
    }

    private void initViews() {
        mIvBack.setVisibility(View.VISIBLE);
        mTitle.setTitle(false, R.color.white, "账户管理");
        if (!TextUtils.isEmpty(SpUtil.getString(Constant.CACHE_TAG_USERNAME))) {
            mNickname.setText(SpUtil.getString(Constant.CACHE_TAG_USERNAME));
        }
    }

    @OnClick({R.id.iv_avatar, R.id.tv_back, R.id.rl_modify_name, R.id.rl_reset_pwd})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_avatar:
                //takeCamera();
                showTipWindow(v);
                break;
            case R.id.tv_back:
                finish();
                break;
            case R.id.rl_modify_name:
                startActivity(ModifyNicknameActivity.class);
                break;
            case R.id.rl_reset_pwd:
                startActivity(ResetPwdActivity.class);
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
                    takeCamera(mActivity);
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
        if (haveStoragePermissions()) {
            startLoadPicture();
        }
    }

    private void startLoadPicture() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_PICKED_WITH_ALBUM);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == 341) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                haveStoragePermissions();
            } else {
                showToast(getString(R.string.please_open_permission, getString(R.string.camera)));
            }
        } else if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mRequestType == 0) {
                    startLoadPicture();
                } else {
                    startTakePhoto();
                }
            } else {
                showToast(getString(R.string.please_open_permission, getString(R.string.storage)));
            }
        }
    }

    /**
     * 调用相机拍照
     *
     * @param activity
     */
    private void takeCamera(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //TODO ImagesSelectorActivity.MY_PERMISSIONS_REQUEST_CAMERA_CODE
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CAMERA},
                    341);
        } else {
            mRequestType = 1;
            if (haveStoragePermissions()) {
                startTakePhoto();
            }
        }
    }

    /**
     * 权限
     *
     * @return
     */
    public boolean haveStoragePermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }

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
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        mFile = file;
                        mPresenter.updateUserAvatar(filePath);
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
    public void updateUserAvatarSuccess() {
        showToast("头像上传成功");
    }
}
