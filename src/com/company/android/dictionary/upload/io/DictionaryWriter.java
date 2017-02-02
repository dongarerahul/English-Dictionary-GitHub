package com.company.android.dictionary.upload.io;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.util.Log;

import com.company.android.dictionary.db.IDictionaryDB;
import com.company.android.dictionary.upload.UploadStats;
import static com.company.android.dictionary.upload.UploadStats.Result;

public class DictionaryWriter implements IDictionaryWriter {

	public static final long TIME_OUT = 10;
	private final ExecutorService service;
	private final IDictionaryDB database;
	private final List<Future<Integer>> futures;
	private final IDictionaryReader reader;
	private int insertionCount = 0;
	
	public DictionaryWriter(IDictionaryDB database, IDictionaryReader reader) {
		this.database = database;
		this.reader = reader;
		//Need for Custom Thread Pool with finite blocking queue ? 
		service = Executors.newCachedThreadPool();
		futures = new ArrayList<Future<Integer>>();
	}

	@Override
	public List<Future<Integer>> insertBatch() {

		while (true) {
			List<WordDefinition> records = reader.nextBatch();
			if (records == null || records.isEmpty()) break;

			InsertionTask task = new InsertionTask(database, records);
			Future<Integer> future = service.submit(task);
			futures.add(future);
		}
		return futures;
	}

	@Override
	public UploadStats getResult() {
		Result result = Result.SUCCESS;
		
		for (Future<Integer> future : futures) {
			try {
				int record = future.get(TIME_OUT, TimeUnit.SECONDS);
				insertionCount += record;
			} catch (TimeoutException e) {
				future.cancel(true);
				result = UploadStats.Result.DB_FAILURE;
				Log.e("UploadOperation", "Timeout: " + e.getMessage());
				break;
			} catch (Exception e) {
				future.cancel(true);
				result = Result.FAILURE;
				Log.e("UploadOperation", "DB Exception: " + e.getMessage());
				break;
			}
		}
		Log.d("DictionaryWriter", "No. Of Records Inserted: " + insertionCount);
		service.shutdownNow();
		return new UploadStats(result, insertionCount);
	}
	
	@Override
	public int getInsertionCount() {
		return insertionCount;
	}
}
