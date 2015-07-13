package com.uniat.eduscore.processor;

import android.os.AsyncTask;

import com.uniat.eduscore.interfaces.IAsyncJSONResponseHandler;

import org.json.JSONArray;

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
