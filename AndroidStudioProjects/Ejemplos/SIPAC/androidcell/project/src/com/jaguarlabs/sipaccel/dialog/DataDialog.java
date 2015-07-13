package com.jaguarlabs.sipaccel.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.jaguarlabs.sipaccel.R;
import com.jaguarlabs.sipaccel.util.DismissDialog;
import com.jaguarlabs.sipaccel.vo.PolizaVO;

public class DataDialog extends DismissDialog{

	private Button cerrar;
	private PolizaVO pPoliza;
	
	public DataDialog(Context context, PolizaVO pPoliza) {
		super(context);
		this.pPoliza = pPoliza;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView( R.layout.dialog_datos );
		setListeners();
	}
	
	private void setListeners() {
		cerrar = (Button) findViewById( R.id.closeData );
		renderPoliza( pPoliza );
		cerrar.setOnClickListener( this );
	}
	
	private void renderPoliza(PolizaVO pPoliza) {
		((TextView) findViewById(R.id.TextEstatus)).setText(pPoliza.getStatus_poliza());
		((TextView) findViewById(R.id.TextRetenedor)).setText(pPoliza.getRet_poliza());
		((TextView) findViewById(R.id.TextNombre)).setText(pPoliza.getNombre_poliza());
		((TextView) findViewById(R.id.TextNoEmpleado)).setText(pPoliza.getEmp_poliza());
		((TextView) findViewById(R.id.TextRFC)).setText(pPoliza.getRfc_poliza());

	}
	
	@Override
	public void onClick(View v) {
		View dialogDatos = (View) findViewById( R.id.dialogDatos );
		super.dismiss( dialogDatos );
	}

}
