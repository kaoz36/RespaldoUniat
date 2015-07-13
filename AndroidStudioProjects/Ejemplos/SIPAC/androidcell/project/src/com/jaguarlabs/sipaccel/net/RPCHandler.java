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

package com.jaguarlabs.sipaccel.net;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.jaguarlabs.sipaccel.util.IJSONResponseHandler;

public class RPCHandler extends AsyncTask<Object, Integer, RESTClient> {

	public static final String RPC_URL = "http://sipac-app.com.mx/index.php/";

	public static final String OPERATION_LOGIN = "usuariocontroller/userLogin";
	public static final String OPERATION_REGISTER_LOCATION = "promotorialocationcontroller/registerPromotoria";
	public static final String OPERATION_GET_ALL_LOCATIONS = "promotorialocationcontroller/getAllPromotorias";

	public static final String OPERATION_GET_ALL_PROFESIONES = "cotizadorcontroller/getAllProfesionesCotizador";
	public static final String OPERATION_GET_ALL_EDADES = "cotizadorcontroller/getAllEdadesCotizador";
	public static final String OPERATION_GET_ALL_PROSPECCIONES = "prospeccioncontroller/getAllProspecciones";
	
	public static final String OPERATION_SAVE_SEARCH = "usuariocontroller/registerHistory";
	public static final String OPERATION_SAVE_LOCATION = "promotorialocationcontroller/saveLocation";
	public static final String OPERATION_SAVE_CHEKIN = "promotorialocationcontroller/saveChekin";

	public static final String OPERATION_BACKUP_POLIZAS = "promotorialocationcontroller/backupPolizas/";
	public static final String OPERATION_BACKUP_COBERTURAS = "promotorialocationcontroller/backupCoberturas/";
	public static final String OPERATION_BACKUP_SERVICIOS = "promotorialocationcontroller/backupServicios/";
	public static final String OPERATION_BACKUP_AFECTACION = "promotorialocationcontroller/backupServiciosAfectacion/";
	public static final String OPERATION_BACKUP_SERVINTERNOS = "promotorialocationcontroller/backupServiciosInternos/";
	public static final String OPERATION_BACKUP_PROSPECCIONES = "promotorialocationcontroller/backupProspecciones/";
	
	
	private IJSONResponseHandler handler;

	public RPCHandler(IJSONResponseHandler responseHandler) {
		this.handler = responseHandler;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		handler.onRequest();
	}

	@Override
	protected RESTClient doInBackground(Object... params) {
		int index = 0;
		NameValuePair item;
		JSONObject post = new JSONObject();
		RESTClient rest = new RESTClient(RPC_URL);
		try {
			if (params.length > 0) {
				rest.setUrl(rest.getUrl() + params[0]);
				++index;
				for (; index < params.length; index++) {
					item = (NameValuePair) params[index];
					post.put(item.getName(), item.getValue());
				}
				rest.setPost(post.toString());
				rest.Execute(RESTClient.RequestMethod.POST);
			}

		} catch (Exception e) {
			Log.e("Reques Error", "" + e);
		}
		return rest;
	}

	@Override
	protected void onPostExecute(RESTClient result) {
		try {
			JSONObject response = new JSONObject( result.getResponse() );
			handler.onResponse(response);
		} catch (Exception error) {
			Log.e("RPC Handler", "Error on Processing Response");
			handler.onException(error);
		}
	}
}
