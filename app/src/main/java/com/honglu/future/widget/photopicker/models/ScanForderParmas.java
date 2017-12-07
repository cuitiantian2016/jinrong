package com.honglu.future.widget.photopicker.models;

import android.content.Context;


import com.honglu.future.widget.photopicker.utilities.ScanForderAsyncTask;

import java.util.List;

public class ScanForderParmas {
    public Context context;
    public ScanForderAsyncTask.OnScanCallBack onScanCallBack;
    public List<ImageItem> imageItems;

    public ScanForderParmas(Context context, ScanForderAsyncTask.OnScanCallBack onScanCallBack){
        this.context = context;
        this.onScanCallBack = onScanCallBack;
    }

}
