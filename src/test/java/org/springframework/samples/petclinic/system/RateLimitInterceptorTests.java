/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.system;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Test for {@link RateLimitInterceptor} configuration.
 *
 * @author Copilot
 */
@SpringBootTest
class RateLimitInterceptorTests {

	@Autowired
	private RateLimitInterceptor rateLimitInterceptor;

	@Test
	void testMaxRequestsConfigurationIsSetTo10() {
		// Use reflection to access the private field
		Integer maxRequests = (Integer) ReflectionTestUtils.getField(rateLimitInterceptor, "maxRequests");
		assertThat(maxRequests).isEqualTo(10);
	}

	@Test
	void testRateLimitingIsEnabled() {
		// Use reflection to access the private field
		Boolean rateLimitEnabled = (Boolean) ReflectionTestUtils.getField(rateLimitInterceptor, "rateLimitEnabled");
		assertThat(rateLimitEnabled).isTrue();
	}

	@Test
	void testWindowSizeIsConfigured() {
		// Use reflection to access the private field
		Integer windowSizeMinutes = (Integer) ReflectionTestUtils.getField(rateLimitInterceptor, "windowSizeMinutes");
		assertThat(windowSizeMinutes).isEqualTo(1);
	}

}