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
import java.util.GregorianCalendar;
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
		assertEquals(211, stocks.getStocks().size());
		Calendar expected = new GregorianCalendar(2012, 10, 23, 15, 46);
		expected.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		assertEquals(expected, stocks.getAsOf());
	}
}
