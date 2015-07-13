package com.university3dmx.basura;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Admin on 03/06/2015.
 */
public class ObjectStorage {

    private final String USER_PREFS = "objPrefs";
    private SharedPreferences preferences = null;
    private Context context;

    public ObjectStorage (Context context) {

        this.context = context;

        this.preferences = context.getSharedPreferences(USER_PREFS,
                Context.MODE_PRIVATE);
    }

    public void saveMyObject(MyObject myObject) {

        SharedPreferences.Editor editor = this.preferences.edit();


        editor.commit();
    }

}
