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
package com.googlecode.phisix.api.repository;

import java.util.Date;

import com.googlecode.phisix.api.model.Stocks;

/**
 * CrudRepository for {@link Stocks}
 * 
 * @author Edge Dalmacio
 * 
 */
public interface StocksRepository {

	Stocks findAll();

	String findBySymbol(String symbol);
	
	Stocks findBySymbolAndTradingDate(String symbol, Date tradingDate);

	void save(Stocks stocks);

}
