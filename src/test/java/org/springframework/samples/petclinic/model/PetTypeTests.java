package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple unit tests for the {@link PetType} class.
 */
class PetTypeTests {

    @Test
    void shouldSetAndGetName() {
        // Given
        PetType petType = new PetType();

        // When
        petType.setName("Dog");

        // Then
        assertEquals("Dog", petType.getName());
    }

    @Test
    void shouldSetAndGetId() {
        // Given
        PetType petType = new PetType();

        // When
        petType.setId(1);

        // Then
        assertEquals(1, petType.getId());
    }

    @Test
    void shouldBeNew_whenIdIsNull() {
        // Given
        PetType petType = new PetType();

        // When & Then
        assertTrue(petType.isNew());
    }

    @Test
    void shouldNotBeNew_whenIdIsSet() {
        // Given
        PetType petType = new PetType();
        petType.setId(1);

        // When & Then
        assertFalse(petType.isNew());
    }

    @Test
    void toString_shouldReturnName() {
        // Given
        PetType petType = new PetType();
        petType.setName("Cat");

        // When
        String result = petType.toString();

        // Then
        assertEquals("Cat", result);
    }

    @Test
    void shouldHandleNullName() {
        // Given
        PetType petType = new PetType();

        // When & Then
        assertNull(petType.getName());
        assertEquals("null", petType.toString());
    }
}
