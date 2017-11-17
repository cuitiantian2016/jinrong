package com.honglu.future.widget.photo;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class HackyViewPager extends ViewPager {

	private boolean mIsLocked;
	
    public HackyViewPager(Context context) {
        super(context);
		mIsLocked = false;
    }

    public HackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
		mIsLocked = false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
    	if (!mIsLocked) {
	        try {
	            return super.onInterceptTouchEvent(ev);
	        } catch (IllegalArgumentException e) {
	            e.printStackTrace();
	            return false;
	        }
    	}
    	return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mIsLocked) {
            return super.onTouchEvent(event);
        }
        return false;
    }
    
	public void toggleLock() {
		mIsLocked = !mIsLocked;
	}

	public void setLocked(boolean isLocked) {
		this.mIsLocked = isLocked;
	}

	public boolean isLocked() {
		return mIsLocked;
	}
	
}
