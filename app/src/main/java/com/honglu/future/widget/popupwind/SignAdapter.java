package com.honglu.future.widget.popupwind;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.JsonNull;
import com.honglu.future.R;
import com.honglu.future.config.Constant;
import com.honglu.future.http.HttpManager;
import com.honglu.future.http.HttpSubscriber;
import com.honglu.future.http.RxHelper;
import com.honglu.future.ui.circle.bean.SignBean;
import com.honglu.future.util.SpUtil;
import com.honglu.future.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class SignAdapter extends BaseAdapter {
	private List<SignBean.SignListBean> mList = new ArrayList<>();
	private static final String EMPTY = "empty";
	PopupWindow popupWindow;
	Handler handler;
	public SignAdapter(PopupWindow popupWindow,Handler handler){
		this.popupWindow =popupWindow;
		this.handler = handler;
	}
	public void refreshPhotos(List<SignBean.SignListBean> list){
		if (list != null){
			mList.clear();
			mList.addAll(list);
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public SignBean.SignListBean getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PublishImgViewHolder holder;
		convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sign, parent, false);
		holder = new PublishImgViewHolder(convertView);
		SignBean.SignListBean item = getItem(position);
		holder.bindView(item,position);
		setListener(holder);
		return convertView;
	}

	private void setListener(final PublishImgViewHolder holder) {
		final SignBean.SignListBean item = getItem(holder.position);
		View.OnClickListener onClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				saveSingInfo(item,holder.position);
			}
		};
		if (item.isSignClick()){
			holder.view.setOnClickListener(onClickListener);
		}else {
			holder.view.setOnClickListener(null);
		}

	}


	//签到http
	private void saveSingInfo(final SignBean.SignListBean bean ,int position) {
		HttpManager.getApi().saveSignData(SpUtil.getString(Constant.CACHE_TAG_MOBILE),SpUtil.getString(Constant.CACHE_TAG_UID),position+1).compose(RxHelper.<JsonNull>handleSimplyResult())
				.subscribe(new HttpSubscriber<JsonNull>() {
					@Override
					protected void _onError(String message) {
						super._onError(message);
						ToastUtil.show(message);
					}

					@Override
					protected void _onNext(JsonNull jsonNull) {
						super._onNext(jsonNull);
						bean.setSignClick(false);
						bean.setSign(true);
						//当前item 已签到
						notifyDataSetChanged();
						ToastUtil.show("签到成功");
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								popupWindow.dismiss();
							}
						},500);
					}
				});
	}


	static class PublishImgViewHolder {
		ImageView iv_sign_image;
		View v_sign_selected,bg_view,view;
		TextView tv_day ,tv_money;
		int position;

		public PublishImgViewHolder(View view) {
			this.view = view;
			iv_sign_image = (ImageView) view.findViewById(R.id.iv_sign_image);
			v_sign_selected = view.findViewById(R.id.v_sign_selected);
			tv_day = (TextView) view.findViewById(R.id.tv_day);
			bg_view =  view.findViewById(R.id.bg_view);
			tv_money = (TextView) view.findViewById(R.id.tv_money);
			v_sign_selected.setVisibility(View.INVISIBLE);
		}

		public void bindView(SignBean.SignListBean item, int position) {
			this.position = position;
			if (position == 6){
				iv_sign_image.setImageResource(R.mipmap.icon_nb_sd);
			}else {
				iv_sign_image.setImageResource(R.drawable.sign_img);
			}
			bg_view.setVisibility(View.GONE);
			tv_day.setText(view.getContext().getString(R.string.the_first_few_days, item.getParamNameOne()));
			tv_money.setText(view.getContext().getString(R.string.integral_s, item.getParamValueOne()));
			if (item.isSign()){
				v_sign_selected.setVisibility(View.VISIBLE);
				view.setAlpha(1f);
			}else {
				v_sign_selected.setVisibility(View.INVISIBLE);
				view.setAlpha(0.2f);
			}
			if (item.isSignClick()){
				v_sign_selected.setVisibility(View.INVISIBLE);
				view.setAlpha(1f);
			}
		}

	}


}
