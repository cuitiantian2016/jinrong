package com.honglu.future.ui.home.HomeTabViewUtil;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.honglu.future.R;
import com.honglu.future.ui.home.bean.NewsFlashData;
import com.honglu.future.util.ImageUtil;
import java.util.List;


/**
 * deprecation:
 * author:hy
 * time:2017/8/26
 */

public class FastMsgViewUtils {

    public static void refreashDatas(List<NewsFlashData> datas, LinearLayout parentView) {
        if (datas ==null||datas.size()==0){
            parentView.removeAllViews();
            View inflate = View.inflate(parentView.getContext(), R.layout.empty_view, null);
            parentView.addView(inflate);
            return;
        }
        int preCount = parentView.getChildCount();
        int currentCount = datas.size();
        if(currentCount>preCount){
            for (int i = 0; i < currentCount-preCount; i++) {
                View view = LayoutInflater.from(parentView.getContext()).inflate(R.layout.view_24h_item, null);
                ViewHolder viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
                parentView.addView(view);
            }
        }else {
            for (int i = preCount-currentCount-1; i >= 0; i--) {
                View childView = parentView.getChildAt(i);
                parentView.removeView(childView);
            }
        }
        for (int i = 0; i < parentView.getChildCount(); i++) {
                final NewsFlashData entity = datas.get(i);
                final View view = parentView.getChildAt(i);
                ViewHolder viewHolder = (ViewHolder) view.getTag();
                if (viewHolder == null){
                    viewHolder = new ViewHolder(view);
                    view.setTag(viewHolder);
                }
                viewHolder.bindView(entity,datas , i);
        }
    }

    public static class ViewHolder{
        private TextView unfoldTv;
        private  TextView contentTv;
        private  ImageView photoIv;
        private  TextView dayTv;
        private  TextView mouthTv;
        private  LinearLayout dateLl;
        private  View mLineTop;
        private  TextView mTvTime;
        private CardView cv_img_activity;
    public ViewHolder(View view){
        contentTv = (TextView) view.findViewById(R.id.tv_content);
        unfoldTv = (TextView) view.findViewById(R.id.tv_unfold);
        photoIv = (ImageView) view.findViewById(R.id.iv_photo);
        dayTv = (TextView) view.findViewById(R.id.tv_day);
        mouthTv = (TextView) view.findViewById(R.id.tv_mouth);
        dateLl = (LinearLayout) view.findViewById(R.id.ll_date);
        mLineTop = view.findViewById(R.id.line_top);
        mTvTime = (TextView) view.findViewById(R.id.tv_time);
        cv_img_activity = (CardView) view.findViewById(R.id.cv_img_activity);
    }

    /**
     * 绑定数据
     * @param entity
     * @param datas
     * @param i
     */
    public void bindView(final NewsFlashData entity, List<NewsFlashData> datas, int i){
        contentTv.setTextColor(entity.isRedText() ? Color.parseColor("#FFA117") : Color.parseColor("#323232"));
        contentTv.setText(Html.fromHtml(TextUtils.isEmpty(entity.content)?"":entity.content));
        mTvTime.setText(entity.getTime());
        contentTv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                contentTv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (contentTv.getLayout() != null && contentTv.getLayout().getLineCount() > 3) {
                    unfoldTv.setVisibility(View.VISIBLE);
                    contentTv.setMaxLines(3);
                    contentTv.postInvalidate();
                } else {
                    unfoldTv.setVisibility(View.GONE);
                    contentTv.setMaxLines(10086);
                    contentTv.postInvalidate();
                }
            }
        });
        unfoldTv.setText("全文");
        unfoldTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.equals(unfoldTv.getText().toString(), "全文")) {
                    contentTv.setMaxLines(10086);
                    contentTv.postInvalidate();
                    unfoldTv.setText("收起");
                } else {
                    contentTv.setMaxLines(3);
                    contentTv.postInvalidate();
                    unfoldTv.setText("全文");
                }
            }
        });
        if (TextUtils.isEmpty(entity.img)){
            cv_img_activity.setVisibility(View.GONE);
        }else {
            cv_img_activity.setVisibility(View.VISIBLE);
            cv_img_activity.setOnClickListener(new View.OnClickListener() {//点击方大
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(view.getContext(), FullScreenDisplayActivity.class);
//                    Bundle b = new Bundle();
//                    ArrayList<String> images = new ArrayList<>();
//                    if (entity.img != null) {
//                        images.add(entity.img);
//                        b.putStringArrayList("image_urls", images);
//                        b.putInt("position", 0);
//                        intent.putExtras(b);
//                        int[] location = new int[2];
//                        view.getLocationOnScreen(location);
//                        intent.putExtra("locationX", location[0]);
//                        intent.putExtra("locationY", location[1]);
//                        intent.putExtra("width", view.getWidth());
//                        intent.putExtra("height", view.getHeight());
//                        view.getContext().startActivity(intent);
//                    }
                }
            });
            ImageUtil.display(entity.img, photoIv, R.mipmap.defult_24_hour);
        }
        boolean isNewDay = false;
        if (i == 0) {
            dateLl.setVisibility(View.VISIBLE);
            mLineTop.setVisibility(View.INVISIBLE);
            isNewDay = true;
        } else {
            NewsFlashData msgCurrent = datas.get(i);
            NewsFlashData msgPre = datas.get(i - 1);
            if (!TextUtils.equals(msgCurrent.date, msgPre.date)) {
                isNewDay = true;
                mLineTop.setVisibility(View.INVISIBLE);
                dateLl.setVisibility(View.VISIBLE);
            }else {
                mLineTop.setVisibility(View.VISIBLE);
                dateLl.setVisibility(View.GONE);
            }
        }
        if (isNewDay) {
            mLineTop.setVisibility(View.INVISIBLE);
            if (!TextUtils.isEmpty(entity.day)) {
                dayTv.setText(entity.day+"/");
            }
            mouthTv.setText(!TextUtils.isEmpty(entity.month) ? entity.month + "月" : "--月");
        }
    }}

}
