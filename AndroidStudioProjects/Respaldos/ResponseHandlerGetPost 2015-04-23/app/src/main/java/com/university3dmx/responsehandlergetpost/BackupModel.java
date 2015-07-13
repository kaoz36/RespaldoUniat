package com.university3dmx.responsehandlergetpost;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 23/04/2015.
 */
public class BackupModel {

    protected ProgressDialog dialog;
    protected ExtendedActivity mActivity;
    protected DataProcessor dataProcessor = null;
    private RPCHandler datadownlooader = null;
    private long total;
    private String currentTable;
    private Boolean cancelledBackup;
    private DialogInterface.OnCancelListener cancelBackupListener;

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

    public void setContextActivity(ExtendedActivity pActivity) {
        mActivity = pActivity;
    }

    protected ProgressDialog getDialog() {
        if (dialog == null) {
            dialog = ProgressDialog.show(mActivity.getApplicationContext(), "Progreso", "", true);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
        }
        return dialog;
    }

    protected void removeDialog() {
        getDialog().cancel();
        dialog = null;
    }

    private DialogInterface.OnCancelListener getBackupListener() {
        if (cancelBackupListener == null) {
            cancelBackupListener = new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity.getMContext());
                    builder.setMessage("Desea interrumpir la actualizacion ?").setCancelable(false);
                    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelledBackup = true;
                            cancelBackup();
                            if (dataProcessor != null) {
                                dataProcessor.cancel(true);
                            }

                            if (datadownlooader != null) {
                                datadownlooader.cancel(true);
                            }
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeDialog();
                            getDialog().setOnCancelListener(getBackupListener());
                            getDialog().setMessage("Reanudando Actualizacion");
                        }
                    });

                    builder.create().show();
                    return;
                }
            };
        }
        return cancelBackupListener;
    }

    public void cancelBackup() {
        getDialog().setOnCancelListener(null);
        removeDialog();
        mActivity.getMessageBuilder().setMessage(
                "El proceso de Actualizacion se ha suspendido, Para reanudarlo presionar el boton de actualizacion");
        mActivity.getMessageBuilder().create().show();
    }

    public void startBackup() {

        SharedPreferences shared = mActivity.getSharedPreferences("sipacPrefs", Context.MODE_PRIVATE);
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity.getMContext());

        cancelledBackup = false;
        total = shared.getLong("totalRespaldo", 0);
        currentTable = shared.getString("tablaRespaldo", "");

        getDialog().setTitle("Descarga de Informaci�n");
        getDialog().setMessage("Comenzando descarga....");
        getDialog().setOnCancelListener(getBackupListener());

        if (currentTable.length() > 0) {

            builder.setMessage("Existe un proceso de actualizaci�n anterior .Desea continuar este proceso?").setCancelable(false);
            builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    restoreBackup(currentTable);
                }
            });
            builder.setNegativeButton("Desde el inicio", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    clearDownloadState();
                    startBackup();
                }
            });

            builder.create().show();
            return;

        }
        backupUsuarios();
    }

    public void restoreBackup(String pTable) {

    }

    private void backupUsuarios() {
        getDialog().setTitle("Paso 1/1");
        currentTable = OpenHelper.TABLE_SIS_USER;
        saveDownLoadState();
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
                            OpenHelper.TABLE_SIS_USER);
                    HashMap<String, Object> itemMap;


                    try {
                        profesionesDAO.open();
                        profesionesDAO.clearTable(OpenHelper.CREATE_SIS_USER);
                        profesionesDAO.getDatabase().beginTransaction();
                        for (int i = 0; i < data.length(); i++) {
                            itemMap = new HashMap<String, Object>();
                            itemMap.put("idUser", data.getJSONObject(i)
                                    .getString("idUser"));
                            itemMap.put("user", data.getJSONObject(i)
                                    .getDouble("user"));
                            itemMap.put("password", data.getJSONObject(i)
                                    .getDouble("password"));
                            itemMap.put("email", data.getJSONObject(i)
                                    .getDouble("email"));
                            itemMap.put("idPerson", data.getJSONObject(i)
                                    .getDouble("idPerson"));
                            profesionesDAO.createItem(itemMap);
                        }
                        profesionesDAO.getDatabase().setTransactionSuccessful();
                        profesionesDAO.getDatabase().endTransaction();
                    } catch (Exception e) {
                        Log.e("Data Backup", "Error al Respaldar Datos");
                    } finally {
                        profesionesDAO.close();
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
                if (cancelledBackup) return;
                //NEXT BACKUP;
            }

            @Override
            public void onRequest() {
                getDialog().setMessage("Respaldando Profesiones de Cotizacion");
            }

            @Override
            public void onException(Exception e) {
                cancelBackup();
            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_GET_ALL_USERS);
    }

    private void saveDownLoadState() {
        SharedPreferences shared = mActivity.getSharedPreferences("sipacPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor datos = shared.edit();
        datos.putString("tablaRespaldo", currentTable).putLong("totalRespaldo", total).commit();
    }

    public void clearDownloadState() {
        SharedPreferences shared = mActivity.getSharedPreferences("sipacPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor datos = shared.edit();
        datos.remove("tablaRespaldo").remove("totalRespaldo").commit();
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
                clearDownloadState();
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
                    OpenHelper.TABLE_SIS_USER);
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
                    OpenHelper.TABLE_SIS_USER);
            saveInDB(userDAO);
            return null;
        }
    }

}
