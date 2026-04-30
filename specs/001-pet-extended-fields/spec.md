# Feature Specification: Pet Extended Fields

**Feature Branch**: `001-pet-extended-fields`
**Created**: 2026-04-30
**Status**: Draft
**Input**: Unified specification covering 7 new fields/capabilities for the Pet entity

## User Scenarios & Testing

### User Story 1 - Add Photo to Pet (Priority: P2)

A pet owner or clinic staff uploads a photo for a pet so that the animal can be visually identified in the system.

**Why this priority**: Visual identification aids recognition but is not critical for clinical workflows.

**Independent Test**: Upload a photo for a pet and verify it displays on the pet's profile page.

**Acceptance Scenarios**:

1. **Given** a pet exists without a photo, **When** a user uploads a valid image, **Then** the photo is stored and displayed on the pet's detail view.
2. **Given** a pet has a photo, **When** a user uploads a new image, **Then** the previous photo is replaced.
3. **Given** a user uploads an invalid file (e.g., .exe), **When** the system validates, **Then** the upload is rejected with an appropriate error message.

---

### User Story 2 - Register Microchip ID (Priority: P1)

Clinic staff registers the official microchip identification number for a pet to link it to national/international registries.

**Why this priority**: Legal/official identification is critical for pet traceability and compliance.

**Independent Test**: Enter a microchip ID for a pet, verify it is saved and that duplicate IDs across different pets are rejected.

**Acceptance Scenarios**:

1. **Given** a pet without a microchip ID, **When** staff enters a valid microchip number, **Then** it is saved and displayed on the pet profile.
2. **Given** a microchip ID already exists for another pet, **When** staff tries to assign the same ID, **Then** the system rejects it with a uniqueness error.
3. **Given** a pet has a microchip ID, **When** staff updates it, **Then** the new value replaces the old one (subject to uniqueness).

---

### User Story 3 - Record Color and Breed (Priority: P2)

Staff records the color and breed of a pet for identification and medical record purposes.

**Why this priority**: Useful for identification and breed-specific medical considerations.

**Independent Test**: Set color and breed for a pet and verify they display correctly.

**Acceptance Scenarios**:

1. **Given** a pet profile, **When** staff enters color and breed values, **Then** both fields are saved and visible on the pet detail page.
2. **Given** color or breed is left empty, **When** the form is submitted, **Then** the pet is saved successfully (fields are optional).

---

### User Story 4 - Mark Pet Active/Inactive (Priority: P1)

Staff marks a pet as inactive when it has been transferred, lost, or has passed away, so it no longer appears in active patient lists.

**Why this priority**: Directly impacts daily clinical workflow by filtering relevant patients.

**Independent Test**: Toggle active status and verify the pet is excluded from/included in active listings.

**Acceptance Scenarios**:

1. **Given** a pet is active, **When** staff marks it inactive, **Then** it no longer appears in default pet listings.
2. **Given** a pet is inactive, **When** staff reactivates it, **Then** it reappears in active listings.
3. **Given** a new pet is created, **Then** it defaults to active status.

---

### User Story 5 - Record Pet Weight (Priority: P2)

Staff records the current weight of a pet during a visit for health monitoring.

**Why this priority**: Weight is important for medication dosing and health tracking but is not blocking other workflows.

**Independent Test**: Enter a weight value for a pet and verify it is stored and displayed.

**Acceptance Scenarios**:

1. **Given** a pet profile, **When** staff enters a decimal weight value, **Then** it is saved and displayed.
2. **Given** a negative or zero weight is entered, **When** the form is submitted, **Then** validation rejects it.

---

### User Story 6 - Add Owner Notes (Priority: P3)

Owners or staff add free-text notes to a pet's profile for observations that don't fit structured fields.

**Why this priority**: Convenience feature that enhances records but is not essential.

**Independent Test**: Add notes text to a pet and verify it persists and displays.

**Acceptance Scenarios**:

1. **Given** a pet profile, **When** a user enters text in the notes field, **Then** it is saved and viewable.
2. **Given** notes already exist, **When** a user edits them, **Then** the updated text is saved.

---

### User Story 7 - Assign Gender/Sex (Priority: P1)

Staff assigns a gender (Male, Female, Unknown) to a pet for medical and identification purposes.

**Why this priority**: Gender is medically relevant (spay/neuter, breed-specific conditions) and a basic identification attribute.

**Independent Test**: Select a gender for a pet and verify it persists.

**Acceptance Scenarios**:

1. **Given** a pet profile, **When** staff selects a gender value, **Then** it is saved and displayed.
2. **Given** no gender is selected, **When** the pet is saved, **Then** it defaults to "Unknown".

---

### Edge Cases

- What happens when a photo exceeds maximum allowed size? → System rejects with a clear file-size error.
- What happens when weight is entered with excessive decimal precision? → System rounds to 2 decimal places.
- What happens when a pet is inactive and a visit is attempted? → System warns but does not block (inactive is informational).
- What happens when the microchip ID format is invalid? → System accepts any alphanumeric string up to 25 characters (no strict format validation beyond length and uniqueness).

## Requirements

### Functional Requirements

- **FR-001**: System MUST allow uploading and storing a photo for each pet (one photo per pet).
- **FR-002**: System MUST store a microchip ID field per pet with a uniqueness constraint across all pets.
- **FR-003**: System MUST provide color and breed as optional free-text fields on the pet profile.
- **FR-004**: System MUST support an active/inactive status for each pet, defaulting to active on creation.
- **FR-005**: System MUST allow recording a weight (positive decimal value) for each pet.
- **FR-006**: System MUST provide a free-text notes field for each pet.
- **FR-007**: System MUST support assigning a gender (Male, Female, Unknown) to each pet, defaulting to Unknown.
- **FR-008**: System MUST display all new fields on the pet detail view and pet edit form.
- **FR-009**: System MUST validate microchip ID uniqueness at save time and display an error if violated.
- **FR-010**: System MUST validate that weight, if provided, is a positive number.
- **FR-011**: System MUST allow filtering or excluding inactive pets from default listings.

### Key Entities

- **Pet** (extended): Existing entity gaining new attributes — photoUrl/photo, microchipId, color, breed, active, weight, notes, gender.
- **Gender**: Enumeration with values MALE, FEMALE, UNKNOWN.

## Success Criteria

### Measurable Outcomes

- **SC-001**: All 7 new fields are visible and editable on the pet form within 2 interactions (navigate + edit).
- **SC-002**: Duplicate microchip IDs are rejected 100% of the time with a user-friendly message.
- **SC-003**: Inactive pets are excluded from default listings; users can opt-in to view them.
- **SC-004**: Photo upload completes in under 5 seconds for images up to 5 MB.
- **SC-005**: All existing functionality (visits, owners, pet types) continues to work without regression.
- **SC-006**: Gender defaults to "Unknown" for all pets without explicit assignment.

## Assumptions

- Photo storage uses a URL reference or binary storage — implementation will decide. Max file size assumed 5 MB.
- Microchip ID is a free-form alphanumeric string (max 25 characters). No specific country-format validation required.
- Weight is stored as a single current value (decimal, 2 decimal places). Historical weight tracking is out of scope for this specification and may be addressed in a future iteration.
- Color and breed are free-text (no predefined catalog). A catalog may be introduced later.
- "Inactive" is a soft state — no data is deleted. Inactive pets remain accessible via explicit filtering.
- Notes field has no maximum length restriction beyond what the database supports (assumed TEXT type).
- The system currently has a Pet entity with name, birthDate, type, and owner. All new fields are added to this existing entity.
