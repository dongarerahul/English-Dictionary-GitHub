package com.symantec.android.dictionary.upload.io;

import java.util.List;
import java.util.concurrent.Future;

import com.symantec.android.dictionary.upload.UploadStats;

public interface IDictionaryWriter {

	List<Future<Integer>> insertBatch();

	UploadStats getResult();

	int getInsertionCount();

}
