package com.symantec.android.dictionary.contentprovider.lookup;

import android.content.Context;
import android.database.Cursor;

import com.symantec.android.dictionary.contentprovider.api.DictionaryConstants.Columns;
import com.symantec.android.dictionary.db.DictionaryDB;
import com.symantec.android.dictionary.db.IDictionaryDB;

public class LookupHelper {

	private final String[] arguments;
	private final String whereClause;
	private final Context context;
	private final IDictionaryDB dictionaryDB;

	public LookupHelper(Context context, String whereClause, String[] arguments) {
		this.context = context;
		this.whereClause = whereClause;
		this.arguments = arguments;
		dictionaryDB = DictionaryDB.getInstance(context);
	}

	public Cursor query() {
		LookupSanitizer sanitizer = new LookupSanitizer(context, whereClause, arguments);
		String wordEnquired = sanitizer.getArgument();
		String query = sanitizer.getQuery();
		
		return lookup(wordEnquired, query);
	}

	private Cursor lookup(String wordEnquired, String queryString) {
		Cursor cursor = dictionaryDB.query(queryString, wordEnquired);
		if(cursor == null || cursor.getCount() == 0) {
			queryString = DictionaryDB.getColumn(Columns.ALIAS_WORD) + " match ?";
			cursor = dictionaryDB.query(queryString, wordEnquired);
		}
		return cursor;
	}
}
