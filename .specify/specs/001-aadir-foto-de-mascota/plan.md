# Implementation Plan: Añadir Foto De Mascota

**Branch**: `001-add-pet-photo` | **Date**: 2026-04-22 | **Spec**: [.specify/specs/001-add-pet-photo/spec.md](.specify/specs/001-add-pet-photo/spec.md)

**Input**: Feature specification from `.specify/specs/001-add-pet-photo/spec.md`

## Summary

Añadir campo photoUrl o photo (BLOB) a la entidad Pet. Implementar CRUD básico para gestionar la fotografía de cada mascota. El proyecto YA cuenta con un campo `photoUrl` existente (VARCHAR 255) en la entidad Pet, con soporte en schema.sql, formularios JSP y visualización en vistas. Se requiere verificar la implementación existente y añadir funcionalidades adicionales de upload de imagen si es necesario.

---

## Technical Context

**Language/Version**: Java 17 | Spring Framework 5.x (verificar versión exacta)
**Primary Dependencies**: Spring Boot, Spring MVC, Hibernate/JPA, H2 Database
**Storage**: H2 (desarrollo) - PostgreSQL (producción)
**Testing**: JUnit, Mockito, Spring Test
**Target Platform**: Web (JSP + Thymeleaf)
**Project Type**: Web Application (Pet Clinic Management)
**Performance Goals**: Carga rápida de imágenes, rendimiento BD óptimo
**Constraints**: Compatibilidad con configuración existente, soportar imágenes hasta X MB
**Scale/Scope**: Aplicación de gestión de clínica veterinaria multiusuario

---

## Data Model

### New Entities

| Entity | Attributes | Relationships |
|--------|-----------|---------------|
| PetImage (nueva entidad opcional) | id: Long, petId: Long, imageUrl: String, blobData: byte[], contentType: String, createdAt: Timestamp | ManyToOne con Pet |

### Modified Entities

| Entity | New Attributes |
|--------|---------------|
| Pet | [photoUrl ya existe - verificar] |

---

## API Design

### New Endpoints

| Method | Path | Description | Request | Response |
|--------|------|-------------|---------|----------|
| GET | /pet/photo/{petId} | Obtener foto de mascota | - | 200 OK (imagen) o 404 |
| POST | /pet/photo/{petId} | Subir foto de mascota | multipart/form-data | 200 OK |
| DELETE | /pet/photo/{petId} | Eliminar foto | - | 204 No Content |

---

## Implementation Phases

### Phase 1: Data Layer

- [x] Entidad Pet.java ya tiene campo photoUrl (verificar línea 63-64)
- [x] schema.sql ya tiene photo_url VARCHAR(255) (verificar línea 52)
- [ ] Verificar consistencia entre modelo y schema
- [ ] Añadir tabla PetImage si se requiere almacenar BLOB
- [ ] Actualizar data.sql con datos de prueba si es necesario

**Files**: `src/main/java/.../model/Pet.java`, `src/main/resources/db/h2/schema.sql`

### Phase 2: Service Layer

- [ ] Verificar ClinicService/ClinicServiceImpl actualice photoUrl
- [ ] Crear PetPhotoService para gestión de upload/delete
- [ ] Añadir validación de tipo de archivo (image/*)
- [ ] Añadir limitación de tamaño (ej: 5MB max)

**Files**: `src/main/java/.../service/`

### Phase 3: API Layer

- [ ] Crear PetPhotoController para endpoints REST
- [ ] Crear DTOs: PetPhotoRequest, PetPhotoResponse
- [ ] Mapear a endpoints existentes si se usa estilo RESTful
- [ ] Añadir documentación OpenAPI/Swagger

**Files**: `src/main/java/.../controller/`

### Phase 4: UI Layer (if applicable)

- [ ] Verificar ownerDetails.jsp muestre photoUrl
- [ ] Verificar createOrUpdatePetForm.jsp permita editar
- [ ] Añadir preview de imagen antes de guardar
- [ ] Añadir styling responsive para mobile

**Files**: `src/main/webapp/WEB-INF/jsp/`, `src/main/resources/templates/`

### Phase 5: Testing

- [ ] Unit tests para PetPhotoService
- [ ] Integration tests para PetPhotoController
- [ ] Tests de UI para formularios de foto
- [ ] Tests de validación de archivos

**Files**: `src/test/java/...`

---

## Dependencies

| Task | Depends On |
|------|------------|
| PetPhotoController | PetPhotoService, PetRepository |
| PetPhotoService | Pet entity, validation logic |
| UI photo upload forms | PetPhotoController endpoints |

---

## Risks & Open Questions

- [Pregunta] ¿Se usará photoUrl externo (URL) o almacenamiento BLOB en BD?
- [Pregunta] ¿Se requiere servir las imágenes directamente o vía CDN externo?
- [Pregunta] ¿Cuál es el límite máximo de tamaño de archivo?
- [Pregunta] ¿Se mantiene compatibilidad con implementación JDBC existente?
- [Riesgo] Almacenar imágenes grandes en BD puede afectar rendimiento - considerar almacenamiento externo

---

## References

| Resource | Location |
|----------|----------|
| Pet Entity | `src/main/java/.../model/Pet.java` |
| Schema H2 | `src/main/resources/db/h2/schema.sql` |
| Pet Form JSP | `createOrUpdatePetForm.jsp` |
| Owner Details JSP | `ownerDetails.jsp` |
| PetRepository | `.../repository/` |
| ClinicService | `.../service/` |
| PetController | `.../controller/` |