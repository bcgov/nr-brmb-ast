# BC Permanent Pacific Time (PCT, UTC-7): Impact Report for nr-brmb-ast

| | |
|---|---|
| **Prepared** | 2026-10-07 |
| **Repo state** | Scanned upstream `bcgov/nr-brmb-ast`, branch `main` @ `23ed328` (2026-10-07 14:25 PDT, "Merge pull request #175 from bcgov/feature/3986"). The requested fork `dhlevi/nr-brmb-ast` is at `17c7845` (same day, 09:01 PDT) and is **3 commits behind** upstream; the 3 newer commits only change JDBC parameter types for CRA file 22 staging and stop the CRA import when staging fails, and do not touch date handling. |
| **Components** | FARMS (AgriStability / AgriInvest) monorepo. **`farms-legacy`**: Struts 1.2 web app, Java 8, 1,356 main Java files, on `tomcat:9.0.86-jre8`; the main user interface, benefit calculator, CHEFS and CRM integrations, CDOGS document generation, Jasper reports and three scheduled agents. **`farms-api`**: Spring Boot 3.4.10 REST API, Java 21, 204 main Java files, on `tomcat:10.1.44-jre21`, using `brmb-common` 2.0.0-SNAPSHOT. **`farms-liquibase`**: PostgreSQL schema and PL/pgSQL packages (374 `timestamp` and 236 `date` columns, no `timestamptz`). **`crunchy-postgres`** (PostgreSQL 17), **`cdogs/templates`** (11 Word templates), `openshift`, `load-tests`, `farms-api-postman`. |
| **Deadline** | **Sunday 2026-11-01, 02:00 local (09:00 UTC)**, about 3.5 weeks away |
| **Bottom line** | **High risk on the platform.** No code uses a fixed PDT or PST offset, and both apps run in `America/Vancouver` (`/etc/localtime` and `TZ`). However, **both runtime images predate tzdata 2026b**: `farms-legacy` runs JRE 8 from `tomcat:9.0.86-jre8` (February 2024) and needs **8u501 or later**; `farms-api` runs the August 2025 JRE 21 and needs **21.0.12 or later**. `farms-legacy` does almost all of its date and time work with `java.util.Date` and `Calendar` in the JVM zone, so from Nov 1 an unpatched JRE shows and records **times one hour behind**, treats 23:00 to midnight as the next day for "today" and expiry checks, and runs its scheduled agents one hour late (T1). The database code uses `current_timestamp`, `statement_timestamp()` and `current_date` more than 1,600 times (counting both the source packages and their Liquibase changesets), all computed in the session zone with PostgreSQL's own tzdata (T3), and two application roles default that session zone to **`America/New_York`** (T4), which needs verification. **Fix: update both images, set `-Duser.timezone=America/Vancouver`, confirm the Crunchy PostgreSQL image carries tzdata 2026b, and confirm the effective session `TimeZone` of every database user.** |

---

## 1. What changed (same basis as the earlier reports)

- **Government rule.** BC stopped changing clocks after 2026-03-08. On **2026-11-01 clocks do not fall back.** BC stays at **UTC-7** all year, named *Pacific time (PCT)*.
- **IANA tzdata 2026b** models `America/Vancouver` as permanent UTC-7 from 2026-11-01 02:00.
- **JDK builds with the rule:** 8u501, 11.0.32, 17.0.20, 21.0.12 and 25.0.4 or later. Earlier builds treat BC winter as PST (UTC-8).
- **PostgreSQL** applies its own tzdata (from the server image) to `current_timestamp`, `current_date` and conversions into `timestamp` columns, in the session `TimeZone`.
- **New York keeps changing clocks.** The gap between Eastern time and BC time is 3 hours all year until 2026-11-01, then 2 hours in winter and 3 hours in summer.

---

## 2. Summary of findings

