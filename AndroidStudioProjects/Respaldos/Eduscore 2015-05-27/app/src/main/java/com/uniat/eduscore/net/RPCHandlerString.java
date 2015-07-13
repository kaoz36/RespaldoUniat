package com.uniat.eduscore.net;

import android.os.AsyncTask;
import android.util.Log;

import com.uniat.eduscore.interfaces.IStringResponseHandler;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

public class RPCHandlerString extends AsyncTask<Object, Integer, RESTClient> {

    public static final String RPC_URL = "http://192.168.2.179/www/basura/";

    public static final String OPERATION_LOGIN = "login.php";

    public static final String OPERATION_GET_ALL_USERS = "getAllUsers.php";
    public static final String OPERATION_GET_ALL_CAREERS = "getAllCareers.php";
    public static final String OPERATION_GET_ALL_CAREERTYPES = "getAllCareerTypes.php";
    public static final String OPERATION_GET_ALL_INTEREST = "getAllInterest.php";
    public static final String OPERATION_GET_ALL_CHARGES = "getAllCharges.php";
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

    public static final String OPERATION_COUNT_USERS = "countUsers.php";
    public static final String OPERATION_COUNT_CAREERS = "countCareers.php";
    public static final String OPERATION_COUNT_CAREERTYPES = "countCareersType.php";
    public static final String OPERATION_COUNT_MEDIAS = "countMedia.php";
    public static final String OPERATION_COUNT_CAMPUS = "countCampus.php";
    public static final String OPERATION_COUNT_ORIGINS = "countOrigins.php";
    public static final String OPERATION_COUNT_EVENTS = "countEvents.php";
    public static final String OPERATION_COUNT_COUNTRYS = "countCountries.php";
    public static final String OPERATION_COUNT_REGIONS = "countRegions.php";
    public static final String OPERATION_COUNT_CITYS = "countCities.php";
    public static final String OPERATION_COUNT_EMAILS = "countEmails.php";


    private IStringResponseHandler handler;

    public RPCHandlerString(IStringResponseHandler responseHandler) {
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
            handler.onResponse(result.getResponse());
        } catch (Exception error) {
            Log.e("RPC Handler", result.getResponse());
            Log.e("RPC Handler", "Error on Processing Response");
            handler.onException(error);
        }
    }
}
