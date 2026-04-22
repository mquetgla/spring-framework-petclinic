/*
 * Copyright 2002-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.web;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.service.PetPhotoService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Unit tests for {@link PetPhotoController}.
 *
 * @author Petclinic Team
 */
@ExtendWith(MockitoExtension.class)
class PetPhotoControllerTests {

    private static final int TEST_PET_ID = 1;

    private PetPhotoController petPhotoController;

    private ClinicService clinicService;

    private PetPhotoService petPhotoService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() throws Exception {
        clinicService = mock(ClinicService.class);
        petPhotoService = mock(PetPhotoService.class);
        petPhotoController = new PetPhotoController(petPhotoService, clinicService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(petPhotoController).build();
    }

    @Test
    void shouldReturn404WhenPetNotFoundForGet() throws Exception {
        when(clinicService.findPetById(TEST_PET_ID)).thenReturn(null);

        mockMvc.perform(get("/pet/photo/{petId}", TEST_PET_ID)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenPhotoNotFound() throws Exception {
        Pet pet = new Pet();
        pet.setId(TEST_PET_ID);
        pet.setName("Test Pet");

        when(clinicService.findPetById(TEST_PET_ID)).thenReturn(pet);
        when(petPhotoService.getPhotoUrl(TEST_PET_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get("/pet/photo/{petId}", TEST_PET_ID)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnPhotoUrlWhenExists() throws Exception {
        Pet pet = new Pet();
        pet.setId(TEST_PET_ID);
        pet.setName("Test Pet");

        when(clinicService.findPetById(TEST_PET_ID)).thenReturn(pet);
        when(petPhotoService.getPhotoUrl(TEST_PET_ID)).thenReturn(Optional.of("/resources/images/pets/pet_1.jpg"));

        mockMvc.perform(get("/pet/photo/{petId}", TEST_PET_ID)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.petId").value(TEST_PET_ID))
            .andExpect(jsonPath("$.photoUrl").value("/resources/images/pets/pet_1.jpg"));
    }

    @Test
    void shouldReturn404WhenPetNotFoundForUpload() throws Exception {
        when(clinicService.findPetById(TEST_PET_ID)).thenReturn(null);

        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());

        mockMvc.perform(multipart("/pet/photo/{petId}", TEST_PET_ID)
            .file(file)
            .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenNoFileProvided() throws Exception {
        Pet pet = new Pet();
        pet.setId(TEST_PET_ID);
        pet.setName("Test Pet");

        when(clinicService.findPetById(TEST_PET_ID)).thenReturn(pet);

        MockMultipartFile file = new MockMultipartFile("file", "", "image/jpeg", new byte[0]);

        mockMvc.perform(multipart("/pet/photo/{petId}", TEST_PET_ID)
            .file(file)
            .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400ForInvalidFile() throws Exception {
        Pet pet = new Pet();
        pet.setId(TEST_PET_ID);
        pet.setName("Test Pet");

        when(clinicService.findPetById(TEST_PET_ID)).thenReturn(pet);
        when(petPhotoService.isValidFile(any())).thenReturn(false);

        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "test data".getBytes());

        mockMvc.perform(multipart("/pet/photo/{petId}", TEST_PET_ID)
            .file(file)
            .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn404WhenPetNotFoundForDelete() throws Exception {
        when(clinicService.findPetById(TEST_PET_ID)).thenReturn(null);

        mockMvc.perform(delete("/pet/photo/{petId}", TEST_PET_ID))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeletePhotoSuccessfully() throws Exception {
        Pet pet = new Pet();
        pet.setId(TEST_PET_ID);
        pet.setName("Test Pet");

        when(clinicService.findPetById(TEST_PET_ID)).thenReturn(pet);
        when(petPhotoService.deletePhoto(TEST_PET_ID)).thenReturn(true);

        mockMvc.perform(delete("/pet/photo/{petId}", TEST_PET_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.petId").value(TEST_PET_ID))
            .andExpect(jsonPath("$.deleted").value(true));
    }

}