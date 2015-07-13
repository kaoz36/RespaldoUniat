package com.jaguarlabs.sipaccel.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

public class WifiDetector extends BroadcastReceiver {

	private IStateNotifier handler;
	public WifiDetector(IStateNotifier pHandler){
		this.handler = pHandler;
	}
	
	@Override
	public void onReceive(Context pContext, Intent pIntent) {
		final String action = pIntent.getAction();
		if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
	        if (pIntent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)) {
	           this.handler.successHandler();
	        } else {
	           this.handler.failureHandler();
	        }
		}
	}

}
