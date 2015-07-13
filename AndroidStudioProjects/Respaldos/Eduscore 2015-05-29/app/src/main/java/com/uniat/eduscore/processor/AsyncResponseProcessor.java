package com.uniat.eduscore.processor;

import android.os.AsyncTask;

import com.uniat.eduscore.interfaces.IAsyncStringResponseHandler;

public class AsyncResponseProcessor extends AsyncTask<Object, Integer, Void> {

    protected IAsyncStringResponseHandler handler;
    protected String data;
    public AsyncResponseProcessor(IAsyncStringResponseHandler handler, String data){
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
