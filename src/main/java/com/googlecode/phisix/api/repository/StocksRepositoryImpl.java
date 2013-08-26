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

import javax.ws.rs.client.Client;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.URLConnectionEngine;

import com.googlecode.phisix.api.client.PseClient;
import com.googlecode.phisix.api.ext.StocksProvider;
import com.googlecode.phisix.api.model.Stocks;

/**
 * 
 * @author Edge Dalmacio
 *
 */
public class StocksRepositoryImpl implements StocksRepository {

	private final PseClient client;
	
	public StocksRepositoryImpl() {
		Client client = new ResteasyClientBuilder()
				.httpEngine(new URLConnectionEngine())
				.register(StocksProvider.class)
				.build();
		ResteasyWebTarget target = (ResteasyWebTarget) client.target("http://www.pse.com.ph/stockMarket");
		this.client = target.proxy(PseClient.class);
	}
	
	@Override
	public Stocks findAll() {
		return client.getSecuritiesAndIndicesForPublic("getSecuritiesAndIndicesForPublic", true);
	}
	
	@Override
	public String findBySymbol(String symbol) {
//		client.findSecurityOrCompany("findSecurityOrCompany", true, String.format("start=0&limit=1&query=%1", symbol) );
//		client.companyInfo("fetchHeaderData", true, "company=599&security=520");
		throw new UnsupportedOperationException("TODO");
	}
}
