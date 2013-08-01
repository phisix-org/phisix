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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.SocketTimeoutException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.googlecode.phisix.api.model.Stocks;
import com.googlecode.phisix.api.parser.GsonAwareParser;
import com.googlecode.phisix.api.parser.Parser;
import com.googlecode.phisix.api.urlfetch.URLFetchService;
import com.googlecode.phisix.api.urlfetch.URLFetchServiceImpl;

/**
 * Handles:
 * <ul>
 * <li>GET /stocks.xml</li>
 * <li>GET /stocks.json</li>
 * </ul>
 * 
 * @author Edge Dalmacio
 * @deprecated Use {@link StocksResource} instead.
 */
public class StocksServlet extends HttpServlet {

	private static final Logger LOGGER = LoggerFactory.getLogger(StocksServlet.class);
	private static final String DEFAULT_URL = "http://pse.com.ph/stockMarket/home.html?method=getSecuritiesAndIndicesForPublic&ajax=true";
	private final URLFetchService urlFetchService;
	private final Parser<Reader, Stocks> parser;
	private volatile Marshaller marshaller;
	private volatile Gson gson;
	
	public StocksServlet() throws Exception {
		this(new URLFetchServiceImpl(), new GsonAwareParser());
	}
	
	public StocksServlet(URLFetchService urlFetchService, Parser<Reader, Stocks> parser) {
		this.urlFetchService = urlFetchService;
		this.parser = parser;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Reader reader = null;
		try {
			InputStream stream = urlFetchService.fetch(new URL(DEFAULT_URL));
			reader = new BufferedReader(new InputStreamReader(stream));
			Stocks stocks = parser.parse(reader);
			resp.setDateHeader("Last-Modified", stocks.getAsOf().getTimeInMillis());
			String requestURI = req.getRequestURI();
			if (requestURI.endsWith(".xml")) {
				resp.setContentType("text/xml");
				getMarshaller().marshal(stocks, resp.getOutputStream());
			} else if (requestURI.endsWith(".json")) {
				resp.setContentType("application/json");
				getGson().toJson(stocks, resp.getWriter());
			}
		} catch (SocketTimeoutException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(e.getMessage(), e);
			}
			resp.setStatus(HttpServletResponse.SC_GATEWAY_TIMEOUT);
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(e.getMessage(), e);
			}
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
	
	private Marshaller getMarshaller() throws JAXBException {
		Marshaller m = marshaller;
		if (m == null) { // 1st check (no lock)
			synchronized (this) {
				m = marshaller;
				if (m == null) { // 2nd check (w/lock)
					JAXBContext context = JAXBContext.newInstance("com.googlecode.phisix.api.model");
					m = marshaller = context.createMarshaller();
				}
			}
		}
		return marshaller;
	}
	
	private Gson getGson() {
		Gson g = gson;
		if (g == null) { // 1st check (no lock)
			synchronized (this) {
				g = gson;
				if (g == null) { // 2nd check (w/lock)
					g = gson = new GsonBuilder()
						.create();
				}
			}
		}
		return gson;
	}
	
	private static final long serialVersionUID = -1419919200052574553L;

}
