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

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Slack integration.
 *
 * @author Copilot
 */
@ConfigurationProperties(prefix = "slack")
public class SlackProperties {

	/**
	 * Whether Slack integration is enabled.
	 */
	private boolean enabled = false;

	/**
	 * Slack webhook URL for sending notifications.
	 */
	private String webhookUrl;

	/**
	 * Default channel for notifications (e.g., "copilot-review").
	 */
	private String defaultChannel = "copilot-review";

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getWebhookUrl() {
		return webhookUrl;
	}

	public void setWebhookUrl(String webhookUrl) {
		this.webhookUrl = webhookUrl;
	}

	public String getDefaultChannel() {
		return defaultChannel;
	}

	public void setDefaultChannel(String defaultChannel) {
		this.defaultChannel = defaultChannel;
	}

}