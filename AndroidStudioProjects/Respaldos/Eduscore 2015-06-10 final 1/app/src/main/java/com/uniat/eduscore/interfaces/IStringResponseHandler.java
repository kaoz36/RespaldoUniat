package com.uniat.eduscore.interfaces;

import org.json.JSONObject;

public interface IStringResponseHandler {
    void onResponse(String stringResponse) throws Exception;
    void onRequest();
    void onException(Exception e);
}
