package com.symantec.android.dictionary.db;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.symantec.android.dictionary.upload.io.WordDefinition;

public interface IDictionaryDB {

	Cursor query(String whereClause, String word);

	long insert(ContentValues values);
	int insertBatch(List<WordDefinition> values);
	
}
