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

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

/**
 * @author Edge Dalmacio
 */
@Provider
public class RobotsProvider implements Feature {

	private RobotsFilter robotsFilter;

	public RobotsProvider() {
		this(new RobotsFilter());
	}
	
	public RobotsProvider(RobotsFilter robotsFilter) {
		this.robotsFilter = robotsFilter;
	}
	
	@Override
	public boolean configure(FeatureContext context) {
		context.register(robotsFilter);
		return true;
	}

}
