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
*   should not be construed as a commitment by JaguarLabs
*
* @(#)$Id: $
* Last Revised By   : $Author:efren campillo
* Last Checked In   : $Date: $
* Last Version      : $Revision:  $
*
* Original Author   : efren campillo  -- efren.campillo@jaguarlabs.com
* Origin            : SEnE -- november 21 @ 11:00 (PST)
* Notes             :
*
* *************************************************************************/

package com.jaguarlabs.taxymobile;



import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PostTask extends  AsyncTask<String, Integer, RESTClient> {
	private ProgressDialog dialog;
	protected Context applicationContext;
	int post_case 	= 0;
	boolean action	= false;

	/*
	 * OnPreExcute
	 * 	this method is called before execute for do another validations
	 * 
	 * */
	@Override
	protected void onPreExecute() {
		Log.i("onpreExcetuted", "starting posttask  with case: "+post_case);
		if(post_case != 4){
			this.dialog = ProgressDialog.show(applicationContext, "", "", true);
		}
	}
	/*
	 * doInBackground method
	 * this method do all the Async task we can receive or manage another url
	 * 
	 * */
	@Override
	protected RESTClient doInBackground(String... arg0) {
		//TaxyMobile.mthis.log("posting", "post case:"+post_case);
		
		switch(post_case){
			case 0:
				return TaxyMobile.mthis.post_createpeticion();
			case 1:				
				return TaxyMobile.mthis.post_cancelPeticion();
			case 2:
				return TaxyMobile.mthis.post_noAtendedPeticion();
			case 3:
				return Capture.mthis.post_register();
			case 4:
				return TaxyMobile.mthis.post_update();
			case 5:
				return FeedBack.mthis.post_feedback();
			case 6:
				return TaxyMobile.mthis.post_AtendedPeticion();
			case 7:
				TaxyMobile.mthis.clearApplicationData();
				break;
			case 8:
				return TaxyMobile.mthis.post_getLocalization();
			case 9:
			try {
				Thread.sleep(1500);
				return null;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		return null;
	}
	
	/*
	 * onPostExecute
	 * this metod is called after finish the doInBackground
	 * for close the asynctask
	 * */	
	@Override
	protected void onPostExecute(RESTClient result) {
		//TaxyMobile.mthis.log("result", ""+result.getResponse()); 
		try{
			if( result.getResponse() == null && post_case != 8 && post_case != 4 ){
				TaxyMobile.mthis.showAlertDialog("Error de conexión.", "Intenta mas tarde.");
				TaxyMobile.mthis.showCallTaxy();
				if( post_case != 4 )
					dialog.cancel();
				return;
			}else{
				JSONObject response = new JSONObject(result.getResponse());
				switch(post_case){
					case 0:///
						TaxyMobile.mthis.manage_createpeticion(response);
						break;
					case 1:////
						TaxyMobile.mthis.manage_cancelPeticion();
						break;
					case 2:	///
						TaxyMobile.mthis.manage_noAtendedPeticion();
						break;
					case 3:	//		
						Capture.mthis.manage_register(response);
						break;
					case 4:	///
						TaxyMobile.mthis.manage_update(response);
						break;
					case 5:	///
						FeedBack.mthis.manage_feedback();
						break;
					case 6: ///
						TaxyMobile.mthis.manage_atendedPeticion();
						break;
					case 7: ///
						TaxyMobile.mthis.reiniciarAplicacion();
						break;
					case 8:////
						if( result.getResponse() != null ){
							TaxyMobile.mthis.manage_getLocalization(response); 
						}							
						break;
					case 9:////
						TaxyMobile.mthis.log("si lo hace", "Entra al post de 9");
						TaxyMobile.mthis.menu( (LinearLayout)TaxyMobile.mthis.findViewById(R.id.menulayout), 
								(ImageView)TaxyMobile.mthis.findViewById(R.id.buttonmenu) );
						break;
				}
			}
			
		}catch(Exception e){
			//TaxyMobile.mthis.log("PostExecutedError", ""+e);
			e.printStackTrace();
			Log.i( "Catch post_case", post_case + "");
			if( post_case != 3 && post_case != 4){
				TaxyMobile.mthis.showCallTaxy();
				TaxyMobile.mthis.showAlertDialog("Error de conexión.", "Intenta mas tarde.");
				TaxyMobile.mthis.log("Error de conexión", "Intenta mas tarde");				
//				if( post_case != 4 )
//					dialog.cancel();
			}			
//			switch(post_case){
//				case 0://
//					//TODO:revert to the main page
//					TaxyMobile.mthis.showCallTaxy();
//					break;
//				case 1://TODO: show msj error
//					TaxyMobile.mthis.showAlertDialog("Error de conexión", "Intenta mas tarde");
//					TaxyMobile.mthis.showCallTaxy();
//					if( post_case != 4 )
//						dialog.cancel();
//					break;
//				case 2://
//					TaxyMobile.mthis.showAlertDialog("Error de conexión", "Intenta mas tarde");
//					TaxyMobile.mthis.showCallTaxy();
//					if( post_case != 4 )
//						dialog.cancel();
//					break;
//				case 3://TODO:show msg error
//					TaxyMobile.mthis.showAlertDialog("Error de conexión", "Intenta mas tarde");
//					TaxyMobile.mthis.showCallTaxy();
//					if( post_case != 4 )
//						dialog.cancel();
//					break;
//				case 4:///
//					break;	
//				case 5:	///
//					break;
//				case 6:	///
//					break;
//				case 7: //
//					break;
//				case 8: ////TODO:
//					TaxyMobile.mthis.showAlertDialog("Error de conexión", "Intenta mas tarde");
//					TaxyMobile.mthis.showCallTaxy();
//					if( post_case != 4 )
//						dialog.cancel();
//					break;
//			}			
		}	
		if(post_case!=4)
			dialog.cancel();
	}
}