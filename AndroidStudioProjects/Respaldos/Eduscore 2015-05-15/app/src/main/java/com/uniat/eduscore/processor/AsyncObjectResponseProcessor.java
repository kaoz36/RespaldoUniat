package com.uniat.eduscore.processor;

import android.os.AsyncTask;

import com.uniat.eduscore.interfaces.IAsyncJSONResponseHandler;

import org.json.JSONObject;

/**
 * Created by Admin on 23/04/2015.
 */
public class AsyncObjectResponseProcessor extends AsyncTask<Object, Integer, Void> {
    protected IAsyncJSONResponseHandler handler;
    protected JSONObject data;

    public AsyncObjectResponseProcessor(IAsyncJSONResponseHandler handler, JSONObject data){
        this.handler = handler;
        this.data = data;
    }

    public AsyncObjectResponseProcessor(IAsyncJSONResponseHandler handler){
        this.handler = handler;
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
