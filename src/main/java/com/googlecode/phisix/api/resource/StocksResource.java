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

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;

import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.apache.commons.lang3.time.DateParser;
import org.apache.commons.lang3.time.FastDateFormat;

import com.google.appengine.api.taskqueue.QueueFactory;
import com.googlecode.phisix.api.model.Stock;
import com.googlecode.phisix.api.model.Stocks;
import com.googlecode.phisix.api.repository.StocksRepository;
import com.googlecode.phisix.api.repository.StocksRepositoryImpl;

/**
 * * Handles:
 * <ul>
 * <li>GET /stocks.xml</li>
 * <li>GET /stocks.json</li>
 * <li>GET /stocks/{symbol}.xml</li>
 * <li>GET /stocks/{symbol}.json</li>
 * <li>GET /stocks/{symbol}.{yyyy-MM-dd}.xml</li>
 * <li>GET /stocks/{symbol}.{yyyy-MM-dd}.json</li>
 * </ul>
 * 
 * @author Edge Dalmacio
 * 
 */
@Path(value = "/stocks")
public class StocksResource {

	private static final DateParser dateParser = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT+8"));
	private final StocksRepository stocksRepository;

	public StocksResource() throws Exception {
		this(new StocksRepositoryImpl());
	}
	
	public StocksResource(StocksRepository stocksRepository) {
		this.stocksRepository = stocksRepository;
	}

	@GET
	@Path(value = "/")
	public Stocks getAllStocks() {
		return stocksRepository.findAll();
	}

	@GET
	@Path(value = "/{symbol}")
	public Stocks getStock(@PathParam(value = "symbol") String symbol) {
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
	
	public Stocks getStockByDate(@PathParam(value = "symbol") String symbol,
			@PathParam(value = "date") String date) {
		return getStockByDate(symbol, date, "PH");
	}
	
	/**
	 * 
	 * @param symbol           stock symbol
	 * @param date             trading date
	 * @param appengineCountry determines if the request is from Appengine or not
	 * @return
	 */
	@GET
	@Path(value = "/{symbol}.{date}")
	public Stocks getStockByDate(@PathParam(value = "symbol") String symbol,
			@PathParam(value = "date") String date,
			@HeaderParam(value = "X-Appengine-Country") String appengineCountry) {
		if (appengineCountry == null) {
			return stocksRepository.findBySymbolAndTradingDate(symbol, date);
		} else {
			Date tradingDate;
			try {
				tradingDate = dateParser.parse(date);
			} catch (ParseException e) {
				throw new BadRequestException(e.getMessage());
			}
			return stocksRepository.findBySymbolAndTradingDate(symbol.toUpperCase(), tradingDate);
		}
	}

	@GET @POST
	@Path(value = "/archive")
	public void archive(
			@HeaderParam(value = "X-AppEngine-Cron") 
			@DefaultValue(value = "false") boolean isCron) {
		if (isCron) {
			QueueFactory.getDefaultQueue().add(withUrl("/stocks/archive"));
		} else {
			stocksRepository.save(getAllStocks());
		}
	}
}
