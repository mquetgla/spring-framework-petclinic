# Requirement #1: Añadir foto de mascota

## Original Requirement
Añadir campo photoUrl o photo (BLOB) a la entidad Pet. Implementar CRUD básico para gestionar la fotografía de cada mascota.

**Categoría:** Baja

## Detailed Specification (from Code Analysis)

# Especificación: Gestión de Fotografía de Mascota

## 1. Título
**Añadir photoUrl o photo (BLOB) a la entidad Pet. Implementar CRUD básico para gestionar la fotografía de cada mascota.**

---

## 2. Análisis del Código Existente

### 2.1 Estado Actual
El proyecto **YA cuenta** con un campo `photoUrl` en la entidad Pet:

| Archivo | Campo existente |
|---------|-----------------|
| `src/main/java/.../model/Pet.java:63-64` | `photoUrl` (VARCHAR) |
| `src/main/resources/db/h2/schema.sql:52` | `photo_url VARCHAR(255)` |
| `createOrUpdatePetForm.jsp:31` | Input Photo URL |
| `ownerDetails.jsp:58-64` | Display de imagen |

### 2.2 Estructura JPA Actual
- **Entidad Pet**: extiende `NamedEntity` → `BaseEntity`
- **Relaciones**: ManyToOne con Owner, PetType; OneToMany con Visits
- **Repositorios**: PetRepository (interface), implementaciones JDBC/JPA/SpringDataJPA
- **Servicios**: ClinicService/ClinicServiceImpl (fachada)
- **Controladores**: PetController (MVC con JSP)

---

## 3. Especificación Técnica

### 3.1 Opción Elegida: photoUrl (URL externa) + soporte para BLOB opcional

** Recomendado usar `photoUrl` (VARCHAR) por:**
- Menor complejidad técnica
- Mejor rendimiento (BD no inflada)
- CDN externo posible
- Thymeleaf/JSP ya configurados para mostrar URLs

### 3.2 Entidades y Atributos

| Entidad | Atributo | Tipo | Notas |
|--------|----------|------|-------|
| Pet | photoUrl | String (255) | URL externa - ya existe |

### 3.3 Campos adicionales opcionales (si se requiere BLOB)

| Entidad | Atributo | Tipo | Notas |
|--------|----------|------|-------|
| Pet | photo | byte[] | BLOB para imagen |
| Pet | photoContentType | String | MIME type (image/jpeg, etc.) |

---

## 4. Servicios y Repositorios

### 4.1 Modificar archivos existentes

| Archivo | Acción |
|---------|--------|
| `model/Pet.java` | Ya tiene photoUrl - verificar setter/getter |
| `schema.sql` (todos) | Ya tiene photo_url - verificar consistencia |
| `PetRepository` | No requiere cambios |
| `ClinicService` | No requiere cambios |
| `JdbcPetRepositoryImpl.java:115` | Ya incluye photo_url en SQL |

### 4.2 Nuevos archivos a crear

| Archivo | Descripción |
|---------|-------------|
| `web/PetPhotoController.java` | Controlador para-upload/download de imagen |

---

## 5. Vistas/Formularios Existentes y Necesarios

### 5.1 Vistas existentes (ya implementadas)

| Vista | Estado | Descripción |
|-------|--------|-------------|
| `createOrUpdatePetForm.jsp` | ✅ Existe | Campo Photo URL input (línea 31) |
| `ownerDetails.jsp` | ✅ Existe | Display imagen (líneas 58-64) |

### 5.2 Nuevas vistas a crear

| Vista | Descripción |
|-------|-------------|
| `pets/uploadPhoto.jsp` | Formulario upload de archivo |

---

## 6. Endpoints API (REST)

### 6.1 Endpoints existentes
- GET `/owners/{ownerId}/pets/new` - Crear mascota (incluye photoUrl)
- POST `/owners/{ownerId}/pets/new` - Guardar mascota
- GET `/owners/{ownerId}/pets/{petId}/edit` - Editar mascota
- POST `/owners/{ownerId}/pets/{petId}/edit` - Actualizar mascota

### 6.2 Nuevos endpoints

