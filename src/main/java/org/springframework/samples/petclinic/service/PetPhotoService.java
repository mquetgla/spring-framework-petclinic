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

import org.springframework.samples.petclinic.model.Pet;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Service for managing pet photos.
 *
 * @author Petclinic Team
 */
public interface PetPhotoService {

    /**
     * Maximum file size for pet photos (5MB).
     */
    long MAX_FILE_SIZE = 5 * 1024 * 1024;

    /**
     * Allowed image content types.
     */
    String[] ALLOWED_CONTENT_TYPES = {"image/jpeg", "image/png", "image/gif", "image/webp"};

    /**
     * Uploads a photo for a pet.
     *
     * @param petId the ID of the pet
     * @param file the uploaded file
     * @return the photo URL
     * @throws IOException if file processing fails
     */
    String uploadPhoto(int petId, MultipartFile file) throws IOException;

    /**
     * Gets the photo URL for a pet.
     *
     * @param petId the ID of the pet
     * @return optional containing the photo URL if exists
     */
    Optional<String> getPhotoUrl(int petId);

    /**
     * Deletes the photo for a pet.
     *
     * @param petId the ID of the pet
     * @return true if photo was deleted
     */
    boolean deletePhoto(int petId);

    /**
     * Validates the uploaded file.
     *
     * @param file the file to validate
     * @return true if file is valid
     */
    boolean isValidFile(MultipartFile file);

}