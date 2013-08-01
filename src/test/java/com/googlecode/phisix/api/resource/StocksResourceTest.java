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
package com.googlecode.phisix.api.resource;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.googlecode.phisix.api.model.Price;
import com.googlecode.phisix.api.model.Stock;
import com.googlecode.phisix.api.model.Stocks;
import com.googlecode.phisix.api.parser.Parser;
import com.googlecode.phisix.api.urlfetch.URLFetchService;

@RunWith(MockitoJUnitRunner.class)
public class StocksResourceTest {

	private StocksResource stocksResource;
	
	@Mock
	private URLFetchService urlFetchService;
	
	@Mock
	private Parser<Reader, Stocks> parser;

	@Before
	public void setUp() throws Exception {
		stocksResource = new StocksResource(urlFetchService, parser);
		when(urlFetchService.fetch(any(URL.class))).thenReturn(mock(InputStream.class));
	}
	
	@Test
	public void getAllStocks() throws Exception {
		Stocks expectedStocks = new Stocks();
		expectedStocks.setAsOf(Calendar.getInstance());
		Stock expectedStock = new Stock();
		expectedStock.setName("A");
		expectedStock.setPercentChange(new BigDecimal(0));
		Price price = new Price();
		price.setAmount(new BigDecimal(100));
		price.setCurrency("PHP");
		expectedStock.setPrice(price );
		expectedStock.setSymbol("A");
		expectedStock.setVolume(100);
		expectedStocks.getStocks().add(expectedStock);

		when(parser.parse(any(Reader.class))).thenReturn(expectedStocks);

		Stocks actualStocks = stocksResource.getAllStocks();
		
		assertNotNull(actualStocks);
		assertEquals(1, actualStocks.getStocks().size());
		Stock actualStock = actualStocks.getStocks().get(0);
		assertEquals(expectedStock.getName(), actualStock.getName());
		assertEquals(expectedStock.getPercentChange(), actualStock.getPercentChange());
		assertEquals(expectedStock.getPrice().getAmount(), actualStock.getPrice().getAmount());
		assertEquals(expectedStock.getPrice().getCurrency(), actualStock.getPrice().getCurrency());
		assertEquals(expectedStock.getSymbol(), actualStock.getSymbol());
		assertEquals(expectedStock.getVolume(), actualStock.getVolume());
	}
}
