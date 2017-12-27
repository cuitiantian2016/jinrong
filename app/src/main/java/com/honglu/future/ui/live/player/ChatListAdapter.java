package com.honglu.future.ui.live.player;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gensee.entity.ChatMsg;
import com.gensee.view.MyTextViewEx;
import com.honglu.future.R;
import com.honglu.future.util.TimeUtil;
import com.honglu.future.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zq on 2017/12/26.
 */

public class ChatListAdapter extends BaseRecyclerAdapter<ChatListAdapter.ViewHolder, ChatMsg> {
    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.chat_listitem_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, final int position) {
        holder.name.setText(item.getSender());
        holder.time.setText(TimeUtil.formatData(TimeUtil.dateFormatHM, item.getTimeStamp()/1000));
        holder.content.setChatContent(item.getContent(),item.getRichText());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chatnametext)
        TextView name;
        @BindView(R.id.chattimetext)
        TextView time;
        @BindView(R.id.chatcontexttextview)
        MyTextViewEx content;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void addMsg(ChatMsg msg) {
        data.add(msg);
        notifyDataSetChanged();
    }

}
