package com.uniat.eduscore.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.uniat.eduscore.db.CommonDAO;
import com.uniat.eduscore.db.EduscoreOpenHelper;
import com.uniat.eduscore.util.DatePickerOnClickListener;
import com.uniat.eduscore.vo.CityVO;
import com.uniat.eduscore.vo.CountryVO;
import com.uniat.eduscore.vo.EventVO;
import com.uniat.eduscore.vo.RegionVO;
import com.university3dmx.eduscore.R;

import java.util.ArrayList;
import java.util.List;

import static android.widget.AdapterView.*;

/**
 * Created by Admin on 09/04/2015.
 */
public class PersonalesAdicionalesFragment extends Fragment {

    private TextView textDatePicker;
    private Spinner spinnerPaisResidencia;
    private Spinner spinnerEstadoResidencia;
    private Spinner spinnerCiudadResidencia;
    private Spinner spinnerPaisOrigen;
    private Spinner spinnerEstadoOrigen;
    private Spinner spinnerCiudadOrigen;

    private List<CountryVO> paisesVO;
    private List<RegionVO> estadosVO;
    private List<CityVO> ciudadesVO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_datos_personales_adicionales, null);
        init(rootView);
        loadSpinners();
        events();
        return rootView;
    }

    private void  init( View rootView ){
        textDatePicker = (TextView) rootView.findViewById(R.id.TextNacimiento);
        spinnerPaisResidencia = (Spinner) rootView.findViewById(R.id.spinnerPaisResidencia);
        spinnerEstadoResidencia = (Spinner) rootView.findViewById(R.id.spinnerEstadoResidencia);
        spinnerCiudadResidencia = (Spinner) rootView.findViewById(R.id.spinnerCiudadResidencia);
        spinnerPaisOrigen = (Spinner) rootView.findViewById(R.id.spinnerPaisOrigen);
        spinnerEstadoOrigen = (Spinner) rootView.findViewById(R.id.spinnerEstadoOrigen);
        spinnerCiudadOrigen = (Spinner) rootView.findViewById(R.id.spinnerCiudadOrigen);
    }

    private void events(){
        textDatePicker.setOnClickListener(new DatePickerOnClickListener(textDatePicker, getActivity()));

    }

    private void loadSpinners(){
        paisesVO = getPaisesList();
        paisesVO.add(0, new CountryVO("00", getResources().getString(R.string.text_select_pais)));

        String[] countrys = new String[paisesVO.size()];
        for (int i = 0; i < countrys.length; i++){
            countrys[i] = paisesVO.get(i).getName();
        }

        ArrayAdapter<String> spinnerCountryAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, countrys);
        spinnerCountryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPaisResidencia.setAdapter(spinnerCountryAdapter);
        spinnerPaisOrigen.setAdapter(spinnerCountryAdapter);

        spinnerPaisResidencia.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < paisesVO.size(); i++) {
                    if (paisesVO.get(i).getName().equals(parent.getSelectedItem().toString())) {
                        getEstados(paisesVO.get(i).getIdContry());
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

            private void getEstados(String idCountry) {
                estadosVO = getEstadosList(idCountry);
                estadosVO.add(0, new RegionVO("00", "00", getResources().getString(R.string.text_select_estado)));

                String[] regions = new String[estadosVO.size()];
                for (int i = 0; i < regions.length; i++) {
                    regions[i] = estadosVO.get(i).getName();
                }

                ArrayAdapter<String> spinnerRegionAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, regions);
                spinnerRegionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerEstadoResidencia.setAdapter(spinnerRegionAdapter);

            }
        });

        spinnerEstadoResidencia.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < estadosVO.size(); i++) {
                    if (estadosVO.get(i).getName().equals(parent.getSelectedItem().toString())) {
                        getCiudades(estadosVO.get(i).getIdRegion());
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

            private void getCiudades(String idRegion) {
                ciudadesVO = getCiudadesList(idRegion);
                ciudadesVO.add(0, new CityVO("00", "00", getResources().getString(R.string.text_select_ciudad)));

                String[] citys = new String[ciudadesVO.size()];
                for (int i = 0; i < citys.length; i++) {
                    citys[i] = ciudadesVO.get(i).getName();
                }

                ArrayAdapter<String> spinnerCitysAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, citys);
                spinnerCitysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCiudadResidencia.setAdapter(spinnerCitysAdapter);
            }
        });

        spinnerPaisOrigen.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < paisesVO.size(); i++) {
                    if (paisesVO.get(i).getName().equals(parent.getSelectedItem().toString())) {
                        getEstados( paisesVO.get(i).getIdContry() );
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

            private void getEstados(String idCountry) {
                estadosVO = getEstadosList(idCountry);
                estadosVO.add(0, new RegionVO("00", "00", getResources().getString(R.string.text_select_estado)));

                String[] regions = new String[estadosVO.size()];
                for (int i = 0; i < regions.length; i++){
                    regions[i] = estadosVO.get(i).getName();
                }

                ArrayAdapter<String> spinnerRegionAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, regions);
                spinnerRegionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerEstadoOrigen.setAdapter(spinnerRegionAdapter);

                spinnerEstadoOrigen.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        for (int i = 0; i < estadosVO.size(); i++) {
                            if (estadosVO.get(i).getName().equals(parent.getSelectedItem().toString())) {
                                getCiudades(estadosVO.get(i).getIdRegion());
                                break;
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }

                    private void getCiudades(String idRegion) {
                        ciudadesVO = getCiudadesList(idRegion);
                        ciudadesVO.add(0, new CityVO("00", "00", getResources().getString(R.string.text_select_ciudad)));

                        String[] citys = new String[ciudadesVO.size()];
                        for (int i = 0; i < citys.length; i++) {
                            citys[i] = ciudadesVO.get(i).getName();
                        }

                        ArrayAdapter<String> spinnerCitysAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, citys);
                        spinnerCitysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCiudadOrigen.setAdapter(spinnerCitysAdapter);
                    }
                });
            }
        });
    }

    private List<CountryVO> getPaisesList(){
        List<CountryVO> countryVO;
        CommonDAO<CountryVO> countryDAO = new CommonDAO<>(getActivity(), CountryVO.class, EduscoreOpenHelper.TABLE_DAT_COUNTRY);
        countryDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_DAT_COUNTRY + " GROUP BY name";
        countryVO = countryDAO.runQuery( sql );
        return countryVO;
    }

    private List<RegionVO> getEstadosList(String idCountry){
        List<RegionVO> regionVO;
        CommonDAO<RegionVO> regionDAO = new CommonDAO<>(getActivity(), RegionVO.class, EduscoreOpenHelper.TABLE_DAT_REGION);
        regionDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_DAT_REGION +
                " WHERE idContry = '" + idCountry + "' GROUP BY name";
        regionVO = regionDAO.runQuery( sql );
        return regionVO;
    }

    private List<CityVO> getCiudadesList(String idEstado){
        List<CityVO> cityVO;
        CommonDAO<CityVO> cityDAO = new CommonDAO<>(getActivity(), CityVO.class, EduscoreOpenHelper.TABLE_DAT_CITY);
        cityDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_DAT_CITY +
                " WHERE idRegion = '" + idEstado + "' GROUP BY name";
        cityVO = cityDAO.runQuery( sql );
        return cityVO;
    }
}
