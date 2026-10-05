# Jasper COB integration (2023 onward)

Status: Andrew confirmed there was no particular reason for the old library pins and
said upgrading is probably fine, while noting that Digester and Collections are major
version upgrades that may require code changes. The three upgrades are included here;
the targeted compatibility tests below do not replace full application verification.
Local repository authentication blocks the full WAR build. A successful CI build and
DEV smoke test are still required before release. Local verification did not deploy
the application or write to the DEV database.

## Flow

- The existing Print COB and Reprint COB actions still enforce their current permissions,
  scenario state and assignment checks. No new public download endpoint is introduced.
- `ReportServiceImpl.saveCob/updateCob` route program years 2023 onward to
  `CobReportRenderer`. Older years retain their existing Oracle report selection.
- The renderer compiles `/reports/BenefitNotice2023-postgresql.jrxml` once per classloader,
  fills a separate print per request with `IN_SCENARIO_ID` as an Integer, and exports PDF.
- The connection comes from the existing application transaction/JNDI datasource.
  It is passed to every subdataset through `REPORT_CONNECTION`. There is no embedded
  database URL, password, local port-forward or dependency on a running Jaspersoft Studio.
- Rendering must finish before any document is cleared. The Jasper-only DAO operation
  locks the scenario, checks insert/reprint state, and writes document metadata and PDF
  in the same caller-owned transaction. A failure rolls back on transaction close.
  Existing coverage-notice and Oracle DAO methods have not been changed.
- PDFs remain in `farms.farm_benefit_calc_documents` and are opened through the existing
  view/download actions. Previously stored PDFs are not regenerated on read.
  Print COB now uses the registered `viewBenefitDocument.do` route, replacing its stale
  `viewCob.do` link; Reprint already used the registered route.
- Automatic COB generation after verification is enabled for 2023 onward, still respecting
  `-Dgenerate.cob.enabled=N`. Earlier-year automatic generation remains disabled as it was
  before this change. Coverage notices continue using CDOGS.

## Template and runtime

The packaged copy originates from
`C:\MAL_FARM\output\BenefitNotice2023-postgresql.jrxml`; that source is unchanged.
The application copy removes Studio adapter properties and the sample scenario default,
and explicitly passes scenario/year to `DS_BPU_STRUCTURAL_CHANGE` (which previously
used its preview defaults). SQL calculations, layout and other report expressions are
otherwise retained; this integration does not re-validate benefit calculations.

JasperReports 6.21.5 retains the 6.x JRXML format and Java 8 compatibility. Do not replace
it with 7.x without a template migration. Compilation uses the bundled ECJ compiler,
so the Java 8 JRE container does not need `javac` installed.

The font extension maps Arial and SansSerif to embedded DejaVu Sans supplied by
`jasperreports-fonts`. This avoids missing Windows fonts on Linux, but can change text
metrics: visually compare the actual dev notice before release. No proprietary Windows
font files are copied. Locale is Canadian English; report timezone is America/Vancouver.
The renderer virtualizes pages in a private temporary directory and cleans up after
export/failure. Queries have a 180-second timeout and reports a 500-page limit.

## Outstanding build prerequisites

The old application pins override Jasper's transitive dependencies. A diagnostic run with
the existing pins fails with `NoClassDefFoundError: ...digester/SetNestedPropertiesRule`.
The following versions are included following Andrew's response:

| Library | Original | Updated version |
| --- | --- | --- |
| commons-beanutils | 1.6 | 1.11.0 (Jasper 6.21.5 dependency) |
| commons-digester | 1.5 | 2.1 (Jasper 6.21.5 dependency) |
| commons-collections | 2.1 | 3.2.2 (BeanUtils dependency) |

Full Maven compilation also needs access to the existing `javax.mail:mail:1.3.3` and
`javax.xml:namespace:1.0.1` artifacts. The configured Artifactory returned HTTP 401 locally;
these legacy JARs are not available from Maven Central. Do not substitute different JARs
under those version coordinates or commit repository credentials.

## Verification

The added tests use synthetic JDBC connections, never a live database:

```text
mvn --settings=../settings.xml -DskipTests=false "-Dtest=CobReportRendererTest,CobPersistenceTest,CobStrutsCompatibilityTest" test
mvn --settings=../settings.xml -DskipTests package
```

Tests cover template compilation/cache, all list parameter/connection mappings, PDF
export/font embedding, no-data/invalid-ID failures, insert/reprint procedure selection,
caller-owned transactions, persistence failure and stale insert/reprint requests.
The full app build is currently blocked by the repository prerequisites above. Initially,
the renderer and DAO tests passed in an isolated harness with Jasper's declared dependencies.
Those 10 tests passed under Java 8 (1.8.0_144), and the synthetic first/last pages were
rendered and visually checked. The PDF review caught an encoding issue in the packaged
checkmark, which was corrected and covered by a regression assertion.
That isolated result does not establish application-wide Struts compatibility.

After the provisional upgrades, Maven's dependency graph confirms BeanUtils 1.11.0,
Digester 2.1 and Collections 3.2.2. A second diagnostic harness copies the current application
dependency declarations/pins, excluding ONLY the two unavailable JavaMail/Namespace JARs,
and compiles the COB/DAO/Struts-form test slice. All 13 tests passed on Java 8:
the original 10 plus parsing the real `struts-config.xml` (including COB permissions),
populating the existing `ReportForm` from request-style arrays, and creating/populating
a typed Struts dynamic form. No live database or web server is used.

The diagnostic harness is `C:\MAL_FARM\output\cob-application-classpath-check-pom.xml`.
Results are in `C:\MAL_FARM\output\cob-app-classpath-target\surefire-reports`.
It is not a WAR build or deployable substitute: it does not validate the whole application,
JavaMail, or the end-to-end service/UI flow. The full app still reports HTTP 401/403 when
resolving the two existing legacy artifacts. Credentials should be configured locally,
not pasted into chat or committed.

## Rolling back the library upgrades

The original versions are recorded above. Reverting those three versions alone makes
the Jasper path incompatible again, so do not deploy that combination. If rollback is
required, also back out the Jasper integration or isolate Jasper from the shared legacy
classpath. Preserve unrelated application changes when preparing a rollback.

Do not run the entire legacy test suite against a shared database without reviewing it;
some tests are integration tests with writes.

## Dev smoke test after a successful build

1. Build/deploy or restart the local Tomcat application with the updated WAR/classpath.
   Confirm its normal datasource targets the intended dev environment, not a hardcoded URL.
2. On a 2023+ scenario you are authorized to test, use Print COB; verify the stored PDF
   opens and its PIN/year/payment match the selected scenario and saved claim.
3. Reprint the same scenario; ensure the document updates without a duplicate row.
4. Verify a combined farm (known dev example: scenario 1081568, year 2024, PIN 4375671),
   a non-combined farm, zero benefit, cash/non-cash adjustments, and multi-page lists.
5. Compare page numbering, PIN, negative currency, combined share, benchmark rows and
   closing text with Studio. Check long labels/wrapping with the portable fonts.
6. Verify automatic generation only after the scenario state change is committed, and
   verify the existing `generate.cob.enabled=N` switch suppresses it when requested.
7. Check existing saved PDFs, pre-2023 historical handling, coverage notices and ordinary
   Struts form binding/navigation. Those historical templates are not migrated by this work.

No database migration, report-server deployment or SQL function installation is required
for the new path. Normal report-query SELECT and existing COB procedure permissions
must be available to the application role. No dev database writes or deployment were
performed during local verification.
