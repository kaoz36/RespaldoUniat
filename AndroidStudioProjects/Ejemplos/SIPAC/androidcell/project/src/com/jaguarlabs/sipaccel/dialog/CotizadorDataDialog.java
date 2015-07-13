/** *************************************************************************
 *
 *   Copyright (c)  2013 by Jaguar Labs.
 *   Confidential and Proprietary
 *   All Rights Reserved
 *
 *   This software is furnished under license and may be used and copied
 *   only in accordance with the terms of its license and with the
 *   inclusion of the above copyright notice. This software and any other
 *   copies thereof may not be provided or otherwise made available to any
 *   other party. No title to and/or ownership of the software is hereby
 *   transferred.
 *
 *   The information in this software is subject to change without notice and
 *   should not be construed as a commitment by JaguarLabs.
 *
 * @(#)$Id: $
 * Last Revised By   : Raul Acevedo
 * Last Checked In   : $Date: $
 * Last Version      : $Revision:  $
 *
 * Original Author   : Julio Hernandez  julio.hernadez@jaguarlabs.com
 * Origin            : SEnE -- April 25 @ 11:00 (PST)
 * Notes             :
 *
 * *************************************************************************/

package com.jaguarlabs.sipaccel.dialog;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.jaguarlabs.sipaccel.R;
import com.jaguarlabs.sipaccel.fragment.CotizadorFragment;
import com.jaguarlabs.sipaccel.util.CotizadorUtil;
import com.jaguarlabs.sipaccel.util.DismissDialog;
import com.jaguarlabs.sipaccel.util.IDialogDataRefresh;
import com.jaguarlabs.sipaccel.util.IKeyboardHider;
import com.jaguarlabs.sipaccel.util.IRangeCallback;
import com.jaguarlabs.sipaccel.util.OnFocusRangeChangeListener;
import com.jaguarlabs.sipaccel.vo.ProfesionVO;

public class CotizadorDataDialog extends DismissDialog  implements IKeyboardHider{

	private ImageButton cerrar;
	private IDialogDataRefresh dialogRefresh;
	private String fullName;
	
	@Override
	public void hideKeyboard(EditText pEdit) {}

	private int sexo;
	private int edad;
	private boolean _fuma;
	private ProfesionVO selectedProfesion;
	private Spinner spinList;
	private RadioButton radH;
	private RadioButton radM;
	private EditText textEdad;
	private CheckBox fuma;
	private CotizadorUtil cotizadorUtil;

	public CotizadorDataDialog(Context context, IDialogDataRefresh dataRefresh) throws Exception{
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		cotizadorUtil = CotizadorUtil.getInstance();
		dialogRefresh = dataRefresh;
		setContentView(R.layout.dialog_datos_cotizador);
		spinList = (Spinner) findViewById(R.id.profesionesSpinner);

		radH = (RadioButton) findViewById(R.id.radioMale);
		radM = (RadioButton) findViewById(R.id.radioFemale);
		fuma = (CheckBox) findViewById(R.id.fumaCheck);
		textEdad = (EditText) findViewById(R.id.realAge);
		edad = 15;
		sexo = cotizadorUtil.MASCULINO;
		_fuma = true;
		((TextView) findViewById(R.id.calculateAge))
						.setText(getEdadCalculo(edad, sexo, _fuma)+ "");
		setListeners();
	}

	public void setProfesionesData(List<ProfesionVO> profesiones) {
		ArrayAdapter<ProfesionVO> dataAdapter = new ArrayAdapter<ProfesionVO>(
				getContext(), android.R.layout.simple_spinner_item, profesiones);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinList.setAdapter(dataAdapter);

		selectedProfesion = profesiones.get(0);
		dialogRefresh.refresh(sexo, edad, _fuma, selectedProfesion, fullName);
	}

	private int getEdadCalculo(int pEdad, int pSexo, boolean pFuma) {
		return Math
				.max((pEdad - ((pSexo == cotizadorUtil.FEMENINO) ? 3 : 0) - ((pFuma) ? 0
						: 2)), 15);
	}

	private void getValores() {
		edad = Integer.parseInt(textEdad.getText().toString());
	}

	private void setListeners() {

		spinList.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				selectedProfesion = (ProfesionVO) parent.getItemAtPosition(pos);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});

		fuma.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				_fuma = ((CheckBox)v).isChecked();
				((TextView) findViewById(R.id.calculateAge))
						.setText(getEdadCalculo(edad, sexo, _fuma)+ "");
			}
		});
		
		textEdad.setOnFocusChangeListener(new OnFocusRangeChangeListener<Integer>(
				15, 70, new IRangeCallback() {			
					@Override
					public void doAfterValidation(double value, boolean valid) {
						textEdad.setText( ( valid ? value : (value <= 15 ? 15 : 70) ) + "" );
						getValores();
						((TextView) findViewById(R.id.calculateAge))
						.setText(getEdadCalculo(edad, sexo, _fuma) + "");
					}
		}, textEdad,this));

		RadioGroup radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);

		radioSexGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.radioMale:
					sexo = cotizadorUtil.MASCULINO;
					break;
				case R.id.radioFemale:
					sexo = cotizadorUtil.FEMENINO;
					break;
				default:
					break;
				}
				((TextView) findViewById(R.id.calculateAge))
						.setText(getEdadCalculo(edad, sexo, _fuma) + "");
			}
		});

		cerrar = (ImageButton) findViewById(R.id.closeData);
		cerrar.setOnClickListener(this);
	}

	private boolean validar() {
		int val = textEdad.getText().toString().equals("") ? 0 : Integer.parseInt(textEdad.getText().toString() );
		return ( val >= 15 && val <= 70 ) ? true : false;
	}

	@Override
	public void onBackPressed() {
		View dialogView = findViewById(R.id.dialogDatos);
		fullName = ((EditText) findViewById(R.id.textFullName)).getText().toString();
		if ( validar() ) {
			getValores();
			dialogRefresh.refresh(sexo, edad, _fuma, selectedProfesion, fullName);
			super.dismiss(dialogView);
		} else {
			Animation tween = AnimationUtils.loadAnimation(getContext(),
					R.anim.tilt);
			tween.reset();
			dialogView.clearAnimation();
			dialogView.startAnimation(tween);
		}
	}

	@Override
	public void onClick(View v) {
		onBackPressed();
	}

}
