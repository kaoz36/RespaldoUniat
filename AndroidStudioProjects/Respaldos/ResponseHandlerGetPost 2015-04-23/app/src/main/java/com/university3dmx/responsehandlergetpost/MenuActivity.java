package com.university3dmx.responsehandlergetpost;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Admin on 22/04/2015.
 */
public class MenuActivity extends ExtendedActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void onClickGuardar(View view){
        BackupModel.getInstance().setContextActivity(this);

        if (!wifiOn) {
            getMessageBuilder().setMessage("Es necesario tener una conexion WIFI Activa para reaizar este proceso");
            getMessageBuilder().create().show();
            return;
        }

        BackupModel.getInstance().startBackup();
    }

}
