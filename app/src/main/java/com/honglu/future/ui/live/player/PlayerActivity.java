package com.honglu.future.ui.live.player;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gensee.common.ServiceType;
import com.gensee.db.PlayerChatDataBaseManager;
import com.gensee.entity.ChatMsg;
import com.gensee.entity.InitParam;
import com.gensee.entity.LiveInfo;
import com.gensee.entity.PayInfo;
import com.gensee.entity.PingEntity;
import com.gensee.entity.RewardResult;
import com.gensee.net.AbsRtAction;
import com.gensee.player.OnChatListener;
import com.gensee.player.OnPlayListener;
import com.gensee.player.Player;
import com.gensee.routine.UserInfo;
import com.gensee.utils.GenseeLog;
import com.honglu.future.R;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.config.Constant;
import com.honglu.future.dialog.AlertFragmentDialog;
import com.honglu.future.ui.live.bean.LiveListBean;
import com.honglu.future.util.NetUtil;
import com.honglu.future.util.ShareUtils;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;
import com.honglu.future.widget.tab.CommonTabLayout;
import com.honglu.future.widget.tab.CustomTabEntity;
import com.honglu.future.widget.tab.SimpleOnTabSelectListener;
import com.honglu.future.widget.tab.TabEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 直播页面
 * Created by zq on 2017/12/22.
 */
public class PlayerActivity extends BaseActivity implements OnPlayListener, View.OnClickListener, OnChatListener {

    private static final String TAG = "PlayerDemoActivity";
    private SharedPreferences preferences;
    private FrameLayout topFrameLayout;
    private LinearLayout midtabsLayout;
    private View rlBottom;
    private Player mPlayer;

    private RelativeLayout relTip;
    private TextView txtTip;
    private TextView mTvRoomName;

    private ProgressBar mProgressBar;
    private ChatFragment mChatFragment;
    private MainPointFragment mMainPointFragment;
    private ViedoFragment mViedoFragment;
    private LiveInfoFragment mLiveInfoFragment;
    private FragmentManager mFragmentManager;
    private CommonTabLayout mCommonTabLayout;
    private ImageView mIvBack, mIvShare;
    private ArrayList<CustomTabEntity> mTabList;
    private ArrayList<Fragment> mFragments;
    private InitParam initParam;
    private ServiceType serviceType = ServiceType.WEBCAST;
    private boolean mIsShowToast;
    private boolean mIsPlaying;

    private int videoWidth = 320, videoHeight = 180;

    private AudioManager manager;
    private boolean bJoinSuccess = false;
    private LiveListBean mLiveBean;

    private Intent serviceIntent;

