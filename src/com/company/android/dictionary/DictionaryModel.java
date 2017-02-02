package com.company.android.dictionary;

import static com.company.android.dictionary.contentprovider.api.DictionaryConstants.CONTENT_URI;
import static com.company.android.dictionary.contentprovider.api.DictionaryConstants.DEFINE_PATTERN;
import static com.company.android.dictionary.contentprovider.api.DictionaryConstants.Columns.ALIAS_DEFINITION;
import static com.company.android.dictionary.contentprovider.api.DictionaryConstants.Columns.ALIAS_WORD;

import java.util.ArrayList;
import java.util.List;

import com.company.android.dictionary.utilties.StringSanitizer;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class DictionaryModel {
	
	private final ContentProviderClient client;
	private final Activity activity;
	
	public DictionaryModel(Activity activity) {
		this.activity = activity;
		client = new ContentProviderClient(activity);
	}

	public List<String> lookup(String word) {
		Cursor cursor = client.lookup(word);
		Log.d("DictionaryModel", "Row Count in Lookup: " + cursor.getCount());
		
		List<String> definitions = new ArrayList<String>();
		if (cursor.moveToFirst() == false)	return definitions;
		
		int indexDefinition = cursor.getColumnIndex(ALIAS_DEFINITION);
		do {
			String definition = cursor.getString(indexDefinition);
			definition = StringSanitizer.getInstance().deSanitize(definition); 
			definitions.add(definition);
		} while (cursor.moveToNext());
		
		cursor.close();
		return definitions;
	}

	public Uri addDefinition(String word, String definition) {
		ContentValues values = new ContentValues();
		values.put(ALIAS_WORD, word);
		values.put(ALIAS_DEFINITION, definition);

		Uri uri = Uri.withAppendedPath(CONTENT_URI, DEFINE_PATTERN);
		return activity.getContentResolver().insert(uri, values);
	}
}
