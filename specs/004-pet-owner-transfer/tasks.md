---
description: "Task list for Pet Owner Transfer feature implementation"
---

# Tasks: Pet Owner Transfer

**Input**: Design documents from `/specs/004-pet-owner-transfer/`
**Prerequisites**: plan.md, spec.md

**Organization**: Tasks are grouped by user story to enable independent implementation and testing.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (US1, US2)
- Include exact file paths in descriptions

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Verify project structure and prepare for implementation

- [X] T001 Verify Spring PetClinic project structure and build configuration
- [X] T002 [P] Review existing Pet, Owner, and Visit entity relationships in `src/main/java/org/springframework/samples/petclinic/model/`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [X] T003 Add `transferPetToOwner` method signature to `src/main/java/org/springframework/samples/petclinic/service/ClinicService.java` interface
- [X] T004 Implement `transferPetToOwner` method in `src/main/java/org/springframework/samples/petclinic/service/ClinicServiceImpl.java` to update pet's owner reference and save pet
- [X] T005 [P] Add `findOwnerById` validation logic to ensure owner exists in `src/main/java/org/springframework/samples/petclinic/service/ClinicServiceImpl.java`

**Checkpoint**: Foundation ready - user story implementation can now begin

---

## Phase 3: User Story 1 - Transfer Pet to New Owner (Priority: P1) 🎯 MVP

**Goal**: Enable veterinary staff to transfer pet ownership to an existing owner while preserving all historical data (visits, weight records).

**Independent Test**: Verify that a pet can be transferred from Owner A to Owner B, and after transfer, Owner B can see all historical visits and medical records that were created under Owner A.

### Implementation for User Story 1

- [X] T006 [US1] Create `initTransferPetForm` method in `src/main/java/org/springframework/samples/petclinic/web/PetController.java` to display transfer form with owner search
- [X] T007 [US1] Create `searchOwnersForTransfer` method in `src/main/java/org/springframework/samples/petclinic/web/PetController.java` to search for new owner by lastName
- [X] T008 [US1] Create `processPetTransfer` method in `src/main/java/org/springframework/samples/petclinic/web/PetController.java` to execute ownership transfer via clinicService.transferPetToOwner()
- [X] T009 [US1] Create transfer confirmation view `src/main/webapp/WEB-INF/jsp/pets/transferPetForm.jsp` with owner search and pet details display
- [X] T010 [US1] Create transfer success view `src/main/webapp/WEB-INF/jsp/pets/transferPetSuccess.jsp` showing updated ownership information
- [X] T011 [US1] Add transfer link to pet details view in `src/main/webapp/WEB-INF/jsp/pets/petDetails.jsp` (or update owner details view)

**Checkpoint**: At this point, User Story 1 should be fully functional - staff can transfer pet ownership and all historical data remains accessible.

---

## Phase 4: User Story 2 - Transfer Validation and Confirmation (Priority: P2)

**Goal**: Add validation to prevent invalid transfers (same owner, non-existent owner) and display confirmation screen before finalizing transfer.

**Independent Test**: Attempt to transfer a pet to the same owner or a non-existent owner, and verify the system prevents the transfer with appropriate messaging.

### Implementation for User Story 2

- [ ] T012 [US2] Add validation in `src/main/java/org/springframework/samples/petclinic/web/PetController.java` to prevent transferring pet to same owner (add error message)
- [ ] T013 [US2] Add validation in `src/main/java/org/springframework/samples/petclinic/web/PetController.java` to verify new owner exists before transfer
- [ ] T014 [US2] Update `processPetTransfer` in `src/main/java/org/springframework/samples/petclinic/web/PetController.java` to show confirmation screen with pet details, current owner, and new owner
- [ ] T015 [US2] Update transfer confirmation view `src/main/webapp/WEB-INF/jsp/pets/transferPetForm.jsp` to display confirmation screen with warning that historical data stays with pet
- [ ] T016 [US2] Add success message display after transfer completion in `src/main/webapp/WEB-INF/jsp/pets/transferPetSuccess.jsp`
- [ ] T017 [US2] Add error message handling for invalid transfer attempts in transfer form view

**Checkpoint**: At this point, User Stories 1 AND 2 should both work independently - transfers are validated and confirmed before completion.

---

## Phase 5: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [ ] T018 [P] Update navigation to include transfer option in pet/owner views in `src/main/webapp/WEB-INF/jsp/`
- [ ] T019 Add integration test for pet transfer in `src/test/java/org/springframework/samples/petclinic/service/ClinicServiceTests.java`
- [ ] T020 Add web test for transfer flow in `src/test/java/org/springframework/samples/petclinic/web/PetControllerTests.java`
- [ ] T021 Code cleanup and review of transfer implementation
- [ ] T022 Verify all historical data (visits, weight records) remains accessible after transfer by checking `src/main/java/org/springframework/samples/petclinic/model/Pet.java` relationships

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3+)**: All depend on Foundational phase completion
  - User stories can then proceed in parallel (if staffed)
  - Or sequentially in priority order (P1 → P2)
- **Polish (Phase 5)**: Depends on all desired user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational (Phase 2) - No dependencies on other stories
- **User Story 2 (P2)**: Can start after Foundational (Phase 2) - Builds on US1 but can be tested independently

### Within Each User Story

- Service methods before controller endpoints
- Controller endpoints before views
- Core implementation before validation enhancements

### Parallel Opportunities

- All Setup tasks marked [P] can run in parallel
- All Foundational tasks marked [P] can run in parallel (within Phase 2)
- Once Foundational phase completes, all user stories can start in parallel (if team capacity allows)
- Different user stories can be worked on in parallel by different team members

---

## Parallel Example: User Story 1

```bash
# Launch all view creation tasks for User Story 1 together:
Task: "Create transfer confirmation view transferPetForm.jsp"
Task: "Create transfer success view transferPetSuccess.jsp"
Task: "Add transfer link to pet details view"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational (CRITICAL - blocks all stories)
3. Complete Phase 3: User Story 1
4. **STOP and VALIDATE**: Test User Story 1 independently - verify pet transfer works and historical data is preserved
5. Deploy/demo if ready

### Incremental Delivery

1. Complete Setup + Foundational → Foundation ready
2. Add User Story 1 → Test independently → Deploy/Demo (MVP!)
3. Add User Story 2 → Test independently → Deploy/Demo
4. Each story adds value without breaking previous stories

### Parallel Team Strategy

With multiple developers:

1. Team completes Setup + Foundational together
2. Once Foundational is done:
   - Developer A: User Story 1
   - Developer B: User Story 2
3. Stories complete and integrate independently

---

## Notes

- [P] tasks = different files, no dependencies
- [Story] label maps task to specific user story for traceability
- Each user story should be independently completable and testable
- The Pet entity's relationship with Visit and WeightRecord is via `@OneToMany(mappedBy = "pet")`, so changing the owner does NOT affect these relationships
- Transfer is achieved by simply updating `pet.owner` reference and calling `savePet()`
- Commit after each task or logical group
- Stop at any checkpoint to validate story independently
