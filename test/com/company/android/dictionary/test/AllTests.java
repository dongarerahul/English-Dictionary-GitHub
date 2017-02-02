package com.company.android.dictionary.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite("Dictionary Tests");
		
		suite.addTestSuite(DictionaryActivityTest.class);
		suite.addTestSuite(DictionaryContentProviderTest.class);
		suite.addTestSuite(DictionaryDBTest.class);
		suite.addTestSuite(UploadOperationTest.class);
		suite.addTestSuite(UploadActivityTest.class);
		suite.addTestSuite(StringSanitizerTest.class);
		
		return suite;
	}
}
