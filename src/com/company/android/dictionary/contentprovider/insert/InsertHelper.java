package com.company.android.dictionary.contentprovider.insert;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.net.Uri;

import com.company.android.dictionary.R;
import com.company.android.dictionary.contentprovider.api.DictionaryConstants;
import com.company.android.dictionary.db.DictionaryDB;
import com.company.android.dictionary.db.IDictionaryDB;

public class InsertHelper {

	private final ContentValues values;
	private final Context context;
	private final IDictionaryDB dictionaryDB;

	public InsertHelper(Context context, ContentValues values) {
		this.context = context;
		this.values = values;
		dictionaryDB = DictionaryDB.getInstance(context);
	}

	public Uri insert() {
		InsertSanitizer sanitizer = new InsertSanitizer(context, values);
		ContentValues values = sanitizer.sanitize();
		
		Uri uriNew = insert(values);
		if(uriNew != null) return uriNew;

		String errorMessage = context.getString(R.string.error_dcp_insert);
		throw new SQLException(errorMessage);
	}

	private Uri insert(ContentValues values) {
		long rowId = dictionaryDB.insert(values);
		if(rowId > 0) {
			Uri uriNew = ContentUris.withAppendedId(DictionaryConstants.CONTENT_URI, rowId);
			context.getContentResolver().notifyChange(uriNew, null);
			return uriNew;
		}
		
		return null;
	}
}
