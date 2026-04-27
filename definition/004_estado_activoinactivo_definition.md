# Definition: 004_estado_activoinactivo

## Source Requirement
[004_estado_activoinactivo.md](../requirements/004_estado_activoinactivo.md)

---

# Implementation Plan: 004 Estado Activo/Inactivo

**Branch**: `004-active-status` | **Date**: 2026-04-27 | **Spec**: No disponible (usar requisito original)

**Input**: Requirement #4 - Campo active (boolean) para marcar mascotas fallecidas o transferidas

---

## Summary

Agregar campo booleano `active` a la entidad Pet para permitir marcar mascotas como inactivas (fallecidas o transferidas). El campo ya existe en la entidad (default true). Se requiere: (1) agregar control UI en formulario de mascotas, (2) filtrar mascotas inactivas en vistas de propietario, (3) añadir validaciones y tests específicos para el nuevo comportamiento.

---

## Technical Context

**Language/Version**: Java 17
**Primary Dependencies**: Spring Boot 3.x, Spring Data JPA, Jakarta Persistence
**Storage**: H2 (desarrollo), PostgreSQL/MySQL (producción)
**Testing**: JUnit 5, Mockito, Spring MVC Test
**Target Platform**: Web (JSP/Thymeleaf)

**Project Type**: Web application (Spring MVC)
**Performance Goals**: Minimal - field addition has no performance impact
**Constraints**: Mantener compatibilidad con datos existentes (default TRUE)
**Scale/Scope**: Single entity modification, low impact change

---

## Data Model

### New Entities
*Ninguna nueva entidad requerida*

### Modified Entities

| Entity | New Attributes | Notes |
|--------|----------------|-------|
| Pet | +active: Boolean | Ya existe en código (línea 75-76), defaults a `true` |

---

## API Design

### New Endpoints
*No aplica* - Proyecto usa Spring MVC con vistas JSP, no REST API

| Método | Path | Descripción | Request | Response |
|--------|------|-------------|---------|----------|
| GET | /owners/{ownerId} | Lista mascotas | - | Vista con mascotas activas/inactivas |
| GET | /owners/{ownerId}/pets/{petId}/edit | Editar mascota | - | Formulario con toggle active |
| POST | /owners/{ownerId}/pets/{petId}/edit | Guardar cambios | Form con campo active | Redirect |

---

## Implementation Phases

### Phase 1: Data Layer
- [x] Entidad Pet ya tiene campo `active: Boolean` (línea 75-76)
- [x] Schema H2 ya incluye columna `active BOOLEAN DEFAULT TRUE` (línea 56)
- [ ] Verificar schema PostgreSQL incluye columna active
- [ ] Verificar schema MySQL incluye columna active
- [ ] Revisar data.sql para datos de prueba existentes

**Files**: 
- `src/main/java/org/springframework/samples/petclinic/model/Pet.java`
- `src/main/resources/db/h2/schema.sql`
- `src/main/resources/db/postgresql/schema.sql`
- `src/main/resources/db/mysql/schema.sql`

### Phase 2: Service Layer
- [x] ClinicService.savePet() ya persiste el campo active
- [ ] Agregar método en ClinicService para filtrar mascotas activas
- [ ] Agregar método en ClinicService para marcar mascota como inactiva
- [ ] Añadir validaciones de negocio (ej: no permitir visitas en mascotas inactivas)

**Files**: 
- `src/main/java/org/springframework/samples/petclinic/service/ClinicService.java`
- `src/main/java/org/springframework/samples/petclinic/service/ClinicServiceImpl.java`

### Phase 3: API Layer
*N/A* - Spring MVC tradicional, no requiere cambios en API REST

### Phase 4: UI Layer
- [ ] Modificar formulario `createOrUpdatePetForm` para incluir checkbox de estado active
- [ ] Agregar indicador visual en lista de mascotas para mascotas inactivas (estilo tachado/gris)
- [ ] Ocultar/mostrar mascotas inactivas según filtro en vista de propietario
- [ ] Añadir botón para marcar como transferida/fallecida

**Files**:
- `src/main/webapp/WEB-INF/jsp/pets/createOrUpdatePetForm.jsp`
- `src/main/webapp/WEB-INF/jsp/owners/ownerDetails.jsp`
- `src/main/webapp/WEB-INF/jsp/owners/createOrUpdateOwnerForm.jsp`

### Phase 5: Testing
- [ ] Unit test: PetTests para verificar default value y setters/getters
- [ ] Unit test: ClinicServiceImpl tests para método filterActive
- [ ] Integration test: PetControllerTests para verify formulario con active
- [ ] UI test: Verificar visualización de mascotas inactivas
- [ ] Edge case: Intentar agregar visita a mascota inactiva

**Files**:
- `src/test/java/org/springframework/samples/petclinic/model/PetTests.java`
- `src/test/java/org/springframework/samples/petclinic/web/PetControllerTests.java`
- `src/test/java/org/springframework/samples/petclinic/service/ClinicServiceTests.java`

---

## Dependencies

| Task | Depends On |
|------|------------|
| UI Layer (Phase 4) | Data Layer (Phase 1) - verificar schema |
| Testing (Phase 5) | UI Layer (Phase 4) - para tests UI |
| Service methods | Data Layer (Phase 1) |

---

## Risks & Open Questions

1. **Visitas en mascotas inactivas**: ¿Debe permitirse agregar visitas a mascotas marcadas como inactivas (fallecidas)? El negocio puede requerir阻止 esto.
2. **Filtro por defecto**: ¿Las mascotas inactivas deben ocultarse por defecto en la lista de propietarios o mostrarse con filtro?
3. **Migración de datos**: ¿Hay datos existentes que deban marcarse como inactivos? Verificar si hay mascotas que representan transferencias/fallecimientos.
4. **Historial**: ¿Se debe mantener historial de cambios de estado (quién, cuándo, razón)?
5. **Compatibilidad**: Verificar que el campo active no rompe ninguna query existente en PostgreSQL/MySQL (H2 ya lo soporta).
```

---

## Resumen de Hallazgos

✅ **El campo `active` ya está implementado en:**
- Entidad Pet.java (líneas 75-76)
- Schema H2 (línea 56)

❌ **Pendiente de implementar:**
- UI: Checkbox en formulario
- UI: Indicador visual en listas
- UI: Filtro de mascotas activas/inactivas
- Service: Métodos de filtrado
- Testing: Tests específicos
- Schema: PostgreSQL y MySQL

---

*Generated by Phase 2: Definition - 2026-04-27 10:07:57*
