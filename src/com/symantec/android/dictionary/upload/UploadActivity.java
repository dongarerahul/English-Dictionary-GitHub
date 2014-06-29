package com.symantec.android.dictionary.upload;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.symantec.android.dictionary.R;
import com.symantec.android.dictionary.utilties.ActivityHelper;

public class UploadActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.import_activity);
		
		addListeners();
	}

	private void addListeners() {
		
		ActivityHelper helper = new ActivityHelper(this);
		Button importButton = helper.getButton(R.id.uploadButton);
		
		OnClickListener importListener = new UploadListener(this);
		importButton.setOnClickListener(importListener);
		
		Button cancelButton = helper.getButton(R.id.buttonCancel);
		setOnCancelClickListener(cancelButton);
	}

	private void setOnCancelClickListener(Button button) {
		button.setOnClickListener(new OnClickListener() {
			@Override 
			public void onClick(View view) {
				UploadActivity.this.finish();
			}
		});
	}
}
