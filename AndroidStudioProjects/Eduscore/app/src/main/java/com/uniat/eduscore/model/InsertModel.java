package com.uniat.eduscore.model;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.uniat.eduscore.util.ICallback;
import com.uniat.eduscore.util.Task;
import com.uniat.eduscore.vo.CampusVO;
import com.uniat.eduscore.vo.CareerTypeVO;
import com.uniat.eduscore.vo.CareerVO;
import com.uniat.eduscore.vo.CityVO;
import com.uniat.eduscore.vo.CountryVO;
import com.uniat.eduscore.vo.EmailVO;
import com.uniat.eduscore.vo.EventVO;
import com.uniat.eduscore.vo.MediaVO;
import com.uniat.eduscore.vo.NewCorreosVO;
import com.uniat.eduscore.vo.NewInteresesVO;
import com.uniat.eduscore.vo.NewRegistroVO;
import com.uniat.eduscore.vo.NewTelefonosVO;
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
public class InsertModel implements ICallback{

    protected ExtendedFragmentActivity mActivity;
    private RPCHandlerString datadownlooader = null;
    private static InsertModel instance;

    private List<NewRegistroVO> newRegistroVO;
    List<NewCorreosVO> newCorreoVO = new ArrayList<NewCorreosVO>();
    List<NewTelefonosVO> newTelefonoVO = new ArrayList<NewTelefonosVO>();
    List<NewInteresesVO> newInteresVO = new ArrayList<NewInteresesVO>();

    ArrayList<String> listIdPerson;
    ArrayList<String> listIdTable;

    private String idPerson;

    private InsertModel() throws Exception {
        if (instance != null) {
            throw new Exception("Error de Singleton");
        }
    }

