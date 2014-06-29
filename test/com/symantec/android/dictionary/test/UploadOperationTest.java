package com.symantec.android.dictionary.test;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.os.AsyncTask;
import android.test.ActivityInstrumentationTestCase2;

import com.symantec.android.dictionary.upload.UploadActivity;
import com.symantec.android.dictionary.upload.UploadOperation;
import com.symantec.android.dictionary.upload.UploadStats;
import com.symantec.android.dictionary.upload.UploadStats.Result;
import com.symantec.android.dictionary.upload.io.DictionaryWriter;

public class UploadOperationTest extends ActivityInstrumentationTestCase2<UploadActivity> {

	private Instrumentation instrument;
	private UploadActivity activity;
	private UploadOperation operation;

	public UploadOperationTest() {
		super(UploadActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		instrument = getInstrumentation();
		activity = getActivity();
		operation = new UploadOperation(activity);
	}
	
	public void testActivityNull() {
		assertNotNull(instrument);
		assertNotNull(activity);
	}
	
	public void testNewUploadOperation() {
		assertNotNull(operation);
	}

	public void testStartUploadOperationBothNull() throws Exception {
		AsyncTask<Object, Void, UploadStats> task = operation.execute(null, null);
		UploadStats stats = task.get(DictionaryWriter.TIME_OUT, TimeUnit.SECONDS);
		assertNotNull(stats);
		assertEquals(Result.FAILURE, stats.getResult());
	}
	
	public void testStartUploadOperationManyNull() throws Exception {
		AsyncTask<Object, Void, UploadStats> task1 = operation.execute(null, null, null);
		UploadStats stats = task1.get(DictionaryWriter.TIME_OUT, TimeUnit.SECONDS);
		assertNotNull(stats);
		assertEquals(Result.FAILURE, stats.getResult());
	}
	
	public void testStartUploadOperationNullArray() throws Exception {
		AsyncTask<Object, Void, UploadStats> task2 = operation.execute((Object[])null);
		UploadStats stats = task2.get(DictionaryWriter.TIME_OUT, TimeUnit.SECONDS);
		assertNotNull(stats);
		assertEquals(Result.FAILURE, stats.getResult());
	}
	
	@SuppressLint("SdCardPath") //Pre-requisite File should present at the full-path
	public void testStartUploadOperationFullPath() throws Exception {
		String filePath = "/data/data/com.symantec.android.dictionary/databases/datafile.csv";
		File file = new File(filePath);
		Reader reader = new FileReader(file);
		
		UploadStats result = startUploadOperation(reader);
		assertEquals(5, result.getInsertionCount());
		assertEquals(0, result.getSkipCount());
		assertEquals(Result.SUCCESS, result.getResult());
	}
	
	public void testStartUploadOperationFromAssets() throws Exception {
		UploadStats result = assetUpload("samples/datafile.csv");
		
		assertEquals(5, result.getInsertionCount());
		assertEquals(0, result.getSkipCount());
		assertEquals(Result.SUCCESS, result.getResult());
	}

	public void testUploadOperationFaultyFile() throws Exception {
		UploadStats result = assetUpload("samples/SomeWhatFaulty.csv");
		assertEquals(3, result.getInsertionCount());
		assertEquals(4, result.getSkipCount());
		assertEquals(Result.SUCCESS, result.getResult());
	}
	
	private UploadStats startUploadOperation(Reader reader) throws Exception {
		AsyncTask<Object, Void, UploadStats> task = operation.execute(reader, "|");
		UploadStats result = task.get(DictionaryWriter.TIME_OUT, TimeUnit.SECONDS);
		assertNotNull(result);
		return result;
	}
	
	private UploadStats assetUpload(String sampleFile) throws Exception {
		InputStream stream = activity.getAssets().open(sampleFile);
		Reader reader = new InputStreamReader(stream);
		
		return startUploadOperation(reader);
	}
}
