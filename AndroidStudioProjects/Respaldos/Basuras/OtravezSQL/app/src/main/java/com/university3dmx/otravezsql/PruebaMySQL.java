package com.university3dmx.otravezsql;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

/**
 * Created by Admin on 20/04/2015.
 */
public class PruebaMySQL extends Activity{

    public List<Datos> datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }

    public void onClick(View view){
        Log.i("Entrar", "Si entra");
        datos = DBDatos.getDatos();
    }
}
