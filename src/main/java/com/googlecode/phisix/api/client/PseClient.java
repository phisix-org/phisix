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

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import com.googlecode.phisix.api.model.Stocks;

/**
 * Proxy interface for making calls to PSE website
 * 
 * @author Edge Dalmacio
 * 
 */
public interface PseClient {

	Stocks getSecuritiesAndIndicesForPublic();

	@POST
	@Path("home.html")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	String findSecurityOrCompany(
			@QueryParam(value = "method") String method, 
			@QueryParam(value = "ajax") boolean ajax,
			String body);

	@POST
	@Path("companyInfo.html")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	String companyInfo(
			@QueryParam(value = "method") String method, 
			@QueryParam(value = "ajax") boolean ajax, 
			String body);

	@POST
	@Path("companyInfoHistoricalData.html")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	String companyInfoHistoricalData(
			@QueryParam(value = "method") String method, 
			@QueryParam(value = "ajax") boolean ajax, 
			String body);
}
