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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for {@link SlackNotificationController}
 *
 * @author Copilot
 */
@WebMvcTest(SlackNotificationController.class)
class SlackNotificationControllerTests {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private SlackNotificationService slackNotificationService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testNotifyPlanReady() throws Exception {
		// Given
		SlackNotificationController.NotificationRequest request = new SlackNotificationController.NotificationRequest();
		request.setPrUrl("https://github.com/test/pr/1");

		// When/Then
		mvc.perform(post("/api/notifications/plan-ready").contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(content().string("Plan ready notification sent"));

		verify(slackNotificationService).notifyPlanReady("https://github.com/test/pr/1");
	}

	@Test
	void testNotifyPrReady() throws Exception {
		// Given
		SlackNotificationController.NotificationRequest request = new SlackNotificationController.NotificationRequest();
		request.setPrUrl("https://github.com/test/pr/2");

		// When/Then
		mvc.perform(post("/api/notifications/pr-ready").contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(content().string("PR ready notification sent"));

		verify(slackNotificationService).notifyPrReady("https://github.com/test/pr/2");
	}

}