# BC Permanent Pacific Time (PCT, UTC-7): Impact Report for nr-brmb-ast

| | |
|---|---|
| **Prepared** | 2026-10-29 (replaces the 2026-10-07 report, which reviewed `main` @ `23ed328`) |
| **Repo state** | public GitHub `bcgov/nr-brmb-ast`, branch `main` @ `a31c89b` (2026-10-28, "Merge pull request #187 from bcgov/feature/AS-4024_chefs-too-many-requests"). 45 commits since the previous review, including **PR #186 "AS-4052 Time zone fixes"** (merged 2026-10-20), which addressed most of the earlier findings. |
| **Components** | FARMS (AgriStability / AgriInvest) monorepo. **`farms-legacy`**: Struts 1.2 web app, Java 8, 1,356 main Java files, now on `tomcat:9.0.123-jre8`. **`farms-api`**: Spring Boot 3.4.10 REST API, Java 21, 204 main Java files, now on `tomcat:10.1.61-jre21`. **`farms-liquibase`**: PostgreSQL schema and PL/pgSQL packages (`timestamp` and `date` columns, no `timestamptz`). **`crunchy-postgres`** (PostgreSQL 17), **`cdogs/templates`**, `openshift`, `load-tests`, `farms-api-postman`. |
| **Deadline** | **Sunday 2026-11-01, 02:00 local (09:00 UTC)**, **3 days away** |
| **Bottom line** | **Medium risk, down from High. The code changes are done; three deployment checks remain.** PR #186 moved both apps to newer Tomcat images, added `-Duser.timezone=America/Vancouver` to both `setenv.sh` files, set the CDOGS time zone to `America/Vancouver`, changed the two database roles from `America/New_York` to `America/Vancouver`, and fixed every week-year (`YYYY`) pattern and shared date formatter. What remains before Sunday: **(1)** confirm that the images **actually deployed** contain a JRE with tzdata 2026b (8u501+ and 21.0.12+), because the Tomcat tags float and the Dockerfiles do not pin a JRE build (N1); **(2)** apply the role time zone change **by hand in each database**, because the edited role file is not re-run by Liquibase in existing environments (N2); and **(3)** confirm that the Crunchy PostgreSQL image carries tzdata 2026b (T3, unchanged). |

---

## 1. What changed (same basis as the earlier reports)

- **Government rule.** BC stopped changing clocks after 2026-03-08. On **2026-11-01 clocks do not fall back.** BC stays at **UTC-7** all year, named *Pacific time (PCT)*.
- **IANA tzdata 2026b** models `America/Vancouver` as permanent UTC-7 from 2026-11-01 02:00.
- **JDK builds with the rule:** 8u501, 11.0.32, 17.0.20, 21.0.12 and 25.0.4 or later. Earlier builds treat BC winter as PST (UTC-8).
- **PostgreSQL** applies its own tzdata (from the server image) to `current_timestamp`, `current_date` and conversions into `timestamp` columns, in the session `TimeZone`.

---

## 2. Summary of findings

| # | Area | Severity | Fails on Nov 1? | Fix |
|---|---|---|---|---|
| N1 | **Deployed images not yet verified.** `farms-legacy/Dockerfile` line 1 is `FROM tomcat:9.0.123-jre8` and `farms-api/Dockerfile` line 1 is `FROM tomcat:10.1.61-jre21`. These tags are rebuilt on a floating `eclipse-temurin` base, so the JRE inside depends on when each image was built and pushed to `ghcr.io` | **High until verified** | **Yes, if the deployed `farms-legacy` image lacks 8u501+:** Java-generated times one hour behind, "today" wrong from 23:00, agents one hour late (former T1). Less visible in `farms-api` | R1 |
| N2 | **Role time zone fix not applied automatically.** `database/roles/proxy_farms_rest.sql` line 15 and `db_preconditions/logins/farms.ddl.create_login_app_farms.sql` line 15 now set `America/Vancouver`, but the role changeset has already run in each environment and `db_preconditions` is run by hand | **Medium** | Only for sessions that do not send their own zone (for example `psql`, scripts, the Jasper report server): Eastern wall times and Eastern `current_date` until the change is applied | R2 |
| T3 | Database-generated times and dates: 1,160 `current_timestamp`, 332 `statement_timestamp()`, 126 `current_date`, 6 `clock_timestamp()` (source packages and changesets combined), stored in `timestamp` columns | **Medium** | **Yes, if the PostgreSQL image lacks tzdata 2026b:** database-generated times one hour off; `current_date` wrong from 23:00 | R3 |
| N3 | CDOGS documents now request `timezone = "America/Vancouver"` (`CdogsOptionsResource.java`) | Low (verify) | Only if CDOGS ignores the option for `convDate` fields or runs stale tz data | R4 |
| T6 | Scheduled agents (`ImportAgent`, `ChefsAgent`, `BenefitTriageAgent`) run in local time windows in the JVM zone | Low | Only if N1 fails | R1 |
| N4 | Stale copy of the old role script in `scripts/01_00_xx/01_00_00/00/ddl/roles/farms.ddl.create_login_proxy_farms_rest.sql` line 15 still sets `America/New_York` | Low | No; this directory is not wired into `changelog.json`, but a manual rebuild from it would reintroduce the old setting | R5 |
| T7 | Crunchy backup schedule in UTC | None | No | n/a |
| T8 | CRM transfer timestamps include the offset (`yyyy-MM-dd H:mm:ssZ`) | None | No | n/a |
| F | **Fixed in PR #186:** explicit `-Duser.timezone` (former R2), week-year `YYYY` patterns (former D1, D2), shared static `SimpleDateFormat` instances (former D3), CDOGS time zone (former T5), role defaults in source (former T4) | Resolved | No | n/a |

