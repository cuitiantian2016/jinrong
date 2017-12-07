package com.honglu.future.widget.photopicker;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.honglu.future.R;
import com.honglu.future.util.DeviceUtils;
import com.honglu.future.widget.photopicker.models.ImageItem;
import com.honglu.future.widget.photopicker.models.ImageListContent;
import com.honglu.future.widget.photopicker.utilities.FileUtils;
import com.honglu.future.widget.photopicker.utilities.OnImageItemClickListener;

import java.io.File;
import java.util.List;

public class ImageRecyclerViewAdapter extends RecyclerView.Adapter<ImageRecyclerViewAdapter.ViewHolder> {

    private final List<ImageItem> mValues;
    private final OnImageRecyclerViewInteractionListener mListener;
    private OnImageItemClickListener mImageItemClickListener;

    public void setOnImageItemClickListener(OnImageItemClickListener onImageItemClickListener){
        if (onImageItemClickListener != null){
            mImageItemClickListener = onImageItemClickListener;
        }
    }




    public ImageRecyclerViewAdapter(List<ImageItem> items, OnImageRecyclerViewInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_image_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ImageItem imageItem = mValues.get(position);
        holder.mItem = imageItem;

        Uri newURI;
        if (!imageItem.isCamera()) {
            // draw image first
            File imageFile = new File(imageItem.path);
            if (imageFile.exists()) {
                newURI = Uri.fromFile(imageFile);
            } else {
                newURI = FileUtils.getUriByResId(R.drawable.ic_loading);
            }
            Glide.with(holder.mDrawee.getContext()).load(newURI).fitCenter()
                    .placeholder(R.drawable.ic_loading).dontAnimate().into(holder.mDrawee);
            holder.mImageName.setVisibility(View.GONE);
            holder.mChecked.setVisibility(View.VISIBLE);
            if (ImageListContent.isImageSelected(imageItem.path)) {
                holder.mMask.setVisibility(View.VISIBLE);
                holder.mChecked.setImageResource(R.drawable.image_selected);
            } else {
                holder.mMask.setVisibility(View.GONE);
                holder.mChecked.setImageResource(R.drawable.image_unselected);
            }
        } else {
            // camera icon, not normal image
            newURI = FileUtils.getUriByResId(R.drawable.ic_photo_camera_white_48dp);
            Glide.with(holder.mDrawee.getContext()).load(newURI).fitCenter()
                    .placeholder(R.drawable.ic_photo_camera_white_48dp).dontAnimate().into(holder.mDrawee);

            holder.mImageName.setVisibility(View.VISIBLE);
            holder.mChecked.setVisibility(View.GONE);
            holder.mMask.setVisibility(View.GONE);
        }

        holder.mDrawee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mImageItemClickListener != null){
                    if(!holder.mItem.isCamera()) {
                        if(ImageListContent.isImageSelected(imageItem.path)){
                            mImageItemClickListener.onImageClick(position);
                        }
                    }else{
                        if (null != mListener) {
                            // Notify the active callbacks interface (the activity, if the
                            // fragment is attached to one) that an item has been selected.
                            mListener.onImageItemInteraction(holder.mItem);
                        }
                    }
                }
            }
        });


        holder.mChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log.d(TAG, "onClick: " + holder.mItem.toString());
                if(!holder.mItem.isCamera()) {
                    if(!ImageListContent.isImageSelected(imageItem.path)) {
                        // just select one new image, make sure total number is ok
                        if(ImageListContent.SELECTED_IMAGES.size() < SelectorSettings.mMaxImageNumber) {
                            ImageListContent.toggleImageSelected(imageItem.path);
                            notifyItemChanged(position);
                        } else {
                            // set flag
                            ImageListContent.bReachMaxNumber = true;
                        }
                    } else {
                        // deselect
                        ImageListContent.toggleImageSelected(imageItem.path);
                        notifyItemChanged(position);
                    }
                } else {
                    // do nothing here, listener will launch camera to capture image
                }
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onImageItemInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mDrawee;
        public final ImageView mChecked;
        public final View mMask;
        public ImageItem mItem;
        public TextView mImageName;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDrawee = (ImageView) view.findViewById(R.id.image_drawee);
            int scale = DeviceUtils.getScreenWidth(view.getContext())/3;
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(scale,scale);
            mDrawee.setLayoutParams(params);
            assert mDrawee != null;
            mMask = view.findViewById(R.id.image_mask);
            assert mMask != null;
            mChecked = (ImageView) view.findViewById(R.id.image_checked);
            assert mChecked != null;
            mImageName = (TextView) view.findViewById(R.id.image_name);
            assert mImageName != null;
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
