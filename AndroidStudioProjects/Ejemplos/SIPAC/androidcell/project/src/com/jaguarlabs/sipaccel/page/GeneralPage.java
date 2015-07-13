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

package com.jaguarlabs.sipaccel.page;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jaguarlabs.sipaccel.R;
import com.jaguarlabs.sipaccel.tables.GeneralTable;
import com.jaguarlabs.sipaccel.util.DataRenderer;
import com.jaguarlabs.sipaccel.vo.PolizaVO;

public class GeneralPage extends DataRenderer<PolizaVO>{
		
	@Override
	public void build(Context pContext, LayoutInflater inflate, ViewGroup view) {
		View screenView;
		super.build(pContext, inflate, view);
		screenView = inflate.inflate( R.layout.page_poliza_general, view, false );
		super.renderView = screenView;	
	}
	
	private long calculateEdad(String dateString)
	{
		
		long edad =0;
		try{
		SimpleDateFormat df =  new SimpleDateFormat("yyyy-MM-dd");
		Long baseNum =  df.parse(getData().getNac_poliza()).getTime();
		Long current = (new Date()).getTime();
		edad = (current-baseNum);
		edad /= 1000;
		edad /= 60;
		edad /=  60;
		edad /= 24;
		edad /= 365.242;
		}catch (Exception error)
		{
			android.util.Log.e("Error","Error al calcular Edad");
			edad = 0;
		}finally{
			
		}
		return edad;
	}
	
	
	public void refreshData(){
		View screenView = getView();
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		((TextView)screenView.findViewById(R.id.sexoText)).setText(getData().getSexo_poliza());
		((TextView)screenView.findViewById(R.id.fumaText)).setText(getData().getFuma_poliza());
		((TextView)screenView.findViewById(R.id.nacText)).setText(getData().getNac_poliza());
		((TextView)screenView.findViewById(R.id.edadText)).setText(""+calculateEdad(getData().getNac_poliza()));
		((TextView)screenView.findViewById(R.id.calleText)).setText(getData().getDomicilio_poliza());
		((TextView)screenView.findViewById(R.id.cpText)).setText(getData().getCp_poliza());
		((TextView)screenView.findViewById(R.id.telefonoText)).setText(getData().getTel_poliza());
		((TextView)screenView.findViewById(R.id.emailText)).setText(getData().getEmail_poliza());
		((TextView)screenView.findViewById(R.id.pPagadaText)).setText(nf.format(defaultNumber(getData().getPrima_pag_poliza())));
		((TextView)screenView.findViewById(R.id.reservaText)).setText("("+((getData().getSigno_reserva().equals("N"))?"-":"+")+")"+nf.format(defaultNumber(getData().getRes_poliza())));
		((TextView)screenView.findViewById(R.id.invText)).setText("("+((getData().getSigno_fondoi().equals("N"))?"-":"+")+")"+nf.format(defaultNumber(getData().getInv_poliza())));
		((TextView)screenView.findViewById(R.id.pPendienteText)).setText(nf.format(defaultNumber(getData().getPrima_pen_poliza())));
		((TextView)screenView.findViewById(R.id.rPendienteText)).setText(getData().getRec_pen_poliza());
		((TextView)screenView.findViewById(R.id.uPagoText)).setText(getData().getUlt_pago_poliza());
		((TextView)screenView.findViewById(R.id.pEmitidaText)).setText(nf.format(defaultNumber(getData().getPrima_emi_poliza())));
		((TextView)screenView.findViewById(R.id.pQuincenalText)).setText(nf.format(defaultNumber(getData().getPrima_quin_poliza())));
		((TextView)screenView.findViewById(R.id.iniVigText)).setText(getData().getFec_ini_vig_poliza());
		((TextView)screenView.findViewById(R.id.uModText)).setText(getData().getFec_ult_mod_poliza());
		((TextView)screenView.findViewById(R.id.uRetiroText)).setText(getData().getFec_ult_ret_poliza());
		((TextView)screenView.findViewById(R.id.poblacionText)).setText(getData().getPob_poliza());
		((TextView)screenView.findViewById(R.id.civilText)).setText(getData().getEstado_civil_poliza());
		events();
	}

	private void events(){
		ImageButton table = (ImageButton) renderView.findViewById( R.id.table_cobertura );
		table.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {		
				Intent i = new Intent( context, GeneralTable.class);
				context.startActivity(i);
				getAct().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
			}
		});
	}

	
}
