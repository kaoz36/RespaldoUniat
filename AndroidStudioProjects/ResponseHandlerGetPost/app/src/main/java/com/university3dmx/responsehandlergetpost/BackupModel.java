package com.university3dmx.responsehandlergetpost;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin on 23/04/2015.
 */
public class BackupModel {

    protected ProgressDialog dialog;
    protected ExtendedFragmentActivity mActivity;
    private RPCHandler datadownlooader = null;
    private static BackupModel instance;
    private int count = 0;

    private BackupModel() throws Exception {
        if (instance != null) {
            throw new Exception("Error de Singleton");
        }
    }

    public static BackupModel getInstance() {
        if (instance == null) {
            try {
                instance = new BackupModel();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
        }
        return instance;
    }

    public void setContextActivity(ExtendedFragmentActivity pActivity) {
        mActivity = pActivity;
    }

    protected ProgressDialog getDialog() {
        if (dialog == null) {
            dialog = ProgressDialog.show(mActivity.getMContext(), "Progreso", "", true);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
        }
        return dialog;
    }

    protected void removeDialog() {
        getDialog().cancel();
        dialog = null;
    }

    public void cancelBackup() {
        getDialog().setOnCancelListener(null);
        removeDialog();
        mActivity.getMessageBuilder().setMessage(
                "El proceso de Actualizacion se ha suspendido, Para reanudarlo presionar el boton de actualizacion");
        mActivity.getMessageBuilder().create().show();
    }

    public void startBackup() {
        countMails();
        //backupUsuarios(0);
    }

    public void restoreBackup(String pTable) {

    }

    private void countMails(){
/*
        datadownlooader = new RPCHandler(new IAsyncJSONResponseHandler() {

            @Override
            public void continueProcessing() {
            }

            @Override
            public void onResponse(String jsonResponse) throws Exception {
                jsonResponse = jsonResponse.replace(" ", "");
                jsonResponse = jsonResponse.replace("\n", "");
                count = Integer.parseInt(jsonResponse);
            }

            @Override
            public void onRequest() {

            }

            @Override
            public void onException(Exception e) {

            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_COUNT_EMAIL);
    }

    private void backupUsuarios(final int index) {

        getDialog().setTitle("Paso 1/1");

        datadownlooader = new RPCHandler(new IAsyncJSONResponseHandler() {
            final class ProfesionesProcessor extends AsyncResponseProcessor {

                public ProfesionesProcessor(IAsyncJSONResponseHandler handler,
                                            String data) {
                    super(handler, data);
                }

                @Override
                protected Void doInBackground(Object... params) {
                    super.doInBackground();
                    SQLiteOpenHelper helper = OpenHelper.getInstance(mActivity.getMContext());
                    SQLiteDatabase dataBase = helper.getWritableDatabase();
                    try {
                        String sql = "INSERT INTO " + OpenHelper.TABLE_SIS_USER + " (email) VALUES " + data;
                        dataBase.beginTransaction();
                        dataBase.execSQL(sql);
                        dataBase.setTransactionSuccessful();
                        dataBase.endTransaction();
                    } catch (Exception e) {
                        Log.e("Data Backup", "Error al Respaldar Datos");
                    } finally {
                        dataBase.close();
                    }
                    return null;

                }
            }


            @Override
            public void onResponse(String jsonResponse) throws Exception {
                (new ProfesionesProcessor(this, jsonResponse)).execute();

            }

            @Override
            public void continueProcessing() {
                int t = index;
                if( index < count ){
                    t += 300;
                    backupUsuarios(t);
                }else{
                    //NEXT BACKUP;
                    removeDialog();
                }

            }

            @Override
            public void onRequest() {
                getDialog().setMessage("Respaldando Usuarios " + index + " / " + count);
            }

            @Override
            public void onException(Exception e) {
                cancelBackup();
            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_GET_ALL_USERS2,
                new BasicNameValuePair("index", index + ""));
                /*/
    }

}
