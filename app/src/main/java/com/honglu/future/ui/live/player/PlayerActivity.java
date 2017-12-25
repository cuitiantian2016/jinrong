package com.honglu.future.ui.live.player;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gensee.common.ServiceType;
import com.gensee.entity.InitParam;
import com.gensee.entity.LiveInfo;
import com.gensee.entity.PayInfo;
import com.gensee.entity.PingEntity;
import com.gensee.entity.RewardResult;
import com.gensee.net.AbsRtAction;
import com.gensee.player.OnPlayListener;
import com.gensee.player.Player;
import com.gensee.routine.UserInfo;
import com.gensee.utils.GenseeLog;
import com.gensee.view.ILocalVideoView;
import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.Constant;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;
import com.honglu.future.util.Tool;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.honglu.future.util.ToastUtil.showToast;

/**
 * 直播页面
 * Created by zq on 2017/12/22.
 */

public class PlayerActivity extends BaseActivity implements OnPlayListener {
    private static final String TAG = "PlayerActivity";
    private ServiceType serviceType = ServiceType.WEBCAST;
    private InitParam initParam;
    private FragmentManager mFragmentManager;
    private boolean bJoinSuccess = false;
    private ViedoFragment mViedoFragment;
    private ChatFragment mChatFragment;
    private Player mPlayer;
    private int videoWidth = 320,videoHeight = 180;
    @BindView(R.id.bnt_public_chat)
    Button mBtnPublicChat;
    @BindView(R.id.progress)
    ProgressBar mProgressBar;
    @BindView(R.id.rl_tip)
    RelativeLayout relTip;
    @BindView(R.id.tv_tip)
    TextView txtTip;
    @BindView(R.id.top_framelayout)
    FrameLayout topFrameLayout;
    @BindView(R.id.top3_rl)
    View rlBottom;
    @BindView(R.id.ly_midtabs)
    LinearLayout midtabsLayout;

    interface HANDlER {
        int USERINCREASE = 1;
        int USERDECREASE = 2;
        int USERUPDATE = 3;
        int SUCCESSJOIN = 4;
        int SUCCESSLEAVE = 5;
        int CACHING = 6;
        int CACHING_END = 7;
        int RECONNECTING = 8;
    }

