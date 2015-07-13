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

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaguarlabs.sipaccel.PolizaDetailActivity;
import com.jaguarlabs.sipaccel.R;
import com.jaguarlabs.sipaccel.model.DataModel;
import com.jaguarlabs.sipaccel.tables.GeneralTable;
import com.jaguarlabs.sipaccel.tables.ServiciosTable;
import com.jaguarlabs.sipaccel.util.DataRenderer;
import com.jaguarlabs.sipaccel.vo.PolizaVO;
import com.jaguarlabs.sipaccel.vo.ServicioGralVO;

public class ServiciosPage extends DataRenderer<PolizaVO> {
	
	@Override
	public void build(Context pContext,LayoutInflater inflater,ViewGroup parent) {
		View screenView;
		super.build(pContext, inflater, parent);
		screenView = inflater.inflate(R.layout.page_poliza_servicios, parent, false);
		super.renderView = screenView;
	}
	
	@Override
	public void refreshData()
	{
		events();
	}
		
	private void events(){
		ImageButton table = (ImageButton) renderView.findViewById(R.id.tables_serv_sistema);
		table.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent( context, ServiciosTable.class);
				context.startActivity(i);
			}
		});
	}

}
