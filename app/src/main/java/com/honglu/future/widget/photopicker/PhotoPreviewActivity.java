package com.honglu.future.widget.photopicker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.honglu.future.R;

import java.util.List;

public class PhotoPreviewActivity extends AppCompatActivity {

	private ImagePagerFragment pagerFragment;

	public final static String EXTRA_CURRENT_ITEM = "current_item";
	public final static String EXTRA_PHOTOS = "photos";
	public final static String EXTRA_SHOW_DELETE = "show_delete";

	private ActionBar actionBar;
	private boolean showDelete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_photo_pager);
		int currentItem = getIntent().getIntExtra(EXTRA_CURRENT_ITEM, 0);
		List<String> paths = getIntent().getStringArrayListExtra(EXTRA_PHOTOS);
		showDelete = getIntent().getBooleanExtra(EXTRA_SHOW_DELETE, true);
		pagerFragment = (ImagePagerFragment) getSupportFragmentManager().findFragmentById(R.id.photoPagerFragment);
		pagerFragment.setPhotos(paths, currentItem);
		actionBar = getSupportActionBar();
		updateActionBarTitle();
		pagerFragment.getViewPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				updateActionBarTitle();
			}

			@Override
			public void onPageSelected(int i) {

			}

			@Override
			public void onPageScrollStateChanged(int i) {

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (showDelete) {
			getMenuInflater().inflate(R.menu.menu_preview, menu);
		}
		return true;
	}

	@Override
	public void onBackPressed() {

		Intent intent = new Intent();
		intent.putExtra(ImagesSelectorActivity.KEY_SELECTED_PHOTOS, pagerFragment.getPaths());
		setResult(RESULT_OK, intent);
		finish();

		super.onBackPressed();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}

		if (item.getItemId() == R.id.delete) {
			final int index = pagerFragment.getCurrentItem();

			final String deletedPath = pagerFragment.getPaths().get(index);
			Toast snackbar = Toast.makeText(PhotoPreviewActivity.this, R.string.picker_deleted_a_photo,
					Toast.LENGTH_LONG);
			if (pagerFragment.getPaths().size() <= 1) {
					AlertDialog.Builder alert = new AlertDialog.Builder(PhotoPreviewActivity.this);
				alert.setMessage(R.string.picker_confirm_to_delete);
				alert.setPositiveButton(R.string.picker_yes, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog, final int which) {
						dialog.dismiss();
						setResult(RESULT_OK);
						finish();
					}
				});
				alert.setNegativeButton(R.string.picker_cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(final DialogInterface dialog, final int which) {
						dialog.dismiss();
					}
				});

				alert.show();

			} else {
				snackbar.show();
				pagerFragment.getPaths().remove(index);
				// pagerFragment.getViewPager().removeViewAt(index);
				pagerFragment.getViewPager().getAdapter().notifyDataSetChanged();
			}
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void updateActionBarTitle() {
		actionBar.setTitle(getString(R.string.picker_image_index, pagerFragment.getViewPager().getCurrentItem() + 1,
				pagerFragment.getPaths().size()));
	}

	@Override
	public Resources getResources() {
		return getResources(super.getResources());
	}
	public static Resources getResources(Resources resources) {
		try {
			Configuration config = new Configuration();
			config.setToDefaults();
			resources.updateConfiguration(config, resources.getDisplayMetrics());
		}catch (Exception e){
		}
		return resources;
	}
}
