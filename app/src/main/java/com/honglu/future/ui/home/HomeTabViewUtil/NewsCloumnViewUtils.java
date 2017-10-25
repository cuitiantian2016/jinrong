package com.honglu.future.ui.home.HomeTabViewUtil;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honglu.future.R;
import com.honglu.future.ui.home.bean.HomeMessageItem;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.util.ImageUtil;
import com.honglu.future.util.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * deprecation:海豚专栏
 * author:ayb
 * time:2017/8/25
 */
public class NewsCloumnViewUtils {

    /**
     * 刷新海豚专栏布局列表
     *
     * @param parentLy 外层父布局
     * @param list     内容列表
     */
    public static void refreshEconomicViews(LinearLayout parentLy, List<HomeMessageItem.DataBeanX.DataBean> list) {
        if (list == null || list.size() == 0) {
            parentLy.removeAllViews();
            View inflate = View.inflate(parentLy.getContext(), R.layout.empty_view, null);
            parentLy.addView(inflate);
            return;
        }
        try {
            int preCount = parentLy.getChildCount();
            int currentCount = list.size();
            if (currentCount > preCount) {
                for (int i = 0; i < currentCount - preCount; i++) {
                    if (TextUtils.equals(list.get(i).getType(), "1")) {
                        View view = LayoutInflater.from(parentLy.getContext()).inflate(R.layout.home_newest_itemtype1, null);
                        BaseViewHolder viewHolder = new TypeOneViewHolder(view);
                        view.setTag(viewHolder);
                        parentLy.addView(view);
                    } else {
                        View view = LayoutInflater.from(parentLy.getContext()).inflate(R.layout.home_newest_itemtype2, null);
                        BaseViewHolder viewHolder = new TypeTwoViewHolder(view);
                        view.setTag(viewHolder);
                        parentLy.addView(view);
                    }
                }
            } else {
                for (int i = preCount - currentCount - 1; i >= 0; i--) {
                    View childView = parentLy.getChildAt(i);
                    parentLy.removeView(childView);
                }
            }

            for (int i = 0; i < parentLy.getChildCount(); i++) {
                HomeMessageItem.DataBeanX.DataBean item = list.get(i);
                View view = parentLy.getChildAt(i);
                BaseViewHolder viewHolder = (BaseViewHolder) view.getTag();
                if (viewHolder == null) {
                    if (TextUtils.equals(list.get(i).getType(), "1")) {
                        viewHolder = new TypeOneViewHolder(view);
                        view.setTag(viewHolder);
                    } else {
                        viewHolder = new TypeTwoViewHolder(view);
                        view.setTag(viewHolder);
                    }
                }
                viewHolder.bindView(item, i);
            }
        } catch (Exception e) {
            //LogUtils.e(e.getMessage());
        }
    }

    //消息时间处理方法
    public static String computingTime(String time) {
        String backTime = "";
        //获取系统时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        //获取当前序列时间
        String mCurrentTime = formatter.format(curDate);
        String mdate = time.substring(8, 10);
        String mCurrentDate = mCurrentTime.substring(8, 10);
        String mMonth = time.substring(5, 7);
        String mCurrentMonth = mCurrentTime.substring(5, 7);
        String mYear = time.substring(0, 4);
        String mCurrentYear = mCurrentTime.substring(0, 4);
        if (Integer.parseInt(mCurrentYear) - Integer.parseInt(mYear) == 0) {
            //当年
            if (Integer.parseInt(mCurrentMonth) - Integer.parseInt(mMonth) == 0) {
                //当月
                if (Integer.parseInt(mCurrentDate) - Integer.parseInt(mdate) == 0) {
                    //当天
                    backTime = time.substring(11, 16);

                } else if (Integer.parseInt(mCurrentDate) - Integer.parseInt(mdate) == 1) {
                    //昨天
                    backTime = "昨天";

                } else if (Integer.parseInt(mCurrentDate) - Integer.parseInt(mdate) > 1) {
                    //前天包括之前
                    backTime = time.substring(5, 10);

                }
            } else {
                //上月或者之前
                //本月第一天与上月最后一天处理
                Date date = new Date();
                long l = 24 * 60 * 60 * 1000; //每天的毫秒数
                long dateTime = date.getTime();//获得现在的毫秒数
                long dataTimeZero = dateTime - (dateTime % l) - 8 * 60 * 60 * 1000;//当天零点的毫秒值
                if (dataTimeZero - TimeUtil.dateToLong(time) > 0 && dataTimeZero - TimeUtil.dateToLong(time) < l) {
                    //昨天
                    backTime = "昨天";
                } else {
                    backTime = time.substring(5, 10);
                }
            }
        } else {
            //去年或之前
            backTime = time.substring(0, 7);
        }
        return backTime;
    }

    public static class BaseViewHolder {
        int Type;

        public void bindView(HomeMessageItem.DataBeanX.DataBean item, int position) {

        }
    }

    /**
     * 第一种类型
     */
    public static class TypeOneViewHolder extends BaseViewHolder {
        ImageView newest_content_iv;
        RelativeLayout rel_left;
        LinearLayout content_ll;
        TextView tv_title;
        TextView tv_time;
        TextView mTvName;
        TextView tv_praise;
        TextView tvReplyNum;
        TextView mTvUserType;
        ImageView profit_icon;
        ImageView mIvAvatar;

