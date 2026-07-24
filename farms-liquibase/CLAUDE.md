# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

`farms-liquibase` holds the PostgreSQL schema, tables, views, and PL/pgSQL functions/procedures for BC Gov's FARMS program (see `../farms-legacy/CLAUDE.md` and `../farms-api/CLAUDE.md`), plus the Liquibase changesets that apply them. It's a Postgres port of a legacy Oracle schema — the `farms_<name>_pkg` naming throughout is a holdover from Oracle packages, each translated into its own Postgres schema. It is one component of the larger `nr-brmb-ast` monorepo (siblings: `farms-api`, `farms-legacy`, `openshift`, `crunchy-postgres`).

There is no local build/test tooling — this repo's only "build" is packaging SQL files into a Liquibase Docker image; correctness is verified by running `liquibase update` against a real Postgres instance.

## Directory structure — read this before editing

- **`database/`** — the single source of truth for the *current* definition of every DB object, one `.sql` file per object, organized by schema:
  - **`database/farms/`** — the main data schema: `tables/`, `views/`, `constraints/` (FK/check constraints, one file per table, applied separately from the table's own `CREATE TABLE`).
  - **`database/farms_<name>_pkg/`** — one Postgres schema per legacy Oracle package (`farms_import_pkg`, `farms_calculator_pkg`, `farms_enrolment_read_pkg`, etc.), each with `schema.sql` (the `CREATE SCHEMA ... AUTHORIZATION "app_farms"`), `grants.sql` (all `GRANT EXECUTE ... TO "app_farms_rest_proxy"` for that schema's routines), and `functions/`/`procedures/` subfolders, one file per routine.
  - **`database/roles/`** — role DDL (e.g. `proxy_farms_rest.sql`, the login role the REST API connects as; password comes in as a `${POSTGRES_PROXY_USER_PASSWORD}` placeholder substituted by env/Vault, not a literal).
- **`releases/01_00_xx/01_00_00/00/`** — the **only** release currently wired into `changelog.json`. Its per-schema `.json` files (`00_schema.json`, `01_functions.json`, `02_procedures.json`, `03_grants.json`) are Liquibase changesets whose `sqlFile.path` entries point *back* at files under `database/` via relative paths (e.g. `../../../../../database/farms_import_pkg/functions/append_imp.sql`) — they don't embed or duplicate SQL. `changelog.json` → `release.json` → per-package `includeAll`.
- **`db_preconditions/`** — one-off manual bootstrap SQL (create database, create login, create roles, create schema, create extensions) run by hand via `psql` before Liquibase ever runs against a fresh environment — see `README.md` for the exact walkthrough (including the `ora2pg` Oracle→Postgres data migration steps).
- **`scripts/`** — **legacy/dead**, superseded by the `database/`+`releases/` split in PR #85 (`Feature/as 3510 reorganize database scripts for liquibase`). Not copied into the Docker image (see `Dockerfile`) and not referenced by any changelog. Don't add new files here.

### The critical gotcha: how routine changes actually get deployed

Because every changeset in `releases/01_00_xx/01_00_00/00/` points at a file in `database/`, and no changeset uses `runOnChange`, the normal Liquibase pattern would be to add a *new* changeset for every modification. **That is not what happens in practice** — routine/table fixes are made by editing the file directly in `database/` in place (e.g. PR #132 changed only `database/farms_negative_margin_pkg/procedures/calculate_negative_margins.sql`; PR #123 changed only `database/farms_read_pkg/functions/read_op_fmv_prev_year.sql`), with no new release/changeset added. Follow this same pattern for routine/table DDL fixes: edit the file in `database/` in place rather than authoring a new versioned changeset, unless you're intentionally adding a brand-new object (which does need a new `sqlFile` entry in the relevant package's `.json`) or cutting an actual new release directory. Be aware this means Liquibase's checksum for that changeset will no longer match what's recorded in `DATABASECHANGELOG` in any environment where it already ran — expect to need `liquibase clear-checksums` (or equivalent) there.

## Deployment pipeline

- **`Dockerfile`** — `FROM liquibase/liquibase:4.30.0`, copies in only `database/`, `releases/`, `changelog.json`, `liquibase.properties`. `liquibase.properties` sets `driver: org.postgresql.Driver` and `schema: farms`; the JDBC URL/credentials are supplied at runtime via `LIQUIBASE_COMMAND_URL`/`LIQUIBASE_COMMAND_USERNAME`/`LIQUIBASE_COMMAND_PASSWORD` env vars, not baked into the image.
- **CI** (`../.github/workflows/liquibase-ddl.yml`) — manually dispatched (`workflow_dispatch`, choose `ENVIRONMENT_NAME`/`NAMESPACE`/`TAG`). Builds and pushes `ghcr.io/bcgov/farms-liquibase`, then calls `openshift-deploy.yml` with `MICROSERVICE_NAME: farms-liquibase`.
- **Actual migration run** — `../openshift/farms-liquibase-job.yaml`, a Kubernetes `Job` (not covered by `openshift-deploy.yml`'s normal flow — see `../openshift/CLAUDE.md`) that runs `liquibase update --changelog-file=#{CHANGELOG_FILE}#` after sourcing DB credentials injected by HashiCorp Vault into `/vault/secrets/env`.
- To stand up a brand-new environment from scratch, follow `README.md` end-to-end: manually run the `db_preconditions/` SQL via `psql` against the Crunchy Postgres cluster, optionally migrate data from Oracle via `ora2pg`, then trigger the *Liquibase DDL application* GitHub Action to apply `database/`/`releases/`.

## Conventions

- All identifiers are lowercase `snake_case`, often abbreviated to fit Oracle's legacy 30-character identifier limit (e.g. `pi_deemed_bnft_manual_calc_ind`, `farm_zbpu_benchmark_per_units`) — match existing abbreviation style rather than using full words when adding columns/objects to an existing table family.
- Object ownership/grants follow a fixed two-role model throughout `database/`: `app_farms` owns every schema/object; `app_farms_rest_proxy` gets `USAGE` on the schema plus `EXECUTE` on every function/procedure (read/write split further by which `_read_pkg`/`_write_pkg` schema a routine lives in). When adding a new routine, add a matching `GRANT EXECUTE ... TO "app_farms_rest_proxy"` line to that schema's `grants.sql`.
- Table-level FK/check constraints live separately from the table DDL, under `database/farms/constraints/<table_name>.sql`, one file per table — add new constraints there, not inline in the `tables/` file.
