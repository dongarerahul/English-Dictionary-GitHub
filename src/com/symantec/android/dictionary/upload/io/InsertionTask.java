package com.symantec.android.dictionary.upload.io;

import java.util.List;
import java.util.concurrent.Callable;

import com.symantec.android.dictionary.db.IDictionaryDB;

public class InsertionTask implements Callable<Integer>{

	private final List<WordDefinition> records;
	private final IDictionaryDB database;
	
	public InsertionTask(IDictionaryDB database, List<WordDefinition> records) {
		this.records = records;
		this.database = database;
	}
	
	@Override
	public Integer call() {
		return database.insertBatch(records);
	}
}
