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

import java.text.NumberFormat;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaguarlabs.sipaccel.R;
import com.jaguarlabs.sipaccel.model.DataModel;
import com.jaguarlabs.sipaccel.vo.ServAffectVO;

public class VentasTableOne extends Activity{
	protected Double defaultNumber(String sParam){
		Double d = 0.0;
		try{
			d = Double.valueOf(sParam);
		}catch(Exception error)
		{
			d =0.0;
		}finally{
			
		}
		return d;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.show_table_ventas);
		showTable();
	}
	
	private void showTable() {
		int i = 0;		
		LinearLayout cList = (LinearLayout)findViewById(R.id.servicioAfecList);
		View row;
		cList.removeAllViews();	
		for( ServAffectVO servicios:DataModel.getInstance().getServAfect() ){
			row = ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
					inflate(R.layout.item_serviciosafec, cList,false);
			row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
			if(((i++) % 2) !=0){
			    row.setBackgroundColor(getResources().getColor(R.color.even_row));
			}else{
			    row.setBackgroundColor(getResources().getColor(R.color.odd_row));
			}
			renderRow(row, servicios);
			cList.addView(row);
		}
	}
	
public void renderRow(View row, ServAffectVO servicios) {		
	NumberFormat nf = NumberFormat.getCurrencyInstance();
	((TextView)row.findViewById(com.jaguarlabs.sipaccel.R.id.agente)).setText(""+servicios.getId_serv());
	((TextView)row.findViewById(com.jaguarlabs.sipaccel.R.id.positivonegativo)).setText(""+servicios.getMas_men_serv());
	((TextView)row.findViewById(com.jaguarlabs.sipaccel.R.id.prima)).setText(nf.format(defaultNumber(servicios.getPrima_serv())));
	((TextView)row.findViewById(com.jaguarlabs.sipaccel.R.id.fechaEmicion)).setText(""+servicios.getFec_emi_serv());
	((TextView)row.findViewById(com.jaguarlabs.sipaccel.R.id.plan)).setText(""+servicios.getPlan_serv());
	}
	
}
