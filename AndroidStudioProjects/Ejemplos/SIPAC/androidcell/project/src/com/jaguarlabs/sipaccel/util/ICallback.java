package com.jaguarlabs.sipaccel.util;

import android.view.LayoutInflater;
import android.view.ViewGroup;

public interface ICallback {
	
	public void callback();
	public void time();
	public void callback( LayoutInflater inflater, ViewGroup container );
}
