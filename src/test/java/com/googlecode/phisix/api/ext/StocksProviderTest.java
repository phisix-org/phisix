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
import static org.mockito.Mockito.*;

import java.io.InputStream;
import java.io.Reader;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.MessageBodyReader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.googlecode.phisix.api.model.Stocks;
import com.googlecode.phisix.api.parser.Parser;

@RunWith(MockitoJUnitRunner.class)
public class StocksProviderTest {

	private MessageBodyReader<Stocks> reader;
	
	@Mock
	private Parser<Reader, Stocks> parser;
	
	@Before
	public void setUp() {
		reader = new StocksProvider(parser);
	}
	
	@Test
	public void isReadable() {
		assertTrue(reader.isReadable(Stocks.class, null, null, null));
	}
	
	@Test
	public void readFrom() throws Exception {
		reader.readFrom(null, null, null, new MediaType(), null, mock(InputStream.class));
		verify(parser).parse(any(Reader.class));
	}

}
