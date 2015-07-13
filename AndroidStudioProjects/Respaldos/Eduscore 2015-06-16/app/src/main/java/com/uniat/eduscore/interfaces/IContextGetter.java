package com.uniat.eduscore.interfaces;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Admin on 23/04/2015.
 */
public interface IContextGetter {
    Context getMContext();

    AlertDialog.Builder getMessageBuilder();

    SharedPreferences getSharedPreferences(String string, int modePrivate);

}
