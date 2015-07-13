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
public class LoginActivity extends ExtendedFragmentActivity{

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



    public void loginClick(View targetView) {
        Toast.makeText(getMContext(), sha256("eduSc0r3d2014_score%.2" + sha256("Abcd1234")), Toast.LENGTH_LONG).show();
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



}
