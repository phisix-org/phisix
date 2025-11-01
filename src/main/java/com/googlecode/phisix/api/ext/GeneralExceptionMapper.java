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

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maps {@link Exception} to HTTP 500 Internal Server Error
 * 
 * @author Edge Dalmacio
 */
@Provider
public class GeneralExceptionMapper implements ExceptionMapper<Exception> {

	private final static Logger LOGGER = LoggerFactory.getLogger(GeneralExceptionMapper.class);
	
	@Override
	public Response toResponse(Exception exception) {
		LOGGER.error("Unhandled exception: " + exception.getClass().getName(), exception);
		String errorMessage = exception.getMessage();
		if (errorMessage == null || errorMessage.isEmpty()) {
			errorMessage = exception.getClass().getName();
		}
		return Response.status(Status.INTERNAL_SERVER_ERROR)
				.entity("{\"error\":\"" + errorMessage.replace("\"", "\\\"") + "\"}")
				.type("application/json")
				.build();
	}

}
