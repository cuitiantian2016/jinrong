package com.honglu.future.ui.msg.mainmsg;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.base.BaseActivity;
import com.honglu.future.ui.msg.bean.HasUnreadMsgBean;
import com.honglu.future.ui.msg.bean.SystemMsgBean;
import com.honglu.future.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 系统消息详情
 * Created by hefei on 2018/1/2
 */
@Route(path = "/future/systemmsgdetail")
public class SystemMsgDetailActivity extends BaseActivity implements MainMsgContract.View{

    @BindView(R.id.tv_msg_title)
    TextView tv_title;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_content)
    TextView tv_content;
    @Autowired(name = "msgId")
    String msgId;
    public static final String KEY_SYSTEM ="KEY_SYSTEM";


    public static void startSystemMsgDetailActivity(Context context, SystemMsgBean systemMsgBean){
        Intent intent = new Intent(context, SystemMsgDetailActivity.class);
        intent.putExtra(KEY_SYSTEM,systemMsgBean);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main_msg_system_detail;
    }


    @Override
    public void showLoading(String content) {
        if (!TextUtils.isEmpty(content)) {
            App.loadingContent(SystemMsgDetailActivity.this, content);
        }
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        if (!TextUtils.isEmpty(msg)) {
            ToastUtil.show(msg);
        }
    }


    @Override
    public void initPresenter() {
    }

    @Override
    public void loadData() {
        Log.d("tga",msgId+"---msgId");
        mTitle.setTitle(false, R.color.color_white,"详情");
        Intent intent = getIntent();
        SystemMsgBean systemMsgBean = (SystemMsgBean) intent.getSerializableExtra(KEY_SYSTEM);
        if (systemMsgBean!=null){
            tv_title.setText(systemMsgBean.title);
            tv_time.setText(systemMsgBean.time);
            tv_content.setText(systemMsgBean.content);
        }
    }


    @Override
    public void hasUnreadMsg(HasUnreadMsgBean bean) {

    }
}
