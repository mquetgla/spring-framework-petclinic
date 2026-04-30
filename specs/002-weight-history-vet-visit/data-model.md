# Data Model: Weight History & Vet-Visit Assignment

## New Entity: WeightRecord

| Field | Column | Type (Java) | Type (SQL) | Constraints |
|-------|--------|-------------|------------|-------------|
| id | id | int (from BaseEntity) | INTEGER PK AUTO | inherited |
| weight | weight | BigDecimal | DECIMAL(5,2) | NOT NULL, positive |
| measureDate | measure_date | LocalDate | DATE | NOT NULL |
| pet | pet_id | Pet (ManyToOne) | INTEGER FK → pets(id) | NOT NULL |

### Relationships

- **WeightRecord → Pet**: ManyToOne (required). Each weight record belongs to exactly one pet.
- **Pet → WeightRecord**: OneToMany (cascade ALL, mapped by pet). A pet has zero or more weight records.

### Validation Rules

- `weight`: Must be positive (> 0). Stored as DECIMAL(5,2) — max 999.99 kg.
- `measureDate`: Required. Defaults to current date.
- `pet`: Required FK. Cannot create orphan weight records.

---

## Modified Entity: Visit

| Field | Column | Type (Java) | Type (SQL) | Constraints |
|-------|--------|-------------|------------|-------------|
| vet | vet_id | Vet (ManyToOne) | INTEGER FK → vets(id) | nullable |

### Relationships

- **Visit → Vet**: ManyToOne (optional). A visit may have zero or one assigned vet.
- Existing Visit → Pet relationship unchanged.

### Validation Rules

- `vet`: Optional. NULL means no vet assigned.
- Existing `description` (NOT EMPTY) and `date` validations unchanged.

---

## Modified Entity: Pet

| Field | Column | Type (Java) | Type (SQL) | Constraints |
|-------|--------|-------------|------------|-------------|
| weightRecords | - (mapped by pet) | Set\<WeightRecord\> | - | OneToMany, cascade ALL |

No new columns on pets table. Collection is mapped via weight_records.pet_id FK.

---

## New Table: weight_records

```sql
CREATE TABLE weight_records (
  id           INTEGER PK,
  weight       DECIMAL(5,2) NOT NULL,
  measure_date DATE NOT NULL,
  pet_id       INTEGER NOT NULL FK → pets(id)
);
```
