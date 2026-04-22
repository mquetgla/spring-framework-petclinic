# Definition: 001_aadir_foto_de_mascota

## Source Requirement
[001_aadir_foto_de_mascota.md](001_aadir_foto_de_mascota.md)

---

Voy a analizar el código existente para crear un plan de implementación preciso. Primero exploraré los archivos relevantes.
Basándome en el análisis del código existente, genero el siguiente plan:

---

# PLAN DE IMPLEMENTACIÓN #001: Gestión de Fotografía de Mascota

## 1. FUNCTIONAL SPECIFICATION REVIEW

### 1.1 Estado Actual Confirmado

| Componente | Estado | Ubicación |
|------------|--------|------------|
| Entidad Pet.photoUrl | ✅ Implementado | `Pet.java:63-64` |
| Columna photo_url | ✅ Implementado | `schema.sql:52` |
| Input Photo URL | ✅ Implementado | `createOrUpdatePetForm.jsp:31` |
| Display imagen | ✅ Implementado | `ownerDetails.jsp:58-64` |

### 1.2 Funcionalidades Pendientes

| Requisito | Descripción | Prioridad |
|----------|------------|----------|
| F1 | Upload de archivo de imagen (no solo URL) | Alta |
| F2 | Almacenamiento en sistema de archivos | Alta |
| F3 | Endpoint REST para gestión de foto | Media |
| F4 | Servicio de almacenamiento (StorageService) | Alta |
| F5 | Validación de tipo MIME y tamaño | Media |

### 1.3 Criterios de Aceptación

- [ ] Subir imagen de mascota mediante formulario web
- [ ] Almacenar imagen en `/resources/static/uploads/pets/`
- [ ] Actualizar photoUrl con ruta relativa tras upload
- [ ] Eliminar foto existente
- [ ] Validar extensiones: jpg, jpeg, png, gif, webp
- [ ] Tamaño máximo: 5MB
- [ ] Endpoint REST GET/POST/DELETE para foto

---

## 2. TECHNICAL SPECIFICATION

### 2.1 Diseño de Datos

**Entidad Pet** (ya existe - no requiere cambios):
| Atributo | Tipo | Nullable | Descripción |
|----------|-----|----------|-------------|
| photoUrl | String(255) | true | URL o ruta relativa de la foto |

**Tabla pets** (ya existe - no requiere cambios):
```sql
photo_url VARCHAR(255) -- ya existe en todos los schema.sql
```

### 2.2 Diseño de API REST

