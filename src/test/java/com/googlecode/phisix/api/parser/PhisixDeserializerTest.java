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
package com.googlecode.phisix.api.parser;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import com.googlecode.phisix.api.model.Price;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.googlecode.phisix.api.model.Stock;

public class PhisixDeserializerTest {

	private JsonDeserializer<Stock> deserializer;
	
	@Before
	public void setUp() {
		deserializer = new PhisixDeserializer();
	}
	
	@Test
	public void deserialize() {
		JsonObject json = new JsonObject();
		
		json.addProperty("Volume", "21,000");
		json.addProperty("indicator", "D");
		json.addProperty("Change", "-5");
		json.addProperty("Price", "1.9");
		json.addProperty("StockName", "2GO Group");
		json.addProperty("indicatorImg", "");
		json.addProperty("StockSymbol", "2GO");
		
		Stock actual = deserializer.deserialize(json, null, null);
		
		assertEquals("2GO Group", actual.getName());
		assertEquals(new BigDecimal("-5"), actual.getPercentChange());
		assertEquals("PHP", actual.getPrice().getCurrency());
		assertEquals(new BigDecimal("1.9"), actual.getPrice().getAmount());
		assertEquals("2GO", actual.getSymbol());
		assertEquals(21000, actual.getVolume());
	}
	
	@Test
	public void header() {
		JsonObject json = new JsonObject();
		
		json.addProperty("Volume", "");
		json.addProperty("indicator", "U");
		json.addProperty("Change", "");
		json.addProperty("Price", "DATE");
		json.addProperty("StockName", "11/23/2012 03:46 PM");
		json.addProperty("indicatorImg", "<img src='/styles/pse/images/icons/upChange.png' width='15' height='12' hspace='100' vspace='-15'>");
		json.addProperty("StockSymbol", "Stock Update As of");
		
		Stock actual = deserializer.deserialize(json, null, null);
		
		assertNull(actual);
	}
	
	@Test
	public void index() {
		Price price = new Price();
		price.setCurrency("PHP");
		price.setAmount(new BigDecimal("0.71"));

		Stock expected = new Stock();
		expected.setName("PSEi");
		expected.setSymbol("PSEi");
		expected.setPercentChange(new BigDecimal("38.97"));
		expected.setVolume(5552);
		expected.setPrice(price);

		JsonObject json = new JsonObject();
		
		json.addProperty("Volume", "5,552.34");
		json.addProperty("indicator", "U");
		json.addProperty("Change", "38.97");
		json.addProperty("Price", "0.71");
		json.addProperty("StockName", "PSEi");
		json.addProperty("indicatorImg", "<img src='/styles/pse/images/icons/upChange.png' width='15' height='12' hspace='100' vspace='-15'>");
		json.addProperty("StockSymbol", "PSE");
		
		Stock actual = deserializer.deserialize(json, null, null);

		assertEquals(expected, actual);
	}
	
}
