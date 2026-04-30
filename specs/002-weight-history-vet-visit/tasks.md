# Tasks: Weight History & Vet-Visit Assignment

**Input**: Design documents from `specs/002-weight-history-vet-visit/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md

**Tests**: Not explicitly requested — test tasks omitted.

**Organization**: Two user stories (both P1), each independently implementable after foundational phase. US1 = Weight History (new entity + chart), US2 = Vet-Visit Assignment (modify existing entity + UI).

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2)
- Include exact file paths in descriptions

---

## Phase 1: Setup

**Purpose**: Verify branch and working tree

- [ ] T001 Verify current branch is `002-weight-history-vet-visit` and working tree is clean

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Schema changes shared by both user stories. MUST complete before any story work.

**CRITICAL**: No user story work can begin until this phase is complete.

- [ ] T002 [P] Update HSQLDB schema: add `weight_records` table and `vet_id` nullable FK column on `visits` table in `src/main/resources/db/hsqldb/schema.sql`
- [ ] T003 [P] Update H2 schema: add `weight_records` table and `vet_id` nullable FK column on `visits` table in `src/main/resources/db/h2/schema.sql`
- [ ] T004 [P] Update MySQL schema: add `weight_records` table and `vet_id` nullable FK column on `visits` table in `src/main/resources/db/mysql/schema.sql`
- [ ] T005 [P] Update PostgreSQL schema: add `weight_records` table and `vet_id` nullable FK column on `visits` table in `src/main/resources/db/postgresql/schema.sql`
- [ ] T006 [P] Update HSQLDB seed data: add weight_records rows and vet_id values to visit inserts (use explicit column lists) in `src/main/resources/db/hsqldb/data.sql`
- [ ] T007 [P] Update H2 seed data: add weight_records rows and vet_id values to visit inserts (use explicit column lists) in `src/main/resources/db/h2/data.sql`
- [ ] T008 [P] Update MySQL seed data: add weight_records rows and vet_id values to visit inserts (use explicit column lists) in `src/main/resources/db/mysql/data.sql`
- [ ] T009 [P] Update PostgreSQL seed data: add weight_records rows and vet_id values to visit inserts (use explicit column lists) in `src/main/resources/db/postgresql/data.sql`

**Checkpoint**: All schema and seed data updated. Application should start with `mvn test`.

---

## Phase 3: User Story 1 - Record Pet Weight Over Time (Priority: P1) MVP

**Goal**: Staff records weight measurements for a pet; system stores history and displays a trend chart for pets with 3+ records.

**Independent Test**: Create 3+ weight records for a pet, verify they appear in chronological order and the chart renders.

### Implementation for User Story 1

- [ ] T010 [US1] Create `WeightRecord.java` entity extending BaseEntity with fields: weight (BigDecimal, @DecimalMin("0.01"), NOT NULL), measureDate (LocalDate, NOT NULL), pet (ManyToOne to Pet, NOT NULL) in `src/main/java/org/springframework/samples/petclinic/model/WeightRecord.java`
- [ ] T011 [US1] Add `weightRecords` OneToMany (cascade ALL, mappedBy "pet", fetch EAGER) collection with getter/adder methods to `src/main/java/org/springframework/samples/petclinic/model/Pet.java`
- [ ] T012 [US1] Add `saveWeightRecord(WeightRecord)`, `findWeightRecordsByPetId(int)`, and `deleteWeightRecord(int)` methods to ClinicService interface in `src/main/java/org/springframework/samples/petclinic/service/ClinicService.java`
- [ ] T013 [US1] Implement WeightRecord repository and service methods in JPA implementation in `src/main/java/org/springframework/samples/petclinic/repository/jpa/`
- [ ] T014 [US1] Implement WeightRecord repository and service methods in Spring Data JPA implementation in `src/main/java/org/springframework/samples/petclinic/repository/springdatajpa/`
- [ ] T015 [US1] Implement WeightRecord repository and service methods in JDBC implementation in `src/main/java/org/springframework/samples/petclinic/repository/jdbc/`
- [ ] T016 [US1] Create `WeightRecordController.java` with POST (add weight) and GET (delete weight) handlers for `/owners/{ownerId}/pets/{petId}/weights` in `src/main/java/org/springframework/samples/petclinic/web/WeightRecordController.java`
- [ ] T017 [US1] Add weight history section to pet detail in `src/main/webapp/WEB-INF/jsp/owners/ownerDetails.jsp`: table of weight records (date, weight, delete link) + Chart.js line chart (via CDN) for pets with 3+ records
- [ ] T018 [US1] Add "Add Weight" inline form or link per pet in `src/main/webapp/WEB-INF/jsp/owners/ownerDetails.jsp`

**Checkpoint**: Weight records can be added, viewed (list + chart), and deleted per pet.

---

## Phase 4: User Story 2 - Assign Veterinarian to Visit (Priority: P1)

**Goal**: Staff selects an attending vet from a dropdown when creating/editing a visit; vet name displayed in visit history.

**Independent Test**: Create a visit with a vet assigned, verify vet name appears in visit history on owner detail page.

### Implementation for User Story 2

- [ ] T019 [US2] Add `vet` ManyToOne (optional, nullable) field with getter/setter to `src/main/java/org/springframework/samples/petclinic/model/Visit.java`
- [ ] T020 [US2] Add `findVetById(int)` method to ClinicService interface in `src/main/java/org/springframework/samples/petclinic/service/ClinicService.java`
- [ ] T021 [US2] Implement findVetById in all 3 repository/service implementations (JPA, Spring Data JPA, JDBC) in `src/main/java/org/springframework/samples/petclinic/repository/`
- [ ] T022 [US2] Update `VisitController.java` to populate `vets` model attribute (all vets) and bind vet field on visit form submission in `src/main/java/org/springframework/samples/petclinic/web/VisitController.java`
- [ ] T023 [US2] Add vet dropdown (optional) to visit form in `src/main/webapp/WEB-INF/jsp/pets/createOrUpdateVisitForm.jsp`
- [ ] T024 [US2] Display vet name alongside visit date and description in visit history table in `src/main/webapp/WEB-INF/jsp/owners/ownerDetails.jsp`

**Checkpoint**: Visits can be created with an assigned vet; vet name appears in visit history.

---

## Phase 5: Polish & Cross-Cutting Concerns

**Purpose**: Final validation and cleanup.

- [ ] T025 Run `mvn test` and verify all tests pass across all 3 persistence profiles (JPA, JDBC, Spring Data JPA)
- [ ] T026 Verify all 4 schema.sql files are consistent (weight_records table + vet_id on visits)
- [ ] T027 Verify all 4 data.sql files use explicit column lists and include values for new columns
- [ ] T028 Verify Chart.js CDN link loads correctly and chart renders for sample data

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies
- **Foundational (Phase 2)**: Depends on Phase 1 — BLOCKS all user stories
- **US1 (Phase 3)**: Depends on Phase 2 — can run in parallel with US2
- **US2 (Phase 4)**: Depends on Phase 2 — can run in parallel with US1
- **Polish (Phase 5)**: Depends on both US1 and US2

### User Story Dependencies

- **US1 (Weight History)**: Phase 2 only — no dependency on US2
- **US2 (Vet-Visit)**: Phase 2 only — no dependency on US1
- Both stories modify `ownerDetails.jsp` — sequential recommended to avoid conflicts

### Within Each User Story

- Entity before service
- Service before controller
- Controller before UI
- All within a story are sequential

### Parallel Opportunities

- T002-T005 (schema updates) can all run in parallel
- T006-T009 (data updates) can all run in parallel
- US1 and US2 can run in parallel after Phase 2 (different entities/controllers)
- Within US1: T013-T015 (3 repository implementations) can run in parallel

---

## Parallel Example: User Story 1

```bash
# Repository implementations can run in parallel:
Task: "Implement WeightRecord in JPA repo"
Task: "Implement WeightRecord in Spring Data JPA repo"
Task: "Implement WeightRecord in JDBC repo"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational (schema + seed data)
3. Complete Phase 3: US1 (Weight History)
4. **STOP and VALIDATE**: Test weight recording + chart
5. Deploy/demo if ready

### Incremental Delivery

1. Phase 2 → Foundation ready
2. Add US1 (Weight History) → Test → Deploy (MVP)
3. Add US2 (Vet-Visit) → Test → Deploy
4. Each story adds value without breaking the other

---

## Notes

- Both stories are P1 but US1 (Weight History) is MVP because it introduces a new entity
- US2 (Vet-Visit) is simpler — modifies existing entity only
- Chart.js loaded via CDN (no pom.xml changes needed)
- ownerDetails.jsp modified by both stories — do US1 first, then US2
- Total: 28 tasks across 5 phases
