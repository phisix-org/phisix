/**
 * Copyright (C) 2012 Edge Dalmacio <edgar.dalmacio@gmail.com>
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
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import com.googlecode.phisix.api.model.Stock;
import com.googlecode.phisix.api.model.Stocks;
import com.googlecode.phisix.api.parser.Parser;
import com.googlecode.phisix.api.parser.PhisixParser;

public class PhisixParserTest {

	private Parser<Reader, Stocks> parser;
	
	@Before
	public void setUp() {
		parser = new PhisixParser();
	}
	
	@Test
	public void parseFeed() throws Exception {
		// http://www2.pse.com.ph/servlet/TickerServletFile
		String feed = ".;Stock Update as of ;.;TUE JAN 24, 2012 12:00:00 PM;" +
				"AAA ;4.85 1.0417 %;Asia Amalgamate ;642,000 ;" +
				"ACPA ;570.00 0.00 %;Ayala Pref. A ;160 ;" +
				"PSEi ;4,712.40 -35.50 ;.;3,211,466,154.00 ;" +
				"All Shares ;3,176.39 -16.76 ;.;5,012,020,509.00 ;";
		
		Stocks actual = parser.parse(new StringReader(feed));
		assertNotNull(actual);
		assertNotNull(actual.getAsOf());
		assertEquals(new GregorianCalendar(2012, 0, 24, 12, 0, 0), actual.getAsOf());
		assertEquals(4, actual.getStocks().size());
		
		// AAA
		Stock aaa = actual.getStocks().get(0);
		assertEquals("AAA", aaa.getSymbol());
		assertEquals(new BigDecimal("4.85"), aaa.getPrice().getAmount());
		assertEquals(new BigDecimal("1.0417"), aaa.getPercentChange());
		assertEquals("Asia Amalgamate", aaa.getName());
		assertEquals(642000, aaa.getVolume());
		
		// PSEi
		Stock psei = actual.getStocks().get(2);
		assertEquals("PSEi", psei.getSymbol());
		assertEquals(new BigDecimal("4712.40"), psei.getPrice().getAmount());
		assertEquals(new BigDecimal("-35.50"), psei.getPercentChange());
		assertNull(psei.getName());
		assertEquals(3211466154L, psei.getVolume());
		
		// All Shares
		Stock allShares = actual.getStocks().get(3);
		assertEquals("All Shares", allShares.getSymbol());
		assertEquals(new BigDecimal("3176.39"), allShares.getPrice().getAmount());
		assertEquals(new BigDecimal("-16.76"), allShares.getPercentChange());
		assertNull(allShares.getName());
		assertEquals(5012020509L, allShares.getVolume());
	}
	
//	@Test
	public void parseDate() throws Exception {
		Date expected = new GregorianCalendar(2012, 0, 24, 12, 0, 0).getTime();
		DateFormat df = new SimpleDateFormat("E MMM dd, yyyy hh:mm:ss a");
//		System.out.println(df.format(expected));
		Date actual = df.parse("TUE JAN 24, 2012 12:00:00 PM");
		assertEquals(expected, actual);
	}
	
//	@Test
	public void parseShouldBeFast() throws Exception {
		Reader reader = new BufferedReader(new FileReader("src/test/resources/RawSample.txt"));
		long start = System.currentTimeMillis();
		Stocks actual = parser.parse(reader);
		long end = System.currentTimeMillis();
		System.out.println(end - start + "ms");
		System.out.println(actual.getStocks().size());
	}
	
}
