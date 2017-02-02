package com.company.android.dictionary.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.company.android.dictionary.DictionaryActivity;
import com.company.android.dictionary.R;
import com.company.android.dictionary.upload.UploadActivity;
import com.company.android.dictionary.utilties.ActivityHelper;

public class DictionaryActivityTest extends ActivityInstrumentationTestCase2<DictionaryActivity> {

	private Instrumentation instrument;
	private DictionaryActivity activity;
	private ActivityHelper helper;
	
	public DictionaryActivityTest() {
		super(DictionaryActivity.class);
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
	
	public void testOnOptionsItemSelected() {
		ActivityMonitor monitor = instrument.addMonitor(UploadActivity.class.getName(), null, false);
		
		instrument.sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
		instrument.invokeMenuActionSync(activity, R.id.menu_import, 0);
		
		Activity uploadActivity = instrument.waitForMonitorWithTimeout(monitor, 1000);
		assertEquals(true, instrument.checkMonitorHit(monitor, 1));
		uploadActivity.finish();
	}
	
	@UiThreadTest
	public void testDefineLookup() {
		
		long time = System.currentTimeMillis();
		String newWord = "TW" + time;
		String newDefinition = "TM" + time;
		
		addWord(newWord, newDefinition);
		assertEquals(newDefinition, lookupWord(newWord)); 
	}
	
	@UiThreadTest
	public void testNullBlankWordDefinition() {
		addWord(null, null);
		assertEquals(null, lookupWord(null));
		
		addWord("", "Blank");
		assertEquals(null, lookupWord("")); 
		
		String word = "word" + System.currentTimeMillis();
		addWord(word, "");
		assertEquals(null, lookupWord(word)); 
	}

	private String lookupWord(String word) {
		helper.getTextField(R.id.wordText).setText(word);
		Button lookup = helper.getButton(R.id.lookupButton);
		lookup.performClick();
		
		LinearLayout layout = helper.getLayout(R.id.definitionLayout);
		if(layout.getChildCount() == 0) return null;
		
		TextView textView = (TextView)layout.getChildAt(0);
		return textView.getText().toString();
	}

	private void addWord(String word, String definition) {
		
		EditText defineText = helper.getTextField(R.id.definitionText);
		EditText wordText = helper.getTextField(R.id.wordText);
		
		wordText.setText(word);
		defineText.setText(definition);
		
		Button define = helper.getButton(R.id.defineButton);		
		define.performClick();
	}
}
