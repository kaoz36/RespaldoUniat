package com.uniat.eduscore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.uniat.eduscore.db.CommonDAO;
import com.uniat.eduscore.db.EduscoreOpenHelper;
import com.uniat.eduscore.interfaces.IJSONResponseHandler;
import com.uniat.eduscore.interfaces.IStringResponseHandler;
import com.uniat.eduscore.model.DataModel;
import com.uniat.eduscore.net.RPCHandler;
import com.uniat.eduscore.net.RPCHandlerString;
import com.uniat.eduscore.util.ExtendedFragmentActivity;
import com.uniat.eduscore.vo.CityVO;
import com.uniat.eduscore.vo.UserVO;
import com.university3dmx.eduscore.R;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends ExtendedFragmentActivity implements IJSONResponseHandler{

    private ProgressDialog dialog;
    private EditText textUser;
    private EditText textPass;
    private SharedPreferences shared;

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.activity_login);
        shared  = this.getSharedPreferences("eduscoreUser", MODE_PRIVATE);
        textUser = (EditText) findViewById(R.id.editTextUser);
        textPass = (EditText) findViewById(R.id.editTextPass);
    }


    public void onClickLogin(View view){

        if(textUser.getText().length()>0 && textPass.getText().length()>0){
            if( wifiOn ){
                RPCHandler rpc;
                rpc = new RPCHandler(this);
                rpc.execute(RPCHandler.OPERATION_LOGIN,
                        new BasicNameValuePair("user",textUser.getText().toString()),
                        new BasicNameValuePair("password",textPass.getText().toString()));
            }else{
                if( loginOffline(textUser.getText().toString() , textPass.getText().toString() )   ){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getMContext(), getString(R.string.msj_error_sesion), Toast.LENGTH_LONG).show();
                }
            }
        }else {
            Toast.makeText(getApplicationContext(), getString(R.string.msj_llenar_datos), Toast.LENGTH_LONG).show();
        }
    }

    public void onClickSalir(View view){
        finish();
    }


    @Override
    public void onResponse(JSONObject jsonResponse) throws Exception {
        JSONObject user;

        dialog.cancel();
        if (jsonResponse.has("usuario")) {
            user = jsonResponse.getJSONObject("usuario");
            DataModel.getInstance().setAppUser(new UserVO());
            DataModel.getInstance().getAppUser().setIdUser(user.getString("idUser"));
            DataModel.getInstance().getAppUser().setUser(user.getString("user"));
            DataModel.getInstance().getAppUser().setPassword(textPass.getText().toString());
            DataModel.getInstance().getAppUser().setIdPerson(user.getString("idPerson"));
            savePreferences();

            RPCHandlerString rpcString = new RPCHandlerString(new IStringResponseHandler() {
                @Override
                public void onResponse(String stringResponse) throws Exception {
                    if( stringResponse.contains("5") || stringResponse.contains("7") ||
                            stringResponse.contains("8") || stringResponse.contains("16") ||
                            stringResponse.contains("21") ) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        textUser.setText("");
                        textPass.setText("");
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in_login, R.anim.fade_out_login);
                        dialog.cancel();
                    }else{
                        Toast.makeText(getMContext(), getString(R.string.msj_error_privilegios), Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    }
                }

                @Override
                public void onRequest() {
                    dialog = ProgressDialog.show(getMContext(), getString(R.string.text_ingresando_permisos), getString(R.string.text_ingresando), true);
                }

                @Override
                public void onException(Exception e) {
                    Toast.makeText(getMContext(), getString(R.string.msj_error_privilegios), Toast.LENGTH_LONG).show();
                    dialog.cancel();
                }
            });
            rpcString.execute(RPCHandler.OPERATION_GET_ID_CHARGE,
                    new BasicNameValuePair("idPerson", DataModel.getInstance().getAppUser().getIdPerson()));

        }else if( jsonResponse.has("error")){
            Toast.makeText(getMContext(), getString(R.string.msj_error_sesion), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequest() {
        dialog = ProgressDialog.show(this, getString(R.string.text_progress), getString(R.string.text_ingresando), true);
    }

    @Override
    public void onException(Exception e) {
        Toast.makeText(getMContext(), getString(R.string.msj_error_log), Toast.LENGTH_LONG).show();
        dialog.cancel();
    }

    private void savePreferences(){

        SharedPreferences.Editor datos = shared.edit();

        datos.putString("idUser", DataModel.getInstance().getAppUser().getIdUser());
        datos.putString("user", DataModel.getInstance().getAppUser().getUser());
        datos.putString("password", DataModel.getInstance().getAppUser().getPassword());
        datos.putString("idPerson", DataModel.getInstance().getAppUser().getIdPerson());
        datos.commit();
    }

    private boolean loginOffline(String user, String pass){
        List<UserVO> userVO;
        CommonDAO<UserVO> userDAO = new CommonDAO<>(getMContext(), UserVO.class, EduscoreOpenHelper.TABLE_SIS_USER);
        userDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_SIS_USER +
                " WHERE user = '" + user + "' AND password = '" + sha256("eduSc0r3d2014_score%.2" + sha256(pass)) + "'";
        userVO = userDAO.runQuery( sql );
        return userVO.size() > 0 ? true : false;

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
