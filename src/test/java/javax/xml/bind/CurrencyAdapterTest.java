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

import java.util.Currency;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Lorenzo Dee
 *
 */
public class CurrencyAdapterTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParseCurrency() {
		assertEquals(Currency.getInstance("USD"), CurrencyAdapter.parseCurrency("USD"));
		assertEquals(Currency.getInstance("JPY"), CurrencyAdapter.parseCurrency("JPY"));
	}

	@Test
	public void testPrintCurrency() {
		assertEquals("USD", CurrencyAdapter.printCurrency(Currency.getInstance("USD")));
		assertEquals("JPY", CurrencyAdapter.printCurrency(Currency.getInstance("JPY")));
	}

}