| # | Area | Severity | Fails on Nov 1? | Fix |
|---|---|---|---|---|
| T1 | `farms-legacy` on `tomcat:9.0.86-jre8` (JRE 8u402 era); all `Date` / `Calendar` logic, Jasper `REPORT_TIME_ZONE` and CHEFS `@JsonFormat(timezone = "America/Vancouver")` use the stale rules | **High** | **Yes**, if the image is not updated: Java-generated times one hour behind, "today" wrong from 23:00 to midnight, agents run one hour late | R1, R2 |
| T2 | `farms-api` on `tomcat:10.1.44-jre21` (August 2025) | Low | Little visible effect: the API uses `LocalDate` / `LocalDateTime` and lets PostgreSQL compute "now" | R1, R2 |
| T3 | PL/pgSQL (source packages and changesets combined): 1,159 `current_timestamp`, 332 `statement_timestamp()`, 126 `current_date`, 6 `clock_timestamp()`, stored in `timestamp` (without time zone) columns | **Medium** | If the PostgreSQL image lacks tzdata 2026b: database-generated times one hour off and `current_date` wrong from 23:00 to midnight | R3 |
| T4 | Database roles `proxy_farms_rest` and `app_farms` default to `TIMEZONE 'America/New_York'` | **Medium (verify)** | Not caused by the change, but any session that does not override the zone stores Eastern wall time and computes Eastern dates; the Eastern-to-BC gap becomes 2 hours in winter | R4 |
| T5 | CDOGS templates format epoch timestamps (`createdAt`, `updatedAt`, `reportDate`) in the CDOGS service's time zone; the request sets no time zone | Low (verify) | Not caused by the change: printed times depend on the CDOGS server zone | R5 |
| T6 | Scheduled agents (`ImportAgent`, `ChefsAgent`, `BenefitTriageAgent`) run in configured local time windows in the JVM zone | Low | Windows shift one hour on an unpatched JRE (part of T1) | R1 |
| T7 | Crunchy backup schedule in UTC (full and differential at 00:00 UTC) | None | No; backups run at 17:00 local all year instead of alternating 17:00 / 16:00 | n/a |
| T8 | CRM transfer timestamps `yyyy-MM-dd H:mm:ssZ` include the offset | None | No; the offset always matches the wall time | n/a |
| D1 | Week-year `YYYY` in four date formats (pre-existing, reproduced) | **Medium** | Not related to the change: dates from 2026-12-27 to 2026-12-31 print as **2027** | R6 |
| D2 | `WebADEDeveloperFilter` uses `"YYYMMDD"` (week-year and day-of-year) (pre-existing) | Low | Not related to the change | R6 |
| D3 | Shared static `SimpleDateFormat` instances used across threads (pre-existing) | Low | Not related to the change | R6 |

---

## 3. Overview

`farms-legacy` and `farms-api` are two live front ends on the same PostgreSQL database. Both Dockerfiles link `/etc/localtime` to `America/Vancouver` (`farms-legacy/Dockerfile` line 31, `farms-api/Dockerfile` line 25), and both deployments set `TZ` from `TIME_ZONE` (`openshift/farms-legacy-deployment.yaml` and `farms-api-deployment.yaml` lines 84-88; `.github/workflows/openshift-deploy.yml` line 157). Neither `setenv.sh` sets `-Duser.timezone`.

The JVM therefore runs in `America/Vancouver` but applies the rules in its own time zone database. pgJDBC sends that zone ID to PostgreSQL at connection start, and PostgreSQL then computes `current_timestamp` and `current_date` with its own tzdata. Most business logic lives in PL/pgSQL packages (`farms-liquibase/database/*_pkg`), and both apps call them.

---

## 4. Areas of Concern

### T1. `farms-legacy` on a stale Java 8 runtime: HIGH

- **Runtime.** `tomcat:9.0.86-jre8` dates from February 2024 (JRE 8u402 era). No Java 8 build before 8u501 carries the BC rule.
- **Date handling.** The app uses `java.util.Date` and `Calendar` throughout (88 files use `SimpleDateFormat`, `Calendar`, `new Date()` or JDBC `getDate` / `setDate` / `getTimestamp` / `setTimestamp`). `util/DateUtils.java` lines 232-247 convert between `Date` and `LocalDate` and compute `todayAtStartOfDay()` with `ZoneId.systemDefault()`; `isExpired`, `isEffective`, `oneYearAgo` and the agents build on these.
- **Explicit `America/Vancouver`.** `report/CobReportRenderer.java` line 41 sets `JRParameter.REPORT_TIME_ZONE` to `America/Vancouver`, and the CHEFS resources (`NppSubmissionDataResource.java` lines 38 and 40, `StatementASubmissionDataResource.java` lines 51 and 53, `InterimSubmissionDataResource.java` lines 41 and 43) use `@JsonFormat(pattern = "yyyy-M-d", timezone = "America/Vancouver")`. This is the right design, but it still needs a JRE that knows the new rule.

Calendar dates entered in the Struts forms are parsed and written in the same JVM zone, so they round-trip correctly even on a stale JRE. What fails on an unpatched JRE after Nov 1:

1. **Times.** Every time the Java code creates (`new Date()`, `setTimestamp`) is stored and displayed one hour behind real BC time, while times created by the database (T3) are correct if PostgreSQL is patched. The same table can then hold a mix of both.
2. **"Today".** Between 23:00 and midnight, `todayAtStartOfDay()`, `isExpired` and `isEffective` use the next day.
3. **Agents.** See T6.
4. **Reports and CHEFS.** Jasper and the CHEFS date parsing apply PST, consistent with the JVM, so they shift only near midnight.

