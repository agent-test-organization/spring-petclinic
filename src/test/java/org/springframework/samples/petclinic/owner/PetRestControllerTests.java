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

package org.springframework.samples.petclinic.owner;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the {@link PetRestController}
 */
@WebMvcTest(PetRestController.class)
@DisabledInNativeImage
@DisabledInAotMode
class PetRestControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private OwnerRepository ownerRepository;

	private Owner createOwnerWithPets() {
		Owner owner = new Owner();
		owner.setFirstName("John");
		owner.setLastName("Doe");
		owner.setId(1);

		Pet pet1 = new Pet();
		pet1.setName("Buddy");
		pet1.setBirthDate(LocalDate.of(2020, 1, 1));
		// Don't set ID so pet.isNew() returns true

		PetType dogType = new PetType();
		dogType.setId(1);
		dogType.setName("dog");
		pet1.setType(dogType);

		Pet pet2 = new Pet();
		pet2.setName("Whiskers");
		pet2.setBirthDate(LocalDate.of(2021, 5, 15));
		// Don't set ID so pet.isNew() returns true

		PetType catType = new PetType();
		catType.setId(2);
		catType.setName("cat");
		pet2.setType(catType);

		owner.addPet(pet1);
		owner.addPet(pet2);

		return owner;
	}

	@BeforeEach
	void setup() {
		given(this.ownerRepository.findAll()).willReturn(Lists.newArrayList(createOwnerWithPets()));
	}

	@Test
	void testShowResourcesPetList() throws Exception {
		ResultActions actions = mockMvc.perform(get("/pets").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
		actions.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.petList[0].name").value("Buddy"))
			.andExpect(jsonPath("$.petList[1].name").value("Whiskers"));
	}

}