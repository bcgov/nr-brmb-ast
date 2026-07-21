# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

`farms-legacy` is the BC Government "FARM" application (WebADE acronym `FARM`) — a Struts 1.2 Java web app implementing an AgriStability/AgriInvest-style farm income-stabilization program: calculating whole-farm reference margins and benefit payments from client-submitted farm income/expense data, plus enrolment, reasonability/risk testing, verification workflows, TIPS (Towards Increased Profits) reporting, and reference-code management.

It is one component of the larger `nr-brmb-ast` repo (siblings: `farms-api`, `farms-liquibase`, `openshift`, `crunchy-postgres`). This directory is its own Maven module/war and its own git checkout context for Claude Code.

## Build & run

- Build: `mvn package` (or `mvn install`) — produces `target/farms-legacy-1.0-SNAPSHOT.war`. No `<build>` section in `pom.xml`; packaging relies on Maven defaults (`maven-war-plugin` defaults, Java 8 source/target).
- Tests are **skipped by default** (`<skipTests>true</skipTests>` in `pom.xml`). Run tests explicitly with `mvn test -DskipTests=false`.
- Run a single test class: `mvn test -DskipTests=false -Dtest=ClassName`.
- Framework: JUnit 5 (Jupiter). DAO tests (e.g. `TestUserDAO`) are integration-style against a real DB connection via `TestTransactionProvider`, and several are `@Disabled`. Calculator/reasonability/FMV/CHEFS tests are fixture-driven, comparing calculator output against pre-computed JSON scenarios in `src/test/resources/data/{benefit/calc2020,calc2021,reasonability/expense,fmv,chefs}`.
- Local dev: IntelliJ SmartTomcat plugin config lives in `../.smarttomcat/farms-legacy/` (sibling to this module, at the `nr-brmb-ast` repo root) — that's how the app is normally run locally against Tomcat, not via a Maven plugin.
- Deployment target: Tomcat 9 / Java 8, containerized (`Dockerfile`) for OpenShift. `deploy-tools/` holds the runtime `context.xml` (JNDI Postgres datasource), `server.xml`, `setenv.sh`, and `log4j2.xml` that get copied into the image at build time.
- Database: PostgreSQL, accessed via a container-managed JNDI datasource (`jdbc/${POSTGRES_RESOURCE_NAME}`, configured in `deploy-tools/context.xml`). No connection pooling logic in the app itself — it's all Tomcat's DBCP/pool.

## Architecture

### Request flow (Struts 1.2)
Classic Struts: `web.xml` maps `*.do` to `org.apache.struts.action.ActionServlet`, configured from `src/main/webapp/WEB-INF/struts-config.xml` (~319 action mappings). Action classes live under `src/main/java/ca/bc/gov/srm/farm/ui/struts/`, organized by domain subpackage (`account`, `agent`, `benefit/triage`, `calculator/*`, `chefs`, `codes/*`, `dataimport`, `enrolment`, `export`, `newparticipant`, `report`, `tipreport`, `transfer`, `welcome`). Most actions extend `SecureAction`, whose `execute()` resolves a configured "secure action" name and calls `FarmSecurityUtils.canPerformAction()` (WebADE authorization) before delegating to the subclass's `doExecute(...)`, which returns an `ActionForward` to a JSP (`src/main/webapp/WEB-INF/jsp/...`, tiles-based layout). Some newer actions (e.g. under `chefs`) instead build a JSON string on the ActionForm for the JSP to embed client-side, rather than returning a clean REST response — there is no server-side JAX-RS/annotated REST API in this app.

### Auth — two chained mechanisms
Every `*.do` request passes through both, in order (see `web.xml` filter-mappings):
1. **AAD Filter** (`ca.bc.gov.aad.authservlets.AuthenticationFilter`) — Azure AD/MSAL login, redirect servlet, config in `src/main/resources/aadConfig/authentication.properties`.
2. **WebADE Filter** (`ca.bc.gov.webade.j2ee.WebADEFilter`) — BC Gov's legacy identity/authorization framework (`ca/bc/gov/webade/` package tree: user info providers, SiteMinder/BCeID/IDIR user types, role/permission model). WebADE remains the authorization source of truth (roles/actions defined in `src/main/resources/webadeConfig/applicationConfiguration.json`) even though AAD now fronts authentication.

`DataServiceFilter` runs after both to initialize the per-request user/cache context.

