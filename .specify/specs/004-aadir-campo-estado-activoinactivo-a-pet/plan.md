# Implementation Plan: 004-añadir-campo-estado-activoinactivo-a-pet

**Branch**: `004-añadir-campo-estado-activoinactivo-a-pet` | **Date**: 2026-04-27 | **Spec**: [.specify/specs/004-añadir-campo-estado-activoinactivo-a-pet/spec.md](./.specify/specs/004-añadir-campo-estado-activoinactivo-a-pet/spec.md)

**Input**: Feature specification from `.specify/specs/004-añadir-campo-estado-activoinactivo-a-pet/spec.md`

## Summary

**Primary Requirement**: Añadir campo `active` (boolean) en la entidad `Pet` para marcar mascotas fallecidas o transferidas, permitiendo filtrar y visualizar el estado de cada mascota.

**Technical Approach**: Extender la entidad `Pet` existente con un campo booleano `active` con valor por defecto `true`, existente en todas las capas: modelo, persistencia, servicio y UI.

---

## Technical Context

| Contexto | Valor |
|----------|-------|
| **Language/Version** | Java 17 |
| **Primary Dependencies** | Spring Boot 3.x, Spring Data JPA, Hibernate |
| **Storage** | H2 (desarrollo), PostgreSQL, MySQL, HSQLDB (todos configurados) |
| **Testing** | JUnit 5, Mockito |
| **Target Platform** | Web (JSP + Spring MVC) |

| Aspecto | Detalle |
|---------|---------|
| **Project Type** | Web Application (Pet Clinic Management) |
| **Performance Goals** | N/A para este cambio sencillo |
| **Constraints** | Compatibilidad con BD existentes |
| **Scale/Scope** | Bajo - Campo único en entidad existente |

---

## Data Model

### ✅ New Entities
**Ninguno** - El campo se añade a entidad existente.

### Modified Entities

| Entity | New Attributes | Status |
|--------|----------------|--------|
| `Pet` | `active: boolean (default=true)` | ✅ Ya implementado |

**Detalle del atributo existente:**
```java
@Column(name = "active")
private boolean active = true;

public boolean isActive() {
    return this.active;
}

public void setActive(boolean active) {
    this.active = active;
}
```

---

## API Design

### Existing Endpoints (No changes required)

| Method | Path | Description |
|--------|------|-------------|
| GET | `/owners/{ownerId}` | Ver detalle owner + mascotas (incluye `active`) |
| GET | `/owners/{ownerId}/pets/new` | Formulario nueva mascota |
| POST | `/owners/{ownerId}/pets/new` | Crear mascota (con `active` por defecto) |
| GET | `/owners/{ownerId}/pets/{petId}/edit` | Formulario editar mascota |
| POST | `/owners/{ownerId}/pets/{petId}/edit` | Actualizar mascota (incluye `active`) |

---

## Implementation Phases

### Phase 1: Data Layer ✅ COMPLETADO

- [x] Entidad `Pet.java` - Campo `active` con getter/setter
- [x] Schema H2 - Columna `active BOOLEAN DEFAULT TRUE`
- [x] Schema PostgreSQL - Columna `active BOOLEAN DEFAULT TRUE`
- [x] Schema MySQL - Columna `active BOOLEAN DEFAULT TRUE`
- [x] Schema HSQLDB - Columna `active BOOLEAN DEFAULT TRUE`
- [x] Data SQL - Todos los INSERT incluyen `TRUE` para `active`

**Files**: 
- `src/main/java/org/springframework/samples/petclinic/model/Pet.java`
- `src/main/resources/db/h2/schema.sql`
- `src/main/resources/db/postgresql/schema.sql`
- `src/main/resources/db/mysql/schema.sql`
- `src/main/resources/db/hsqldb/schema.sql`
- `src/main/resources/db/h2/data.sql`

### Phase 2: Service Layer ✅ COMPLETADO

- [x] `ClinicService` - Métodos existentes (`savePet`, `findPetById`) procesan `active`
- [x] No requiere cambios adicionales

**Files**: 
- `src/main/java/org/springframework/samples/petclinic/service/ClinicService.java`

### Phase 3: Controller Layer ✅ COMPLETADO

- [x] `PetController` - Formularios procesan campo `active`
- [x] Binding automático vía Spring MVC

**Files**: 
- `src/main/java/org/springframework/samples/petclinic/web/PetController.java`

### Phase 4: UI Layer ✅ COMPLETADO

