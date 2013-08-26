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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.util.NoContent;

import com.googlecode.phisix.api.model.Stocks;
import com.googlecode.phisix.api.parser.GsonAwareParser;
import com.googlecode.phisix.api.parser.Parser;

/**
 * {@link MessageBodyReader} that converts an {@link InputStream} to
 * {@link Stocks} via {@link Parser}
 * 
 * @author Edge Dalmacio
 * 
 */
@Provider
public class StocksProvider implements MessageBodyReader<Stocks> {

	private final Parser<Reader, Stocks> parser;
	
	public StocksProvider() {
		this(new GsonAwareParser());
	}

	public StocksProvider(Parser<Reader, Stocks> parser) {
		this.parser = parser;
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return type.equals(Stocks.class);
	}

	@Override
	public Stocks readFrom(Class<Stocks> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {
		
		if (NoContent.isContentLengthZero(httpHeaders)) {
			return new Stocks();
		}
		
		String charset = mediaType.getParameters().get("charset");
		
		Reader isr = charset == null 
				? new InputStreamReader(entityStream) 
				: new InputStreamReader(entityStream, charset);
		
		Reader br = new BufferedReader(isr);
		
		try {
			return parser.parse(br);
		} finally {
			br.close();
		}
	}

}
