/**
 * Copyright (C) 2012 Edge Dalmacio <edgar.dalmacio@gmail.com>
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
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.phisix.api.model.Price;
import com.googlecode.phisix.api.model.Stock;
import com.googlecode.phisix.api.model.Stocks;

/**
 * 
 * @author Edge Dalmacio
 *
 */
public class PhisixParser implements Parser<Reader, Stocks> {

	private final static Logger logger = LoggerFactory.getLogger(PhisixParser.class);
	private final static String DEFAULT_DATE_PATTERN = "E MMM dd, yyyy hh:mm:ss a";
	private String datePattern;
	
	public PhisixParser() {
		this(DEFAULT_DATE_PATTERN);
	}
	
	public PhisixParser(String datePattern) {
		this.datePattern = datePattern;
	}
	
	@Override
	public Stocks parse(Reader source) throws Exception {
		Stocks stocks = new Stocks();
		
		Scanner scanner = new Scanner(source);
		scanner.useDelimiter(";");
		
		int i = 0; boolean isFirstLine = true; Stock stock = null;
		while(scanner.hasNext()) {
			String token = scanner.next().trim();
			if (logger.isTraceEnabled()) {
				logger.trace("token = " + token);
			}
			switch (i++) {
			case 0:
				if (!isFirstLine) {
					stock = new Stock();
					stock.setSymbol(token);
				}
				break;
			case 1:
				if (!isFirstLine && !".".equals(token)) {
					String[] strings = token.replaceAll(",", "").split("\\s+");
					stock.setPrice(new Price());
					stock.getPrice().setCurrency(Currency.getInstance("PHP"));
					stock.getPrice().setAmount(new BigDecimal(strings[0].trim()));
					stock.setPercentChange(new BigDecimal(strings[1].trim()));
				}
				break;
			case 2:
				if (!isFirstLine && !".".equals(token)) {
					stock.setName(token);
				}
				break;
			case 3:
				if (!isFirstLine) {
					if (token.contains(".")) {
						token = token.substring(0, token.indexOf('.'));
					}
					stock.setVolume(Long.parseLong(token.replace(",", "")));
					stocks.getStocks().add(stock);
				} else if (isFirstLine) {
					DateFormat dateFormat = new SimpleDateFormat(datePattern);
					Date date = dateFormat.parse(token);
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(date);
					stocks.setAsOf(calendar);
					isFirstLine = false;
				}
				i = 0;
				break;
			}
		}
		return stocks;
	}

}
