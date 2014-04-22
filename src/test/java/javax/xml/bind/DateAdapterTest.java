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
package javax.xml.bind;


import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Lorenzo Dee
 * @author Edge Dalmacio
 *
 */
public class DateAdapterTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void parseDate() throws Exception {
		//GregorianCalendar c = new GregorianCalendar(2010, Calendar.JUNE, 28);
		//assertEquals(c, actual);
		final Calendar actual = DateAdapter.parseDate("2010-06-28"); // yyyy-MM-dd
		assertEquals(2010, actual.get(Calendar.YEAR));
		assertEquals(Calendar.JUNE, actual.get(Calendar.MONTH));
		assertEquals(28, actual.get(Calendar.DAY_OF_MONTH));
	}

	@Test
	public void parseDateTime() throws Exception {
		Calendar c = new GregorianCalendar(
				2000, Calendar.JANUARY, 12, 13, 14, 15);
		c.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		final Calendar actual = DateAdapter.parseDate("2000-01-12T13:14:15Z");
		assertEquals(2000, actual.get(Calendar.YEAR));
		assertEquals(Calendar.JANUARY, actual.get(Calendar.MONTH));
		assertEquals(12, actual.get(Calendar.DAY_OF_MONTH));
		assertEquals(13, actual.get(Calendar.HOUR_OF_DAY));
		assertEquals(14, actual.get(Calendar.MINUTE));
		assertEquals(15, actual.get(Calendar.SECOND));
		//assertEquals(c, DateAdapter.parseDateTime("2000-01-12T12:13:14Z"));
	}
	
	@Test
	public void parseYear() throws Exception {
		final int actual = DateAdapter.parseYear("2010"); // yyyy
		assertEquals(2010, actual);
	}

	@Test
	public void printDate() throws Exception {
		Calendar c = new GregorianCalendar(
				2000, Calendar.JANUARY, 12, 12, 13, 14);
		int z = c.getTimeZone().getRawOffset() / 3600000;
		String s = DateAdapter.printDate(c);
		if (z == 0) {
			assertEquals("2000-01-12Z", s);
		} else {
			assertEquals(
					String.format("2000-01-12+%02d:00", z), s);
		}
	}

	@Test
	public void printDateTime() throws Exception {
		Calendar c = new GregorianCalendar(
				2000, Calendar.JANUARY, 12, 12, 13, 14);
		int z = c.getTimeZone().getRawOffset() / 3600000;
		String s = DateAdapter.printDateTime(c);
		if (z == 0) {
			assertEquals("2000-01-12T12:13:14Z", s);
		} else {
			assertEquals(
					String.format("2000-01-12T12:13:14+%02d:00", z), s);
		}
	}
	
	@Test
	public void printYear() throws Exception {
		String actual = DateAdapter.printYear(2012);
		assertEquals("2012", actual);
	}
	
}
