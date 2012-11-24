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

import static org.junit.Assert.*;

import org.junit.Test;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;

public class StocksServletITCase {

	@Test
	public void getAllStocks() throws Exception {
		MockHttpServletRequest req = new MockHttpServletRequest();
		MockHttpServletResponse resp = new MockHttpServletResponse();
		new StocksServlet().doGet(req , resp );
		String content = resp.getOutputStreamContent();
		assertFalse(content.isEmpty());
		System.out.println(content);
	}
}
