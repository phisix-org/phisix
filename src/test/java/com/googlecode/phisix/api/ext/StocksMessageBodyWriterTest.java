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

import java.io.ByteArrayOutputStream;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.TimeZone;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.MessageBodyWriter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.googlecode.phisix.api.model.Price;
import com.googlecode.phisix.api.model.Stock;
import com.googlecode.phisix.api.model.Stocks;

@RunWith(MockitoJUnitRunner.class)
public class StocksMessageBodyWriterTest {

	private MessageBodyWriter<Stocks> writer;
	private static final TimeZone ASIA_MANILA = TimeZone.getTimeZone("Asia/Manila");

	@Before
	public void setUp() {
		writer = new StocksMessageBodyWriter();
	}

	@Test
	public void isWriteableShouldReturnTrueForStocksClassWithJsonMediaType() {
		assertTrue(writer.isWriteable(Stocks.class, null, null, MediaType.APPLICATION_JSON_TYPE));
		assertTrue(writer.isWriteable(Stocks.class, null, null, 
			new MediaType("application", "json")));
		assertTrue(writer.isWriteable(Stocks.class, null, null, 
			new MediaType("application", "json", "UTF-8")));
	}

	@Test
	public void isWriteableShouldReturnFalseForNonStocksClass() {
		assertFalse(writer.isWriteable(String.class, null, null, MediaType.APPLICATION_JSON_TYPE));
		assertFalse(writer.isWriteable(Object.class, null, null, MediaType.APPLICATION_JSON_TYPE));
	}

	@Test
	public void isWriteableShouldReturnFalseForNonJsonMediaType() {
		assertFalse(writer.isWriteable(Stocks.class, null, null, MediaType.APPLICATION_XML_TYPE));
		assertFalse(writer.isWriteable(Stocks.class, null, null, MediaType.TEXT_PLAIN_TYPE));
		assertFalse(writer.isWriteable(Stocks.class, null, null, null));
	}

	@Test
	public void writeToShouldSerializeWithAsOfInIso8601Format() throws Exception {
		// Arrange
		Stocks stocks = createStocksWithDate(2024, Calendar.JANUARY, 15, 0, 0, 0);
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		// Act
		writer.writeTo(stocks, Stocks.class, null, new Annotation[0], 
			MediaType.APPLICATION_JSON_TYPE, null, outputStream);
		
		// Assert
		String json = outputStream.toString("UTF-8");
		JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
		
		// Verify as_of field exists and is in ISO 8601 format
		assertTrue("JSON should contain 'as_of' field", jsonObject.has("as_of"));
		String asOfValue = jsonObject.get("as_of").getAsString();
		assertEquals("as_of should be in ISO 8601 format with colon in timezone", "2024-01-15T00:00:00+08:00", asOfValue);
		
		// Verify stock array exists (singular to match XML schema)
		assertTrue("JSON should contain 'stock' array", jsonObject.has("stock"));
		assertTrue("stock should be an array", jsonObject.get("stock").isJsonArray());
	}

	@Test
	public void writeToShouldSerializeMultipleStocks() throws Exception {
		// Arrange
		Stocks stocks = new Stocks();
		Calendar calendar = Calendar.getInstance(ASIA_MANILA);
		calendar.set(2024, Calendar.JANUARY, 15, 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		stocks.setAsOf(calendar);
		
		// Add multiple stocks
		stocks.getStocks().add(createStock("BDO", "Banco de Oro", new BigDecimal("139.00"), 3643470L));
		stocks.getStocks().add(createStock("SM", "SM Investments", new BigDecimal("729.00"), 582410L));
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		// Act
		writer.writeTo(stocks, Stocks.class, null, new Annotation[0], 
			MediaType.APPLICATION_JSON_TYPE, null, outputStream);
		
		// Assert
		String json = outputStream.toString("UTF-8");
		JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
		
		assertEquals("Should have 2 stocks", 2, jsonObject.get("stock").getAsJsonArray().size());
		assertTrue("Should have as_of field", jsonObject.has("as_of"));
	}

	@Test
	public void writeToShouldHandleNullAsOf() throws Exception {
		// Arrange
		Stocks stocks = new Stocks();
		stocks.setAsOf(null);
		stocks.getStocks().add(createStock("BDO", "Banco de Oro", new BigDecimal("139.00"), 3643470L));
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		// Act
		writer.writeTo(stocks, Stocks.class, null, new Annotation[0], 
			MediaType.APPLICATION_JSON_TYPE, null, outputStream);
		
		// Assert
		String json = outputStream.toString("UTF-8");
		JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
		
		// When asOf is null, as_of field should not be present
		assertFalse("as_of should not be present when asOf is null", jsonObject.has("as_of"));
		assertTrue("stock array should still be present", jsonObject.has("stock"));
	}

	@Test
	public void writeToShouldUseUnderscoreFieldName() throws Exception {
		// Arrange
		Stocks stocks = createStocksWithDate(2024, Calendar.JANUARY, 15, 0, 0, 0);
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		// Act
		writer.writeTo(stocks, Stocks.class, null, new Annotation[0], 
			MediaType.APPLICATION_JSON_TYPE, null, outputStream);
		
		// Assert
		String json = outputStream.toString("UTF-8");
		
		// Verify it uses "as_of" (underscore) not "asOf" (camelCase)
		assertTrue("JSON should contain 'as_of' field", json.contains("\"as_of\""));
		assertFalse("JSON should NOT contain 'asOf' field", json.contains("\"asOf\""));
	}

	@Test
	public void writeToShouldFormatDateWithTimezone() throws Exception {
		// Arrange - Test with specific date that should include timezone
		Stocks stocks = createStocksWithDate(2025, Calendar.OCTOBER, 31, 0, 0, 0);
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		// Act
		writer.writeTo(stocks, Stocks.class, null, new Annotation[0], 
			MediaType.APPLICATION_JSON_TYPE, null, outputStream);
		
		// Assert
		String json = outputStream.toString("UTF-8");
		JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
		
		String asOfValue = jsonObject.get("as_of").getAsString();
		// Should be in format: yyyy-MM-dd'T'HH:mm:ssXXX (e.g., "2025-10-31T00:00:00+08:00")
		assertTrue("Date should include timezone offset with colon", asOfValue.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}[+-]\\d{2}:\\d{2}"));
		assertTrue("Date should be 2025-10-31", asOfValue.startsWith("2025-10-31"));
		assertTrue("Date should end with +08:00", asOfValue.endsWith("+08:00"));
	}

	// Helper methods
	private Stocks createStocksWithDate(int year, int month, int day, int hour, int minute, int second) {
		Stocks stocks = new Stocks();
		Calendar calendar = Calendar.getInstance(ASIA_MANILA);
		calendar.set(year, month, day, hour, minute, second);
		calendar.set(Calendar.MILLISECOND, 0);
		stocks.setAsOf(calendar);
		stocks.getStocks().add(createStock("BDO", "Banco de Oro", new BigDecimal("139.00"), 3643470L));
		return stocks;
	}

	private Stock createStock(String symbol, String name, BigDecimal amount, Long volume) {
		Stock stock = new Stock();
		stock.setSymbol(symbol);
		stock.setName(name);
		
		Price price = new Price();
		price.setCurrency("PHP");
		price.setAmount(amount);
		stock.setPrice(price);
		
		stock.setVolume(volume);
		return stock;
	}
}

