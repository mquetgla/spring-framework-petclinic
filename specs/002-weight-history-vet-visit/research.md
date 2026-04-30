# Research: Weight History & Vet-Visit Assignment

## Decision 1: WeightRecord Entity Design

**Decision**: Separate `weight_records` table with FK to pets, extending BaseEntity
**Rationale**: Follows PetClinic convention (Visit follows same pattern — separate entity linked to Pet). Allows unlimited historical entries.
**Alternatives considered**:
- JSON array in Pet: not queryable, violates relational model
- Embedded collection: doesn't support independent CRUD

## Decision 2: Visit-Vet Relationship

**Decision**: Add nullable `vet_id` FK column to existing `visits` table, ManyToOne in Visit entity
**Rationale**: Simple FK is the standard JPA pattern. Nullable preserves backward compatibility with existing visits. Follows the same pattern as Pet→PetType and Pet→Owner.
**Alternatives considered**:
- Join table (visits_vets): over-engineered for ManyToOne
- Separate assignment entity: unnecessary complexity

## Decision 3: Weight Chart Implementation

**Decision**: Use Chart.js via CDN for the weight trend line chart
**Rationale**: Chart.js is lightweight (~60KB), well-documented, requires no server-side changes, and renders via Canvas. The project already uses CDN-style webjars for flatpickr, so external JS is acceptable.
**Alternatives considered**:
- Inline SVG/Canvas with raw JS: too much custom code for proper axis labels
- Server-side image generation: heavy, requires new dependencies
- Chart.js via webjar: would need pom.xml change; CDN is simpler

## Decision 4: ClinicService Method Additions

**Decision**: Add 4 new methods to ClinicService interface: `saveWeightRecord`, `findWeightRecordsByPetId`, `deleteWeightRecord`, `findVetById`
**Rationale**: Constitution requires all data access through ClinicService (Principle I). Each method maps to a distinct use case.
**Alternatives considered**:
- Direct repository access from controller: violates layered architecture

## Decision 5: Weight Record Deletion

**Decision**: Hard delete via `deleteWeightRecord(int id)` — no soft delete
**Rationale**: Spec says "no soft delete or audit trail required." WeightRecord is simple measurement data, not a critical business document.

## All NEEDS CLARIFICATION: Resolved

No outstanding clarifications.
