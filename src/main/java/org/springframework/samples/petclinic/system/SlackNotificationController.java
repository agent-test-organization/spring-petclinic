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

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for Slack notifications.
 *
 * @author Copilot
 */
@RestController
@RequestMapping("/api/notifications")
public class SlackNotificationController {

	private final SlackNotificationService slackNotificationService;

	public SlackNotificationController(SlackNotificationService slackNotificationService) {
		this.slackNotificationService = slackNotificationService;
	}

	/**
	 * Endpoint to notify when copilot plan is ready for review.
	 * @param request the notification request containing PR URL
	 * @return response indicating success
	 */
	@PostMapping("/plan-ready")
	public ResponseEntity<String> notifyPlanReady(@RequestBody NotificationRequest request) {
		slackNotificationService.notifyPlanReady(request.getPrUrl());
		return ResponseEntity.ok("Plan ready notification sent");
	}

	/**
	 * Endpoint to notify when PR is ready for review.
	 * @param request the notification request containing PR URL
	 * @return response indicating success
	 */
	@PostMapping("/pr-ready")
	public ResponseEntity<String> notifyPrReady(@RequestBody NotificationRequest request) {
		slackNotificationService.notifyPrReady(request.getPrUrl());
		return ResponseEntity.ok("PR ready notification sent");
	}

	/**
	 * Request object for notification endpoints.
	 */
	public static class NotificationRequest {

		private String prUrl;

		public String getPrUrl() {
			return prUrl;
		}

		public void setPrUrl(String prUrl) {
			this.prUrl = prUrl;
		}

	}

}