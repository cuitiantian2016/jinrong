package com.honglu.future.ui.live.player;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gensee.chat.gif.SpanResource;
import com.gensee.common.RoleType;
import com.gensee.entity.ChatMsg;
import com.gensee.holder.chat.ExpressionResource;
import com.gensee.player.Player;
import com.gensee.routine.UserInfo;
import com.gensee.taskret.OnTaskRet;
import com.gensee.view.ChatEditText;
import com.honglu.future.R;
import com.honglu.future.ui.live.bean.PictureBean;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.KeyBordUtil;
import com.honglu.future.util.ToastUtil;
import com.honglu.future.widget.recycler.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SuppressLint("ValidFragment")
public class ChatFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
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
    private GridView mFaceGrid;
    private ImageButton mFace;
    private LinearLayout mRlFace;
    private boolean mIsFaceShow;

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
        ExpressionResource.initExpressionResource(mContext);
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
        mChatEditText.setOnClickListener(this);
        mTvAll = (TextView) mView.findViewById(R.id.tv_all);
        mTvAll.setOnClickListener(this);
        mFaceGrid = (GridView) mView.findViewById(R.id.face_grid);
        mFace = (ImageButton) mView.findViewById(R.id.expressionbuttton);
        mFace.setOnClickListener(this);
        mRlFace = (LinearLayout) mView.findViewById(R.id.viewpageexpressionlinear);
        initFaceBar();
        return mView;
    }

    private void initFaceBar() {
        Map<String, Drawable> faceMap = SpanResource.getBrowMap(mContext);
        FaceImgAdapter adapter = new FaceImgAdapter(faceMap, mContext);
        mFaceGrid.setOnItemClickListener(this);
        mFaceGrid.setAdapter(adapter);
        mFaceGrid.setNumColumns(6);
        mFaceGrid.setBackgroundColor(Color.TRANSPARENT);
        mFaceGrid.setHorizontalSpacing(1);
        mFaceGrid.setVerticalSpacing(com.scwang.smartrefresh.layout.util.DeviceUtils.dip2px(mContext, 10));
        mFaceGrid.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        mFaceGrid.setCacheColorHint(0);
        mFaceGrid.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mFaceGrid.setGravity(Gravity.CENTER);
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
                final ChatMsg chatMsg = new ChatMsg(mChatEditText.getChatText(), mChatEditText.getRichText(), ChatMsg.CHAT_MSG_TYPE_PUBLIC, msgId);
                chatMsg.setSender(mSelfInfo.getName());
                chatMsg.setSenderId(mSelfInfo.getId());
                chatMsg.setSenderRole(mSelfInfo.getRole());
                mChatEditText.setText("");
                DeviceUtils.hintKeyboard(getActivity());
                mPlayer.chatToPublic(chatMsg, new OnTaskRet() {
                    @Override
                    public void onTaskRet(boolean b, int value, String s) {
                        if (b) {
                            chatMsg.setTimeStamp(System.currentTimeMillis());
                            mChatList.add(chatMsg);
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
            case R.id.expressionbuttton:
                if (!mIsFaceShow) {
                    mRlFace.setVisibility(View.VISIBLE);
                    mIsFaceShow = true;
                } else {
                    mRlFace.setVisibility(View.GONE);
                    mIsFaceShow = false;
                }
                KeyBordUtil.hideSoftKeyboard(v);

                break;
            case R.id.edittalking:
                if (mIsFaceShow) {
                    mRlFace.setVisibility(View.GONE);
                    mIsFaceShow = false;
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PictureBean face = (PictureBean) parent.getItemAtPosition(position);
        mChatEditText.insertAvatar(face.getKey(), 0);
    }
}
