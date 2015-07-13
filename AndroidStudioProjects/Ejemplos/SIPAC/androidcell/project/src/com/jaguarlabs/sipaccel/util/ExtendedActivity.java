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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Window;

public class ExtendedActivity extends Activity implements IContextGetter{
	
	protected Context mContext;
	private AlertDialog.Builder builder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		init();
	}
	
	public AlertDialog.Builder getMessageBuilder(){
		if(builder == null){
			builder = new AlertDialog.Builder(this);
			builder.setCancelable(false);
			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		            }
		    });
		}
		return builder;
	}
	
	protected void init(){
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
	}
	
	public Context getMContext()
	{
		return mContext;
	}

}
