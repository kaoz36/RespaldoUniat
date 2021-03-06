package com.uniat.eduscore.net;

import android.os.AsyncTask;
import android.util.Log;

import com.uniat.eduscore.interfaces.IJSONResponseHandler;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

public class RPCHandler extends AsyncTask<Object, Integer, RESTClient> {

    public static final String RPC_URL = "http://192.168.2.179/www/basura/";
    //public static final String RPC_URL = "http://192.168.2.20/webServices/";

    public static final String OPERATION_LOGIN = "login.php";

    public static final String OPERATION_GET_ALL_USERS = "getAllUsers.php";
    public static final String OPERATION_GET_ALL_CAREERS = "getAllCareers.php";
    public static final String OPERATION_GET_ALL_CAREERTYPES = "getAllCareerTypes.php";
    public static final String OPERATION_GET_ALL_PERSONS = "getAllPersons.php";
    public static final String OPERATION_GET_ALL_PERSONDESCRIPTIONS = "getAllPersonDescriptions.php";
    public static final String OPERATION_GET_ALL_MEDIAS = "getAllMedias.php";
    public static final String OPERATION_GET_ALL_CAMPUS = "getAllCampus.php";
    public static final String OPERATION_GET_ALL_ORIGINS = "getAllOrigins.php";
    public static final String OPERATION_GET_ALL_EVENTS = "getAllEvents.php";
    public static final String OPERATION_GET_ALL_COUNTRYS = "getAllCountrys.php";
    public static final String OPERATION_GET_ALL_REGIONS = "getAllRegions.php";
    public static final String OPERATION_GET_ALL_CITYS = "getAllCitys.php";
    public static final String OPERATION_GET_ALL_EMAILS = "getAllEmails.php";

    public static final String OPERATION_INSERT_PERSON = "insertPerson.php";
    public static final String OPERATION_INSERT_PERSON_DESCRIPTION = "insertPersonDescriptions.php";
    public static final String OPERATION_INSERT_EMAILS = "insertEmail.php";
    public static final String OPERATION_INSERT_TELEPHONE = "insertTelephone.php";
    public static final String OPERATION_INSERT_INTEREST = "insertInterest.php";

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
            JSONObject response = new JSONObject( result.getResponse() );
            handler.onResponse(response);
        } catch (Exception error) {
            Log.e("RPC Handler", "Error on Processing Response");
            handler.onException(error);
        }
    }
}
