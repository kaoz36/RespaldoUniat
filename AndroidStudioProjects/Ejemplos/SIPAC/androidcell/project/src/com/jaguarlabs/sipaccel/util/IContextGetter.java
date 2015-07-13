package com.jaguarlabs.sipaccel.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;

public interface IContextGetter {
	 Context getMContext();
	 AlertDialog.Builder getMessageBuilder();
	SharedPreferences getSharedPreferences(String string, int modePrivate);
}
