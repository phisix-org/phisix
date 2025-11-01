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

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.Provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.googlecode.phisix.api.model.Stocks;

	/**
	 * Custom MessageBodyWriter for Stocks that formats Calendar dates as ISO 8601 strings
	 * and uses "as_of" field name instead of "asOf"
	 * 
	 * @author Edge Dalmacio
	 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class StocksMessageBodyWriter implements MessageBodyWriter<Stocks> {

	private static final TimeZone ASIA_MANILA = TimeZone.getTimeZone("Asia/Manila");
	// Use XXX for timezone offset with colon: +08:00 instead of +08
	private static final SimpleDateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
	
	static {
		ISO_DATE_FORMAT.setTimeZone(ASIA_MANILA);
	}

	private final Gson gson;

	public StocksMessageBodyWriter() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		// Register custom serializer for Stocks to format Calendar as ISO 8601 and rename field
		gsonBuilder.registerTypeAdapter(Stocks.class, new StocksSerializer());
		this.gson = gsonBuilder.create();
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return Stocks.class.isAssignableFrom(type) && mediaType != null && 
			MediaType.APPLICATION_JSON_TYPE.getType().equals(mediaType.getType()) && 
			MediaType.APPLICATION_JSON_TYPE.getSubtype().equals(mediaType.getSubtype());
	}

	@Override
	public void writeTo(Stocks stocks, Class<?> type, Type genericType, Annotation[] annotations,
			MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
			throws IOException, WebApplicationException {
		
		Writer writer = new OutputStreamWriter(entityStream, "UTF-8");
		gson.toJson(stocks, Stocks.class, writer);
		writer.flush();
	}

	/**
	 * Custom serializer for Stocks that:
	 * 1. Formats Calendar asOf as ISO 8601 string
	 * 2. Uses "as_of" field name instead of "asOf"
	 */
	private static class StocksSerializer implements JsonSerializer<Stocks> {
		
		private final Gson stockGson;

		public StocksSerializer() {
			// Gson for serializing Stock objects
			this.stockGson = new Gson();
		}

		@Override
		public JsonElement serialize(Stocks stocks, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject jsonObject = new JsonObject();
			
			// Serialize stocks array with field name "stocks" (plural)
			JsonElement stocksArray = stockGson.toJsonTree(stocks.getStocks());
			jsonObject.add("stocks", stocksArray);
			
			// Serialize asOf Calendar as ISO 8601 string with field name "as_of"
			Calendar asOf = stocks.getAsOf();
			if (asOf != null) {
				String isoDateString = ISO_DATE_FORMAT.format(asOf.getTime());
				jsonObject.addProperty("as_of", isoDateString);
			}
			
			return jsonObject;
		}
	}
}

