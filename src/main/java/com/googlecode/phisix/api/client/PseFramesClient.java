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
package com.googlecode.phisix.api.client;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.phisix.api.model.Stocks;
import com.googlecode.phisix.api.parser.GsonAwareParser;
import com.googlecode.phisix.api.parser.Parser;

/**
 * @author Edge Dalmacio
 */
public class PseFramesClient implements PseClient {

	private final static Logger LOGGER = LoggerFactory.getLogger(PseFramesClient.class);
	private final Parser<Reader,Stocks> parser;
	
	public PseFramesClient() {
		this.parser = new GsonAwareParser();
	}

	@Override
	public Stocks getSecuritiesAndIndicesForPublic() {
		try {
			Document doc = Jsoup.connect("https://frames.pse.com.ph").get();
			Elements newsHeadlines = doc.select("#JsonId");
			if (!newsHeadlines.isEmpty()) {
				String value = newsHeadlines.get(0).attr("value");
				return parser.parse(new StringReader(value));
			}
		} catch (IOException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		return null;
	}

	@Override
	public String findSecurityOrCompany(String method, boolean ajax, String body) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String companyInfo(String method, boolean ajax, String body) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String companyInfoHistoricalData(String method, boolean ajax, String body) {
		// TODO Auto-generated method stub
		return null;
	}

}
