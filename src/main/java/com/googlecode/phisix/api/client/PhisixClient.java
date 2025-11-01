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

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import com.googlecode.phisix.api.model.Stocks;

/**
 * @author Edge Dalmacio
 */
public interface PhisixClient {

	@GET
	@Path("/stocks/{symbol}.{tradingDate}.json")
	@Produces(MediaType.APPLICATION_JSON)
	Stocks getStockByDate(
			@PathParam("symbol") String symbol, 
			@PathParam("tradingDate") String tradingDate);

}