    public static InsertModel getInstance() {
        if (instance == null) {
            try {
                instance = new InsertModel();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
        }
        return instance;
    }

    public void setContextActivity(ExtendedFragmentActivity pActivity) {
        mActivity = pActivity;
        listIdPerson = new ArrayList<String>();
        listIdTable = new ArrayList<String>();
    }

    public void cancelBackup() {
        mActivity.getMessageBuilder().setMessage(
                "El proceso de Actualizacion se ha suspendido, Para reanudarlo presionar el boton de actualizacion");
        mActivity.getMessageBuilder().create().show();
    }

    public void cancelBackup(String e) {
        mActivity.getMessageBuilder().setMessage(e);
        mActivity.getMessageBuilder().create().show();
    }


    public void startBackup() {
        Task t = new Task( this );
        t.execute();
    }

    private String getIdCampus(){
        SharedPreferences shared = mActivity.getMContext().getSharedPreferences("eduscoreUser", mActivity.getMContext().MODE_PRIVATE);
        String idPerson = shared.getString("idPerson", "1");
        List<PersonVO> personVO;
        CommonDAO<PersonVO> personDAO = new CommonDAO<PersonVO>(mActivity.getMContext(), PersonVO.class, EduscoreOpenHelper.TABLE_DAT_PERSON);
        personDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_DAT_PERSON +
                " WHERE idPerson = '" + idPerson + "' AND status = '1'";
        personVO = personDAO.runQuery(sql );
        return personVO.get(0).getIdCampus();
    }

    private void insertPerson( final int index ) {

        datadownlooader = new RPCHandlerString(new IAsyncStringResponseHandler() {

            final class InsertProcessor extends AsyncResponseProcessor {

                public InsertProcessor(IAsyncStringResponseHandler handler, String data) {
                    super(handler, data);
                }

                @Override
                protected Void doInBackground(Object... params) {
                    super.doInBackground();
                    return null;

                }
            }

            @Override
            public void continueProcessing() {
                //NEXT BACKUP;
                insertPersonDescription(index);
            }

            @Override
            public void onResponse(String stringResponse) throws Exception {
                stringResponse = stringResponse.replace(" ", "");
                stringResponse = stringResponse.replace("\n", "");
                listIdPerson.add(stringResponse);
                (new InsertProcessor(this, stringResponse)).execute();
            }

            @Override
            public void onRequest() {
            }

            @Override
            public void onException(Exception e) {
                cancelBackup( e.getMessage() );
            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_INSERT_PERSON,
                new BasicNameValuePair("idCampus", getIdCampus()));
    }

    private void insertPersonDescription(final int index) {

        datadownlooader = new RPCHandlerString(new IAsyncStringResponseHandler() {

            @Override
            public void continueProcessing() {
            }

            @Override
            public void onResponse(String stringResponse) throws Exception {
                for( int i = 0; i < newCorreoVO.size(); i++ ){
                    insertCorreos( index, i );
                }
                for( int j = 0; j < newTelefonoVO.size(); j++ ){
                    insertTelefonos( index, j );
                }
                for( int k = 0; k < newInteresVO.size(); k++ ){
                    insertInterest( index, k );
                }
            }

            @Override
            public void onRequest() {
            }

            @Override
            public void onException(Exception e) {
                cancelBackup( "Person description: " + e.getMessage() );
            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_INSERT_PERSON_DESCRIPTION,
                new BasicNameValuePair("insert", newRegistroVO.get(index).getInsert(listIdPerson.get(index))));
    }

    private void insertCorreos(final int index, final int i) {

        datadownlooader = new RPCHandlerString(new IAsyncStringResponseHandler() {

            @Override
            public void continueProcessing() {
                //NEXT BACKUP;
            }

            @Override
            public void onResponse(String stringResponse) throws Exception {
            }

            @Override
            public void onRequest() {
            }

            @Override
            public void onException(Exception e) {
                cancelBackup( "Email: " + e.getMessage() );
            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_INSERT_EMAILS,
                new BasicNameValuePair("insert", newCorreoVO.get(i).getInsert(listIdPerson.get(index))));
    }

    private void insertTelefonos(final int index, final int j) {

        datadownlooader = new RPCHandlerString(new IAsyncStringResponseHandler() {

            @Override
            public void continueProcessing() {
                //NEXT BACKUP;
            }

            @Override
            public void onResponse(String stringResponse) throws Exception {
            }

            @Override
            public void onRequest() {
            }

            @Override
            public void onException(Exception e) {
                cancelBackup( "Telefono: " + e.getMessage() );
            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_INSERT_TELEPHONE,
                new BasicNameValuePair("insert", newTelefonoVO.get(j).getInsert(listIdPerson.get(index))));
    }

    private void insertInterest(final int index, final int k) {

        datadownlooader = new RPCHandlerString(new IAsyncStringResponseHandler() {

            @Override
            public void continueProcessing() {
                //NEXT BACKUP;
            }

            @Override
            public void onResponse(String stringResponse) throws Exception {
            }

            @Override
            public void onRequest() {
            }

            @Override
            public void onException(Exception e) {
                cancelBackup( "Interest:" + e.getMessage() );
            }
        });
        datadownlooader.execute(RPCHandler.OPERATION_INSERT_INTEREST,
                new BasicNameValuePair("insert", newInteresVO.get(k).getInsert(listIdPerson.get(index))));
    }

    @Override
    public void callback() {
    }

    @Override
    public void time() {
        CommonDAO<NewRegistroVO> newRegistronDAO = new CommonDAO<NewRegistroVO>(mActivity.getMContext(), NewRegistroVO.class, EduscoreOpenHelper.TABLE_NEW_REGISTRO);
        CommonDAO<NewCorreosVO> newCorreoDAO = new CommonDAO<NewCorreosVO>(mActivity.getMContext(), NewCorreosVO.class, EduscoreOpenHelper.TABLE_NEW_CORREOS);
        CommonDAO<NewTelefonosVO> newTelefonoDAO = new CommonDAO<NewTelefonosVO>(mActivity.getMContext(), NewTelefonosVO.class, EduscoreOpenHelper.TABLE_NEW_TELEFONOS);
        CommonDAO<NewInteresesVO> newInteresDAO = new CommonDAO<NewInteresesVO>(mActivity.getMContext(), NewInteresesVO.class, EduscoreOpenHelper.TABLE_NEW_INTERESES);
        newRegistronDAO.open();
        newCorreoDAO.open();
        newTelefonoDAO.open();
        newInteresDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_NEW_REGISTRO;
        newRegistroVO = newRegistronDAO.runQuery( sql );

        for( int i = 0; i < newRegistroVO.size(); i++){
            listIdTable.add(newRegistroVO.get(i).getIdRegistro());
            String sql1 = "SELECT * FROM " + EduscoreOpenHelper.TABLE_NEW_CORREOS +
                    " WHERE idRegistro = '" + newRegistroVO.get(i).getIdRegistro() + "'";

            String sql2 = "SELECT * FROM " + EduscoreOpenHelper.TABLE_NEW_TELEFONOS +
                    " WHERE idRegistro = '" + newRegistroVO.get(i).getIdRegistro() + "'";

            String sql3 = "SELECT * FROM " + EduscoreOpenHelper.TABLE_NEW_INTERESES +
                    " WHERE idRegistro = '" + newRegistroVO.get(i).getIdRegistro() + "'";
            newCorreoVO = newCorreoDAO.runQuery( sql1 );
            newTelefonoVO = newTelefonoDAO.runQuery( sql2 );
            newInteresVO = newInteresDAO.runQuery( sql3 );
            insertPerson( i );
        }
        SQLiteOpenHelper helper = EduscoreOpenHelper.getInstance(mActivity.getMContext());
        SQLiteDatabase dataBase = helper.getWritableDatabase();
        String sql0 = "DELETE FROM " + EduscoreOpenHelper.TABLE_NEW_REGISTRO;
        String sql1 = "DELETE FROM " + EduscoreOpenHelper.TABLE_NEW_CORREOS;
        String sql2 = "DELETE FROM " + EduscoreOpenHelper.TABLE_NEW_TELEFONOS;
        String sql3 = "DELETE FROM " + EduscoreOpenHelper.TABLE_NEW_INTERESES;
        try {
            dataBase.beginTransaction();
            dataBase.execSQL(sql0);
            dataBase.execSQL(sql1);
            dataBase.execSQL(sql2);
            dataBase.execSQL(sql3);
            dataBase.setTransactionSuccessful();
            dataBase.endTransaction();

        } catch (Exception e) {
            Log.e("Clear Dara", "Error al Limpiar Datos");
        } finally {
            dataBase.close();
        }
    }
}
