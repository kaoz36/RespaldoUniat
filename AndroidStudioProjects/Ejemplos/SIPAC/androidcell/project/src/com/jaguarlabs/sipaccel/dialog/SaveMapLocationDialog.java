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

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.jaguarlabs.sipaccel.LoginActivity;
import com.jaguarlabs.sipaccel.R;
import com.jaguarlabs.sipaccel.model.DataModel;
import com.jaguarlabs.sipaccel.util.DismissDialog;
import com.jaguarlabs.sipaccel.util.ISaveLocationHandler;
import com.jaguarlabs.sipaccel.vo.MapLocationVO;

public class SaveMapLocationDialog extends DismissDialog implements android.view.View.OnClickListener {

	private EditText nombreText;
	private EditText retenedorText;
	private EditText uPagoText;
	private EditText prospectosText;
	private EditText direccionText;
	private EditText zonaText;
	private MapLocationVO location;
	private ISaveLocationHandler saveHandler;
	
	public SaveMapLocationDialog(Context context,MapLocationVO locPoint,ISaveLocationHandler saveHandler) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable( new ColorDrawable( android.graphics.Color.TRANSPARENT ) );
		this.setContentView( R.layout.dialog_register_location );
		location = locPoint;
		this.saveHandler = saveHandler;
		setTitle(null);
		setCancelable(true);
		setListeners();
	}
	
	private void setListeners(){
		nombreText 		= (EditText)this.findViewById( R.id.nombreText );
		retenedorText 	= (EditText)this.findViewById( R.id.retenedorText );
		uPagoText 		= (EditText)this.findViewById( R.id.uPagoText );
		prospectosText 	= (EditText)this.findViewById( R.id.prospectosText );
		direccionText 	= (EditText)this.findViewById( R.id.direccionText );
		zonaText 		= (EditText)this.findViewById( R.id.zonaText );
		
		nombreText.addTextChangedListener( new LoginActivity.ExtendedTextWatcher( (ImageButton)this.findViewById( R.id.deletebutton1 ) ) );
		retenedorText.addTextChangedListener( new LoginActivity.ExtendedTextWatcher( (ImageButton)this.findViewById( R.id.deletebutton2 ) ) );
		uPagoText.addTextChangedListener( new LoginActivity.ExtendedTextWatcher( (ImageButton)this.findViewById( R.id.deletebutton3 ) ) );
		prospectosText.addTextChangedListener( new LoginActivity.ExtendedTextWatcher( (ImageButton)this.findViewById( R.id.deletebutton4 ) ) );
		direccionText.addTextChangedListener( new LoginActivity.ExtendedTextWatcher( (ImageButton)this.findViewById( R.id.deletebutton5 ) ) );
		zonaText.addTextChangedListener( new LoginActivity.ExtendedTextWatcher( (ImageButton)this.findViewById( R.id.deletebutton8 ) ) );
		
		((ImageButton)this.findViewById( R.id.deletebutton1 )).setOnClickListener(new LoginActivity.ExtendedClickListener(nombreText));
		((ImageButton)this.findViewById( R.id.deletebutton2 )).setOnClickListener(new LoginActivity.ExtendedClickListener(retenedorText));
		((ImageButton)this.findViewById( R.id.deletebutton3 )).setOnClickListener(new LoginActivity.ExtendedClickListener(uPagoText));
		((ImageButton)this.findViewById( R.id.deletebutton4 )).setOnClickListener(new LoginActivity.ExtendedClickListener(prospectosText));
		((ImageButton)this.findViewById( R.id.deletebutton5 )).setOnClickListener(new LoginActivity.ExtendedClickListener(direccionText));
		((ImageButton)this.findViewById( R.id.deletebutton8 )).setOnClickListener(new LoginActivity.ExtendedClickListener(zonaText));
		
		((Button)this.findViewById( R.id.saveButton ) ).setOnClickListener(this);
		((Button)this.findViewById( R.id.cancelButton ) ).setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {	
		View dialogView = findViewById( R.id.dialog );
		
		switch( v.getId() ){
			case R.id.saveButton:
				v.setEnabled( false );
				ocultarTeclado();
				if(nombreText.getText().toString().length() > 0 &&
									   retenedorText.getText().toString().length() >0 &&
									   uPagoText.getText().toString().length() >0 &&
									   prospectosText.getText().toString().length() >0 &&
									   direccionText.getText().toString().length() >0 &&
									   zonaText.getText().toString().length() >0){
					location.setIdUbicacion( DataModel.getInstance().getPromotoria().getPromot() );
					location.setNombre(nombreText.getText().toString()); 
					location.setRetenedor(Integer.parseInt(retenedorText.getText().toString()));
					location.setuPago(Integer.parseInt(uPagoText.getText().toString()));
					location.setProspectos(Integer.parseInt(prospectosText.getText().toString()));
					location.setDireccion(direccionText.getText().toString());
					location.setZona( zonaText.getText().toString() );
					saveHandler.saveLocation(location);
					super.dismiss( dialogView );
				}else{					
					Animation tween = AnimationUtils.loadAnimation( getContext(), R.anim.tilt);
			    	tween.reset();
			    	dialogView.clearAnimation();
			    	dialogView.startAnimation(tween);
			    	v.setEnabled( true );
				}
				break;
			case R.id.cancelButton:
				dismiss();
				break;
		}		
	}
	
	private void ocultarTeclado(){
		getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN );
	}
}
