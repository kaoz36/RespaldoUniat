package com.jaguarlabs.xlr8.util;

import android.os.AsyncTask;

public class TaskInit extends AsyncTask<String, Boolean, Boolean> {

	private ICallback callback;

	public TaskInit(ICallback callback) {
		this.callback = callback;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	@Override
	protected Boolean doInBackground(String... params) {
		callback.time();
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		callback.callback();
	}
}