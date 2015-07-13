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
import com.uniat.eduscore.model.DataModel;
import com.uniat.eduscore.net.RPCHandler;
import com.uniat.eduscore.util.ExtendedFragmentActivity;
import com.uniat.eduscore.vo.CityVO;
import com.uniat.eduscore.vo.UserVO;
import com.university3dmx.eduscore.R;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

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
                if( shared.getString("user", "").equals(textUser.getText().toString()) &&
                        shared.getString("password", "").equals(textPass.getText().toString())   ){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getMContext(), getString(R.string.msj_error_log), Toast.LENGTH_LONG).show();
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
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        dialog.cancel();
        if (jsonResponse.has("usuario")) {
            user = jsonResponse.getJSONObject("usuario");
            DataModel.getInstance().setAppUser(new UserVO());
            DataModel.getInstance().getAppUser().setIdUser(user.getString("idUser"));
            DataModel.getInstance().getAppUser().setUser(user.getString("user"));
            DataModel.getInstance().getAppUser().setPassword(textPass.getText().toString());
            DataModel.getInstance().getAppUser().setIdPerson(user.getString("idPerson"));
            savePreferences();
            textUser.setText("");
            textPass.setText("");
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in_login, R.anim.fade_out_login);
        }else if( jsonResponse.has("error")){
            Toast.makeText(getMContext(), getString(R.string.msj_error_sesion), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequest() {
        dialog = ProgressDialog.show(this, "Progreso", "Ingsresando a la Aplicación.", true);
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

    private List<UserVO> getUsuariosList(){
        List<UserVO> userVO;
        CommonDAO<UserVO> userDAO = new CommonDAO<>(getMContext(), UserVO.class, EduscoreOpenHelper.TABLE_DAT_CITY);
        userDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_SIS_USER;
        userVO = userDAO.runQuery( sql );
        return userVO;
    }
}
