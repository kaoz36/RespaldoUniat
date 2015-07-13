package com.jaguarlabs.sipac.fragment;

import android.view.MotionEvent;

public interface IMenuFragment {
	boolean onActionMove(MotionEvent ev);
	boolean onActionDown(MotionEvent ev);
	boolean onActionUp(MotionEvent ev);
	void onActionClick();
	
}
