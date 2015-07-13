package com.university3dmx.responsehandlergetpost;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

/**
 * Created by Admin on 22/04/2015.
 */
public class LoginActivity extends Activity implements IJSONResponseHandler {

    private ProgressDialog dialog;
    private EditText textUser;
    private EditText textPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textUser = (EditText) findViewById(R.id.editText);
        textPass = (EditText) findViewById(R.id.editText2);
    }

    @Override
    public void onResponse(JSONObject jsonResponse) throws Exception {
        JSONObject user;
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        dialog.cancel();
        textUser.setText("");
        textPass.setText("");
        Log.i("jsonResponse", jsonResponse.toString());
        if( jsonResponse.has("usuario")){
            user = jsonResponse.getJSONObject("usuario");
            Log.i("JSON USER:", user.toString());
            startActivity(intent);
        }
    }

    @Override
    public void onRequest() {
        dialog = ProgressDialog.show(this, "Progreso", "Ingresando a la Aplicación.", true);
    }

    @Override
    public void onException(Exception e) {
        dialog.cancel();
    }

    public void loginClick( View targetView ){
        RPCHandler rpc;


        if(textUser.getText().length()>0 && textPass.getText().length()>0){
            rpc = new RPCHandler(this);
            rpc.execute(RPCHandler.OPERATION_LOGIN,
                    new BasicNameValuePair("user",textUser.getText().toString()),
                    new BasicNameValuePair("password",textPass.getText().toString()));
        }
        else
        {

        }
    }

}
