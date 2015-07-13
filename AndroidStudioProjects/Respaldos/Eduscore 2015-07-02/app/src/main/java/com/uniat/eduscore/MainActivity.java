package com.uniat.eduscore;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.uniat.eduscore.adapters.NavDrawerListAdapter;
import com.uniat.eduscore.fragments.NewAspiranteFragment;
import com.uniat.eduscore.fragments.ReportesFragment;
import com.uniat.eduscore.util.ExtendedFragmentActivity;
import com.uniat.eduscore.util.NavDrawerItem;
import com.uniat.eduscore.vo.NewRegistroVO;
import com.university3dmx.eduscore.R;

import java.util.ArrayList;

/**
 * Created by Admin on 06/04/2015.
 */
    public class MainActivity extends ExtendedFragmentActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList navDrawerItems;
    private NavDrawerListAdapter adapter;

    public static MainActivity mthis;

    private TextView textTitle;

    @Override
    protected void onResume() {
        super.onResume();
        if( !wifiOn ){
            Toast.makeText(getMContext(), getString(R.string.msj_error_internet), Toast.LENGTH_LONG).show();
        }else{
        }
    }

    @Override
    public void onBackPressed() {
        if( mDrawerLayout.isDrawerOpen(mDrawerList) ){
            mDrawerLayout.closeDrawer(mDrawerList);
        }else{
            super.onBackPressed();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    public void onClickGuardar(View view){
        if( wifiOn ){
            NewAspiranteFragment.mthis.isCorrectPassword();
            if( !NewAspiranteFragment.mthis.isCorrectSesion ){
                Toast.makeText(getMContext(), getString(R.string.msj_pass_no_existe), Toast.LENGTH_LONG).show();
                return;
            }
        }
        NewAspiranteFragment.mthis.registroVO = new NewRegistroVO();
        NewAspiranteFragment.mthis.idRegistro = ((int)( Math.random()*1000000 )) + "";
        NewAspiranteFragment.mthis.registroVO.setIdRegistro(NewAspiranteFragment.mthis.idRegistro);
        try{
            NewAspiranteFragment.mthis.registroVO.setNombre(NewAspiranteFragment.mthis.capitalizado(((EditText) findViewById(R.id.editTextName)).getText().toString()));
        }catch (Exception e){
            Toast.makeText(getMContext(), getString(R.string.msj_name_obligatorio), Toast.LENGTH_LONG).show();
            return;
        }

        try{
            NewAspiranteFragment.mthis.registroVO.setApellidos(NewAspiranteFragment.mthis.capitalizado(((EditText) findViewById(R.id.editTextLastName)).getText().toString()));
        }catch (Exception e){
            Toast.makeText(getMContext(), getString(R.string.msj_lastname_obligatorio), Toast.LENGTH_LONG).show();
            return;
        }

        if( NewAspiranteFragment.mthis.listEmail.size() == 0 && NewAspiranteFragment.mthis.listTelephone.size() == 0){
            Toast.makeText(getMContext(), getString(R.string.msj_correo_telefono), Toast.LENGTH_LONG).show();
            return;
        }

        NewAspiranteFragment.mthis.registroVO.setMedio(NewAspiranteFragment.mthis.getIdMedia());

        if( NewAspiranteFragment.mthis.registroVO.getMedio().equals("null") ) {
            Toast.makeText(getMContext(), getString(R.string.msj_media_obligatorio), Toast.LENGTH_LONG).show();
            return;
        }

        try{
            NewAspiranteFragment.mthis.registroVO.setFechaNacimiento(((TextView) findViewById(R.id.TextNacimiento)).getText().toString());
        }catch (Exception e){
            NewAspiranteFragment.mthis.registroVO.setFechaNacimiento("");
        }

        try{
            NewAspiranteFragment.mthis.registroVO.setPaisOrigen(NewAspiranteFragment.mthis.getIdPais(((Spinner) findViewById(R.id.spinnerPaisOrigen)).getSelectedItem().toString()));
        }catch (Exception e){
            NewAspiranteFragment.mthis.registroVO.setPaisOrigen("0");
        }

        try{
            NewAspiranteFragment.mthis.registroVO.setEstadoOrigen(NewAspiranteFragment.mthis.getIdEstado(((Spinner) findViewById(R.id.spinnerEstadoOrigen)).getSelectedItem().toString()));
            if( NewAspiranteFragment.mthis.registroVO.getEstadoOrigen().equals("null") ){
                NewAspiranteFragment.mthis.registroVO.setEstadoOrigen("0");
            }
        }catch (Exception e){
            NewAspiranteFragment.mthis.registroVO.setEstadoOrigen("0");
        }

        try{
            NewAspiranteFragment.mthis.registroVO.setCiudadOrigen(NewAspiranteFragment.mthis.getIdCiudad(((Spinner) findViewById(R.id.spinnerCiudadOrigen)).getSelectedItem().toString()));
            if( NewAspiranteFragment.mthis.registroVO.getCiudadOrigen().equals("null") ){
                NewAspiranteFragment.mthis.registroVO.setCiudadOrigen("0");
            }
        }catch (Exception e){
            NewAspiranteFragment.mthis.registroVO.setCiudadOrigen("0");
        }

        try{
            NewAspiranteFragment.mthis.registroVO.setPaisResidencia(NewAspiranteFragment.mthis.getIdPais(((Spinner) findViewById(R.id.spinnerPaisResidencia)).getSelectedItem().toString()));
        }catch (Exception e){
            NewAspiranteFragment.mthis.registroVO.setPaisResidencia("0");
        }

        try{
            NewAspiranteFragment.mthis.registroVO.setEstadoResidencia(NewAspiranteFragment.mthis.getIdEstado(((Spinner) findViewById(R.id.spinnerEstadoResidencia)).getSelectedItem().toString()));
        }catch (Exception e){
            NewAspiranteFragment.mthis.registroVO.setEstadoResidencia("0");
        }

        try{
            NewAspiranteFragment.mthis.registroVO.setCiudadResidencia(NewAspiranteFragment.mthis.getIdCiudad(((Spinner) findViewById(R.id.spinnerCiudadResidencia)).getSelectedItem().toString()));
        }catch (Exception e){
            NewAspiranteFragment.mthis.registroVO.setCiudadResidencia("0");
        }

        try{
            NewAspiranteFragment.mthis.registroVO.setCurp(NewAspiranteFragment.mthis.mayus(((EditText) findViewById(R.id.editTextCurp)).getText().toString()));
        }catch (Exception e){
            NewAspiranteFragment.mthis.registroVO.setCurp("");
        }

        try{
            NewAspiranteFragment.mthis.registroVO.setCalle(((EditText) findViewById(R.id.editTextCalle)).getText().toString());
        }catch (Exception e){
            NewAspiranteFragment.mthis.registroVO.setCalle("");
        }

        try{
            NewAspiranteFragment.mthis.registroVO.setNumExt(((EditText) findViewById(R.id.editTextNumExt)).getText().toString());
        }catch (Exception e){
            NewAspiranteFragment.mthis.registroVO.setNumExt("");
        }

        try{
            NewAspiranteFragment.mthis.registroVO.setNumInt(((EditText) findViewById(R.id.editTextNumInt)).getText().toString());
        }catch (Exception e){
            NewAspiranteFragment.mthis.registroVO.setNumInt("");
        }

        try{
            NewAspiranteFragment.mthis.registroVO.setColonia(((EditText) findViewById(R.id.editTextColonia)).getText().toString());
        }catch (Exception e){
            NewAspiranteFragment.mthis.registroVO.setColonia("");
        }

        try{
            NewAspiranteFragment.mthis.registroVO.setCp(((EditText) findViewById(R.id.editTextCP)).getText().toString());
        }catch (Exception e){
            NewAspiranteFragment.mthis.registroVO.setCp("");
        }

        try{
            if( ((Spinner) findViewById(R.id.spinnerProbabilidad)).getSelectedItem().toString().equals(getString(R.string.text_probabilidad_select))){
                NewAspiranteFragment.mthis.registroVO.setProbabilidad("Sin Asignar");
            }else{
                NewAspiranteFragment.mthis.registroVO.setProbabilidad(((Spinner) findViewById(R.id.spinnerProbabilidad)).getSelectedItem().toString());
            }
        }catch (Exception e){
            NewAspiranteFragment.mthis.registroVO.setProbabilidad("");
        }

        try{
            NewAspiranteFragment.mthis.registroVO.setTipoInteres(((Spinner) findViewById(R.id.spinnerTipoInteres)).getSelectedItemPosition() + "");
        }catch (Exception e){
            NewAspiranteFragment.mthis.registroVO.setTipoInteres("0");
        }

        try{
            NewAspiranteFragment.mthis.registroVO.setHorarios(((Spinner) findViewById(R.id.spinnerHorario)).getSelectedItemPosition() + "");
        }catch (Exception e){
            NewAspiranteFragment.mthis.registroVO.setHorarios("0");
        }

        try{
            NewAspiranteFragment.mthis.registroVO.setLugar(NewAspiranteFragment.mthis.getIdOrigin());
        }catch (Exception e){
            NewAspiranteFragment.mthis.registroVO.setLugar("0");
        }

        try{
            NewAspiranteFragment.mthis.registroVO.setEvento(NewAspiranteFragment.mthis.getIdEvent());
        }catch (Exception e){
            NewAspiranteFragment.mthis.registroVO.setEvento("0");
        }

        if( NewAspiranteFragment.mthis.guardarRegistro(NewAspiranteFragment.mthis.registroVO)){
            if( NewAspiranteFragment.mthis.listEmail.size() > 0 ){
                NewAspiranteFragment.mthis.guardarEmail();
            }
            if( NewAspiranteFragment.mthis.listTelephone.size() > 0){
                NewAspiranteFragment.mthis.guardarTelefonos();
            }
            NewAspiranteFragment.mthis.guardarIntereses();
            Toast.makeText(getMContext(), getString(R.string.msj_registro_ok), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getMContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(getMContext(), getString(R.string.msj_registro_fail), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mthis = this;

        textTitle = (TextView) findViewById(R.id.textTitle);
        textTitle.setText(getString(R.string.title_new_registro));

        if( !getResources().getBoolean(R.bool.isTablet) ){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else{

        }

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList();

        // agregar un nuevo item al menu deslizante
        // Seccion1
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Seccion2
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Seccion3
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Seccion4
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));

        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.selector_menu, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
    }

    public void onToggleClicked(View view) {

        if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
            // Disable vibrate
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // Enable vibrate
            mDrawerLayout.openDrawer(mDrawerList);
        }
    }

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                textTitle.setText(getString(R.string.title_new_registro));
                fragment = new NewAspiranteFragment();
                break;
            case 1:
                textTitle.setText(getString(R.string.title_reportes));
                fragment = new ReportesFragment();
                break;
            case 2:
                //fragment = new Seccion3();
                break;
            case 3:
                //fragment = new Seccion4();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("Error", "MainActivity - Error cuando se creo el fragment");
        }
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}
