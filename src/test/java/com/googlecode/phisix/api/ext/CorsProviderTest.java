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
package com.googlecode.phisix.api.ext;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import jakarta.ws.rs.core.FeatureContext;

import org.jboss.resteasy.plugins.interceptors.CorsFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CorsProviderTest {

	@Mock
	private FeatureContext context;

	@Test
	public void allowedOriginsAll() {
		assertTrue(new CorsProvider().getCorsFilter().getAllowedOrigins().contains("*"));
	}
	
	@Test
	public void configure() {
		CorsFilter corsFilter = new CorsFilter();

		boolean actual = new CorsProvider(corsFilter).configure(context);
		assertTrue(actual);
		
		verify(context).register(same(corsFilter));
	}
}
