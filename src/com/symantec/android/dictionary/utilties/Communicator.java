package com.symantec.android.dictionary.utilties;

import android.app.Activity;
import android.widget.Toast;

public class Communicator {

	private Activity activity;

	public Communicator(Activity activity) {
		this.activity = activity;
	}
	
	public void send(String message) {
		Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
	}
}
