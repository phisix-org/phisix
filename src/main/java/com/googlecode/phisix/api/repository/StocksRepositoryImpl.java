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

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;

import org.apache.commons.lang3.time.DatePrinter;
import org.apache.commons.lang3.time.FastDateFormat;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.URLConnectionEngine;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.googlecode.phisix.api.client.GaClient;
import com.googlecode.phisix.api.client.GaClientConstants;
import com.googlecode.phisix.api.client.PseClient;
import com.googlecode.phisix.api.client.PseClientConstants;
import com.googlecode.phisix.api.ext.StocksProvider;
import com.googlecode.phisix.api.model.Price;
import com.googlecode.phisix.api.model.Stock;
import com.googlecode.phisix.api.model.Stocks;

/**
 * 
 * @author Edge Dalmacio
 *
 */
public class StocksRepositoryImpl implements StocksRepository {

	private static final DatePrinter datePrinter = FastDateFormat.getInstance("yyyy-MM-dd", TimeZone.getTimeZone("GMT+8"));
	private final PseClient pseClient;
	private final GaClient gaClient;
	private final Pattern pattern = Pattern.compile("\"listedCompany_companyId\":\"(\\d+)\".*\"securityId\":\"(\\d+)\"");
	private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private final MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
	
	public StocksRepositoryImpl() {
		Client pseClient = new ResteasyClientBuilder()
				.httpEngine(new URLConnectionEngine())
				.register(StocksProvider.class)
				.build();
		this.pseClient = ((ResteasyWebTarget) pseClient.target("https://www1.pse.com.ph/stockMarket")).proxy(PseClient.class);
		
		// TODO make this async
		Client gaClient = new ResteasyClientBuilder()
				.httpEngine(new URLConnectionEngine())
				.build();
		this.gaClient = ((ResteasyWebTarget) gaClient.target("http://www.google-analytics.com")).proxy(GaClient.class);
	}
	
	public StocksRepositoryImpl(PseClient client) {
		this(client, null);
	}

	public StocksRepositoryImpl(PseClient pseClient, GaClient gaClient) {
		this.pseClient = pseClient;
		this.gaClient = gaClient;
	}
	
	@Override
	public Stocks findAll() {
		Stocks stocks = (Stocks) memcache.get("ALL");
		if (stocks == null) {
			stocks = pseClient.getSecuritiesAndIndicesForPublic(PseClientConstants.REFERER, "getSecuritiesAndIndicesForPublic", true);
			memcache.put("ALL", stocks, Expiration.byDeltaSeconds(60));
		}
		if (gaClient != null) {
			gaClient.eventTracking(GaClientConstants.VERSION, GaClientConstants.TRACKING_ID, GaClientConstants.CLIENT_ID, GaClientConstants.EVENT_HIT, "stocks", "all", GaClientConstants.USER_AGENT);
		}
		return stocks;
	}
	
	@Override
	public String findBySymbol(String symbol) {
		
		String securityOrCompany = pseClient.findSecurityOrCompany(
				"findSecurityOrCompany", true,
				String.format("start=0&limit=1&query=%s", symbol));
		
		Matcher matcher = pattern.matcher(securityOrCompany);
		
		if (matcher.find()) {
			if (gaClient != null) {
				gaClient.eventTracking(GaClientConstants.VERSION, GaClientConstants.TRACKING_ID, GaClientConstants.CLIENT_ID, GaClientConstants.EVENT_HIT, "stocks", symbol, GaClientConstants.USER_AGENT);
			}
			return pseClient.companyInfo("fetchHeaderData", true, 
					String.format("company=%s&security=%s", 
							matcher.group(1),
							matcher.group(2)));
		}
		
		return null;
	}
	
	@Override
	public Stocks findBySymbolAndTradingDate(String symbol, Date tradingDate) {
		Key stockKey = KeyFactory.createKey("Stock", symbol);
		Entity stockEntity;
		try {
			stockEntity = datastore.get(stockKey);
		} catch (EntityNotFoundException e) {
			throw new NotFoundException();
		}

		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+8"));
		calendar.setTime(tradingDate);
		
		Key historyKey = KeyFactory.createKey(stockKey, "History", calendar.getTimeInMillis());
		Entity historyEntity;
		try {
			historyEntity = datastore.get(historyKey);
		} catch (EntityNotFoundException e) {
			throw new NotFoundException();
		}
		
		Price price = new Price();
		price.setCurrency("PHP");
		price.setAmount(new BigDecimal((String) historyEntity.getProperty("close")));

		Stock stock = new Stock();
		stock.setSymbol(symbol);
		stock.setName((String) stockEntity.getProperty("name"));
		stock.setPrice(price);
		stock.setVolume((Long) historyEntity.getProperty("volume"));
		
		Stocks stocks = new Stocks();
		stocks.setAsOf(calendar);
		stocks.getStocks().add(stock);
		
		if (gaClient != null) {
			String formattedTradingDate = datePrinter.format(tradingDate);
			gaClient.eventTracking(GaClientConstants.VERSION, GaClientConstants.TRACKING_ID, GaClientConstants.CLIENT_ID, GaClientConstants.EVENT_HIT, "stocks", symbol + "." + formattedTradingDate, GaClientConstants.USER_AGENT);
		}
		return stocks;
	}
	
	@Override
	public void save(Stocks stocks) {
		Calendar tradingDate = retrieveTradingDate(stocks);
		for (Stock stock : stocks.getStocks()) {
			Transaction txn = datastore.beginTransaction(TransactionOptions.Builder.withXG(true));
			try {
				Entity stockEntity;
				Key key = KeyFactory.createKey("Stock", stock.getSymbol());
				try {
					stockEntity = datastore.get(key);
				} catch (EntityNotFoundException e) {
					stockEntity = new Entity(key);
					stockEntity.setUnindexedProperty("name", stock.getName());
					datastore.put(stockEntity);
				}
				Entity historyEntity = new Entity("History", tradingDate.getTimeInMillis(), key);
				historyEntity.setProperty("tradingDate", tradingDate.getTime());
				historyEntity.setUnindexedProperty("close", stock.getPrice().getAmount().toPlainString());
				historyEntity.setUnindexedProperty("volume", stock.getVolume());
				datastore.put(historyEntity);
				txn.commit();
			} finally {
				if (txn.isActive()) {
					txn.rollback();
				}
			}
		}
	}

	private Calendar retrieveTradingDate(Stocks stocks) {
		Calendar tradingDate = stocks.getAsOf();
		tradingDate.set(Calendar.HOUR_OF_DAY, 0);
		tradingDate.set(Calendar.MINUTE, 0);
		tradingDate.set(Calendar.SECOND, 0);
		return tradingDate;
	}
}
