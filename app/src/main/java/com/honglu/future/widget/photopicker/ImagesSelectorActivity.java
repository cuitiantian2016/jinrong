package com.honglu.future.widget.photopicker;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.honglu.future.R;
import com.honglu.future.util.ToastUtil;
import com.honglu.future.widget.photopicker.models.FolderItem;
import com.honglu.future.widget.photopicker.models.FolderListContent;
import com.honglu.future.widget.photopicker.models.ImageItem;
import com.honglu.future.widget.photopicker.models.ImageListContent;
import com.honglu.future.widget.photopicker.models.ScanForderParmas;
import com.honglu.future.widget.photopicker.utilities.FileUtils;
import com.honglu.future.widget.photopicker.utilities.OnImageItemClickListener;
import com.honglu.future.widget.photopicker.utilities.ScanForderAsyncTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

public class ImagesSelectorActivity extends Activity
        implements OnImageRecyclerViewInteractionListener, OnFolderRecyclerViewInteractionListener, View.OnClickListener {

    public final static String EXTRA_CURRENT_ITEM = "current_item";
    public final static String EXTRA_PHOTOS = "photos";
    public final static String EXTRA_SHOW_DELETE = "show_delete";
    public final static String KEY_SELECTED_PHOTOS = "SELECTED_PHOTOS";


    private static final String TAG = "ImageSelector";
    public static final int MY_PERMISSIONS_REQUEST_STORAGE_CODE = 197;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA_CODE = 341;
    public final static int PHOTO_PREVIEW_CODE = 2;
    private int mColumnCount = 3;
    //    private ImageView mButtonBack;
    private Button mButtonConfirm;
    private RecyclerView recyclerView;
    private View mPopupAnchorView;
    private TextView mFolderSelectButton;
    private FolderPopupWindow mFolderPopupWindow;
    private String currentFolderPath;
    private File mTempImageFile;
    private static final int CAMERA_REQUEST_CODE = 694;
    private ImageRecyclerViewAdapter mImageRecyclerViewAdapter;
    private Button mPreviewButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_selector);
        // get parameters from bundle
        Intent intent = getIntent();
        SelectorSettings.mMaxImageNumber = intent.getIntExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, SelectorSettings.mMaxImageNumber);
        SelectorSettings.isShowCamera = intent.getBooleanExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, SelectorSettings.isShowCamera);
        SelectorSettings.mMinImageSize = intent.getIntExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, SelectorSettings.mMinImageSize);

        ArrayList<String> selected = intent.getStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST);
        ImageListContent.SELECTED_IMAGES.clear();
        if (selected != null && selected.size() > 0) {
            ImageListContent.SELECTED_IMAGES.addAll(selected);
        }
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // initialize widgets in custom actionbar
//        mButtonBack = (ImageView) findViewById(R.id.selector_button_back);
//        mButtonBack.setOnClickListener(this);

        mButtonConfirm = (Button) findViewById(R.id.selector_button_confirm);
        mButtonConfirm.setOnClickListener(this);

        // initialize recyclerview
        View rview = findViewById(R.id.image_recycerview);
        // Set the adapter
        if (rview instanceof RecyclerView) {
            Context context = rview.getContext();
            recyclerView = (RecyclerView) rview;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            mImageRecyclerViewAdapter = new ImageRecyclerViewAdapter(ImageListContent.IMAGES, this);
            mImageRecyclerViewAdapter.setOnImageItemClickListener(new OnImageItemClickListener() {
                @Override
                public void onImageClick(int position) {
                    Intent intent = new Intent(ImagesSelectorActivity.this, PhotoPreviewActivity.class);
                    intent.putExtra(ImagesSelectorActivity.EXTRA_CURRENT_ITEM, position);
                    intent.putExtra(ImagesSelectorActivity.EXTRA_PHOTOS, ImageListContent.SELECTED_IMAGES);
                    intent.putExtra(ImagesSelectorActivity.EXTRA_SHOW_DELETE, true);
                    startActivityForResult(intent, PHOTO_PREVIEW_CODE);
                }
            });
            recyclerView.setAdapter(mImageRecyclerViewAdapter);

            VerticalRecyclerViewFastScroller fastScroller = (VerticalRecyclerViewFastScroller) findViewById(R.id.recyclerview_fast_scroller);
            // Connect the recycler to the scroller (to let the scroller scroll the list)
            fastScroller.setRecyclerView(recyclerView);
            // Connect the scroller to the recycler (to let the recycler scroll the scroller's handle)
            recyclerView.addOnScrollListener(fastScroller.getOnScrollListener());
        }

        // popup windows will be anchored to this view
        mPopupAnchorView = findViewById(R.id.selector_footer);
        mPreviewButton = (Button) findViewById(R.id.selector_image_preview_button);
        mPreviewButton.setEnabled(false);
        mPreviewButton.setOnClickListener(this);

        // initialize buttons in footer
        mFolderSelectButton = (TextView) findViewById(R.id.selector_image_folder_button);
        mFolderSelectButton.setText(R.string.selector_folder_all);
        mFolderSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                if (mFolderPopupWindow == null) {
                    mFolderPopupWindow = new FolderPopupWindow();
                    mFolderPopupWindow.initPopupWindow(ImagesSelectorActivity.this);
                }

                if (mFolderPopupWindow.isShowing()) {
                    mFolderPopupWindow.dismiss();
                } else {
                    mFolderPopupWindow.showAtLocation(mPopupAnchorView, Gravity.BOTTOM, 10, 150);
                }
            }
        });

        currentFolderPath = "";
        FolderListContent.clear();
        ImageListContent.clear();

        updateDoneButton();

        requestReadStorageRuntimePermission();
    }

    public void requestReadStorageRuntimePermission() {
        if (ContextCompat.checkSelfPermission(ImagesSelectorActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ImagesSelectorActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_STORAGE_CODE);
        } else {
            LoadFolderAndImages();
        }
    }

    public void requestCameraRuntimePermissions() {
        if (ContextCompat.checkSelfPermission(ImagesSelectorActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(ImagesSelectorActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ImagesSelectorActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA_CODE);
        } else {
            launchCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_STORAGE_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    LoadFolderAndImages();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(ImagesSelectorActivity.this, getString(R.string.selector_permission_error), Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_CAMERA_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    launchCamera();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(ImagesSelectorActivity.this, getString(R.string.selector_permission_error), Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    // this method is to load images and folders for all
    public void LoadFolderAndImages() {
        Log.d(TAG, "Load Folder And Images...");
        ScanForderParmas parmas = new ScanForderParmas(this, new ScanForderAsyncTask.OnScanCallBack() {
            @Override
            public void onSuccess(ImageItem imageItem) {
                ImageListContent.addItem(imageItem);
                recyclerView.getAdapter().notifyItemChanged(ImageListContent.IMAGES.size() - 1);
            }

            @Override
            public void onError(String errorMsg) {
                ToastUtil.show(errorMsg);
            }
        });
        new ScanForderAsyncTask().execute(parmas);
    }

    public void updateDoneButton() {
        if (ImageListContent.SELECTED_IMAGES.size() == 0) {
            mPreviewButton.setEnabled(false);
            mButtonConfirm.setEnabled(false);
            mButtonConfirm.setText(getResources().getString(R.string.selector_done));
            mPreviewButton.setText("预览");
        } else {
            mButtonConfirm.setEnabled(true);
            String caption = getResources().getString(R.string.selector_action_done, ImageListContent.SELECTED_IMAGES.size(), SelectorSettings.mMaxImageNumber);
            mButtonConfirm.setText(caption);
            String pre = getResources().getString(R.string.preview_str, ImageListContent.SELECTED_IMAGES.size(), SelectorSettings.mMaxImageNumber);
            mPreviewButton.setEnabled(true);
            mPreviewButton.setText(pre);
        }


    }

    public void OnFolderChange() {
        mFolderPopupWindow.dismiss();

        FolderItem folder = FolderListContent.getSelectedFolder();
        if (!TextUtils.equals(folder.path, this.currentFolderPath)) {
            this.currentFolderPath = folder.path;
            mFolderSelectButton.setText(folder.name);

            ImageListContent.IMAGES.clear();
            ImageListContent.IMAGES.addAll(folder.mImages);
            recyclerView.getAdapter().notifyDataSetChanged();
        } else {
            Log.d(TAG, "OnFolderChange: " + "Same folder selected, skip loading.");
        }
    }


    @Override
    public void onFolderItemInteraction(FolderItem item) {
        // dismiss popup, and update image list if necessary
        OnFolderChange();
    }

    @Override
    public void onImageItemInteraction(ImageItem item) {
        if (ImageListContent.bReachMaxNumber) {
            String hint = getResources().getString(R.string.selector_reach_max_image_hint, SelectorSettings.mMaxImageNumber);
            ToastUtil.show(hint);
            ImageListContent.bReachMaxNumber = false;
        }

        if ((item != null) && item.isCamera()) {
            requestCameraRuntimePermissions();
        }

        updateDoneButton();
    }


    public void launchCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // set the output file of camera
            try {
                mTempImageFile = FileUtils.createTmpFile(this);
            } catch (IOException e) {
                Log.e(TAG, "launchCamera: ", e);
            }
            if (mTempImageFile != null && mTempImageFile.exists()) {
                Uri uri;
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
                    uri = Uri.fromFile(mTempImageFile);
                }else{
                    ContentValues contentValues = new ContentValues(1);
                    contentValues.put(MediaStore.Images.Media.DATA, mTempImageFile.getAbsolutePath());
                    uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
                }
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
            } else {
                Toast.makeText(this, R.string.camera_temp_file_error, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // after capturing image, return the image path as selected result
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (mTempImageFile != null) {
                    // notify system
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(mTempImageFile)));

                    Intent resultIntent = new Intent();
                    ImageListContent.clear();
                    ImageListContent.SELECTED_IMAGES.add(mTempImageFile.getAbsolutePath());
                    resultIntent.putStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS, ImageListContent.SELECTED_IMAGES);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            } else {
                // if user click cancel, delete the temp file
                while (mTempImageFile != null && mTempImageFile.exists()) {
                    boolean success = mTempImageFile.delete();
                    if (success) {
                        mTempImageFile = null;
                    }
                }
            }
        } else if (requestCode == PHOTO_PREVIEW_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    List<String> list = data.getStringArrayListExtra(ImagesSelectorActivity.KEY_SELECTED_PHOTOS);
                    ImageListContent.SELECTED_IMAGES.clear();
                    ImageListContent.SELECTED_IMAGES.addAll(list);
                    mImageRecyclerViewAdapter.notifyDataSetChanged();
                    onImageItemInteraction(null);
                } else {
                    ImageListContent.SELECTED_IMAGES.clear();
                    mImageRecyclerViewAdapter.notifyDataSetChanged();
                    onImageItemInteraction(null);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        finish();
    }


    @Override
    public void onClick(View v) {
        if (v == mButtonConfirm) {
            Intent data = new Intent();
            data.putStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS, ImageListContent.SELECTED_IMAGES);
            setResult(Activity.RESULT_OK, data);
            finish();
        }else if(v == mPreviewButton){
            if (ImageListContent.SELECTED_IMAGES.size() > 0){
                Intent intent = new Intent(ImagesSelectorActivity.this, PhotoPreviewActivity.class);
                intent.putExtra(ImagesSelectorActivity.EXTRA_CURRENT_ITEM, 0);
                intent.putExtra(ImagesSelectorActivity.EXTRA_PHOTOS, ImageListContent.SELECTED_IMAGES);
                intent.putExtra(ImagesSelectorActivity.EXTRA_SHOW_DELETE, true);
                startActivityForResult(intent, PHOTO_PREVIEW_CODE);
            }
        }
    }
}
