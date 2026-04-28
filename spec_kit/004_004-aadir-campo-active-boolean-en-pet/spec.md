# Feature Specification: 004 Aadir Campo Active Boolean En Pet

**Feature Branch**: `[004-add-active-flag-to-pet]`  
**Created**: 2026-04-27  
**Status**: Draft  
**Input**: User description: "Requirement #4: Añadir campo active (boolean) en Pet"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Mark Pet as Inactive (Priority: P1)

As an admin, I want to mark a Pet as inactive (active = false) when deceased or transferred so that historical data is preserved without appearing in active listings.

**Why this priority**: Critical for lifecycle management and data integrity; ensures non-active pets are retained without being shown in standard active views.

**Independent Test**:  
- Create a Pet (active defaults to true).  
- Update Pet.active to false via API/UI.  
- Verify in the data store that Pet.active is false and API returns active: false for that Pet.

**Acceptance Scenarios**:

1. **Given** a Pet with active = true, **When** an admin updates Pet.active to false, **Then** Pet.active is false.
2. **Given** a Pet with active = false, **When** listing active Pets with default filters, **Then** the Pet is not included in the response.

---

### User Story 2 - Default Visibility & Inactives Filtering (Priority: P2)

Users expect the system to show only active pets by default, with an option to include inactive ones when needed.

**Why this priority**: Improves usability and relevance of data presented to users; aligns with common domain expectations.

**Independent Test**:  
- Create two Pets: one active, one inactive.  
- GET /pets without extra parameters returns only the active Pet.  
- GET /pets?includeInactive=true returns both Pets.

**Acceptance Scenarios**:

1. **Given** an active and an inactive Pet, **When** calling GET /pets without includeInactive, **Then** only the active Pet appears.
2. **Given** an inactive Pet, **When** enabling includeInactive, **Then** the inactive Pet appears in the results.

---

### Edge Cases

- What happens when attempting to set active to null or an invalid value?  
  The system MUST reject invalid inputs and return a 400 Bad Request with a clear validation message.

- How does the system handle existing Pet records when introducing the new field?  
  All existing records SHOULD default active to true unless a migration strategy is applied; any migration plan should be documented and tested.

---

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST add an `active` boolean field to the Pet entity with a default value of `true`.
- **FR-002**: System MUST enforce non-null constraint on `active` (defaulting to `true` where not explicitly set).
- **FR-003**: System MUST expose the `active` field in create and update API operations for Pet.
- **FR-004**: System MUST implement default filtering for pet listings to show only active Pets, with an optional parameter to include inactive Pets (e.g., `includeInactive=true`).
- **FR-005**: System MUST log or audit state changes to `active` when a Pet transitions between active and inactive states (if audit is in scope for Pet entity changes).

### Key Entities *(include if feature involves data)*

- **Pet**: Represents a pet in the system. Key attributes include:
  - `id` (identifier)
  - `name`
  - `species` / `type`
  - `birthDate` (optional)
  - `ownerId` (optional)
  - `active` (boolean, default: true)

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 100% of newly created Pets have `active = true` by default.
- **SC-002**: Admins can set `active` to `false` via update operations and the change persists.
- **SC-003**: Pet listings without `includeInactive` return only Pets with `active = true`.
- **SC-004**: When `includeInactive=true`, listings return both active and inactive Pets.

## Assumptions

- The Pet entity already exists in the domain model and persistence layer.
- The API and UI layers are capable of handling and persisting a new boolean field.
- There is an established mechanism for default values and data migrations; any migration plan will be documented if required.
- Authentication/authorization rules allow admins to update the `active` flag on Pet records.

---