### Data access — hand-written JDBC, no ORM
~35 domain DAOs live flat under `src/main/java/ca/bc/gov/srm/farm/dao/`, one per subsystem (enrolment, benefit, staging, codes, etc.), taking a `java.sql.Connection` via constructor injection. Most call Postgres table functions via plain `PreparedStatement`s (`SELECT * FROM pkg.proc(?,?)`); `DAOStoredProcedure` wraps `CallableStatement` with typed accessor helpers for the subset of DAOs still using that style, with null-handling conventions (`Y`/`N` indicators, `NullConstants` sentinels for primitives).

Transactions are managed separately from DAOs via `ca/bc/gov/srm/farm/transaction/` (`Transaction`, `WebADETransaction`, `TransactionProvider`), wrapping a `Connection` with begin/commit/rollback/close.

Implementation lookup uses a classic factory/service-locator: `ca/bc/gov/srm/farm/factory/ObjectFactory` (extends `BaseFactory`) resolves interface → implementation class name from `src/main/resources/config/implementation.properties` (overridable per-class), then instantiates via reflection. Call sites use `ObjectFactory.createObject(SomeInterface.class)`; tests override bindings via `ObjectFactory.setImplementingClass(...)`.

### Configuration — layered provider chain
`ca/bc/gov/srm/farm/configuration/ConfigurationUtility` delegates to an ordered chain of `ConfigurationProvider`s — first to resolve a key wins:
- `WebADEConfigurationProvider` — DB-backed preferences via WebADE
- `FarmDatabaseConfigurationProvider` — app-specific DB config tables
- `PropertiesProvider` / `SystemProvider` — flat `.properties` / system properties

`ConfigurationKeys` centralizes the ~100 string keys in use (CRM, CHEFS, CDOGS, reasonability-test thresholds, benefit calculation parameters, TIPS prefix, etc). Static bootstrap config lives in `src/main/resources/config/{applicationResources,implementation,messages}.properties`.

Note: `src/main/resources/aadConfig/authentication.properties` and `src/main/resources/webadeConfig/applicationConfiguration.json` contain plaintext dev secrets that are already tracked in git — be careful not to add new real secrets to tracked config files; follow existing patterns for env-var substitution (`${POSTGRES_URL}`-style, `#{...}` placeholders) where present.

### Calculator engine
`src/main/java/ca/bc/gov/srm/farm/calculator/` implements the actual AgriStability benefit math — reference margins, structural change adjustments, farm-size ratios, fair-market-value calculations, final benefit/payment amounts (`BenefitCalculator`, `ProductionMarginCalculator`, `StructuralChangeCalculator`, `NegativeMarginCalculator`, `FmvCalculator`, `AccrualCalculator`, `ReferenceYearCalculator`, `InventoryCalculator`, etc., wired via `CalculatorFactory`/`CalculatorConfig`). `calculator/basic` and `calculator/combined` are year/scenario-specific variants — `combined` handles multi-participant "combined farm" scenarios, `basic` the single-farm case. Corresponding domain model lives under `src/main/java/ca/bc/gov/srm/farm/domain/` (`benefit`, `enrolment`, `reasonability`, `staging`, `tips`, `codes`, `chefs`).

### External integrations
- **CHEFS** (Common Hosted Forms Service, a separate BC Gov product) — `ca/bc/gov/srm/farm/chefs/resource/` holds Jackson POJOs modeling CHEFS form submissions (adjustment, coverage, interim, NOL, NPP, statement A, supplemental). These are used by service classes (e.g. `ChefsFormSubmissionService`) to call the external CHEFS API, and by Struts actions to serialize/deserialize form data. This app is a *client* of CHEFS, not a REST API provider.
- **`ca/bc/gov/srm/farm/rest/`** — generic outbound REST client abstraction (`RestApiDao`, raw `HttpURLConnection` + Jackson) with pluggable auth handlers (`RestAuthenticationHandler`/`BasicAuthenticationHandler`), used for CHEFS, CRM, and CDOGS integrations.
- **CRM** (`ca/bc/gov/srm/farm/crm/`) — Dynamics CRM integration for account/enrolment sync.
- **CDOGS** (`ca/bc/gov/srm/farm/cdogs/`) — BC Gov's Common Document Generation Service.
- **Jasper Reports** — external report server (config under `reports.*` preference keys).