    private int inviteMediaType;
    private String mRoomId;

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
                    mProgressBar.setVisibility(View.GONE);
                    bJoinSuccess = true;
                    if (mViedoFragment != null) {
                        mViedoFragment.onJoin(bJoinSuccess);
                    }
                    mChatFragment.setUserInfo(mPlayer.getSelfInfo());
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

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            int orientation = getRequestedOrientation();
            if (orientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                    || orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                videoViewNormalSize();
            }
        }
    };

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

    ;

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

    @Override
    public void loadData() {
        preferences = getPreferences(MODE_PRIVATE);
        if (!NetUtil.isConnected(this)) {
            new AlertFragmentDialog.Builder(this).setContent("网络异常,请检查网络连接...")
                    .setRightBtnText("知道了")
                    .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                        @Override
                        public void dialogRightBtnClick(String inputString) {
                            onBackPressed();
                        }
                    })
                    .setCancel(false).create(AlertFragmentDialog.Builder.TYPE_NORMAL);
        }
        initWidget();
    }

    public void initWidget() {
        Bundle bundle = getIntent().getExtras();
        mLiveBean = (LiveListBean) bundle.getSerializable("liveBean");
        topFrameLayout = (FrameLayout) findViewById(R.id.top_framelayout);
        midtabsLayout = (LinearLayout) findViewById(R.id.ly_midtabs);
        rlBottom = findViewById(R.id.top3_rl);

        relTip = (RelativeLayout) findViewById(R.id.rl_tip);
        txtTip = (TextView) findViewById(R.id.tv_tip);
        mCommonTabLayout = (CommonTabLayout) findViewById(R.id.trade_common_tab_layout);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(this);
        mIvShare = (ImageView) findViewById(R.id.iv_share);
        mIvShare.setOnClickListener(this);
        mTvRoomName = (TextView) findViewById(R.id.tv_name);

        videoViewNormalSize();

        mPlayer = new Player();
        mPlayer.setOnChatListener(this);

        mFragmentManager = getSupportFragmentManager();
        //添加tab实体
        addTabEntities();
        //添加fragment
        addFragments();

        initModule();
        setInitParam();
        GenseeLog.i(TAG, "initWidget end");

    }

    private void setInitParam() {
        mTvRoomName.setText(mLiveBean.liveTitle);
        mLiveInfoFragment.setLiveInfo(mLiveBean);
        String domain = mLiveBean.domainUrl;
        String number = mLiveBean.chatRoomId;
        String nickName = SpUtil.getString(Constant.CACHE_TAG_USERNAME);
        String joinPwd = mLiveBean.roomJoinPassword;
        mRoomId = mLiveBean.chatRoomId;
        if ("".equals(domain) || "".equals(number) || "".equals(nickName)) {
            ToastUtil.show("域名/编号/昵称 都不能为空");
            return;
        }

        initParam = new InitParam();
        // 设置域名
        initParam.setDomain(domain);
        //设置直播间编号
        //initParam.setNumber(number);
        initParam.setLiveId(number);
        // 设置显示昵称 不能为空,请传入正确的昵称，有显示和统计的作用
        // 设置显示昵称，如果设置为空，请确保
        initParam.setNickName(nickName + "|" + SpUtil.getString(Constant.CACHE_TAG_UID));
        // 设置加入口令（根据配置可选）
        initParam.setJoinPwd(joinPwd);
        // 设置服务类型，如果站点是webcast类型则设置为ServiceType.ST_CASTLINE，
        // training类型则设置为ServiceType.ST_TRAINING
        initParam.setServiceType(serviceType);
        //如果启用第三方认证，必填项，且要正确有效
        joinLive(initParam);
    }

    private void addTabEntities() {
        mTabList = new ArrayList<>();
        mTabList.add(new TabEntity("实时互动"));
        mTabList.add(new TabEntity("直播重点"));
        mTabList.add(new TabEntity("节目信息"));
    }

    private void addFragments() {
        if (mFragments == null) {
            mFragments = new ArrayList<>();
        }
        mChatFragment = new ChatFragment(this, mPlayer);
        mFragments.add(mChatFragment);
        mMainPointFragment = new MainPointFragment();
        mFragments.add(mMainPointFragment);
        mLiveInfoFragment = new LiveInfoFragment();
        mFragments.add(mLiveInfoFragment);

        mCommonTabLayout.setTabData(mTabList, this, R.id.trade_fragment_container, mFragments);
        mCommonTabLayout.setOnTabSelectListener(new SimpleOnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                super.onTabSelect(position);
//                if (position == 0) {
//                    mMyToFriendFragment.refresh();
//                } else if (position == 1) {
//                    mFriendToMyFragment.refresh();
//                }
                if(position == 1){
                    mMainPointFragment.setRoomId(mRoomId);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//		if (mPlayer != null && bJoinSuccess) {
//			mPlayer.audioSet(false);
//			mPlayer.videoSet(false);
//		}
    }

    @Override
    protected void onPause() {
        super.onPause();
//		if (mPlayer != null && bJoinSuccess) {
//			mPlayer.audioSet(true);
//			mPlayer.videoSet(true);
//		}
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

    private void initModule() {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        processVideoFragment(ft);
        ft.commit();
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

    /**************************************** OnPlayListener ********************************************/
    @Override
    public void onJoin(int result) {
        String msg = null;
        switch (result) {
            case JOIN_OK:
                msg = "加入成功";
                mHandler.sendEmptyMessage(HANDlER.SUCCESSJOIN);
                //toastMsg(msg);
                break;
            case JOIN_CONNECTING:
                msg = "正在加入";
                break;
            case JOIN_CONNECT_FAILED:
                msg = "连接失败";
                //toastMsg(msg);
                break;
            case JOIN_RTMP_FAILED:
                msg = "连接服务器失败";
                //toastMsg(msg);
                break;
            case JOIN_TOO_EARLY:
                msg = "直播还未开始";
                new AlertFragmentDialog.Builder(this).setContent(msg)
                        .setRightBtnText("知道了")
                        .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                            @Override
                            public void dialogRightBtnClick(String inputString) {
                                onBackPressed();
                            }
                        })
                        .setCancel(false).create(AlertFragmentDialog.Builder.TYPE_NORMAL);
                break;
            case JOIN_LICENSE:
                msg = "人数已满";
                break;
            default:
                msg = "加入返回错误" + result;
                break;
        }
        showTip(false, "");
        //toastMsg(msg);
    }

    @Override
    public void onUserJoin(UserInfo info) {
        Log.i("testUser", info.getName());
        // 用户加入
        mHandler.sendMessage(mHandler.obtainMessage(HANDlER.USERINCREASE, info));
    }

    @Override
    public void onUserLeave(UserInfo info) {
        Log.i("testUser", info.getName() + "leave");
        // 用户离开
        mHandler.sendMessage(mHandler.obtainMessage(HANDlER.USERDECREASE, info));
    }

    @Override
    public void onUserUpdate(UserInfo info) {
        Log.i("testUser", info.getName() + "update");
        // 用户更新
        mHandler.sendMessage(mHandler.obtainMessage(HANDlER.USERUPDATE, info));
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
            showErrorMsg(msg);
        }
    }

    /**
     * 缓冲变更
     *
     * @param isCaching true 缓冲/false 缓冲完成
     */
    @Override
    public void onCaching(boolean isCaching) {
        GenseeLog.d(TAG, "onCaching isCaching = " + isCaching);
//		mHandler.sendEmptyMessage(isCaching ? HANDlER.CACHING
//				: HANDlER.CACHING_END);
//        toastMsg(isCaching ? "正在缓冲" : "缓冲完成");
    }

    /**
     * 文档切换
     *
     * @param docType 文档类型（ppt、word、txt、png）
     * @param docName 文档名称
     */
    @Override
    public void onDocSwitch(int docType, String docName) {
    }

    /**
     * 视频开始
     */
    @Override
    public void onVideoBegin() {
        GenseeLog.d(TAG, "onVideoBegin");
//        toastMsg("视频开始");
    }

    /**
     * 视频结束
     */
    @Override
    public void onVideoEnd() {
        GenseeLog.d(TAG, "onVideoEnd");
//        toastMsg("视频已停止");
    }

    /**
     * 音频电频值
     */
    @Override
    public void onAudioLevel(int level) {

    }

    /**
     * 错误响应
     *
     * @param errCode 错误码 请参考文档
     */
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
            //toastMsg(msg);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_share:
                ShareUtils.getIntance().share(this, "", "http://www.baidu.com", "直播间", "直播间分享");
                break;
            default:
                break;
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

    //	private void processInfoFragment(FragmentTransaction ft){
//		if(mInfoFragment==null){
//			mInfoFragment = new JoinInfoFragment();
//			ft.add(R.id.fragement_update, mInfoFragment);
//		}else{
//			ft.show(mInfoFragment);
//		}
//	}

    public void exit() {
        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        startActivity(mHomeIntent);
    }

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
//        if (bJoinSuccess) {
//            dialogLeave();
//        } else {
        super.onBackPressed();
//        }
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(mRunnable);
        releasePlayer();
        // onFinshAll();
        super.onDestroy();
    }

    private void releasePlayer() {
        if (null != mPlayer && bJoinSuccess) {
            mPlayer.leave();
            mPlayer.release(getApplicationContext());
            bJoinSuccess = false;
        }

    }

    private void showErrorMsg(final String sMsg) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        PlayerActivity.this);
                builder.setTitle("提示");
                builder.setMessage(sMsg);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        finish();
                    }
                });
                if (!isFinishing()) {
                    AlertDialog alertDialog = builder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();
                }

            }
        });

    }

    public void toastMsg(final String msg) {
        if (msg != null) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), msg,
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            videoFullScreen();
            mViedoFragment.setFullImage(true);
        } else {
            videoNormalScreen();
            mViedoFragment.setFullImage(false);
        }
    }

    private void videoFullScreen() {
        ViewGroup.LayoutParams p = topFrameLayout.getLayoutParams();
        p.height = ViewGroup.LayoutParams.MATCH_PARENT;
        topFrameLayout.setLayoutParams(p);
        rlBottom.setVisibility(View.GONE);
        midtabsLayout.setVisibility(View.GONE);
    }

    private void videoViewNormalSize() {
        ViewGroup.LayoutParams p = topFrameLayout.getLayoutParams();
        p.height = getResources().getDisplayMetrics().widthPixels * videoHeight / videoWidth;
        topFrameLayout.setLayoutParams(p);
    }

    private void videoNormalScreen() {
        videoViewNormalSize();
        rlBottom.setVisibility(View.VISIBLE);
        midtabsLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onChatWithPerson(ChatMsg chatMsg) {
    }

    @Override
    public void onChatWithPublic(final ChatMsg chatMsg) {
        //Log.i("testUrl", chatMsg.getSender() + ":" + chatMsg.getContent() +",,,"+chatMsg.getRichText() +",,," + chatMsg.getTimeStamp());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chatMsg.setTimeStamp(System.currentTimeMillis());
                mChatFragment.appendMsg(chatMsg);
            }
        });
    }

    @Override
    public void onMute(boolean b) {

    }

    @Override
    public void onRoomMute(boolean b) {

    }

    @Override
    public void onReconnection() {

    }

    @Override
    public void onPublish(boolean isPlaying) {
        if (!mIsShowToast || mIsPlaying != isPlaying) {
//            toastMsg(isPlaying ? "直播中" : "直播暂停");
            mIsShowToast = true;
            mIsPlaying = isPlaying;
        }
    }

    @Override
    public void onChatcensor(String s, String s1) {
    }

    @Override
    public void onPageSize(int pos, int w, int h) {
        //文档开始显示
//        toastMsg("文档分辨率 w = " + w + " h = " + h);
    }

    /**
     * 直播主题
     */
    @Override
    public void onSubject(String subject) {
        GenseeLog.d(TAG, "onSubject subject = " + subject);

    }

    /**
     * 在线人数
     *
     * @param total
     */
    @Override
    public void onRosterTotal(final int total) {
        GenseeLog.d(TAG, "onRosterTotal total = " + total);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mViedoFragment.setUserNum(String.valueOf(total));
            }
        });
    }

    /**
     * 系统广播消息
     */
    @Override
    public void onPublicMsg(long userId, String msg) {
        GenseeLog.d(TAG, "广播消息：" + msg);
//        toastMsg("广播消息：" + msg);
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
                mPlayer.inviteAck(inviteMediaType, false, null);
                break;
            case MicNotify.MIC_OPENED:
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        mViedoFragment.onMicOpened(inviteMediaType);
                    }
                });
                mPlayer.inviteAck(inviteMediaType, true, null);

                break;
            case MicNotify.MIC_OPEN_FAILED:
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
//                        toastMsg("麦克风打开失败，请重试并允许程序打开麦克风");
                    }
                });
                mPlayer.openMic(this, false, null);
                mPlayer.inviteAck(inviteMediaType, false, null);
                break;

            default:
                break;
        }
    }

    @Override
    public void onLiveText(String language, String text) {
//        toastMsg("文字直播\n语言：" + language + "\n内容：" + text);
    }

    @Override
    public void onLottery(int cmd, String info) {
        //cmd 1:start, 2: stop, 3: abort
//        toastMsg("抽奖\n指令：" + (cmd == 1 ? "开始" : (cmd == 2 ? "结束" : "取消"))
//                + "\n结果：" + info);
    }

    @Override
    public void onRollcall(final int timeOut) {

    }

    @Override
    public void onFileShare(int cmd, String fileName, String fileUrl) {
        //cmd:1:add, 2: remove
        //TODO 应用层根据需要进行界面显示后可以调用  player的
    }

    @Override
    public void onFileShareDl(int ret, String fileUrl, String filePath) {

    }

    @Override
    public void onInvite(int i, boolean b) {

    }

    @Override
    public void onVideoSize(int width, int height, boolean iaAs) {
        GenseeLog.d(TAG, "onVideoSize");
//        toastMsg("onVideoSize width = " + width + " height = " + height + " isAs = " + iaAs);
        //如果明确视频尺寸比例，或中途不会变化，初始化确定好比例，可以不用这段代码
        if (videoHeight != height || videoWidth != width) {
            videoHeight = height;
            videoWidth = width;
            mHandler.post(mRunnable);
        }
    }

    @Override
    public void onModuleFocus(int arg0) {

    }

    @Override
    public void onScreenStatus(boolean isAs) {
//        toastMsg("onScreenStatus isAs = " + isAs);
    }

    @Override
    public void onIdcList(List<PingEntity> arg0) {
        mViedoFragment.onIdcList(arg0);
    }

    @Override
    public void onCameraNotify(int notify) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onGotoPay(PayInfo info) {
        //用info.getOrderInfo()作为支付参数调用支付宝进行支付流程;
    }

    @Override
    public void onRedBagTip(RewardResult arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRewordEnable(boolean arg0, boolean arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onThirdVote(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGetUserInfo(UserInfo[] arg0) {
        // TODO Auto-generated method stub

    }


    @SuppressLint("InlinedApi")
    private void rigister() {
        manager = (AudioManager) getSystemService(AUDIO_SERVICE);
        BroadcastReceiver receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if (AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED.equals(intent.getAction())) {
                    int state = intent.getIntExtra(AudioManager.EXTRA_SCO_AUDIO_STATE, 0);
                    int statePre = intent.getIntExtra(AudioManager.EXTRA_SCO_AUDIO_PREVIOUS_STATE, 0);

                    switch (state) {
                        case AudioManager.SCO_AUDIO_STATE_CONNECTED:
                            GenseeLog.d(TAG, "onReceive SCO_AUDIO_STATE_CONNECTED " + state + " preState = " + statePre);
//					   if(!manager.isBluetoothScoOn()){
//						}else{
//						}
                            manager.setBluetoothScoOn(true);
                            break;
                        case AudioManager.SCO_AUDIO_STATE_CONNECTING:
                            GenseeLog.d(TAG, "onReceive SCO_AUDIO_STATE_CONNECTING " + state + " preState = " + statePre);
                            break;
                        case AudioManager.SCO_AUDIO_STATE_DISCONNECTED:
                            GenseeLog.d(TAG, "onReceive SCO_AUDIO_STATE_DISCONNECTED " + state + " preState = " + statePre);
                            manager.setBluetoothScoOn(false);
                            break;
                        case AudioManager.SCO_AUDIO_STATE_ERROR:
                            GenseeLog.d(TAG, "onReceive SCO_AUDIO_STATE_ERROR " + state + " preState = " + statePre);
                            break;
                    }

                }
            }
        };
        IntentFilter f = new IntentFilter();
        f.addAction(AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED);
        registerReceiver(receiver, f);


        manager.startBluetoothSco();
    }

    private void switchBlutooth() {
        AudioManager manager = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (manager.isBluetoothScoOn()) {
            manager.setBluetoothScoOn(false);
        } else {
            manager.setBluetoothScoOn(true);
        }

//		manager.startBluetoothSco();

    }

    @Override
    public void onVideoDataNotify() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLiveInfo(final LiveInfo info) {
    }

}
