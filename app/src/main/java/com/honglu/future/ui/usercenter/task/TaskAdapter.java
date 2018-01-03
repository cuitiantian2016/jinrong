package com.honglu.future.ui.usercenter.task;

import android.app.Activity;
import android.net.Uri;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.ui.circle.circlemain.adapter.CommonAdapter;
import com.honglu.future.ui.main.CheckAccount;
import com.honglu.future.ui.usercenter.bean.TaskBean;
import com.honglu.future.util.AndroidUtil;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.ToastUtil;


public class TaskAdapter extends CommonAdapter<TaskBean> {
    private static String TRADE_FRAGMENT = "xiaoniuqihuo://future/trade/pay";
    private static String KAI_HU = "kaihu";

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == getCount() - 1) {
            holder.mLine.setVisibility(View.INVISIBLE);
        } else {
            holder.mLine.setVisibility(View.VISIBLE);
        }
        TaskBean item = getItem(position);
        holder.bindView(item, convertView, position);
        return convertView;
    }

    class ViewHolder {
        TextView tv_title, tv_content;
        ImageView iv_attention;
        View mLine;

        public ViewHolder(View convertView) {
            tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            iv_attention = (ImageView) convertView.findViewById(R.id.iv_complete);
            mLine = convertView.findViewById(R.id.v_line);
        }

        public void bindView(final TaskBean item, final View mContext, final int position) {
            tv_title.setText(item.title);
            tv_content.setText(item.content);
            int resId = item.isComplete() ? R.mipmap.already_recommend : R.mipmap.add_recommend;
            iv_attention.setImageResource(resId);
            iv_attention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!item.isComplete() && !TextUtils.isEmpty(item.url)) {
                        if (KAI_HU.equals(item.url)) {
                            CheckAccount mCheckAccount = new CheckAccount();
                            mCheckAccount.checkAccount();
                            return;
                        }
                        if (TRADE_FRAGMENT.equals(item.url)) {
                            if (!App.getConfig().getAccountLoginStatus()) {
                                item.url = "xiaoniuqihuo://future/future/main?select=2&redirect=xiaoniuqihuo://future/trade/pay";
                            }
                        }
                        ARouter.getInstance()
                                .build(Uri.parse(item.url))
                                .navigation(mContext.getContext(), new NavCallback() {
                                    @Override
                                    public void onArrival(Postcard postcard) {
                                        ((Activity) mContext.getContext()).finish();
                                    }
                                });
                    }
                }
            });
        }
    }

}
