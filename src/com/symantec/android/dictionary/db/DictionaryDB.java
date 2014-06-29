package com.symantec.android.dictionary.db;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.symantec.android.dictionary.contentprovider.api.DictionaryConstants;
import com.symantec.android.dictionary.upload.io.WordDefinition;

public class DictionaryDB extends SQLiteOpenHelper implements IDictionaryDB {
    
	private static final String COLUMN_DEFINITION = "definition";
	private static final String COLUMN_WORD = "word";
	private static final String DATABASE_NAME = "DictionaryDB.db";
	private static final String TABLE_NAME = "Dictionary";
	
	private static final String KEY_WORD = DictionaryConstants.Columns.ALIAS_WORD;
    private static final String KEY_DEFINITION = DictionaryConstants.Columns.ALIAS_DEFINITION;
    private static final int DATABASE_VERSION = 1;
	private static final Map<String, String> aliasMap = buildColumnsMap(); 
	private static IDictionaryDB instance;
	
	private static final String CREATE_DB_TABLE_QUERY = 
			"CREATE VIRTUAL TABLE " + TABLE_NAME + 
			" USING fts4 " +
			"(" + aliasMap.get(KEY_WORD) + ", " +  
			aliasMap.get(KEY_DEFINITION) + ")";
	
	private static final String insertSQL = 
			"INSERT INTO " + TABLE_NAME + " (" + COLUMN_WORD + 
			", " + COLUMN_DEFINITION + ") VALUES (?, ?)";
	
	private final SQLiteDatabase database;
	
	private DictionaryDB(Context context) {
       super(context, DATABASE_NAME, null, DATABASE_VERSION);
       database = getWritableDatabase();
    }
	
	//Decoupling between actual columns & query columns  
    private static Map<String, String> buildColumnsMap() {
    	Map<String, String> map = new HashMap<String, String>();
    	map.put(KEY_WORD, COLUMN_WORD);
    	map.put(KEY_DEFINITION, COLUMN_DEFINITION);
    	return map;
	}
	
	public static IDictionaryDB getInstance(Context context) {
		if(instance == null) {
			instance = new DictionaryDB(context);
		}
		return instance;
	}

	@Override
    public void onCreate(SQLiteDatabase database) {
    	database.execSQL(CREATE_DB_TABLE_QUERY);
    }
    
    @Override // TODO: Handle DB upgrade
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    	Log.w("DictionaryDB", "Upgrading database from version " + oldVersion + 
    		  " to " + newVersion + ", which will destroy all old data !");
    	
    	database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    @Override
	public long insert(ContentValues values) {
		return database.insert(TABLE_NAME, null, values);
	}
    
    @Override
    public int insertBatch(List<WordDefinition> values) {
    	int insertionCount = 0;
    	long startTime = System.currentTimeMillis();
    	try {
	    	database.beginTransaction();
	    	insertionCount = insertAsBatch(values);
	    	database.setTransactionSuccessful();
    	} catch(SQLException exception) {
    		exception.printStackTrace();
    		Log.e("DictionaryDB", "SQL Exception: " + exception.getMessage());
    	} finally {
	    	database.endTransaction();
    	}
    	long timeTaken = (System.currentTimeMillis() - startTime) / 1000;
    	Log.d("DictionaryDB", "Time Taken for Batch Insert: " + timeTaken + " seconds");
    	return insertionCount;
    }

	private int insertAsBatch(List<WordDefinition> values) {
		int insertionCount = 0;
		SQLiteStatement statement = database.compileStatement(insertSQL);
		
		for (WordDefinition record : values) {
    	    statement.bindString(1, record.getWord());
    	    statement.bindString(2, record.getDefinition());
    	    
    	    long result = statement.executeInsert();
    	    if(result > 0) insertionCount++;
    	    statement.clearBindings();
    	}
		return insertionCount;
	}

    @Override
    public Cursor query(String whereClause, String word) {
		
		String[] columnsToReturn = new String[] { aliasMap.get(KEY_DEFINITION) };
		String wordLowerCase = word.toLowerCase(Locale.US);
		
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
 	    builder.setTables(TABLE_NAME);
 	    
 	    Cursor cursor = null;
 	    try {
 	    	cursor = builder.query(
				database, 
				columnsToReturn, 
				whereClause, 
				new String[] { wordLowerCase }, 
				null, 
				null, 
				null);
		
			if(cursor != null) cursor.moveToFirst();
 	    } catch(SQLException exception) {
 	    	exception.printStackTrace();
 	    }
 	    
 	    return cursor;
	}
	
	public static final String getColumn(String columnAlias) {
		return aliasMap.get(columnAlias);
	}
}