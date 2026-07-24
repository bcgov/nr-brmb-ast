# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

`load-tests` holds Apache JMeter test plans (`.jmx`, JMeter 5.6.3 XML format) used to load-test FARMS services deployed to OpenShift. It is one component of the larger `nr-brmb-ast` monorepo (siblings: `farms-api`, `farms-legacy`, `farms-liquibase`, `crunchy-postgres`, `openshift`). There is currently one plan, `farms-api-load-test.jmx`, targeting `../farms-api`.

There's no local tooling checked in here (no wrapper scripts, no README) — everything about how these plans are invoked lives in `../.github/workflows/load-test.yml`.

## Running a test

Via CI (the normal path): trigger the *load test* GitHub Action (`workflow_dispatch`) on `../.github/workflows/load-test.yml`, choosing `MICROSERVICE_NAME` (currently only `farms-api` is a valid option) and `ENVIRONMENT_NAME` (dev/dlvr/test/prod). The workflow downloads JMeter 5.6.3, then runs:

```bash
apache-jmeter-5.6.3/bin/jmeter -n \
  -t load-tests/${MICROSERVICE_NAME}-load-test.jmx \
  -Jtenant_id=<secret> -Jclient_id=<secret> -Jclient_secret=<secret> \
  -Jhost=<API_HOST var> -Jthreads=1 \
  -l results.csv
```
and uploads `results.csv` as a build artifact.

To run locally, install JMeter 5.6.3+ and pass the same `-J` properties yourself: `tenant_id`/`client_id`/`client_secret` (Azure AD app registration for the OAuth2 client-credentials flow against the target environment), `host` (the API's hostname, no scheme — e.g. `farms-dev.apps.silver.devops.gov.bc.ca`), and optionally `threads` (defaults to `1`, see below).

## Naming convention for adding a new plan

The workflow builds the filename from the input as `${MICROSERVICE_NAME}-load-test.jmx`. To load-test a different service, add a same-named `<service>-load-test.jmx` here and add `<service>` to the `MICROSERVICE_NAME` workflow_dispatch `options` list in `load-test.yml` — the runner script itself needs no changes.

## `farms-api-load-test.jmx` structure

Two thread groups, run in order:
1. **`Login Thread Group`** (1 thread, runs once via a `OnceOnlyController`) — the `OAuth Login` transaction hits `login.microsoftonline.com/${tenant_id}/oauth2/v2.0/token` with a client-credentials grant, and a Groovy `JSR223PostProcessor` stashes the returned `access_token` into a JMeter `props` global so it's visible to the second thread group.
2. **`Thread Group`** (`${threads}` threads, default 1, 300s ramp-up, `on_sample_error: continue` so one failing endpoint doesn't abort the run) — a `GaussianRandomTimer` (1000ms ± 500ms) paces requests between samplers. Every `HTTPSamplerProxy` in this group reads `Authorization: Bearer ${access_token}` from a shared `HeaderManager` sourced from the login thread group's stashed token.

Requests are grouped into `TransactionController`s, one per API resource area, each measured as a single logical transaction:

`Benchmark Per Units`, `Check Health`, `Configuration Parameters`, `Crop Unit Conversions`, `Expected Productions`, `Fair Market Values`, `Fruit Veg Type Details`, `Generic Codes`, `Inventory Item Attributes`, `Inventory Item Details`, `Inventory Type Xrefs`, `Line Items`, `Market Rate Premiums`, `Productive Unit Codes`, `Root`, `Structure Group Attributes`, `Year Configuration Parameters`.

Several of these chain requests within the transaction (e.g. `Benchmark Per Units`: list → `JSONPostProcessor` extracts `benchmarkPerUnitId` from the response → a second sampler fetches that specific record by ID) — when adding a new transaction that depends on a prior response, follow this same list-then-extract-then-fetch pattern rather than hardcoding an ID.

All API samplers hit `${host}:443` over HTTPS with `HTTPSampler.path` set to a hardcoded `farms-api` endpoint (e.g. `/farms-api/v1/benchmarkPerUnits`) — see `../farms-api/CLAUDE.md` for the controller layer these paths map to.

## Editing the `.jmx`

This is JMeter's native GUI-editable XML format — the normal workflow is to open it in the JMeter GUI (`jmeter.sh`/`jmeter.bat`, no `-n`) rather than hand-editing the XML, since GUI edits are easy to get subtly wrong by hand (mismatched `hashTree` nesting, stale `guiclass`/`testclass` pairs). If you must hand-edit, keep every `<TransactionController>`/`<HTTPSamplerProxy>` paired with its sibling `<hashTree>` at the same nesting depth as its neighbors.