### T2. `farms-api` on a stale Java 21 runtime: LOW

The API models use `LocalDate` (13 fields) and `LocalDateTime` (28 fields, mainly `createDate` and `updateDate`), and its mappers use `now()` (52) and `current_date` (11) in PostgreSQL (for example `BenchmarkPerUnitMapper.xml` lines 53, 76 and 133 and `ProductiveUnitCodeMapper.xml` lines 11, 16 and 22). It does not register the `brmb-common` `InstantTypeHandler`. CRA import dates are parsed with `ParseUtils.DATE_FORMAT` (`yyyyMMdd`) and stored in the same JVM zone. The stale JRE therefore has little visible effect, but it should still be updated to 21.0.12 or later.

### T3. Database-generated times and dates: MEDIUM

Across the PL/pgSQL source packages and their Liquibase changesets (each routine appears in both places), the code uses `current_timestamp` (1,159), `statement_timestamp()` (332), `current_date` (126) and `clock_timestamp()` (6), mostly for `when_created` and `when_updated` and for effective and expiry checks. Stored in `timestamp` (without time zone) columns, these values become wall time in the session `TimeZone`. If the Crunchy PostgreSQL 17 image does not carry tzdata 2026b, they are one hour off from Nov 1, and `current_date` is wrong between 23:00 and midnight.

### T4. Database roles default to New York time: MEDIUM (verify)

- `farms-liquibase/database/roles/proxy_farms_rest.sql` line 15 (and its changeset `scripts/01_00_xx/01_00_00/00/ddl/roles/farms.ddl.create_login_proxy_farms_rest.sql` line 15): `ALTER USER proxy_farms_rest set TIMEZONE to 'America/New_York';`
- `farms-liquibase/db_preconditions/logins/farms.ddl.create_login_app_farms.sql` line 15: the same for `app_farms`.

pgJDBC normally sends the JVM zone (`America/Vancouver`) when it connects, and connection settings take precedence over role defaults, so the two apps probably run in BC time. Any session that does not send a zone, however, runs in Eastern time: `psql` and scripted jobs, other JDBC clients whose JVM is not in BC time, and possibly the Jasper report server if it connects as one of these roles. Such sessions store Eastern wall time in `timestamp` columns and compute `current_date` in Eastern time, so between 21:00 (22:00 in winter after Nov 1) and midnight BC time they see the next day. The intent of this setting is unclear, and it should be confirmed with `SHOW TimeZone` from each client.

### T5. CDOGS document times: LOW (verify)

The templates format `{d.createdAt:convDate('x', 'LLLL')}` and `{d.updatedAt:convDate('x', 'LLLL')}` (10 templates each), `{d.reportDate:convDate('x', 'LL')}` (`CdogsServiceImpl.java` line 463 sets `reportDate` to `new Date()`), and several `formatD('LL', 'x')` fields. All take epoch milliseconds, which CDOGS renders in its own time zone. `CdogsOptionsResource` sends only `convertTo`, `reportName` and `overwrite`, with no time zone. Dates at BC midnight print correctly in any zone east of BC, but full timestamps (`LLLL`) and "now" values print in whatever zone CDOGS uses. This does not change with the BC rule unless CDOGS itself renders in `America/Vancouver` with stale data. Check a generated document's `createdAt` time against the real time.

### T6. Scheduled agents: LOW

`agent/ImportAgent.java` lines 278-282, `ChefsAgent.java` lines 227-231 and `BenefitTriageAgent.java` lines 228-232 build today's start and stop times with `DateUtils.setTime(now, "HH:mm")` in the JVM zone. On an unpatched JRE after Nov 1, a 02:00 window opens at 03:00 real BC time.

### T7 and T8. No impact

- `crunchy-postgres/charts/crunchy-postgres/values.yaml` lines 52-55 schedule backups in UTC with no local-time assumption.
- `crm/CrmTransferFormatUtil.java` line 80 formats `yyyy-MM-dd H:mm:ssZ`; the offset always matches the wall time, so the instant is correct.

---

## 5. Areas of Failure

- **F1 (from T1), High likelihood if the image is not updated:** From Nov 1, `farms-legacy` records and shows Java-generated times one hour behind, mixes them with correct database-generated times, applies "today" and expiry checks to the next day from 23:00, and runs import, CHEFS and triage agents one hour late.
- **F2 (from T3), Medium:** If the PostgreSQL image is not updated, every database-generated audit time is one hour off and `current_date` is wrong from 23:00.
- **F3 (from T4), to verify:** Any database session running in the role default (`America/New_York`) writes times 2 or 3 hours ahead of BC time and sees the next day from 21:00 or 22:00 BC time.

