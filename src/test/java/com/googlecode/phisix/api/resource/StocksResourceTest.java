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
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.googlecode.phisix.api.model.Price;
import com.googlecode.phisix.api.model.Stock;
import com.googlecode.phisix.api.model.Stocks;
import com.googlecode.phisix.api.repository.StocksRepository;

@RunWith(MockitoJUnitRunner.class)
public class StocksResourceTest {

	private StocksResource stocksResource;
	
	private Stock expectedStock;

	@Mock
	private StocksRepository stocksRepository;

	@Before
	public void setUp() throws Exception {
		Stocks expectedStocks = new Stocks();
		expectedStocks.setAsOf(Calendar.getInstance());
		expectedStock = new Stock();
		expectedStock.setName("A");
		expectedStock.setPercentChange(new BigDecimal(0));
		Price price = new Price();
		price.setAmount(new BigDecimal(100));
		price.setCurrency("PHP");
		expectedStock.setPrice(price );
		expectedStock.setSymbol("A");
		expectedStock.setVolume(100);
		expectedStocks.getStocks().add(expectedStock);

		when(stocksRepository.findAll()).thenReturn(expectedStocks);
		
		stocksResource = new StocksResource(stocksRepository);
	}
	
	@Test
	public void getAllStocks() throws Exception {
		Stocks actualStocks = stocksResource.getAllStocks();
		
		assertNotNull(actualStocks);
		assertNotNull(actualStocks.getAsOf());
		assertEquals(1, actualStocks.getStocks().size());
		Stock actualStock = actualStocks.getStocks().get(0);
		assertEquals(expectedStock.getName(), actualStock.getName());
		assertEquals(expectedStock.getPercentChange(), actualStock.getPercentChange());
		assertEquals(expectedStock.getPrice().getAmount(), actualStock.getPrice().getAmount());
		assertEquals(expectedStock.getPrice().getCurrency(), actualStock.getPrice().getCurrency());
		assertEquals(expectedStock.getSymbol(), actualStock.getSymbol());
		assertEquals(expectedStock.getVolume(), actualStock.getVolume());
	}
	
	@Test
	public void getValidStock() throws Exception {
		Stocks actualStocks = stocksResource.getStock("a");
		assertNotNull(actualStocks);
		assertNotNull(actualStocks.getAsOf());
		assertEquals(1, actualStocks.getStocks().size());
		Stock actualStock = actualStocks.getStocks().get(0);
		assertEquals(expectedStock.getName(), actualStock.getName());
		assertEquals(expectedStock.getPercentChange(), actualStock.getPercentChange());
		assertEquals(expectedStock.getPrice().getAmount(), actualStock.getPrice().getAmount());
		assertEquals(expectedStock.getPrice().getCurrency(), actualStock.getPrice().getCurrency());
		assertEquals(expectedStock.getSymbol(), actualStock.getSymbol());
		assertEquals(expectedStock.getVolume(), actualStock.getVolume());
	}
	
	@Test(expected = NotFoundException.class)
	public void getInvalidStock() throws Exception {
		stocksResource.getStock("X");
	}
	
	@Test(expected = BadRequestException.class)
	public void getStockByInvalidDate() {
		stocksResource.getStockByDate("symbol", "date");
	}
	
	@Test
	public void getStockByDate() {
		stocksResource.getStockByDate("a", "2013-09-03");
		Calendar calendar = new GregorianCalendar(2013, 8, 3);
		calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		Date tradingDate = calendar.getTime();
		verify(stocksRepository).findBySymbolAndTradingDate("A", tradingDate);
	}
}
