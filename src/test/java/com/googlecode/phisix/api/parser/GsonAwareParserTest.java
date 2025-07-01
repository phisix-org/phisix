/**
 * Copyright 2012 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.phisix.api.parser;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.googlecode.phisix.api.model.Stocks;

public class GsonAwareParserTest {

	private Parser<Reader, Stocks> parser;
	
	@Before
	public void setUp() {
		parser = new GsonAwareParser();
	}
	
	@Test
	public void parse() throws Exception {
		Reader reader = new BufferedReader(new FileReader("src/test/resources/JsonRawSample.txt"));
		Stocks stocks = parser.parse(reader);
		assertEquals(363, stocks.getStocks().size());
		
		// Verify that asOf is set to current date with time zeroed out
		Calendar asOf = stocks.getAsOf();
		assertNotNull(asOf);
		assertEquals(TimeZone.getTimeZone("Asia/Manila"), asOf.getTimeZone());
		assertEquals(0, asOf.get(Calendar.HOUR_OF_DAY));
		assertEquals(0, asOf.get(Calendar.MINUTE));
		assertEquals(0, asOf.get(Calendar.SECOND));
		assertEquals(0, asOf.get(Calendar.MILLISECOND));
		
		// Verify it's today's date (within a reasonable range to handle timing differences)
		Calendar today = Calendar.getInstance(TimeZone.getTimeZone("Asia/Manila"));
		assertTrue("AsOf date should be today's date", 
			Math.abs(asOf.getTimeInMillis() - today.getTimeInMillis()) < 24 * 60 * 60 * 1000); // within 24 hours
	}
}
