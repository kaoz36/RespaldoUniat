package com.uniat.eduscore.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.university3dmx.eduscore.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Admin on 10/04/2015.
 */
public class DatePickerDialog extends DismissDialog {

    private TextView textDate;
    private DatePicker datePicker;
    private Button btnAcep;
    private Button btnClear;

    public DatePickerDialog(Context context, TextView textDate){
        super(context);
        this.textDate = textDate;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_data_picker);
        setListeners();
    }

    private void setListeners(){
        datePicker = (DatePicker) this.findViewById(R.id.datePicker);
        btnAcep = (Button) this.findViewById(R.id.btnAceptar);
        btnClear = (Button) this.findViewById(R.id.btnLimpiarFiltro);
        btnAcep.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        try{
            if( textDate.getText().toString().trim().length() > 0 ){
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                Date date = format.parse(textDate.getText().toString().trim());
                c.setTime(date);
                datePicker.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            }
        }catch (Exception e){

        }
    }

    @Override
    public void onClick(View v) {
        String dia = "00";
        String mes = "00";
        String anio = "";
        View dialogPicker = findViewById(R.id.dialogPicker);
        switch (v.getId()){
            case R.id.btnAceptar:
                dia += datePicker.getDayOfMonth();
                mes += ( datePicker.getMonth() + 1 );
                anio += datePicker.getYear();
                textDate.setText( anio + "-" + mes.substring( mes.length() - 2) + "-" + dia.substring( dia.length() -2 ));
                break;
            case R.id.btnLimpiarFiltro:
                textDate.setText("");
                break;
        }
        super.dismiss( dialogPicker );
    }
}
