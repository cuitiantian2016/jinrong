package com.honglu.future.ui.circle.circlemine.adapter;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonNull;
import com.honglu.future.R;
import com.honglu.future.config.Constant;
import com.honglu.future.dialog.areward.ArewardDialog;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.http.RxHelper;
import com.honglu.future.ui.circle.bean.PostAndReplyBean;
import com.honglu.future.ui.circle.bean.Reply;
import com.honglu.future.ui.circle.circledetail.CircleDetailActivity;
import com.honglu.future.ui.circle.circlemain.OnClickThrottleListener;
import com.honglu.future.ui.circle.circlemain.adapter.ReplyListAdapter;
import com.honglu.future.ui.circle.circlemain.adapter.VerticalImageSpan;
import com.honglu.future.ui.register.activity.RegisterActivity;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.StringUtil;
import com.honglu.future.util.ToastUtil;
import com.honglu.future.util.ViewHelper;
import com.honglu.future.widget.AllListView;
import com.honglu.future.widget.CircleImageView;
import com.honglu.future.widget.gridimage.NineGridImageView;
import com.honglu.future.widget.gridimage.NineGridImageViewAdapter;
import com.honglu.future.widget.photo.FullScreenDisplayActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zq on 2017/12/9.
 */

public class BBSMineAdapter extends BaseAdapter {

    private Activity mContext;
    private ListView mListView;
    private ToRefreshListViewListener mToRefefreshListViewListener;
    private List<PostAndReplyBean> mList = new ArrayList<>();
    private String topicType;
    private AttentionCallBack mAttentionCallBack;
    private PraiseCallBack mPraiseCallBack;
    private String imgHead, nickName, userId;
    private boolean isFocued;
    private String userRole;
    private ArewardDialog mArewardDialog;


    public List<PostAndReplyBean> getList() {
        return mList;
    }

    private ClipboardManager cmb;

    public BBSMineAdapter(ListView listview, Activity context, String imgHead, String nickName, String userId ,ArewardDialog arewardDialog) {
        mListView = listview;
        mContext = context;
        this.imgHead = imgHead;
        this.nickName = nickName;
        this.userId = userId;

        cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        this.mArewardDialog = arewardDialog;
    }

    public void setToRefreshListViewListener(ToRefreshListViewListener toRefefreshListViewListener) {
        mToRefefreshListViewListener = toRefefreshListViewListener;
    }

    public void setTopicType(String types) {
        this.topicType = types;
    }

    public interface ScrollToLastCallBack {
        void onScrollToLast(Integer pos);
    }

    public void setDatas(List<PostAndReplyBean> list, boolean isFocued,String userRole) {
        this.isFocued = isFocued;
        this.userRole = userRole;
        for (int i = 0; i < list.size(); i++) {
            mList.add(list.get(i));
        }
        notifyDataSetChanged();
    }

    public void clearDatas() {
        mList.clear();
        notifyDataSetChanged();
    }

