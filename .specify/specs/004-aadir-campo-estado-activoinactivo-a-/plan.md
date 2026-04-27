Voy a generar el Implementation Plan completo para el requisito 004. Primero verifico la especificación existente:
Veo que el campo `active` ya existe en la entidad. Verifico los servicios y capas restantes:
Verifico los otros esquemas de base de datos:
Verifico la vista de detalle del Owner para ver si se muestran las mascotas:
Ahora tengo toda la información necesaria. El análisis revela que **el campo `active` ya está parcialmente implementado** en la mayoría de las capas. Procedo a generar el Implementation Plan completo:

---

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
## ✅ Implementation Plan Generado

He creado el **Implementation Plan** completo en:
> `.specify/specs/004-aadir-campo-estado-activoinactivo-a-pet/plan.md`

### 📊 Hallazgo Importante

**El requisito #004 ya está IMPLEMENTADO** en la mayoría de las capas:

| Capa | Archivo | Estado |
|------|---------|--------|
| **Modelo** | `Pet.java` (líneas 75-84) | ✅ Campo `active` con getter/setter |
| **Schema H2** | `schema.sql` (línea 56) | ✅ Columna `active BOOLEAN DEFAULT TRUE` |
| **Schema PostgreSQL** | `schema.sql` (línea 69) | ✅ Columna `active BOOLEAN DEFAULT TRUE` |
| **Schema MySQL** | `schema.sql` (línea 54) | ✅ Columna `active BOOLEAN DEFAULT TRUE` |
| **Schema HSQLDB** | `schema.sql` (línea 56) | ✅ Columna `active BOOLEAN DEFAULT TRUE` |
| **Data SQL** | `data.sql` | ✅ Todos los INSERT incluyen `TRUE` |
| **UI - Formulario** | `createOrUpdatePetForm.jsp` (líneas 59-63) | ✅ Checkbox "Active" |
| **UI - Detalle** | `ownerDetails.jsp` (líneas 75-85) | ✅ Badge de estado |
| **Controller** | `PetController.java` | ✅ Binding automático |
| **Service** | `ClinicService.java` | ✅ Sin cambios necesarios |

### ⚠️ Trabajo Pendiente

**Solo quedan los tests:**

| Test | Descripción |
|------|-------------|
| `PetTests.java` | Unit tests para getter/setter de `active` |
| `PetControllerTests.java` | Tests con parámetro `active` |

**Esfuerzo estimado**: 2-3 horas (solo testing).