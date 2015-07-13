package com.jaguarlabs.sipac.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.jaguarlabs.sipac.LoginActivity;
import com.jaguarlabs.sipac.R;
import com.jaguarlabs.sipac.model.DataModel;
import com.jaguarlabs.sipac.util.DismissDialog;
import com.jaguarlabs.sipac.util.ISaveLocationHandler;
import com.jaguarlabs.sipac.vo.MapLocationVO;

public class SaveMapLocationDialog extends DismissDialog implements android.view.View.OnClickListener {

	private EditText nombreText,retenedorText,uPagoText,prospectosText,direccionText,zonaText;
	private MapLocationVO location;
	private ISaveLocationHandler saveHandler;
	
	public SaveMapLocationDialog(Context context,MapLocationVO locPoint,ISaveLocationHandler saveHandler) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		this.setContentView(com.jaguarlabs.sipac.R.layout.dialog_register_location);
		location = locPoint;
		this.saveHandler = saveHandler;
		setTitle(null);
		setCancelable(true);
		setListeners();
	}
	
	private void setListeners(){
		nombreText =  (EditText)this.findViewById(com.jaguarlabs.sipac.R.id.nombreText);
		retenedorText = (EditText)this.findViewById(com.jaguarlabs.sipac.R.id.retenedorText);
		uPagoText = (EditText)this.findViewById(com.jaguarlabs.sipac.R.id.uPagoText);
		prospectosText = (EditText)this.findViewById(com.jaguarlabs.sipac.R.id.prospectosText);
		direccionText = (EditText)this.findViewById(com.jaguarlabs.sipac.R.id.direccionText);
		zonaText = (EditText)this.findViewById(com.jaguarlabs.sipac.R.id.claveText);
		
		nombreText.addTextChangedListener(new LoginActivity.ExtendedTextWatcher((Button)this.findViewById(com.jaguarlabs.sipac.R.id.deletebutton1)));
		retenedorText.addTextChangedListener(new LoginActivity.ExtendedTextWatcher((Button)this.findViewById(com.jaguarlabs.sipac.R.id.deletebutton2)));
		uPagoText.addTextChangedListener(new LoginActivity.ExtendedTextWatcher((Button)this.findViewById(com.jaguarlabs.sipac.R.id.deletebutton3)));
		prospectosText.addTextChangedListener(new LoginActivity.ExtendedTextWatcher((Button)this.findViewById(com.jaguarlabs.sipac.R.id.deletebutton4)));
		direccionText.addTextChangedListener(new LoginActivity.ExtendedTextWatcher((Button)this.findViewById(com.jaguarlabs.sipac.R.id.deletebutton5)));
		zonaText.addTextChangedListener(new LoginActivity.ExtendedTextWatcher((Button)this.findViewById(com.jaguarlabs.sipac.R.id.deletebutton6)));
				
		((Button)this.findViewById(com.jaguarlabs.sipac.R.id.deletebutton1)).setOnClickListener(new LoginActivity.ExtendedClickListener(nombreText));
		((Button)this.findViewById(com.jaguarlabs.sipac.R.id.deletebutton2)).setOnClickListener(new LoginActivity.ExtendedClickListener(retenedorText));
		((Button)this.findViewById(com.jaguarlabs.sipac.R.id.deletebutton3)).setOnClickListener(new LoginActivity.ExtendedClickListener(uPagoText));
		((Button)this.findViewById(com.jaguarlabs.sipac.R.id.deletebutton4)).setOnClickListener(new LoginActivity.ExtendedClickListener(prospectosText));
		((Button)this.findViewById(com.jaguarlabs.sipac.R.id.deletebutton5)).setOnClickListener(new LoginActivity.ExtendedClickListener(direccionText));
		((Button)this.findViewById(com.jaguarlabs.sipac.R.id.deletebutton6)).setOnClickListener(new LoginActivity.ExtendedClickListener(zonaText));
				
		((Button)this.findViewById(com.jaguarlabs.sipac.R.id.saveButton)).setOnClickListener(this);
		((Button)this.findViewById(com.jaguarlabs.sipac.R.id.cancelButton)).setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		View dialogView = findViewById(com.jaguarlabs.sipac.R.id.dialog);
		
		switch(v.getId())
		{
			case com.jaguarlabs.sipac.R.id.saveButton:
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
			case com.jaguarlabs.sipac.R.id.cancelButton:
					dismiss();
					break;
		}
		
	}
	
	private void ocultarTeclado(){
		getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN );
	}
}
