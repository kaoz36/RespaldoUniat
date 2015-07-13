package com.uniat.eduscore.model;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.uniat.eduscore.MainActivity;
import com.uniat.eduscore.db.CommonDAO;
import com.uniat.eduscore.db.EduscoreOpenHelper;
import com.uniat.eduscore.interfaces.IAsyncStringResponseHandler;
import com.uniat.eduscore.net.RPCHandler;
import com.uniat.eduscore.net.RPCHandlerString;
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
import com.uniat.eduscore.vo.PersonDescriptionVO;
import com.uniat.eduscore.vo.PersonVO;
import com.uniat.eduscore.vo.RegionVO;
import com.uniat.eduscore.vo.UserVO;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 23/04/2015.
 */
public class BackupModel {

    protected ProgressDialog dialog;
    protected ExtendedFragmentActivity mActivity;
    private RPCHandlerString datadownlooader = null;
    private static BackupModel instance;
    private int count;

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
        countElements(RPCHandlerString.OPERATION_COUNT_USERS);
        backupUsuarios(maxElementUser(EduscoreOpenHelper.TABLE_SIS_USER));
    }

    private int maxElementUser(String table){
        List<UserVO> results = new ArrayList<>();
        CommonDAO<UserVO> user = new CommonDAO<>(mActivity.getMContext(), UserVO.class, table);
        user.open();
        results = user.runQuery("Select * FROM " + table);
        return results.size();
    }

    private int maxElementCareers(String table){
        List<CareerVO> results = new ArrayList<>();
        CommonDAO<CareerVO> career = new CommonDAO<>(mActivity.getMContext(), CareerVO.class, table);
        career.open();
        results = career.runQuery("Select * FROM " + table);
        return results.size();
    }

    private int maxElementCareersType(String table){
        List<CareerTypeVO> results = new ArrayList<>();
        CommonDAO<CareerTypeVO> careerType = new CommonDAO<>(mActivity.getMContext(), CareerTypeVO.class, table);
        careerType.open();
        results = careerType.runQuery("Select * FROM " + table);
        return results.size();
    }

    private int maxElementMedia(String table){
        List<MediaVO> results = new ArrayList<>();
        CommonDAO<MediaVO> media = new CommonDAO<>(mActivity.getMContext(), MediaVO.class, table);
        media.open();
        results = media.runQuery("Select * FROM " + table);
        return results.size();
    }

    private int maxElementCampus(String table){
        List<CampusVO> results = new ArrayList<>();
        CommonDAO<CampusVO> campus = new CommonDAO<>(mActivity.getMContext(), CampusVO.class, table);
        campus.open();
        results = campus.runQuery("Select * FROM " + table);
        return results.size();
    }

    private int maxElementEvents(String table){
        List<EventVO> results = new ArrayList<>();
        CommonDAO<EventVO> events = new CommonDAO<>(mActivity.getMContext(), EventVO.class, table);
        events.open();
        results = events.runQuery("Select * FROM " + table);
        return results.size();
    }

    private int maxElementOrigins(String table){
        List<OriginVO> results = new ArrayList<>();
        CommonDAO<OriginVO> origins = new CommonDAO<>(mActivity.getMContext(), OriginVO.class, table);
        origins.open();
        results = origins.runQuery("Select * FROM " + table);
        return results.size();
    }

    private int maxElementEmail(String table){
        List<EmailVO> results = new ArrayList<>();
        CommonDAO<EmailVO> email = new CommonDAO<>(mActivity.getMContext(), EmailVO.class, table);
        email.open();
        results = email.runQuery("Select * FROM " + table);
        return results.size();
    }

    private int maxElementCountry(String table){
        List<CountryVO> results = new ArrayList<>();
        CommonDAO<CountryVO> country = new CommonDAO<>(mActivity.getMContext(), CountryVO.class, table);
        country.open();
        results = country.runQuery("Select * FROM " + table);
        return results.size();
    }

    private int maxElementRegion(String table){
        List<RegionVO> results = new ArrayList<>();
        CommonDAO<RegionVO> region = new CommonDAO<>(mActivity.getMContext(), RegionVO.class, table);
        region.open();
        results = region.runQuery("Select * FROM " + table);
        return results.size();
    }

    private int maxElementCity(String table){
        List<CityVO> results = new ArrayList<>();
        CommonDAO<CityVO> city = new CommonDAO<>(mActivity.getMContext(), CityVO.class, table);
        city.open();
        results = city.runQuery("Select * FROM " + table);
        return results.size();
    }

    private int maxElementPerson(String table){
        List<PersonVO> results = new ArrayList<>();
        CommonDAO<PersonVO> person = new CommonDAO<>(mActivity.getMContext(), PersonVO.class, table);
        person.open();
        results = person.runQuery("Select * FROM " + table);
        return results.size();
    }

    private int maxElementPersonDescription(String table){
        List<PersonDescriptionVO> results = new ArrayList<>();
        CommonDAO<PersonDescriptionVO> person = new CommonDAO<>(mActivity.getMContext(), PersonDescriptionVO.class, table);
        person.open();
        results = person.runQuery("Select * FROM " + table);
        return results.size();
    }

    private void countElements(String url){

        datadownlooader = new RPCHandlerString(new IAsyncStringResponseHandler() {
            @Override
            public void onResponse(String stringResponse) throws Exception {
                stringResponse = stringResponse.replace(" ", "");
                stringResponse = stringResponse.replace("\n", "");
                count = Integer.parseInt(stringResponse);
            }

            @Override
            public void onRequest() {

            }

            @Override
            public void onException(Exception e) {

            }

            @Override
            public void continueProcessing() {

            }
        });
        datadownlooader.execute(url);
    }

    private void backupUsuarios( final int index ) {
        getDialog().setTitle("Paso 1/13");
        datadownlooader = new RPCHandlerString(new IAsyncStringResponseHandler() {

            final class UsuariosProcessor extends AsyncResponseProcessor {

                public UsuariosProcessor(IAsyncStringResponseHandler handler, String data) {
                    super(handler, data);
                }

                @Override
                protected Void doInBackground(Object... params) {
                    super.doInBackground();
                    SQLiteOpenHelper helper = EduscoreOpenHelper.getInstance(mActivity.getMContext());
                    SQLiteDatabase dataBase = helper.getWritableDatabase();
                    try {
                        String sql = "INSERT INTO " + EduscoreOpenHelper.TABLE_SIS_USER +
                                " (idUser, user, password, idPerson) VALUES " + data;
                        String sqlDelete = "DELETE FROM " + EduscoreOpenHelper.TABLE_SIS_USER +
                                " WHERE password = '0'";
                        dataBase.beginTransaction();
                        dataBase.execSQL(sql);
                        dataBase.execSQL(sqlDelete);
                        dataBase.setTransactionSuccessful();
                        dataBase.endTransaction();
                    } catch (Exception e) {
                        Log.e("Data Backup", "Error al Respaldar USUARIOS");
                    } finally {
                        dataBase.close();
                    }
                    return null;

                }
            }

            @Override
            public void continueProcessing() {
                int t = index;
                if( (index + 300) < count ){
                    t += 300;
                    backupUsuarios(t);
                }else{
                    //NEXT BACKUP;
                    countElements(RPCHandlerString.OPERATION_COUNT_CAREERS);
                    backupCareers(maxElementCareers(EduscoreOpenHelper.TABLE_ESC_CAREER));
                }
            }

            @Override
            public void onResponse(String stringResponse) throws Exception {
                (new UsuariosProcessor(this, stringResponse)).execute();
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
        datadownlooader.execute(RPCHandler.OPERATION_GET_ALL_USERS,
                new BasicNameValuePair("index", index + ""));
    }

    private void backupCareers( final int index ) {
        getDialog().setTitle("Paso 2/13");
        datadownlooader = new RPCHandlerString(new IAsyncStringResponseHandler() {

            final class CareerProcessor extends AsyncResponseProcessor {

                public CareerProcessor(IAsyncStringResponseHandler handler,
                                         String data) {
                    super(handler, data);
                }

                @Override
                protected Void doInBackground(Object... params) {
                    super.doInBackground();
                    SQLiteOpenHelper helper = EduscoreOpenHelper.getInstance(mActivity.getMContext());
                    SQLiteDatabase dataBase = helper.getWritableDatabase();
                    try {
                        String sql = "INSERT INTO " + EduscoreOpenHelper.TABLE_ESC_CAREER +
                                " (idCareer, name, idCareerType, prefix, status) VALUES " + data;
                        String sqlDelete = "DELETE FROM " + EduscoreOpenHelper.TABLE_ESC_CAREER +
                                " WHERE name = '0'";
                        dataBase.beginTransaction();
                        dataBase.execSQL(sql);
                        dataBase.execSQL(sqlDelete);
                        dataBase.setTransactionSuccessful();
                        dataBase.endTransaction();
                    } catch (Exception e) {
                        Log.e("Data Backup", "Error al Respaldar CARRERAS");
                    } finally {
                        dataBase.close();
                    }
                    return null;

                }
            }

            @Override
            public void onResponse(String stringResponse) throws Exception {
                (new CareerProcessor(this, stringResponse)).execute();
            }

            @Override
            public void onRequest() {
                getDialog().setMessage("Respaldando Carreras " + index + " / " + count);
            }

            @Override
            public void onException(Exception e) {
                cancelBackup();
            }

            @Override
            public void continueProcessing() {
                int t = index;
                if( (index + 300) < count ){
                    t += 300;
                    backupCareers(t);
                }else{
                    //NEXT BACKUP;
                    countElements(RPCHandlerString.OPERATION_COUNT_CAREERTYPES);
                    backupCareersType(maxElementCareersType(EduscoreOpenHelper.TABLE_ESC_CAREERTYPE));
                }

            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_GET_ALL_CAREERS,
                new BasicNameValuePair("index", index + ""));
    }

    private void backupCareersType( final int index) {
        getDialog().setTitle("Paso 3/13");
        datadownlooader = new RPCHandlerString(new IAsyncStringResponseHandler() {

            final class CareerTypeProcessor extends AsyncResponseProcessor {

                public CareerTypeProcessor(IAsyncStringResponseHandler handler, String data) {
                    super(handler, data);
                }

                @Override
                protected Void doInBackground(Object... params) {
                    super.doInBackground();
                    SQLiteOpenHelper helper = EduscoreOpenHelper.getInstance(mActivity.getMContext());
                    SQLiteDatabase dataBase = helper.getWritableDatabase();
                    try {
                        String sql = "INSERT INTO " + EduscoreOpenHelper.TABLE_ESC_CAREERTYPE +
                                " (idCareerType, name, status) VALUES " + data;
                        String sqlDelete = "DELETE FROM " + EduscoreOpenHelper.TABLE_ESC_CAREERTYPE +
                                " WHERE status = '0'";
                        dataBase.beginTransaction();
                        dataBase.execSQL(sql);
                        dataBase.execSQL(sqlDelete);
                        dataBase.setTransactionSuccessful();
                        dataBase.endTransaction();
                    } catch (Exception e) {
                        Log.e("Data Backup", "Error al Respaldar TIPO CARRERA");
                    } finally {
                        dataBase.close();
                    }
                    return null;

                }
            }

            @Override
            public void continueProcessing() {
                int t = index;
                if( (index + 300) < count ){
                    t += 300;
                    backupCareersType(t);
                }else{
                    //NEXT BACKUP;
                    countElements(RPCHandlerString.OPERATION_COUNT_MEDIAS);
                    backupMedia(maxElementMedia(EduscoreOpenHelper.TABLE_DAT_MEDIA));
                }
            }

            @Override
            public void onResponse(String stringResponse) throws Exception {
                (new CareerTypeProcessor(this, stringResponse)).execute();
            }

            @Override
            public void onRequest() {
                getDialog().setMessage("Respaldando Tipos de Carrera " + index + " / " + count);
            }

            @Override
            public void onException(Exception e) {
                cancelBackup();
            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_GET_ALL_CAREERTYPES,
                new BasicNameValuePair("index", index + ""));
    }

    private void backupMedia(final int index) {
        getDialog().setTitle("Paso 4/13");
        datadownlooader = new RPCHandlerString(new IAsyncStringResponseHandler() {

            final class MediaProcessor extends AsyncResponseProcessor {

                public MediaProcessor(IAsyncStringResponseHandler handler, String data) {
                    super(handler, data);
                }

                @Override
                protected Void doInBackground(Object... params) {
                    super.doInBackground();
                    SQLiteOpenHelper helper = EduscoreOpenHelper.getInstance(mActivity.getMContext());
                    SQLiteDatabase dataBase = helper.getWritableDatabase();
                    try {
                        String sql = "INSERT INTO " + EduscoreOpenHelper.TABLE_DAT_MEDIA +
                                " (idMedia, name, status, idCampus) VALUES " + data;
                        String sqlDelete = "DELETE FROM " + EduscoreOpenHelper.TABLE_DAT_MEDIA +
                                " WHERE name = '0'";
                        dataBase.beginTransaction();
                        dataBase.execSQL(sql);
                        dataBase.execSQL(sqlDelete);
                        dataBase.setTransactionSuccessful();
                        dataBase.endTransaction();
                    } catch (Exception e) {
                        Log.e("Data Backup", "Error al Respaldar MEDIOS");
                    } finally {
                        dataBase.close();
                    }
                    return null;

                }
            }

            @Override
            public void continueProcessing() {
                int t = index;
                if( (index + 300) < count ){
                    t += 300;
                    backupMedia(t);
                }else{
                    //NEXT BACKUP;
                    countElements(RPCHandlerString.OPERATION_COUNT_CAMPUS);
                    backupCampus(maxElementCampus(EduscoreOpenHelper.TABLE_DAT_CAMPUS));
                }
            }

            @Override
            public void onResponse(String stringResponse) throws Exception {
                (new MediaProcessor(this, stringResponse)).execute();
            }

            @Override
            public void onRequest() {
                getDialog().setMessage("Respaldando Medios " + index + " / " + count);
            }

            @Override
            public void onException(Exception e) {
                cancelBackup();
            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_GET_ALL_MEDIAS,
                new BasicNameValuePair("index", index + ""));
    }

    private void backupCampus(final int index) {
        getDialog().setTitle("Paso 5/13");
        datadownlooader = new RPCHandlerString(new IAsyncStringResponseHandler() {

            final class CampusProcessor extends AsyncResponseProcessor {

                public CampusProcessor(IAsyncStringResponseHandler handler, String data) {
                    super(handler, data);
                }

                @Override
                protected Void doInBackground(Object... params) {
                    super.doInBackground();
                    SQLiteOpenHelper helper = EduscoreOpenHelper.getInstance(mActivity.getMContext());
                    SQLiteDatabase dataBase = helper.getWritableDatabase();
                    try {
                        String sql = "INSERT INTO " + EduscoreOpenHelper.TABLE_DAT_CAMPUS +
                                " (idCampus, name, address, status, idCountry, idRegion, idCity, nameAlt) VALUES " + data;
                        String sqlDelete = "DELETE FROM " + EduscoreOpenHelper.TABLE_DAT_CAMPUS +
                                " WHERE name = '0'";
                        dataBase.beginTransaction();
                        dataBase.execSQL(sql);
                        dataBase.execSQL(sqlDelete);
                        dataBase.setTransactionSuccessful();
                        dataBase.endTransaction();
                    } catch (Exception e) {
                        Log.e("Data Backup", "Error al Respaldar CAMPUS");
                    } finally {
                        dataBase.close();
                    }
                    return null;

                }
            }

            @Override
            public void continueProcessing() {
                int t = index;
                if( (index + 300) < count ){
                    t += 300;
                    backupCampus(t);
                }else{
                    //NEXT BACKUP;
                    countElements(RPCHandlerString.OPERATION_COUNT_EVENTS);
                    backupEvents(maxElementEvents(EduscoreOpenHelper.TABLE_DAT_EVENTS));
                }
            }

            @Override
            public void onResponse(String stringResponse) throws Exception {
                (new CampusProcessor(this, stringResponse)).execute();
            }

            @Override
            public void onRequest() {
                getDialog().setMessage("Respaldando Tipos de Campus " + index + " / " + count);
            }

            @Override
            public void onException(Exception e) {
                cancelBackup();
            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_GET_ALL_CAMPUS,
                new BasicNameValuePair("index", index + ""));
    }

    private void backupEvents(final int index) {
        getDialog().setTitle("Paso 6/13");
        datadownlooader = new RPCHandlerString(new IAsyncStringResponseHandler() {

            final class EventsProcessor extends AsyncResponseProcessor {

                public EventsProcessor(IAsyncStringResponseHandler handler, String data) {
                    super(handler, data);
                }

                @Override
                protected Void doInBackground(Object... params) {
                    super.doInBackground();
                    SQLiteOpenHelper helper = EduscoreOpenHelper.getInstance(mActivity.getMContext());
                    SQLiteDatabase dataBase = helper.getWritableDatabase();
                    try {
                        String sql = "INSERT INTO " + EduscoreOpenHelper.TABLE_DAT_EVENTS +
                                " (idEvent, name, status, idCampus) VALUES " + data;
                        String sqlDelete = "DELETE FROM " + EduscoreOpenHelper.TABLE_DAT_ORIGIN +
                                " WHERE status = '0'";
                        dataBase.beginTransaction();
                        dataBase.execSQL(sql);
                        dataBase.execSQL(sqlDelete);
                        dataBase.setTransactionSuccessful();
                        dataBase.endTransaction();
                    } catch (Exception e) {
                        Log.e("Data Backup", "Error al Respaldar EVENTOS");
                    } finally {
                        dataBase.close();
                    }
                    return null;

                }
            }

            @Override
            public void continueProcessing() {
                int t = index;
                if( (index + 300) < count ){
                    t += 300;
                    backupEvents(t);
                }else{
                    //NEXT BACKUP;
                    countElements(RPCHandlerString.OPERATION_COUNT_ORIGINS);
                    backupOrigins(maxElementOrigins(EduscoreOpenHelper.TABLE_DAT_ORIGIN));
                }
            }

            @Override
            public void onResponse(String stringResponse) throws Exception {
                (new EventsProcessor(this, stringResponse)).execute();
            }

            @Override
            public void onRequest() {
                getDialog().setMessage("Respaldando Eventos " + index + " / " + count);
            }

            @Override
            public void onException(Exception e) {
                cancelBackup();
            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_GET_ALL_EVENTS,
                new BasicNameValuePair("index", index + ""));
    }

    private void backupOrigins(final int index) {
        getDialog().setTitle("Paso 7/13");
        datadownlooader = new RPCHandlerString(new IAsyncStringResponseHandler() {

            final class OriginsProcessor extends AsyncResponseProcessor {

                public OriginsProcessor(IAsyncStringResponseHandler handler, String data) {
                    super(handler, data);
                }

                @Override
                protected Void doInBackground(Object... params) {
                    super.doInBackground();
                    SQLiteOpenHelper helper = EduscoreOpenHelper.getInstance(mActivity.getMContext());
                    SQLiteDatabase dataBase = helper.getWritableDatabase();
                    try {
                        String sql = "INSERT INTO " + EduscoreOpenHelper.TABLE_DAT_ORIGIN +
                                " (idOrigin, name, type, status, idCampus) VALUES " + data;
                        String sqlDelete = "DELETE FROM " + EduscoreOpenHelper.TABLE_DAT_ORIGIN +
                                " WHERE name = '0'";
                        dataBase.beginTransaction();
                        dataBase.execSQL(sql);
                        dataBase.execSQL(sqlDelete);
                        dataBase.setTransactionSuccessful();
                        dataBase.endTransaction();
                    } catch (Exception e) {
                        Log.e("Data Backup", "Error al Respaldar ORIGENES");
                    } finally {
                        dataBase.close();
                    }
                    return null;

                }
            }

            @Override
            public void continueProcessing() {
                int t = index;
                if( (index + 300) < count ){
                    t += 300;
                    backupOrigins(t);
                }else{
                    //NEXT BACKUP;
                    countElements(RPCHandlerString.OPERATION_COUNT_EMAILS);
                    backupEmail(maxElementEmail(EduscoreOpenHelper.TABLE_DAT_EMAIL));
                }
            }

            @Override
            public void onResponse(String stringResponse) throws Exception {
                (new OriginsProcessor(this, stringResponse)).execute();
            }

            @Override
            public void onRequest() {
                getDialog().setMessage("Respaldando Origenes " + index + " / " + count);
            }

            @Override
            public void onException(Exception e) {
                cancelBackup();
            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_GET_ALL_ORIGINS,
                new BasicNameValuePair("index", index + ""));
    }

    private void backupEmail(final int index) {
        getDialog().setTitle("Paso 8/13");
        datadownlooader = new RPCHandlerString(new IAsyncStringResponseHandler() {

            final class EmailProcessor extends AsyncResponseProcessor {

                public EmailProcessor(IAsyncStringResponseHandler handler, String data) {
                    super(handler, data);
                }

                @Override
                protected Void doInBackground(Object... params) {
                    super.doInBackground();
                    SQLiteOpenHelper helper = EduscoreOpenHelper.getInstance(mActivity.getMContext());
                    SQLiteDatabase dataBase = helper.getWritableDatabase();
                    try {
                        String sql = "INSERT INTO " + EduscoreOpenHelper.TABLE_DAT_EMAIL +
                                " (email) VALUES " + data;
                        String sqlDelete = "DELETE FROM " + EduscoreOpenHelper.TABLE_DAT_EMAIL +
                                " WHERE email = '0'";
                        dataBase.beginTransaction();
                        dataBase.execSQL(sql);
                        dataBase.execSQL(sqlDelete);
                        dataBase.setTransactionSuccessful();
                        dataBase.endTransaction();
                    } catch (Exception e) {
                        Log.e("Data Backup", "Error al Respaldar CORREOS");
                    } finally {
                        dataBase.close();
                    }
                    return null;

                }
            }

            @Override
            public void continueProcessing() {
                int t = index;
                if( (index + 300) < count ){
                    t += 300;
                    backupEmail(t);
                }else{
                    //NEXT BACKUP;
                    countElements(RPCHandlerString.OPERATION_COUNT_COUNTRYS);
                    backupCountry(maxElementCountry(EduscoreOpenHelper.TABLE_DAT_COUNTRY));
                }
            }

            @Override
            public void onResponse(String stringResponse) throws Exception {
                (new EmailProcessor(this, stringResponse)).execute();
            }

            @Override
            public void onRequest() {
                getDialog().setMessage("Respaldando Correos " + index + " / " + count);
            }

            @Override
            public void onException(Exception e) {
                cancelBackup();
            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_GET_ALL_EMAILS,
                new BasicNameValuePair("index", index + ""));
    }

    private void backupCountry(final int index) {
        getDialog().setTitle("Paso 9/13");
        datadownlooader = new RPCHandlerString(new IAsyncStringResponseHandler() {

            final class CountryProcessor extends AsyncResponseProcessor {

                public CountryProcessor(IAsyncStringResponseHandler handler, String data) {
                    super(handler, data);
                }

                @Override
                protected Void doInBackground(Object... params) {
                    super.doInBackground();
                    SQLiteOpenHelper helper = EduscoreOpenHelper.getInstance(mActivity.getMContext());
                    SQLiteDatabase dataBase = helper.getWritableDatabase();
                    try {
                        String sql = "INSERT INTO " + EduscoreOpenHelper.TABLE_DAT_COUNTRY +
                                " (idCountry, name) VALUES " + data;
                        dataBase.beginTransaction();
                        dataBase.execSQL(sql);
                        String sqlDelete = "DELETE FROM " + EduscoreOpenHelper.TABLE_DAT_COUNTRY +
                                " WHERE name = '0'";
                        dataBase.execSQL(sqlDelete);
                        dataBase.setTransactionSuccessful();
                        dataBase.endTransaction();
                    } catch (Exception e) {
                        Log.e("Data Backup", "Error al Respaldar PAISES");
                    } finally {
                        dataBase.close();
                    }
                    return null;

                }
            }

            @Override
            public void continueProcessing() {
                int t = index;
                if( (index + 300) < count ){
                    t += 300;
                    backupCountry(t);
                }else{
                    //NEXT BACKUP;
                    countElements(RPCHandlerString.OPERATION_COUNT_REGIONS);
                    backupRegion(maxElementRegion(EduscoreOpenHelper.TABLE_DAT_REGION));
                }
            }

            @Override
            public void onResponse(String stringResponse) throws Exception {
                (new CountryProcessor(this, stringResponse)).execute();
            }

            @Override
            public void onRequest() {
                getDialog().setMessage("Respaldando Paises " + index + " / " + count);
            }

            @Override
            public void onException(Exception e) {
                cancelBackup();
            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_GET_ALL_COUNTRYS,
                new BasicNameValuePair("index", index + ""));
    }

    private void backupRegion(final int index) {
        getDialog().setTitle("Paso 10/13");
        datadownlooader = new RPCHandlerString(new IAsyncStringResponseHandler() {

            final class RegionProcessor extends AsyncResponseProcessor {

                public RegionProcessor(IAsyncStringResponseHandler handler, String data) {
                    super(handler, data);
                }

                @Override
                protected Void doInBackground(Object... params) {
                    super.doInBackground();
                    SQLiteOpenHelper helper = EduscoreOpenHelper.getInstance(mActivity.getMContext());
                    SQLiteDatabase dataBase = helper.getWritableDatabase();
                    try {
                        String sql = "INSERT INTO " + EduscoreOpenHelper.TABLE_DAT_REGION +
                                " (idRegion, idContry, name) VALUES " + data;
                        String sqlDelete = "DELETE FROM " + EduscoreOpenHelper.TABLE_DAT_REGION+
                                " WHERE name = '0'";
                        dataBase.beginTransaction();
                        dataBase.execSQL(sql);
                        dataBase.execSQL(sqlDelete);
                        dataBase.setTransactionSuccessful();
                        dataBase.endTransaction();
                    } catch (Exception e) {
                        Log.e("Data Backup", "Error al Respaldar ESTADOS");
                    } finally {
                        dataBase.close();
                    }
                    return null;

                }
            }

            @Override
            public void continueProcessing() {
                int t = index;
                if( (index + 150) < count ){
                    t += 150;
                    backupRegion(t);
                }else{
                    //NEXT BACKUP;
                    countElements(RPCHandlerString.OPERATION_COUNT_CITYS);
                    backupCity(maxElementCity(EduscoreOpenHelper.TABLE_DAT_CITY));
                }
            }

            @Override
            public void onResponse(String stringResponse) throws Exception {
                (new RegionProcessor(this, stringResponse)).execute();
            }

            @Override
            public void onRequest() {
                getDialog().setMessage("Respaldando Estados " + index + " / " + count);
            }

            @Override
            public void onException(Exception e) {
                cancelBackup();
            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_GET_ALL_REGIONS,
                new BasicNameValuePair("index", index + ""));
    }

    private void backupCity(final int index) {
        getDialog().setTitle("Paso 11/13");
        datadownlooader = new RPCHandlerString(new IAsyncStringResponseHandler() {

            final class CityProcessor extends AsyncResponseProcessor {

                public CityProcessor(IAsyncStringResponseHandler handler, String data) {
                    super(handler, data);
                }

                @Override
                protected Void doInBackground(Object... params) {
                    super.doInBackground();
                    SQLiteOpenHelper helper = EduscoreOpenHelper.getInstance(mActivity.getMContext());
                    SQLiteDatabase dataBase = helper.getWritableDatabase();
                    try {
                        String sql = "INSERT INTO " + EduscoreOpenHelper.TABLE_DAT_CITY +
                                " (idCity, idRegion, name) VALUES " + data;
                        String sqlDelete = "DELETE FROM " + EduscoreOpenHelper.TABLE_DAT_CITY +
                                " WHERE name = '0'";
                        dataBase.beginTransaction();
                        dataBase.execSQL(sql);
                        dataBase.execSQL(sqlDelete);
                        dataBase.setTransactionSuccessful();
                        dataBase.endTransaction();
                    } catch (Exception e) {
                        Log.e("Data Backup", "Error al Respaldar CIUDADES");
                    } finally {
                        dataBase.close();
                    }
                    return null;

                }
            }

            @Override
            public void continueProcessing() {
                int t = index;
                if( (index + 150) < count ){
                    t += 150;
                    backupCity(t);
                }else{
                    countElements(RPCHandlerString.OPERATION_COUNT_PERSONS);
                    backupPerson(maxElementPerson(EduscoreOpenHelper.TABLE_DAT_PERSON));
                }
            }

            @Override
            public void onResponse(String stringResponse) throws Exception {
                (new CityProcessor(this, stringResponse)).execute();
            }

            @Override
            public void onRequest() {
                getDialog().setMessage("Respaldando Ciudades " + index + " / " + count);
            }

            @Override
            public void onException(Exception e) {
                cancelBackup();
            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_GET_ALL_CITYS,
                new BasicNameValuePair("index", index + ""));
    }

    private void backupPerson(final int index) {
        getDialog().setTitle("Paso 12/13");
        datadownlooader = new RPCHandlerString(new IAsyncStringResponseHandler() {

            final class PersonProcessor extends AsyncResponseProcessor {

                public PersonProcessor(IAsyncStringResponseHandler handler, String data) {
                    super(handler, data);
                }

                @Override
                protected Void doInBackground(Object... params) {
                    super.doInBackground();
                    SQLiteOpenHelper helper = EduscoreOpenHelper.getInstance(mActivity.getMContext());
                    SQLiteDatabase dataBase = helper.getWritableDatabase();
                    try {
                        String sql = "INSERT INTO " + EduscoreOpenHelper.TABLE_DAT_PERSON +
                                " (idPerson, comments, idCharge, status, idCamnpus) VALUES " + data;
                        String sqlDelete = "DELETE FROM " + EduscoreOpenHelper.TABLE_DAT_PERSON +
                                " WHERE idPerson = '0'";
                        dataBase.beginTransaction();
                        dataBase.execSQL(sql);
                        dataBase.execSQL(sqlDelete);
                        dataBase.setTransactionSuccessful();
                        dataBase.endTransaction();
                    } catch (Exception e) {
                        Log.e("Data Backup", "Error al Respaldar PERSONAS");
                    } finally {
                        dataBase.close();
                    }
                    return null;

                }
            }

            @Override
            public void continueProcessing() {
                int t = index;
                if( (index + 150) < count ){
                    t += 150;
                    backupPerson(t);
                }else{
                    countElements(RPCHandlerString.OPERATION_COUNT_PERSONDESCRIPTIONS);
                    backupPersonDescription(maxElementPersonDescription(EduscoreOpenHelper.TABLE_DAT_PERSONDESCRIPTION));
                }
            }

            @Override
            public void onResponse(String stringResponse) throws Exception {
                (new PersonProcessor(this, stringResponse)).execute();
            }

            @Override
            public void onRequest() {
                getDialog().setMessage("Respaldando Personas " + index + " / " + count);
            }

            @Override
            public void onException(Exception e) {
                cancelBackup();
            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_GET_ALL_PERSONS,
                new BasicNameValuePair("index", index + ""));
    }

    private void backupPersonDescription(final int index) {
        getDialog().setTitle("Paso 13/13");
        datadownlooader = new RPCHandlerString(new IAsyncStringResponseHandler() {

            final class PersonDescriptionProcessor extends AsyncResponseProcessor {

                public PersonDescriptionProcessor(IAsyncStringResponseHandler handler, String data) {
                    super(handler, data);
                }

                @Override
                protected Void doInBackground(Object... params) {
                    super.doInBackground();
                    SQLiteOpenHelper helper = EduscoreOpenHelper.getInstance(mActivity.getMContext());
                    SQLiteDatabase dataBase = helper.getWritableDatabase();
                    try {
                        String sql = "INSERT INTO " + EduscoreOpenHelper.TABLE_DAT_PERSONDESCRIPTION +
                                " (idPerson, name, lastName, idCountry, idRegion, idCity, gender, curp, " +
                                "rfc, street, numberExt, numberInt, zip) VALUES " + data;
                        String sqlDelete = "DELETE FROM " + EduscoreOpenHelper.TABLE_DAT_PERSONDESCRIPTION +
                                " WHERE name = '0'";
                        dataBase.beginTransaction();
                        dataBase.execSQL(sql);
                        dataBase.execSQL(sqlDelete);
                        dataBase.setTransactionSuccessful();
                        dataBase.endTransaction();
                    } catch (Exception e) {
                        Log.e("Data Backup", "Error al Respaldar DES PERSONAS");
                    } finally {
                        dataBase.close();
                    }
                    return null;

                }
            }

            @Override
            public void continueProcessing() {
                int t = index;
                if( (index + 150) < count ){
                    t += 150;
                    backupPersonDescription(t);
                }else{
                    //NEXT BACKUP;
                    removeDialog();
                    Intent intent = new Intent(mActivity.getMContext(), MainActivity.class);
                    mActivity.startActivity(intent);
                    mActivity.finish();
                }
            }

            @Override
            public void onResponse(String stringResponse) throws Exception {
                (new PersonDescriptionProcessor(this, stringResponse)).execute();
            }

            @Override
            public void onRequest() {
                getDialog().setMessage("Respaldando Personas " + index + " / " + count);
            }

            @Override
            public void onException(Exception e) {
                cancelBackup();
            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_GET_ALL_PERSONDESCRIPTIONS,
                new BasicNameValuePair("index", index + ""));
    }
    
}
