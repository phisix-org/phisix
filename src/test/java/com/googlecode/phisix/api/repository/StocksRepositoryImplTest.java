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

import static org.mockito.Mockito.*;

import java.math.BigDecimal;
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
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.phisix.api.client.PseClient;
import com.googlecode.phisix.api.model.Price;
import com.googlecode.phisix.api.model.Stock;
import com.googlecode.phisix.api.model.Stocks;

@RunWith(MockitoJUnitRunner.class)
public class StocksRepositoryImplTest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private DatastoreService datastore;
	private StocksRepository stocksRepository;
	
	@Mock
	private PseClient client;
	
	@Before
	public void setUp() {
		helper.setUp();
		datastore = DatastoreServiceFactory.getDatastoreService();
		stocksRepository = new StocksRepositoryImpl(client);
	}
	
	@After
	public void tearDown() {
		helper.tearDown();
	}
	
	@Test
	public void findBySymbol() {
		String securityOrCompany = "{\"count\":1,\"totalCount\":11,\"records\":[{\"securityStatus\":\"O\",\"listedCompany_companyId\":\"599\",\"symbol\":\"SM\",\"listedCompany_companyName\":\"SM Investments Corporation\",\"securityId\":\"520\",\"securityName\":\"SM INVESTMENTS CORPORATION\"}]}";
		when(client.findSecurityOrCompany(eq("findSecurityOrCompany"), eq(true), eq("start=0&limit=1&query=sm"))).thenReturn(securityOrCompany);
		stocksRepository.findBySymbol("sm");
		verify(client).companyInfo("fetchHeaderData", true, "company=599&security=520");
	}
	
	@Test
	public void save() throws Exception {
		Stocks stocks = new Stocks();
		stocks.setAsOf(new GregorianCalendar(2013, 7, 26));
		
		Stock sm = new Stock();
		sm.setSymbol("SM");
		sm.setName("SM Investments");
		Price smPrice = new Price();
		smPrice.setAmount(new BigDecimal("730"));
		sm.setPrice(smPrice);
		stocks.getStocks().add(sm);
		
		Stock bdo = new Stock();
		bdo.setSymbol("BDO");
		bdo.setName("Banco de Oro");
		Price bdoPrice = new Price();
		bdoPrice.setAmount(new BigDecimal("5.28"));
		bdo.setPrice(bdoPrice);
		stocks.getStocks().add(bdo);
		
		stocksRepository.save(stocks);
		
		datastore.get(KeyFactory.createKey("Stock", "SM"));
		datastore.get(KeyFactory.createKey("Stock", "BDO"));
	}

}
