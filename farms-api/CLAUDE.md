# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository context

`farms-api` is one module inside the larger `nr-brmb-ast` monorepo (siblings: `farms-legacy`, `farms-liquibase`, `crunchy-postgres`, `farms-api-postman`, `load-tests`, `openshift`). This CLAUDE.md covers `farms-api` only — a Spring Boot 3 (Java 21) REST API, deployed as a WAR to Tomcat, for BC Gov's Farm Income Reference Margin Program (FARMS).

Database schema/DDL and stored procedures live in the sibling `../farms-liquibase` module, not here — consult it when a change requires new tables, columns, or PL/pgSQL procedures.

## Build & test

Maven settings live at the monorepo root (`../settings.xml`), not inside this directory — it configures the BC Gov Artifactory repositories (`repo.login`/`repo.password` credentials) that host the private `ca.bc.gov.brmb.common:*` and `ca.bc.gov.nrs.wforg:*` SNAPSHOT dependencies this project depends on. Builds/dependency resolution will fail without access to that Artifactory and valid credentials.

```bash
# from farms-api/, always pass the root settings.xml
mvn --settings=../settings.xml test                 # run all tests
mvn --settings=../settings.xml test -Dtest=InventoryItemDetailServiceTest      # single test class
mvn --settings=../settings.xml test -Dtest=InventoryItemDetailServiceTest#testCreate  # single test method
mvn --settings=../settings.xml clean package         # build the WAR
```

CI (`.github/workflows/farms-api-ci.yml` → `build-package.yml`) runs `mvn --settings=settings.xml --batch-mode --update-snapshots -f farms-api/pom.xml deploy` from the monorepo root, then builds/pushes a Docker image from `farms-api/Dockerfile` and deploys to OpenShift dev.

There is no standalone local run path: the app looks up its `DataSource` via JNDI (`java:comp/env/jdbc/farms_rest`, see `PersistenceConfig`), so `spring-boot:run` will fail outside a properly configured servlet container. `ServletInitializer` builds the WAR for deployment to Tomcat (see `Dockerfile`/`deploy-tools`).

### Tests

Tests are full `@SpringBootTest` + `MockMvc` integration tests (`@AutoConfigureMockMvc(addFilters = false)`), backed by a real Postgres via Testcontainers — no mocking of the persistence layer. Two test-scoped `@Configuration` classes under `src/test/java/ca/bc/gov/farms/` override the production ones:

- `PersistenceConfig` — starts a static `PostgreSQLContainer` and initializes it by running the SQL scripts in `src/test/resources/farms/` (schema, extensions, sequences, one `.sql` file per table, then `data.sql` for seed/fixture rows). When adding a new entity/table, add its DDL to `src/test/resources/farms/tables/` and seed rows to `data.sql` so tests can exercise it.
- `SecurityConfig` — replaces the Azure AD JWT decoder with one that accepts any token and injects a fake `sub` claim, since security filters are disabled in tests anyway.

Controller tests frequently use `@TestMethodOrder(MethodOrderer.OrderAnnotation.class)` with `@Order(n)` because create/update/delete tests chain off IDs created by earlier tests in the same class — keep that ordering in mind when adding new test methods.

## Architecture

Layered, one set of files per resource type: `Controller → Service → Mapper/Repository → Entity`, with a `ResourceAssembler` converting between persistence `Entity` objects and API `Rsrc` (resource/DTO) objects.

- **`controllers/`** — `@RestController`s, thin. Extend `common/controllers/CommonController` for standard `ResponseEntity` helpers (`ok()`, `notFound()`, `internalServerError()`, etc.). Catch `NotFoundException` → 404 and `RuntimeException` → 500; validation failures on `@Valid @RequestBody` surface as 400. Swagger/OpenAPI annotations (`@Operation`, `@ApiResponses`) are expected on every endpoint.
- **`services/`** — `@Component` business logic, one per resource, calling a mapper/repository, validating with `jakarta.validation.Validator`, and using the matching assembler to build the response. Write operations are `@Transactional`. Exceptions from mappers are wrapped in `ServiceException`; not-found lookups throw `NotFoundException` (both from `ca.bc.gov.brmb.common.service.api`, an external library — not defined in this repo).
- **`data/entities/`** — MyBatis-mapped POJOs (snake_case DB columns auto-mapped to camelCase fields via `map-underscore-to-camel-case: true` in `application.yaml`).
- **`data/mappers/`** — MyBatis `@Mapper` interfaces, one per entity, paired 1:1 with an XML file of the same name under `src/main/resources/mapper/`. This is the standard pattern for most CRUD entities.
- **`data/repositories/`** — hand-written `JdbcTemplate`-based repositories used instead of MyBatis where the SQL is dynamic:
  - `CodeRepository` implements a **generic code-table CRUD** pattern: `CodeService` holds an allow-list map (`codeNameMap`) from table name → code-column name for every `farm_*_codes` lookup table, and `CodeRepository` builds SQL against those table/column names after validating them against `SQL_IDENTIFIER_PATTERN` (defense against SQL injection since identifiers can't be parameterized). Adding a new code table means adding an entry to `CodeService.codeNameMap`, not writing a new mapper.
  - `ImportRepository`/`StagingRepository`/`Import*Repository` support the CSV import pipeline (see below).
- **`data/models/`** — API resource DTOs (`*Rsrc`, `*ListRsrc`), extending `BaseResource` from the external `brmb-common-rest-common` library (adds `@type`, HATEOAS-style `links`, etc.). Validation annotations live here.
- **`data/assemblers/`** — map `Entity → Rsrc` and `Rsrc → Entity`, and set self-links via `BaseResourceAssembler`. This is also where code-table lookups get resolved into the `*Desc` fields that ride alongside `*Code` fields on resources (e.g. `commodityTypeCode`/`commodityTypeDesc`).

### CSV import pipeline

`services/csv/` and `data/entities/staging/` implement a distinct subsystem for importing external file formats (`FIPD`/`Z01`–`Z99` fixed-format records — CRA/BPU/FMV/IVPR feeds) into staging tables before they're processed into the main schema. `FileHandle`/`BpuFileHandle`/`FmvFileHandle`/`IvprFileHandle` parse specific file types; `ImportClassCodes`/`ImportStateCodes` track import lifecycle state; `ImportService`/`ImportBPUService`/`ImportCRAService`/`ImportFMVService`/`ImportIVPRService` drive per-format import runs, orchestrated through `ImportController`.

### Security

`SecurityConfig` (production) requires an OAuth2/JWT bearer token (Azure AD, via `spring-cloud-azure-starter-active-directory`) on every request except `/checkHealth`. `CheckHealthController` (in `common/controllers/`) exposes a health endpoint used by `PersistenceConfig`'s `CompositeValidator`/`FarmsDatabaseCheckHealthValidator` beans, which check DB connectivity.

### External shared library

Many core types (`ServiceException`, `NotFoundException`, `BaseResource`, `MessageListRsrc`, `CheckHealthValidator`, `CompositeValidator`, etc.) come from the private `ca.bc.gov.brmb.common:*` artifacts declared in `pom.xml` — they are not part of this repo's source tree, so don't expect to find their definitions locally.
