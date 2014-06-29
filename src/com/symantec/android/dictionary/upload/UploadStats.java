package com.symantec.android.dictionary.upload;

public class UploadStats {
	
	public static enum Result {
		SUCCESS, DB_FAILURE, INVALID_ARGUMENTS, FAILURE;
	}

	private final Result result;
	private final int insertionCount;
	private int skipCount;
	private long timeTaken;
	
	public UploadStats(Result result, int insertions) {
		this.insertionCount = insertions;
		this.result = result;
	}

	public int getInsertionCount() {
		return insertionCount;
	}

	public int getSkipCount() {
		return skipCount;
	}

	public Result getResult() {
		return result;
	}
	
	public void setSkips(int skips) {
		this.skipCount = skips;
	}

	public long getTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(long timeTaken) {
		this.timeTaken = timeTaken;
	}
}
