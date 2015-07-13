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

import org.json.JSONArray;

import android.os.AsyncTask;

public class AsyncResponseProcessor extends AsyncTask<Object, Integer, Void> {

	protected IAsyncJSONResponseHandler handler;
	protected JSONArray data;
	public AsyncResponseProcessor(IAsyncJSONResponseHandler handler, JSONArray data){
		this.handler = handler;
		this.data = data;
	}
	
	@Override
	protected Void doInBackground(Object... params) {
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		return null ;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		handler.continueProcessing();
	}
	
	

}
