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

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.jaguarlabs.sipaccel.db.CommonDAO;
import com.jaguarlabs.sipaccel.db.SipacOpenHelper;
import com.jaguarlabs.sipaccel.fragment.CotizadorFragment;
import com.jaguarlabs.sipaccel.util.DismissDialog;
import com.jaguarlabs.sipaccel.util.IDialogDataRefresh;
import com.jaguarlabs.sipaccel.util.IRangeCallback;
import com.jaguarlabs.sipaccel.vo.ProfesionVO;

public class CotizadorDataDialogMas extends DismissDialog {

	private ImageButton cerrar;
	private TextView textPrimaQuincenal;
	private TextView textPrimaMensual;
	private TextView textPrimaTotal;

	public CotizadorDataDialogMas(Context context, IDialogDataRefresh dataRefresh) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.dialog_mas_datos_cotizador);

		textPrimaQuincenal = (TextView) findViewById(R.id.textPrimaQuincenal);
		textPrimaMensual = (TextView) findViewById(R.id.textPrimaMensual);
		textPrimaTotal = (TextView) findViewById(R.id.textPrimaAnual);
		
		setListeners();
	}
	
	public void refresh( double primaAnual, int pago ){
		textPrimaQuincenal.setText( redondea(primaAnual / 24 * pago) + "");
		textPrimaMensual.setText( redondea(primaAnual / 12 * pago) + "");
		textPrimaTotal.setText( redondea(primaAnual * pago) + "");
	}

	private void setListeners() {
		cerrar = (ImageButton) findViewById(R.id.closeData);
		cerrar.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
		View dialogView = findViewById(R.id.dialogDatos);
		super.dismiss(dialogView);
	}

	@Override
	public void onClick(View v) {
		onBackPressed();
	}
	
	private double redondea(double valor) {
		BigDecimal decimal = new BigDecimal(valor);
		return decimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
	}

}
