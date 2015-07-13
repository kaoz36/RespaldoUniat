package com.uniat.eduscore;

import android.content.Intent;
import android.content.SharedPreferences;

import com.uniat.eduscore.adapters.AdapterFormulario;
import com.uniat.eduscore.db.CommonDAO;
import com.uniat.eduscore.db.EduscoreOpenHelper;
import com.uniat.eduscore.model.BackupModel;
import com.uniat.eduscore.model.InsertModel;
import com.uniat.eduscore.util.ExtendedFragmentActivity;
import com.uniat.eduscore.vo.CityVO;
import com.uniat.eduscore.vo.CountryVO;
import com.uniat.eduscore.vo.EventVO;
import com.uniat.eduscore.vo.MediaVO;
import com.uniat.eduscore.vo.OriginVO;
import com.uniat.eduscore.vo.PersonVO;
import com.uniat.eduscore.vo.RegionVO;
import com.uniat.eduscore.vo.NewRegistroVO;
import com.university3dmx.eduscore.R;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 06/04/2015.
 */
    public class MainActivity extends ExtendedFragmentActivity {

    static ViewPager vViewPager;
    static AdapterFormulario adapterFormulario;
    private ImageButton imgBtn1;
    private ImageButton imgBtn2;
    private ImageButton imgBtn3;
    private Button btnActualizar;
    NewRegistroVO registroVO;
    public ArrayList<String> listEmail;
    public ArrayList<String> listTelephone;
    public ArrayList<String> listTipos;
    public ArrayList<String> listIntereses;

    public static MainActivity mthis;

    private String idRegistro;

    @Override
    protected void onResume() {
        super.onResume();
        if( !wifiOn ){
            Toast.makeText(getMContext(), "Debes tener internet para poder Actualizar Datos.", Toast.LENGTH_LONG).show();
            btnActualizar.setVisibility(View.INVISIBLE);
        }else{
            btnActualizar.setVisibility(View.VISIBLE);
            saveServer();
        }
    }

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.activity_main);
        mthis = this;
        listEmail = new ArrayList<>();
        listTelephone = new ArrayList<>();
        listTipos = new ArrayList<>();
        listIntereses = new ArrayList<>();

        adapterFormulario = new AdapterFormulario(getSupportFragmentManager(), 3);
        imgBtn1 = (ImageButton) findViewById(R.id.imgBtnSelectedView1);
        imgBtn2 = (ImageButton) findViewById(R.id.imgBtnSelectedView2);
        imgBtn3 = (ImageButton) findViewById(R.id.imgBtnSelectedView3);
        btnActualizar = (Button) findViewById(R.id.btnActualizar);
        adapterFormulario.setImgBtns(imgBtn1, imgBtn2, imgBtn3);
        vViewPager = (ViewPager) this.findViewById(R.id.pager);
        vViewPager.setAdapter(adapterFormulario);
        vViewPager.setCurrentItem(0);
        vViewPager.setOffscreenPageLimit(2);
        vViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        imgBtn1.setImageResource(R.drawable.selector_btn_select_on);
                        imgBtn2.setImageResource(R.drawable.selector_btn_select_off);
                        imgBtn3.setImageResource(R.drawable.selector_btn_select_off);
                        break;
                    case 1:
                        imgBtn1.setImageResource(R.drawable.selector_btn_select_off);
                        imgBtn2.setImageResource(R.drawable.selector_btn_select_on);
                        imgBtn3.setImageResource(R.drawable.selector_btn_select_off);
                        break;
                    case 2:
                        imgBtn1.setImageResource(R.drawable.selector_btn_select_off);
                        imgBtn2.setImageResource(R.drawable.selector_btn_select_off);
                        imgBtn3.setImageResource(R.drawable.selector_btn_select_on);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void onClickActualizar(View pView){
        BackupModel.getInstance().setContextActivity(this);

        if (!wifiOn) {
            getMessageBuilder().setMessage("Es necesario tener una conexion WIFI Activa para reaizar este proceso");
            getMessageBuilder().create().show();
            return;
        }

        BackupModel.getInstance().startBackup();
        
    }

    public void onClickSelectedView(View pView){
        switch (pView.getId()){
            case R.id.imgBtnSelectedView1:
                imgBtn1.setImageResource(R.drawable.selector_btn_select_on);
                imgBtn2.setImageResource(R.drawable.selector_btn_select_off);
                imgBtn3.setImageResource(R.drawable.selector_btn_select_off);
                vViewPager.setCurrentItem(0);
                break;
            case R.id.imgBtnSelectedView2:
                imgBtn1.setImageResource(R.drawable.selector_btn_select_off);
                imgBtn2.setImageResource(R.drawable.selector_btn_select_on);
                imgBtn3.setImageResource(R.drawable.selector_btn_select_off);
                vViewPager.setCurrentItem(1);
                break;
            case R.id.imgBtnSelectedView3:
                imgBtn1.setImageResource(R.drawable.selector_btn_select_off);
                imgBtn2.setImageResource(R.drawable.selector_btn_select_off);
                imgBtn3.setImageResource(R.drawable.selector_btn_select_on);
                vViewPager.setCurrentItem(2);
                break;
        }
    }


    public void onClickGuardar(View pView){
        registroVO = new NewRegistroVO();
        idRegistro = ((int)( Math.random()*1000000 )) + "";
        registroVO.setIdRegistro(idRegistro);
        try{
            registroVO.setNombre(capitalizado(((EditText) findViewById(R.id.editTextName)).getText().toString()));
        }catch (Exception e){
            Toast.makeText(getMContext(), "El Nombre es Obligatorio.", Toast.LENGTH_LONG).show();
            return;
        }

        try{
            registroVO.setApellidos(capitalizado(((EditText) findViewById(R.id.editTextLastName)).getText().toString()));
        }catch (Exception e){
            Toast.makeText(getMContext(), "El Campo Apellidos es Obligatorio.", Toast.LENGTH_LONG).show();
            return;
        }

        if( listEmail.size() == 0 && listTelephone.size() == 0){
            Toast.makeText(getMContext(), "Es Obligatorio poner al menos un Correo o Telefono.", Toast.LENGTH_LONG).show();
            return;
        }

        registroVO.setMedio(getIdMedia());

        if( registroVO.getMedio().equals("null") ) {
            Toast.makeText(getMContext(), "El Medio de informacion es Obligatorio.", Toast.LENGTH_LONG).show();
            return;
        }

        if( listIntereses.size() == 0 ){
            Toast.makeText(getMContext(), "Debes seleccionar por lo menos un Interes.", Toast.LENGTH_LONG).show();
            return;
        }



        try{
            registroVO.setFechaNacimiento(((TextView) findViewById(R.id.TextNacimiento)).getText().toString());
        }catch (Exception e){
            registroVO.setFechaNacimiento("");
        }

        try{
            registroVO.setPaisOrigen(getIdPais(((Spinner) findViewById(R.id.spinnerPaisOrigen)).getSelectedItem().toString()));
        }catch (Exception e){
            registroVO.setPaisOrigen("0");
        }

        try{
            registroVO.setEstadoOrigen(getIdEstado(((Spinner) findViewById(R.id.spinnerEstadoOrigen)).getSelectedItem().toString()));
            if( registroVO.getEstadoOrigen().equals("null") ){
                registroVO.setEstadoOrigen("0");
            }
        }catch (Exception e){
            registroVO.setEstadoOrigen("0");
        }

        try{
            registroVO.setCiudadOrigen(getIdCiudad(((Spinner) findViewById(R.id.spinnerCiudadOrigen)).getSelectedItem().toString()));
            if( registroVO.getCiudadOrigen().equals("null") ){
                registroVO.setCiudadOrigen("0");
            }
        }catch (Exception e){
            registroVO.setCiudadOrigen("0");
        }

        try{
            registroVO.setPaisResidencia(getIdPais(((Spinner) findViewById(R.id.spinnerPaisResidencia)).getSelectedItem().toString()));
        }catch (Exception e){
            registroVO.setPaisResidencia("0");
        }

        try{
            registroVO.setEstadoResidencia(getIdEstado(((Spinner) findViewById(R.id.spinnerEstadoResidencia)).getSelectedItem().toString()));
        }catch (Exception e){
            registroVO.setEstadoResidencia("0");
        }

        try{
            registroVO.setCiudadResidencia(getIdCiudad(((Spinner) findViewById(R.id.spinnerCiudadResidencia)).getSelectedItem().toString()));
        }catch (Exception e){
            registroVO.setCiudadResidencia("0");
        }

        try{
            registroVO.setCurp(((EditText) findViewById(R.id.editTextCurp)).getText().toString());
        }catch (Exception e){
            registroVO.setCurp("");
        }

        try{
            registroVO.setCalle(((EditText) findViewById(R.id.editTextCalle)).getText().toString());
        }catch (Exception e){
            registroVO.setCalle("");
        }

        try{
            registroVO.setNumExt(((EditText) findViewById(R.id.editTextNumExt)).getText().toString());
        }catch (Exception e){
            registroVO.setNumExt("");
        }

        try{
            registroVO.setNumInt(((EditText) findViewById(R.id.editTextNumInt)).getText().toString());
        }catch (Exception e){
            registroVO.setNumInt("");
        }

        try{
            registroVO.setColonia(((EditText) findViewById(R.id.editTextColonia)).getText().toString());
        }catch (Exception e){
            registroVO.setColonia("");
        }

        try{
            registroVO.setCp(((EditText) findViewById(R.id.editTextCP)).getText().toString());
        }catch (Exception e){
            registroVO.setCp("");
        }

        try{
            if( ((Spinner) findViewById(R.id.spinnerProbabilidad)).getSelectedItem().toString().equals("Seleccione una Probabilidad")){
                registroVO.setProbabilidad("Sin Asignar");
            }else{
                registroVO.setProbabilidad(((Spinner) findViewById(R.id.spinnerProbabilidad)).getSelectedItem().toString());
            }
        }catch (Exception e){
            registroVO.setProbabilidad("");
        }

        try{
            registroVO.setTipoInteres(((Spinner) findViewById(R.id.spinnerTipoInteres)).getSelectedItemPosition() + "");
        }catch (Exception e){
            registroVO.setTipoInteres("0");
        }

        try{
            registroVO.setHorarios(((Spinner) findViewById(R.id.spinnerHorario)).getSelectedItemPosition() + "");
        }catch (Exception e){
            registroVO.setHorarios("0");
        }

        try{
            registroVO.setLugar(getIdOrigin());
        }catch (Exception e){
            registroVO.setLugar("0");
        }

        try{
            registroVO.setEvento(getIdEvent());
        }catch (Exception e){
            registroVO.setEvento("0");
        }

        if( guardarRegistro( registroVO )){
            if( listEmail.size() > 0 ){
                guardarEmail();
            }
            if( listTelephone.size() > 0){
                guardarTelefonos();
            }
            guardarIntereses();
            Toast.makeText(getMContext(), "Registro Guardado Correctamente", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getMContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(getMContext(), "Error al Guardar Datos", Toast.LENGTH_LONG).show();
        }


    }

    private boolean guardarRegistro( NewRegistroVO registro ){
        SQLiteOpenHelper helper = EduscoreOpenHelper.getInstance(getMContext());
        SQLiteDatabase dataBase = helper.getWritableDatabase();
        try {
            String sql = "INSERT INTO " + EduscoreOpenHelper.TABLE_NEW_REGISTRO +
                    " (idRegistro, nombre, apellidos, tipoIntereses, horario, medio, lugar, evento, probabilidad, " +
                    " paisResidencia, estadoResidencia, ciudadResidencia, calle, numExt, numInt, " +
                    " colonia, cp, curp, paisOrigen, estadoOrigen, ciudadOrigen, fechaNacimiento) " +
                    "VALUES (" + registro.toString() + ")";
            dataBase.beginTransaction();
            dataBase.execSQL(sql);
            dataBase.setTransactionSuccessful();
            dataBase.endTransaction();

        } catch (Exception e) {
            Log.e("Data Backup", "Error al Guardar Datos");
            return false;
        } finally {
            dataBase.close();
        }
        return true;
    }

    private void guardarEmail(){
        SQLiteOpenHelper helper = EduscoreOpenHelper.getInstance(getMContext());
        SQLiteDatabase dataBase = helper.getWritableDatabase();
        try {
            for( int i = 0; i < listEmail.size(); i++ ){
                String sql = "INSERT INTO " + EduscoreOpenHelper.TABLE_NEW_CORREOS +
                        " (idRegistro, correo) " +
                        "VALUES ( '" + registroVO.getIdRegistro() + "', '" + listEmail.get(i) + "' )";
                dataBase.beginTransaction();
                dataBase.execSQL(sql);
                dataBase.setTransactionSuccessful();
                dataBase.endTransaction();
            }

        } catch (Exception e) {
            Log.e("Data Backup", "Error al Guardar Email");
        } finally {
            dataBase.close();
        }
    }

    private void guardarTelefonos(){
        SQLiteOpenHelper helper = EduscoreOpenHelper.getInstance(getMContext());
        SQLiteDatabase dataBase = helper.getWritableDatabase();
        try {
            for( int i = 0; i < listTelephone.size(); i++ ){
                String sql = "INSERT INTO " + EduscoreOpenHelper.TABLE_NEW_TELEFONOS +
                        " (idRegistro, telefono, tipo) " +
                        "VALUES ('" + registroVO.getIdRegistro() + "', '" + listTelephone.get(i) +
                        "', '" + listTipos.get(i) + "' )";
                dataBase.beginTransaction();
                dataBase.execSQL(sql);
                dataBase.setTransactionSuccessful();
                dataBase.endTransaction();
            }

        } catch (Exception e) {
            Log.e("Data Backup", "Error al Guardar Telefono");
        } finally {
            dataBase.close();
        }
    }

    private void guardarIntereses(){
        SQLiteOpenHelper helper = EduscoreOpenHelper.getInstance(getMContext());
        SQLiteDatabase dataBase = helper.getWritableDatabase();
        try {
            for( int i = 0; i < listIntereses.size(); i++ ){
                String sql = "INSERT INTO " + EduscoreOpenHelper.TABLE_NEW_INTERESES +
                        " (idRegistro, idCareer) " +
                        "VALUES ('" + registroVO.getIdRegistro() + "', '" + listIntereses.get(i) + "' )";
                dataBase.beginTransaction();
                dataBase.execSQL(sql);
                dataBase.setTransactionSuccessful();
                dataBase.endTransaction();
            }

        } catch (Exception e) {
            Log.e("Data Backup", "Error al Guardar Intereses");
        } finally {
            dataBase.close();
        }
    }

    private String capitalizado(String cadena){
        String newString = "";
        String cadenas[] = cadena.split(" ");

        for(int i = 0; i < cadenas.length; i++ ){
            String tem = "";
            if( cadenas[i].charAt(0) > 96){
                for( int j = 0; j < cadenas[i].length(); j++ ){
                    if( j == 0 ){
                        tem += (char)(cadenas[i].charAt(j) - 32) ;
                        continue;
                    }
                    tem += cadenas[i].charAt(j);
                }
            }
            newString += tem + " ";
        }
        return newString;
    }

    private String getIdMedia(){
        String media = ((Spinner) findViewById(R.id.spinnerMedio)).getSelectedItem().toString();
        if( media.equals("Seleccione un medio.")){
            return "null";
        }
        List<MediaVO> mediaVO;
        CommonDAO<MediaVO> mediaDAO = new CommonDAO<>(getMContext(), MediaVO.class, EduscoreOpenHelper.TABLE_DAT_MEDIA);
        mediaDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_DAT_MEDIA +
                " WHERE name = '" + media + "' AND status = '1'";
        mediaVO = mediaDAO.runQuery( sql );
        return mediaVO.get(0).getIdMedia();
    }

    private String getIdOrigin(){
        String origin = ((Spinner) findViewById(R.id.spinnerProcedencia)).getSelectedItem().toString();
        List<OriginVO> originVO = new ArrayList<>();
        CommonDAO<OriginVO> originDAO = new CommonDAO<>(getMContext(), OriginVO.class, EduscoreOpenHelper.TABLE_DAT_ORIGIN);
        originDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_DAT_ORIGIN +
                " WHERE name = '" + origin + "' AND status = '1'";
        originVO = originDAO.runQuery(sql );
        return originVO.get(0).getIdOrigen();
    }

    private String getIdEvent(){
        String event = ((Spinner) findViewById(R.id.spinnerEvento)).getSelectedItem().toString();
        List<EventVO> eventVO = new ArrayList<>();
        CommonDAO<EventVO> eventDAO = new CommonDAO<>(getMContext(), EventVO.class, EduscoreOpenHelper.TABLE_DAT_EVENTS);
        eventDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_DAT_EVENTS +
                " WHERE name = '" + event + "' AND status = '1'";
        eventVO = eventDAO.runQuery( sql );
        return eventVO.get(0).getIdEvent();
    }

    private String getIdCampus(){
        SharedPreferences shared = getMContext().getSharedPreferences("eduscoreUser", getMContext().MODE_PRIVATE);
        String idPerson = shared.getString("idPerson", "1");
        List<PersonVO> personVO = new ArrayList<>();
        CommonDAO<PersonVO> personDAO = new CommonDAO<>(getMContext(), PersonVO.class, EduscoreOpenHelper.TABLE_DAT_PERSON);
        personDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_DAT_PERSON +
                " WHERE idPerson = '" + idPerson + "' AND status = '1'";
        personVO = personDAO.runQuery(sql );
        return personVO.get(0).getIdCampus();
    }


    private String getIdPais(String name){
        List<CountryVO> countryVO = new ArrayList<>();
        CommonDAO<CountryVO> countryDAO = new CommonDAO<>(getMContext(), CountryVO.class, EduscoreOpenHelper.TABLE_DAT_COUNTRY);
        countryDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_DAT_COUNTRY +
                " WHERE name = '" + name + "'";
        countryVO = countryDAO.runQuery(sql );
        return countryVO.get(0).getIdContry();
    }

    private String getIdEstado(String name){
        List<RegionVO> regionVO = new ArrayList<>();
        CommonDAO<RegionVO> regionDAO = new CommonDAO<>(getMContext(), RegionVO.class, EduscoreOpenHelper.TABLE_DAT_REGION);
        regionDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_DAT_REGION +
                " WHERE name = '" + name + "'";
        regionVO = regionDAO.runQuery(sql );
        return regionVO.get(0).getIdRegion();
    }

    private String getIdCiudad(String name){
        List<CityVO> cityVO = new ArrayList<>();
        CommonDAO<CityVO> cityDAO = new CommonDAO<>(getMContext(), CityVO.class, EduscoreOpenHelper.TABLE_DAT_CITY);
        cityDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_DAT_CITY +
                " WHERE name = '" + name + "'";
        cityVO = cityDAO.runQuery( sql );
        return cityVO.get(0).getIdCity();
    }

    private void saveServer(){
        InsertModel.getInstance().setContextActivity(this);
        InsertModel.getInstance().startBackup();

        /*List<NewRegistroVO> newRegistroVO = new ArrayList<>();
        List<EmailVO> newCorreoVO = new ArrayList<>();
        List<NewTelefonosVO> newTelefonoVO = new ArrayList<>();
        List<NewInteresesVO> newInteresVO = new ArrayList<>();
        CommonDAO<NewRegistroVO> newRegistronDAO = new CommonDAO<>(getMContext(), NewRegistroVO.class, EduscoreOpenHelper.TABLE_NEW_REGISTRO);
        CommonDAO<EmailVO> newCorreoDAO = new CommonDAO<>(getMContext(), EmailVO.class, EduscoreOpenHelper.TABLE_DAT_EMAIL);
        CommonDAO<NewTelefonosVO> newTelefonoDAO = new CommonDAO<>(getMContext(), NewTelefonosVO.class, EduscoreOpenHelper.TABLE_NEW_TELEFONOS);
        CommonDAO<NewInteresesVO> newInteresDAO = new CommonDAO<>(getMContext(), NewInteresesVO.class, EduscoreOpenHelper.TABLE_NEW_INTERESES);
        newRegistronDAO.open();
        newCorreoDAO.open();
        newTelefonoDAO.open();
        newInteresDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_NEW_REGISTRO;
        String sql1 = "SELECT * FROM " + EduscoreOpenHelper.TABLE_DAT_EMAIL;
        String sql2 = "SELECT * FROM " + EduscoreOpenHelper.TABLE_NEW_TELEFONOS;
        String sql3 = "SELECT * FROM " + EduscoreOpenHelper.TABLE_NEW_INTERESES;
        newRegistroVO = newRegistronDAO.runQuery( sql );
        newCorreoVO = newCorreoDAO.runQuery( sql1 );
        newTelefonoVO = newTelefonoDAO.runQuery( sql2 );
        newInteresVO = newInteresDAO.runQuery( sql3 );/**/

    }
}
