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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

	private static final TimeZone ASIA_MANILA = TimeZone.getTimeZone("Asia/Manila");
	private final JsonParser jsonParser;
	private final Gson gson;
	
	public GsonAwareParser() {
		jsonParser = new JsonParser();
		Type type = new TypeToken<Collection<Stock>>() {}.getType();
		gson = new GsonBuilder()
			.registerTypeAdapter(type, new PhisixDeserializer())
			.create();
	}

	@Override
	public Stocks parse(Reader source) throws Exception {
		Stocks stocks = new Stocks();
		
		JsonArray jsonArray = jsonParser.parse(source).getAsJsonArray();
		Type type = new TypeToken<Collection<Stock>>() {}.getType();
		
		boolean isFirst = true;
		for (JsonElement jsonElement : jsonArray) {
			if (isFirst) {
				isFirst = !isFirst;
				stocks.setAsOf(parseAsOfDate(jsonElement.getAsJsonObject()));
				continue;
			}
			Stock stock = gson.fromJson(jsonElement, type);
			if (stock != null) {
				stocks.getStocks().add(stock);
			}
		}
		
		return stocks;
	}
	
	protected Calendar parseAsOfDate(JsonObject jsonObject) throws ParseException {
		String asOfDate = jsonObject.get("securityAlias").getAsString();
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		dateFormat.setTimeZone(ASIA_MANILA);
		Date date = dateFormat.parse(asOfDate);
		Calendar calendar = Calendar.getInstance(ASIA_MANILA);
		calendar.setTime(date);
		return calendar;
	}

}
