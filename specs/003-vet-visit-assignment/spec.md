# Feature Specification: Vet Visit Assignment

**Feature Branch**: `[003-vet-visit-assignment]`
**Created**: 2026-05-04
**Status**: Draft
**Input**: User description: "Requisito #9: Relación Vet-Visit Asignar veterinario a cada Visit. Visit.vet (ManyToOne). Categoría: Media"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Assign Vet to New Visit (Priority: P1)

Clinic staff creating a new visit for a pet selects an active veterinarian from a dropdown list to assign to the visit before saving.

**Why this priority**: Core functionality required to fulfill the ManyToOne Vet-Visit relationship requirement; enables tracking which vet handled each visit.

**Independent Test**: Create a new visit for a pet, select an active vet from the available list, save the visit, and verify the assigned vet is persisted.

**Acceptance Scenarios**:

1. **Given** a clinic staff member is creating a new visit for a pet, **When** they select an active vet from the vet dropdown and save the visit, **Then** the visit is saved with the selected vet assigned.
2. **Given** a clinic staff member is creating a new visit, **When** they attempt to save without selecting a vet, **Then** the system displays an error requiring vet selection.

---

### User Story 2 - View Assigned Vet on Visit Details (Priority: P1)

Any user viewing a visit's details page sees the full name and contact information of the vet assigned to the visit.

**Why this priority**: Critical for clinic operations to quickly identify which vet handled a specific visit.

**Independent Test**: Open an existing visit's details page and verify the assigned vet's information is displayed clearly.

**Acceptance Scenarios**:

1. **Given** a user is viewing a visit's details page, **When** the page loads, **Then** the assigned vet's name and contact info are visible.
2. **Given** a user is viewing a list of visits, **When** the list loads, **Then** each visit entry displays the assigned vet's name.

---

### Edge Cases

- What happens when a vet assigned to a visit is marked as inactive? (Assigned visits remain unchanged, but the vet is no longer selectable for new visits)
- How does the system handle existing visits that were created before this feature (no assigned vet)? (Existing visits must be updated with a valid vet assignment via a data migration)
- What happens when no active vets are available in the system? (Users cannot create new visits until at least one active vet is added)

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST associate each Visit with exactly one Vet via a ManyToOne relationship (one vet can have many visits, each visit has one vet).
- **FR-002**: Users MUST be able to select a Vet from a list of active Vets when creating or editing a Visit.
- **FR-003**: System MUST display the assigned Vet's information on all Visit-related views (details page, visit list, pet visit history).
- **FR-004**: System MUST NOT allow inactive Vets to be selected for new or updated Visit assignments.
- **FR-005**: System MUST require Vet assignment for all Visit creation and edit operations (no unassigned visits permitted).

### Key Entities *(include if feature involves data)*

- **Visit**: Represents a veterinary clinic visit for a pet. Adds a new `vet` attribute referencing a single Vet (ManyToOne relationship).
- **Vet**: Represents a veterinarian in the clinic. No changes to the Vet entity itself, but gains an implicit one-to-many relationship to Visit (not required for this feature's scope).

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 100% of Visits (new and existing) have an assigned Vet with no unassigned visits in the system.
- **SC-002**: Users can assign a Vet to a Visit in under 30 seconds from start to save.
- **SC-003**: Assigned Vet information is visible on 100% of Visit-related views (details, list, history).
- **SC-004**: Inactive Vets appear in 0% of Vet selection dropdowns for Visit assignment.

## Assumptions

- All Visits (new and existing) must have an assigned Vet (mandatory relationship).
- Only active Vets are available for assignment to Visits (inactive vets are excluded from selection).
- Vet assignment is required for both Visit creation and edit operations.
- Existing visits without an assigned Vet will be updated via a data migration to assign a default active vet.
- The Vet-Visit relationship is unidirectional (Visit to Vet) for this feature's scope; no reverse relationship lookup is required.
