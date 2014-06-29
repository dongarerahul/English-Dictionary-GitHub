package com.symantec.android.dictionary.contentprovider.api;

import android.net.Uri;

public interface DictionaryConstants {
	
	interface Columns {
		String ALIAS_WORD = "word";
		String ALIAS_DEFINITION = "definition";
	}
	
	String AUTHORITY = "com.symantec.android.contentprovider";
	String BASE_PATH = "dictionary";
	
	String DEFINE_PATTERN = "/+";
	Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
}