package com.university3dmx.pruebapost;

import android.view.KeyEvent;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;


/**
 * Created by Admin on 24/04/2015.
 */
public class httpHandler {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    public Call get(String url, Callback callback) throws IOException {
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(callback);
            return call;
        }catch (Exception e){
            Call call = null;
            return call;
        }
    }

    public String getS(String url, Callback callback) throws IOException {
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            /*Call call = client.newCall(request);
            call.enqueue(callback);
            return "";*/
            Response response = client.newCall(request).execute();
            return response.body().string();
        }catch (Exception e){
            return "Error";
        }
    }

    public String post(String posturl, String json){

        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(posturl)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }

        catch(Exception e) { return "error";}
    }

    public Call postCall(String posturl, String json, Callback callback) throws IOException{

        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(posturl)
                    .post(body)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(callback);
            return call;
        }

        catch(Exception e) { return null;}
    }


}
