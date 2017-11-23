package com.strategy.intecom.vtc.fixuser.view.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager {

	private boolean enabled;

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.enabled = true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		try {
			if (this.enabled) {
				return super.onTouchEvent(event);
			}
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {

		try {
			if (this.enabled) {
				return super.onInterceptTouchEvent(event);
			}
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		}

		return false;
	}

	public void setPagingEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
