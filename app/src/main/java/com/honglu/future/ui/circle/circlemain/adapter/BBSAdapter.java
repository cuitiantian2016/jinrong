package com.honglu.future.ui.circle.circlemain.adapter;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonNull;
import com.honglu.future.R;
import com.honglu.future.config.ConfigUtil;
import com.honglu.future.config.Constant;
import com.honglu.future.dialog.areward.ArewardDialog;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.http.RxHelper;
import com.honglu.future.ui.circle.bean.BBS;
import com.honglu.future.ui.circle.circledetail.CircleDetailActivity;
import com.honglu.future.ui.circle.circlemain.OnClickThrottleListener;
import com.honglu.future.ui.circle.circlemine.CircleMineActivity;
import com.honglu.future.ui.register.activity.RegisterActivity;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.util.LogUtils;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;
import com.honglu.future.util.ViewHelper;
import com.honglu.future.widget.AllListView;
import com.honglu.future.widget.CircleImageView;
import com.honglu.future.widget.gridimage.NineGridImageView;
import com.honglu.future.widget.gridimage.NineGridImageViewAdapter;
import com.honglu.future.widget.photo.FullScreenDisplayActivity;

import java.util.ArrayList;
import java.util.List;


public class BBSAdapter extends BaseAdapter {

    private Activity mContext;
    private ListView mListView;
    private ScrollToLastCallBack mScrollToLastCallBack = null;
    private ToRefreshListViewListener mToRefefreshListViewListener;
    private List<BBS> mList = new ArrayList<>();
    private String topicType;
    private AttentionCallBack mAttentionCallBack;
    private PraiseCallBack mPraiseCallBack;
    private ArewardDialog mArewardDialog;

    public List<BBS> getList() {
        return mList;
    }

    private ClipboardManager cmb;

