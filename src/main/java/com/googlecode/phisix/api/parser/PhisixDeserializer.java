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

import java.lang.reflect.Type;
import java.math.BigDecimal;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.googlecode.phisix.api.model.Price;
import com.googlecode.phisix.api.model.Stock;

/**
 * {@link JsonDeserializer} that converts a {@link JsonObject} to a
 * {@link Stock}
 * 
 * @author Edge Dalmacio
 * 
 */
public class PhisixDeserializer implements JsonDeserializer<Stock> {

	@Override
	public Stock deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		
		JsonObject jsonObject = json.getAsJsonObject();
		
		String totalVolume = jsonObject.get("totalVolume").getAsString().replace(",", "");
		if (totalVolume.length() == 0) {
			return null;
		} else if (totalVolume.contains(".")) {
			totalVolume = totalVolume.substring(0, totalVolume.length() - 3);
		}
		
		Stock stock = new Stock();
		
		stock.setName(jsonObject.get("securityAlias").getAsString());
		stock.setPercentChange(jsonObject.get("percChangeClose").getAsBigDecimal());
		Price price = new Price();
		price.setCurrency("PHP");
		String lastTradedPrice = jsonObject.get("lastTradedPrice").getAsString().replace(",", "");
		price.setAmount(new BigDecimal(lastTradedPrice));
		stock.setPrice(price);
		stock.setSymbol(jsonObject.get("securitySymbol").getAsString());
		stock.setVolume(Long.valueOf(totalVolume));
		
		return stock;
	}

}
