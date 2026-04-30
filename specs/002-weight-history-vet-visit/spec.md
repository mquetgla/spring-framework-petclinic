# Feature Specification: Weight History & Vet-Visit Assignment

**Feature Branch**: `002-weight-history-vet-visit`
**Created**: 2026-04-30
**Status**: Draft
**Input**: Unified specification covering pet weight history tracking and veterinarian-to-visit assignment

## User Scenarios & Testing

### User Story 1 - Record Pet Weight Over Time (Priority: P1)

During a visit, clinic staff records the pet's current weight. The system stores each weight measurement with a date, building a history over time. Staff can view the weight evolution to detect trends (weight gain, loss, or stability).

**Why this priority**: Weight tracking is fundamental for medication dosing, dietary planning, and early detection of health issues. A single current weight (already in the system) is insufficient for longitudinal health monitoring.

**Independent Test**: Record 3 weight entries for a pet on different dates; verify all entries are listed chronologically and a visual trend is displayed.

**Acceptance Scenarios**:

1. **Given** a pet exists, **When** staff records a weight measurement with a date, **Then** the entry is saved and appears in the pet's weight history.
2. **Given** a pet has multiple weight records, **When** staff views the pet's profile, **Then** all weight entries are displayed in chronological order (most recent first).
3. **Given** a pet has 3 or more weight records, **When** staff views the weight history, **Then** a visual chart shows the weight trend over time.
4. **Given** a pet has no weight records, **When** staff views the pet's profile, **Then** the weight history section shows "No weight records" with an option to add the first entry.

---

### User Story 2 - Assign Veterinarian to Visit (Priority: P1)

When creating or editing a visit, staff selects the attending veterinarian from the list of available vets. Each visit records which vet performed the consultation.

**Why this priority**: Knowing which vet handled a visit is essential for continuity of care, accountability, and follow-up scheduling.

**Independent Test**: Create a visit and assign a vet; verify the vet's name appears on the visit record in the pet's history.

**Acceptance Scenarios**:

1. **Given** staff is creating a new visit, **When** the visit form is displayed, **Then** a dropdown lists all available veterinarians.
2. **Given** staff selects a vet and saves the visit, **Then** the vet assignment is persisted and displayed on the visit record.
3. **Given** a visit exists without an assigned vet, **When** staff edits the visit, **Then** they can assign a vet retroactively.
4. **Given** a visit has an assigned vet, **When** staff views the pet's visit history, **Then** the vet's name is displayed alongside the visit date and description.

---

### Edge Cases

- What happens when a weight value is negative or zero? The system rejects it with a validation error.
- What happens when two weight records have the same date for the same pet? Both are accepted — multiple measurements per day are valid (e.g., before and after a procedure).
- What happens when a vet is removed from the system after being assigned to visits? The visit retains the vet reference; deletion of vets with existing visits is prevented.
- What happens when no vets exist in the system? The vet dropdown is empty with a message indicating no vets are available; the visit can still be saved without a vet.

## Requirements

### Functional Requirements

- **FR-001**: System MUST allow creating weight records for a pet, each consisting of a weight value (positive decimal) and a measurement date.
- **FR-002**: System MUST display all weight records for a pet in reverse chronological order.
- **FR-003**: System MUST display a visual weight trend chart when a pet has 3 or more weight records.
- **FR-004**: System MUST allow deleting individual weight records.
- **FR-005**: System MUST allow assigning a veterinarian to each visit via a selection control.
- **FR-006**: System MUST display the assigned veterinarian's name on each visit record in the pet's visit history.
- **FR-007**: System MUST allow the vet field on a visit to be optional (visits without an assigned vet are valid).
- **FR-008**: System MUST validate that weight values are positive numbers greater than zero.
- **FR-009**: System MUST display the weight history and weight chart on the pet's detail/owner page.

### Key Entities

- **WeightRecord**: A measurement entry linked to a specific pet, containing weight (positive decimal), date of measurement, and the pet reference.
- **Visit** (modified): Extended to include an optional reference to a veterinarian.
- **Vet** (existing): Referenced by visits; no modifications needed.

## Success Criteria

### Measurable Outcomes

- **SC-001**: Staff can record a weight and see it in the pet's history within a single page interaction (no more than 2 clicks).
- **SC-002**: Weight trend chart renders correctly for pets with 3+ records, displaying date on the horizontal axis and weight on the vertical axis.
- **SC-003**: 100% of new visits display the assigned vet's name in the visit history.
- **SC-004**: The vet selection dropdown loads all registered vets within 1 second.
- **SC-005**: All existing visit and pet functionality continues to work without regression.
- **SC-006**: Weight records are accurately stored to 2 decimal places of precision.

## Assumptions

- Weight is measured in kilograms (consistent with the existing weight field on Pet).
- The existing `weight` field on the Pet entity stores the most recent weight; the weight history provides longitudinal data. The current weight field may be updated automatically when a new weight record is added, or kept independent — assumed independent (the existing field is a quick-reference, the history is the full record).
- The weight chart uses a simple line chart. No specific charting library is prescribed; implementation will choose the appropriate approach.
- The vet assignment on Visit is optional to maintain backward compatibility with existing visits that have no vet assigned.
- All registered vets appear in the dropdown regardless of specialties. Filtering by specialty is out of scope.
- Weight record deletion is a simple remove — no soft delete or audit trail required.