---

## 3. Changes Since the Previous Review

| Previous finding (2026-10-07) | Status on `a31c89b` | Evidence |
|---|---|---|
| T1 `farms-legacy` on `tomcat:9.0.86-jre8` (8u402 era) | **Image bumped; JRE build still to verify (N1)** | Commit `a937991`: `FROM tomcat:9.0.123-jre8` |
| T2 `farms-api` on `tomcat:10.1.44-jre21` (August 2025) | **Image bumped; JRE build still to verify (N1)** | Commit `fa6b6c8`: `FROM tomcat:10.1.61-jre21` |
| R2 No `-Duser.timezone` | **Fixed** | Commit `58d2e63`: `farms-api/deploy-tools/setenv.sh` line 7 and `farms-legacy/deploy-tools/setenv.sh` line 7 add `-Duser.timezone=America/Vancouver` |
| T3 PostgreSQL tzdata | **Open** | `crunchy-postgres` unchanged since 2026-07-21; no image is pinned (`values.yaml` uses the operator default for PostgreSQL 17) |
| T4 Roles default to `America/New_York` | **Fixed in source; must be applied by hand (N2)** | Commit `58d2e63` |
| T5 CDOGS time zone not set | **Fixed; verify output (N3)** | Commit `58d2e63`: `CdogsOptionsResource` gains `timezone = "America/Vancouver"` |
| T6 Agents in JVM zone | Unchanged; depends on N1 | `ChefsAgent.java` lines 228-231, `BenefitTriageAgent.java` lines 229-232 |
| D1 `YYYY` week-year patterns | **Fixed** | Commit `00d072c`: `TipReportDAO`, `TipBenchmarkExtractDAO` (`yyyy-MM-dd hh:mm a`) and `ReportServiceImpl` (`MMMM dd yyyy`) |
| D2 `"YYYMMDD"` in `WebADEDeveloperFilter` | **Fixed** | Commit `00d072c`: `"yyyyMMdd"` |
| D3 Shared static `SimpleDateFormat` | **Fixed** | Commit `00d072c`: `DateUtils`, `DataParseUtils`, both `ParseUtils`, `TipBenchmarkExtractDAO`, `StatementASubmissionDataResource` now create formatters per use or per instance; no static `SimpleDateFormat` remains in main code |

The other 41 commits (CHEFS 429 handling, CRM `If-Match`, combined-farm performance, version display, SVN syncs) add no new time zone risk. The new token fetchers use elapsed milliseconds (`expires_in`), the new `read_old_error_submissions` function uses `current_timestamp - make_interval(...)`, and the new fiscal-year-end value is built at local midnight and formatted by CDOGS in `America/Vancouver`.

---

## 4. Areas of Concern

### N1. Deployed images not yet verified: HIGH UNTIL VERIFIED

The official `tomcat` images are rebuilt whenever their base image changes, and their Dockerfiles use floating bases (`FROM eclipse-temurin:21-jre-…` and `FROM eclipse-temurin:8-jre-…`). Docker Hub showed `tomcat:10.1.61-jre21` and `tomcat:9.0.123-jre8` re-pushed within the last few days. The JRE build in the image that `build-package.yml` pushed to `ghcr.io/bcgov/<app>` is therefore whatever Temurin release was current when that build ran (on or after 2026-10-19), and it is not recorded in the repository. Public sources reviewed did not state the exact Temurin build or tzdata version in these tags.

Bumping the tags was the right fix: the old tags (`10.1.44`, `9.0.86`) are no longer rebuilt and could never receive the new rule. The remaining step is to prove that the running pods have it. If they do not, `farms-legacy` shows the failures described as T1 in the previous report: Java-generated times one hour behind, "today" and expiry checks wrong from 23:00 to midnight, and agents starting one hour late.

### N2. Role time zone change needs manual application: MEDIUM

The source now reads `ALTER USER proxy_farms_rest set TIMEZONE to 'America/Vancouver';` and the same for `app_farms`. However:

- `releases/01_00_xx/01_00_00/00/roles/00_proxy_farms_rest.json` points at `database/roles/proxy_farms_rest.sql`, and that changeset has already run in each environment. The repository's own `farms-liquibase/CLAUDE.md` notes that an in-place edit is never re-applied by `liquibase update` where the changeset already ran.
- `db_preconditions/` is a one-off manual bootstrap run with `psql`.