    /**
     * 关注刷新
     *
     * @param follow
     * @param uid
     */
    public void follow(String follow, String uid) {
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getPostUserId().equals(uid)) {
                mList.get(i).setFollow(follow);
            }
        }
        if (mAttentionCallBack != null) {
            mAttentionCallBack.attention(uid, follow);
        }
        notifyDataSetChanged();
    }

    /**
     * 点赞刷新
     *
     * @param
     */
    public void praise(String topic_id, String num) {
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getCircleId().equals(topic_id)) {
                mList.get(i).setThumbUpSum(num);
            }
        }
        if (mPraiseCallBack != null) {
            mPraiseCallBack.praise(topic_id, num);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public PostAndReplyBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bbs, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PostAndReplyBean item = getItem(position);
        holder.bindView(item, position);
        return convertView;
    }

    class ViewHolder {
        View itemLayout;
        CircleImageView header_img;
        ImageView best, iv_heart, follow;
        TextView announce_time, support, reply, hot_title;

        TextView content;
        LinearLayout support_iv;
        AllListView replay_ll;
        LinearLayout replay_ll_BB;
        TextView user_name, status;
        //LinearLayout supportLinear;
        NineGridImageView mMultiImgLy;
        View mSeparatorCommLy;
        TextView btn_lookDetail;
        ImageView ic_reward_state;
        LinearLayout ll_reward;
        TextView mRewardNum;

        public ViewHolder(View convertView) {
            itemLayout = convertView;
            hot_title = (TextView) convertView.findViewById(R.id.hot_title);
            announce_time = (TextView) convertView.findViewById(R.id.announce_time);
            user_name = (TextView) convertView.findViewById(R.id.user_name);
            iv_heart = (ImageView) convertView.findViewById(R.id.iv_heart);
            best = (ImageView) convertView.findViewById(R.id.iv_essence);
            header_img = (CircleImageView) convertView.findViewById(R.id.header_img);
            status = (TextView) convertView.findViewById(R.id.status);
            support_iv = (LinearLayout) convertView.findViewById(R.id.support_iv);
            follow = (ImageView) convertView.findViewById(R.id.iv_follow);
            content = (TextView) convertView.findViewById(R.id.content);
            replay_ll = (AllListView) convertView.findViewById(R.id.replay_ll);
            replay_ll_BB = (LinearLayout) convertView.findViewById(R.id.replay_ll_BB);
            support = (TextView) convertView.findViewById(R.id.support);
            reply = (TextView) convertView.findViewById(R.id.reply);
            mMultiImgLy = (NineGridImageView) convertView.findViewById(R.id.ly_multi_img);
            mSeparatorCommLy = convertView.findViewById(R.id.ly_separator_comment);
            btn_lookDetail = (TextView) convertView.findViewById(R.id.btn_lookDetail);
            ic_reward_state = (ImageView) convertView.findViewById(R.id.ic_reward_state);
            ll_reward = (LinearLayout) convertView.findViewById(R.id.ll_reward);
            mRewardNum = (TextView) convertView.findViewById(R.id.txt_reward_number);
        }

        public void bindView(final PostAndReplyBean item, int position) {

            mRewardNum.setText(String.valueOf(item.getExceptionalCount()));
            ic_reward_state.setImageResource(item.isExceptional() ? R.mipmap.ic_bbs_reward : R.mipmap.ic_bbs_reward_undone);
            ll_reward.setOnClickListener(new OnClickThrottleListener() {
                @Override
                protected void onThrottleClick(View v) {
                    if (TextUtils.equals(SpUtil.getString(Constant.CACHE_TAG_UID),item.getPostUserId())){
                        ToastUtil.show("自己不能打赏自己");
                    }else {
                        mArewardDialog.arewardCircle(ArewardDialog.AREWARD_CIRCLE_TYPE,item.getPostUserId(),item.getCircleId(),nickName,imgHead);
                    }
                }
            });
            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (DeviceUtils.isFastDoubleClick())
                        return;
                    if (item != null) {
                        Intent intent = new Intent(v.getContext(), CircleDetailActivity.class);
                        intent.putExtra(CircleDetailActivity.POST_USER_KEY, item.getPostUserId());
                        intent.putExtra(CircleDetailActivity.CIRCLEID_KEY, item.getCircleId());
                        v.getContext().startActivity(intent);
                    }
                }
            });
            itemLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
//                    if (AndroidUtil.isAdmin()) {
//                        if (item != null) {
//                            showAdminDialog(v.getContext(), item);
//                        }
//                    }
                    return false;
                }
            });
            //内容复制
            content.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    cmb.setText(item.getContent());
                    ToastUtil.show("内容复制成功！");
                    return true;
                }
            });
            //由于内容复制长按事件将点击焦点吸收故做此操作
            content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (DeviceUtils.isFastDoubleClick())
                        return;
                    if (item != null) {
                        Intent intent = new Intent(v.getContext(), CircleDetailActivity.class);
                        intent.putExtra(CircleDetailActivity.POST_USER_KEY, item.getPostUserId());
                        intent.putExtra(CircleDetailActivity.CIRCLEID_KEY, item.getCircleId());
                        v.getContext().startActivity(intent);
                    }
                }
            });
