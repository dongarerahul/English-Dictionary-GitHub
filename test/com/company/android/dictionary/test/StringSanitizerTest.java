package com.company.android.dictionary.test;

import junit.framework.TestCase;

import org.junit.Test;

import com.company.android.dictionary.utilties.StringSanitizer;

public class StringSanitizerTest extends TestCase {

	private StringSanitizer instance;

	@Override
	protected void setUp() throws Exception {
		instance = StringSanitizer.getInstance();
	}
	
	public void testGetInstance() {
		assertNotNull(instance);
	}

	public void testSantize() {
		String argument = "\"MW\"";
		String sanitized = instance.sanitize(argument);
		
		assertEquals("&quote;MW&quote;", sanitized);
	}

	@Test
	public void testDeSantize() {
		String deSantize = instance.deSanitize("&quote;MW&quote;");
		assertEquals("\"MW\"", deSantize);
	}
}
