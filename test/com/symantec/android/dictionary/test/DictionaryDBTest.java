package com.symantec.android.dictionary.test;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.ActivityInstrumentationTestCase2;

import com.symantec.android.dictionary.DictionaryActivity;
import com.symantec.android.dictionary.contentprovider.api.DictionaryConstants.Columns;
import com.symantec.android.dictionary.db.DictionaryDB;
import com.symantec.android.dictionary.db.IDictionaryDB;
import com.symantec.android.dictionary.upload.io.WordDefinition;

public class DictionaryDBTest extends ActivityInstrumentationTestCase2<DictionaryActivity> {

	private IDictionaryDB database;
	private DictionaryActivity activity;
	
	public DictionaryDBTest() {
		super(DictionaryActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		activity = getActivity();
		assertNotNull(activity);
		database = DictionaryDB.getInstance(activity.getApplicationContext());
	}
	
	public void testDictionaryDB() {
		assertNotNull(database);
	}

	public void testInsert() {
		long rowId = insertTestData("wordTest", "definitionTest");
		assertTrue(rowId > 0);
	}

	public void testInsertBatches() {
		List<WordDefinition> values = new ArrayList<WordDefinition>();
		
		for(int i = 0; i < 5; i++) {
			values.add(createRecord("word" + i, "definition" + i));
		}
		
		int rowsInserted = database.insertBatch(values);
		assertEquals(5, rowsInserted);
	}
	
	private WordDefinition createRecord(String word, String definition) {
		return new WordDefinition(word, definition);
	}
	
	public void testGetMatch() {
		insertTestData("WMT", "Word Match Test");
		insertTestData("WMT", "Word Match Test1");
		
		Cursor cursor = database.query(Columns.ALIAS_WORD + " MATCH ? ", "myWord");
		assertEquals(1, cursor.getColumnCount());

		int index = cursor.getColumnIndex(Columns.ALIAS_DEFINITION);
		assertEquals("myDefinition", cursor.getString(index));

		cursor.close();
	}
	
	public void testInvalidQuery() {
		Cursor cursor = database.query(Columns.ALIAS_WORD + " MATCH ", "myWord");
		assertNull(cursor);
	}

	public void testQuery() {
		String word = "MW" + System.currentTimeMillis();
		String definition = "My Word";
		insertTestData(word, definition);
		
		String query = DictionaryDB.getColumn(Columns.ALIAS_WORD) + "=? COLLATE NOCASE";
		Cursor cursor = database.query(query, word);
		
		assertEquals(1, cursor.getCount());
		assertEquals(true, cursor.moveToFirst());
		
		int index = cursor.getColumnIndex(Columns.ALIAS_DEFINITION);
		assertEquals(definition, cursor.getString(index));
		cursor.close();
	}
	
	private long insertTestData(String word, String definition) {
		ContentValues values = createContentValues(word, definition);
		return database.insert(values);
	}
	
	private ContentValues createContentValues(String word, String definition) {
		ContentValues values = new ContentValues();
		values.put(Columns.ALIAS_WORD, word);
		values.put(Columns.ALIAS_DEFINITION, definition);
		return values;
	}
}
