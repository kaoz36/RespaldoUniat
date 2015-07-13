package com.jaguarlabs.sipac.util;

import org.json.JSONObject;

public interface IJSONResponseHandler {
	void onResponse(JSONObject jsonResponse)throws Exception;
	void onRequest();
	void onException(Exception e);
}
