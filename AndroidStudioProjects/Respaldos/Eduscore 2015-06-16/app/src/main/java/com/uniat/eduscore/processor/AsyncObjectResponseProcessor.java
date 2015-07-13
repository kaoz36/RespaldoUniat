package com.uniat.eduscore.processor;

import android.os.AsyncTask;

import com.uniat.eduscore.interfaces.IAsyncStringResponseHandler;

import org.json.JSONObject;

/**
 * Created by Admin on 23/04/2015.
 */
public class AsyncObjectResponseProcessor extends AsyncTask<Object, Integer, Void> {
    protected IAsyncStringResponseHandler handler;
    protected JSONObject data;

    public AsyncObjectResponseProcessor(IAsyncStringResponseHandler handler, JSONObject data){
        this.handler = handler;
        this.data = data;
    }

    public AsyncObjectResponseProcessor(IAsyncStringResponseHandler handler){
        this.handler = handler;
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