    public BBSAdapter(ListView listview, Activity context, ScrollToLastCallBack callback,ArewardDialog arewardDialog) {
        mListView = listview;
        mContext = context;
        mScrollToLastCallBack = callback;
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

    public void setDatas(List<BBS> list) {
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
            if (mList.get(i).uid.equals(uid)) {
                mList.get(i).follow = follow;
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
            if (mList.get(i).topic_id.equals(topic_id)) {
                mList.get(i).support_num = num;
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
    public BBS getItem(int position) {
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
        BBS item = getItem(position);
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
        TextView txt_reward_number;
        ImageView ic_reward_state;
        LinearLayout ll_reward;

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
            txt_reward_number = (TextView) convertView.findViewById(R.id.txt_reward_number);
            ic_reward_state = (ImageView) convertView.findViewById(R.id.ic_reward_state);
            ll_reward = (LinearLayout) convertView.findViewById(R.id.ll_reward);
        }

        public void bindView(final BBS item, int position) {
            //打赏
            ic_reward_state.setImageResource( (item.exceptional ? R.mipmap.ic_bbs_reward : R.mipmap.ic_bbs_reward_undone));
            ll_reward.setOnClickListener(new OnClickThrottleListener() {
                @Override
                protected void onThrottleClick(View v) {
                     if (TextUtils.equals(SpUtil.getString(Constant.CACHE_TAG_UID),item.uid)){
                         ToastUtil.show("自己不能打赏自己");
                     }else {
                         mArewardDialog.arewardCircle(ArewardDialog.AREWARD_CIRCLE_TYPE,item.uid,item.topic_id,item.user_name,item.header_img);
                     }

                }
            });
            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item != null) {
                        // reportBBSItemClickEvent();
                        Intent intent = new Intent(v.getContext(), CircleDetailActivity.class);
                        intent.putExtra(CircleDetailActivity.POST_USER_KEY, item.uid);
                        intent.putExtra(CircleDetailActivity.CIRCLEID_KEY, item.topic_id);
                        v.getContext().startActivity(intent);
                    }
                }
            });
            if (TextUtils.equals(SpUtil.getString(Constant.CACHE_TAG_UID), item.uid)) {
                follow.setVisibility(View.GONE);
            } else {
                follow.setVisibility(View.VISIBLE);
            }
            ImageUtil.display(item.header_img, header_img, R.mipmap.img_head);
            ViewHelper.safelySetText(user_name, item.user_name);
            ViewHelper.safelySetText(txt_reward_number, String.valueOf(item.exceptionalCount));



            status.setVisibility(TextUtils.isEmpty(item.user_level) ? View.GONE : View.VISIBLE);
            status.setText(item.user_level);
            if (SpUtil.getString(Constant.CACHE_TAG_UID).equals(item.uid)) {
                follow.setVisibility(View.INVISIBLE);
            } else {
                follow.setVisibility(View.VISIBLE);
                follow.setImageResource(("1".equals(item.follow)) ? R.mipmap.already_recommend : R.mipmap.add_recommend);
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
                        if (user_id.equals(item.uid)) {
                            ToastUtil.show("自己不能关注自己");
                            return;
                        }
                        final String foll = item.follow.equals("1") ? "0" : "1";
                        HttpManager.getApi().focus(item.uid, SpUtil.getString(Constant.CACHE_TAG_UID), foll).compose(RxHelper.<JsonNull>handleSimplyResult()).subscribe(new HttpSubscriber<JsonNull>() {
                            @Override
                            protected void _onNext(JsonNull jsonNull) {
                                super._onNext(jsonNull);
                                if (foll.equals("0")) {
                                    ToastUtil.show("取消关注成功");
                                } else {
                                    ToastUtil.show("关注成功");
                                }
                                follow.setImageResource(R.mipmap.already_recommend);
                                item.follow = foll;
                                follow(item.follow, item.uid);
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
                    Intent intent = new Intent(mContext, CircleMineActivity.class);
                    intent.putExtra("userId", item.uid);
                    if (SpUtil.getString(Constant.CACHE_TAG_UID).equals(item.uid)) {
                        intent.putExtra("imgHead", SpUtil.getString(Constant.CACHE_USER_AVATAR));
                    } else {
                        intent.putExtra("imgHead", item.header_img);
                    }
                    intent.putExtra("nickName", item.user_name);
                    mContext.startActivity(intent);
                }
            });
            ViewHelper.setVisibility(best, item.isEssence());
            ViewHelper.safelySetText(hot_title, item.hot_topic_title);
            ViewHelper.safelySetText(announce_time, item.announce_time);// 热门不展示
            if (item.getReplyList() != null && item.getReplyList().size() != 0) {
                ReplyListAdapter replyListAdapter = new ReplyListAdapter();
                replyListAdapter.addView(mContext, replay_ll_BB, item.getReplyList(), topicType, item);
                ViewHelper.setVisibility(replay_ll_BB, true);
                ViewHelper.setVisibility(mSeparatorCommLy, true);
            } else {
                ViewHelper.setVisibility(replay_ll_BB, false);
                ViewHelper.setVisibility(mSeparatorCommLy, false);
            }

            content.setLineSpacing(0, 1.2f);
            content.setText(getNewsContentByType(item.topic_type, item.content + ""));
            if (item.images != null) {
                mMultiImgLy.setAdapter(new NineGridImageViewAdapter<String>() {
                    @Override
                    protected void onDisplayImage(Context context, ImageView imageView, String url) {
                        if (TextUtils.isEmpty(url)) {
                            Glide.with(context).load(R.mipmap.other_empty).centerCrop().
                                    placeholder(R.mipmap.other_empty).error(R.mipmap.other_empty).
                                    diskCacheStrategy(DiskCacheStrategy.NONE).
                                    dontAnimate().into(imageView);
                        } else {
                            Glide.with(context).load(url).centerCrop().
                                    placeholder(R.mipmap.other_empty).error(R.mipmap.other_empty).
                                    diskCacheStrategy(DiskCacheStrategy.NONE).
                                    dontAnimate().into(imageView);
                        }
                    }

                    @Override
                    protected void onItemImageClick(View view, int index, List<String> list) {
                        super.onItemImageClick(view, index, list);
                        Intent intent = new Intent(view.getContext(), FullScreenDisplayActivity.class);
                        Bundle b = new Bundle();
                        ArrayList<String> images = new ArrayList<>();
                        if (item.images.size() > 0) {
                            images.addAll(item.images);
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
                mMultiImgLy.setImagesData(item.images);
            } else {
                mMultiImgLy.setImagesData(null);// 控件复用, 无数据时传空处理
            }


            if (item.attutude.equals("0")) {
                iv_heart.setImageResource(R.mipmap.ic_support_done);
            } else if (item.attutude.equals("1")) {
                iv_heart.setImageResource(R.mipmap.ic_support);
            } else if (item.attutude.equals("2")) {
                iv_heart.setImageResource(R.mipmap.ic_support_done);
            }

            support.setText(item.support_num);
            reply.setText(item.reply_num);

            support_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (DeviceUtils.isFastDoubleClick()) {
                        return;
                    }
                    if (TextUtils.isEmpty(SpUtil.getString(Constant.CACHE_TAG_UID))) {
                        mContext.startActivity(new Intent(mContext, RegisterActivity.class));
                        return;
                    }
                    if (item.attutude.equals("1")) {
                        ToastUtil.show("您已点赞");
                        return;
                    }
                    declareForTopicThread(support_iv, item);
                }
            });

            if (!TextUtils.isEmpty(item.topic_link)) {
                btn_lookDetail.setVisibility(View.VISIBLE);
                btn_lookDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(item.topic_link)) {
                            // WmbbUtil.openWmbbScheme(mContext, item.topic_link);
                        }
                    }
                });
            } else {
                btn_lookDetail.setVisibility(View.GONE);
            }
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
     * @param item
     */
    private void declareForTopicThread(final LinearLayout self, final BBS item) {
        self.setEnabled(false);
        HttpManager.getApi().praise(item.uid, SpUtil.getString(Constant.CACHE_TAG_UID), true, item.topic_id,SpUtil.getString(Constant.CACHE_TAG_USERNAME)).compose(RxHelper.<JsonNull>handleSimplyResult()).subscribe(new HttpSubscriber<JsonNull>() {
            @Override
            protected void _onNext(JsonNull jsonNull) {
                super._onNext(jsonNull);
                item.attutude = "1";
                item.support_num = Integer.valueOf(item.support_num) + 1 + "";
                self.setEnabled(true);
                praise(item.topic_id, item.support_num);
            }

            @Override
            protected void _onError(String message) {
                super._onError(message);
                ToastUtil.show(message);
                self.setEnabled(true);
            }
        });
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
