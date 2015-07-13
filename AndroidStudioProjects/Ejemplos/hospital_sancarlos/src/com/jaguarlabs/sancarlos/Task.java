package com.jaguarlabs.sancarlos;


import android.os.AsyncTask;
import android.util.Log;

public class Task extends  AsyncTask <String, Integer, String> {

	int post_case = 0;
	
	protected void onPreExecute() {
	}
	
	protected String doInBackground(String... arg0) {
		try{
			switch (post_case) {
			case 0:
				Thread.sleep(1000);
				break;
			default:
				break;
			}			
		}catch(Exception e){			
		}
		return null;
	}	

	protected void onPostExecute(String s) {
		Log.i("post", post_case + " ");
		switch ( post_case ) {
		case 0:	
			Splash.mthis.callback();
			break;
		default:
			break;
		}
	}
	
	
}
