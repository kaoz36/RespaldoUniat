package com.uniat.eduscore.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.uniat.eduscore.db.CommonDAO;
import com.uniat.eduscore.db.EduscoreOpenHelper;
import com.uniat.eduscore.vo.CareerVO;
import com.uniat.eduscore.vo.EventVO;
import com.uniat.eduscore.vo.MediaVO;
import com.uniat.eduscore.vo.OriginVO;
import com.uniat.eduscore.vo.PersonVO;
import com.university3dmx.eduscore.R;

import java.util.ArrayList;
import java.util.List;

public class AdicionalesFragment extends Fragment {

    private Spinner spinnerProbabilidadIngreso;
    private Spinner spinnerMedio;
    private Spinner spinnerProcedencia;
    private Spinner spinnerEvento;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_datos_adicionales, null);
        spinnerProbabilidadIngreso = (Spinner) rootView.findViewById(R.id.spinnerProbabilidad);
        spinnerMedio = (Spinner) rootView.findViewById(R.id.spinnerMedio);
        spinnerProcedencia = (Spinner) rootView.findViewById(R.id.spinnerProcedencia);
        spinnerEvento = (Spinner) rootView.findViewById(R.id.spinnerEvento);
        try{
            loadSpinner();
        }catch (Exception e){
            Toast.makeText(getActivity(), "Necesitas Actualizar los Datos.", Toast.LENGTH_LONG).show();
        }
        return rootView;
    }

    private void loadSpinner(){
        String arrayProbabilidad[] = getResources().getStringArray(R.array.spinner_array_probabilidad_ingreso);
        ArrayAdapter<String> spinnerProbabilidadAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, arrayProbabilidad);
        spinnerProbabilidadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProbabilidadIngreso.setAdapter(spinnerProbabilidadAdapter);

        String arrayMedias[] = getMediasArray();
        ArrayAdapter<String> spinnerMedioAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, arrayMedias);
        spinnerMedioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMedio.setAdapter(spinnerMedioAdapter);

        String arrayEvents[] = getEventsArray();
        ArrayAdapter<String> spinnerEventAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, arrayEvents);
        spinnerEventAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEvento.setAdapter(spinnerEventAdapter);

        String arrayLugar[] = getOriginsArray();
        ArrayAdapter<String> spinnerProcedenciaAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, arrayLugar);
        spinnerProcedenciaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProcedencia.setAdapter(spinnerProcedenciaAdapter);

    }

    private String getIdCampus(){
        SharedPreferences shared = getActivity().getSharedPreferences("sipacUser", getActivity().MODE_PRIVATE);
        String idPerson = shared.getString("idPerson", "1");
        List<PersonVO> personVO = new ArrayList<>();
        CommonDAO<PersonVO> personDAO = new CommonDAO<>(getActivity(), PersonVO.class, EduscoreOpenHelper.TABLE_DAT_PERSON);
        personDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_DAT_PERSON +
                " WHERE idPerson = '" + idPerson + "' AND status = '1'";
        personVO = personDAO.runQuery( sql );
        return personVO.get(0).getIdCampus();
    }

    private String[] getMediasArray(){
        List<MediaVO> mediaVO = new ArrayList<>();
        CommonDAO<MediaVO> mediaDAO = new CommonDAO<>(getActivity(), MediaVO.class, EduscoreOpenHelper.TABLE_DAT_MEDIA);
        mediaDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_DAT_MEDIA +
                " WHERE idCampus = '" + getIdCampus() + "' AND status = '1' GROUP BY name";
        mediaVO = mediaDAO.runQuery( sql );
        mediaVO.add(0, new MediaVO("00", "Seleccione un medio.", "1", "00"));
        String[] medias = new String[mediaVO.size()];
        for (int i = 0; i < medias.length; i++){
            medias[i] = mediaVO.get(i).getName();
        }
        return medias;
    }

    private String[] getEventsArray(){
        List<EventVO> eventVO = new ArrayList<>();
        CommonDAO<EventVO> eventDAO = new CommonDAO<>(getActivity(), EventVO.class, EduscoreOpenHelper.TABLE_DAT_EVENTS);
        eventDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_DAT_EVENTS +
                " WHERE idCampus = '" + getIdCampus() + "' AND status = '1' GROUP BY name";
        eventVO = eventDAO.runQuery( sql );
        eventVO.add(0, new EventVO("00", "Seleccione un evento.", "1", "00"));
        String[] medias = new String[eventVO.size()];
        for (int i = 0; i < medias.length; i++){
            medias[i] = eventVO.get(i).getName();
        }
        return medias;
    }

    private String[] getOriginsArray(){
        List<OriginVO> originVO = new ArrayList<>();
        CommonDAO<OriginVO> originDAO = new CommonDAO<>(getActivity(), OriginVO.class, EduscoreOpenHelper.TABLE_DAT_ORIGIN);
        originDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_DAT_ORIGIN +
                " WHERE idCampus = '" + getIdCampus() + "' AND status = '1' GROUP BY name";
        originVO = originDAO.runQuery( sql );
        originVO.add(0, new OriginVO("00", "Seleccione un lugar.", "00", "00", "00"));
        String[] origins = new String[originVO.size()];
        for (int i = 0; i < origins.length; i++){
            origins[i] = originVO.get(i).getName();
        }
        return origins;
    }

}