    @Override
    public int getLayoutId() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        return R.layout.activity_player_layout;
    }

    @Override
    public void initPresenter() {

    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case HANDlER.USERDECREASE:
                    break;
                case HANDlER.USERINCREASE:
                case HANDlER.USERUPDATE:
                    break;
                case HANDlER.SUCCESSJOIN:
                    mBtnPublicChat.setEnabled(true);
                    mBtnPublicChat.performClick();
                    mProgressBar.setVisibility(View.GONE);
                    bJoinSuccess = true;
                    if (mViedoFragment != null) {
                        mViedoFragment.onJoin(bJoinSuccess);
                    }
                    break;
                case HANDlER.SUCCESSLEAVE:
                    dialog();
                    break;
                case HANDlER.CACHING:
                    showTip(true, "正在缓冲...");
                    relTip.setVisibility(View.VISIBLE);
                    break;
                case HANDlER.CACHING_END:
                    showTip(false, "");
                    break;
                case HANDlER.RECONNECTING:
                    showTip(true, "正在重连...");
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };

    protected void dialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(PlayerActivity.this);
        builder.setMessage("你已经被踢出");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                finish();
                // onFinshAll();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void showTip(final boolean isShow, final String tip) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (isShow) {
                    if (relTip.getVisibility() != View.VISIBLE) {
                        relTip.setVisibility(View.VISIBLE);
                    }
                    txtTip.setText(tip);
                } else {
                    relTip.setVisibility(View.GONE);
                }

            }
        });
    }

    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(Constant.PARAMS_JOINSUCCESS, bJoinSuccess);
        outState.putBoolean(
                Constant.PARAMS_VIDEO_FULLSCREEN,
                getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
//		if (bJoinSuccess) {
//			if(mInfoFragment != null){
//				mInfoFragment.onSaveInstanceState(outState);
//			}
//		}
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        boolean bJoinSuccess = savedInstanceState
                .getBoolean(Constant.PARAMS_JOINSUCCESS);
        boolean bVideoFullScreen = savedInstanceState
                .getBoolean(Constant.PARAMS_VIDEO_FULLSCREEN);
        if (bVideoFullScreen) {
            videoFullScreen();
        }
        if (bJoinSuccess) {
            joinLive(initParam);
        }
    }

    private void videoFullScreen() {
        ViewGroup.LayoutParams p = topFrameLayout.getLayoutParams();
        p.height = ViewGroup.LayoutParams.MATCH_PARENT;
        topFrameLayout.setLayoutParams(p);
        rlBottom.setVisibility(View.GONE);
        midtabsLayout.setVisibility(View.GONE);
    }

    @Override
    public void loadData() {
        setInitParam();
        initViews();
        initModule();
        joinLive(initParam);
    }

    private void setInitParam() {
        Bundle bundle = getIntent().getExtras();
        String domain = bundle.getString("domain", "");
        String number = bundle.getString("number", "");
        String nickName = SpUtil.getString(Constant.CACHE_TAG_USERNAME);
        String joinPwd = bundle.getString("joinPwd", "");
        if ("".equals(domain) || "".equals(number) || "".equals(nickName)) {
            ToastUtil.show("域名/编号/昵称 都不能为空");
            return;
        }

        initParam = new InitParam();
        // 设置域名
        initParam.setDomain(domain);
        //设置直播间编号
        initParam.setNumber(number);
        // 设置显示昵称 不能为空,请传入正确的昵称，有显示和统计的作用
        // 设置显示昵称，如果设置为空，请确保
        initParam.setNickName(nickName);
        // 设置加入口令（根据配置可选）
        initParam.setJoinPwd(joinPwd);
        // 设置服务类型，如果站点是webcast类型则设置为ServiceType.ST_CASTLINE，
        // training类型则设置为ServiceType.ST_TRAINING
        initParam.setServiceType(serviceType);
        //如果启用第三方认证，必填项，且要正确有效

    }

    private void initViews() {
//        mTitle.setTitle(false, R.color.white, "直播间");
        videoViewNormalSize();
        mFragmentManager = getSupportFragmentManager();
        mPlayer = new Player();
    }

    private void initModule() {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        processChatFragment(ft);
        processVideoFragment(ft);
        hideFragment(ft);
        ft.commit();
    }

    public void joinLive(InitParam p) {
        if (p == null) {
            return;
        }
        if (!bJoinSuccess) {
//			mProgressBar.setVisibility(View.VISIBLE);
            showTip(true, "正在玩命加入...");
            mPlayer.join(getApplicationContext(), p, this);
        } else {
            dialogLeave();
        }
    }

    private void videoViewNormalSize(){
        ViewGroup.LayoutParams p = topFrameLayout.getLayoutParams();
        p.height = getResources().getDisplayMetrics().widthPixels * videoHeight/ videoWidth;
        topFrameLayout.setLayoutParams(p);
    }

    @Override
    public void onJoin(int result) {
        String msg = null;
        switch (result) {
            case JOIN_OK:
                msg = "加入成功";
                mHandler.sendEmptyMessage(HANDlER.SUCCESSJOIN);
                break;
            case JOIN_CONNECTING:
                msg = "正在加入";
                break;
            case JOIN_CONNECT_FAILED:
                msg = "连接失败";
                break;
            case JOIN_RTMP_FAILED:
                msg = "连接服务器失败";
                break;
            case JOIN_TOO_EARLY:
                msg = "直播还未开始";
                break;
            case JOIN_LICENSE:
                msg = "人数已满";
                break;
            default:
                msg = "加入返回错误" + result;
                break;
        }
        showTip(false, "");
        showToast(msg);
    }

    @Override
    public void onUserJoin(UserInfo info) {
        // 用户加入
        mHandler.sendMessage(mHandler.obtainMessage(HANDlER.USERINCREASE, info));
    }

    @Override
    public void onUserLeave(UserInfo info) {
        // 用户离开
        mHandler.sendMessage(mHandler.obtainMessage(HANDlER.USERDECREASE, info));
    }

    @Override
    public void onUserUpdate(UserInfo info) {
// 用户更新
        mHandler.sendMessage(mHandler.obtainMessage(HANDlER.USERUPDATE, info));
    }

    @Override
    public void onRosterTotal(int total) {
        GenseeLog.d(TAG, "onRosterTotal total = " + total);
    }

    @Override
    public void onReconnecting() {
        GenseeLog.d(TAG, "onReconnecting");
        //断线重连
        mHandler.sendEmptyMessage(HANDlER.RECONNECTING);
    }

    @Override
    public void onLeave(int reason) {
        // 当前用户退出
        // bJoinSuccess = false;
        String msg = null;
        switch (reason) {
            case LEAVE_NORMAL:
                msg = "您已经退出直播间";
                break;
            case LEAVE_KICKOUT:
                msg = "您已被踢出直播间";
                mHandler.sendEmptyMessage(HANDlER.SUCCESSLEAVE);
                break;
            case LEAVE_TIMEOUT:
                msg = "连接超时，您已经退出直播间";
                break;
            case LEAVE_CLOSE:
                msg = "直播已经停止";
                break;
            case LEAVE_UNKNOWN:
                msg = "您已退出直播间，请检查网络、直播间等状态";
                break;
            case LEAVE_RELOGIN:
                msg = "被踢出直播间（相同用户在其他设备上加入）";
                break;
            default:
                break;
        }
        if (null != msg) {
            showToast(msg);
        }
    }

    @Override
    public void onCaching(boolean isCaching) {
        GenseeLog.d(TAG, "onCaching isCaching = " + isCaching);
//		mHandler.sendEmptyMessage(isCaching ? HANDlER.CACHING
//				: HANDlER.CACHING_END);
        showToast(isCaching ? "正在缓冲" : "缓冲完成");
    }

    @Override
    public void onErr(int errCode) {
        GenseeLog.i(TAG, "onErr code = " + errCode);
        String msg = null;
        switch (errCode) {
            case AbsRtAction.ErrCode.ERR_DOMAIN:
                msg = "域名domain不正确";
                break;
            case AbsRtAction.ErrCode.ERR_TIME_OUT:
                msg = "请求超时，稍后重试";
                break;
            case AbsRtAction.ErrCode.ERR_SITE_UNUSED:
                msg = "站点不可用，请联系客服或相关人员";
                break;
            case AbsRtAction.ErrCode.ERR_UN_NET:
                msg = "网络不可用，请检查网络连接正常后再试";
                break;
            case AbsRtAction.ErrCode.ERR_SERVICE:
                msg = "service  错误，请确认是webcast还是training";
                break;
            case AbsRtAction.ErrCode.ERR_PARAM:
                msg = "initparam参数不全";
                break;
            case AbsRtAction.ErrCode.ERR_THIRD_CERTIFICATION_AUTHORITY:
                msg = "第三方认证失败";
                break;
            case AbsRtAction.ErrCode.ERR_NUMBER_UNEXIST:
                msg = "编号不存在";
                break;
            case AbsRtAction.ErrCode.ERR_TOKEN:
                msg = "口令错误";
                break;
            case AbsRtAction.ErrCode.ERR_LOGIN:
                msg = "站点登录帐号或登录密码错误";
                break;
            default:
                msg = "错误：errCode = " + errCode;
                break;
        }
        showTip(false, "");
        if (msg != null) {
            showToast(msg);
        }
    }

    @Override
    public void onDocSwitch(int i, String s) {

    }

    @Override
    public void onVideoBegin() {
        GenseeLog.d(TAG, "onVideoBegin");
        showToast("视频开始");
    }

    @Override
    public void onVideoEnd() {
        GenseeLog.d(TAG, "onVideoEnd");
        showToast("视频已停止");
    }

    @Override
    public void onVideoSize(int i, int i1, boolean b) {

    }

    @Override
    public void onAudioLevel(int i) {

    }

    @Override
    public void onPublish(boolean isPlaying) {
        showToast(isPlaying ? "直播（上课）中" : "直播暂停（下课）");
    }

    @Override
    public void onSubject(String subject) {
        GenseeLog.d(TAG, "onSubject subject = " + subject);
    }

    @Override
    public void onPageSize(int i, int i1, int i2) {

    }

    @Override
    public void onVideoDataNotify() {

    }

    @Override
    public void onPublicMsg(long l, String s) {

    }

    @Override
    public void onLiveText(String s, String s1) {

    }

    @Override
    public void onRollcall(int i) {

    }

    @Override
    public void onLottery(int i, String s) {

    }

    @Override
    public void onFileShare(int i, String s, String s1) {

    }

    @Override
    public void onFileShareDl(int i, String s, String s1) {

    }

    @Override
    public void onInvite(int i, boolean b) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            videoFullScreen();
        } else {
            videoNormalScreen();
        }
    }

    private void videoNormalScreen() {
        videoViewNormalSize();
        rlBottom.setVisibility(View.VISIBLE);
        midtabsLayout.setVisibility(View.VISIBLE);
    }

    public void dialogLeave() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayerActivity.this);
        builder.setMessage("确定离开");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                // release();
                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // mPlayer.leave();
                // mPlayer.release();
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (bJoinSuccess) {
            dialogLeave();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        releasePlayer();
        super.onDestroy();
    }

    private void releasePlayer() {
        if (null != mPlayer && bJoinSuccess) {
            mPlayer.leave();
            mPlayer.release(this);
            bJoinSuccess = false;
        }
    }

    @OnClick({R.id.bnt_public_chat})
    public void onClick(View view) {
        if (Tool.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.bnt_public_chat:
                FragmentTransaction ftChat = mFragmentManager.beginTransaction();
                hideFragment(ftChat);
                processChatFragment(ftChat);
                ftChat.commit();
                break;
        }
    }

    public void hideFragment(FragmentTransaction ft) {
        if (mChatFragment != null) {
            ft.hide(mChatFragment);
        }
    }

    private void processChatFragment(FragmentTransaction ft) {
        if (mChatFragment == null) {
            mChatFragment = new ChatFragment(mPlayer);
            ft.add(R.id.fragement_update, mChatFragment);
        } else {
            ft.show(mChatFragment);
        }
    }

    private void processVideoFragment(FragmentTransaction ft) {
        if (mViedoFragment == null) {
            mViedoFragment = new ViedoFragment(mPlayer);
            ft.add(R.id.top_framelayout, mViedoFragment);
        } else {
            ft.show(mViedoFragment);
        }

//		if (null != mViedoFragment) {
//			mViedoFragment.setVideoViewVisible(true);
//		}
    }

    @Override
    public void onMicNotify(int notify) {
        GenseeLog.d(TAG, "onMicNotify notify = " + notify);
        switch (notify) {
            case MicNotify.MIC_COLSED:
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        mViedoFragment.onMicColesed();
                    }
                });

                break;
            case MicNotify.MIC_OPENED:
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        mViedoFragment.onMicOpened(0);
                    }
                });

                break;
            case MicNotify.MIC_OPEN_FAILED:
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        showToast("麦克风打开失败，请重试并允许程序打开麦克风");
                    }
                });
                mPlayer.openMic(this, false, null);
                break;

            default:
                break;
        }
    }

    @Override
    public void onCameraNotify(int i) {

    }

    @Override
    public void onScreenStatus(boolean b) {

    }

    @Override
    public void onModuleFocus(int i) {

    }

    @Override
    public void onIdcList(List<PingEntity> arg0) {
        mViedoFragment.onIdcList(arg0);
    }

    @Override
    public void onThirdVote(String s) {

    }

    @Override
    public void onRewordEnable(boolean b, boolean b1) {

    }

    @Override
    public void onRedBagTip(RewardResult rewardResult) {

    }

    @Override
    public void onGotoPay(PayInfo payInfo) {

    }

    @Override
    public void onGetUserInfo(UserInfo[] userInfos) {

    }

    @Override
    public void onLiveInfo(LiveInfo liveInfo) {
        Log.i("testUrl","直播议程:"+liveInfo.getScheduleInfo());
        Log.i("testUrl","主讲信息/主讲信息:"+liveInfo.getSpeakerInfo());
        Log.i("testUrl","直播简介:"+liveInfo.getDescription());
    }

}
