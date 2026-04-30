# Implementation Plan: Pet Extended Fields

**Branch**: `001-pet-extended-fields` | **Date**: 2026-04-30 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `specs/001-pet-extended-fields/spec.md`

## Summary

Add 7 new fields/capabilities to the Pet entity: photo URL, microchip ID (unique), color, breed, active/inactive status, weight, notes, and gender enum. Five of these fields (photoUrl, microchip, color, breed, active) already exist in the codebase. The remaining three (weight, notes, gender) need to be added along with UI updates for all fields.

---

## Technical Context

**Language/Version**: Java 17
**Primary Dependencies**: Spring Framework 7.0.6, Spring Data JPA, Hibernate 7.3.0.Final
**Storage**: HSQLDB (default), H2, MySQL, PostgreSQL — all must be kept in sync
**Testing**: JUnit Jupiter 6.0.2, Mockito 5.23.0, AssertJ 3.27.7
**Target Platform**: Web (Servlet container — Jetty 11 / Tomcat 11)

**Project Type**: Web application (Spring MVC + JSP)
**Performance Goals**: Standard web app — page loads under 2s
**Constraints**: 3-layer architecture (Presentation → Service → Repository); all changes through ClinicService; schema changes in all DB scripts
**Scale/Scope**: Single-server educational application

---

## Constitution Check

| Principle | Status | Notes |
|-----------|--------|-------|
| I. Layered Architecture | ✅ Compliant | All access through ClinicService |
| II. Convention-First | ✅ Compliant | Gender extends NamedEntity or uses enum; new fields follow existing patterns |
| III. Test Coverage | ✅ Planned | Tests in Phase 5 |
| IV. Schema Backwards-Compatibility | ✅ Compliant | All new columns are nullable or have defaults |
| V. Simplicity | ✅ Compliant | Simple field additions, no new abstractions |

---

## Data Model

### New Entities

| Entity | Attributes | Relationships |
|--------|-----------|---------------|
| Gender (enum) | MALE, FEMALE, UNKNOWN | Used by Pet.gender |

### Modified Entities

| Entity | New Attributes | Notes |
|--------|---------------|-------|
| Pet | +weight: BigDecimal (nullable) | Positive decimal, 2 decimal places |
| Pet | +notes: String (TEXT) | Free-text, no max length |
| Pet | +gender: Gender (enum) | Defaults to UNKNOWN |

### Already Existing (no changes needed)

| Entity | Existing Attributes |
|--------|-------------------|
| Pet | photoUrl: String, microchip: String (unique), color: String, breed: String, active: Boolean (default true) |

---

## UI Design

### Modified Views

| View | Changes |
|------|---------|
| createOrUpdatePetForm.jsp | Add fields: weight, notes, gender (dropdown), photoUrl, microchip, color, breed, active (checkbox) |
| Pet detail (owner page) | Display all new fields |

### No New Endpoints

This is a traditional MVC app — the existing PetController handles create/update via form submission. No new REST endpoints needed.

---

## Implementation Phases

### Phase 1: Data Layer

- [ ] Create `Gender.java` enum in model package
- [ ] Add `weight` (BigDecimal), `notes` (String), `gender` (Gender enum) fields to `Pet.java`
- [ ] Add JPA annotations (@Column, @Enumerated) for new fields
- [ ] Update `hsqldb/schema.sql` — add weight, notes, gender columns to pets table
- [ ] Update `h2/schema.sql` — same
- [ ] Update `mysql/schema.sql` — same
- [ ] Update `postgresql/schema.sql` — same
- [ ] Update all `data.sql` files with sample data for new columns

**Files**: `src/main/java/.../model/Gender.java`, `src/main/java/.../model/Pet.java`, `src/main/resources/db/*/schema.sql`, `src/main/resources/db/*/data.sql`

### Phase 2: Service Layer

- [ ] Verify `ClinicService` handles Pet persistence (already does via repository)
- [ ] Add weight validation (positive number) in service or via Bean Validation annotation
- [ ] Add microchip uniqueness validation (already handled by DB constraint, add service-level check for better error message)

**Files**: `src/main/java/.../service/ClinicService.java`

### Phase 3: UI Layer

- [ ] Update `createOrUpdatePetForm.jsp` — add form fields for weight, notes, gender, and ensure existing fields (photoUrl, microchip, color, breed, active) are displayed
- [ ] Add gender dropdown with MALE/FEMALE/UNKNOWN options
- [ ] Add active checkbox (default checked)
- [ ] Update owner detail page to display new pet fields
- [ ] Add validation messages for weight (positive number)

**Files**: `src/main/webapp/WEB-INF/jsp/pets/createOrUpdatePetForm.jsp`, `src/main/webapp/WEB-INF/jsp/owners/ownerDetails.jsp`

### Phase 4: Controller Updates

- [ ] Update `PetController` to bind new fields from form
- [ ] Add `@InitBinder` or converter for Gender enum if needed
- [ ] Ensure validation errors display correctly

**Files**: `src/main/java/.../web/PetController.java`

### Phase 5: Testing

- [ ] Update `ClinicServiceTests` to test Pet with new fields (weight, notes, gender)
- [ ] Test microchip uniqueness constraint
- [ ] Test gender default value (UNKNOWN)
- [ ] Test weight validation (reject negative/zero)
- [ ] Test active filtering if applicable
- [ ] Ensure all 3 persistence profiles pass (JPA, JDBC, Spring Data JPA)

**Files**: `src/test/java/.../service/ClinicServiceTests.java`, `src/test/java/.../web/PetControllerTests.java`

---

## Dependencies

| Task | Depends On |
|------|------------|
| Phase 2 (Service) | Phase 1 (Data Layer) |
| Phase 3 (UI) | Phase 1 (Data Layer) |
| Phase 4 (Controller) | Phase 3 (UI) |
| Phase 5 (Testing) | All phases |

---

## Risks & Open Questions

- **Risk**: H2/HSQLDB may not support enum columns natively — use VARCHAR with @Enumerated(EnumType.STRING)
- **Risk**: Existing data.sql INSERT statements may need updating if they use positional VALUES — use explicit column lists
- **Decision**: Weight stored as single current value (no history table) per spec assumptions
- **Decision**: Gender stored as VARCHAR via @Enumerated(STRING) for DB portability
