package com.company.android.dictionary.utilties;

import java.util.HashMap;
import java.util.Map;

public class StringSanitizer {
	
	private final static StringSanitizer instance = new StringSanitizer();
	private final Map<String, String> map;
	
	private StringSanitizer() {
		map = new HashMap<String, String>();
		register("\"", "&quote;");
		//register("?", "&question;");
		//register("*", "&asterisk;");
	}
	
	public static StringSanitizer getInstance() {
		return instance;
	}
	
	private void register(String key, String value) {
		map.put(key, value);
	}

	public String sanitize(String argument) {
		for(String key : map.keySet()) {
			argument = argument.replaceAll(key, map.get(key));
		}
		return argument;
	}
	
	public String deSanitize(String argument) {
		for(String key : map.keySet()) {
			argument = argument.replaceAll(map.get(key), key);
		}
		return argument;
	}
}
