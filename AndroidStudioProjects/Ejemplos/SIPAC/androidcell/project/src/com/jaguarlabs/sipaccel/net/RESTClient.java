/* *************************************************************************
 *
 *   Copyright (c)  2012 by Jaguar Labs.
 *   Confidential and Proprietary
 *   All Rights Reserved
 *
 *   This software is furnished under license and may be used and copied
 *   only in accordance with the terms of its license and with the
 *   inclusion of the above copyright notice. This software and any other
 *   copies thereof may not be provided or otherwise made available to any
 *   other party. No title to and/or ownership of the software is hereby
 *   transferred.
 *
 *   The information in this software is subject to change without notice and
 *   should not be construed as a commitment by JaguarLabs.
 *
 * @(#)$Id: $
 * Last Revised By   : $Author:efren campillo
 * Last Checked In   : $Date: $
 * Last Version      : $Revision:  $
 *
 * Original Author   : efren campillo  -- efren.campillo@jaguarlabs.com
 * Origin            : SEnE -- november 2 @ 11:00 (PST)
 * Notes             :
 *
 * *************************************************************************/

package com.jaguarlabs.sipaccel.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;


public class RESTClient {

	public static enum RequestMethod {
		GET, POST
	}

	private ArrayList<NameValuePair> params;
	private ArrayList<NameValuePair> headers;
	private String post;
	private String url;

	private int responseCode;
	private String message;
	private String response;

/*
 * default methods to manage the restclient
 * */
	
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getResponse() {
		return response;
	}
	public String getErrorMessage() {
		return message;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public RESTClient(String url) {
		this.url = url;
		params = new ArrayList<NameValuePair>();
		headers = new ArrayList<NameValuePair>();
	}

	public void AddParam(String name, String value) {
		params.add(new BasicNameValuePair(name, value));
	}

	public void AddHeader(String name, String value) {
		headers.add(new BasicNameValuePair(name, value));
	}
/*
 * execute the post defined to the url defined
 * 
 * */
	public void Execute(RequestMethod method) throws Exception {
		switch (method) {
		case GET: {
			// add parameters
			String combinedParams = "";
			if (!params.isEmpty()) {
				combinedParams += "?";
				for (NameValuePair p : params) {
					String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(), "UTF-8");
					if (combinedParams.length() > 1) {
						combinedParams += "&" + paramString;
					} else {
						combinedParams += paramString;
					}
				}
			}
			HttpGet request = new HttpGet(url + combinedParams);
			// add headers
			for (NameValuePair h : headers) {
				request.addHeader(h.getName(), h.getValue());
			}
			executeRequest(request, url);
			break;
		}
		case POST: {			
			HttpPost request = new HttpPost(url);
			request.setHeader("Content-type","application/json");
			// add headers
			for (NameValuePair h : headers) {
				request.addHeader(h.getName(), h.getValue());
				//Log.i("headeradding",""+h.getName()+","+h.getValue());
			}		
			//if (!params.isEmpty()) {
				Log.i("addingjson","ading to post the json");
				request.setEntity(new ByteArrayEntity(post.toString().getBytes("UTF8")));
				Log.i("posting",""+post.toString()+" in "+url );
			//}		
			
			executeRequest(request, url);
			break;
		}
		}
	}
/*
 * executing recuest from post in execute restclient
 * 
 * */
	private void executeRequest(HttpUriRequest request, String url) {
		HttpClient client = new DefaultHttpClient();
		HttpResponse httpResponse;
		try {
			httpResponse = client.execute(request);
			responseCode = httpResponse.getStatusLine().getStatusCode();
			message = httpResponse.getStatusLine().getReasonPhrase();
			HttpEntity entity = httpResponse.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				response = convertStreamToString(instream);
				instream.close();
			}
		} catch (ClientProtocolException e) {
			client.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (IOException e) {
			client.getConnectionManager().shutdown();
			e.printStackTrace();
		}
	}
/*
 * 
 * convert the Stream from the request in a String readeable to the rest client
 * 
 * */
	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
}