| Método | URL | Descripción |
|--------|-----|-------------|
| GET | `/owners/{ownerId}/pets/{petId}/photo` | Ver foto de mascota |
| GET | `/owners/{ownerId}/pets/{petId}/photo/edit` | Formulario editar foto |
| POST | `/owners/{ownerId}/pets/{petId}/photo` | Subir nueva foto |
| DELETE | `/owners/{ownerId}/pets/{petId}/photo` | Eliminar foto |

---

## 7. Validaciones de Negocio

| Regla | Descripción |
|-------|-------------|
| photoUrl vacío | Permitido (nullable) |
| Formato URL | Validar formato HTTP/HTTPS |
| Tamaño máximo | 255 caracteres |
| Extensiones permitidas | jpg, jpeg, png, gif, webp |
| Tamaño archivo máx | 5MB (para BLOB) |

---

## 8. Consideraciones Técnicas Específicas

### 8.1 Configuración de seguridad
- Validar tipo MIME del archivo
- Sanitizar nombre de archivo
- Usar storage seguro (no guardar en BD directamente si es grande)

### 8.2 Paths estáticos
- Configurar `webjars` o carpeta `/resources/images/pets/`
- Agregar MIME types en web.xml si es necesario

### 8.3 Base de datos
- Todos los schema.sql ya tienen `photo_url`
- MySQL: `VARCHAR(255)` o `LONGTEXT` para URLs largas
- H2/PostgreSQL: `VARCHAR(255)` suficiente

---

## 9. Archivos a Modificar/Crear

### 9.1 Archivos a modificar

| Archivo | Cambios |
|---------|---------|
| `model/Pet.java` | Ya tiene photoUrl - verificar línea 100-106 |
| `schema.sql` (h2, mysql, postgresql, hsqldb) | Ya tiene photo_url - verificar consistencia |
| `createOrUpdatePetForm.jsp` | Ya tiene campo - verificar label |
| `ownerDetails.jsp` | Ya tiene imagen - mejorar estilos |
| service/ClinicService.java | No requiere cambios |
| repository/PetRepository.java | No requiere cambios |
| repository/jdbc/JdbcPetRepositoryImpl.java | Ya tiene photoUrl (línea 115) |

### 9.2 Archivos a crear

| Archivo | Descripción |
|---------|-------------|
| `web/PetPhotoController.java` | Controlador upload/download |
| `pets/uploadPhoto.jsp` | Vista upload de imagen |
| `service/PetPhotoService.java` | Servicio de lógica de foto |
| `service/PetPhotoServiceImpl.java` | Implementación |

---

## 10. resumen de Implementación

El proyecto Spring PetClinic **ya tiene implementado**:

1. ✅ Campo `photoUrl` en entidad Pet
2. ✅ Columna `photo_url` en todas las bases de datos
3. ✅ Input field en formulario de mascota
4. ✅ Display de imagen en ownerDetails

**Faltante por implementar**:

1. ⬜ Endpoint para upload de imagen (no solo URL)
2. ⬜ Guardar imagen en servidor/disco
3. ⬜ Opción de BLOB si se requiere almacenamiento binario

---

## 11. Recomendación

Dados los requisitos "CRUD básico", se recomienda:

1. **Mantener photoUrl** actual con validación URL
2. **Agregar funcionalidad de upload** usando StorageService
3. **Persistir archivos** en directorio `/resources/uploads/pets/`
4. **Exponer endpoint** REST para integración futura

Esta especificación permite cumplir con el requisito de gestionar fotografías de mascotas mediante las opciones existentes y mejoras incrementales.

## Context

This requirement should be implemented in the Spring PetClinic application.

### Existing Features
- Pet types (Dog, Cat, Bird, Rabbit, Hamster)
- Owners and their pets
- Visits and medical history
- Veterinarians and specialties
- Spring Data JPA with H2 database

### Technical Stack
- Spring Boot
- Spring Data JPA (Hibernate)
- Thymeleaf templates
- Bootstrap CSS
- Maven build system
- H2 in-memory database (default)

### Database Conventions
- Use JPA entities with proper relationships (@OneToMany, @ManyToOne, etc.)
- Follow existing naming conventions (camelCase for fields, PascalCase for classes)
- Use @Transactional for service layer
- Follow REST conventions for APIs
- Update schema.sql and data.sql for any schema changes

---

*Generated by Phase 1: Requirements*
