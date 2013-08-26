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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.googlecode.phisix.api.client.PseClient;

@RunWith(MockitoJUnitRunner.class)
public class StocksRepositoryImplTest {

	private StocksRepository stocksRepository;
	
	@Mock
	private PseClient client;
	
	@Before
	public void setUp() {
		stocksRepository = new StocksRepositoryImpl(client);
	}
	
	@Test
	public void findBySymbol() {
		String securityOrCompany = "{\"count\":1,\"totalCount\":11,\"records\":[{\"securityStatus\":\"O\",\"listedCompany_companyId\":\"599\",\"symbol\":\"SM\",\"listedCompany_companyName\":\"SM Investments Corporation\",\"securityId\":\"520\",\"securityName\":\"SM INVESTMENTS CORPORATION\"}]}";
		when(client.findSecurityOrCompany(eq("findSecurityOrCompany"), eq(true), eq("start=0&limit=1&query=sm"))).thenReturn(securityOrCompany);
		stocksRepository.findBySymbol("sm");
		verify(client).companyInfo("fetchHeaderData", true, "company=599&security=520");
	}

}
