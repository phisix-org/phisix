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

import com.googlecode.phisix.api.ext.StocksProvider;

public class GaClientTest {

	private GaClient gaClient;

	@Before
	public void setUp() {
		Client client = new ResteasyClientBuilder()
				.httpEngine(new URLConnectionEngine())
				.register(StocksProvider.class)
				.build();
		ResteasyWebTarget target = (ResteasyWebTarget) client.target("http://www.google-analytics.com");
		gaClient = target.proxy(GaClient.class);
	}

	@Test
	public void pageTracking() {
		String version = "1";
		String trackingId = "UA-8834758-3";
		String clientId = "2A446A02-7235-4905-B45D-38736FCA2669";
		String hitType = "pageview";
		String page = "stocks";
		gaClient.pageTracking(version, trackingId, clientId, hitType, page);
		assertTrue(true);
	}
}