//            if (TextUtils.equals(SpUtil.getString(Constant.CACHE_TAG_UID), item.getPostUserId())) {
            follow.setVisibility(View.GONE);
//            } else {
//                follow.setVisibility(View.VISIBLE);
//            }
            ImageUtil.display(imgHead, header_img, R.mipmap.img_head);
            ViewHelper.safelySetText(user_name, nickName);

            status.setVisibility(TextUtils.isEmpty(userRole) ? View.GONE : View.VISIBLE);
            status.setText(userRole);
            if (SpUtil.getString(Constant.CACHE_TAG_UID).equals(item.getPostUserId())) {
                follow.setVisibility(View.GONE);
            } else {
                follow.setVisibility(View.GONE);
                follow.setImageResource(isFocued ? R.mipmap.already_recommend : R.mipmap.add_recommend);
                follow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (DeviceUtils.isFastDoubleClick()) {
                            return;
                        }
                        if (TextUtils.isEmpty(SpUtil.getString(Constant.CACHE_TAG_UID))) {
                            mContext.startActivity(new Intent(mContext, RegisterActivity.class));
                            return;
                        }
                        String user_id = SpUtil.getString(Constant.CACHE_TAG_UID);
                        if (user_id.equals(item.getPostUserId())) {
                            ToastUtil.show("自己不能关注自己");
                            return;
                        }
                        final String foll = isFocued ? "0" : "1";
                        HttpManager.getApi().focus(item.getPostUserId(), SpUtil.getString(Constant.CACHE_TAG_UID), foll).compose(RxHelper.<JsonNull>handleSimplyResult()).subscribe(new HttpSubscriber<JsonNull>() {
                            @Override
                            protected void _onNext(JsonNull jsonNull) {
                                super._onNext(jsonNull);
                                follow.setImageResource(R.mipmap.already_recommend);
                                item.setFollow(foll);
                                isFocued = !isFocued;
                                follow(item.getFollow(), item.getPostUserId());
                            }

                            @Override
                            protected void _onError(String message) {
                                super._onError(message);
                                ToastUtil.show(message);
                            }
                        });
                    }
                });
            }
            header_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (DeviceUtils.isFastDoubleClick())
                        return;
                    if (!SpUtil.getString(Constant.CACHE_TAG_UID).equals(item.getPostUserId())) {
                    } else {
                    }
                }
            });
            //是否精华
            boolean isHot = item.getIsHot() != 0;
            ViewHelper.setVisibility(best, isHot);
            ViewHelper.safelySetText(hot_title, item.getTitle());
            ViewHelper.safelySetText(announce_time, item.getCreateTime());// 热门不展示
            // 评论内容
            if (!StringUtil.isEmpty(item.getReplyContent())) {
                List<Reply> replyList = new ArrayList<>();
                Reply reply = new Reply();
                reply.user_name = item.getReplyNickName();
                reply.content = item.getReplyContent();
                replyList.add(reply);
                ReplyListAdapter replyListAdapter = new ReplyListAdapter();
                replyListAdapter.addView(mContext, replay_ll_BB, replyList, topicType, item);
                ViewHelper.setVisibility(replay_ll_BB, true);
                ViewHelper.setVisibility(mSeparatorCommLy, true);
            } else {
                ViewHelper.setVisibility(replay_ll_BB, false);
                ViewHelper.setVisibility(mSeparatorCommLy, false);
            }

            content.setLineSpacing(0, 1.2f);
            content.setText(getNewsContentByType(item.getCircleTypeId(), item.getContent() + ""));

            final List<String> imgList = new ArrayList<>();
            if (!StringUtil.isEmpty(item.getPicOne())) {
                imgList.add(item.getPicOne());
            }
            if (!StringUtil.isEmpty(item.getPicTwo())) {
                imgList.add(item.getPicTwo());
            }
            if (!StringUtil.isEmpty(item.getPicThree())) {
                imgList.add(item.getPicThree());
            }
            if (!StringUtil.isEmpty(item.getPicFour())) {
                imgList.add(item.getPicFour());
            }
            if (!StringUtil.isEmpty(item.getPicFive())) {
                imgList.add(item.getPicFive());
            }
            if (!StringUtil.isEmpty(item.getPicSix())) {
                imgList.add(item.getPicSix());
            }

            if (imgList.size() > 0) {

                mMultiImgLy.setAdapter(new NineGridImageViewAdapter<String>() {
                    @Override
                    protected void onDisplayImage(Context context, ImageView imageView, String url) {
                        if (TextUtils.isEmpty(url)) {
                            Glide.with(context).load(R.mipmap.other_empty).centerCrop().
                                    placeholder(R.mipmap.other_empty).error(R.mipmap.other_empty).
                                    diskCacheStrategy(DiskCacheStrategy.ALL).
                                    dontAnimate().into(imageView);
                        } else {
                            Glide.with(context).load(url).centerCrop().
                                    placeholder(R.mipmap.other_empty).error(R.mipmap.other_empty).
                                    diskCacheStrategy(DiskCacheStrategy.ALL).
                                    dontAnimate().into(imageView);
                        }
                    }

                    @Override
                    protected void onItemImageClick(View view, int index, List<String> list) {
                        super.onItemImageClick(view, index, list);
                        Intent intent = new Intent(view.getContext(), FullScreenDisplayActivity.class);
                        Bundle b = new Bundle();
                        ArrayList<String> images = new ArrayList<>();
                        if (imgList.size() > 0) {
                            images.addAll(imgList);
                            b.putStringArrayList("image_urls", images);
                            b.putInt("position", index);
                            intent.putExtras(b);

                            int[] location = new int[2];
                            view.getLocationOnScreen(location);
                            intent.putExtra("locationX", location[0]);
                            intent.putExtra("locationY", location[1]);
                            intent.putExtra("width", view.getWidth());
                            intent.putExtra("height", view.getHeight());
                            view.getContext().startActivity(intent);
                        }
                    }
                });
                mMultiImgLy.setImagesData(imgList);
            } else {
                mMultiImgLy.setImagesData(null);// 控件复用, 无数据时传空处理
            }


            if (item.getIsPraised() == 0) {
                iv_heart.setImageResource(R.mipmap.ic_support_done);
            } else if (item.getIsPraised() == 1) {
                iv_heart.setImageResource(R.mipmap.ic_support);
            } else {
                iv_heart.setImageResource(R.mipmap.ic_support_done);
            }

            support.setText(item.getThumbUpSum());
            reply.setText(item.getCommentSum());

            support_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 2017/12/12 需要增加自己是否点赞字段
                    if (DeviceUtils.isFastDoubleClick()) {
                        return;
                    }
                    if (TextUtils.isEmpty(SpUtil.getString(Constant.CACHE_TAG_UID))) {
                        mContext.startActivity(new Intent(mContext, RegisterActivity.class));
                        return;
                    }
                    if (item.getThumbUpSum().equals("1") || item.getThumbUpSum().equals("2"))
                        return;
                    declareForTopicThread(support_iv, "1", item, support, iv_heart);
                }
            });

            // TODO: 2017/12/9 查看专题链接
