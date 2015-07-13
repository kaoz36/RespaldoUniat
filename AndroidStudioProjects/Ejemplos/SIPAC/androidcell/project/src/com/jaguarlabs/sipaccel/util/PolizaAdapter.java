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


package com.jaguarlabs.sipaccel.util;

import java.util.List;

import com.jaguarlabs.sipaccel.R;
import com.jaguarlabs.sipaccel.vo.PolizaVO;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PolizaAdapter extends ArrayAdapter<PolizaVO>{

	private int resourceId;
	private List<PolizaVO> items;
	private Context parenteContext;
	
	public PolizaAdapter( Context pContext, int pResourseId, List<PolizaVO> pItems ){
		super(pContext, pResourseId, pItems);
		this.parenteContext 	= pContext;
		this.resourceId 		= pResourseId;
		this.items 				= pItems;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
		TextView tv;
		LayoutInflater li = (LayoutInflater) parenteContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = li.inflate(resourceId, parent, false);		
		
		PolizaVO item = items.get( position );
		int resources[] = {
				R.id.idPoliza, R.id.nombre, R.id.rfc, R.id.uniPago, R.id.positivonegativo, 
				R.id.quincena, R.id.retenedor, R.id.positivonegativo
		};
		
		 for( int currentResource = 0; currentResource < resources.length; currentResource++ ){
	        	tv = (TextView) v.findViewById( resources[currentResource] );
	        	if(tv != null){
	        		tv.setText( item.getData(resources[currentResource]) );
	        	}
	        }
	        return v;
	}
	
}
