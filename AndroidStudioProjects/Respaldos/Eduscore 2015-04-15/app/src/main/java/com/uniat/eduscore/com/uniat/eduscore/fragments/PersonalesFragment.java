package com.uniat.eduscore.com.uniat.eduscore.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.uniat.eduscore.com.uniat.eduscore.util.ExtendedClickListener;
import com.uniat.eduscore.com.uniat.eduscore.util.ExtendedTextWatcher;
import com.university3dmx.eduscore.R;

public class PersonalesFragment extends Fragment {

    private Spinner spinnerTelefono;
    private Spinner spinnerTipoInteres;
    private Spinner spinnerHorario;

    private EditText editTextName;
    private EditText editTextLastName;
    private EditText editTextEmail;
    private EditText editTextPhone;
    private ImageButton imageButtonName;
    private ImageButton imageButtonLastName;
    private ImageButton imageButtonEmail;
    private ImageButton imageButtonPhone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_datos_personales, null);
        init(rootView);
        loadSpinners();
        return rootView;
    }

    public void init(View rootView){
        spinnerTelefono = (Spinner) rootView.findViewById(R.id.spinnerTelefono);
        spinnerTipoInteres = (Spinner) rootView.findViewById(R.id.spinnerTipoInteres);
        spinnerHorario = (Spinner) rootView.findViewById(R.id.spinnerHorario);
        editTextName = (EditText) rootView.findViewById(R.id.editTextName);
        editTextLastName = (EditText) rootView.findViewById(R.id.editTextLastName);
        editTextEmail = (EditText) rootView.findViewById(R.id.editTextEmail);
        editTextPhone = (EditText) rootView.findViewById(R.id.editTextPhone);
        imageButtonName = (ImageButton) rootView.findViewById(R.id.clearButtonName);
        imageButtonLastName = (ImageButton) rootView.findViewById(R.id.clearButtonLastName);
        imageButtonEmail = (ImageButton) rootView.findViewById(R.id.clearButtonEmail);
        imageButtonPhone = (ImageButton) rootView.findViewById(R.id.clearButtonPhone);
        editTextName.addTextChangedListener(new ExtendedTextWatcher(imageButtonName));
        editTextLastName.addTextChangedListener(new ExtendedTextWatcher(imageButtonLastName));
        editTextEmail.addTextChangedListener(new ExtendedTextWatcher(imageButtonEmail));
        editTextPhone.addTextChangedListener(new ExtendedTextWatcher(imageButtonPhone));
        imageButtonName.setOnClickListener(new ExtendedClickListener(editTextName));
        imageButtonLastName.setOnClickListener(new ExtendedClickListener(editTextLastName));
        imageButtonEmail.setOnClickListener(new ExtendedClickListener(editTextEmail));
        imageButtonPhone.setOnClickListener(new ExtendedClickListener(editTextPhone));
    }

    private void loadSpinners(){
        //Carga Spinner Telefono
        String arrayTelefono[] = getResources().getStringArray(R.array.spinner_array_tel);
        ArrayAdapter<String> spinnerTelefonoAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, arrayTelefono);
        spinnerTelefonoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTelefono.setAdapter(spinnerTelefonoAdapter);

        //Cargar Spinner Tipo Interes
        String arrayInteres[] = getResources().getStringArray(R.array.spinner_array_tipo_interes);
        ArrayAdapter<String> spinnerTipoInteresAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, arrayInteres);
        spinnerTipoInteresAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoInteres.setAdapter(spinnerTipoInteresAdapter);

        //Cargar Spinner Horario
        String arrayHorario[] = getResources().getStringArray(R.array.spinner_array_horario);
        ArrayAdapter<String> spinnerHorarioAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, arrayHorario);
        spinnerHorarioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHorario.setAdapter(spinnerHorarioAdapter);
    }
}
