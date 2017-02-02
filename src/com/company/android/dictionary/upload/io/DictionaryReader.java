package com.company.android.dictionary.upload.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import android.util.Log;

public class DictionaryReader implements IDictionaryReader {

	private BufferedReader reader;
	private String delimiter;
	private int counter;
	private int skipCount;
	
	private final int batchSize;
	
	public DictionaryReader(Reader reader, String delimiter, int batchSize) {
		this.delimiter = delimiter;
		this.reader = new BufferedReader(reader);
		this.batchSize = batchSize;
	}

	@Override
	public List<WordDefinition> nextBatch() {
		long startTime = System.currentTimeMillis();
		List<WordDefinition> batch = null;
		try {
			batch = createRecordsBatch();
			Log.d("DictionaryReader", "File WordDefinition Parsed Count: " + counter);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		long timeTaken = (System.currentTimeMillis() - startTime);
		Log.d("DictionaryReader", "Time Taken For a Batch Reading: " + timeTaken + " milli-seconds !");
		return batch;
	}

	private List<WordDefinition> createRecordsBatch() throws IOException {
		String line = null;
		List<WordDefinition> records = new ArrayList<WordDefinition>();
		
		for(int index = 0; index < batchSize; index++) {
			if((line = reader.readLine()) == null) break; // File End
			
			WordDefinition record = createRecord(line);
			if(record == null) continue;
			
			records.add(record);
			counter++;
		}
		
		return records;
	}

	private WordDefinition createRecord(String line) {
		StringTokenizer tokenizer = new StringTokenizer(line, delimiter);
		if(tokenizer.countTokens() < 2)  {
			Log.d("InsertionTask", "Skipping Invalid Row: " + line);
			skipCount++;
			return null;
		}
		
		String word = tokenizer.nextToken();
		String definition = tokenizer.nextToken();
		
		return new WordDefinition(word, definition);
	}
	
	@Override
	public int getSkipCount() {
		return skipCount;
	}
	
	@Override
	public int getParsedCount() {
		return counter;
	}
}