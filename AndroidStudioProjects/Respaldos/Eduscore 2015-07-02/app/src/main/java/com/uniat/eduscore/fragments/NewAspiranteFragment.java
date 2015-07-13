package com.uniat.eduscore.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.uniat.eduscore.MainActivity;
import com.uniat.eduscore.adapters.AdapterFormulario;
import com.uniat.eduscore.db.CommonDAO;
import com.uniat.eduscore.db.EduscoreOpenHelper;
import com.uniat.eduscore.interfaces.IJSONResponseHandler;
import com.uniat.eduscore.model.BackupModel;
import com.uniat.eduscore.model.InsertModel;
import com.uniat.eduscore.net.RPCHandler;
import com.uniat.eduscore.vo.CityVO;
import com.uniat.eduscore.vo.CountryVO;
import com.uniat.eduscore.vo.EventVO;
import com.uniat.eduscore.vo.MediaVO;
import com.uniat.eduscore.vo.NewRegistroVO;
import com.uniat.eduscore.vo.OriginVO;
import com.uniat.eduscore.vo.PersonVO;
import com.uniat.eduscore.vo.RegionVO;
import com.university3dmx.eduscore.R;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Admin on 25/06/2015.
 */
public class NewAspiranteFragment extends Fragment {

    public boolean isCorrectSesion;

    static ViewPager vViewPager;
    static AdapterFormulario adapterFormulario;
    public ImageButton imgBtn1;
    public ImageButton imgBtn2;
    public ImageButton imgBtn3;
    public Button btnActualizar;
    public NewRegistroVO registroVO;
    public ArrayList<String> listEmail;
    public ArrayList<String> listTelephone;
    public ArrayList<String> listTipos;
    public ArrayList<String> listIntereses;

    public static NewAspiranteFragment mthis;

