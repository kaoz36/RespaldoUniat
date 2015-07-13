package com.uniat.eduscore.com.uniat.eduscore.util;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Admin on 27/03/2015.
 */
public class Task extends AsyncTask<String, Integer, String> {

    public int post_case = 0;
    private ICallback call;

    public Task( ICallback call ){
        this.call = call;
    }

    protected void onPreExecute(){
        Log.i("Entrar", "MyAsyncTask");
    }

    protected String doInBackground(String... arg0){
        try {
            call.time();
        }catch (Exception e){
        }
        return null;
    }

    protected void onPostExecute(String s){
        call.callback();
    }

}
