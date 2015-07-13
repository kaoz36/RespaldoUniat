/** *************************************************************************
 *
 *   Copyright (c)  2012 by Jaguar Labs.
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
 * Last Revised By   : $Author:efren campillo
 * Last Checked In   : $Date: $
 * Last Version      : $Revision:  $
 *
 * Original Author   : efren campillo  -- efren.campillo@jaguarlabs.com
 * Origin            : SEnE -- november 1 @ 11:00 (PST)
 * Notes             :
 *
 * *************************************************************************/

package com.jaguarlabs.taxymobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class MyTask extends  AsyncTask<String, Integer, Boolean> {
	private ProgressDialog dialog;
	protected Context applicationContext;
	int execute = 0;
	
	public MyTask(Context context){
		applicationContext = context;
	}
	
	/*
	 * OnPreExcute
	 * 	this method is called before execute for do another validations
	 * 
	 * */
	
	@Override
	protected void onPreExecute() {
		this.dialog = ProgressDialog.show(applicationContext, "", "Checando conexión de internet.", true);
	}
	/*
	 * doInBackground method
	 * this method do all the Async task we can receive or manage another url
	 * 
	 * */
	@Override
	protected Boolean doInBackground(String... arg0) {
		Splash.mthis.log("tryuing", "canreach called execute = " + execute);
		return Splash.canReach(TaxyMobile.base_url);
	}
	/*
	 * onPostExecute
	 * this metod is called after finish the doInBackground
	 * for close the asynctask
	 * */
	@Override
	protected void onPostExecute(Boolean result) {
		Splash.mthis.log("result", ""+result);
		try{
			switch (execute) {
			case 0:
				Splash.mthis.start();
				break;
			case 1:
				TaxyMobile.mthis.call_cancel();
				TaxyMobile.mthis.findViewById(R.id.help_icon).setEnabled(true);
				break;
			case 2:
				TaxyMobile.mthis.call_taxy();
				break;
			case 3:
				TaxyMobile.mthis.call_cancel();
				Log.i("Status", TaxyMobile.mthis.status);
				TaxyMobile.mthis._setCronometro = false;
				TaxyMobile.mthis.contador = -1;	
				break;
			case 4:
				TaxyMobile.mthis.call_noAtend();
				TaxyMobile.mthis.contador = -1;	
				break;
			case 5:
				TaxyMobile.mthis.call_atendido();
				TaxyMobile.mthis.contador = -1;		
				break;
//			case 6:
//				TaxyMobile.mthis.
//				TaxyMobile.mthis.
//				break;
			default:
				break;
			}
			
		}catch(Exception e){
			Splash.mthis.log("errorMytask", ""+e);
		}	
		this.dialog.cancel();
	}
}