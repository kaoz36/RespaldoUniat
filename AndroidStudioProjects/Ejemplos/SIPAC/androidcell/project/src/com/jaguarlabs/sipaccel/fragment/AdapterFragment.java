package com.jaguarlabs.sipaccel.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AdapterFragment extends FragmentPagerAdapter{

	public AdapterFragment(FragmentManager fm) {
		super(fm);
		
	}

	@Override
	public Fragment getItem(int arg0) {
		return null;
	}

	@Override
	public int getCount() {
		return 4;
	}

	
	
}
