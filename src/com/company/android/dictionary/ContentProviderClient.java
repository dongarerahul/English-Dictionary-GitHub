package com.company.android.dictionary;

import static com.company.android.dictionary.contentprovider.api.DictionaryConstants.CONTENT_URI;
import static com.company.android.dictionary.contentprovider.api.DictionaryConstants.Columns.ALIAS_DEFINITION;
import static com.company.android.dictionary.contentprovider.api.DictionaryConstants.Columns.ALIAS_WORD;
import android.app.Activity;
import android.database.Cursor;

public class ContentProviderClient {

	private Activity activity;

	public ContentProviderClient(Activity activity) {
		this.activity = activity;
	}
	
	public Cursor lookup(String word) {
		//boolean result = true;
		String[] arguments = new String[] { word };
		String[] projections = new String[] { ALIAS_DEFINITION, ALIAS_WORD };
		
		return activity.getContentResolver().query(
				CONTENT_URI, 		// content URI of the words table
				projections, 		// The columns to return for each row
				ALIAS_WORD + "=?",	// Either null, or the word the user entered
				arguments, 			// Either empty, or the string the user entered
				null); 				// The sort order for the returned rows

		//cursor.close();
		//return result;
	}
}
