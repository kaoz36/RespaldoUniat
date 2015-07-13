package com.uniat.eduscore;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.uniat.eduscore.adapters.AdapterFormulario;
import com.uniat.eduscore.db.CommonDAO;
import com.uniat.eduscore.db.EduscoreOpenHelper;
import com.uniat.eduscore.model.BackupModel;
import com.uniat.eduscore.util.ExtendedFragmentActivity;
import com.uniat.eduscore.vo.EventVO;
import com.uniat.eduscore.vo.MediaVO;
import com.uniat.eduscore.vo.OriginVO;
import com.uniat.eduscore.vo.PersonVO;
import com.uniat.eduscore.vo.RegistroVO;
import com.university3dmx.eduscore.R;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 06/04/2015.
 */
    public class MainActivity extends ExtendedFragmentActivity {

    static ViewPager vViewPager;
    AdapterFormulario adapterFormulario;
    private ImageButton imgBtn1;
    private ImageButton imgBtn2;
    private ImageButton imgBtn3;
    private Button btnActualizar;
    RegistroVO registroVO;
    public ArrayList<String> listEmail;
    public ArrayList<String> listTelephone;
    public ArrayList<String> listTipos;
    public ArrayList<String> listIntereses;

    private ArrayList<RegistroVO> registrosVO;

    public static MainActivity mthis;


    @Override
    protected void onResume() {
        super.onResume();
        if( !wifiOn ){
            Toast.makeText(getMContext(), "Debes tener internet para poder Actualizar Datos.", Toast.LENGTH_LONG).show();
            btnActualizar.setVisibility(View.INVISIBLE);
        }else{
            btnActualizar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void init() {
        super.init();
        setContentView(R.layout.activity_main);
        mthis = this;
        registrosVO = new ArrayList<>();
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
        registroVO = new RegistroVO();
        try{
            registroVO.setNombre(capitalizado(((EditText) findViewById(R.id.editTextName)).getText().toString()));
        }catch (Exception e){
            Toast.makeText(getMContext(), "El Nombre es Obligatorio.", Toast.LENGTH_LONG).show();
            return;
        }


        /*if( registroVO.getNombre().length() < 2 ) {
        }*/

        registroVO.setCorreos(listEmail);
        registroVO.setTelefonos(listTelephone);
        registroVO.setTipos(listTipos);

        if( registroVO.getCorreos().size() == 0 && registroVO.getTelefonos().size() == 0){
            Toast.makeText(getMContext(), "Es Obligatorio poner al menos un Correo o Telefono.", Toast.LENGTH_LONG).show();
            return;
        }

        registroVO.setMedio(getIdMedia());

        if( registroVO.getMedio().equals("null") ) {
            Toast.makeText(getMContext(), "El Medio de informacion es Obligatorio.", Toast.LENGTH_LONG).show();
            return;
        }

        registroVO.setIntereses(listIntereses);
        if( registroVO.getIntereses().size() == 0 ){
            Toast.makeText(getMContext(), "Debes seleccionar por lo menos un Interes.", Toast.LENGTH_LONG).show();
            return;
        }

        registroVO.setApellidos(capitalizado(((EditText) findViewById(R.id.editTextLastName)).getText().toString()));
        registroVO.setFechaNacimiento(((TextView) findViewById(R.id.TextNacimiento)).getText().toString());
        registroVO.setPaisOrigen(((Spinner) findViewById(R.id.spinnerPaisOrigen)).getSelectedItem().toString());
        registroVO.setEstadoOrigen(((Spinner) findViewById(R.id.spinnerEstadoOrigen)).getSelectedItem().toString());
        registroVO.setCiudadOrigen(((Spinner) findViewById(R.id.spinnerCiudadOrigen)).getSelectedItem().toString());
        registroVO.setPaisResidencia(((Spinner) findViewById(R.id.spinnerPaisResidencia)).getSelectedItem().toString());
        registroVO.setEstadoResidencia(((Spinner) findViewById(R.id.spinnerEstadoResidencia)).getSelectedItem().toString());
        registroVO.setCiudadResidencia(((Spinner) findViewById(R.id.spinnerCiudadResidencia)).getSelectedItem().toString());
        registroVO.setCurp(((EditText) findViewById(R.id.editTextCurp)).getText().toString());
        registroVO.setCalle(((EditText) findViewById(R.id.editTextCalle)).getText().toString());
        registroVO.setNumExt(((EditText) findViewById(R.id.editTextNumExt)).getText().toString());
        registroVO.setNumInt(((EditText) findViewById(R.id.editTextCP)).getText().toString());
        registroVO.setColonia(((EditText) findViewById(R.id.editTextColonia)).getText().toString());
        registroVO.setCp(((EditText) findViewById(R.id.editTextCP)).getText().toString());
        registroVO.setProbabilidad(((Spinner) findViewById(R.id.spinnerProbabilidad)).getSelectedItem().toString());
        registroVO.setTipoInteres(((Spinner) findViewById(R.id.spinnerTipoInteres)).getSelectedItemPosition() + "");
        registroVO.setHorarios(((Spinner) findViewById(R.id.spinnerHorario)).getSelectedItemPosition() + "");

        registroVO.setLugar(getIdOrigin());
        registroVO.setEvento(getIdEvent());

        registrosVO.add(registroVO);

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
        List<MediaVO> mediaVO = new ArrayList<>();
        CommonDAO<MediaVO> mediaDAO = new CommonDAO<>(getMContext(), MediaVO.class, EduscoreOpenHelper.TABLE_DAT_MEDIA);
        mediaDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_DAT_MEDIA +
                " WHERE name = '" + media + "' AND status = '1'";
        mediaVO = mediaDAO.runQuery( sql );
        return mediaVO.get(0).getIdCampus();
    }

    private String getIdOrigin(){
        String origin = ((Spinner) findViewById(R.id.spinnerProcedencia)).getSelectedItem().toString();
        List<OriginVO> originVO = new ArrayList<>();
        CommonDAO<OriginVO> originDAO = new CommonDAO<>(getMContext(), OriginVO.class, EduscoreOpenHelper.TABLE_DAT_ORIGIN);
        originDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_DAT_ORIGIN +
                " WHERE name = '" + origin + "' AND status = '1'";
        originVO = originDAO.runQuery( sql );
        return originVO.get(0).getIdCampus();
    }

    private String getIdEvent(){
        String event = ((Spinner) findViewById(R.id.spinnerEvento)).getSelectedItem().toString();
        List<EventVO> eventVO = new ArrayList<>();
        CommonDAO<EventVO> eventDAO = new CommonDAO<>(getMContext(), EventVO.class, EduscoreOpenHelper.TABLE_DAT_EVENTS);
        eventDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_DAT_EVENTS +
                " WHERE name = '" + event + "' AND status = '1'";
        eventVO = eventDAO.runQuery( sql );
        return eventVO.get(0).getIdCampus();
    }

    private String getIdCampus(){
        SharedPreferences shared = getMContext().getSharedPreferences("eduscoreUser", getMContext().MODE_PRIVATE);
        String idPerson = shared.getString("idPerson", "1");
        List<PersonVO> personVO = new ArrayList<>();
        CommonDAO<PersonVO> personDAO = new CommonDAO<>(getMContext(), PersonVO.class, EduscoreOpenHelper.TABLE_DAT_PERSON);
        personDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_DAT_PERSON +
                " WHERE idPerson = '" + idPerson + "' AND status = '1'";
        personVO = personDAO.runQuery( sql );
        return personVO.get(0).getIdCampus();
    }


}
