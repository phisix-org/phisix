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
package com.googlecode.phisix.api.resource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.googlecode.phisix.api.model.Stock;
import com.googlecode.phisix.api.model.Stocks;
import com.googlecode.phisix.api.parser.GsonAwareParser;
import com.googlecode.phisix.api.parser.Parser;
import com.googlecode.phisix.api.urlfetch.URLFetchService;
import com.googlecode.phisix.api.urlfetch.URLFetchServiceImpl;

/**
 * * Handles:
 * <ul>
 * <li>GET /stocks.xml</li>
 * <li>GET /stocks.json</li>
 * <li>GET /stocks/{symbol}.xml</li>
 * <li>GET /stocks/{symbol}.json</li>
 * </ul>
 * 
 * @author Edge Dalmacio
 * 
 */
@Path(value = "/stocks")
public class StocksResource {

	private static final String DEFAULT_URL = "http://pse.com.ph/stockMarket/home.html?method=getSecuritiesAndIndicesForPublic&ajax=true";
	private final URLFetchService urlFetchService;
	private final Parser<Reader, Stocks> parser;

	public StocksResource() throws Exception {
		this(new URLFetchServiceImpl(), new GsonAwareParser());
	}
	
	public StocksResource(URLFetchService urlFetchService, Parser<Reader, Stocks> parser) {
		this.urlFetchService = urlFetchService;
		this.parser = parser;
	}

	@GET
	@Path(value = "/")
	public Stocks getAllStocks() throws Exception {
		Reader reader = null;
		try {
			InputStream stream = urlFetchService.fetch(new URL(DEFAULT_URL));
			reader = new BufferedReader(new InputStreamReader(stream));
			return parser.parse(reader);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	@GET
	@Path(value = "/{symbol}")
	public Stocks getStock(@PathParam(value = "symbol") String symbol) throws Exception {
		Stocks allStocks = getAllStocks();
		for (Stock stock : allStocks.getStocks()) {
			if (symbol.equalsIgnoreCase(stock.getSymbol())) {
				Stocks stocks = new Stocks();
				stocks.getStocks().add(stock);
				stocks.setAsOf(allStocks.getAsOf());
				return stocks;
			}
		}
		throw new NotFoundException();
	}
	
}
