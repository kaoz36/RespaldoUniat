package com.university3dmx.responsehandlergetpost;

import org.json.JSONObject;

public interface IJSONResponseHandler {
    void onResponse( JSONObject jsonResponse ) throws Exception;
    void onRequest();
    void onException(Exception e);
}
