package com.uniat.eduscore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.university3dmx.eduscore.R;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    public void onClickLogin(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickSalir(View view){
        finish();
    }


}
