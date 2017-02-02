package com.company.android.dictionary.test;

import static com.company.android.dictionary.contentprovider.api.DictionaryConstants.CONTENT_URI;
import static com.company.android.dictionary.contentprovider.api.DictionaryConstants.DEFINE_PATTERN;
import static com.company.android.dictionary.contentprovider.api.DictionaryConstants.Columns.ALIAS_DEFINITION;
import static com.company.android.dictionary.contentprovider.api.DictionaryConstants.Columns.ALIAS_WORD;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import com.company.android.dictionary.R;
import com.company.android.dictionary.contentprovider.DictionaryContentProvider;
import com.company.android.dictionary.contentprovider.api.DictionaryConstants;
import com.company.android.dictionary.contentprovider.insert.InsertSanitizer;

public class DictionaryContentProviderTest extends ProviderTestCase2<DictionaryContentProvider> {

	private MockContentResolver resolver;
	private final String maxWord = 
			"fafdffdsafafdffdsafafdffdsafafdffdsafafdffdsafafdZ"; // 50 
	private final String maxDefinition = 
			"fafdffdsafafdffdsafafdffdsafafdffdsafafdffdsafafdZ" +
			"fafdffdsafafdffdsafafdffdsafafdffdsafafdffdsafafdZ" +
			"fafdffdsafafdffdsafafdffdsafafdffdsafafdffdsafafdZ" +
			"fafdffdsafafdffdsafafdffdsafafdffdsafafdffdsafafdZ" +
			"fafdffdsafafdffdsafafdffdsafafdffdsafafdffdsafafdZ" +
			"fafdffdsafafdffdsafafdffdsafafdffdsafafdffdsafafdZ" +
			"fafdffdsafafdffdsafafdffdsafafdffdsafafdffdsafafdZ" +
			"fafdffdsafafdffdsafafdffdsafafdffdsafafdffdsafafdZ" +
			"fafdffdsafafdffdsafafdffdsafafdffdsafafdffdsafafdZ" +
			"fafdffdsafafdffdsafafdffdsafafdffdsafafdffdsafafdZ";
	
	public DictionaryContentProviderTest() {
		super(DictionaryContentProvider.class, DictionaryConstants.AUTHORITY);
	}
	
	@Override
    protected void setUp() throws Exception {
		super.setUp();
		resolver = getMockContentResolver();
	}
	
	public void testOnCreate() {
		assertNotNull(resolver);
	}

	public void testInsertUriContentValues() {
		Uri uri = insertRecord("TW" + System.currentTimeMillis(), "Test Word");
		assertNotNull(uri);
	}

	private Uri insertRecord(String word, String definition) {
		ContentValues values = new ContentValues();
		values.put(ALIAS_WORD, word);
		values.put(ALIAS_DEFINITION, definition);

		Uri uri = Uri.withAppendedPath(CONTENT_URI, DEFINE_PATTERN);
		return resolver.insert(uri, values);
	}

	public void testLookup() {
		String word = "tw" + System.currentTimeMillis();
		String definition = "Test Word";
		
		insertRecord(word, definition);
		
		Cursor cursor = lookup(word);
		assertEquals(definition, getDefinition(cursor));
	}

	private Cursor lookup(String word) {
		String[] arguments = new String[] { word };
		String[] projections = new String[] { ALIAS_DEFINITION, ALIAS_WORD };
		
		Cursor cursor = resolver.query(
				CONTENT_URI, 		// content URI of the words table
				projections, 		// The columns to return for each row
				ALIAS_WORD + "=?",	// Either null, or the maxWord the user entered
				arguments, 			// Either empty, or the string the user entered
				null); 				// The sort order for the returned rows
		
		return cursor;
	}

	@SuppressLint("DefaultLocale")
	public void testMatch() {
		String word = "Match" + System.currentTimeMillis();
		String definition = "Match Word";
		
		assertNotNull(insertRecord(word + " Tail", definition));
		
		Cursor cursor = lookup("match"); // incorrect prefix
		assertNotNull(cursor);
		assertEquals(0, cursor.getCount());
		
		cursor = lookup(word.toUpperCase()); // correct prefix
		assertEquals(definition, getDefinition(cursor));
	}

	private String getDefinition(Cursor cursor) {
		assertNotNull(cursor);
		assertEquals(1, cursor.getCount());
		assertTrue(cursor.moveToFirst());
		
		int indexDefinition = cursor.getColumnIndex(ALIAS_DEFINITION);
		return cursor.getString(indexDefinition);
	}
	
	public void testInsertExceedingMaxLength() {
		String message = getContext().getString(
				R.string.error_max_length,
				InsertSanitizer.WORD_MAX_LENGTH, 
				InsertSanitizer.DEFINITION_MAX_LENGTH);
		
		try { // exceed max definition length
			insertRecord(maxWord, maxDefinition + "a");
		} catch(IllegalArgumentException e) {
			assertEquals(message, e.getMessage());
		}
		
		try { // exceed max word length
			insertRecord(maxWord + "Z", maxDefinition);
		} catch(IllegalArgumentException e) {
			assertEquals(message, e.getMessage());
		}
	}
	
	public void testInsertJustMaxLength() {
		Uri uri = insertRecord(maxWord, maxDefinition);
		assertNotNull(uri);
	}
	
	public void testInsertDoubleQuotes() {
		String word = "\"SW\"" + System.currentTimeMillis();
		String definition = "Special Words Meaning !";
		
		Uri uri = insertRecord(word, definition);
		assertNotNull(uri);
		
		Cursor cursor = lookup(word);
		assertEquals(definition, getDefinition(cursor));
	}
	
	public void testGetTypeUri() {
		String queryResultType = resolver.getType(CONTENT_URI);
		assertEquals(DictionaryContentProvider.DEFINITION_MIME_TYPE, queryResultType);
		
		Uri uri = Uri.withAppendedPath(CONTENT_URI, DEFINE_PATTERN);
		String insertionResultType = resolver.getType(uri);
		assertEquals(ContentResolver.CURSOR_DIR_BASE_TYPE, insertionResultType);
	}

	
	public void testDelete() {
		Exception exception = null;
		try {
			resolver.delete(CONTENT_URI, null, null);
		} catch(UnsupportedOperationException e) {
			exception = e;
		}
		
		assertNotNull(exception);
	}

	public void testUpdate() {
		Exception exception = null;
		try {
			resolver.update(CONTENT_URI, null, null, null);
		} catch(UnsupportedOperationException e) {
			exception = e;
		}
		
		assertNotNull(exception);
	}
}