Until someone runs both `ALTER USER` statements in dev, test and prod, the database still holds `America/New_York` as the default for these roles. The two apps are unaffected, because pgJDBC sends the JVM zone (now explicitly `America/Vancouver`) at connection time. Any other client of these roles still runs in Eastern time: Eastern wall times in `timestamp` columns, and `current_date` rolling over at 21:00 BC time.

### T3. Database-generated times and dates: MEDIUM (unchanged)

The PL/pgSQL code still computes most audit times and "today" checks in the database (see the counts in Section 2). These depend on the tzdata inside the Crunchy PostgreSQL 17 image chosen by the operator, which this repository does not pin.

### N3. CDOGS time zone: LOW (verify)

`farms-legacy/.../cdogs/resource/CdogsOptionsResource.java` now sends `timezone: "America/Vancouver"` with every request. The templates use both `formatD(...)` and `convDate('x', ...)` on epoch values (`createdAt`, `updatedAt`, `reportDate`, `dateOfDeath`). Generate one document after deployment and confirm that `createdAt` / `updatedAt` show BC time, since `convDate` may not apply the time zone option in the same way as `formatD`.

### T6, N4, T7 and T8

- **T6.** The agents read their start and stop times in the JVM zone; they are correct once N1 is confirmed.
- **N4.** `scripts/01_00_xx/` is an older, unwired copy of the bootstrap scripts; updating line 15 there avoids confusion.
- **T7 and T8** are unchanged and have no impact.

---

## 5. Areas of Failure

- **F1 (from N1), High likelihood only if the deployed `farms-legacy` image lacks 8u501+:** from Nov 1, Java-generated times are one hour behind, "today" and expiry checks are wrong from 23:00, and agents start one hour late.
- **F2 (from T3), Medium:** if the PostgreSQL image lacks tzdata 2026b, every database-generated time is one hour off and `current_date` is wrong from 23:00.
- **F3 (from N2), Low to Medium:** any session using the role default runs in Eastern time until the `ALTER USER` statements are applied.

---

## 6. Pre-existing Defects

All defects listed in the previous report (D1 to D3) are fixed on `a31c89b`. No new date-related defects were found in the 45 new commits. One behaviour change to note: the TIP benchmark extract and TIP report `Generated_Date` now print in 12-hour form (`yyyy-MM-dd hh:mm a`); any downstream parser of those files should expect that format.

---

## 7. Potential Resolutions

| # | Action | Owner | Priority |
|---|--------|-------|----------|
| R1 | In each running pod, run `java -version` and confirm **8u501 or later** (`farms-legacy`) and **21.0.12 or later** (`farms-api`). Also confirm `ZoneId.of("America/Vancouver").getRules().getOffset(Instant.parse("2026-12-01T12:00:00Z"))` returns `-07:00` (for example with `jshell` in a test pod, or a one-line health endpoint). If either is older, re-run `build-farms-legacy.yml` / `build-farms-api.yml` to pick up the current base image and redeploy. Consider recording the image digest or JRE version in the build output. | Development and Platform | **Before 2026-11-01** |
| R2 | Run `ALTER USER proxy_farms_rest SET TIMEZONE TO 'America/Vancouver';` and `ALTER USER app_farms SET TIMEZONE TO 'America/Vancouver';` in dev, test and prod, then confirm with `SELECT rolname, rolconfig FROM pg_roles WHERE rolname IN ('proxy_farms_rest','app_farms');`. | DBA | **Before 2026-11-01** |
| R3 | Confirm that the Crunchy PostgreSQL 17 image carries tzdata 2026b, for example `SELECT now() AT TIME ZONE 'America/Vancouver'` and `SELECT * FROM pg_timezone_names WHERE name = 'America/Vancouver'` on or after 2026-11-01 (expect a `-07:00` offset). If not, update the Crunchy operator or image. | Platform | **Before 2026-11-01** |
| R4 | Generate one CDOGS document and compare its `createdAt` / `updatedAt` with the real BC time. | Development | Before 2026-11-01 |
| R5 | Update the stale role script under `scripts/01_00_xx/01_00_00/00/ddl/roles/` to `America/Vancouver`. | Development | Housekeeping |

---

## 8. Verification Checklist

1. `java -version` in each pod (R1).
2. `SHOW TimeZone` from each database client, and the `pg_roles` query (R2).
3. On or after 2026-11-01, save a record in `farms-legacy` and confirm that both Java-set and database-set `when_updated` values match the wall-clock time.
4. Generate a TIP report and benchmark extract dated 2026-12-28 and confirm that the year prints as 2026.
5. Confirm that the import, CHEFS and triage agents start at the configured BC time.
6. Generate a CDOGS document and check its times (R4).

---

## 9. Cross-References

- **nr-brmb-common** report: `farms-api` uses `brmb-common` 2.0.0-SNAPSHOT but does not register its `InstantTypeHandler`.
- **nr-brmb-agristability-powerapps** report: the enrolment Power Apps call the FARMS API.
- **nr-brmb-pit-claim** and **nr-brmb-pim** reports: still pinned to `tomcat:10.1.44-jre21` at the time of their reviews; the AS-4052 approach (bump the tag, add `-Duser.timezone`) applies there too.
