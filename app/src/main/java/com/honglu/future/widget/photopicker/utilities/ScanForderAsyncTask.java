package com.honglu.future.widget.photopicker.utilities;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;


import com.honglu.future.R;
import com.honglu.future.widget.photopicker.SelectorSettings;
import com.honglu.future.widget.photopicker.models.FolderItem;
import com.honglu.future.widget.photopicker.models.FolderListContent;
import com.honglu.future.widget.photopicker.models.ImageItem;
import com.honglu.future.widget.photopicker.models.ImageListContent;
import com.honglu.future.widget.photopicker.models.ScanForderParmas;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Anyb
 * 替代rxjava框架(rxJava跟项目有冲突)
 */
public class ScanForderAsyncTask extends AsyncTask<ScanForderParmas, Void, ScanForderParmas> {
    private final String[] projections = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media._ID};

    @Override
    protected ScanForderParmas doInBackground(ScanForderParmas... params) {
        ScanForderParmas scanForderParmas = params[0];
        Context context = scanForderParmas.context;
        OnScanCallBack onScanCallBack = scanForderParmas.onScanCallBack;
        if (onScanCallBack == null) {
            throw new NullPointerException("callBack为空！");
        }
        if (context == null) {
            onScanCallBack.onError("暂无图片");
        }
        List<ImageItem> results = new ArrayList<>();
//
        Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String where = MediaStore.Images.Media.SIZE + " > " + SelectorSettings.mMinImageSize;
        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";
        Cursor cursor = context.getContentResolver().query(contentUri, projections, null, null, sortOrder);
        ImageItem item = null;
        if (cursor == null) {
            onScanCallBack.onError("暂无图片");
        } else if (cursor.moveToFirst()) {
            FolderItem allImagesFolderItem = null;
            int pathCol = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int nameCol = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
            int DateCol = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
            do {
                String path = cursor.getString(pathCol);
                String name = cursor.getString(nameCol);
                long dateTime = cursor.getLong(DateCol);
                item = new ImageItem(name, path, dateTime);
                // if FolderListContent is still empty, add "All Images" option
                if (FolderListContent.FOLDERS.size() == 0) {
                    // add folder for all image
                    FolderListContent.selectedFolderIndex = 0;
                    // use first image's path as cover image path
                    allImagesFolderItem = new FolderItem(context.getString(R.string.selector_folder_all), "", path);
                    FolderListContent.addItem(allImagesFolderItem);
                    // show camera icon ?
                    if (SelectorSettings.isShowCamera) {
                        results.add(ImageListContent.cameraItem);
                        allImagesFolderItem.addImageItem(ImageListContent.cameraItem);
                    }
                }
                results.add(item);
                allImagesFolderItem.addImageItem(item);
                String folderPath = new File(path).getParentFile().getAbsolutePath();
                FolderItem folderItem = FolderListContent.getItem(folderPath);
                if (folderItem == null) {
                    folderItem = new FolderItem(StringUtils.getLastPathSegment(folderPath), folderPath, path);
                    FolderListContent.addItem(folderItem);
                }
                folderItem.addImageItem(item);
            } while (cursor.moveToNext());
            cursor.close();
        }
        if (results != null){
            scanForderParmas.imageItems = results;
        }
        return scanForderParmas;
    }


    @Override
    protected void onPostExecute(ScanForderParmas parmas) {
        super.onPostExecute(parmas);
        if (parmas != null){
            if (parmas.imageItems != null && parmas.imageItems.size() > 0){
                for (ImageItem item :parmas.imageItems) {
                    parmas.onScanCallBack.onSuccess(item);
                }
            }else {
                parmas.onScanCallBack.onError("暂无图片");
            }
        }
    }

    public interface OnScanCallBack{
        void onSuccess(ImageItem imageItem);
        void onError(String errorMsg);
    }

}
