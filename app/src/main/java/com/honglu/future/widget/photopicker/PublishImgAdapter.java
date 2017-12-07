package com.honglu.future.widget.photopicker;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.honglu.future.R;


import java.io.File;
import java.util.ArrayList;

public class PublishImgAdapter extends BaseAdapter {
	private ArrayList<String> mList = new ArrayList<>();
	private static final String EMPTY = "empty";
	private PublishClickCallBack mPublishClickCallBack;

	public void setOnPublishClickCallBack(PublishClickCallBack publishClickCallBack){
		if (publishClickCallBack != null){
			mPublishClickCallBack = publishClickCallBack;
		}
	}

	public void refreshPhotos(ArrayList<String> list){
		if (list != null){
			mList.clear();
			mList.addAll(list);
			if (list.size() == 0 || list.size() != 9){
				mList.add(EMPTY);
			}
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public String getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PublishImgViewHolder holder;
		convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
		holder = new PublishImgViewHolder(convertView);
		setListener(holder);
		String item = getItem(position);
		holder.bindView(item,position);
		return convertView;
	}

	private void setListener(final PublishImgViewHolder holder) {
		View.OnClickListener onClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String item = getItem(holder.position);
				if (item.equals(EMPTY)){
					if (mPublishClickCallBack != null){
						mPublishClickCallBack.toAddImage();
					}
				}else {
					if (mPublishClickCallBack != null){
						mPublishClickCallBack.onClick(holder.position);
					}
				}
			}
		};
		holder.ivPhoto.setOnClickListener(onClickListener);
	}


	static class PublishImgViewHolder {
		ImageView ivPhoto;
		View vSelected;
		int position;

		public PublishImgViewHolder(View view) {
			ivPhoto = (ImageView) view.findViewById(R.id.iv_photo);
			vSelected = view.findViewById(R.id.v_selected);
			vSelected.setVisibility(View.GONE);
		}

		public void bindView(String item, int position) {
			this.position = position;
			if (item.equals(EMPTY)){
				ivPhoto.setScaleType(ImageView.ScaleType.FIT_XY);
				ivPhoto.setBackgroundResource(R.drawable.photo_no_bg);
				ivPhoto.setImageResource(R.drawable.icon_addpic_shaidan);
			}else {
				ivPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
				ivPhoto.setBackgroundResource(R.drawable.photo_bg);
				Uri uri = Uri.fromFile(new File(item));
				Glide.with(ivPhoto.getContext()).load(uri).centerCrop().
						thumbnail(0.1f).placeholder(R.drawable.ic_loading).into(ivPhoto);
			}
		}

	}

	public interface PublishClickCallBack {
		void toAddImage();
		void onClick(int position);
	}

}
