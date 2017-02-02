package com.company.android.dictionary.db;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.company.android.dictionary.upload.io.WordDefinition;

public interface IDictionaryDB {

	Cursor query(String whereClause, String word);

	long insert(ContentValues values);
	int insertBatch(List<WordDefinition> values);
	
}
