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

import jakarta.ws.rs.client.Client;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.URLConnectionEngine;
import org.junit.Test;

import com.googlecode.phisix.api.ext.StocksProvider;
import com.googlecode.phisix.api.model.Stocks;

public class PhisixClientTest {

	@Test
	public void getStockByDate() {
		Client client = ((ResteasyClientBuilder) ResteasyClientBuilder.newBuilder())
				.httpEngine(new URLConnectionEngine())
				.register(StocksProvider.class)
				.build();
		ResteasyWebTarget target = (ResteasyWebTarget) client.target("https://phisix-api2.appspot.com");
		PhisixClient phisixClient = target.proxy(PhisixClient.class);

		Stocks actual = phisixClient.getStockByDate("SM", "2025-10-31");
		assertNotNull(actual);
		System.out.println(actual);
	}
}