---

## 6. Pre-existing Defects Found (independent of this change)

- **D1.** Week-year `YYYY` instead of calendar year `yyyy`:
  - `farms-legacy/.../dao/TipReportDAO.java` line 678: `new SimpleDateFormat("YYYY-MM-dd HH:mm a")`.
  - `farms-legacy/.../dao/TipBenchmarkExtractDAO.java` line 50: the same pattern, used for `Generated_Date` (lines 189 and 204).
  - `farms-legacy/.../service/impl/ReportServiceImpl.java` lines 433 and 503: `new SimpleDateFormat("MMMM dd YYYY")`.

  Reproduced on Java 21: 2026-12-27 to 2026-12-31 format as "2027-12-27" to "2027-12-31" and "December 27 2027" to "December 31 2027". `HH` with `a` also prints a 24-hour time with an AM/PM marker. The fix is `yyyy` (and `hh` with `a`, or `HH` without it).
- **D2.** `ca/bc/gov/webade/developer/j2ee/WebADEDeveloperFilter.java` line 260: `new SimpleDateFormat("YYYMMDD")` combines week-year with day-of-year (`DD`). It appears to be a developer-only filter.
- **D3.** `util/DateUtils.java` lines 21-22 (`TIME_FORMAT`, used by all three agents through `setTime`), `TipBenchmarkExtractDAO.java` line 50 and `farms-api/.../services/csv/ParseUtils.java` line 20 share static `SimpleDateFormat` instances, which are not thread-safe.

---

## 7. Potential Resolutions

| # | Action | Owner | Priority |
|---|--------|-------|----------|
| R1 | Move `farms-legacy` to a Tomcat 9 image on JRE 8u501 or later (or to a supported newer JRE) and `farms-api` to a Tomcat 10.1 image on JRE 21.0.12 or later. Confirm with `java -version` and with `ZoneId.of("America/Vancouver").getRules().getOffset(Instant.parse("2026-12-01T12:00:00Z"))` returning `-07:00`. | Development and Platform | Before 2026-11-01 |
| R2 | Add `-Duser.timezone=America/Vancouver` to `CATALINA_OPTS` in both `setenv.sh` files, and confirm that `vars.TIME_ZONE` is `America/Vancouver` in every GitHub environment. | Development | Before 2026-11-01 |
| R3 | Confirm that the Crunchy PostgreSQL 17 image carries tzdata 2026b (for example `SELECT now() AT TIME ZONE 'America/Vancouver'` after 2026-11-01). | Platform | Before 2026-11-01 |
| R4 | Run `SHOW TimeZone` from each client (both apps, the Liquibase job, the Jasper report server, operator `psql` sessions). Unless Eastern time is intended, change the role defaults to `America/Vancouver` in the role scripts and on each database. | DBA and Development | Before 2026-11-01 |
| R5 | Generate one CDOGS document and compare its `createdAt` / `updatedAt` times with the real time; if CDOGS renders in another zone, pass the CDOGS time zone option or send pre-formatted BC times. | Development | Before 2026-11-01 |
| R6 | Fix D1 (`YYYY` to `yyyy`) before 2026-12-27, and fix D2 and D3. | Development | D1 before 2026-12-27 |

---

## 8. Verification Checklist

1. In each pod, run `java -version` and confirm 8u501+ (`farms-legacy`) and 21.0.12+ (`farms-api`); confirm `TimeZone.getDefault().getID()` is `America/Vancouver`.
2. After 2026-11-01 in a test environment, save a record in `farms-legacy` and confirm that Java-set and database-set `when_updated` values both match the wall-clock time.
3. From each database client, run `SELECT current_setting('TimeZone'), now(), current_date` and confirm `America/Vancouver` and a `-07` offset.
4. Generate a TIP report and a benchmark extract dated 2026-12-28 and confirm that the year prints as 2026 (after D1 is fixed).
5. Confirm that the import, CHEFS and triage agents start at the configured BC time.

---

## 9. Cross-References

- **nr-brmb-common** report: `farms-api` uses `brmb-common` 2.0.0-SNAPSHOT (model, rest-common, rest-client, persistence); its `InstantTypeHandler` finding does not apply here because the API does not register it.
- **nr-brmb-agristability-powerapps** report: the enrolment Power Apps call the FARMS API.
- **nr-brmb-pit-claim** and **nr-brmb-pim** reports: same `tomcat:10.1.44-jre21` image and Crunchy PostgreSQL platform.
