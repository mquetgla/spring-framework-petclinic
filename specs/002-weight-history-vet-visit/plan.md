# Implementation Plan: Weight History & Vet-Visit Assignment

**Branch**: `002-weight-history-vet-visit` | **Date**: 2026-04-30 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `specs/002-weight-history-vet-visit/spec.md`

## Summary

Add a WeightRecord entity for longitudinal pet weight tracking with chart visualization, and extend Visit with an optional veterinarian assignment (ManyToOne to Vet).

---

## Technical Context

**Language/Version**: Java 17
**Primary Dependencies**: Spring Framework 7.0.6, Spring Data JPA, Hibernate 7.3.0.Final
**Storage**: HSQLDB (default), H2, MySQL, PostgreSQL — all must be kept in sync
**Testing**: JUnit Jupiter 6.0.2, Mockito 5.23.0, AssertJ 3.27.7
**Target Platform**: Web (Servlet container — Jetty 11 / Tomcat 11)

**Project Type**: Web application (Spring MVC + JSP)
**Performance Goals**: Standard web app — page loads under 2s
**Constraints**: 3-layer architecture (Presentation → Service → Repository); all access through ClinicService; schema changes in all DB scripts; JSP views with custom tags
**Scale/Scope**: Single-server educational application

---

## Constitution Check

| Principle | Status | Notes |
|-----------|--------|-------|
| I. Layered Architecture | ✅ Compliant | All access through ClinicService; new WeightRecord operations via ClinicService |
| II. Convention-First | ✅ Compliant | WeightRecord extends BaseEntity; schema changes in all 4 DBs; JSP uses existing tags |
| III. Test Coverage | ✅ Planned | Tests in Phase 5 |
| IV. Schema Backwards-Compatibility | ✅ Compliant | New table (weight_records) is additive; new nullable column (vet_id) on visits |
| V. Simplicity | ✅ Compliant | Simple entity + relationship; chart via lightweight JS (no new heavy dependencies) |

---

## Data Model

### New Entities

| Entity | Attributes | Relationships |
|--------|-----------|---------------|
| WeightRecord | id: int, weight: BigDecimal(5,2), measureDate: LocalDate | ManyToOne → Pet |

### Modified Entities

| Entity | New Attributes | Notes |
|--------|---------------|-------|
| Visit | +vet: Vet (ManyToOne, optional) | Nullable FK to vets table |
| Pet | +weightRecords: Set\<WeightRecord\> (OneToMany) | Cascade ALL, mapped by pet |

---

## UI Design

### New Views

| View | Purpose |
|------|---------|
| Weight history section on ownerDetails.jsp | List weight records + chart |
| Add weight form (inline or separate) | Record new weight measurement |

### Modified Views

| View | Changes |
|------|---------|
| createOrUpdateVisitForm.jsp | Add vet dropdown (optional) |
| ownerDetails.jsp | Show vet name on visit rows; show weight history + chart per pet |

### No New REST Endpoints

Traditional MVC — form submissions handled by controllers.

---

## Implementation Phases

### Phase 1: Data Layer

- [ ] Create `WeightRecord.java` entity extending BaseEntity in model package
- [ ] Add `weightRecords` OneToMany collection to `Pet.java`
- [ ] Add `vet` ManyToOne field (optional) to `Visit.java`
- [ ] Update all 4 `schema.sql` — add `weight_records` table + `vet_id` column on visits
- [ ] Update all 4 `data.sql` — seed weight records + add vet_id to visit inserts

**Files**: `src/main/java/.../model/`, `src/main/resources/db/`

### Phase 2: Service & Repository Layer

- [ ] Add `saveWeightRecord(WeightRecord)` to ClinicService interface
- [ ] Add `findWeightRecordsByPetId(int petId)` to ClinicService interface
- [ ] Add `deleteWeightRecord(int id)` to ClinicService interface
- [ ] Add `findVetById(int id)` to ClinicService interface (for vet dropdown binding)
- [ ] Implement in all 3 service implementations (JPA, JDBC, Spring Data JPA)

**Files**: `src/main/java/.../service/`, `src/main/java/.../repository/`

### Phase 3: Controller Layer

- [ ] Create `WeightRecordController` for add/delete weight operations
- [ ] Update `VisitController` to populate vets model attribute and bind vet field

**Files**: `src/main/java/.../web/`

### Phase 4: UI Layer

- [ ] Add vet dropdown to `createOrUpdateVisitForm.jsp`
- [ ] Add vet name display to visit rows in `ownerDetails.jsp`
- [ ] Add weight history table per pet in `ownerDetails.jsp`
- [ ] Add "Add Weight" form/link per pet
- [ ] Add weight trend chart (simple JS line chart using Canvas or lightweight library)

**Files**: `src/main/webapp/WEB-INF/jsp/`

### Phase 5: Testing

- [ ] Test WeightRecord CRUD via ClinicServiceTests
- [ ] Test Visit with vet assignment
- [ ] Test VisitController vet binding
- [ ] Ensure all 3 persistence profiles pass

**Files**: `src/test/java/...`

---

## Dependencies

| Task | Depends On |
|------|------------|
| Phase 2 (Service) | Phase 1 (Data Layer) |
| Phase 3 (Controller) | Phase 2 (Service) |
| Phase 4 (UI) | Phase 3 (Controller) |
| Phase 5 (Testing) | All phases |

---

## Risks & Open Questions

- **Risk**: Chart library — need a lightweight JS solution that doesn't require new webjars. Canvas-based inline chart or Chart.js via CDN are options.
- **Risk**: JDBC implementation — WeightRecord repository needs manual JDBC queries in the JDBC service implementation.
- **Decision**: vet_id on visits is nullable — existing visits retain NULL (no vet assigned).
- **Decision**: Weight chart rendered client-side via simple inline JavaScript.
