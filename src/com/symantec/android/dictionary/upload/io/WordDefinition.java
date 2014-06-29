package com.symantec.android.dictionary.upload.io;

public class WordDefinition {

	private String word;
	private String definition;

	public WordDefinition(String word, String definition) {
		this.word = word.trim();
		this.definition = definition.trim();
	}
	
	public String getWord() {
		return word;
	}
	
	public String getDefinition() {
		return definition;
	}
	
	public boolean isValid() {
		return (word == null || definition == null ||
				word.length() == 0 || definition.length() == 0) ? false : true; 
	}
}
