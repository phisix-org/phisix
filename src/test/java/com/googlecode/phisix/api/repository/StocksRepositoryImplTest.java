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
package com.googlecode.phisix.api.repository;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.phisix.api.client.GaClient;
import com.googlecode.phisix.api.client.GaClientConstants;
import com.googlecode.phisix.api.client.PseClient;
import com.googlecode.phisix.api.client.PseClientConstants;
import com.googlecode.phisix.api.model.Price;
import com.googlecode.phisix.api.model.Stock;
import com.googlecode.phisix.api.model.Stocks;

@RunWith(MockitoJUnitRunner.class)
public class StocksRepositoryImplTest {

	// maximum eventual consistency
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig().setDefaultHighRepJobPolicyUnappliedJobPercentage(20), 
			new LocalMemcacheServiceTestConfig());
	private DatastoreService datastore;
	private StocksRepository stocksRepository;
	
	@Mock
	private PseClient pseClient;
	
	@Mock
	private GaClient gaClient;
	
	@Before
	public void setUp() {
		helper.setUp();
		datastore = DatastoreServiceFactory.getDatastoreService();
		stocksRepository = new StocksRepositoryImpl(pseClient, gaClient);
	}
	
	@After
	public void tearDown() {
		helper.tearDown();
	}
	
	@Test
	public void findAll() {
		Stocks expected = new Stocks();
		
		when(pseClient.getSecuritiesAndIndicesForPublic(PseClientConstants.REFERER, "getSecuritiesAndIndicesForPublic", true)).thenReturn(expected);

		assertSame(expected, stocksRepository.findAll());
		verify(pseClient).getSecuritiesAndIndicesForPublic(PseClientConstants.REFERER, "getSecuritiesAndIndicesForPublic", true);

		assertEquals(expected, stocksRepository.findAll());
		verify(pseClient).getSecuritiesAndIndicesForPublic(PseClientConstants.REFERER, "getSecuritiesAndIndicesForPublic", true);
		
		verify(gaClient, times(2)).eventTracking(GaClientConstants.VERSION, GaClientConstants.TRACKING_ID, GaClientConstants.CLIENT_ID, GaClientConstants.EVENT_HIT, "stocks", "all");
	}
	
	@Test
	public void findBySymbol() {
		String securityOrCompany = "{\"count\":1,\"totalCount\":11,\"records\":[{\"securityStatus\":\"O\",\"listedCompany_companyId\":\"599\",\"symbol\":\"SM\",\"listedCompany_companyName\":\"SM Investments Corporation\",\"securityId\":\"520\",\"securityName\":\"SM INVESTMENTS CORPORATION\"}]}";
		when(pseClient.findSecurityOrCompany(eq("findSecurityOrCompany"), eq(true), eq("start=0&limit=1&query=sm"))).thenReturn(securityOrCompany);
		String symbol = "sm";
		stocksRepository.findBySymbol(symbol);
		verify(pseClient).companyInfo("fetchHeaderData", true, "company=599&security=520");
		verify(gaClient).eventTracking(GaClientConstants.VERSION, GaClientConstants.TRACKING_ID, GaClientConstants.CLIENT_ID, GaClientConstants.EVENT_HIT, "stocks", symbol);
	}
	
	@Test
	public void findBySymbolAndTradingDate() throws Exception {
		save();
		Date tradingDate = new GregorianCalendar(2013, 7, 26).getTime();
		String symbol = "SM";
		Stocks actual = stocksRepository.findBySymbolAndTradingDate(symbol, tradingDate);
		assertNotNull(actual);
		assertEquals(1, actual.getStocks().size());
		Stock actualStock = actual.getStocks().get(0);
		assertEquals(symbol, actualStock.getSymbol());
		assertEquals("SM Investments", actualStock.getName());
		assertEquals(1080760, actualStock.getVolume());
		assertEquals("PHP", actualStock.getPrice().getCurrency());
		assertEquals(new BigDecimal("730"), actualStock.getPrice().getAmount());
		verify(gaClient).eventTracking(GaClientConstants.VERSION, GaClientConstants.TRACKING_ID, GaClientConstants.CLIENT_ID, GaClientConstants.EVENT_HIT, "stocks", symbol + ".2013-08-26");
	}
	
	@Test
	public void save() throws Exception {
		Stocks stocks = new Stocks();
		stocks.setAsOf(new GregorianCalendar(2013, 7, 26, 15, 46, 0));
		
		Stock sm = new Stock();
		sm.setSymbol("SM");
		sm.setName("SM Investments");
		sm.setVolume(1080760);
		Price smPrice = new Price();
		smPrice.setAmount(new BigDecimal("730"));
		sm.setPrice(smPrice);
		stocks.getStocks().add(sm);
		
		Stock bdo = new Stock();
		bdo.setSymbol("BDO");
		bdo.setName("Banco de Oro");
		bdo.setVolume(2979110);
		Price bdoPrice = new Price();
		bdoPrice.setAmount(new BigDecimal("76.50"));
		bdo.setPrice(bdoPrice);
		stocks.getStocks().add(bdo);

		Stock mbt = new Stock();
		mbt.setSymbol("MBT");
		mbt.setName("Metrobank");
		mbt.setVolume(2169810);
		Price mbtPrice = new Price();
		mbtPrice.setAmount(new BigDecimal("81.3"));
		mbt.setPrice(mbtPrice);
		stocks.getStocks().add(mbt);

		Stock col = new Stock();
		col.setSymbol("COL");
		col.setName("COL Financial");
		col.setVolume(5100);
		Price colPrice = new Price();
		colPrice.setAmount(new BigDecimal("18"));
		col.setPrice(colPrice);
		stocks.getStocks().add(col);

		Stock tel = new Stock();
		tel.setSymbol("TEL");
		tel.setName("PLDT");
		tel.setVolume(112850);
		Price telPrice = new Price();
		telPrice.setAmount(new BigDecimal("2850"));
		tel.setPrice(telPrice);
		stocks.getStocks().add(tel);

		Stock smph = new Stock();
		smph.setSymbol("SMPH");
		smph.setName("SM Prime Hldg.");
		smph.setVolume(9228500);
		Price smphPrice = new Price();
		smphPrice.setAmount(new BigDecimal("9228500"));
		smph.setPrice(smphPrice);
		stocks.getStocks().add(smph);

		stocksRepository.save(stocks);
		
		datastore.get(KeyFactory.createKey("Stock", "SM"));
		datastore.get(KeyFactory.createKey("Stock", "BDO"));
		datastore.get(KeyFactory.createKey("Stock", "MBT"));
		datastore.get(KeyFactory.createKey("Stock", "COL"));
		datastore.get(KeyFactory.createKey("Stock", "TEL"));
		datastore.get(KeyFactory.createKey("Stock", "SMPH"));
	}

}
