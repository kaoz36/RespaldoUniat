package com.jaguarlabs.sipac.util;

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
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		handler.continueProcessing();
	}
	
	

}
