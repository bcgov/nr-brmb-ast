# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

`openshift` holds the raw Kubernetes/OpenShift manifest templates for deploying the two Java web apps (`farms-api`, `farms-legacy`) and the `farms-liquibase` DB-migration job to BC Gov's Silver OpenShift cluster (namespace prefix `e980f4-*`). It is one component of the larger `nr-brmb-ast` monorepo (siblings: `farms-api`, `farms-legacy`, `farms-liquibase`, `crunchy-postgres`). There is no CLI/build tooling here — these are plain `.yaml` files, templated and applied entirely by GitHub Actions workflows in `../.github/workflows/`.

The Postgres cluster itself (a Crunchy Data `PostgresCluster` Helm chart, not plain manifests) lives in `../crunchy-postgres/charts/crunchy-postgres` and is deployed by a separate workflow — see below.

## Templating mechanism

Files are **not** valid Kubernetes manifests as checked in — every environment-specific value is a `#{TOKEN}#` placeholder substituted at deploy time by `cschleiden/replace-tokens@v1.3` inside `.github/workflows/openshift-deploy.yml`. Never hand-edit a token value in one file without checking whether the same token is used elsewhere (`grep -rn '#{TOKEN}#' openshift/`) — e.g. `#{ENV}#`, `#{NAMESPACE}#`, `#{LICENSE_PLATE}#` (`e980f4`, hardcoded in the workflow), and `#{VAULT_RESOURCE}#` (`prod` vs `nonprod`, chosen by whether `NAMESPACE == e980f4-prod`) appear across nearly every file.

Naming convention: `<app>-<kind>.yaml`, and every resource `metadata.name` is suffixed `-#{ENV}#` (dev/dlvr/test/prod) so multiple environments can coexist in the same namespace.

### Deploy flow (`openshift-deploy.yml`, dispatched or called with `MICROSERVICE_NAME` = `farms-api`|`farms-legacy`)
1. Copies only `openshift/<MICROSERVICE_NAME>*.yaml` into a `staging/` dir (so `farms-api` and `farms-legacy` are deployed independently; `farms-liquibase-*.yaml` isn't touched by this workflow).
2. Pulls the already-built `ghcr.io/bcgov/<image>:<TAG>` image and resolves it to an immutable digest (`IMAGE_BY_DIGEST`) — deployments always pin by digest, not tag, so re-running a deploy with the same `TAG` is reproducible even if the tag is later overwritten.
3. Substitutes tokens (env vars in the workflow step supply every `#{TOKEN}#`, sourced from GitHub Actions `vars`/`secrets` scoped to the `ENVIRONMENT_NAME` environment).
4. `oc login` then `oc apply -f` every file in `staging/`.

The `farms-liquibase-job.yaml` Job is applied by a different, separate workflow path (`liquibase-ddl.yml` / `openshift-create-update-db.yml` territory in `../.github/workflows/`) — check there before assuming `openshift-deploy.yml` touches migrations.

## Per-app resource sets

Both `farms-api` and `farms-legacy` follow the identical Deployment/Service/Route/Autoscaler/ConfigMap/NetworkPolicy shape; `farms-legacy` has two extra ConfigMaps because it needs config files mounted as volumes, not just env vars.

- **`*-deployment.yaml`** — single-replica `Deployment`, `RollingUpdate` strategy. Runs Tomcat via `catalina.sh run`, prefixed by `. /vault/secrets/env` to source secrets HashiCorp Vault has written to a local file (see below) before startup. Config values come in as env vars via `configMapKeyRef` against the app's `*-configmap*.yaml`.
  - `farms-api-deployment.yaml` has a `livenessProbe` hitting `/farms-api/v1/checkHealth?callstack=test`; `farms-legacy-deployment.yaml` has none.
  - `farms-legacy-deployment.yaml` additionally mounts two ConfigMaps as files directly into the WAR's `WEB-INF/classes/` (`webadeConfig/applicationConfiguration.json`, `aadConfig/authentication.properties`) — these are the *deployed* counterparts of the same-named files under `../farms-legacy/src/main/resources/`, so a change to the checked-in dev version of those files usually needs a matching change to `farms-legacy-configmap-webade.yaml`/`farms-legacy-configmap-aad.yaml` here to take effect in a deployed environment.
- **`*-configmap.yaml`** — plain key/value env config (ports, thread pools, log level, Postgres connection string built from the in-cluster `crunchy-postgres-#{ENV}#-pgbouncer` service DNS name, Azure AD client/tenant IDs).
- **`*-configmap-aad.yaml`** / **`*-configmap-webade.yaml`** (legacy only) — whole config *files* embedded as ConfigMap data, mounted as volumes rather than env vars, because the underlying frameworks (see `../farms-legacy/CLAUDE.md` — AAD filter, WebADE filter) read them as files, not env.
- **`*-db-access.yaml`** — `NetworkPolicy` opening ingress to the shared `crunchy-postgres-#{ENV}#` pods from just this app's pods, keyed by pod label (`app: <app>-#{ENV}#`). `farms-liquibase` gets its own copy since the migration Job runs as a distinct pod identity from the running app.
- **`*-route.yaml`** — edge-TLS `Route` (HTTP→HTTPS redirect) fronting the app's `Service`.
- **`*-service.yaml`** — `ClusterIP`-style `Service` selecting the deployment's pods.
- **`farms-api-autoscaler.yaml`** / **`farms-legacy-autoscaler.yaml`** — HPA scaling 1..`#{MAX_API_COUNT}#`/`#{MAX_LEGACY_COUNT}#` replicas on CPU (30%) and memory (150%) utilization, asymmetric scale-up (fast, 15s stabilization) vs scale-down (slow, 120s period).

## Secrets — Vault, not plain Kubernetes Secrets

None of these manifests define a `Secret` object. Every pod template instead carries `vault.hashicorp.com/agent-inject*` annotations (HashiCorp Vault Agent Injector sidecar pattern): the annotations point at a path in Vault (`#{LICENSE_PLATE}#-#{VAULT_RESOURCE}#/data/#{ENV}#/secrets`) and an inline template that renders the fetched values as `export VAR="..."` lines into `/vault/secrets/env` before the container's real entrypoint runs. `vault.hashicorp.com/auth-path` selects the cluster (`k8s-silver`/`k8s-gold`/`k8s-golddr`/`k8s-emerald` — this repo deploys to Silver). When adding a new secret-backed env var, extend the `agent-inject-template-env` block (not a `configMapKeyRef`) and reference it as a plain shell env var in the container's `args`/`command`, matching the existing `POSTGRES_PASSWORD` pattern.

## Adding a third app / new environment

- New app: copy the full file set for `farms-api` or `farms-legacy`, rename with the new app prefix, add its `#{TOKEN}#`s to the `Fill yaml files` step's `env:` block in `openshift-deploy.yml`, and add it to the workflow's `MICROSERVICE_NAME` input options.
- New environment: environments are just a token value (`dev`/`dlvr`/`test`/`prod`) resolved against a matching GitHub Actions `environment:` (for `vars`/`secrets` scoping) and `NAMESPACE` choice — no manifest changes needed, only workflow input options and the corresponding GitHub environment/variable configuration.
