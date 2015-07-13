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
 * Original Author   : Julio Hernandez julio.hernadez@jaguarlabs.com
 * Origin            : SEnE -- April 25 @ 11:00 (PST)
 * Notes             :
 *
 * *************************************************************************/

package com.jaguarlabs.sipaccel.fragment;

import com.jaguarlabs.sipaccel.MainAplicationActivity;
import com.jaguarlabs.sipaccel.R;
import com.jaguarlabs.sipaccel.model.DataModel;
import com.jaguarlabs.sipaccel.vo.UserVO;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class ProfileFragment extends RightFragment{

	@Override
	protected void makeView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ImageButton menu;
		MainAplicationActivity.MainAppTouch iteration = new MainAplicationActivity.MainAppTouch(listener);
		mainView = inflater.inflate(R.layout.fragment_profile, container, false);
		menu = (ImageButton) mainView.findViewById(R.id.menu);
		menu.setOnTouchListener( iteration );
		menu.setOnClickListener( iteration );
		llenarDatos();
	}
	
	private void llenarDatos(){
		UserVO userVO = DataModel.getInstance().getAppUser();
		if (userVO != null) {
			TextView tvExpDate = (TextView)mainView.findViewById( R.id.TextDateExpiration );
			tvExpDate.setText( DataModel.getInstance().getConfiguracion().getExpiracion() );
			
			TextView tvId = (TextView)mainView.findViewById( R.id.TextId );
			tvId.setText(userVO.getAgente());
			
			TextView tvNombre = (TextView)mainView.findViewById( R.id.TextName );
			tvNombre.setText(userVO.getNombre());
			
			TextView tvRfc = (TextView)mainView.findViewById( R.id.TextRFC );
			tvRfc.setText(userVO.getRfc());
			
			TextView tvEmail = (TextView)mainView.findViewById( R.id.TextEmail );
			tvEmail.setText(userVO.getCorreo());
			
		} 
	}

	
}
