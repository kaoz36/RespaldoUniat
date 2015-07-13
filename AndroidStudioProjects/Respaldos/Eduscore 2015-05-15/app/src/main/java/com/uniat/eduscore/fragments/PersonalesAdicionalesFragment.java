package com.uniat.eduscore.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uniat.eduscore.util.DatePickerOnClickListener;
import com.university3dmx.eduscore.R;

/**
 * Created by Admin on 09/04/2015.
 */
public class PersonalesAdicionalesFragment extends Fragment {

    public TextView textDatePicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_datos_personales_adicionales, null);
        init(rootView);
        events();
        return rootView;
    }

    private void  init( View rootView ){
        textDatePicker = (TextView) rootView.findViewById(R.id.TextNacimiento);

    }

    private void events(){
        textDatePicker.setOnClickListener( new DatePickerOnClickListener(textDatePicker, getActivity()));
    }
}
