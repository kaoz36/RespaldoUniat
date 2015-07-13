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

import com.jaguarlabs.sipaccel.R;
import com.jaguarlabs.sipaccel.model.DataModel;
import com.jaguarlabs.sipaccel.util.IRowRenderer;
import com.jaguarlabs.sipaccel.vo.CoberturaVO;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GeneralTable extends Activity implements IRowRenderer<CoberturaVO>{
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
		setContentView(R.layout.show_table_gral);
		showTable();
	}
	
	private void showTable() {
		
		LinearLayout cList = (LinearLayout) findViewById( R.id.coberturaList );
		View row;
		int i = 0;
		cList.removeAllViews();
		for( CoberturaVO cobertura: DataModel.getInstance().getCoberturas() ){
			row = ( (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE ) ).
					inflate( R.layout.item_cobertura, cList, false);
			row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
			if(((i++) % 2) !=0)
			{
				row.setBackgroundColor( getResources().getColor(R.color.even_row));
			}else{
				row.setBackgroundColor( getResources().getColor(R.color.odd_row));
			}			
			renderRow(row, cobertura);
			cList.addView(row);
		}
	}
	
	@Override
	public void renderRow(View targetRow, CoberturaVO dataItem) {
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		((TextView)targetRow.findViewById(R.id.idCobertura)).setText(""+dataItem.getClave_cob());
		((TextView)targetRow.findViewById(R.id.prima)).setText(nf.format(defaultNumber(dataItem.getPrima_cob())));
		((TextView)targetRow.findViewById(R.id.primaExtra)).setText(nf.format(defaultNumber(dataItem.getPrima_ext_cob())));
		((TextView)targetRow.findViewById(R.id.primaAsegurada)).setText(nf.format(defaultNumber(dataItem.getSuma_cob())));
	}
	
}
