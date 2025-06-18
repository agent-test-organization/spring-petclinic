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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatNoException;

/**
 * Unit tests for {@link SlackNotificationService}
 *
 * @author Copilot
 */
@ExtendWith(MockitoExtension.class)
class SlackNotificationServiceTests {

	private SlackNotificationService slackNotificationService;

	private SlackProperties slackProperties;

	@BeforeEach
	void setUp() {
		slackProperties = new SlackProperties();
		slackNotificationService = new SlackNotificationService(slackProperties);
	}

	@Test
	void testNotifyPlanReadyWhenDisabled() {
		// Given
		slackProperties.setEnabled(false);

		// When/Then - should not throw exception
		assertThatNoException()
			.isThrownBy(() -> slackNotificationService.notifyPlanReady("https://github.com/test/pr/1"));
	}

	@Test
	void testNotifyPrReadyWhenDisabled() {
		// Given
		slackProperties.setEnabled(false);

		// When/Then - should not throw exception
		assertThatNoException()
			.isThrownBy(() -> slackNotificationService.notifyPrReady("https://github.com/test/pr/1"));
	}

	@Test
	void testNotifyPlanReadyWhenEnabledButNoWebhookUrl() {
		// Given
		slackProperties.setEnabled(true);
		slackProperties.setWebhookUrl(null);

		// When/Then - should not throw exception
		assertThatNoException()
			.isThrownBy(() -> slackNotificationService.notifyPlanReady("https://github.com/test/pr/1"));
	}

	@Test
	void testNotifyPrReadyWhenEnabledButNoWebhookUrl() {
		// Given
		slackProperties.setEnabled(true);
		slackProperties.setWebhookUrl(null);

		// When/Then - should not throw exception
		assertThatNoException()
			.isThrownBy(() -> slackNotificationService.notifyPrReady("https://github.com/test/pr/1"));
	}

}