//            if (!TextUtils.isEmpty(item.topic_link)) {
//                btn_lookDetail.setVisibility(View.VISIBLE);
//                btn_lookDetail.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (!TextUtils.isEmpty(item.topic_link)) {
//                            // WmbbUtil.openWmbbScheme(mContext, item.topic_link);
//                        }
//                    }
//                });
//            } else {
//                btn_lookDetail.setVisibility(View.GONE);
//            }
        }

        private CharSequence getNewsContentByType(String type, String message) {
            CharSequence result;
            SpannableStringBuilder spannableString;

            if (!TextUtils.isEmpty(type)) {
                content.setGravity(Gravity.START);
                spannableString = new SpannableStringBuilder();
                switch (type) {
                    case "1":// 专家策略
                        spannableString.append("  ");// 空格占位进行图片替换
                        spannableString.setSpan(new VerticalImageSpan(mContext
                                , R.mipmap.ic_news_type_strategy_expert), 0, 1, 0);
                        break;
                    case "2":// 实时策略
                        spannableString.append("  ");
                        spannableString.setSpan(new VerticalImageSpan(mContext
                                , R.mipmap.ic_news_type_strategy_real), 0, 1, 0);
                        break;
                    case "3":// 资讯
                        spannableString.append("  ");
                        spannableString.setSpan(new VerticalImageSpan(mContext
                                , R.mipmap.ic_news_type_info), 0, 1, 0);
                        break;
                    case "4":// 活动
                        spannableString.append("  ");
                        spannableString.setSpan(new VerticalImageSpan(mContext
                                , R.mipmap.ic_news_type_active), 0, 1, 0);
                        break;
                    case "5":// 早晚快报
                        spannableString.append("  ");
                        spannableString.setSpan(new VerticalImageSpan(mContext
                                , R.mipmap.ic_news_type_quick_comm), 0, 1, 0);
                        break;
                    case "6":// 牛人回顾
                        spannableString.append("  ");
                        spannableString.setSpan(new VerticalImageSpan(mContext
                                , R.mipmap.ic_news_type_review), 0, 1, 0);
                        break;
                    case "7":// 牛圈热门
                        spannableString.append("  ");
                        spannableString.setSpan(new VerticalImageSpan(mContext
                                , R.mipmap.ic_news_type_publish), 0, 1, 0);
                        break;
                    case "8":// 牛圈热门
                        spannableString.append("  ");
                        spannableString.setSpan(new VerticalImageSpan(mContext
                                , R.mipmap.ic_news_type_active_publish), 0, 1, 0);
                        break;
                    case "9":// 盈利分享
                        content.setGravity(Gravity.CENTER);
                        break;
                }
                result = spannableString.append(Html.fromHtml(message));
            } else {
                result = message;
            }

            return result;
        }
    }

    /**
     * 点赞
     *
     * @param self
     * @param type
     * @param item
     * @param tv
     * @param iv
     */
    private void declareForTopicThread(final LinearLayout self, final String type, final PostAndReplyBean item, final TextView tv, final ImageView iv) {
        final String type2 = type;
        self.setEnabled(false);
//        ServerAPI.declareForTopic(mContext, type, item.topic_id, new ServerCallBack<String>() {
//            @Override
//            public void onSucceed(Context context, String result) {
//                self.setEnabled(true);
//                if (type2.equals("1")) {
//                    item.attutude = "1";
//                    item.support_num = Integer.valueOf(item.support_num) + 1 + "";
//                    iv.setImageResource(R.drawable.ic_support);
//                    tv.setText(item.support_num);
//                } else if (type2.equals("2")) {
//                    item.attutude = "2";
//                    item.oppose_num = Integer.valueOf(item.oppose_num) + 1 + "";
//                    iv.setImageResource(R.drawable.ic_support);
//                    tv.setText(item.oppose_num);
//                }
//                AnimUtils.startZanScaleAnim(iv);
//                AttutudeUser a = new AttutudeUser();
//                a.headimgurl = SPUtil.getString(mContext, "headimg", "");
//                a.uid = SPUtil.getString(mContext, "user_id", "");
//                a.user_name = SPUtil.getString(mContext, "user_name", "");
//                item.attutude_user.add(a);
//                notifyDataSetChanged();
//            }
//
//            @Override
//            public void onError(Context context, String errorMsg) {
//                self.setEnabled(true);
//                Toaster.toast("操作失败，原因：" + errorMsg);
//            }
//
//            @Override
//            public void onFinished(Context context) {
//            }
//        });
    }

    public interface ToRefreshListViewListener {
        void refresh();
    }

    //关注回调，需要时可以添加
    public interface AttentionCallBack {
        void attention(String uid, String follow);
    }

    public void setAttentionCallBack(AttentionCallBack callBack) {
        this.mAttentionCallBack = callBack;
    }

    //关注回调，需要时可以添加
    public interface PraiseCallBack {
        void praise(String topic, String num);
    }

    public void setPraiseCallBack(PraiseCallBack callBack) {
        this.mPraiseCallBack = callBack;
    }
}
