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

import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.phisix.api.model.Stocks;

public class StocksRepositoryImplITCase {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalMemcacheServiceTestConfig());
	private StocksRepository stocksRepository;
	
	@Before
	public void setUp() {
		helper.setUp();
		stocksRepository = new StocksRepositoryImpl();
	}
	
	@Test
	public void findAll() {
		Stocks actual = stocksRepository.findAll();
		assertNotNull(actual);
		assertEquals(382, actual.getStocks().size());
	}
	
//	@Test
	public void findBySymbol() {
		String actual = stocksRepository.findBySymbol("sm");
		assertNotNull(actual);
		System.out.println(actual);
	}

}
