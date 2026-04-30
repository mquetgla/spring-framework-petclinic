# Research: Pet Extended Fields

## Decision 1: Gender Enum Storage Strategy

**Decision**: Use `@Enumerated(EnumType.STRING)` with a VARCHAR column
**Rationale**: All 4 target databases (H2, HSQLDB, MySQL, PostgreSQL) support VARCHAR. String storage is human-readable in DB and resilient to enum reordering.
**Alternatives considered**:
- INTEGER ordinal: fragile if enum values are reordered
- DB-native ENUM type: not portable across H2/HSQLDB/MySQL/PostgreSQL

## Decision 2: Weight Column Type

**Decision**: Use `DECIMAL(5,2)` in SQL, `BigDecimal` in Java
**Rationale**: Supports pets up to 999.99 kg with 2-decimal precision. BigDecimal avoids floating-point rounding issues.
**Alternatives considered**:
- DOUBLE/Float: rounding issues
- INTEGER (grams): less intuitive for users

## Decision 3: Photo Storage

**Decision**: Keep existing `photoUrl` (VARCHAR) — store URL reference only
**Rationale**: Already implemented. BLOB storage would complicate the simple architecture. URL can point to external storage or a static resource path.
**Alternatives considered**:
- BLOB column: complex, violates Simplicity principle
- Separate table: over-engineered for single photo

## Decision 4: Notes Column Type

**Decision**: Use `CLOB` for HSQLDB/H2, `TEXT` for MySQL/PostgreSQL, mapped as `@Lob` or `@Column(columnDefinition)` in JPA
**Rationale**: Allows unlimited text. Standard across all target DBs with appropriate type.
**Alternatives considered**:
- VARCHAR(4000): artificial limit on free-text notes

## Decision 5: Active/Inactive Filtering

**Decision**: No separate filtered queries initially. The `active` field is informational — displayed in listings but not filtered by default.
**Rationale**: Spec says "System MUST allow filtering or excluding inactive pets" but marks it as informational. Start with display, add filtering in a subsequent iteration if needed.
**Alternatives considered**:
- Repository method `findByActiveTrue()`: would need UI toggle — add later if requested

## All NEEDS CLARIFICATION: Resolved

No outstanding clarifications. All technical decisions made based on project conventions and spec assumptions.
