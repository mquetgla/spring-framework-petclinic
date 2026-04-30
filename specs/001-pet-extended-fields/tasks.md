# Tasks: Pet Extended Fields

**Input**: Design documents from `specs/001-pet-extended-fields/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md

**Tests**: Not explicitly requested — test tasks omitted.

**Organization**: Tasks grouped by user story for independent implementation. Five fields (photoUrl, microchip, color, breed, active) already exist in the data layer and JSP form. Only weight, notes, and gender require data layer + UI additions.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

---

## Phase 1: Setup

**Purpose**: No project initialization needed — existing Spring PetClinic project.

- [x] T001 Verify current branch is `001-pet-extended-fields` and working tree is clean

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Create the Gender enum and add all missing columns to schema/data across all 4 databases. These changes are shared by multiple user stories and MUST complete first.

**CRITICAL**: No user story work can begin until this phase is complete.

- [x] T002 Create `Gender.java` enum with values MALE, FEMALE, UNKNOWN in `src/main/java/org/springframework/samples/petclinic/model/Gender.java`
- [x] T003 Add `weight` (BigDecimal), `notes` (String @Lob), `gender` (Gender @Enumerated STRING) fields with getters/setters to `src/main/java/org/springframework/samples/petclinic/model/Pet.java`
- [x] T004 [P] Update HSQLDB schema: add `weight DECIMAL(5,2)`, `notes CLOB`, `gender VARCHAR(20) DEFAULT 'UNKNOWN'` columns to pets table in `src/main/resources/db/hsqldb/schema.sql`
- [x] T005 [P] Update MySQL schema: add `weight DECIMAL(5,2)`, `notes TEXT`, `gender VARCHAR(20) DEFAULT 'UNKNOWN'` columns to pets table in `src/main/resources/db/mysql/schema.sql`
- [x] T006 [P] Update PostgreSQL schema: add `weight DECIMAL(5,2)`, `notes TEXT`, `gender VARCHAR(20) DEFAULT 'UNKNOWN'` columns to pets table in `src/main/resources/db/postgresql/schema.sql`
- [x] T007 [P] Update H2 schema: add `weight DECIMAL(5,2)`, `notes CLOB`, `gender VARCHAR(20) DEFAULT 'UNKNOWN'` columns to pets table in `src/main/resources/db/h2/schema.sql`
- [x] T008 [P] Update HSQLDB seed data: add weight, notes, gender values to INSERT statements using explicit column lists in `src/main/resources/db/hsqldb/data.sql`
- [x] T009 [P] Update MySQL seed data: add weight, notes, gender values to INSERT statements using explicit column lists in `src/main/resources/db/mysql/data.sql`
- [x] T010 [P] Update PostgreSQL seed data: add weight, notes, gender values to INSERT statements using explicit column lists in `src/main/resources/db/postgresql/data.sql`
- [x] T011 [P] Update H2 seed data: add weight, notes, gender values to INSERT statements using explicit column lists in `src/main/resources/db/h2/data.sql`

**Checkpoint**: All schema and entity changes complete. Application should start with `mvn spring-javaformat:apply && mvn test`.

---

## Phase 3: User Story 4 - Mark Pet Active/Inactive (Priority: P1)

**Goal**: Staff can toggle active/inactive status; inactive pets are visually distinguished in listings.

**Independent Test**: Create a pet, mark it inactive, verify it shows as inactive in owner detail page.

**Note**: The `active` field, checkbox in the form, and schema column already exist. This story only needs owner detail page display.

### Implementation for User Story 4

- [x] T012 [US4] Display `active` status badge (Active/Inactive) on pet entry in `src/main/webapp/WEB-INF/jsp/owners/ownerDetails.jsp`

**Checkpoint**: Active/inactive status visible on owner detail page.

---

## Phase 4: User Story 2 - Register Microchip ID (Priority: P1)

**Goal**: Microchip ID displayed on pet detail with uniqueness enforced.

**Independent Test**: Save a pet with microchip ID, verify it displays. Try duplicating the ID on another pet, verify rejection.

**Note**: microchip field, form input, and unique DB constraint already exist. This story needs display + service-level validation for user-friendly error.

### Implementation for User Story 2

- [x] T013 [US2] Display microchip ID on pet entry in `src/main/webapp/WEB-INF/jsp/owners/ownerDetails.jsp`
- [x] T014 [US2] Add service-level microchip uniqueness check with user-friendly error message in `src/main/java/org/springframework/samples/petclinic/service/ClinicService.java`
- [x] T015 [US2] Handle microchip uniqueness validation error in `src/main/java/org/springframework/samples/petclinic/web/PetController.java`

**Checkpoint**: Microchip ID visible and uniqueness enforced with clear error.

---

## Phase 5: User Story 7 - Assign Gender/Sex (Priority: P1)

**Goal**: Staff selects gender (Male, Female, Unknown) from a dropdown; defaults to Unknown.

**Independent Test**: Create a pet without selecting gender — verify it defaults to Unknown. Edit pet, change gender, verify persistence.

### Implementation for User Story 7

- [x] T016 [US7] Add gender dropdown (MALE, FEMALE, UNKNOWN) to pet form in `src/main/webapp/WEB-INF/jsp/pets/createOrUpdatePetForm.jsp`
- [x] T017 [US7] Register Gender enum converter or @InitBinder in `src/main/java/org/springframework/samples/petclinic/web/PetController.java`
- [x] T018 [US7] Display gender on pet entry in `src/main/webapp/WEB-INF/jsp/owners/ownerDetails.jsp`

**Checkpoint**: Gender selectable on form, defaults to UNKNOWN, displayed on owner detail.

---

## Phase 6: User Story 1 - Add Photo to Pet (Priority: P2)

**Goal**: Photo URL displayed as thumbnail image on pet detail page.

**Independent Test**: Enter a photo URL for a pet, verify image renders on owner detail page.

**Note**: photoUrl field and form input already exist. This story needs image display.

### Implementation for User Story 1

- [x] T019 [US1] Display photo as thumbnail image (from photoUrl) on pet entry in `src/main/webapp/WEB-INF/jsp/owners/ownerDetails.jsp`

**Checkpoint**: Pet photo renders as image on owner detail page.

---

## Phase 7: User Story 3 - Record Color and Breed (Priority: P2)

**Goal**: Color and breed displayed on pet detail page.

**Independent Test**: Set color and breed for a pet, verify they display on owner detail page.

**Note**: Fields and form inputs already exist. This story needs display.

### Implementation for User Story 3

- [x] T020 [US3] Display color and breed on pet entry in `src/main/webapp/WEB-INF/jsp/owners/ownerDetails.jsp`

**Checkpoint**: Color and breed visible on owner detail page.

---

## Phase 8: User Story 5 - Record Pet Weight (Priority: P2)

**Goal**: Staff enters weight in the pet form; displayed on detail page.

**Independent Test**: Enter a weight for a pet, verify it persists and displays. Enter a negative value, verify validation rejects it.

### Implementation for User Story 5

- [x] T021 [US5] Add weight input field to pet form in `src/main/webapp/WEB-INF/jsp/pets/createOrUpdatePetForm.jsp`
- [x] T022 [US5] Add `@Positive` or `@DecimalMin("0.01")` validation annotation on weight field in `src/main/java/org/springframework/samples/petclinic/model/Pet.java`
- [x] T023 [US5] Display weight on pet entry in `src/main/webapp/WEB-INF/jsp/owners/ownerDetails.jsp`

**Checkpoint**: Weight field on form, validated positive, displayed on detail page.

---

## Phase 9: User Story 6 - Add Owner Notes (Priority: P3)

**Goal**: Free-text notes field on pet form; displayed on detail page.

**Independent Test**: Enter notes for a pet, verify they persist and display.

### Implementation for User Story 6

- [x] T024 [US6] Add notes textarea field to pet form in `src/main/webapp/WEB-INF/jsp/pets/createOrUpdatePetForm.jsp`
- [x] T025 [US6] Display notes on pet entry in `src/main/webapp/WEB-INF/jsp/owners/ownerDetails.jsp`

**Checkpoint**: Notes editable and displayed.

---

## Phase 10: Polish & Cross-Cutting Concerns

**Purpose**: Final validation and cleanup across all stories.

- [x] T026 Run `mvn spring-javaformat:apply` to ensure code formatting compliance
- [x] T027 Run `mvn test` and verify all tests pass across all 3 persistence profiles (JPA, JDBC, Spring Data JPA)
- [x] T028 Verify all 4 schema.sql files are consistent (HSQLDB, H2, MySQL, PostgreSQL)
- [x] T029 Verify all 4 data.sql files use explicit column lists and include values for all new columns

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies
- **Foundational (Phase 2)**: Depends on Phase 1 — BLOCKS all user stories
- **User Stories (Phases 3-9)**: All depend on Phase 2 completion
  - Stories can proceed in parallel (they modify different parts of the same JSP files, so sequential is safer)
  - Recommended order: P1 stories first (US4 → US2 → US7), then P2 (US1 → US3 → US5), then P3 (US6)
- **Polish (Phase 10)**: Depends on all story phases

### User Story Dependencies

- **US4 (Active/Inactive)**: Phase 2 only — no other story dependencies
- **US2 (Microchip)**: Phase 2 only — no other story dependencies
- **US7 (Gender)**: Phase 2 only (needs Gender enum from T002)
- **US1 (Photo)**: Phase 2 only — no other story dependencies
- **US3 (Color/Breed)**: Phase 2 only — no other story dependencies
- **US5 (Weight)**: Phase 2 only — no other story dependencies
- **US6 (Notes)**: Phase 2 only — no other story dependencies

### Within Each User Story

- JSP display tasks can run in parallel with controller tasks
- Service validation depends on entity being complete (Phase 2)

### Parallel Opportunities

- T004-T007 (schema updates) can all run in parallel
- T008-T011 (data updates) can all run in parallel
- Once Phase 2 is complete, all user story phases can theoretically start in parallel
- In practice, sequential is recommended since multiple stories modify `ownerDetails.jsp`

---

## Implementation Strategy

### MVP First (P1 Stories Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational (schema + entity + Gender enum)
3. Complete Phases 3-5: US4 (Active), US2 (Microchip), US7 (Gender)
4. **STOP and VALIDATE**: Run `mvn test`, verify all P1 stories work
5. Deploy/demo if ready

### Incremental Delivery

1. Phase 2 → Foundation ready
2. Add P1 stories → Test → Deploy (MVP)
3. Add P2 stories (US1, US3, US5) → Test → Deploy
4. Add P3 stories (US6) → Test → Deploy
5. Each story adds value without breaking previous stories

---

## Notes

- 5 of 7 fields already exist in entity + schema + form — most story work is JSP display on ownerDetails.jsp
- Only weight, notes, gender need full data layer + UI additions
- All stories share ownerDetails.jsp for display — recommend sequential editing to avoid merge conflicts
- Total: 29 tasks across 10 phases
