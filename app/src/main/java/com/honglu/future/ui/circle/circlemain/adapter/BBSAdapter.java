package com.honglu.future.ui.circle.circlemain.adapter;

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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.honglu.future.R;
import com.honglu.future.config.Constant;
import com.honglu.future.ui.circle.bean.BBS;
import com.honglu.future.ui.circle.circlemain.OnClickThrottleListener;
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
import java.util.Arrays;
import java.util.List;

public class BBSAdapter extends BaseAdapter {

    private Context mContext;
    private ListView mListView;
    private ScrollToLastCallBack mScrollToLastCallBack = null;
    private ToRefreshListViewListener mToRefefreshListViewListener;
    private List<BBS> mList = new ArrayList<>();
    private String topicType;
    private AttentionCallBack mAttentionCallBack;
    public List<BBS> getList() {
        return mList;
    }
    private ClipboardManager cmb;

    public BBSAdapter(ListView listview, Context context, ScrollToLastCallBack callback) {
        mListView = listview;
        mContext = context;
        mScrollToLastCallBack = callback;
        cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
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

    public void refreshDatas(BBS item) {
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).uid.equals(item.uid)) {
                mList.get(i).follow = item.follow;
            }
        }
        if (mAttentionCallBack != null) {
            mAttentionCallBack.attention(item.uid, item.follow);
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
        TextView announce_time,  support, reply, hot_title;

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
            ll_reward.setOnClickListener(new OnClickThrottleListener() {
                @Override
                protected void onThrottleClick(View v) {

                }
            });
            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (DeviceUtils.isFastDoubleClick())
                        return;
                    if (item != null) {
                       // reportBBSItemClickEvent();
//                        Intent intent = new Intent(v.getContext(), BBSDetailActivity.class);
//                        Bundle b = new Bundle();
//                        b.putSerializable("bbs_item", item);
//                        intent.putExtras(b);
//                        intent.putExtra(BBSDetailActivity.EXTRA_FROM, "apptopic");
//                        intent.putExtra(BBSDetailActivity.EXTRA_TAB, topicType);
//                        v.getContext().startActivity(intent);
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
                    cmb.setText(item.content);
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
//                        Intent intent = new Intent(v.getContext(), BBSDetailActivity.class);
//                        Bundle b = new Bundle();
//                        b.putSerializable("bbs_item", item);
//                        intent.putExtras(b);
//                        intent.putExtra(BBSDetailActivity.EXTRA_FROM, "apptopic");
//                        intent.putExtra(BBSDetailActivity.EXTRA_TAB, topicType);
//                        v.getContext().startActivity(intent);
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
            ViewHelper.safelySetText(txt_reward_number, item.integralUserNum);

            status.setVisibility(TextUtils.isEmpty(item.user_level) ? View.GONE : View.VISIBLE);
            status.setText(item.user_level);
            if (item.uid.equals(SpUtil.getString(Constant.CACHE_TAG_UID))) {
                follow.setVisibility(View.INVISIBLE);
            } else {
                follow.setVisibility(View.VISIBLE);
                follow.setImageResource(("1".equals(item.follow)) ? R.mipmap.already_recommend : R.drawable.add_recommend);
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
                        String type = item.follow.equals("1") ? "2" : "1";
//                        ServerAPI.follow(mContext, type, item.uid, new ServerCallBack<JSONObject>() {
//                            @Override
//                            public void onSucceed(Context context, JSONObject result) {
//                                try {
//                                    String msg = result.getString("msg");
//                                    if (msg.equals("取消关注成功")) {
//                                        follow.setImageResource(R.drawable.add_recommend);
//                                        item.follow = "0";
//                                    } else if (msg.equals("关注成功")) {
//                                        follow.setImageResource(R.drawable.already_recommend);
//                                        item.follow = "1";
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                                refreshDatas(item);
//                            }
//
//                            @Override
//                            public void onError(Context context, String errorMsg) {
//                                Toaster.toast(errorMsg);
//                            }
//
//                            @Override
//                            public void onFinished(Context context) {
//                            }
//                        });
                    }
                });
            }
            header_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (DeviceUtils.isFastDoubleClick())
                        return;
                    if (!SpUtil.getString(Constant.CACHE_TAG_UID).equals(item.uid)) {
                    } else {
                    }
                }
            });
            mMultiImgLy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (DeviceUtils.isFastDoubleClick())
                        return;
                    if (!TextUtils.isEmpty(item.video_url)) {

                    }
                }
            });
            ViewHelper.setVisibility(best, item.isEssence());
            ViewHelper.safelySetText(hot_title, item.hot_topic_title);
            ViewHelper.safelySetText(announce_time, item.announce_time);// 热门不展示
            if (item.replyList != null && item.replyList.size() != 0) {
                ReplyListAdapter replyListAdapter = new ReplyListAdapter();
                replyListAdapter.addView(mContext,replay_ll_BB, item.replyList,topicType,item);
                ViewHelper.setVisibility(replay_ll_BB, true);
                ViewHelper.setVisibility(mSeparatorCommLy, true);
            } else {
                ViewHelper.setVisibility(replay_ll_BB, false);
                ViewHelper.setVisibility(mSeparatorCommLy, false);
            }

            content.setLineSpacing(0, 1.2f);
            content.setText(getNewsContentByType(item.topic_type, item.content + ""));
            if (!TextUtils.isEmpty(item.images)) {
                String[] imgs;
                if (item.images.contains(";")) {
                    imgs = item.images.split(";");
                } else {
                    imgs = new String[]{item.images};
                }
                final List<String> imgList = Arrays.asList(imgs);
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
                        if (imgList != null && imgList.size() > 0) {

                            if (!TextUtils.isEmpty(item.video_url)) {
                                // WmbbUtil.openWmbbScheme(mListView.getContext(), item.video_url);
                            } else {
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
                    }
                });
                mMultiImgLy.setImagesData(imgList);
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
                    if (item.attutude.equals("1") || item.attutude.equals("2"))
                        return;
                    declareForTopicThread(support_iv, "1", item, support, iv_heart);
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

            int end = mListView.getLastVisiblePosition();
            if (getCount() - 2 <= end && end <= getCount())
                mScrollToLastCallBack.onScrollToLast(position);
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

    private void declareForTopicThread(final LinearLayout self, final String type, final BBS item, final TextView tv, final ImageView iv) {
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
}
