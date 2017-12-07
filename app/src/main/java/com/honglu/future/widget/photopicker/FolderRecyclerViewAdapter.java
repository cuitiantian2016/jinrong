package com.honglu.future.widget.photopicker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.honglu.future.R;
import com.honglu.future.widget.photopicker.models.FolderItem;
import com.honglu.future.widget.photopicker.models.FolderListContent;

import java.util.List;

public class FolderRecyclerViewAdapter extends RecyclerView.Adapter<FolderRecyclerViewAdapter.ViewHolder> {

    private final List<FolderItem> mValues;
    private final OnFolderRecyclerViewInteractionListener mListener;
    private final String TAG = "FolderAdapter";

    public FolderRecyclerViewAdapter(List<FolderItem> items, OnFolderRecyclerViewInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popup_folder_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        FolderItem folderItem = mValues.get(position);
        holder.mItem = folderItem;
        holder.folderName.setText(folderItem.name);
        holder.folderPath.setText(folderItem.path);
        holder.folderSize.setText(folderItem.getNumOfImages());

        if(position == FolderListContent.selectedFolderIndex) {
            holder.folderIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.folderIndicator.setVisibility(View.GONE);
        }

        Glide.with(holder.folderCover.getContext()).load(folderItem.coverImagePath).fitCenter()
                .placeholder(R.drawable.ic_loading).dontAnimate().into(holder.folderCover);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pass the selected result to FolderListContent
                int previousSelectedIndex = FolderListContent.selectedFolderIndex;
                FolderListContent.setSelectedFolder(holder.mItem, position);
                // we should notify previous item and current time to change
                notifyItemChanged(previousSelectedIndex);
                notifyItemChanged(position);
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onFolderItemInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FolderItem mItem;
        View mView;
        ImageView folderCover;
        TextView folderName;
        TextView folderPath;
        TextView folderSize;
        ImageView folderIndicator;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            folderCover = (ImageView) view.findViewById(R.id.folder_cover_image);
            folderName = (TextView) view.findViewById(R.id.folder_name);
            folderPath = (TextView) view.findViewById(R.id.folder_path);
            folderSize = (TextView) view.findViewById(R.id.folder_size);
            folderIndicator = (ImageView) view.findViewById(R.id.folder_selected_indicator);
        }

        @Override
        public String toString() {
            return "ViewHolder{" +
                    "folderCover=" + folderCover +
                    ", mView=" + mView +
                    ", folderName=" + folderName +
                    ", folderPath=" + folderPath +
                    ", folderSize=" + folderSize +
                    ", folderIndicator=" + folderIndicator +
                    '}';
        }
    }
}
