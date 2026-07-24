# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

`farms-api-postman` holds Postman collections/environments, in Postman's Git-sync YAML format (a.k.a. "Postman for Git"), exercising `../farms-api`'s REST endpoints. It is one component of the larger `nr-brmb-ast` monorepo (siblings: `farms-api`, `farms-legacy`, `farms-liquibase`, `crunchy-postgres`, `load-tests`). This is not a Newman/CLI-runnable test suite — there's no `.github/workflows` entry for it — it's meant to be opened as a linked Git repository inside the Postman desktop/web app, which owns the sync between these files and Postman Cloud.

## Structure

- **`.postman/resources.yaml`** — the Postman-Git sync manifest for this workspace.
  - `localResources.collections` points at `../Root.postman_collection.json` — **that file does not exist in this repo** (not tracked, not gitignored). Treat it as a stale/local-only artifact of the Postman app, not something to create or rely on.
  - `cloudResources.collections` currently maps only the `Root` folder to a Postman Cloud collection ID — the other 17 collection folders under `postman/collections/` aren't (yet) mapped to a cloud ID here, so don't assume every folder round-trips to Postman Cloud the same way.
- **`postman/collections/<Area>/`** — one folder per API resource area, mirroring `../farms-api`'s controllers/resources and the same grouping used in `../load-tests/farms-api-load-test.jmx`'s transaction controllers (`Benchmark Per Units`, `Configuration Parameters`, `Crop Unit Conversions`, `Expected Productions`, `Fair Market Values`, `Fruit Veg Type Details`, `Generic Codes`, `Imports`, `Inventory Item Attributes`, `Inventory Item Details`, `Inventory Type Xrefs`, `Line Items`, `Market Rate Premiums`, `Productive Unit Codes`, `Structure Group Attributes`, `Year Configuration Parameters`, plus `Check Health` and `Root`). Each folder has:
  - `.resources/definition.yaml` — collection-level `variables`: `tenantId`/`clientId`/`clientSecret`, always sourced from Postman Vault via `{{vault:{{vaultPrefix}}-tenantId}}` syntax, never hardcoded.
  - One `<Request Name>.request.yaml` per HTTP request (`$kind: http-request`).
- **`postman/environments/{DEV,TEST,PROD}.environment.yaml`** — per-environment `baseUrl` (the `farms-api` OpenShift route for that env — see `../openshift/CLAUDE.md`), `loginUrl` (`https://login.microsoftonline.com`, same across environments), and `vaultPrefix` (`farms-api-dev`/`-test`/`-prod`) that keys into Postman Vault for that environment's `tenantId`/`clientId`/`clientSecret`.
- **`postman/globals/workspace.globals.yaml`** — empty; no global variables currently defined.

## Request patterns

- **Auth**: every collection folder has a `Get Access Token` request — an OAuth2 client-credentials call to `{{loginUrl}}/{{tenantId}}/oauth2/v2.0/token` whose `afterResponse` script does `pm.environment.set("access_token", json.access_token)`. Run it first (or via the folder's runner) before any other request in that collection — every other request in the folder authenticates with `auth: type: bearer, credentials: { token: "{{access_token}}" }`.
- **CRUD chaining**: `Create *` requests capture the created record's ID into an environment variable in their `afterResponse` script (e.g. `pm.environment.set("benchmarkPerUnitId", json.benchmarkPerUnitId)`), which sibling `Get *`/`Update *`/`Delete *` requests then reference via `{{benchmarkPerUnitId}}` in their URL — run `Create` before `Get`/`Update`/`Delete` in the same folder, same list-then-extract-then-fetch idea as `../load-tests/farms-api-load-test.jmx`.
- **Assertions**: most `afterResponse` scripts include `pm.test(...)` blocks checking status code and response shape (commonly asserting the `@type` field matches the expected `*Rsrc` type from `../farms-api`'s `data/models/`) — follow this pattern (status + `@type` + key field values) when adding a request for a new endpoint.
- **`Imports/` collection**: the four `Import {BPU,CRA,FMV,IVPR}` requests upload a file (`body.type: file`) from a hardcoded local path (e.g. `/U:/hwang/FARM/BPU Import/bpu.csv`) left over from the original author's machine — replace `body.content.src` with a real local file path before running these.