        public TypeOneViewHolder(View convertView) {
            Type = 0;
            rel_left = (RelativeLayout) convertView.findViewById(R.id.rel_left);
            newest_content_iv = (ImageView) convertView.findViewById(R.id.newest_content_iv);
            content_ll = (LinearLayout) convertView.findViewById(R.id.content_ll);
            tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            tvReplyNum = (TextView) convertView.findViewById(R.id.tv_reply_num);
            tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            tv_praise = (TextView) convertView.findViewById(R.id.tv_praise);
            profit_icon = (ImageView) convertView.findViewById(R.id.profit_icon);
            mIvAvatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            mTvName = (TextView) convertView.findViewById(R.id.tv_name);
            mTvUserType = (TextView) convertView.findViewById(R.id.tv_user_type);
        }

        @Override
        public void bindView(final HomeMessageItem.DataBeanX.DataBean item, final int position) {
            super.bindView(item, position);
            ImageUtil.display(item.getHomePic(), newest_content_iv, R.mipmap.other_empty);
            ImageUtil.display(item.getUserAvatar(), mIvAvatar, R.mipmap.iv_no_image);

            if (!TextUtils.isEmpty(item.getModifyTime()) && item.getModifyTime().length() > 16) {
                tv_time.setText(computingTime(item.getCreateTime()));
            }
            if (!TextUtils.isEmpty(item.getNickname())) {
                mTvName.setText(item.getNickname());
            } else {
                mTvName.setText("");
            }

            if (!TextUtils.isEmpty(item.getUserRole())) {
                mTvUserType.setText(item.getUserRole());
            } else {
                mTvUserType.setText("");
            }

            tv_praise.setText(String.valueOf(item.getPraiseCounts()));
            if (item.isPraise > 0) {
                profit_icon.setImageResource(R.mipmap.ic_support);
            } else {
                profit_icon.setImageResource(R.mipmap.ic_support_done);
            }

            tv_title.setText(item.getTitle());
            tvReplyNum.setText(item.getCommentNum() + "条评论");
            content_ll.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {

                                              }
                                          }

            );
            rel_left.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    initParams(newest_content_iv, rel_left.getHeight());
                    rel_left.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
    }

    /**
     * 第二种类型
     */
    public static class TypeTwoViewHolder extends BaseViewHolder {
        private Context mContext;
        public TextView newset_contenttype2;
        public ImageView typetwonewest_content_iv;
        public LinearLayout ziti_layout_typetwo;
        public TextView tv_timetype2;
        public TextView tv_praisetype2;
        public TextView tv_user_role2;
        ImageView profit_icon;

        public TypeTwoViewHolder(View convertView) {
            mContext = convertView.getContext();
            Type = 1;
            typetwonewest_content_iv = (ImageView) convertView.findViewById(R.id.typetwonewest_content_iv);
            ziti_layout_typetwo = (LinearLayout) convertView.findViewById(R.id.ziti_layout_typetwo);
            newset_contenttype2 = (TextView) convertView.findViewById(R.id.newset_contenttype2);
            tv_timetype2 = (TextView) convertView.findViewById(R.id.tv_timetype2);
            tv_user_role2 = (TextView) convertView.findViewById(R.id.tv_user_role2);
            tv_praisetype2 = (TextView) convertView.findViewById(R.id.tv_praisetype2);
            profit_icon = (ImageView) convertView.findViewById(R.id.profit_icon);
        }

        @Override
        public void bindView(final HomeMessageItem.DataBeanX.DataBean item, final int position) {
            super.bindView(item, position);
            ImageUtil.display(item.getPicOne(), typetwonewest_content_iv, R.mipmap.other_empty);
            newset_contenttype2.setText(item.getTitle());

            LinearLayout.LayoutParams llp = (LinearLayout.LayoutParams) typetwonewest_content_iv.getLayoutParams();
            llp.width = DeviceUtils.getScreenWidth(mContext) - DeviceUtils.dip2px(mContext, 20);
            llp.height = llp.width * 316 / 702;
            typetwonewest_content_iv.setLayoutParams(llp);
            tv_timetype2.setText(computingTime(item.getCreateTime()));
            tv_praisetype2.setText(String.valueOf(item.getPraiseCounts()));
            if (item.isPraise > 0) {
                profit_icon.setImageResource(R.mipmap.ic_support);
            } else {
                profit_icon.setImageResource(R.mipmap.ic_support_done);
            }
            if (!TextUtils.isEmpty(item.getUserRole())) {
                tv_user_role2.setText(item.getUserRole());
            } else {
                tv_user_role2.setText("");
            }
            ziti_layout_typetwo.setOnClickListener(new View.OnClickListener() {
                                                       @Override
                                                       public void onClick(View v) {
                                                           if (DeviceUtils.isFastDoubleClick()) {
                                                               return;
                                                           }
                                                           if (item != null) {

                                                           }
                                                       }
                                                   }

            );
        }
    }

    private static void initParams(View view, int height) {
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) view.getLayoutParams();
        rlp.height = height;
        rlp.width = (int) (210 * rlp.height / 166);
        view.setLayoutParams(rlp);
    }

}
