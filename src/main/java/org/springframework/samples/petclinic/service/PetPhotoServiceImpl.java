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

import jakarta.servlet.ServletContext;
import org.springframework.lang.Nullable;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Optional;

/**
 * Implementation of PetPhotoService for managing pet photos.
 *
 * @author Petclinic Team
 */
@Service
public class PetPhotoServiceImpl implements PetPhotoService {

    private static final String PHOTOS_DIR = "resources/images/pets";

    private final ClinicService clinicService;
    private final ServletContext servletContext;

    public PetPhotoServiceImpl(ClinicService clinicService, @Nullable ServletContext servletContext) {
        this.clinicService = clinicService;
        this.servletContext = servletContext;
    }

    @Override
    public String uploadPhoto(int petId, MultipartFile file) throws IOException {
        if (!isValidFile(file)) {
            throw new IllegalArgumentException("Invalid file: file is empty or not an image");
        }

        // Get the pet
        Pet pet = clinicService.findPetById(petId);
        if (pet == null) {
            throw new IllegalArgumentException("Pet not found with id: " + petId);
        }

        // Delete existing photo if any
        deletePhoto(petId);

        // Determine the file extension
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);

        // Create the filename: pet_{id}.{extension}
        String filename = "pet_" + petId + extension;

        // Get the photos directory path
        Path photosDir = getPhotosDirectory();
        Files.createDirectories(photosDir);

        // Save the file
        Path filePath = photosDir.resolve(filename);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        // Update the pet's photo URL
        String photoUrl = "/resources/images/pets/" + filename;
        pet.setPhotoUrl(photoUrl);
        clinicService.savePet(pet);

        return photoUrl;
    }

    @Override
    public Optional<String> getPhotoUrl(int petId) {
        Pet pet = clinicService.findPetById(petId);
        if (pet != null && pet.getPhotoUrl() != null && !pet.getPhotoUrl().isEmpty()) {
            return Optional.of(pet.getPhotoUrl());
        }
        return Optional.empty();
    }

    @Override
    public boolean deletePhoto(int petId) {
        Pet pet = clinicService.findPetById(petId);
        if (pet == null || pet.getPhotoUrl() == null || pet.getPhotoUrl().isEmpty()) {
            return false;
        }

        // Extract filename from URL
        String photoUrl = pet.getPhotoUrl();
        String filename = photoUrl.substring(photoUrl.lastIndexOf("/") + 1);

        // Delete the file
        try {
            Path photosDir = getPhotosDirectory();
            Path filePath = photosDir.resolve(filename);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // Log the error but don't fail
        }

        // Clear the photo URL from pet
        pet.setPhotoUrl(null);
        clinicService.savePet(pet);

        return true;
    }

    @Override
    public boolean isValidFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        String contentType = file.getContentType();
        if (contentType == null) {
            return false;
        }

        // Check content type
        boolean validType = Arrays.asList(ALLOWED_CONTENT_TYPES).contains(contentType);
        if (!validType) {
            return false;
        }

        // Check file size
        return file.getSize() <= MAX_FILE_SIZE;
    }

    private Path getPhotosDirectory() {
        // Get the real path of the webapp resources directory
        String realPath = null;
        if (servletContext != null) {
            realPath = servletContext.getRealPath("/");
        }

        // Handle case where getRealPath returns null (e.g., running from JAR or WAR file)
        if (realPath == null || realPath.isEmpty()) {
            // Use system temp directory or configurable upload directory
            realPath = System.getProperty("java.io.tmpdir", "/tmp");
        }

        Path photosPath = Paths.get(realPath).resolve(PHOTOS_DIR);
        try {
            Files.createDirectories(photosPath);
        } catch (IOException e) {
            // Fallback to temp directory if unable to create in webapp directory
            photosPath = Paths.get(System.getProperty("java.io.tmpdir", "/tmp"), PHOTOS_DIR);
            try {
                Files.createDirectories(photosPath);
            } catch (IOException ex) {
                throw new RuntimeException("Unable to create photos directory", ex);
            }
        }

        return photosPath;
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return ".jpg";
        }

        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return ".jpg";
        }

        return filename.substring(lastDotIndex);
    }

}