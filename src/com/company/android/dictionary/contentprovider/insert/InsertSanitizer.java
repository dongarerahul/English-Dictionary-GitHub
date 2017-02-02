package com.company.android.dictionary.contentprovider.insert;

import static com.company.android.dictionary.contentprovider.api.DictionaryConstants.Columns.ALIAS_DEFINITION;
import static com.company.android.dictionary.contentprovider.api.DictionaryConstants.Columns.ALIAS_WORD;
import android.content.ContentValues;
import android.content.Context;

import com.company.android.dictionary.R;
import com.company.android.dictionary.contentprovider.lookup.LookupSanitizer;
import com.company.android.dictionary.utilties.StringSanitizer;

public class InsertSanitizer {

	public static final int WORD_MAX_LENGTH = LookupSanitizer.WORD_MAX_LENGTH;
	public static final int DEFINITION_MAX_LENGTH = 500;
	
	private final ContentValues values;
	private final Context context;
	private final StringSanitizer sanitizer;

	public InsertSanitizer(Context context, ContentValues values) {
		this.context = context;
		this.values = values;
		this.sanitizer = StringSanitizer.getInstance();
	}

	public ContentValues sanitize() {
		String message = context.getString(R.string.error_insert_content_null);
		if (values == null)	throw new IllegalArgumentException(message);

		Object wordObject = values.get(ALIAS_WORD);
		Object definitionObject = values.get(ALIAS_DEFINITION);
		if (wordObject == null || definitionObject == null) {
			throw new IllegalArgumentException(message);
		}

		String word = sanitizer.sanitize(wordObject.toString().trim());
		String definition = sanitizer.sanitize(definitionObject.toString().trim());

		validateMaxLength(word, definition);
		return createContentValues(word, definition);
	}

	private ContentValues createContentValues(String word, String definition) {
		ContentValues newValues = new ContentValues();
		newValues.put(ALIAS_WORD, word);
		newValues.put(ALIAS_DEFINITION, definition);
		return newValues;
	}

	private void validateMaxLength(String word, String definition) {
		int wordLength = word.length();
		int definitionLength = definition.length();

		if (wordLength > WORD_MAX_LENGTH || definitionLength > DEFINITION_MAX_LENGTH) {

			String message = context.getString(
					R.string.error_max_length,
					WORD_MAX_LENGTH, 
					DEFINITION_MAX_LENGTH);
			
			throw new IllegalArgumentException(message);
		}
	}
}
