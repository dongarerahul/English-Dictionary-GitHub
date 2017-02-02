package com.company.android.dictionary.upload;

import java.io.Reader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;

import com.company.android.dictionary.R;
import com.company.android.dictionary.db.DictionaryDB;
import com.company.android.dictionary.db.IDictionaryDB;
import com.company.android.dictionary.upload.UploadStats.Result;
import com.company.android.dictionary.upload.io.DictionaryReader;
import com.company.android.dictionary.upload.io.DictionaryWriter;
import com.company.android.dictionary.upload.io.IDictionaryReader;
import com.company.android.dictionary.upload.io.IDictionaryWriter;
import com.company.android.dictionary.utilties.ActivityHelper;
import com.company.android.dictionary.utilties.Communicator;

public class UploadOperation extends AsyncTask<Object, Void, UploadStats> {

	private static final int BATCH_SIZE = 500;
	//API 11 ASyncTask
	private final Activity activity;
	private final ProgressDialog dialog;
	private final Communicator notifier;
	private final ActivityHelper helper;
	
	private long startTime;
	
    public UploadOperation(Activity activity) {
    	this.activity = activity;
    	dialog = new ProgressDialog(activity);
    	notifier = new Communicator(activity);
    	helper = new ActivityHelper(activity);
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage(activity.getString(R.string.upload_dialog_wait_message));
        dialog.setTitle(activity.getString(R.string.upload_dailog_title));
        dialog.setCancelable(false);
        dialog.show();
        startTime = System.currentTimeMillis();
    }   

    // automatically done on worker thread (separate from UI thread)
    protected UploadStats doInBackground(final Object... arguments) {
    	UploadStats result = new UploadStats(Result.FAILURE, 0);
    	
    	if(arguments == null || arguments.length != 2 ||
           arguments[0] == null || arguments[1] == null) return result;
        
        Reader file = (Reader)arguments[0];
		String delimiter = (String)arguments[1];
		
		return upload(file, delimiter);
    }

    private UploadStats upload(Reader file, String delimiter) {
    	Context context = activity.getApplicationContext();
		IDictionaryDB database = DictionaryDB.getInstance(context);
		
		IDictionaryReader reader = new DictionaryReader(file, delimiter, BATCH_SIZE);
		IDictionaryWriter writer = new DictionaryWriter(database, reader);
		writer.insertBatch();
		
		UploadStats result = writer.getResult();
		result.setSkips(reader.getSkipCount());
		return result;
	}

	@Override
    protected void onPostExecute(UploadStats result) {

    	if(dialog.isShowing()) dialog.dismiss();
        
    	long timeTaken = (System.currentTimeMillis() - startTime) / 1000;
        result.setTimeTaken(timeTaken);
    	postResults(result);
        
    	notifier.send(getMessage(result));
        
        Button view = helper.getButton(R.id.uploadButton);
		helper.closeKeyboard(view);
    }

	private void postResults(UploadStats result) {
		int insertionsCount = result.getInsertionCount();
		int skipCount = result.getSkipCount();
		int total = insertionsCount + skipCount;
		long timeTaken = result.getTimeTaken();
		
		String insertions = activity.getString(R.string.upload_insertions_message, insertionsCount);
		String skips = activity.getString(R.string.upload_skips_message, skipCount);
		String summary = activity.getString(R.string.upload_summary_message, total);
		String time = activity.getString(R.string.upload_time_taken, timeTaken);
		
		helper.getLayout(R.id.uploadResultLayout).removeAllViews();
		
		helper.addTextView(R.id.uploadResultLayout, skips);
		helper.addTextView(R.id.uploadResultLayout, insertions);
		helper.addTextView(R.id.uploadResultLayout, time);
		helper.addTextView(R.id.uploadResultLayout, summary);
	}

	private String getMessage(UploadStats code) {
		String message = "Failed To Import File !";
        
        switch(code.getResult()) {
        case SUCCESS :
        	message = activity.getString(R.string.success_upload);
    		break;
        case INVALID_ARGUMENTS :
        	message = activity.getString(R.string.error_upload_invalid_argument);
        	break;
        case DB_FAILURE :
        	message = activity.getString(R.string.error_upload_database_error);
        	break;
    	default :
    		message = activity.getString(R.string.error_upload_other);
        }
		return message;
	}
}