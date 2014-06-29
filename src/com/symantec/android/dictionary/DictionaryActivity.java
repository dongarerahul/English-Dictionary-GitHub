package com.symantec.android.dictionary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.symantec.android.dictionary.upload.UploadActivity;

public class DictionaryActivity extends Activity  {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		
		OnClickListener lookupHandler = new LookupListener(this);
		Button lookupButton = (Button) findViewById(R.id.lookupButton);
		lookupButton.setOnClickListener(lookupHandler);
		
		OnClickListener defineHandler = new DefineListener(this);
		Button defineButton = (Button) findViewById(R.id.defineButton);
		defineButton.setOnClickListener(defineHandler);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.dictionary, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_import:
			Intent intent = new Intent(this, UploadActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}