| Método | Endpoint | Descripción | Request | Response |
|-------|----------|------------|---------|----------|
| GET | `/owners/{ownerId}/pets/{petId}/photo` | Descargar imagen | - | image/* (200) |
| POST | `/owners/{ownerId}/pets/{petId}/photo` | Subir imagen | multipart/form-data | 200 (redirect) |
| DELETE | `/owners/{ownerId}/pets/{petId}/photo` | Eliminar imagen | - | 200 (redirect) |

### 2.3 Diseño de Servicios

**PetPhotoStorageService** (nuevo):
```java
public interface PetPhotoStorageService {
    String store(MultipartFile file, Integer petId) throws StorageException;
    Resource getAsResource(String filename);
    void delete(String filename) throws StorageException;
    boolean deleteByPetId(Integer petId);
}
```

### 2.4 Diseño de Seguridad

- Validar tipo MIME (`image/jpeg`, `image/png`, `image/gif`, `image/webp`)
- Sanitizar nombre de archivo
- Limitar tamaño a 5MB
- Directorio de almacenamiento: `/resources/static/uploads/pets/`

---

## 3. UX/UI DESIGN

### 3.1 Vistas Existentes a Mejorar

| Vista | Cambios |
|-------|---------|
| `ownerDetails.jsp` | Mejorar estilo de imagen: borde redondeado, tamaño consistente |
| `createOrUpdatePetForm.jsp` | Añadir hint de URL válida |

### 3.2 Nuevas Vistas

| Vista | Descripción |
|-------|-------------|
| `pets/uploadPetPhoto.jsp` | Formulario de upload con preview |

### 3.3 Estados UI

| Estado | Descripción |
|--------|-------------|
| Sin foto | Placeholder "No photo available" |
| Cargando | Spinner durante upload |
| Error | Mensaje de error de validación |
| Éxito | Preview de imagen subida |

---

## 4. IMPLEMENTATION PLAN

### Phase 1: Data Layer
**No requiere cambios** - la entidad Pet y schemas ya tienen photoUrl.

| Archivo | Acción | Dependencias |
|---------|--------|--------------|
| - | Ninguna | - |

---

### Phase 2: Service Layer

#### 2.1 `PetPhotoStorageService.java` (CREAR)
- **Ruta**: `src/main/java/org/springframework/samples/petclinic/service/PetPhotoStorageService.java`
- **Descripción**: Interfaz para gestión de almacenamiento de fotos
- **Métodos**:
  - `store(MultipartFile, Integer)` → String (ruta relativa)
  - `getAsResource(String)` → Resource
  - `delete(String)` → void
  - `deleteByPetId(Integer)` → boolean

#### 2.2 `PetPhotoStorageServiceImpl.java` (CREAR)
- **Ruta**: `src/main/java/org/springframework/samples/petclinic/service/PetPhotoStorageServiceImpl.java`
- **Descripción**: Implementación con almacenamiento en sistema de archivos
- **Configuración**:
  - Directorio: `uploads/pets/` (classpath resources)
  - Nombre archivo: `pet-{petId}-{timestamp}.{ext}`
- **Dependencias**: Ninguna

---

### Phase 3: API Layer

#### 3.1 `PetPhotoController.java` (CREAR)
- **Ruta**: `src/main/java/org/springframework/samples/petclinic/web/PetPhotoController.java`
- **Descripción**: Controlador REST/MVC para gestión de fotos
- **Endpoints**:
  - `GET /owners/{ownerId}/pets/{petId}/photo` - Mostrar/formulario edit
  - `POST /owners/{ownerId}/pets/{petId}/photo` - Upload
  - `DELETE /owners/{ownerId}/pets/{petId}/photo` - Eliminar
- **Dependencias**: PetPhotoStorageService, PetRepository, ClinicService

#### 3.2 `PetController.java` (MODIFICAR)
- **Ruta**: `src/main/java/org/springframework/samples/petclinic/web/PetController.java`
- **Cambios**: Añadir redirect tras update para mantener photoUrl
- **Dependencias**: Ninguna

---

### Phase 4: UI Layer

#### 4.1 `pets/uploadPetPhoto.jsp` (CREAR)
- **Ruta**: `src/main/webapp/WEB-INF/jsp/pets/uploadPetPhoto.jsp`
- **Descripción**: Formulario de upload de imagen
- **Componentes**:
  - Input file (accept="image/*")
  - Preview de imagen actual
  - Botón upload/eliminar
- **Dependencias**: PetPhotoController

#### 4.2 `ownerDetails.jsp` (MODIFICAR)
- **Ruta**: `src/main/webapp/WEB-INF/jsp/owners/ownerDetails.jsp`
- **Cambios**: Mejorar visualización de imagen (líneas 58-64)
  - Añadir clase CSS `.pet-photo`
  - Estilo: border-radius, box-shadow, tamaño fijo 150x150
  - Enlace "Edit Photo" a `/owners/{ownerId}/pets/{petId}/photo`
- **Dependencias**: PetPhotoController

---

### Phase 5: Configuration

#### 5.1 `PetClinicProperties.java` (CREAR/MODIFICAR)
- **Ruta**: `src/main/java/org/springframework/samples/petclinic/config/PetClinicProperties.java`
- **Cambios**: Añadir configuración de storage
```java
@NestedConfigurationProperty
private StorageProperties storage = new StorageProperties();
```

#### 5.2 `application.properties` (MODIFICAR)
- **Ruta**: `src/main/resources/application.properties`
- **Añadir**:
```properties
petclinic.storage.upload-dir=uploads/pets
petclinic.storage.max-file-size=5MB
petclinic.storage.allowed-extensions=jpg,jpeg,png,gif,webp
```

#### 5.3 Spring MVC Configuration (MODIFICAR)
- **Ruta**: `WebConfig.java` o crear nuevo
- **Cambios**: Configurar multipart resolver (10MB max)

---

### Phase 6: Testing

#### 6.1 Tests Unitarios
| Test | Descripción |
|------|-------------|
| `PetPhotoStorageServiceImplTests` | Tests de store, get, delete |

#### 6.2 Tests de Integración
| Test | Descripción |
|------|-------------|
| `PetPhotoControllerTests` | Tests de endpoints |

---

### Phase 7: Documentation

#### 7.1 Archivos a Documentar
| Archivo | Actualización |
|---------|---------------|
| `README.md` | Documentar feature de foto |
| `API.md` | Endpoints de PetPhoto |

---

## 5. RESUMEN DE ARCHIVOS

### 5.1 Archivos a Crear
| # | Archivo | Descripción |
|---|---------|-------------|
| 1 | `service/PetPhotoStorageService.java` | Interfaz storage |
| 2 | `service/PetPhotoStorageServiceImpl.java` | Implementación storage |
| 3 | `web/PetPhotoController.java` | Controlador foto |
| 4 | `pets/uploadPetPhoto.jsp` | Vista upload |

### 5.2 Archivos a Modificar
| # | Archivo | Cambios |
|---|---------|--------|
| 1 | `ownerDetails.jsp` | Mejorar display foto + enlace edit |
| 2 | `application.properties` | Config storage |
| 3 | `PetClinicProperties.java` | Añadir config storage |
| 4 | `PetController.java` | Ajuste redirects |
| 5 | `WebConfig.java` | Config multipart |

### 5.3 Archivos Sin Cambios (confirmados)
- `Pet.java` - Ya tiene photoUrl
- `schema.sql` (todos) - Ya tiene photo_url
- `createOrUpdatePetForm.jsp` - Ya tiene input photoUrl
- `PetRepository.java` - No requiere cambios
- `ClinicService.java` - No requiere cambios

---

## 6. ESTIMACIÓN

| Phase | Complejidad | Tiempo Estimado |
|-------|------------|----------------|
| Data Layer | N/A | 0 |
| Service Layer | Media | 2h |
| API Layer | Baja | 1h |
| UI Layer | Baja | 1h |
| Configuration | Baja | 30min |
| Testing | Media | 2h |
| **Total** | - | **~6.5 horas** |

---

*Generated by Phase 2: Definition - 2026-04-22 09:58:24*
