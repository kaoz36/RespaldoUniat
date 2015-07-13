package com.university3dmx.responsehandlergetpost;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;

/**
 * Created by Admin on 23/04/2015.
 */
public class ExtendedActivity extends FragmentActivity implements IStateNotifier, IContextGetter  {

    protected Context mContext;
    private AlertDialog.Builder builder;

    protected Boolean wifiOn = false;

    private IntentFilter filter;
    private WifiDetector wifiDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        init();
    }

    public AlertDialog.Builder getMessageBuilder() {
        if (builder == null) {
            builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
        }
        return builder;
    }

    protected void init() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        filter = new IntentFilter();
        filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        wifiDetector = new WifiDetector(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(wifiDetector);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(wifiDetector, filter);
        initialWifiCheck();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    }

    private void initialWifiCheck() {
        try {
            ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (mWifi.isConnected()) {
                successHandler();
            } else {
                failureHandler();
            }
        } catch (Exception error) {
            Log.e("Sipac Common Activity", error.getMessage());
        }
    }

    @Override
    public void successHandler() {
        Log.i("Audio Activity", "Wifi On");
        wifiOn = true;
    }

    @Override
    public void failureHandler() {
        Log.i("Audio Activity", "Wifi Off");
        wifiOn = false;
    }

    public Context getMContext() {
        return mContext;
    }

}
