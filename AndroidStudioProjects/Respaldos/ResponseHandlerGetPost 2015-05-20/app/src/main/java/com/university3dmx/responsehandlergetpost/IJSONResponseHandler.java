package com.university3dmx.responsehandlergetpost;

import org.json.JSONObject;

public interface IJSONResponseHandler {
    void onResponse( String jsonResponse ) throws Exception;
    void onRequest();
    void onException(Exception e);
}
