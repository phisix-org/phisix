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
package com.googlecode.phisix.api.client;

import static org.junit.Assert.*;

import javax.ws.rs.client.Client;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.URLConnectionEngine;
import org.junit.Before;
import org.junit.Test;

import com.github.mkstayalive.randomuseragent.RandomUserAgent;
import com.googlecode.phisix.api.ext.StocksProvider;
import com.googlecode.phisix.api.model.Stocks;

public class PseClientTest {

	private PseClient pseClient;

	@Before
	public void setUp() {
		Client client = new ResteasyClientBuilder()
				.httpEngine(new URLConnectionEngine())
				.register(StocksProvider.class)
				.build();
		ResteasyWebTarget target = (ResteasyWebTarget) client.target("https://www1.pse.com.ph/stockMarket");
		pseClient = target.proxy(PseClient.class);
	}
	
	@Test
	public void getSecuritiesAndIndicesForPublic() throws Exception {
		Stocks actual = pseClient.getSecuritiesAndIndicesForPublic(PseClientConstants.REFERER, RandomUserAgent.getRandomUserAgent(), "getSecuritiesAndIndicesForPublic", true);
		assertNotNull(actual);
		System.out.println(actual.getStocks().size());
	}

//	@Test
	public void findSecurityOrCompany() throws Exception {
		String actual = pseClient.findSecurityOrCompany("findSecurityOrCompany", true, "start=0&limit=1&query=sm");
		assertNotNull(actual);
		System.out.println(actual);
	}

//	@Test
	public void companyInfo() {
		String actual = pseClient.companyInfo("fetchHeaderData", true, "company=599&security=520");
		assertNotNull(actual);
		System.out.println(actual);
	}
	
//	@Test
	public void companyInfoHistoricalData() {
		String actual = pseClient.companyInfoHistoricalData("getRecentSecurityQuoteData", true, "security=520");
		assertNotNull(actual);
		System.out.println(actual);
	}
}