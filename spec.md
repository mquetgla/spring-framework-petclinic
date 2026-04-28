# Feature Specification: Add Active Boolean Field to Pet

**Feature Branch**: `004-add-active-field-pet`
**Created**: Mon Apr 27 2026
**Status**: Draft
**Input**: User description: "Agregar campo active (boolean) a la entidad Pet para marcar mascotas fallecidas o transferidas."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Mark Pet as Inactive (Priority: P1)

As a clinic staff member, I want to mark a pet as inactive when it passes away or is transferred to another owner, so that the pet does not appear in active pet listings and reports.

**Why this priority**: This is a core operational need to maintain accurate records and avoid confusion in active pet management.

**Independent Test**: Verify that the active field is stored correctly in the database and that querying for active pets returns only those with active=true.

**Acceptance Scenarios**:

1. **Given** a pet record with active=true, **When** the user sets active=false and saves the pet, **Then** the pet's active field is persisted as false.
2. **Given** a pet record with active=false, **When** the user views the pet list filtered for active pets, **Then** the pet is not displayed in the list.

### User Story 2 - Default New Pets to Active (Priority: P2)

As a system, I want new pet records to default to active=true, so that staff do not need to manually set this flag for every new pet.

**Why this priority**: Ensures consistency and reduces data entry errors for the common case of new pets being active.

**Independent Test**: Create a new pet without specifying the active field and verify that it is saved with active=true.

**Acceptance Scenarios**:

1. **Given** a new pet entry without an explicit active value, **When** the pet is saved, **Then** the active field is set to true by default.

---

### Edge Cases

- What happens when the active field is left null during pet creation? The system should default to true.
- How does the system handle bulk updates of the active field? The system should allow updating multiple pets' active status via administrative tools.
- What if a user attempts to set active to a non-boolean value? The system should reject the input with a validation error.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST store an active boolean attribute for each Pet entity.
- **FR-002**: System MUST default the active attribute to true when creating a new Pet if no value is provided.
- **FR-003**: Users MUST be able to update the active status of a Pet via the user interface or API.
- **FR-004**: System MUST provide the ability to filter Pet lists by active status (active/inactive).
- **FR-005**: System MUST ensure that inactive pets are excluded from default active pet views and reports.

### Key Entities *(include if feature involves data)*

- **Pet**: Represents a pet in the clinic; key attributes include id, name, birthDate, type, ownerId, and active (boolean).

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 100% of Pet records in the database have a non-null active field after migration.
- **SC-002**: Filtering pets by active status returns the correct count matching the database records.
- **SC-003**: User satisfaction with the new active status feature, measured via post-release survey, averages 4.0 or higher on a 5-point scale.

## Assumptions

- The existing pet management user interface can be extended to include the active field without major redesign.
- A database migration script will be executed to set active=true for all existing pet records.
- The performance impact of adding a boolean column and filtering on it is negligible.
- No other system components depend on the pet's active status for critical operations beyond listing and reporting.