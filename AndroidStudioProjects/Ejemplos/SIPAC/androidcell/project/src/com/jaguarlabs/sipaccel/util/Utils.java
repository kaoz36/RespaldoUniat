package com.jaguarlabs.sipaccel.util;

import android.app.Activity;
import android.location.LocationManager;
import android.provider.Settings;

public class Utils {
	
	@SuppressWarnings("deprecation")
	public static boolean getGPSStatus(Activity act){
		String allowedLocationProviders = Settings.System.getString(act.getContentResolver(),
		Settings.System.LOCATION_PROVIDERS_ALLOWED);    
		if (allowedLocationProviders == null) {
			allowedLocationProviders = "";
		}    
		return allowedLocationProviders.contains(LocationManager.GPS_PROVIDER);
	}
}
