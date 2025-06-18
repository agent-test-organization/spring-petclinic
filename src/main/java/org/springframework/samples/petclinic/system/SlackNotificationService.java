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

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Service for sending notifications to Slack.
 *
 * @author Copilot
 */
@Service
public class SlackNotificationService {

	private static final Logger logger = LoggerFactory.getLogger(SlackNotificationService.class);

	private final SlackProperties slackProperties;

	private final WebClient webClient;

	public SlackNotificationService(SlackProperties slackProperties) {
		this.slackProperties = slackProperties;
		this.webClient = WebClient.builder()
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.build();
	}

	/**
	 * Send notification when copilot finishes creating a high-level plan.
	 * @param prUrl the URL of the PR to review
	 */
	public void notifyPlanReady(String prUrl) {
		if (!slackProperties.isEnabled()) {
			logger.debug("Slack integration is disabled, skipping plan ready notification");
			return;
		}

		String message = String.format("🤖 Copilot has finished creating a high-level plan! Please review: %s", prUrl);
		sendNotification(message);
	}

	/**
	 * Send notification when a PR is ready for review.
	 * @param prUrl the URL of the PR to review
	 */
	public void notifyPrReady(String prUrl) {
		if (!slackProperties.isEnabled()) {
			logger.debug("Slack integration is disabled, skipping PR ready notification");
			return;
		}

		String message = String.format("📋 PR is ready for review! Please check: %s", prUrl);
		sendNotification(message);
	}

	private void sendNotification(String message) {
		if (!StringUtils.hasText(slackProperties.getWebhookUrl())) {
			logger.warn("Slack webhook URL is not configured, cannot send notification");
			return;
		}

		try {
			Map<String, Object> payload = Map.of("channel", "#" + slackProperties.getDefaultChannel(), "text", message);

			webClient.post()
				.uri(slackProperties.getWebhookUrl())
				.bodyValue(payload)
				.retrieve()
				.bodyToMono(String.class)
				.doOnError(error -> logger.error("Failed to send Slack notification", error))
				.doOnSuccess(response -> logger.info("Slack notification sent successfully"))
				.subscribe();

		}
		catch (Exception e) {
			logger.error("Error sending Slack notification: {}", e.getMessage(), e);
		}
	}

}