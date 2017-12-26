package com.honglu.future.ui.live.player;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gensee.common.RoleType;
import com.gensee.entity.ChatMsg;
import com.gensee.player.Player;
import com.gensee.routine.UserInfo;
import com.gensee.taskret.OnTaskRet;
import com.gensee.view.ChatEditText;
import com.honglu.future.R;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.ToastUtil;
import com.honglu.future.widget.recycler.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressLint("ValidFragment")
public class ChatFragment extends Fragment implements View.OnClickListener {
    private View mView;
    private UserInfo mUserInfo;
    private Context mContext;
    private RecyclerView mChatListView;
    private ChatListAdapter mChatListAdapter;
    private List<ChatMsg> mChatList;
    private List<ChatMsg> mChatSubList;
    private ImageButton mSendBtn;
    private UserInfo mSelfInfo;
    private ChatEditText mChatEditText;
    private Player mPlayer;
    private TextView mTvAll;
    private boolean mIsTeacher;

    public ChatFragment(Context context, Player player) {
        mContext = context;
        mPlayer = player;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.imchat, null);
        mChatListView = (RecyclerView) mView.findViewById(R.id.chat_list_view);
        mChatListView.setLayoutManager(new LinearLayoutManager(mContext));
        mChatListView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mChatListAdapter = new ChatListAdapter();
        mChatListView.setAdapter(mChatListAdapter);

        mChatList = new ArrayList<>();
        mChatSubList = new ArrayList<>();
        mSendBtn = (ImageButton) mView.findViewById(R.id.sendbutton);
        mSendBtn.setOnClickListener(this);
        mChatEditText = (ChatEditText) mView.findViewById(R.id.edittalking);
        mTvAll = (TextView) mView.findViewById(R.id.tv_all);
        mTvAll.setOnClickListener(this);
        return mView;
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
    }

    public void appendMsg(ChatMsg msg) {
        if (mIsTeacher) {
            mChatList.add(msg);
            if (mChatList.size() >= 30) {
                int size = mChatList.size();
                mChatSubList.clear();
                for (int i = size - 30; i <= mChatList.size() - 1; i++) {
                    if (RoleType.isHost(mChatList.get(i).getSenderRole())) {
                        mChatSubList.add(mChatList.get(i));
                    }
                }
            } else {
                if (RoleType.isHost(msg.getSenderRole())) {
                    mChatSubList.add(msg);
                }
            }
            mChatListAdapter.clearData();
            mChatListAdapter.addData(mChatSubList);
            if (mChatListAdapter.getItemCount() >= 1) {
                mChatListView.smoothScrollToPosition(mChatListAdapter.getItemCount() - 1);
            }
        } else {
            mChatList.add(msg);
            if (mChatList.size() >= 30) {
                int size = mChatList.size();
                mChatSubList.clear();
                for (int i = size - 30; i <= mChatList.size() - 1; i++) {
                    mChatSubList.add(mChatList.get(i));
                }
            } else {
                mChatSubList.add(msg);
            }
            mChatListAdapter.clearData();
            mChatListAdapter.addData(mChatSubList);
            if (mChatListAdapter.getItemCount() >= 1) {
                mChatListView.smoothScrollToPosition(mChatListAdapter.getItemCount() - 1);
            }
        }
    }

    public void setUserInfo(UserInfo userinfo) {
        mSelfInfo = userinfo;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendbutton:
                if (TextUtils.isEmpty(mChatEditText.getChatText().trim())) {
                    ToastUtil.show("不能发送空消息");
                    return;
                }
                String msgId = UUID.randomUUID().toString();
                final ChatMsg chatMsg = new ChatMsg(mChatEditText.getChatText(), mChatEditText.getChatText(), ChatMsg.CHAT_MSG_TYPE_PUBLIC, msgId);
                chatMsg.setSender(mSelfInfo.getName());
                chatMsg.setSenderId(mSelfInfo.getId());
                chatMsg.setSenderRole(mSelfInfo.getRole());
                mChatEditText.setText("");
                DeviceUtils.hintKeyboard(getActivity());
                mPlayer.chatToPublic(chatMsg, new OnTaskRet() {
                    @Override
                    public void onTaskRet(boolean b, int value, String s) {
                        if (b) {
                            mChatList.add(chatMsg);
                            if (mChatList.size() >= 30) {
                                int size = mChatList.size();
                                mChatSubList.clear();
                                for (int i = size - 30; i <= mChatList.size() - 1; i++) {
                                    mChatSubList.add(mChatList.get(i));
                                }
                            } else {
                                for (int i = 0; i < mChatList.size(); i++) {
                                    mChatSubList.add(mChatList.get(i));
                                }
                            }
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    if (mIsTeacher) {
                                        mTvAll.setText("只看老师");
                                        mIsTeacher = false;
                                    }
                                    mChatListAdapter.clearData();
                                    mChatListAdapter.addData(mChatSubList);
                                    if (mChatListAdapter.getItemCount() >= 1) {
                                        mChatListView.smoothScrollToPosition(mChatListAdapter.getItemCount() - 1);
                                    }

                                }
                            });

                        }
                    }
                });
                break;
            case R.id.tv_all:
                if (!mIsTeacher) {
                    mTvAll.setText("查看全部");
                    if (mChatList.size() >= 30) {
                        int size = mChatList.size();
                        mChatSubList.clear();
                        for (int i = size - 30; i <= mChatList.size() - 1; i++) {
                            if (RoleType.isHost(mChatList.get(i).getSenderRole())) {
                                mChatSubList.add(mChatList.get(i));
                            }
                        }
                    } else {
                        mChatSubList.clear();
                        for (int i = 0; i < mChatList.size(); i++) {
                            if (RoleType.isHost(mChatList.get(i).getSenderRole())) {
                                mChatSubList.add(mChatList.get(i));
                            }
                        }
                    }
                    mChatListAdapter.clearData();
                    mChatListAdapter.addData(mChatSubList);
                    if (mChatListAdapter.getItemCount() >= 1) {
                        mChatListView.smoothScrollToPosition(mChatListAdapter.getItemCount() - 1);
                    }
                    mIsTeacher = true;
                } else {
                    mTvAll.setText("只看老师");
                    if (mChatList.size() >= 30) {
                        int size = mChatList.size();
                        mChatSubList.clear();
                        for (int i = size - 30; i <= mChatList.size() - 1; i++) {
                            mChatSubList.add(mChatList.get(i));
                        }
                    } else {
                        mChatSubList.clear();
                        for (int i = 0; i < mChatList.size(); i++) {
                            mChatSubList.add(mChatList.get(i));
                        }
                    }
                    mChatListAdapter.clearData();
                    mChatListAdapter.addData(mChatSubList);
                    if (mChatListAdapter.getItemCount() >= 1) {
                        mChatListView.smoothScrollToPosition(mChatListAdapter.getItemCount() - 1);
                    }
                    mIsTeacher = false;
                }
                break;
        }
    }
}
