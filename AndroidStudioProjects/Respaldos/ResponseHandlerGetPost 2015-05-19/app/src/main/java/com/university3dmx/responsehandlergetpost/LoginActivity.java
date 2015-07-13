package com.university3dmx.responsehandlergetpost;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 * Created by Admin on 22/04/2015.
 */
public class LoginActivity extends ExtendedFragmentActivity implements IJSONResponseHandler {

    private ProgressDialog dialog;
    private EditText textUser;
    private EditText textPass;

    private RPCHandler datadownlooader = null;

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
        if (jsonResponse.has("usuario")) {
            user = jsonResponse.getJSONObject("usuario");
            Log.i("JSON USER", user.toString());
            DataModel.getInstance().setAppUser(new UserVO());
            DataModel.getInstance().getAppUser().setIdUser(user.getString("idUser"));
            DataModel.getInstance().getAppUser().setUser(user.getString("user"));
            DataModel.getInstance().getAppUser().setPassword(user.getString("password"));
            DataModel.getInstance().getAppUser().setEmail(user.getString("email"));
            DataModel.getInstance().getAppUser().setIdPerson(user.getString("idPerson"));

            savePreferences();
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
        Toast.makeText(getApplicationContext(), "Error al ingresar.", Toast.LENGTH_LONG).show();
    }

    public void loginClick(View targetView) {
        /*RPCHandler rpc;

        if (textUser.getText().length() > 0 && textPass.getText().length() > 0) {
            rpc = new RPCHandler(this);
            rpc.execute(RPCHandler.OPERATION_LOGIN,
                    new BasicNameValuePair("user", textUser.getText().toString()),
                    new BasicNameValuePair("password", textPass.getText().toString()));
        } else {
            Toast.makeText(getApplicationContext(), "Debes llenar todos los datos", Toast.LENGTH_LONG).show();
        }*/
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(intent);
    }

    public static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private void savePreferences(){
        SharedPreferences shared = this.getSharedPreferences("sipacUser", MODE_PRIVATE);
        SharedPreferences.Editor datos = shared.edit();

        datos.putString("idUser", DataModel.getInstance().getAppUser().getIdUser());
        datos.putString("user", DataModel.getInstance().getAppUser().getUser());
        datos.putString("password", DataModel.getInstance().getAppUser().getPassword());
        datos.putString("email", DataModel.getInstance().getAppUser().getEmail());
        datos.putString("idPerson", DataModel.getInstance().getAppUser().getIdPerson());
        datos.commit();

    }



}
