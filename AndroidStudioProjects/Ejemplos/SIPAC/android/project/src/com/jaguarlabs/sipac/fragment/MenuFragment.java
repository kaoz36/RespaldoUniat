package com.jaguarlabs.sipac.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MenuFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = null;
		view = inflater.inflate(com.jaguarlabs.sipac.R.layout.fragment_left_menu, container,true);
		return view;
	}
	
	
	
	

}
