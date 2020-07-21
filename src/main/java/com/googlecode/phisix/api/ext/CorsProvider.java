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

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.plugins.interceptors.CorsFilter;

/**
 * @author Edge Dalmacio
 */
@Provider
public class CorsProvider implements Feature {

	private final CorsFilter corsFilter;

	public CorsProvider() {
		this.corsFilter = new CorsFilter();
		this.corsFilter.getAllowedOrigins().add("*");
	}

	public CorsProvider(CorsFilter corsFilter) {
		this.corsFilter = corsFilter;
	}

	@Override
	public boolean configure(FeatureContext context) {
		context.register(corsFilter);
		return true;
	}

	public CorsFilter getCorsFilter() {
		return corsFilter;
	}

}
