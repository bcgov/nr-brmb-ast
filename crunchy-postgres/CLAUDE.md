# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

`crunchy-postgres` is a Helm chart that provisions the shared PostgreSQL cluster (via Crunchy Data's `PostgresCluster` CRD / PGO — the Postgres Operator) that backs FARMS. It is one component of the larger `nr-brmb-ast` monorepo (siblings: `farms-api`, `farms-legacy`, `farms-liquibase`, `openshift`). The actual FARMS schema/DDL lives in `../farms-liquibase`, not here — this chart only provisions the cluster the migrations run against.

There is no application code — everything here is a Helm chart (`charts/crunchy-postgres/`) of Kubernetes CRD templates.

## Deployment (this repo's actual practice — ignore the README's chart-releaser instructions)

`README.md` and `charts/crunchy-postgres/README.md` describe a generic bcgov Helm-chart-repo workflow (chart-releaser publishing to `bcgov.github.io/crunchy-postgres`, a companion "tools" chart, GitHub Releases). **None of that applies in this repo** — there is no chart-releaser workflow under `.github/workflows/`, no `charts/tools/` directory, and the chart is never published anywhere. Treat those sections of the READMEs as inherited boilerplate, not instructions.

What actually happens: `../.github/workflows/openshift-create-update-db.yml` (manual `workflow_dispatch`, choosing `ENVIRONMENT_NAME`: dev/dlvr/test/prod/tools and matching `NAMESPACE`) copies `charts/crunchy-postgres` into a `staging/` dir, substitutes every `#{TOKEN}#` placeholder via `cschleiden/replace-tokens@v1.3` (env values sourced from GitHub Actions `vars`/`secrets` scoped to the chosen environment — `DB_STORAGE`, `DB_MAX_CPU`, `DB_MAX_MEMORY`, `DB_REPLICAS`, `DB_BACKUP_VOLUME_SIZE`, `BOUNCER_REPLICAS`, `S3_BUCKET`, plus a computed `S3_ENVIRONMENT`), then runs `helm install` or `helm upgrade` directly against that local, tokenized copy of the chart — never against a published repo.

To test a chart change locally without deploying: `helm template --output-dir yaml charts/crunchy-postgres` (per the chart README), but note the rendered output will still contain unresolved `#{TOKEN}#` placeholders since token substitution only happens in the GitHub Actions step, not via Helm's own `--set`/`values.yaml` mechanism.

## Chart structure

- **`Chart.yaml`** — `name: crunchy-postgres-#{ENV}#` (the chart name itself carries an unresolved token — that's expected, resolved by the CI step above), `appVersion` pins the PGO/Crunchy Postgres Operator version this chart targets.
- **`values.yaml`** — all tunables, several pre-populated with `#{TOKEN}#` placeholders that only get real values during the CI-driven `helm install`/`upgrade` (not usable standalone with plain `helm install -f values.yaml`).
- **`templates/PostgresCluster.yaml`** — the single template, rendering one `postgres-operator.crunchydata.com/v1beta1` `PostgresCluster` resource. Key sections: `instances` (the HA Postgres pod set, `replicaCertCopy` sidecar, pod anti-affinity to spread replicas across nodes), `users` (creates a superuser-ish app role plus `postgres`), `backups.pgbackrest` (two repos — `repo1` local PVC-backed, `repo2` S3-backed at `nrs.objectstore.gov.bc.ca`, both with independently offset backup schedules so `repo1`/`repo2` full/differential/incremental backups don't run at the same time), `patroni.dynamicConfiguration` (HA/replication tuning — `pg_hba`, WAL buffer sizes), `proxy.pgBouncer` (connection pooler in front of the cluster; `client_tls_sslmode: disable` is hardcoded).
- **`templates/_helpers.tpl`** — standard Helm name/label helpers (`crunchy-postgres.fullname`, `.labels`, `.selectorLabels`); nothing FARMS-specific.

## Conventions

- Every environment-specific value is a `#{TOKEN}#` placeholder, matching the same convention used in `../openshift/` — see `../openshift/CLAUDE.md`'s "Templating mechanism" section for the full token-substitution model (`#{ENV}#`, `#{LICENSE_PLATE}#`, etc.) shared across this monorepo's OpenShift-deployed pieces. Never assume a token resolves the same way in every file without grepping for it first.
- The two pgBackRest repos are intentionally offset (`repos.schedules` vs `repos.schedulesOffset`) — when tuning backup cadence, change both schedule blocks together or you'll desync repo1/repo2.
- `postgresVersion`/`crunchyImage`/`postGISVersion` in `values.yaml` gate whether PostGIS is enabled (both must be set together, per the comment in `values.yaml`) — this cluster does not currently run PostGIS.
