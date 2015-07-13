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

package com.jaguarlabs.sipaccel.tables;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaguarlabs.sipaccel.R;
import com.jaguarlabs.sipaccel.model.DataModel;
import com.jaguarlabs.sipaccel.vo.ServicioGralVO;

public class ServiciosTable extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.show_table_serv);
		
		if(DataModel.getInstance().getPromotoria().getMostrar_agte() == 1)
		{
			findViewById(R.id.agenteHeader).setVisibility(View.VISIBLE);
			findViewById(R.id.agenteHeaderSeparator).setVisibility(View.VISIBLE);
		}else
		{
			findViewById(R.id.agenteHeader).setVisibility(View.GONE);
			findViewById(R.id.agenteHeaderSeparator).setVisibility(View.GONE);
		}
		
		showTable();
	}
	
	private void showTable() {
		
		int i = 0;
		LinearLayout cList = (LinearLayout) findViewById(R.id.serviciosList);
		View row;
		cList.removeAllViews();
		for( ServicioGralVO servicios:DataModel.getInstance().getServGral() ){
			row = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
					inflate(R.layout.item_serviciosgeneral, cList,false);
			row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
			if(((i++) % 2) != 0){
			    row.setBackgroundColor(getResources().getColor(R.color.even_row));
			}else{
			    row.setBackgroundColor(getResources().getColor(R.color.odd_row));
			}
			renderRow(row, servicios);
			cList.addView(row);
		}
	}

	public void renderRow(View row, ServicioGralVO dataItem) {
		if(DataModel.getInstance().getPromotoria().getMostrar_agte() == 1)
		{
			(row.findViewById(R.id.agenteSeparator)).setVisibility(View.VISIBLE);
			(row.findViewById(R.id.agente)).setVisibility(View.VISIBLE);
			((TextView)row.findViewById(R.id.agente)).setText(""+dataItem.getAgente_serv());
		}else
		{
			(row.findViewById(R.id.agenteSeparator)).setVisibility(View.GONE);
			(row.findViewById(R.id.agente)).setVisibility(View.GONE);
		}		
		((TextView)row.findViewById(com.jaguarlabs.sipaccel.R.id.noServicio)).setText(""+dataItem.getId_serv());
		((TextView)row.findViewById(com.jaguarlabs.sipaccel.R.id.descripcion)).setText(""+dataItem.getDesc_serv());
		((TextView)row.findViewById(com.jaguarlabs.sipaccel.R.id.fecha)).setText(""+dataItem.getFecha_serv());
		((TextView)row.findViewById(com.jaguarlabs.sipaccel.R.id.ordenpago)).setText(""+dataItem.getOrden_pago());
		((TextView)row.findViewById(com.jaguarlabs.sipaccel.R.id.monto)).setText(""+dataItem.getMonto());
	}

	
}
