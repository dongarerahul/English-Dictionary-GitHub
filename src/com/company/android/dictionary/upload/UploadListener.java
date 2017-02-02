package com.company.android.dictionary.upload;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

import com.company.android.dictionary.R;
import com.company.android.dictionary.utilties.ActivityHelper;
import com.company.android.dictionary.utilties.Communicator;

public class UploadListener implements OnClickListener {

	private Activity activity;
	private ActivityHelper helper;
	private Communicator notifier;
	
	public UploadListener(Activity activity) {
		this.activity = activity;
		this.helper = new ActivityHelper(activity);
		this.notifier = new Communicator(activity);
	}

	@Override
	public void onClick(View view) {
		if (view.getId() != R.id.uploadButton) return;
		
		String filePath = helper.getText(R.id.filePathText);
		String separator = helper.getText(R.id.separatorText);
		
		if(separator == null || separator.length() == 0 ||
			filePath == null || filePath.length() == 0) {
			
			sendMessage(view, R.string.filepath_separator_prompt);
			return;
		}
		
		Reader reader = getInputStream(filePath);
		if (reader == null) {
			sendMessage(view, R.string.error_import_file_path);
			return;
		}
		
		new UploadOperation(activity).execute(reader, separator);
		helper.closeKeyboard(view);
	}

	private void sendMessage(View view, int messageId) {
		notifier.send(activity.getString(messageId));
		helper.closeKeyboard(view);
	}

	private Reader getInputStream(String filePath) {
		if(filePath == null || filePath.length() == 0) return null;
		
		Reader reader = null;
		File file = new File(filePath);
		try {
			if(file.exists()) {
				reader = new FileReader(file);
			} else {
				String path = "samples/" + filePath;
				InputStream resource = activity.getAssets().open(path);
				reader = new InputStreamReader(resource);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return reader;
	}
}
