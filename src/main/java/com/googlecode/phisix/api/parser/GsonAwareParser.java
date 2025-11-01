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

import java.io.Reader;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateParser;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.googlecode.phisix.api.model.Stock;
import com.googlecode.phisix.api.model.Stocks;

/**
 * {@link Parser} that delegates to a {@link JsonParser}.
 * 
 * @author Edge Dalmacio
 * 
 */
public class GsonAwareParser implements Parser<Reader, Stocks> {

	private static final Logger LOGGER = LoggerFactory.getLogger(GsonAwareParser.class);
	private static final TimeZone ASIA_MANILA = TimeZone.getTimeZone("Asia/Manila");
	private static final DateParser dateParser = FastDateFormat.getInstance("MM/dd/yyyy hh:mm a", ASIA_MANILA);
	private final Gson gson;
	
	public GsonAwareParser() {
		Type type = new TypeToken<Stock>() {}.getType();
		gson = new GsonBuilder()
			.registerTypeAdapter(type, new PhisixDeserializer())
			.create();
	}

	@Override
	public Stocks parse(Reader source) {
		Stocks stocks = new Stocks();
		
		JsonElement parse = JsonParser.parseReader(source);
		if (JsonNull.INSTANCE.equals(parse)) {
			return stocks;
		}
		
		JsonArray jsonArray;
		JsonObject rootObject = null;
		
		// Handle both array and object formats
		if (parse.isJsonObject()) {
			rootObject = parse.getAsJsonObject();
			if (rootObject.has("stock") && rootObject.get("stock").isJsonArray()) {
				jsonArray = rootObject.get("stock").getAsJsonArray();
				// Parse as_of date if present
				JsonElement asOfElement = rootObject.get("as_of");
				if (asOfElement != null && !asOfElement.isJsonNull()) {
					Calendar asOfCalendar = parseAsOfDateFromString(asOfElement.getAsString());
					if (asOfCalendar != null) {
						stocks.setAsOf(asOfCalendar);
					}
				}
			} else {
				// Fallback: treat object as array with single element
				jsonArray = new JsonArray();
				jsonArray.add(parse);
			}
		} else {
			jsonArray = parse.getAsJsonArray();
		}
		
		Type type = new TypeToken<Stock>() {}.getType();
		
//		boolean isFirst = true;
		for (JsonElement jsonElement : jsonArray) {
//			if (isFirst) {
//				isFirst = !isFirst;
//				stocks.setAsOf(parseAsOfDate(jsonElement.getAsJsonObject()));
//				continue;
//			}
			Stock stock = gson.fromJson(jsonElement, type);
			if (stock != null) {
				stocks.getStocks().add(stock);
			}
		}
		if (stocks.getAsOf() == null) {
			Calendar calendar = Calendar.getInstance(ASIA_MANILA);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			stocks.setAsOf(calendar);
		}
		
		return stocks;
	}
	
	protected Calendar parseAsOfDate(JsonObject jsonObject) {
		String asOfDate = jsonObject.get("securityAlias").getAsString();
		Calendar calendar = null;
		try {
			Date date = dateParser.parse(asOfDate);
			calendar = Calendar.getInstance(ASIA_MANILA);
			calendar.setTime(date);
		} catch (ParseException e) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn(e.getMessage(), e);
			}
		}
		return calendar;
	}
	
	protected Calendar parseAsOfDateFromString(String asOfDateString) {
		Calendar calendar = null;
		try {
			// Handle ISO 8601 format: "2025-10-31T00:00:00+08:00"
			java.text.SimpleDateFormat isoFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
			Date date = isoFormat.parse(asOfDateString);
			calendar = Calendar.getInstance(ASIA_MANILA);
			calendar.setTime(date);
		} catch (ParseException e) {
			try {
				// Try without timezone offset
				java.text.SimpleDateFormat isoFormatNoTz = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				Date date = isoFormatNoTz.parse(asOfDateString);
				calendar = Calendar.getInstance(ASIA_MANILA);
				calendar.setTime(date);
			} catch (ParseException e2) {
				if (LOGGER.isWarnEnabled()) {
					LOGGER.warn("Failed to parse as_of date: " + asOfDateString, e2);
				}
			}
		}
		return calendar;
	}

}
