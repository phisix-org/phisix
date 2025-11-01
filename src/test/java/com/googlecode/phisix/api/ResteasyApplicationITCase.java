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
package com.googlecode.phisix.api;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.util.Set;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import org.junit.Test;

import com.googlecode.phisix.api.ext.StocksMessageBodyWriter;
import com.googlecode.phisix.api.ext.StocksProvider;
import com.googlecode.phisix.api.resource.StocksResource;

/**
 * Integration test for ResteasyApplication auto-discovery
 * 
 * Verifies that:
 * - ResteasyApplication has @ApplicationPath annotation
 * - getClasses() returns empty set to enable auto-discovery
 * - Resources and providers are discoverable via reflection
 * 
 * @author Edge Dalmacio
 */
public class ResteasyApplicationITCase {

	@Test
	public void testResteasyApplicationHasApplicationPathAnnotation() {
		// Verify that ResteasyApplication is annotated with @ApplicationPath
		Class<?> clazz = ResteasyApplication.class;
		ApplicationPath annotation = clazz.getAnnotation(ApplicationPath.class);
		assertNotNull("ResteasyApplication should have @ApplicationPath annotation", annotation);
		assertEquals("ApplicationPath should be '/'", "/", annotation.value());
	}

	@Test
	public void testGetClassesReturnsEmptySet() {
		// Verify that getClasses() returns empty set for auto-discovery
		ResteasyApplication app = new ResteasyApplication();
		Set<Class<?>> classes = app.getClasses();
		assertNotNull("getClasses() should not return null", classes);
		assertTrue("getClasses() should return empty set for auto-discovery",
			classes.isEmpty());
	}

	@Test
	public void testResteasyApplicationExtendsApplication() {
		// Verify that ResteasyApplication extends jakarta.ws.rs.core.Application
		ResteasyApplication app = new ResteasyApplication();
		assertTrue("ResteasyApplication should be instance of Application",
			app instanceof Application);
	}

	@Test
	public void testResourcesAreDiscoverable() {
		// Verify that StocksResource has @Path annotation and is discoverable
		Class<?> stocksResourceClass = StocksResource.class;
		Annotation[] annotations = stocksResourceClass.getAnnotations();
		
		boolean hasPathAnnotation = false;
		for (Annotation annotation : annotations) {
			if (annotation.annotationType().getSimpleName().equals("Path")) {
				hasPathAnnotation = true;
				break;
			}
		}
		assertTrue("StocksResource should have @Path annotation for auto-discovery",
			hasPathAnnotation);
	}

	@Test
	public void testProvidersAreDiscoverable() {
		// Verify that providers have @Provider annotation and are discoverable
		
		// Check StocksMessageBodyWriter
		Class<?> writerClass = StocksMessageBodyWriter.class;
		Annotation[] writerAnnotations = writerClass.getAnnotations();
		boolean writerHasProvider = false;
		for (Annotation annotation : writerAnnotations) {
			if (annotation.annotationType().getSimpleName().equals("Provider")) {
				writerHasProvider = true;
				break;
			}
		}
		assertTrue("StocksMessageBodyWriter should have @Provider annotation for auto-discovery",
			writerHasProvider);
		
		// Check StocksProvider
		Class<?> providerClass = StocksProvider.class;
		Annotation[] providerAnnotations = providerClass.getAnnotations();
		boolean providerHasProvider = false;
		for (Annotation annotation : providerAnnotations) {
			if (annotation.annotationType().getSimpleName().equals("Provider")) {
				providerHasProvider = true;
				break;
			}
		}
		assertTrue("StocksProvider should have @Provider annotation for auto-discovery",
			providerHasProvider);
	}

	@Test
	public void testApplicationPathValueIsCorrect() {
		// Verify that the ApplicationPath value matches the expected root path
		ApplicationPath annotation = ResteasyApplication.class.getAnnotation(ApplicationPath.class);
		assertNotNull("ApplicationPath annotation should exist", annotation);
		String path = annotation.value();
		assertEquals("ApplicationPath should be root path '/'", "/", path);
	}

	@Test
	public void testEmptySetEnablesAutoDiscovery() {
		// Verify that returning empty set from getClasses() enables auto-discovery
		// This is the RESTEasy convention: empty set means "scan for @Path and @Provider"
		ResteasyApplication app = new ResteasyApplication();
		Set<Class<?>> classes = app.getClasses();
		
		// Empty set signals RESTEasy to use annotation scanning
		assertTrue("Empty set should be returned to enable auto-discovery", classes.isEmpty());
		assertEquals("Size should be 0", 0, classes.size());
	}
}

