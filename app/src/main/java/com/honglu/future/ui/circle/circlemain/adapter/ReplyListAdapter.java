package com.honglu.future.ui.circle.circlemain.adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.ui.circle.bean.BBS;
import com.honglu.future.ui.circle.bean.PostAndReplyBean;
import com.honglu.future.ui.circle.bean.Reply;
import com.honglu.future.ui.circle.circlemain.OnClickThrottleListener;
import com.honglu.future.util.ToastUtil;

import java.util.List;

public class ReplyListAdapter extends CommonAdapter<Reply> {

    private ClipboardManager cmb;
    private String topicType;
    private BBS mBBS;
    private PostAndReplyBean mPostAndReplyBean;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reply, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Reply item = getItem(position);
        holder.bindView(item);

        return convertView;
    }

    static class ViewHolder {

        TextView reply_name;

        public ViewHolder(View convertView) {
            reply_name = (TextView) convertView.findViewById(R.id.reply_name);
        }

        public void bindView(Reply item) {
            SpannableString spannable = new SpannableString(item.user_name + ": " + item.content);
            spannable.setSpan(new ForegroundColorSpan(reply_name.getContext()
                            .getResources()
                            .getColor(R.color.color_151515))
                    , String.valueOf(item.user_name + ": ").length(), spannable.length()
                    , Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
         reply_name.setText(spannable);
        }
    }

    public void addView(Context context, LinearLayout linearLayout, List<Reply> replyList, String topicType, BBS mbbs) {
        linearLayout.removeAllViews();
        if (replyList != null && replyList.size() > 0) {
            this.topicType = topicType;
            this.mBBS = mbbs;
            cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            for (Reply item : replyList) {
                View itemView = LayoutInflater.from(linearLayout.getContext()).inflate(R.layout.item_reply, null);
              TextView reply_name = (TextView) itemView.findViewById(R.id.reply_name);
                bindView(item, reply_name);
                linearLayout.addView(itemView);
            }
        }
    }

    public void addView(Context context, LinearLayout linearLayout, List<Reply> replyList, String topicType, PostAndReplyBean postAndReplyBean) {
        linearLayout.removeAllViews();
        if (replyList != null && replyList.size() > 0) {
            this.topicType = topicType;
            this.mPostAndReplyBean = postAndReplyBean;
            cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            for (Reply item : replyList) {
                View itemView = LayoutInflater.from(linearLayout.getContext()).inflate(R.layout.item_reply, null);
                TextView reply_name = (TextView) itemView.findViewById(R.id.reply_name);
                bindView(item, reply_name);
                linearLayout.addView(itemView);
            }
        }
    }

    public void bindView(final Reply item, TextView tv) {
        SpannableString spannable = new SpannableString(item.user_name + ": " + item.content);
        spannable.setSpan(new ForegroundColorSpan(tv.getContext()
                        .getResources()
                        .getColor(R.color.color_878787))
                , String.valueOf(item.user_name + ": ").length(), spannable.length()
                , Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv.setText(spannable);


        tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                cmb.setText(item.content);
                ToastUtil.show("内容复制成功！");
                return true;
            }
        });
        tv.setOnClickListener(new OnClickThrottleListener() {
            @Override
            protected void onThrottleClick(View v) {
                if (mBBS != null) {
//                    Intent intent = new Intent(v.getContext(), BBSDetailActivity.class);
//                    Bundle b = new Bundle();
//                    b.putSerializable("bbs_item", mBBS);
//                    intent.putExtras(b);
//                    intent.putExtra(BBSDetailActivity.EXTRA_FROM, "apptopic");
//                    intent.putExtra(BBSDetailActivity.EXTRA_TAB, topicType);
//                    v.getContext().startActivity(intent);
                }
            }
        });
    }

}
