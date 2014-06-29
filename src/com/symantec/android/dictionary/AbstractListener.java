package com.symantec.android.dictionary;

import android.app.Activity;
import android.view.View;

import com.symantec.android.dictionary.utilties.ActivityHelper;
import com.symantec.android.dictionary.utilties.Communicator;

public class AbstractListener {

	protected final Activity activity;
	protected final Communicator notifier;
	protected final ActivityHelper helper;
	protected final DictionaryModel model;

	protected AbstractListener(Activity activity) {
		this.activity = activity;
		helper = new ActivityHelper(activity);
		notifier = new Communicator(activity);
		model = new DictionaryModel(activity);
	}
	
	protected void sendMessage(View view, int messageId) {
		notifier.send(activity.getString(messageId));
		helper.closeKeyboard(view);
	}

	protected boolean validate(String text) {
		return (text == null || text.length() == 0) ?
		   false : true;
	}

}
