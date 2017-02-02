package com.company.android.dictionary.test;

import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.company.android.dictionary.R;
import com.company.android.dictionary.upload.UploadActivity;
import com.company.android.dictionary.utilties.ActivityHelper;

public class UploadActivityTest extends ActivityInstrumentationTestCase2<UploadActivity> {

	private Instrumentation instrument;
	private UploadActivity activity;
	private ActivityHelper helper;
	
	public UploadActivityTest() {
		super(UploadActivity.class);
	}

	@Override
	public void setUp() {
		instrument = getInstrumentation();
		activity = getActivity();
		helper = new ActivityHelper(activity);
	}
	
	public void testNull() {
		assertNotNull(instrument);
		assertNotNull(activity);
	}

	public void testCancel() {
		final ActivityMonitor monitor = instrument.addMonitor(
				UploadActivity.class.getName(), null, false);
		
		final Button cancelButton = helper.getButton(R.id.buttonCancel);
		
		activity.runOnUiThread(new Runnable() {
	        @Override public void run() {
	        	cancelButton.performClick();
	        }
	    });
		
		instrument.waitForMonitorWithTimeout(monitor, 5000);		
		//assertEquals(null, activity);
	}

	public void testUpload() throws InterruptedException {
		final EditText file = helper.getTextField(R.id.filePathText);
		final Button uploadButton = helper.getButton(R.id.uploadButton);
		
		activity.runOnUiThread(new Runnable() {
	        @Override public void run() {
	        	file.setText("datafile.csv");
	        	uploadButton.performClick();
	        }
	    });
		
		Thread.sleep(5000);
		
		LinearLayout layout = helper.getLayout(R.id.uploadResultLayout);
		assertEquals(4, layout.getChildCount());
		
		String totalActual = ((TextView)layout.getChildAt(0)).getText().toString();
		String successActual = ((TextView)layout.getChildAt(2)).getText().toString();
		String skipsActual = ((TextView)layout.getChildAt(3)).getText().toString();

		String totalExpected = activity.getString(R.string.upload_summary_message, 5);
		String successExpected = activity.getString(R.string.upload_insertions_message, 5);
		String skipsExpected = activity.getString(R.string.upload_skips_message, 0);
		
		assertEquals(totalExpected, totalActual);
		assertEquals(successExpected, successActual);
		assertEquals(skipsExpected, skipsActual);
	}
}
