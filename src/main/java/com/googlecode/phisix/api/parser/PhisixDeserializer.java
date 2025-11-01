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
		
		Stock stock = new Stock();
		
		// Handle both old PSE format and new PhisixClient API format
		JsonElement volumeElement = jsonObject.get("Volume");
		if (volumeElement == null || volumeElement.isJsonNull()) {
			volumeElement = jsonObject.get("volume");
		}
		
		String totalVolume = null;
		if (volumeElement != null && !volumeElement.isJsonNull()) {
			if (volumeElement.isJsonPrimitive()) {
				totalVolume = volumeElement.getAsString().replace(",", "");
			} else {
				// Try to get as number
				try {
					totalVolume = String.valueOf(volumeElement.getAsLong());
				} catch (Exception e) {
					totalVolume = volumeElement.getAsString();
				}
			}
		}
		
		if (totalVolume == null || totalVolume.length() == 0) {
			return null;
		} else if (totalVolume.contains(".")) {
			totalVolume = totalVolume.substring(0, totalVolume.length() - 3);
		}
		
		// Name field
		JsonElement nameElement = jsonObject.get("StockName");
		if (nameElement == null || nameElement.isJsonNull()) {
			nameElement = jsonObject.get("name");
		}
		if (nameElement != null && !nameElement.isJsonNull()) {
			stock.setName(nameElement.getAsString());
		}
		
		// Percent change field
		JsonElement percentChangeElement = jsonObject.get("PercentChange");
		if (percentChangeElement == null || percentChangeElement.isJsonNull()) {
			percentChangeElement = jsonObject.get("percent_change");
		}
		if (percentChangeElement != null && !percentChangeElement.isJsonNull() 
				&& percentChangeElement.isJsonPrimitive() 
				&& !percentChangeElement.getAsString().equals("null")) {
			stock.setPercentChange(percentChangeElement.getAsBigDecimal());
		}
		
		// Price field - handle both formats
		Price price = new Price();
		price.setCurrency("PHP");
		
		JsonElement priceElement = jsonObject.get("Price");
		if (priceElement != null && !priceElement.isJsonNull()) {
			// Old format: "Price" is a string
			String lastTradedPrice = priceElement.getAsString().replace(",", "");
			price.setAmount(new BigDecimal(lastTradedPrice));
		} else {
			// New format: "price" is an object with "currency" and "amount"
			JsonElement newPriceElement = jsonObject.get("price");
			if (newPriceElement != null && newPriceElement.isJsonObject()) {
				JsonObject priceObj = newPriceElement.getAsJsonObject();
				JsonElement currencyElement = priceObj.get("currency");
				if (currencyElement != null && !currencyElement.isJsonNull()) {
					price.setCurrency(currencyElement.getAsString());
				}
				JsonElement amountElement = priceObj.get("amount");
				if (amountElement != null && !amountElement.isJsonNull()) {
					if (amountElement.isJsonPrimitive()) {
						try {
							price.setAmount(amountElement.getAsBigDecimal());
						} catch (Exception e) {
							String amountStr = amountElement.getAsString().replace(",", "");
							price.setAmount(new BigDecimal(amountStr));
						}
					} else {
						String amountStr = amountElement.getAsString().replace(",", "");
						price.setAmount(new BigDecimal(amountStr));
					}
				}
			}
		}
		stock.setPrice(price);
		
		// Symbol field
		JsonElement symbolElement = jsonObject.get("StockSymbol");
		if (symbolElement == null || symbolElement.isJsonNull()) {
			symbolElement = jsonObject.get("symbol");
		}
		if (symbolElement != null && !symbolElement.isJsonNull()) {
			String symbol = symbolElement.getAsString();
			String stockName = stock.getName();
			stock.setSymbol("PSEi".equalsIgnoreCase(stockName) ? "PSEi" : symbol);
		}
		
		stock.setVolume(Long.valueOf(totalVolume));
		
		return stock;
	}

}
