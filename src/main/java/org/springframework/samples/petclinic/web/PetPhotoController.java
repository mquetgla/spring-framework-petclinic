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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.service.PetPhotoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing pet photos.
 *
 * @author Petclinic Team
 */
@Tag(name = "Pet Photo Management", description = "API for uploading, retrieving and deleting pet photos")
@Controller
@RequestMapping("/pet/photo")
public class PetPhotoController {

    private final PetPhotoService petPhotoService;
    private final ClinicService clinicService;

    public PetPhotoController(PetPhotoService petPhotoService, ClinicService clinicService) {
        this.petPhotoService = petPhotoService;
        this.clinicService = clinicService;
    }

    /**
     * Validates that the pet belongs to the owner for security purposes.
     *
     * @param pet the pet to validate
     * @param ownerId the owner ID to check
     * @return true if the pet belongs to the owner
     */
    private boolean isPetOwnedBy(Pet pet, int ownerId) {
        if (pet == null || pet.getOwner() == null) {
            return false;
        }
        return pet.getOwner().getId() == ownerId;
    }

    /**
     * Get photo URL for a pet.
     *
     * @param petId the pet ID
     * @param ownerId the owner ID (optional, for ownership validation)
     * @return the photo URL or 404 if not found
     */
    @Operation(summary = "Get pet photo URL", description = "Retrieves the photo URL for a specific pet")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Photo URL found",
            content = @Content(schema = @Schema(example = "{\"petId\": 1, \"photoUrl\": \"/resources/images/pets/pet_1.jpg\"}"))),
        @ApiResponse(responseCode = "404", description = "Pet or photo not found"),
        @ApiResponse(responseCode = "403", description = "Pet does not belong to the owner")
    })
    @GetMapping(value = "/{petId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getPhotoUrl(
            @Parameter(description = "ID of the pet") @PathVariable int petId,
            @Parameter(description = "ID of the owner (optional)") @RequestParam(required = false) Integer ownerId) {
        Pet pet = clinicService.findPetById(petId);
        if (pet == null) {
            return ResponseEntity.notFound().build();
        }

        // Validate ownership if ownerId provided
        if (ownerId != null && !isPetOwnedBy(pet, ownerId)) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Pet does not belong to the owner");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }

        Optional<String> photoUrl = petPhotoService.getPhotoUrl(petId);
        if (photoUrl.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("petId", petId);
        response.put("photoUrl", photoUrl.get());
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve the actual image file for a pet.
     *
     * @param petId the pet ID
     * @param ownerId the owner ID (optional, for ownership validation)
     * @return the image file or 404 if not found
     */
    @Operation(summary = "Get pet photo image", description = "Retrieves the actual photo image for a pet")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Photo image found", content = @Content(mediaType = "image/*")),
        @ApiResponse(responseCode = "404", description = "Pet or photo not found"),
        @ApiResponse(responseCode = "403", description = "Pet does not belong to the owner")
    })
    @GetMapping(value = "/{petId}/image", produces = {"image/*", "application/octet-stream"})
    @ResponseBody
    public ResponseEntity<byte[]> getPhotoImage(
            @Parameter(description = "ID of the pet") @PathVariable int petId,
            @Parameter(description = "ID of the owner (optional)") @RequestParam(required = false) Integer ownerId) {
        Pet pet = clinicService.findPetById(petId);
        if (pet == null) {
            return ResponseEntity.notFound().build();
        }

        // Validate ownership if ownerId provided
        if (ownerId != null && !isPetOwnedBy(pet, ownerId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<String> photoUrl = petPhotoService.getPhotoUrl(petId);
        if (photoUrl.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            // Load the image file from the photo URL
            String url = photoUrl.get();
            String filename = url.substring(url.lastIndexOf("/") + 1);
            String photosDir = "resources/images/pets";
            Path imagePath = Paths.get(photosDir).resolve(filename);

            if (Files.exists(imagePath)) {
                byte[] imageData = Files.readAllBytes(imagePath);
                String contentType = Files.probeContentType(imagePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(imageData);
            }
        } catch (IOException e) {
            // Log error and return 500
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Upload a photo for a pet.
     *
     * @param petId the pet ID
     * @param ownerId the owner ID (optional, for ownership validation)
     * @param file the uploaded image file
     * @return success response or error
     */
    @Operation(summary = "Upload pet photo", description = "Uploads a photo for a specific pet")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Photo uploaded successfully",
            content = @Content(schema = @Schema(example = "{\"petId\": 1, \"photoUrl\": \"/resources/images/pets/pet_1.jpg\", \"message\": \"Photo uploaded successfully\"}"))),
        @ApiResponse(responseCode = "400", description = "Invalid file or file too large"),
        @ApiResponse(responseCode = "404", description = "Pet not found"),
        @ApiResponse(responseCode = "403", description = "Pet does not belong to the owner")
    })
    @PostMapping(value = "/{petId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> uploadPhoto(
            @Parameter(description = "ID of the pet") @PathVariable int petId,
            @Parameter(description = "ID of the owner (optional)") @RequestParam(required = false) Integer ownerId,
            @Parameter(description = "Image file to upload") @RequestParam("file") MultipartFile file) {
        // Validate pet exists
        Pet pet = clinicService.findPetById(petId);
        if (pet == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Pet not found with id: " + petId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        // Validate ownership if ownerId provided
        if (ownerId != null && !isPetOwnedBy(pet, ownerId)) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Pet does not belong to the owner");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }

        // Validate file
        if (file == null || file.isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "No file provided");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        if (!petPhotoService.isValidFile(file)) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Invalid file: must be an image (JPEG, PNG, GIF, or WebP) and less than 5MB");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        try {
            String photoUrl = petPhotoService.uploadPhoto(petId, file);

            Map<String, Object> response = new HashMap<>();
            response.put("petId", petId);
            response.put("photoUrl", photoUrl);
            response.put("message", "Photo uploaded successfully");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to upload photo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Delete photo for a pet.
     *
     * @param petId the pet ID
     * @param ownerId the owner ID (optional, for ownership validation)
     * @return success response or error
     */
    @Operation(summary = "Delete pet photo", description = "Deletes the photo for a specific pet")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Photo deleted successfully"),
        @ApiResponse(responseCode = "200", description = "No photo to delete"),
        @ApiResponse(responseCode = "404", description = "Pet not found"),
        @ApiResponse(responseCode = "403", description = "Pet does not belong to the owner")
    })
    @DeleteMapping("/{petId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deletePhoto(
            @Parameter(description = "ID of the pet") @PathVariable int petId,
            @Parameter(description = "ID of the owner (optional)") @RequestParam(required = false) Integer ownerId) {
        // Validate pet exists
        Pet pet = clinicService.findPetById(petId);
        if (pet == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Pet not found with id: " + petId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        // Validate ownership if ownerId provided
        if (ownerId != null && !isPetOwnedBy(pet, ownerId)) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Pet does not belong to the owner");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }

        boolean deleted = petPhotoService.deletePhoto(petId);

        // Return 200 with response indicating deletion result
        Map<String, Object> response = new HashMap<>();
        response.put("petId", petId);
        response.put("deleted", deleted);
        if (!deleted) {
            response.put("message", "No photo to delete");
        }
        return ResponseEntity.ok(response);
    }

}