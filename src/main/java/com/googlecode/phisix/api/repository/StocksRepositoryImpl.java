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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	private final Pattern pattern = Pattern.compile("\"listedCompany_companyId\":\"(\\d+)\".*\"securityId\":\"(\\d+)\"");
	
	public StocksRepositoryImpl() {
		Client client = new ResteasyClientBuilder()
				.httpEngine(new URLConnectionEngine())
				.register(StocksProvider.class)
				.build();
		ResteasyWebTarget target = (ResteasyWebTarget) client.target("http://www.pse.com.ph/stockMarket");
		this.client = target.proxy(PseClient.class);
	}
	
	public StocksRepositoryImpl(PseClient client) {
		this.client = client;
	}
	
	@Override
	public Stocks findAll() {
		return client.getSecuritiesAndIndicesForPublic("getSecuritiesAndIndicesForPublic", true);
	}
	
	@Override
	public String findBySymbol(String symbol) {
		
		String securityOrCompany = client.findSecurityOrCompany(
				"findSecurityOrCompany", true,
				String.format("start=0&limit=1&query=%s", symbol));
		
		Matcher matcher = pattern.matcher(securityOrCompany);
		
		if (matcher.find()) {
			return client.companyInfo("fetchHeaderData", true, 
					String.format("company=%s&security=%s", 
							matcher.group(1),
							matcher.group(2)));
		}
		
		return null;
	}
}
