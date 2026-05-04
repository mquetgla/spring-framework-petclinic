# Feature Specification: Pet Owner Transfer

**Feature Branch**: `004-pet-owner-transfer`
**Created**: 2026-05-04
**Status**: Draft
**Input**: User description: "Requisito #10: Transferencia de mascota. Descripción: Flujo para cambiar owner de una mascota existente (con historial). Categoría: Media"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Transfer Pet to New Owner (Priority: P1)

Veterinary staff can transfer ownership of an existing pet from one owner to another while preserving all historical visit records, medical data, and weight history. The new owner must already exist in the system.

**Why this priority**: This is the core functionality required to support pet ownership changes, which is essential for cases like pet adoption, rehoming, or ownership disputes. Preserving history is critical for continuity of care.

**Independent Test**: Verify that a pet can be transferred from Owner A to Owner B, and after transfer, Owner B can see all historical visits and medical records that were created under Owner A.

**Acceptance Scenarios**:

1. **Given** a pet "Max" belongs to Owner A with 3 historical vet visits, **When** veterinary staff transfers ownership to Owner B (existing owner), **Then** Owner B becomes the new owner and can view all 3 historical visits
2. **Given** a pet has weight history records from previous visits, **When** ownership is transferred to a new owner, **Then** the weight history remains intact and visible to the new owner
3. **Given** veterinary staff searches for a new owner, **When** they enter valid owner name or ID, **Then** the system displays matching owners for selection

---

### User Story 2 - Transfer Validation and Confirmation (Priority: P2)

The system validates that the new owner exists and is different from the current owner before allowing the transfer. A confirmation step shows the pet details and impact of the transfer before finalizing.

**Why this priority**: Prevents accidental transfers and ensures data integrity by validating inputs and showing clear confirmation of the action being taken.

**Independent Test**: Attempt to transfer a pet to the same owner or a non-existent owner, and verify the system prevents the transfer with appropriate messaging.

**Acceptance Scenarios**:

1. **Given** staff attempts to transfer a pet to the same owner, **When** they select the current owner as the new owner, **Then** the system displays an error message and prevents the transfer
2. **Given** staff initiates a pet transfer, **When** they review the confirmation screen, **Then** they see pet name, current owner, new owner, and a warning that all historical data stays with the pet
3. **Given** a pet transfer is confirmed, **When** the transfer completes, **Then** the system displays a success message with the updated ownership information

---

### Edge Cases

- What happens when the new owner has the same name as the current owner but different ID? (System should use ID-based matching, not name)
- How does system handle transfer of a pet that has upcoming scheduled visits? (Visits remain with the pet, new owner can see them)
- What happens when the current owner has multiple pets and only one is transferred? (Only the selected pet is transferred, others remain with current owner)
- How does system handle concurrent transfer attempts on the same pet? (Last-write-wins or optimistic locking should prevent conflicts)

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST allow veterinary staff to select an existing pet and initiate an ownership transfer
- **FR-002**: System MUST require the new owner to be an existing owner in the system (pre-registered)
- **FR-003**: System MUST preserve all historical data (visits, medical records, weight history) after ownership transfer
- **FR-004**: System MUST prevent transferring a pet to the same owner (current owner)
- **FR-005**: System MUST display a confirmation screen showing pet details, current owner, and new owner before finalizing
- **FR-006**: System MUST assign the pet to the new owner after confirmation
- **FR-007**: System MUST display success confirmation after completed transfer with updated ownership details
- **FR-008**: Users MUST be able to search for owners by name or ID when selecting the new owner

### Key Entities *(include if feature involves data)*

- **Pet**: Represents the animal being transferred. Key attributes: id, name, type, birth date, owner (current owner reference), medical history
- **Owner**: Represents the pet owner. Key attributes: id, first_name, last_name, address, phone, email. Relationship: One owner can have multiple pets
- **Visit**: Represents veterinary visits. Key attributes: id, pet_id, date, description, weight. Relationship: Visits stay with the pet regardless of ownership changes

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Veterinary staff can complete a pet ownership transfer in under 2 minutes
- **SC-002**: 100% of historical visit records and medical data remain accessible after ownership transfer
- **SC-003**: Staff cannot complete invalid transfers (same owner, non-existent owner) with 100% accuracy
- **SC-004**: 95% of users successfully complete the transfer process without needing assistance

## Assumptions

- The new owner must already be registered in the system before the transfer (no on-the-fly owner creation during transfer)
- All historical data (visits, medical records, weight history) remains with the pet and is visible to the new owner
- Veterinary staff are the only users authorized to perform ownership transfers

- No notification is sent to the old owner (transfer is administrative, not requiring old owner approval)
- Transfer is immediate upon confirmation (no approval workflow)
