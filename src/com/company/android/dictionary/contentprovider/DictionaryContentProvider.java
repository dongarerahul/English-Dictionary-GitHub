package com.company.android.dictionary.contentprovider;

import static com.company.android.dictionary.contentprovider.api.DictionaryConstants.AUTHORITY;
import static com.company.android.dictionary.contentprovider.api.DictionaryConstants.BASE_PATH;
import static com.company.android.dictionary.contentprovider.api.DictionaryConstants.DEFINE_PATTERN;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.company.android.dictionary.contentprovider.insert.InsertHelper;
import com.company.android.dictionary.contentprovider.lookup.LookupHelper;

public class DictionaryContentProvider extends ContentProvider {

	static final class Operation {
		public static final int LOOKUP = 0;
		public static final int DEFINE = 1;
	}
	
	public static final String DEFINITION_MIME_TYPE = "com.company.android.dictionary.cursor/definition";
	//private static final String WORD_MIME_TYPE = "com.company.android.dictionary.cursor/word";
	
	private static final UriMatcher matcher = buildURIMatcher();

	private static UriMatcher buildURIMatcher() {
		UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		matcher.addURI(AUTHORITY, BASE_PATH, Operation.LOOKUP);
		matcher.addURI(AUTHORITY, BASE_PATH + DEFINE_PATTERN, Operation.DEFINE);
		return matcher;
	}

	private Context context;

	@Override
	public boolean onCreate() {
		context = getContext();
	    return true;
	}

	@Override	
	public Uri insert(Uri uri, ContentValues values) {
		if(matcher.match(uri) != Operation.DEFINE) {
			throw new IllegalArgumentException("Invalid URI: " + uri);
		}
		
		return new InsertHelper(context, values).insert();
	}

	@Override
	public Cursor query(Uri uri, String[] columnsReturning, String whereClause, String[] arguments, String sortOrder) {
		if(matcher.match(uri) != Operation.LOOKUP) {
			throw new IllegalArgumentException("Invalid URI: " + uri);
		}
		
		return new LookupHelper(context, whereClause, arguments).query();
	}

	@Override 
	public String getType(Uri uri) {
		final int uriType = matcher.match(uri);
		
		switch(uriType) {
		case Operation.LOOKUP :
			return DEFINITION_MIME_TYPE;
		case Operation.DEFINE :
			return ContentResolver.CURSOR_DIR_BASE_TYPE;
		default : 
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		throw new UnsupportedOperationException();
	}
}
