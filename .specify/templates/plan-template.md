# Implementation Plan: [FEATURE]

**Branch**: `[###-feature-name]` | **Date**: [DATE] | **Spec**: [link]

**Input**: Feature specification from `/specs/[###-feature-name]/spec.md`

## Summary

[Extract from feature spec: primary requirement + technical approach]

---

## Technical Context

**Language/Version**: [e.g., Java 17, Python 3.11 or NEEDS CLARIFICATION]
**Primary Dependencies**: [e.g., Spring Boot, Hibernate or NEEDS CLARIFICATION]
**Storage**: [e.g., H2, PostgreSQL or NEEDS CLARIFICATION]
**Testing**: [e.g., JUnit, Mockito or NEEDS CLARIFICATION]
**Target Platform**: [e.g., Web, Mobile or NEEDS CLARIFICATION]

**Project Type**: [e.g., web-service/mobile-app/desktop-app or NEEDS CLARIFICATION]
**Performance Goals**: [domain-specific or NEEDS CLARIFICATION]
**Constraints**: [domain-specific or NEEDS CLARIFICATION]
**Scale/Scope**: [domain-specific or NEEDS CLARIFICATION]

---

## Data Model

### New Entities

| Entity | Attributes | Relationships |
|--------|-----------|---------------|
| [EntityName] | [attr1: Type, attr2: Type] | [Relation to existing entity] |

### Modified Entities

| Entity | New Attributes |
|--------|---------------|
| [EntityName] | [+newField: Type] |

---

## API Design

### New Endpoints

| Method | Path | Description | Request | Response |
|--------|------|-------------|---------|----------|
| GET | /api/[resource] | List | - | [Response] |
| POST | /api/[resource] | Create | [Request] | [Response] |
| GET | /api/[resource]/{id} | Get by ID | - | [Response] |
| PUT | /api/[resource]/{id} | Update | [Request] | [Response] |
| DELETE | /api/[resource]/{id} | Delete | - | 204 |

---

## Implementation Phases

### Phase 1: Data Layer

- [ ] Create/modify entity: [EntityName]
- [ ] Create repository: [RepositoryName]
- [ ] Update schema.sql
- [ ] Update data.sql

**Files**: `src/main/java/.../model/`, `src/main/resources/db/`

### Phase 2: Service Layer

- [ ] Create service: [ServiceName]
- [ ] Add business logic
- [ ] Add validations
- [ ] Add error handling

**Files**: `src/main/java/.../service/`

### Phase 3: API Layer

- [ ] Create controller: [ControllerName]
- [ ] Create DTOs
- [ ] Add REST endpoints
- [ ] Add documentation

**Files**: `src/main/java/.../controller/`

### Phase 4: UI Layer (if applicable)

- [ ] Create/modify view: [ViewName]
- [ ] Create form
- [ ] Add styling

**Files**: `src/main/webapp/`, `src/main/resources/templates/`

### Phase 5: Testing

- [ ] Unit tests for service
- [ ] Integration tests for API
- [ ] UI tests (if applicable)

**Files**: `src/test/java/...`

---

## Dependencies

| Task | Depends On |
|------|------------|
| [TaskName] | [Dependency] |

---

## Risks & Open Questions

- [Risk/Question description]
