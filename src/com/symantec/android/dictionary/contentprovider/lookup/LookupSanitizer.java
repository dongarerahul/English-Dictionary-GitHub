package com.symantec.android.dictionary.contentprovider.lookup;

import android.content.Context;

import com.symantec.android.dictionary.R;
import com.symantec.android.dictionary.contentprovider.api.DictionaryConstants.Columns;
import com.symantec.android.dictionary.db.DictionaryDB;
import com.symantec.android.dictionary.utilties.StringSanitizer;

public class LookupSanitizer {

	private final String[] arguments;
	private final Context context;
	private final StringSanitizer sanitizer;
	public static final int WORD_MAX_LENGTH = 50;
	
	public LookupSanitizer(Context context, String query, String[] arguments) {
		this.context = context;
		this.arguments = arguments;
		this.sanitizer = StringSanitizer.getInstance();
	}

	public String getArgument() {
		if(arguments == null || 
		   arguments.length != 1 || 
		   arguments[0] == null || 
		   arguments[0].length() > WORD_MAX_LENGTH) {
			
			String message = context.getString(R.string.error_dcp_query_invalid_args);
			throw new IllegalArgumentException(message);
		}
		
		return sanitizer.sanitize(arguments[0].trim());
	}

	public String getQuery() {
		/*UrlQuerySanitizer sanitizer = new UrlQuerySanitizer();
		sanitizer.registerParameter("word", 
				new IllegalCharacterValueSanitizer(IllegalCharacterValueSanitizer.ALL_OK));*/
		return DictionaryDB.getColumn(Columns.ALIAS_WORD) + "=? COLLATE NOCASE";
	}
}
