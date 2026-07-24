# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

`nr-brmb-ast` is BC Government's FARMS (Farm Income Reference Margin Program / AgriStability-AgriInvest) system — a monorepo of independently-deployed modules that together implement farm income-stabilization benefit calculation, enrolment, and reporting. Each module has its own `CLAUDE.md` with build/test/architecture detail specific to it; this file only covers what spans module boundaries. Read the relevant module's `CLAUDE.md` before working in it — don't duplicate that detail from memory.

| Module | What it is |
|---|---|
| `farms-legacy/` | Struts 1.2 / Java 8 web app (the original "FARM" app) — Struts UI, benefit calculator engine, enrolment/reasonability/TIPS workflows, WebADE+AAD auth |
| `farms-api/` | Spring Boot 3 / Java 21 REST API — newer resource-based endpoints (codes, imports, inventory, benchmarks, etc.), MyBatis + JdbcTemplate over the same DB |
| `farms-liquibase/` | Postgres schema/DDL/PL-pgSQL source of truth + Liquibase changesets applying it |
| `crunchy-postgres/` | Helm chart provisioning the shared Postgres cluster (Crunchy Data PGO) both apps and the migration job run against |
| `openshift/` | Raw K8s/OpenShift manifest templates (Deployment/Service/Route/etc.) for `farms-api`, `farms-legacy`, and the `farms-liquibase` migration Job |
| `load-tests/` | JMeter plans load-testing `farms-api` |
| `farms-api-postman/` | Postman (Git-sync format) collections exercising `farms-api`, mirroring its resource areas |

`farms-legacy` and `farms-api` are two separate applications against the *same* database (`farms-liquibase` schema) — not a legacy-app-being-replaced-by-a-new-one split where the old one goes away, but two live front ends. When a change touches the DB, check both apps for callers before assuming only one needs updating.

## Cross-module conventions

- **Maven settings are centralized**: `settings.xml` at this root (not per-module) configures the BC Gov Artifactory credentials (`repo.login`/`repo.password`, `openshiftRepo.login`/`openshiftRepo.password`) needed to resolve private `ca.bc.gov.brmb.common:*` / `ca.bc.gov.nrs.wforg:*` dependencies and to publish. Any `mvn` invocation in `farms-api/` or `farms-legacy/` needs `--settings=../settings.xml` (or `settings.xml` from repo root, as CI does).
- **`renovate.json`** governs dependency-update PRs repo-wide: automerge is off, updates batch onto `feature/renovate`, capped at 5 concurrent PRs, scheduled weekends only.
- **Token-substitution deployment model**: `openshift/` and `crunchy-postgres/` both check in templates containing unresolved `#{TOKEN}#` placeholders (e.g. `#{ENV}#`, `#{NAMESPACE}#`, `#{LICENSE_PLATE}#`), resolved only at deploy time by `cschleiden/replace-tokens@v1.3` inside the relevant GitHub Actions workflow. Files in these two directories are never valid as checked in — don't `oc apply`/`helm install` them directly.
- **Secrets come from HashiCorp Vault**, not Kubernetes `Secret` objects — pods carry `vault.hashicorp.com/agent-inject*` annotations that render fetched values into `/vault/secrets/env`, sourced at container startup. See `openshift/CLAUDE.md` for the pattern to follow when adding a new secret-backed env var.
- **Build vs. deploy is decoupled**: `.github/workflows/build-package.yml` (called by `build-dev.yml`/`build-farms-legacy-dev.yml`) builds/tests each Java app with Maven, then builds and pushes a Docker image to `ghcr.io/bcgov/<app>`. `openshift-deploy.yml` is a separate, later step that pulls an already-pushed image by tag, resolves it to a digest, and applies the tokenized `openshift/` manifests. `openshift-create-update-db.yml` deploys the `crunchy-postgres` chart; `liquibase-ddl.yml` runs the `farms-liquibase` migration Job — neither is touched by `openshift-deploy.yml`.
- **CHEFS form comparison workflows** (`chef-compare-envs.yml`, `chef-compare-previous.yml`) are standalone `workflow_dispatch` tools that diff CHEFS (Common Hosted Forms Service) form JSON across environments or against a draft — unrelated to the Java build/deploy pipeline, used when auditing form changes referenced by `farms-legacy`'s CHEFS integration.

## Working across modules

- A schema/DDL change almost always spans at least two modules: add the DDL in `farms-liquibase/database/` (per its "edit in place" convention — see its `CLAUDE.md`), then update whichever of `farms-api`/`farms-legacy` reads/writes the changed table.
- A new API endpoint in `farms-api` is typically mirrored in `farms-api-postman/postman/collections/<Area>/` and, if it should be load-tested, `load-tests/farms-api-load-test.jmx` — both follow the same "list → extract ID → fetch/update" request-chaining pattern for CRUD resources.
- Config files checked into `farms-legacy/src/main/resources/{aadConfig,webadeConfig}/` have deployed counterparts baked into `openshift/farms-legacy-configmap-{aad,webade}.yaml` — a change to one usually needs the matching change in the other to actually take effect once deployed.
