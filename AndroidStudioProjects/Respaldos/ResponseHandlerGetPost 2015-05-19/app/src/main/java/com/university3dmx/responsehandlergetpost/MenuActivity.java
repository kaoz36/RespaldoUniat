package com.university3dmx.responsehandlergetpost;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 22/04/2015.
 */
public class MenuActivity extends ExtendedFragmentActivity {

    SharedPreferences shared;
    TextView t;

    private List<UserVO> results;

    @Override
    protected void init() {
        // TODO Auto-generated method stub
        super.init();
        setContentView(R.layout.activity_menu);
        shared = this.getSharedPreferences("sipacUser", MODE_PRIVATE);
        t = (TextView) findViewById(R.id.textView);
        results = new ArrayList<>();

        t.setText(shared.getString("idUser", "") + " " + shared.getString("user", "") + " " +
                shared.getString("password", "") + " " + shared.getString("email", "") + " " +
                shared.getString("idPerson", ""));
        CommonDAO<UserVO> user = new CommonDAO<>(mContext, UserVO.class, OpenHelper.TABLE_SIS_USER);
        user.open();
        results = user.runQuery("Select * FROM " + OpenHelper.TABLE_SIS_USER);
        t.setText(results.size() + "");
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
