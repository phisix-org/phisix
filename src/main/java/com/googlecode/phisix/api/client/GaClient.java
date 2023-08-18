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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * Proxy interface for making calls to Google Analytics
 * 
 * @author Edge Dalmacio
 *
 */
@Deprecated
public interface GaClient {

	@GET
	@Path("collect")
	void pageTracking(@QueryParam(value = "v") String version, 
			@QueryParam(value = "tid") String trackingId,
			@QueryParam(value = "cid") String clientId, 
			@QueryParam(value = "t") String hitType,
			@QueryParam(value = "dp") String page,
			@QueryParam(value = "ua") String userAgent);

	@GET
	@Path("collect")
	void eventTracking(@QueryParam(value = "v") String version, 
			@QueryParam(value = "tid") String trackingId,
			@QueryParam(value = "cid") String clientId, 
			@QueryParam(value = "t") String hitType,
			@QueryParam(value = "ec") String category,
			@QueryParam(value = "ea") String action,
			@QueryParam(value = "ua") String userAgent);

}
