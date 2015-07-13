package com.uniat.eduscore.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.uniat.eduscore.dialogs.InterestDialog;
import com.uniat.eduscore.util.ExtendedClickListener;
import com.uniat.eduscore.util.ExtendedTextWatcher;
import com.uniat.eduscore.util.InterestOnClickListener;
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
    private ImageButton imgBtnAddEmail;
    private ImageButton imgBtnAddPhone;

    private ViewGroup groupEmail;
    private ViewGroup groupPhone;

    private TextView textInterest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_datos_personales, null);
        init(rootView);
        events();
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
        imgBtnAddEmail = (ImageButton) rootView.findViewById(R.id.addButtonEmail);
        imgBtnAddPhone = (ImageButton) rootView.findViewById(R.id.addButtonPhone);
        groupEmail = (ViewGroup) rootView.findViewById(R.id.containerEmail);
        groupPhone = (ViewGroup) rootView.findViewById(R.id.containerTelephone);
        textInterest = (TextView) rootView.findViewById(R.id.textInterest);
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

    private void events(){
        editTextName.addTextChangedListener(new ExtendedTextWatcher(imageButtonName));
        editTextLastName.addTextChangedListener(new ExtendedTextWatcher(imageButtonLastName));
        editTextEmail.addTextChangedListener(new ExtendedTextWatcher(imageButtonEmail));
        editTextPhone.addTextChangedListener(new ExtendedTextWatcher(imageButtonPhone));
        imageButtonName.setOnClickListener(new ExtendedClickListener(editTextName));
        imageButtonLastName.setOnClickListener(new ExtendedClickListener(editTextLastName));
        imageButtonEmail.setOnClickListener(new ExtendedClickListener(editTextEmail));
        imageButtonPhone.setOnClickListener(new ExtendedClickListener(editTextPhone));
        textInterest.setOnClickListener(new InterestOnClickListener(textInterest, getActivity()));

        imgBtnAddEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ViewGroup newEmail = (ViewGroup) LayoutInflater.from(getActivity()).inflate(
                        R.layout.item_email, groupEmail, false);

                TextView eTextEmail = (TextView) newEmail.findViewById(R.id.TextViewEmail);
                ImageButton imgBtnRemoveEmail = (ImageButton) newEmail.findViewById(R.id.removeButtonEmail);

                eTextEmail.setText(editTextEmail.getText().toString());
                editTextEmail.setText("");

                imgBtnRemoveEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        groupEmail.removeView(newEmail);

                    }
                });
                groupEmail.addView(newEmail, 0);

            }
        });

        imgBtnAddPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ViewGroup newPhone = (ViewGroup) LayoutInflater.from(getActivity()).inflate(
                        R.layout.item_phone, groupPhone, false);

                TextView eTextPhone = (TextView) newPhone.findViewById(R.id.TextViewPhone);
                TextView eTextTypePhone = (TextView) newPhone.findViewById(R.id.TextViewTypePhone);
                ImageButton imgBtnRemoveEmail = (ImageButton) newPhone.findViewById(R.id.removeButtonPhone);

                eTextPhone.setText(editTextPhone.getText().toString());
                editTextPhone.setText("");
                eTextTypePhone.setText(spinnerTelefono.getSelectedItem().toString());

                imgBtnRemoveEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        groupPhone.removeView(newPhone);

                    }
                });
                groupPhone.addView(newPhone, 0);

            }
        });
    }


}
