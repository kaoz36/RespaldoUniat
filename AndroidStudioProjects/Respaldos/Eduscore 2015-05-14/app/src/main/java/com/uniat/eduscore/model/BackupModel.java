package com.uniat.eduscore.model;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import com.uniat.eduscore.db.CommonDAO;
import com.uniat.eduscore.db.EduscoreOpenHelper;
import com.uniat.eduscore.interfaces.IAsyncJSONResponseHandler;
import com.uniat.eduscore.net.RPCHandler;
import com.uniat.eduscore.processor.AsyncResponseProcessor;
import com.uniat.eduscore.util.ExtendedFragmentActivity;
import com.uniat.eduscore.vo.CampusVO;
import com.uniat.eduscore.vo.CareerTypeVO;
import com.uniat.eduscore.vo.CareerVO;
import com.uniat.eduscore.vo.CityVO;
import com.uniat.eduscore.vo.CountryVO;
import com.uniat.eduscore.vo.EmailVO;
import com.uniat.eduscore.vo.EventVO;
import com.uniat.eduscore.vo.MediaVO;
import com.uniat.eduscore.vo.OriginVO;
import com.uniat.eduscore.vo.RegionVO;
import com.uniat.eduscore.vo.UserVO;

import org.json.JSONArray;
import org.json.JSONObject;

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
        //backupUsuarios();
        backupCity();
    }

    public void restoreBackup(String pTable) {

    }

    private void backupUsuarios() {
        getDialog().setTitle("Paso 1/11");
        datadownlooader = new RPCHandler(new IAsyncJSONResponseHandler() {
            final class ProfesionesProcessor extends AsyncResponseProcessor {

                public ProfesionesProcessor(IAsyncJSONResponseHandler handler,
                                            JSONArray data) {
                    super(handler, data);
                }

                @Override
                protected Void doInBackground(Object... params) {
                    super.doInBackground();
                    CommonDAO<UserVO> userDAO = new CommonDAO<>(
                            mActivity.getMContext(), UserVO.class,
                            EduscoreOpenHelper.TABLE_SIS_USER);
                    HashMap<String, String> itemMap;


                    try {
                        userDAO.open();
                        userDAO.clearTable(EduscoreOpenHelper.CREATE_SIS_USER);
                        userDAO.getDatabase().beginTransaction();
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
                            userDAO.createItem(itemMap);
                        }
                        userDAO.getDatabase().setTransactionSuccessful();
                        userDAO.getDatabase().endTransaction();
                    } catch (Exception e) {
                        Log.e("Data Backup", "Error al Respaldar USUARIOS");
                        removeDialog();
                    } finally {
                        userDAO.close();
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
                    Log.e("Data Backup", "Error al Respaldar USUARIOS RESULT");
                }
            }

            @Override
            public void continueProcessing() {
                //NEXT BACKUP;
                Toast.makeText(mActivity.getMContext(), "Usuarios Respaldados.", Toast.LENGTH_LONG).show();
                backupCareers();
            }

            @Override
            public void onRequest() {
                getDialog().setMessage("Respaldando Usuarios.");
            }

            @Override
            public void onException(Exception e) {
                cancelBackup();
            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_GET_ALL_USERS);
    }

    private void backupCareers() {
        getDialog().setTitle("Paso 2/11");
        datadownlooader = new RPCHandler(new IAsyncJSONResponseHandler() {
            final class ProfesionesProcessor extends AsyncResponseProcessor {

                public ProfesionesProcessor(IAsyncJSONResponseHandler handler,
                                            JSONArray data) {
                    super(handler, data);
                }

                @Override
                protected Void doInBackground(Object... params) {
                    super.doInBackground();
                    CommonDAO<CareerVO> careerDAO = new CommonDAO<>(
                            mActivity.getMContext(), CareerVO.class,
                            EduscoreOpenHelper.TABLE_ESC_CAREER);
                    HashMap<String, String> itemMap;


                    try {
                        careerDAO.open();
                        careerDAO.clearTable(EduscoreOpenHelper.CREATE_ESC_CAREER);
                        careerDAO.getDatabase().beginTransaction();
                        for (int i = 0; i < data.length(); i++) {
                            itemMap = new HashMap<String, String>();
                            itemMap.put("idCareer", data.getJSONObject(i)
                                    .getString("idCareer"));
                            itemMap.put("name", data.getJSONObject(i)
                                    .getString("name"));
                            itemMap.put("idCareerType", data.getJSONObject(i)
                                    .getString("idCareerType"));
                            itemMap.put("prefix", data.getJSONObject(i)
                                    .getString("prefix"));
                            careerDAO.createItem(itemMap);
                        }
                        careerDAO.getDatabase().setTransactionSuccessful();
                        careerDAO.getDatabase().endTransaction();
                    } catch (Exception e) {
                        Log.e("Data Backup", "Error al Respaldar CARRERAS");
                        removeDialog();
                    } finally {
                        careerDAO.close();
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
                    Log.e("Data Backup", "Error al Respaldar CARERAS RERSULT");
                }
            }

            @Override
            public void continueProcessing() {
                //NEXT BACKUP;
                Toast.makeText(mActivity.getMContext(), "Carreras Respaldados.", Toast.LENGTH_LONG).show();
                backupCareersType();
            }

            @Override
            public void onRequest() {
                getDialog().setMessage("Respaldando Carreras.");
            }

            @Override
            public void onException(Exception e) {
                cancelBackup();
            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_GET_ALL_CAREERS);
    }

    private void backupCareersType() {
        getDialog().setTitle("Paso 3/11");
        datadownlooader = new RPCHandler(new IAsyncJSONResponseHandler() {
            final class ProfesionesProcessor extends AsyncResponseProcessor {

                public ProfesionesProcessor(IAsyncJSONResponseHandler handler,
                                            JSONArray data) {
                    super(handler, data);
                }

                @Override
                protected Void doInBackground(Object... params) {
                    super.doInBackground();
                    CommonDAO<CareerTypeVO> careerTypeDAO = new CommonDAO<>(
                            mActivity.getMContext(), CareerTypeVO.class,
                            EduscoreOpenHelper.TABLE_ESC_CAREERTYPE);
                    HashMap<String, String> itemMap;


                    try {
                        careerTypeDAO.open();
                        careerTypeDAO.clearTable(EduscoreOpenHelper.CREATE_ESC_CAREERTYPE);
                        careerTypeDAO.getDatabase().beginTransaction();
                        for (int i = 0; i < data.length(); i++) {
                            itemMap = new HashMap<String, String>();
                            itemMap.put("idCareerType", data.getJSONObject(i)
                                    .getString("idCareerType"));
                            itemMap.put("name", data.getJSONObject(i)
                                    .getString("name"));
                            itemMap.put("status", data.getJSONObject(i)
                                    .getString("status"));
                            careerTypeDAO.createItem(itemMap);
                        }
                        careerTypeDAO.getDatabase().setTransactionSuccessful();
                        careerTypeDAO.getDatabase().endTransaction();
                    } catch (Exception e) {
                        Log.e("Data Backup", "Error al Respaldar CARRERAS TIPO");
                        removeDialog();
                    } finally {
                        careerTypeDAO.close();
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
                    Log.e("Data Backup", "Error al Respaldar CARRERAS TIPO RESULT");
                }
            }

            @Override
            public void continueProcessing() {
                //NEXT BACKUP;
                Toast.makeText(mActivity.getMContext(), "Tipos Carreras Respaldados.", Toast.LENGTH_LONG).show();
                backupMedia();
            }

            @Override
            public void onRequest() {
                getDialog().setMessage("Respaldando Carreras.");
            }

            @Override
            public void onException(Exception e) {
                cancelBackup();
            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_GET_ALL_CAREERTYPES);
    }

    private void backupMedia() {
        getDialog().setTitle("Paso 4/11");
        datadownlooader = new RPCHandler(new IAsyncJSONResponseHandler() {
            final class ProfesionesProcessor extends AsyncResponseProcessor {

                public ProfesionesProcessor(IAsyncJSONResponseHandler handler,
                                            JSONArray data) {
                    super(handler, data);
                }

                @Override
                protected Void doInBackground(Object... params) {
                    super.doInBackground();
                    CommonDAO<MediaVO> mediaDAO = new CommonDAO<>(
                            mActivity.getMContext(), MediaVO.class,
                            EduscoreOpenHelper.TABLE_DAT_MEDIA);
                    HashMap<String, String> itemMap;


                    try {
                        mediaDAO.open();
                        mediaDAO.clearTable(EduscoreOpenHelper.CREATE_DAT_MEDIA);
                        mediaDAO.getDatabase().beginTransaction();
                        for (int i = 0; i < data.length(); i++) {
                            itemMap = new HashMap<String, String>();
                            itemMap.put("idMedia", data.getJSONObject(i)
                                    .getString("idMedia"));
                            itemMap.put("name", data.getJSONObject(i)
                                    .getString("name"));
                            itemMap.put("status", data.getJSONObject(i)
                                    .getString("status"));
                            itemMap.put("idCampus", data.getJSONObject(i)
                                    .getString("idCampus"));
                            mediaDAO.createItem(itemMap);
                        }
                        mediaDAO.getDatabase().setTransactionSuccessful();
                        mediaDAO.getDatabase().endTransaction();
                    } catch (Exception e) {
                        Log.e("Data Backup", "Error al Respaldar MEDIA");
                        removeDialog();
                    } finally {
                        mediaDAO.close();
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
                    Log.e("Data Backup", "Error al Respaldar MEDIA RESULT");
                }
            }

            @Override
            public void continueProcessing() {
                //NEXT BACKUP;
                Toast.makeText(mActivity.getMContext(), "Medios Respaldados.", Toast.LENGTH_LONG).show();
                backupCampus();
            }

            @Override
            public void onRequest() {
                getDialog().setMessage("Respaldando Medio de Informacion.");
            }

            @Override
            public void onException(Exception e) {
                cancelBackup();
            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_GET_ALL_MEDIAS);
    }

    private void backupCampus() {
        getDialog().setTitle("Paso 5/11");
        datadownlooader = new RPCHandler(new IAsyncJSONResponseHandler() {
            final class ProfesionesProcessor extends AsyncResponseProcessor {

                public ProfesionesProcessor(IAsyncJSONResponseHandler handler,
                                            JSONArray data) {
                    super(handler, data);
                }

                @Override
                protected Void doInBackground(Object... params) {
                    super.doInBackground();
                    CommonDAO<CampusVO> campusDAO = new CommonDAO<>(
                            mActivity.getMContext(), CampusVO.class,
                            EduscoreOpenHelper.TABLE_DAT_CAMPUS);
                    HashMap<String, String> itemMap;


                    try {
                        campusDAO.open();
                        campusDAO.clearTable(EduscoreOpenHelper.CREATE_DAT_CAMPUS);
                        campusDAO.getDatabase().beginTransaction();
                        for (int i = 0; i < data.length(); i++) {
                            itemMap = new HashMap<String, String>();
                            itemMap.put("idCampus", data.getJSONObject(i)
                                    .getString("idCampus"));
                            itemMap.put("name", data.getJSONObject(i)
                                    .getString("name"));
                            itemMap.put("address", data.getJSONObject(i)
                                    .getString("address"));
                            itemMap.put("status", data.getJSONObject(i)
                                    .getString("status"));
                            itemMap.put("idCountry", data.getJSONObject(i)
                                    .getString("idCountry"));
                            itemMap.put("idRegion", data.getJSONObject(i)
                                    .getString("idRegion"));
                            itemMap.put("idCity", data.getJSONObject(i)
                                    .getString("idCity"));
                            itemMap.put("nameAlt", data.getJSONObject(i)
                                    .getString("nameAlt"));
                            campusDAO.createItem(itemMap);
                        }
                        campusDAO.getDatabase().setTransactionSuccessful();
                        campusDAO.getDatabase().endTransaction();
                    } catch (Exception e) {
                        Log.e("Data Backup", "Error al Respaldar CAMPUS");
                        removeDialog();
                    } finally {
                        campusDAO.close();
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
                    Log.e("Data Backup", "Error al Respaldar CAMPUS RESULT");
                }
            }

            @Override
            public void continueProcessing() {
                //NEXT BACKUP;
                Toast.makeText(mActivity.getMContext(), "Campus Respaldados.", Toast.LENGTH_LONG).show();
                backupOrigin();
            }

            @Override
            public void onRequest() {
                getDialog().setMessage("Respaldando Campus.");
            }

            @Override
            public void onException(Exception e) {
                cancelBackup();
            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_GET_ALL_CAMPUS);
    }

    private void backupOrigin() {
        getDialog().setTitle("Paso 6/11");
        datadownlooader = new RPCHandler(new IAsyncJSONResponseHandler() {
            final class ProfesionesProcessor extends AsyncResponseProcessor {

                public ProfesionesProcessor(IAsyncJSONResponseHandler handler,
                                            JSONArray data) {
                    super(handler, data);
                }

                @Override
                protected Void doInBackground(Object... params) {
                    super.doInBackground();
                    CommonDAO<OriginVO> origenDAO = new CommonDAO<>(
                            mActivity.getMContext(), OriginVO.class,
                            EduscoreOpenHelper.TABLE_DAT_ORIGIN);
                    HashMap<String, String> itemMap;


                    try {
                        origenDAO.open();
                        origenDAO.clearTable(EduscoreOpenHelper.CREATE_DAT_ORIGIN);
                        origenDAO.getDatabase().beginTransaction();
                        for (int i = 0; i < data.length(); i++) {
                            itemMap = new HashMap<String, String>();
                            itemMap.put("idOrigin", data.getJSONObject(i)
                                    .getString("idOrigin"));
                            itemMap.put("name", data.getJSONObject(i)
                                    .getString("name"));
                            itemMap.put("type", data.getJSONObject(i)
                                    .getString("type"));
                            itemMap.put("status", data.getJSONObject(i)
                                    .getString("status"));
                            itemMap.put("idCampus", data.getJSONObject(i)
                                    .getString("idCampus"));

                            origenDAO.createItem(itemMap);
                        }
                        origenDAO.getDatabase().setTransactionSuccessful();
                        origenDAO.getDatabase().endTransaction();
                    } catch (Exception e) {
                        Log.e("Data Backup", "Error al Respaldar ORIGIN");
                        removeDialog();
                    } finally {
                        origenDAO.close();
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
                    Log.e("Data Backup", "Error al Respaldar ORIGEN RESULT");
                }
            }

            @Override
            public void continueProcessing() {
                //NEXT BACKUP;
                backupEvent();
                Toast.makeText(mActivity.getMContext(), "Origen Respaldados.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRequest() {
                getDialog().setMessage("Respaldando Lugar Procedencia.");
            }

            @Override
            public void onException(Exception e) {
                cancelBackup();
            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_GET_ALL_ORIGINS);
    }

    private void backupEvent() {
        getDialog().setTitle("Paso 7/11");
        datadownlooader = new RPCHandler(new IAsyncJSONResponseHandler() {
            final class ProfesionesProcessor extends AsyncResponseProcessor {

                public ProfesionesProcessor(IAsyncJSONResponseHandler handler,
                                            JSONArray data) {
                    super(handler, data);
                }

                @Override
                protected Void doInBackground(Object... params) {
                    super.doInBackground();
                    CommonDAO<EventVO> eventDAO = new CommonDAO<>(
                            mActivity.getMContext(), EventVO.class,
                            EduscoreOpenHelper.TABLE_DAT_EVENTS);
                    HashMap<String, String> itemMap;


                    try {
                        eventDAO.open();
                        eventDAO.clearTable(EduscoreOpenHelper.CREATE_DAT_EVENTS);
                        eventDAO.getDatabase().beginTransaction();
                        for (int i = 0; i < data.length(); i++) {
                            itemMap = new HashMap<String, String>();
                            itemMap.put("idEvent", data.getJSONObject(i)
                                    .getString("idEvent"));
                            itemMap.put("name", data.getJSONObject(i)
                                    .getString("name"));
                            itemMap.put("status", data.getJSONObject(i)
                                    .getString("status"));
                            itemMap.put("idCampus", data.getJSONObject(i)
                                    .getString("idCampus"));

                            eventDAO.createItem(itemMap);
                        }
                        eventDAO.getDatabase().setTransactionSuccessful();
                        eventDAO.getDatabase().endTransaction();
                    } catch (Exception e) {
                        Log.e("Data Backup", "Error al Respaldar EVENT");
                        removeDialog();
                    } finally {
                        eventDAO.close();
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
                    Log.e("Data Backup", "Error al Respaldar EVENT RESULT");
                }
            }

            @Override
            public void continueProcessing() {
                //NEXT BACKUP;
                Toast.makeText(mActivity.getMContext(), "Eventos Respaldados.", Toast.LENGTH_LONG).show();
                backupEmail();
            }

            @Override
            public void onRequest() {
                getDialog().setMessage("Respaldando Eventos.");
            }

            @Override
            public void onException(Exception e) {
                cancelBackup();
            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_GET_ALL_EVENTS);
    }

    private void backupEmail() {
        getDialog().setTitle("Paso 8/11");
        datadownlooader = new RPCHandler(new IAsyncJSONResponseHandler() {
            final class ProfesionesProcessor extends AsyncResponseProcessor {

                public ProfesionesProcessor(IAsyncJSONResponseHandler handler,
                                            JSONArray data) {
                    super(handler, data);
                }

                @Override
                protected Void doInBackground(Object... params) {
                    super.doInBackground();
                    CommonDAO<EmailVO> emailDAO = new CommonDAO<>(
                            mActivity.getMContext(), EmailVO.class,
                            EduscoreOpenHelper.TABLE_DAT_EMAIL);
                    HashMap<String, String> itemMap;


                    try {
                        emailDAO.open();
                        emailDAO.clearTable(EduscoreOpenHelper.CREATE_DAT_EMAIL);
                        emailDAO.getDatabase().beginTransaction();
                        for (int i = 0; i < data.length(); i++) {
                            itemMap = new HashMap<String, String>();
                            itemMap.put("idEmail", data.getJSONObject(i)
                                    .getString("idEmail"));
                            itemMap.put("idPerson", data.getJSONObject(i)
                                    .getString("idPerson"));
                            itemMap.put("email", data.getJSONObject(i)
                                    .getString("email"));

                            emailDAO.createItem(itemMap);
                        }
                        emailDAO.getDatabase().setTransactionSuccessful();
                        emailDAO.getDatabase().endTransaction();
                    } catch (Exception e) {
                        Log.e("Data Backup", "Error al Respaldar EMAIL");
                        removeDialog();
                    } finally {
                        emailDAO.close();
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
                    Log.e("Data Backup EMAIL", "Error al Respaldar EMAIL RESULT");
                }
            }

            @Override
            public void continueProcessing() {
                //NEXT BACKUP;
                Toast.makeText(mActivity.getMContext(), "Correos Respaldados.", Toast.LENGTH_LONG).show();
                backupCountry();
            }

            @Override
            public void onRequest() {
                getDialog().setMessage("Respaldando Correos.");
            }

            @Override
            public void onException(Exception e) {
                cancelBackup();
            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_GET_ALL_EMAILS);
    }

    private void backupCountry() {
        getDialog().setTitle("Paso 9/11");
        datadownlooader = new RPCHandler(new IAsyncJSONResponseHandler() {
            final class ProfesionesProcessor extends AsyncResponseProcessor {

                public ProfesionesProcessor(IAsyncJSONResponseHandler handler,
                                            JSONArray data) {
                    super(handler, data);
                }

                @Override
                protected Void doInBackground(Object... params) {
                    super.doInBackground();
                    CommonDAO<CountryVO> countryDAO = new CommonDAO<>(
                            mActivity.getMContext(), CountryVO.class,
                            EduscoreOpenHelper.TABLE_DAT_COUNTRY);
                    HashMap<String, String> itemMap;


                    try {
                        countryDAO.open();
                        countryDAO.clearTable(EduscoreOpenHelper.CREATE_DAT_COUNTRY);
                        countryDAO.getDatabase().beginTransaction();
                        for (int i = 0; i < data.length(); i++) {
                            itemMap = new HashMap<String, String>();
                            itemMap.put("idCountry", data.getJSONObject(i)
                                    .getString("idCountry"));
                            itemMap.put("name", data.getJSONObject(i)
                                    .getString("name"));

                            countryDAO.createItem(itemMap);
                        }
                        countryDAO.getDatabase().setTransactionSuccessful();
                        countryDAO.getDatabase().endTransaction();
                    } catch (Exception e) {
                        Log.e("Data Backup COUNTRY", "Error al Respaldar COUNTRY");
                        removeDialog();
                    } finally {
                        countryDAO.close();
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
                    Log.e("Data Backup", "Error al Respaldar Datos COUNTRY RESULT");
                }
            }

            @Override
            public void continueProcessing() {
                //NEXT BACKUP;
                Toast.makeText(mActivity.getMContext(), "Paises Respaldados.", Toast.LENGTH_LONG).show();
                backupRegion();
            }

            @Override
            public void onRequest() {
                getDialog().setMessage("Respaldando Paises.");
            }

            @Override
            public void onException(Exception e) {
                cancelBackup();
            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_GET_ALL_COUNTRYS);
    }

    private void backupRegion() {
        getDialog().setTitle("Paso 10/11");
        datadownlooader = new RPCHandler(new IAsyncJSONResponseHandler() {
            final class ProfesionesProcessor extends AsyncResponseProcessor {

                public ProfesionesProcessor(IAsyncJSONResponseHandler handler,
                                            JSONArray data) {
                    super(handler, data);
                }

                @Override
                protected Void doInBackground(Object... params) {
                    super.doInBackground();
                    CommonDAO<RegionVO> regionDAO = new CommonDAO<>(
                            mActivity.getMContext(), RegionVO.class,
                            EduscoreOpenHelper.TABLE_DAT_REGION);
                    HashMap<String, String> itemMap;


                    try {
                        regionDAO.open();
                        regionDAO.clearTable(EduscoreOpenHelper.CREATE_DAT_REGION);
                        regionDAO.getDatabase().beginTransaction();
                        for (int i = 0; i < data.length(); i++) {
                            itemMap = new HashMap<String, String>();
                            itemMap.put("idRegion", data.getJSONObject(i)
                                    .getString("idRegion"));
                            itemMap.put("idCountry", data.getJSONObject(i)
                                    .getString("idCountry"));
                            itemMap.put("name", data.getJSONObject(i)
                                    .getString("name"));

                            regionDAO.createItem(itemMap);
                        }
                        regionDAO.getDatabase().setTransactionSuccessful();
                        regionDAO.getDatabase().endTransaction();
                    } catch (Exception e) {
                        Log.e("Data Backup", "Error al Respaldar REGION");
                        removeDialog();
                    } finally {
                        regionDAO.close();
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
                    Log.e("Data Backup", "Error al Respaldar REGION RESULT");
                }
            }

            @Override
            public void continueProcessing() {
                //NEXT BACKUP;
                Toast.makeText(mActivity.getMContext(), "Estados Respaldados.", Toast.LENGTH_LONG).show();
                backupCity();
            }

            @Override
            public void onRequest() {
                getDialog().setMessage("Respaldando Estados.");
            }

            @Override
            public void onException(Exception e) {
                cancelBackup();
            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_GET_ALL_REGIONS);
    }

    private void backupCity() {
        getDialog().setTitle("Paso 11/11");
        datadownlooader = new RPCHandler(new IAsyncJSONResponseHandler() {
            final class ProfesionesProcessor extends AsyncResponseProcessor {

                public ProfesionesProcessor(IAsyncJSONResponseHandler handler,
                                            JSONArray data) {
                    super(handler, data);
                }

                @Override
                protected Void doInBackground(Object... params) {
                    super.doInBackground();
                    CommonDAO<CityVO> cityDAO = new CommonDAO<>(
                            mActivity.getMContext(), CityVO.class,
                            EduscoreOpenHelper.TABLE_DAT_CITY);
                    HashMap<String, String> itemMap;


                    try {
                        cityDAO.open();
                        cityDAO.clearTable(EduscoreOpenHelper.CREATE_DAT_CITY);
                        cityDAO.getDatabase().beginTransaction();
                        for (int i = 0; i < data.length(); i++) {
                            itemMap = new HashMap<String, String>();
                            itemMap.put("idCity", data.getJSONObject(i)
                                    .getString("idCity"));
                            itemMap.put("idRegion", data.getJSONObject(i)
                                    .getString("idRegion"));
                            itemMap.put("name", data.getJSONObject(i)
                                    .getString("name"));

                            cityDAO.createItem(itemMap);
                        }
                        cityDAO.getDatabase().setTransactionSuccessful();
                        cityDAO.getDatabase().endTransaction();
                    } catch (Exception e) {
                        Log.e("Data Backup", "Error al Respaldar CIUDADES");
                        removeDialog();
                    } finally {
                        cityDAO.close();
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
                    Log.e("Data Backup", "Error al Respaldar CIUDADES RESULT");
                }
            }

            @Override
            public void continueProcessing() {
                //NEXT BACKUP;
                Toast.makeText(mActivity.getMContext(), "Ciudades Respaldados.", Toast.LENGTH_LONG).show();
                removeDialog();
            }

            @Override
            public void onRequest() {
                getDialog().setMessage("Respaldando Ciudades.");
            }

            @Override
            public void onException(Exception e) {
                cancelBackup();
            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_GET_ALL_CITYS);
    }

    
}
