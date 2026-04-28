package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple unit tests for the {@link Person} class.
 */
class PersonTests {

    @Test
    void shouldSetAndGetFirstName() {
        // Given
        Person person = new Person();
        
        // When
        person.setFirstName("John");
        
        // Then
        assertEquals("John", person.getFirstName());
    }

    @Test
    void shouldSetAndGetLastName() {
        // Given
        Person person = new Person();
        
        // When
        person.setLastName("Doe");
        
        // Then
        assertEquals("Doe", person.getLastName());
    }

    @Test
    void shouldHandleNullValues() {
        // Given
        Person person = new Person();
        
        // When & Then
        assertNull(person.getFirstName());
        assertNull(person.getLastName());
    }

    @Test
    void shouldUpdateFirstName() {
        // Given
        Person person = new Person();
        person.setFirstName("John");
        
        // When
        person.setFirstName("Jane");
        
        // Then
        assertEquals("Jane", person.getFirstName());
    }
}
