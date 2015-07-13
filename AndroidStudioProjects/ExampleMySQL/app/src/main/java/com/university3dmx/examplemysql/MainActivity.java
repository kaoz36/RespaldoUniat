package com.university3dmx.examplemysql;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;


public class MainActivity extends Activity {

    public List<Familia> familias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btnConectar = (Button) findViewById(R.id.btnConectar);
        final TextView txtMensaje = (TextView) findViewById(R.id.txtMensaje);

        btnConectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                familias = BDFamilia.getDatosFamilia();
                txtMensaje.setText(familias.get(1).getNombre());
            }
        });
    }


}