    public String idRegistro;

    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_registro, container, false);

        mthis = this;
        listEmail = new ArrayList<String>();
        listTelephone = new ArrayList<String>();
        listTipos = new ArrayList<String>();
        listIntereses = new ArrayList<String>();

        adapterFormulario = new AdapterFormulario(MainActivity.mthis.getSupportFragmentManager(), 3);
        imgBtn1 = (ImageButton) rootView.findViewById(R.id.imgBtnSelectedView1);
        imgBtn2 = (ImageButton) rootView.findViewById(R.id.imgBtnSelectedView2);
        imgBtn3 = (ImageButton) rootView.findViewById(R.id.imgBtnSelectedView3);
        btnActualizar = (Button) rootView.findViewById(R.id.btnActualizar);
        adapterFormulario.setImgBtns(imgBtn1, imgBtn2, imgBtn3);
        vViewPager = (ViewPager) rootView.findViewById(R.id.pager);
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

        return rootView;
    }

    public void onClickActualizar(View pView){
        BackupModel.getInstance().setContextActivity(MainActivity.mthis);

        if (!MainActivity.mthis.wifiOn) {
            MainActivity.mthis.getMessageBuilder().setMessage("");
            MainActivity.mthis.getMessageBuilder().create().show();
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
        isCorrectPassword();
        if( !isCorrectSesion ){
            Toast.makeText(MainActivity.mthis.getMContext(), getString(R.string.msj_pass_no_existe), Toast.LENGTH_LONG).show();
            return;
        }
        registroVO = new NewRegistroVO();
        idRegistro = ((int)( Math.random()*1000000 )) + "";
        registroVO.setIdRegistro(idRegistro);
        try{
            registroVO.setNombre(capitalizado(((EditText) rootView.findViewById(R.id.editTextName)).getText().toString()));
        }catch (Exception e){
            Toast.makeText(MainActivity.mthis.getMContext(), getString(R.string.msj_name_obligatorio), Toast.LENGTH_LONG).show();
            return;
        }

        try{
            registroVO.setApellidos(capitalizado(((EditText) rootView.findViewById(R.id.editTextLastName)).getText().toString()));
        }catch (Exception e){
            Toast.makeText(MainActivity.mthis.getMContext(), getString(R.string.msj_lastname_obligatorio), Toast.LENGTH_LONG).show();
            return;
        }

        if( listEmail.size() == 0 && listTelephone.size() == 0){
            Toast.makeText(MainActivity.mthis.getMContext(), getString(R.string.msj_correo_telefono), Toast.LENGTH_LONG).show();
            return;
        }

        registroVO.setMedio(getIdMedia());

        if( registroVO.getMedio().equals("null") ) {
            Toast.makeText(MainActivity.mthis.getMContext(), getString(R.string.msj_media_obligatorio), Toast.LENGTH_LONG).show();
            return;
        }

        try{
            registroVO.setFechaNacimiento(((TextView) rootView.findViewById(R.id.TextNacimiento)).getText().toString());
        }catch (Exception e){
            registroVO.setFechaNacimiento("");
        }

        try{
            registroVO.setPaisOrigen(getIdPais(((Spinner) rootView.findViewById(R.id.spinnerPaisOrigen)).getSelectedItem().toString()));
        }catch (Exception e){
            registroVO.setPaisOrigen("0");
        }

        try{
            registroVO.setEstadoOrigen(getIdEstado(((Spinner) rootView.findViewById(R.id.spinnerEstadoOrigen)).getSelectedItem().toString()));
            if( registroVO.getEstadoOrigen().equals("null") ){
                registroVO.setEstadoOrigen("0");
            }
        }catch (Exception e){
            registroVO.setEstadoOrigen("0");
        }

        try{
            registroVO.setCiudadOrigen(getIdCiudad(((Spinner) rootView.findViewById(R.id.spinnerCiudadOrigen)).getSelectedItem().toString()));
            if( registroVO.getCiudadOrigen().equals("null") ){
                registroVO.setCiudadOrigen("0");
            }
        }catch (Exception e){
            registroVO.setCiudadOrigen("0");
        }

        try{
            registroVO.setPaisResidencia(getIdPais(((Spinner) rootView.findViewById(R.id.spinnerPaisResidencia)).getSelectedItem().toString()));
        }catch (Exception e){
            registroVO.setPaisResidencia("0");
        }

        try{
            registroVO.setEstadoResidencia(getIdEstado(((Spinner) rootView.findViewById(R.id.spinnerEstadoResidencia)).getSelectedItem().toString()));
        }catch (Exception e){
            registroVO.setEstadoResidencia("0");
        }

        try{
            registroVO.setCiudadResidencia(getIdCiudad(((Spinner) rootView.findViewById(R.id.spinnerCiudadResidencia)).getSelectedItem().toString()));
        }catch (Exception e){
            registroVO.setCiudadResidencia("0");
        }

        try{
            registroVO.setCurp(mayus(((EditText) rootView.findViewById(R.id.editTextCurp)).getText().toString()));
        }catch (Exception e){
            registroVO.setCurp("");
        }

        try{
            registroVO.setCalle(((EditText) rootView.findViewById(R.id.editTextCalle)).getText().toString());
        }catch (Exception e){
            registroVO.setCalle("");
        }

        try{
            registroVO.setNumExt(((EditText) rootView.findViewById(R.id.editTextNumExt)).getText().toString());
        }catch (Exception e){
            registroVO.setNumExt("");
        }

        try{
            registroVO.setNumInt(((EditText) rootView.findViewById(R.id.editTextNumInt)).getText().toString());
        }catch (Exception e){
            registroVO.setNumInt("");
        }

        try{
            registroVO.setColonia(((EditText) rootView.findViewById(R.id.editTextColonia)).getText().toString());
        }catch (Exception e){
            registroVO.setColonia("");
        }

        try{
            registroVO.setCp(((EditText) rootView.findViewById(R.id.editTextCP)).getText().toString());
        }catch (Exception e){
            registroVO.setCp("");
        }

        try{
            if( ((Spinner) rootView.findViewById(R.id.spinnerProbabilidad)).getSelectedItem().toString().equals(getString(R.string.text_probabilidad_select))){
                registroVO.setProbabilidad("Sin Asignar");
            }else{
                registroVO.setProbabilidad(((Spinner) rootView.findViewById(R.id.spinnerProbabilidad)).getSelectedItem().toString());
            }
        }catch (Exception e){
            registroVO.setProbabilidad("");
        }

        try{
            registroVO.setTipoInteres(((Spinner) rootView.findViewById(R.id.spinnerTipoInteres)).getSelectedItemPosition() + "");
        }catch (Exception e){
            registroVO.setTipoInteres("0");
        }

        try{
            registroVO.setHorarios(((Spinner) rootView.findViewById(R.id.spinnerHorario)).getSelectedItemPosition() + "");
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
            Toast.makeText(MainActivity.mthis.getMContext(), getString(R.string.msj_registro_ok), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.mthis.getMContext(), MainActivity.class);
            startActivity(intent);
            MainActivity.mthis.finish();
        }else{
            Toast.makeText(MainActivity.mthis.getMContext(), getString(R.string.msj_registro_fail), Toast.LENGTH_LONG).show();
        }


    }

    public void isCorrectPassword(){
        SharedPreferences shared = MainActivity.mthis.getMContext().getSharedPreferences("eduscoreUser", MainActivity.mthis.getMContext().MODE_PRIVATE);
        String user = shared.getString("user", "00");
        String pass = shared.getString("password", "00");
        RPCHandler rpc = new RPCHandler(new IJSONResponseHandler() {
            @Override
            public void onResponse(JSONObject jsonResponse) throws Exception {
                isCorrectSesion = jsonResponse.has("usuario") ? true : false;
            }

            @Override
            public void onRequest() {

            }

            @Override
            public void onException(Exception e) {

            }
        });
        rpc.execute(RPCHandler.OPERATION_LOGIN,
                new BasicNameValuePair("user", user),
                new BasicNameValuePair("password", pass));
    }

    public boolean is_CURP(String curp){
        String regex =
                "[A-Z]{1}[AEIOU]{1}[A-Z]{2}[0-9]{2}" +
                        "(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])" +
                        "[HM]{1}" +
                        "(AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)" +
                        "[B-DF-HJ-NP-TV-Z]{3}" +
                        "[0-9A-Z]{1}[0-9]{1}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(curp);
        if(m.matches()){
            return true;
        }
        return false;

    }

    public boolean guardarRegistro( NewRegistroVO registro ){
        SQLiteOpenHelper helper = EduscoreOpenHelper.getInstance(MainActivity.mthis.getMContext());
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
            Log.e("Data Backup", getString(R.string.msj_registro_fail));
            return false;
        } finally {
            dataBase.close();
        }
        return true;
    }

    public void guardarEmail(){
        SQLiteOpenHelper helper = EduscoreOpenHelper.getInstance(MainActivity.mthis.getMContext());
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
            Log.e("Data Backup", getString(R.string.msj_email_fail));
        } finally {
            dataBase.close();
        }
    }

    public void guardarTelefonos(){
        SQLiteOpenHelper helper = EduscoreOpenHelper.getInstance(MainActivity.mthis.getMContext());
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
            Log.e("Data Backup", getString(R.string.msj_telefono_fail));
        } finally {
            dataBase.close();
        }
    }

    public void guardarIntereses(){
        SQLiteOpenHelper helper = EduscoreOpenHelper.getInstance(MainActivity.mthis.getMContext());
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
            Log.e("Data Backup", getString(R.string.msj_interes_fail));
        } finally {
            dataBase.close();
        }
    }

    public String capitalizado(String cadena){
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
        newString = newString.substring(0, newString.length() - 1);
        return newString;
    }

    public String mayus(String cadena){

        String newString = "";

        for(int i = 0; i < cadena.length(); i++ ){
            String tem = "";
            if( cadena.charAt(i) > 96){
                tem += (char)(cadena.charAt(i) - 32) ;
            }else{
                tem += cadena.charAt(i);
            }
            newString += tem + " ";
        }
        return newString;
    }

    public String getIdMedia(){
        String media = ((Spinner) rootView.findViewById(R.id.spinnerMedio)).getSelectedItem().toString();
        if( media.equals(getString(R.string.text_medio_selected))){
            return "null";
        }
        List<MediaVO> mediaVO;
        CommonDAO<MediaVO> mediaDAO = new CommonDAO<MediaVO>(MainActivity.mthis.getMContext(), MediaVO.class, EduscoreOpenHelper.TABLE_DAT_MEDIA);
        mediaDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_DAT_MEDIA +
                " WHERE name = '" + media + "' AND status = '1'";
        mediaVO = mediaDAO.runQuery( sql );
        return mediaVO.get(0).getIdMedia();
    }

    public String getIdOrigin(){
        String origin = ((Spinner) rootView.findViewById(R.id.spinnerProcedencia)).getSelectedItem().toString();
        List<OriginVO> originVO = new ArrayList<OriginVO>();
        CommonDAO<OriginVO> originDAO = new CommonDAO<OriginVO>(MainActivity.mthis.getMContext(), OriginVO.class, EduscoreOpenHelper.TABLE_DAT_ORIGIN);
        originDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_DAT_ORIGIN +
                " WHERE name = '" + origin + "' AND status = '1'";
        originVO = originDAO.runQuery(sql );
        return originVO.get(0).getIdOrigen();
    }

    public String getIdEvent(){
        String event = ((Spinner) rootView.findViewById(R.id.spinnerEvento)).getSelectedItem().toString();
        List<EventVO> eventVO = new ArrayList<EventVO>();
        CommonDAO<EventVO> eventDAO = new CommonDAO<EventVO>(MainActivity.mthis.getMContext(), EventVO.class, EduscoreOpenHelper.TABLE_DAT_EVENTS);
        eventDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_DAT_EVENTS +
                " WHERE name = '" + event + "' AND status = '1'";
        eventVO = eventDAO.runQuery( sql );
        return eventVO.get(0).getIdEvent();
    }

    public String getIdCampus(){
        SharedPreferences shared = MainActivity.mthis.getMContext().getSharedPreferences("eduscoreUser", MainActivity.mthis.getMContext().MODE_PRIVATE);
        String idPerson = shared.getString("idPerson", "1");
        List<PersonVO> personVO = new ArrayList<PersonVO>();
        CommonDAO<PersonVO> personDAO = new CommonDAO<PersonVO>(MainActivity.mthis.getMContext(), PersonVO.class, EduscoreOpenHelper.TABLE_DAT_PERSON);
        personDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_DAT_PERSON +
                " WHERE idPerson = '" + idPerson + "' AND status = '1'";
        personVO = personDAO.runQuery(sql );
        return personVO.get(0).getIdCampus();
    }


    public String getIdPais(String name){
        List<CountryVO> countryVO = new ArrayList<CountryVO>();
        CommonDAO<CountryVO> countryDAO = new CommonDAO<CountryVO>(MainActivity.mthis.getMContext(), CountryVO.class, EduscoreOpenHelper.TABLE_DAT_COUNTRY);
        countryDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_DAT_COUNTRY +
                " WHERE name = '" + name + "'";
        countryVO = countryDAO.runQuery(sql );
        return countryVO.get(0).getIdContry();
    }

    public String getIdEstado(String name){
        List<RegionVO> regionVO = new ArrayList<RegionVO>();
        CommonDAO<RegionVO> regionDAO = new CommonDAO<RegionVO>(MainActivity.mthis.getMContext(), RegionVO.class, EduscoreOpenHelper.TABLE_DAT_REGION);
        regionDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_DAT_REGION +
                " WHERE name = '" + name + "'";
        regionVO = regionDAO.runQuery(sql );
        return regionVO.get(0).getIdRegion();
    }

    public String getIdCiudad(String name){
        List<CityVO> cityVO = new ArrayList<CityVO>();
        CommonDAO<CityVO> cityDAO = new CommonDAO<CityVO>(MainActivity.mthis.getMContext(), CityVO.class, EduscoreOpenHelper.TABLE_DAT_CITY);
        cityDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_DAT_CITY +
                " WHERE name = '" + name + "'";
        cityVO = cityDAO.runQuery( sql );
        return cityVO.get(0).getIdCity();
    }

    public void saveServer(){
        InsertModel.getInstance().setContextActivity(MainActivity.mthis);
        InsertModel.getInstance().startBackup();
    }

}
