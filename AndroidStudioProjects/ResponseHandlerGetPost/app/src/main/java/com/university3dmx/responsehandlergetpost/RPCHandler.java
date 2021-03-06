package com.university3dmx.responsehandlergetpost;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class RPCHandler extends AsyncTask<Object, Integer, RESTClient> {

    public static final String RPC_URL = "http://192.168.2.179/www/basura/";
    //public static final String RPC_URL = "http://192.168.2.20/webServices/";

    public static final String OPERATION_LOGIN = "login.php";

    public static final String OPERATION_GET_ALL_USERS = "getAllEmails.php";
    public static final String OPERATION_COUNT_EMAIL = "countEmails.php";
    public static final String OPERATION_GET_ALL_USERS2 = "getAllEmails2.php";

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
                    //post.put(item.getName(), item.getValue());
                    rest.AddParam(item.getName(), item.getValue());
                }
                //rest.setPost(post.toString());
                rest.Execute(RESTClient.RequestMethod.GET);
            }

        } catch (Exception e) {
            Log.e("Reques Error", "" + e);
        }
        return rest;
    }

    @Override
    protected void onPostExecute(RESTClient result) {
        try {
            //JSONObject response = new JSONObject( result.getResponse() );
            //handler.onResponse(result.getResponse());
        } catch (Exception error) {
            Log.e("RPC Handler", "Error on Processing Response");
            handler.onException(error);
        }
    }
}
