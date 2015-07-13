package com.uniat.eduscore.com.uniat.eduscore.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.university3dmx.eduscore.R;

public class AdicionalesFragment extends Fragment {

    private Spinner spinnerProbabilidadIngreso;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_datos_adicionales, null);
        spinnerProbabilidadIngreso = (Spinner) rootView.findViewById(R.id.spinnerProbabilidad);

        loadSpinner();
        return rootView;
    }

    private void loadSpinner(){
        String arrayProbabilidad[] = getResources().getStringArray(R.array.spinner_array_probabilidad_ingreso);
        ArrayAdapter<String> spinnerProbabilidadAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, arrayProbabilidad);
        spinnerProbabilidadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProbabilidadIngreso.setAdapter(spinnerProbabilidadAdapter);
    }
}