- [x] `createOrUpdatePetForm.jsp` - Checkbox "Active" (líneas 59-63)
- [x] `ownerDetails.jsp` - Muestra badge de estado (líneas 75-85)

**Files**: 
- `src/main/webapp/WEB-INF/jsp/pets/createOrUpdatePetForm.jsp`
- `src/main/webapp/WEB-INF/jsp/owners/ownerDetails.jsp`

### Phase 5: Testing ⚠️ PENDIENTE

**Unit Tests:**
- [ ] `PetTests.java` - Test para getter/setter de `active`
- [ ] `PetControllerTests.java` - Añadir test con parámetro `active`

**Integration Tests:**
- [ ] Test de persistencia del campo `active`
- [ ] Test de formulario con estado `active=false`

**Files**: 
- `src/test/java/org/springframework/samples/petclinic/model/PetTests.java`
- `src/test/java/org/springframework/samples/petclinic/web/PetControllerTests.java`

---

## Dependencies

| Task | Depends On | Status |
|------|------------|--------|
| Tests | Implementación existente | ✅ Completo |
| Repository extension (opcional) | Implementación existente | ⏳ Pendiente |

---

## Risks & Open Questions

| # | Risk/Question | Mitigation |
|---|---------------|------------|
| 1 | **Mascotas existentes en BDD**: Todas las mascotas existentes tienen `active=TRUE` por default | ✅ Correcto -正好 no hay mascotas inactivas |
| 2 | **Filtrar mascotas inactivas**: ¿Se deben ocultar mascotas inactivas en la vista del owner? | **Decisión requerida**: Actualmente muestra todas; agregar filtro es opcional |
| 3 | **Visitas de mascotas inactivas**: ¿Las visitas de mascotas inactivas deben mostrarse? | **Decisión requerida**: Actualmente sí se muestran |
| 4 | **Repository queries**: ¿Necesitamos método `findActivePets()` o `findInactivePets()`? | **Decisión requerida**: Opcional para versiones futuras |

---

## Code Examples

### Pet.java - Campo existente (líneas 75-84)
```java
@Column(name = "active")
private boolean active = true;

public boolean isActive() {
    return this.active;
}

public void setActive(boolean active) {
    this.active = active;
}
```

### createOrUpdatePetForm.jsp - Checkbox (líneas 59-63)
```jsp
<div class="form-group">
    <div class="col-sm-offset-2 col-sm-10">
        <form:checkbox path="active" label="Active"/>
    </div>
</div>
```

### ownerDetails.jsp - Badge de estado (líneas 75-85)
```jsp
<dt>Active</dt>
<dd>
    <c:choose>
        <c:when test="${pet.active}">
            <span class="label label-success">Yes</span>
        </c:when>
        <c:otherwise>
            <span class="label label-danger">No</span>
        </c:otherwise>
    </c:choose>
</dd>
```

### Schema H2 - pets table (línea 56)
```sql
CREATE TABLE pets (
  id         INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name       VARCHAR(30),
  birth_date DATE,
  type_id    INTEGER NOT NULL,
  owner_id   INTEGER NOT NULL,
  photo_url  VARCHAR(255),
  microchip_id VARCHAR(255) UNIQUE,
  color     VARCHAR(30),
  breed      VARCHAR(50),
  active     BOOLEAN DEFAULT TRUE  -- ✅ NUEVO CAMPO
);
```

### Data SQL - INSERT con active (ejemplo línea 36)
```sql
INSERT INTO pets VALUES (default, 'Leo', '2010-09-07', 1, 1, NULL, NULL, NULL, NULL, TRUE);
```

---

## Verificación de Implementación

```bash
# Compilar proyecto
./mvnw compile

# Ejecutar tests
./mvnw test

# Verificar que H2 schema tiene la columna
grep -n "active" src/main/resources/db/h2/schema.sql

# Verificar que Pet.java tiene el campo
grep -n "active" src/main/java/org/springframework/samples/petclinic/model/Pet.java
```

---

## Estimación de Esfuerzo

| Phase | Effort | Notes |
|-------|--------|-------|
| Data Layer | 0h (completado) | Schema y entidad ya implementados |
| Service Layer | 0h (completado) | Sin cambios necesarios |
| Controller Layer | 0h (completado) | Binding automático |
| UI Layer | 0h (completado) | JSPs ya actualizados |
| **Testing** | **2-3h** | Unit tests + integración |
| **Total** | **2-3h** | Solo testing pendiente |