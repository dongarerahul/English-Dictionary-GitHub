package com.symantec.android.dictionary.upload.io;

import java.util.List;

public interface IDictionaryReader {

	List<WordDefinition> nextBatch();

	int getSkipCount();

	int getParsedCount();

}
