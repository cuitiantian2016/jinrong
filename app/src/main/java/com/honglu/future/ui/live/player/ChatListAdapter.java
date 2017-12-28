package com.honglu.future.ui.live.player;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gensee.common.RoleType;
import com.gensee.entity.ChatMsg;
import com.gensee.view.MyTextViewEx;
import com.honglu.future.R;
import com.honglu.future.util.TimeUtil;
import com.honglu.future.widget.recycler.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zq on 2017/12/26.
 */

public class ChatListAdapter extends BaseRecyclerAdapter<ChatListAdapter.ViewHolder, ChatMsg> {

    private Map<String, String> faceMap;
    private List<String> mFaceList;

    public ChatListAdapter() {
        faceMap = getFaceMap();
    }

    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.chat_listitem_layout, parent, false);
        //mFaceList = new ArrayList<>();
        return new ViewHolder(view);
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, final int position) {
        holder.name.setText(item.getSender());
        holder.time.setText(TimeUtil.formatData(TimeUtil.dateFormatHM, item.getTimeStamp() / 1000));
        if (RoleType.isHost(item.getSenderRole())) {
            holder.content.setTextColor(mContext.getResources().getColor(R.color.chat_select_self));
        } else {
            holder.content.setTextColor(mContext.getResources().getColor(R.color.color_333333));
        }

        String richText = "";
        mFaceList = getFaceKey(item.getContent());
        if (mFaceList != null && mFaceList.size() != 0) {
            richText = item.getContent();
            for (int i = 0; i < mFaceList.size(); i++) {
                if (!TextUtils.isEmpty(faceMap.get(mFaceList.get(i)))) {
                    richText = richText.replace("【" + mFaceList.get(i) + "】", "<IMG src=\"" + faceMap.get(mFaceList.get(i)) + "\" custom=\"false\">");
                }
            }
        }
        if (TextUtils.isEmpty(richText)) {
            richText = item.getContent();
        } else {
            richText = "<SPAN>" + richText + "</SPAN>";
        }

        holder.content.setChatContent(item.getContent(), richText);
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

    private Map<String, String> getFaceMap() {
        Map<String, String> faceMap = new HashMap<>();
        faceMap.put("你好", "emotion\\emotion.smile.gif");
        faceMap.put("再见", "emotion\\emotion.goodbye.gif");
        faceMap.put("高兴", "emotion\\emotion.laugh.gif");
        faceMap.put("伤心", "emotion\\emotion.cry.gif");
        faceMap.put("太快了", "emotion\\feedback.quickly.png");
        faceMap.put("太慢了", "emotion\\feedback.slowly.png");
        faceMap.put("赞同", "emotion\\feedback.agreed.png");
        faceMap.put("反对", "emotion\\feedback.against.gif");
        faceMap.put("鼓掌", "emotion\\feedback.applaud.png");
        faceMap.put("愤怒", "emotion\\emotion.angerly.gif");
        faceMap.put("值得思考", "emotion\\feedback.think.png");
        faceMap.put("无聊", "emotion\\emotion.nod.gif");
        faceMap.put("流汗", "emotion\\emotion.lh.gif");
        faceMap.put("鄙视", "emotion\\emotion.bs.gif");
        faceMap.put("疑问", "emotion\\emotion.question.gif");
        faceMap.put("凋谢", "emotion\\rose.down.png");

        faceMap.put("鲜花", "emotion\\rose.up.png");
        faceMap.put("礼物", "emotion\\chat.gift.png");
        faceMap.put("闭嘴", "emotion\\emotion.bz.gif");
        faceMap.put("奋斗", "emotion\\emotion.fd.gif");
        faceMap.put("尴尬", "emotion\\emotion.gg.gif");
        faceMap.put("鼓掌", "emotion\\emotion.gz.gif");
        faceMap.put("害羞", "emotion\\emotion.hx.gif");
        faceMap.put("惊恐", "emotion\\emotion.jk.gif");
        faceMap.put("惊讶", "emotion\\emotion.jy.gif");
        faceMap.put("抠鼻", "emotion\\emotion.kb.gif");
        faceMap.put("可怜", "emotion\\emotion.kl.gif");
        faceMap.put("流泪", "emotion\\emotion.ll.gif");
        faceMap.put("敲打", "emotion\\emotion.qd.gif");
        faceMap.put("强悍", "emotion\\emotion.qh.gif");
        faceMap.put("亲亲", "emotion\\emotion.qq.gif");
        faceMap.put("弱爆", "emotion\\emotion.rb.gif");
        faceMap.put("色", "emotion\\emotion.se.gif");

        faceMap.put("偷笑", "emotion\\emotion.tx.gif");
        faceMap.put("嘘", "emotion\\emotion.xu.gif");
        faceMap.put("晕", "emotion\\emotion.yun.gif");
        return faceMap;
    }

    private List<String> getFaceKey(String key) {
        List<String> mList = new ArrayList<>();
        String content = key;
        String reg = "\\【(.*?)\\】";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String str = matcher.group(1);
            mList.add(str);
        }
        return mList;
    }

}
