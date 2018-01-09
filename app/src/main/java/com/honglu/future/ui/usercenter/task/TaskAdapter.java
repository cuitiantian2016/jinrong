package com.honglu.future.ui.usercenter.task;

import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.honglu.future.R;
import com.honglu.future.app.App;
import com.honglu.future.dialog.AlertFragmentDialog;
import com.honglu.future.ui.circle.circlemain.adapter.CommonAdapter;
import com.honglu.future.ui.main.CheckAccount;
import com.honglu.future.ui.usercenter.bean.TaskBean;


public class TaskAdapter extends CommonAdapter<TaskBean> {
    private static String TRADE_FRAGMENT = "xiaoniuqihuo://future/trade/pay";
    private static String KAI_HU = "kaihu";
    FragmentActivity activity;

    public TaskAdapter(FragmentActivity activity) {
        this.activity = activity;
    }

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
        TextView iv_attention;
        View mLine;
        View image_dengbao;

        public ViewHolder(View convertView) {
            tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            iv_attention = (TextView) convertView.findViewById(R.id.iv_complete);
            image_dengbao = convertView.findViewById(R.id.image_dengbao);
            mLine = convertView.findViewById(R.id.v_line);
        }

        public void bindView(final TaskBean item, final View mContext, final int position) {
            tv_title.setText(item.title);
            tv_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   if (item.isShowDengBao()){
                       image_dengbao.performClick();
                   }
                }
            });
            tv_content.setText(item.content);
            iv_attention.setEnabled(!item.isComplete());
            iv_attention.setText(item.isComplete()?"已完成":"去完成");
            image_dengbao.setVisibility(item.isShowDengBao() ? View.VISIBLE : View.GONE);
            image_dengbao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertFragmentDialog.Builder(activity).setContent(item.warn_word)
                            .setRightBtnText("知道了")
                            .setTitle("提示")
                            .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                                @Override
                                public void dialogRightBtnClick(String inputString) {

                                }
                            })
                            .setCancel(false).create(AlertFragmentDialog.Builder.TYPE_NORMAL);
                }
            });
            iv_attention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!item.isComplete() && !TextUtils.isEmpty(item.url) && isScheam(item.url)) {
                        if (KAI_HU.equals(item.url)) {
                            CheckAccount mCheckAccount = new CheckAccount();
                            mCheckAccount.checkAccount();
                            return;
                        }
                        if (TRADE_FRAGMENT.equals(item.url)) {
                            if (!App.getConfig().getAccountLoginStatus()) {
                                item.url = "xiaoniuqihuo://future/future/main?select=1&redirect=xiaoniuqihuo://future/trade/pay";
                            }
                        }
                        ARouter.getInstance()
                                .build(Uri.parse(item.url))
                                .navigation(mContext.getContext(), new NavCallback() {
                                    @Override
                                    public void onArrival(Postcard postcard) {
                                        //((Activity) mContext.getContext()).finish();
                                    }
                                });
                    }
                }
            });
        }

        private boolean isScheam(String url) {
            return url.startsWith("xiaoniuqihuo");
        }
    }

}
