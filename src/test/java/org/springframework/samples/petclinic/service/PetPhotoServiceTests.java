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
package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.samples.petclinic.model.Pet;

/**
 * Unit tests for {@link PetPhotoService}.
 *
 * @author Petclinic Team
 */
@ExtendWith(MockitoExtension.class)
class PetPhotoServiceTests {

    @Mock
    private ClinicService clinicService;

    @Mock
    private jakarta.servlet.ServletContext servletContext;

    private PetPhotoService petPhotoService;

    @BeforeEach
    void setUp() {
        petPhotoService = new PetPhotoServiceImpl(clinicService, servletContext);
    }

    @Test
    void shouldValidateValidJpegFile() {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());
        assertThat(petPhotoService.isValidFile(file)).isTrue();
    }

    @Test
    void shouldValidateValidPngFile() {
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png", "test data".getBytes());
        assertThat(petPhotoService.isValidFile(file)).isTrue();
    }

    @Test
    void shouldValidateValidGifFile() {
        MockMultipartFile file = new MockMultipartFile("file", "test.gif", "image/gif", "test data".getBytes());
        assertThat(petPhotoService.isValidFile(file)).isTrue();
    }

    @Test
    void shouldValidateValidWebpFile() {
        MockMultipartFile file = new MockMultipartFile("file", "test.webp", "image/webp", "test data".getBytes());
        assertThat(petPhotoService.isValidFile(file)).isTrue();
    }

    @Test
    void shouldRejectInvalidFileType() {
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "test data".getBytes());
        assertThat(petPhotoService.isValidFile(file)).isFalse();
    }

    @Test
    void shouldRejectNullFile() {
        assertThat(petPhotoService.isValidFile(null)).isFalse();
    }

    @Test
    void shouldRejectEmptyFile() {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[0]);
        assertThat(petPhotoService.isValidFile(file)).isFalse();
    }

    @Test
    void shouldRejectFileWithNullContentType() {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", null, "test data".getBytes());
        assertThat(petPhotoService.isValidFile(file)).isFalse();
    }

    @Test
    void shouldRejectFileExceedingMaxSize() {
        // Create a file larger than 5MB (MAX_FILE_SIZE)
        byte[] largeData = new byte[(int) (PetPhotoService.MAX_FILE_SIZE + 1)];
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", largeData);
        assertThat(petPhotoService.isValidFile(file)).isFalse();
    }

    @Test
    void shouldGetPhotoUrlWhenExists() {
        Pet pet = new Pet();
        pet.setId(1);
        pet.setPhotoUrl("/resources/images/pets/pet_1.jpg");

        when(clinicService.findPetById(1)).thenReturn(pet);

        Optional<String> photoUrl = petPhotoService.getPhotoUrl(1);

        assertThat(photoUrl).isPresent();
        assertThat(photoUrl.get()).isEqualTo("/resources/images/pets/pet_1.jpg");
    }

    @Test
    void shouldReturnEmptyWhenNoPhotoUrl() {
        Pet pet = new Pet();
        pet.setId(1);
        pet.setPhotoUrl(null);

        when(clinicService.findPetById(1)).thenReturn(pet);

        Optional<String> photoUrl = petPhotoService.getPhotoUrl(1);

        assertThat(photoUrl).isEmpty();
    }

    @Test
    void shouldReturnEmptyWhenPetNotFound() {
        when(clinicService.findPetById(1)).thenReturn(null);

        Optional<String> photoUrl = petPhotoService.getPhotoUrl(1);

        assertThat(photoUrl).isEmpty();
    }

    @Test
    void shouldReturnEmptyForEmptyPhotoUrlString() {
        Pet pet = new Pet();
        pet.setId(1);
        pet.setPhotoUrl("");

        when(clinicService.findPetById(1)).thenReturn(pet);

        Optional<String> photoUrl = petPhotoService.getPhotoUrl(1);

        assertThat(photoUrl).isEmpty();
    }

    @Test
    void shouldReturnFalseWhenDeletingNonExistentPhoto() {
        Pet pet = new Pet();
        pet.setId(1);
        pet.setPhotoUrl(null);

        when(clinicService.findPetById(1)).thenReturn(pet);

        boolean deleted = petPhotoService.deletePhoto(1);

        assertThat(deleted).isFalse();
        verify(clinicService, never()).savePet(any());
    }

    @Test
    void shouldReturnFalseWhenPetNotFoundForDelete() {
        when(clinicService.findPetById(1)).thenReturn(null);

        boolean deleted = petPhotoService.deletePhoto(1);

        assertThat(deleted).isFalse();
    }

    @Test
    void shouldReturnFalseForEmptyPhotoUrlString() {
        Pet pet = new Pet();
        pet.setId(1);
        pet.setPhotoUrl("");

        when(clinicService.findPetById(1)).thenReturn(pet);

        boolean deleted = petPhotoService.deletePhoto(1);

        assertThat(deleted).isFalse();
    }

}