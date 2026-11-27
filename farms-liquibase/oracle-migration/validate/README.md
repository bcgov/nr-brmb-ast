# Migration-day validation scripts (all read-only)

Postgres checks run in Windows Python (psycopg2) against the pgbouncer port-forward
(`localhost:5436`, user `app_farms`, password from `pgpass.conf`). Each exits non-zero on failure.
`table.sql` / `sequence.sql` are ora2pg's output, e.g. `\\wsl$\Ubuntu\home\<user>\farmsprod\ora2pg\migprod\schema\...`.

| Step | Run | Script |
|---|---|---|
| after `drop_farmsprod.sql` | `python check_empty.py 5436 farmsprod` | only the bootstrap state is left |
| after `import_all.sh`, before Liquibase | `python validate_structure.py 5436 farmsprod <table.sql>` | every table/column/index/constraint from ora2pg (table.sql plus INDEXES_/CONSTRAINTS_/FKEYS_table.sql beside it) is present and validated |
| after `import_all.sh` | `./validate_counts.sh` (WSL) | Oracle vs Postgres row counts, per table, must match exactly |
| after `import_all.sh` | `python validate_sequences.py 5436 farmsprod <sequence.sql>` | sequence START values match Oracle; next value > MAX(key) |
| after Liquibase | `validate_structure.py` and `validate_sequences.py` again, then `validate_counts.sh` | extras (farm_urls, url_id, ...) are listed as INFO |

Notes
- `validate_sequences.py` prints `setval` statements for any sequence that is behind its data. It never runs them.
  `farm_sga_seq` is known to fail right after `import_all.sh` (Oracle exports START 1 but the table holds keys up to 2073+);
  the Liquibase changeset `01_00_00/00/farms/farm_sga_seq_correct` fixes it, so it must pass after Liquibase.
  Expect that one FAIL at step 10 of the runbook (`../README.md`) and none at step 12. Run the printed `setval` by
  hand only if Liquibase is skipped.
- Row counts after Liquibase should still match: the 02_dml scripts are no-ops on PROD-derived data.
