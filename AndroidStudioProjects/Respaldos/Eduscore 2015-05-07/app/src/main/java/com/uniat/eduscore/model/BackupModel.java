package com.uniat.eduscore.model;

import android.app.ProgressDialog;
import android.util.Log;

import com.uniat.eduscore.db.CommonDAO;
import com.uniat.eduscore.db.EduscoreOpenHelper;
import com.uniat.eduscore.interfaces.IAsyncJSONResponseHandler;
import com.uniat.eduscore.net.RPCHandler;
import com.uniat.eduscore.processor.AsyncObjectResponseProcessor;
import com.uniat.eduscore.processor.AsyncResponseProcessor;
import com.uniat.eduscore.util.ExtendedFragmentActivity;
import com.uniat.eduscore.vo.UserVO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 23/04/2015.
 */
public class BackupModel {

    protected ProgressDialog dialog;
    protected ExtendedFragmentActivity mActivity;
    private RPCHandler datadownlooader = null;
    private static BackupModel instance;

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
        backupUsuarios();
    }

    public void restoreBackup(String pTable) {

    }

    private void backupUsuarios() {
        getDialog().setTitle("Paso 1/1");
        datadownlooader = new RPCHandler(new IAsyncJSONResponseHandler() {
            final class ProfesionesProcessor extends AsyncResponseProcessor {

                public ProfesionesProcessor(IAsyncJSONResponseHandler handler,
                                            JSONArray data) {
                    super(handler, data);
                }

                @Override
                protected Void doInBackground(Object... params) {
                    super.doInBackground();
                    CommonDAO<UserVO> profesionesDAO = new CommonDAO<UserVO>(
                            mActivity.getMContext(), UserVO.class,
                            EduscoreOpenHelper.TABLE_SIS_USERS);
                    HashMap<String, String> itemMap;


                    try {
                        profesionesDAO.open();
                        profesionesDAO.clearTable(EduscoreOpenHelper.CREATE_SIS_USERS);
                        profesionesDAO.getDatabase().beginTransaction();
                        for (int i = 0; i < data.length(); i++) {
                            itemMap = new HashMap<String, String>();
                            itemMap.put("idUser", data.getJSONObject(i)
                                    .getString("idUser"));
                            itemMap.put("user", data.getJSONObject(i)
                                    .getString("user"));
                            itemMap.put("password", data.getJSONObject(i)
                                    .getString("password"));
                            itemMap.put("email", data.getJSONObject(i)
                                    .getString("email"));
                            itemMap.put("idPerson", data.getJSONObject(i)
                                    .getString("idPerson"));
                            profesionesDAO.createItem(itemMap);
                        }
                        profesionesDAO.getDatabase().setTransactionSuccessful();
                        profesionesDAO.getDatabase().endTransaction();
                    } catch (Exception e) {
                        Log.e("Data Backup", "Error al Respaldar Datos");
                    } finally {
                        profesionesDAO.close();
                        removeDialog();
                    }
                    return null;

                }
            }

            @Override
            public void onResponse(JSONObject jsonResponse) throws Exception {
                JSONArray result;
                try {
                    if (jsonResponse.has("error")) {
                        mActivity.getMessageBuilder().setMessage(
                                jsonResponse.get("error").toString());
                        mActivity.getMessageBuilder().create().show();
                    } else {
                        if (jsonResponse.has("result")) {
                            result = jsonResponse.getJSONArray("result");
                            (new ProfesionesProcessor(this, result)).execute();
                        }

                    }
                } catch (Exception e) {
                    Log.e("Data Backup", "Error al Respaldar Datos");
                }
            }

            @Override
            public void continueProcessing() {
                //NEXT BACKUP;
            }

            @Override
            public void onRequest() {
                getDialog().setMessage("Respaldando Usuarios");
            }

            @Override
            public void onException(Exception e) {
                cancelBackup();
            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_GET_ALL_USERS);
    }

    /////////////////CLASES////////////////////////////

    class DataProcessor extends AsyncObjectResponseProcessor {

        private String[] fields;
        private String rawField;
        private ArrayList<String> dataArray;
        private Boolean error = false;

        public DataProcessor(IAsyncJSONResponseHandler handler) {
            super(handler);
            fields = new String[0];
            dataArray = new ArrayList<>();
            rawField = "";
        }

        public void addItems(JSONObject data) {
            String[] auxArray;
            try {
                auxArray = (data.getString("data")).split("\\|");
                if (fields.length == 0) {
                    fields = (auxArray[0]).split(",");
                    rawField = (auxArray[0]).replace("\"", "");
                }

                if (auxArray.length > 1) {
                    for (int i = 1; i < auxArray.length; i++) {
                        dataArray.add(auxArray[i]);
                    }
                }
            } catch (Exception e) {
                Log.e("Data Backuo", "Error al respaldar datos");
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            if (!error) {
                handler.continueProcessing();
            } else {
                getDialog().setOnCancelListener(null);
                removeDialog();
                mActivity.getMessageBuilder().setMessage(
                        "El proceso de Actualizacion ha terminado inesperadamente");
                mActivity.getMessageBuilder().create().show();
            }
        }

        protected void saveInDB(CommonDAO<?> commonDAO) {
            error = false;
            String itemArray;
            String sqlQuery;
            try {
                commonDAO.open();
                commonDAO.getDatabase().beginTransaction();
                while (dataArray.size() > 0) {
                    itemArray = (dataArray.remove(0));
                    sqlQuery = "INSERT INTO " + commonDAO.getTableId() + " (" + rawField.substring(0, rawField.length() - 1) + ") VALUES (" + itemArray.substring(0, itemArray.length() - 1) + ")";
                    commonDAO.runQueryNoReturn(sqlQuery);
                }
                commonDAO.getDatabase().setTransactionSuccessful();
                commonDAO.getDatabase().endTransaction();
            } catch (Exception e) {
                Log.e("Data Backup", "Error al Respaldar Datos");
                error = true;
            } finally {
                commonDAO.close();

            }
        }

        @Override
        protected Void doInBackground(Object... params) {
            super.doInBackground();
            CommonDAO<UserVO> userDAO = new CommonDAO<UserVO>(
                    mActivity.getMContext(), UserVO.class,
                    EduscoreOpenHelper.TABLE_SIS_USERS);
            saveInDB(userDAO);
            return null;
        }
    }

    class UserProcesor extends DataProcessor {

        public UserProcesor(IAsyncJSONResponseHandler handler) {
            super(handler);
        }

        @Override
        protected Void doInBackground(Object... params) {
            CommonDAO<UserVO> userDAO = new CommonDAO<UserVO>(
                    mActivity.getMContext(), UserVO.class,
                    EduscoreOpenHelper.TABLE_SIS_USERS);
            saveInDB(userDAO);
            return null;
        }
    }

}
