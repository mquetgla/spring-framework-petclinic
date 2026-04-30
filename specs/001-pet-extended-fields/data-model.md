# Data Model: Pet Extended Fields

## Modified Entity: Pet

### Current Fields (already in codebase)

| Field | Column | Type (Java) | Type (SQL) | Constraints |
|-------|--------|-------------|------------|-------------|
| photoUrl | photo_url | String | VARCHAR(255) | nullable |
| microchip | microchip_id | String | VARCHAR(255) | unique, nullable |
| color | color | String | VARCHAR(30) | nullable |
| breed | breed | String | VARCHAR(50) | nullable |
| active | active | Boolean | BOOLEAN | default TRUE |

### New Fields (to be added)

| Field | Column | Type (Java) | Type (SQL) | Constraints |
|-------|--------|-------------|------------|-------------|
| weight | weight | BigDecimal | DECIMAL(5,2) | nullable, positive |
| notes | notes | String | CLOB/TEXT | nullable |
| gender | gender | Gender (enum) | VARCHAR(20) | nullable, default 'UNKNOWN' |

### Validation Rules

- `weight`: Must be positive (> 0) if provided. Max 999.99.
- `microchip`: Unique across all pets (DB constraint + service validation for user-friendly error)
- `gender`: Must be one of MALE, FEMALE, UNKNOWN. Defaults to UNKNOWN if not set.

## New Entity: Gender (Enum)

```
MALE
FEMALE
UNKNOWN
```

Stored as STRING in database via `@Enumerated(EnumType.STRING)`.

## Relationships

No new relationships. All new fields are simple attributes on the existing `Pet` entity.

## State Transitions

- `active`: true → false (pet deactivated), false → true (pet reactivated)
- No other state machines.
