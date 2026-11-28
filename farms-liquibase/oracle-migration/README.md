# FARMS PROD: Oracle to Postgres migration runbook

Moves the PROD FARMS database from Oracle to Postgres (`farmsprod` on the Crunchy cluster, namespace
`e980f4-prod`). The goal is minimal PROD downtime.

Path: DBA `expdp` on Oracle PROD -> dump to a government shared drive -> dump to the temporary Oracle server -> `impdp` into its `FARM` schema
-> ora2pg loads Postgres -> Liquibase applies the DDL -> validate -> go live.

Timings come from three dry runs (TEST data exported 2026-06-02, 19.6 GB, 51,848,475 rows, 135 tables).
Most steps scale with data volume, so ask the DBA for PROD's size
(`SELECT ROUND(SUM(bytes)/1024/1024/1024,1) FROM dba_segments WHERE owner='FARM';`) and compare it with the oracle database used for dry runs of the data import to determine if more storage space is needed on the oracle server, the OpenShift quota and volumes; and estimate additional time required for the real data load.

Other files in this folder: `validate/` (validation scripts, see `validate/README.md`),
`pre-work/gen_drop_script.py` (generates the wipe script), `pre-work/oracle_set_passwords.sql` (changes the
passwords on the temporary Oracle server) and `ora2pg.conf.example`.

Replace these placeholders with your own values. Don't commit the real ones, this repository is public:
`<temporary-oracle-server>` (the temporary Oracle server that holds the PROD data during the migration), `<oracle-sid>`,
`<oracle-tns-name>`, `<dump-directory>` (the Data Pump directory on that server), `<primary pod>` (the current Postgres primary) and `<repo>` (your clone of this repository).

## Time summary

| # | Step | Dry run | Real run estimate |
|---|---|---|---|
| 1 | Clear the `FARM` schema on the temporary Oracle server | Not timed | Not estimated; done before the dump arrives |
| 2 | Scale the PROD apps to 0 | Not timed (a single command) | Under 1m |
| 3 | Wipe farmsprod, `check_empty.py` | The drop script took 6s; `check_empty.py` takes seconds | Same: under 1m |
| 4 | DBA runs `expdp` on Oracle PROD | 1m41s on QA (serial, statistics excluded) | At least 1h (a guess: the DBA's turnaround is unknown) |
| 5 | Copy dump from the shared drive to the temporary Oracle server | 2m30s for a 1.04 GB test file, direct. (About 20m for 19.6 GB in the earlier copy to `<temporary-oracle-server>`.) | About 47m for 19.6 GB at the direct-copy rate |
| 6 | `impdp` into temporary Oracle server | about 7m | Same: about 7m |
| 7 | Run the 2.44.7 data scripts on the temporary Oracle server | Not done in the dry runs | 10m (allowed; the scripts run in seconds) |
| 8 | Export the ora2pg schema and diff it | `export_schema.sh` about 1m30s (2026-11-18); the diff takes seconds | Same: about 1m30s |
| 9a | ora2pg data load | 71m in runs 1 and 2; **27m in run 3** (1m schema create, 26m copy) | About 27m, scales with row count (51.8M in the dry run) |
| 9b | ora2pg indexes (183) | 16m40s, 18m28s; **2m20s in run 3** | About 2m20s, scales with data |
| 9c | ora2pg constraints (1,555 statements) | 9m44s, 9m09s; **2m29s in run 3** | About 2m30s, scales with data |
| 9d | ora2pg FKs (183) | 6m14s, 4m24s; **56s in run 3** | About 1m, scales with data |
| 10 | Validators | `validate_counts.sh` 27s in run 2 (1m10s in run 1), the rest is seconds | Same: under 1m |
| 11 | Liquibase | Whole workflow 5m01s in run 2 (Job 3m35s), 4m01s in run 3 (Job 2m33s) | About 4 to 5 min |
| 12 | Validators again | about 1m10s | Same: about 1m |
| 13 | Go live | Apps up in about 1m. Full backups run in the background (repo1 39m, then repo2 41m). Smoke test not timed. | Under 5m, plus smoke testing |
| 14 | Secure and remove the PROD data | Not done in the dry runs | Not estimated; after go-live, so no downtime |

Rows 9a to 9d are one `import_all.sh` run (see step 9): about 33m in run 3, after the prod CPU limits were
raised, down from about 1h43m in run 2.

The real run estimates add up to at least **2h50m**, before smoke testing, 1h of it the guess for the DBA's export. That is run time
only: each step also takes time to review the result, decide, and start the next one.

## Preparation

### Can be done days ahead

- **Install ora2pg** in WSL, once. The dry runs used ora2pg 25.0 built from source on Ubuntu 24.04, with Oracle
  Instant Client 23.26 (basic and SDK).
  ```bash
  # packages
  sudo apt update
  sudo apt install -y libdbi-perl libdbd-pg-perl build-essential libaio1t64 unzip curl postgresql-client tmux
  sudo ln -sf /usr/lib/x86_64-linux-gnu/libaio.so.1t64 /usr/lib/x86_64-linux-gnu/libaio.so.1

  # Oracle Instant Client
  sudo mkdir -p /opt/oracle && cd /opt/oracle
  sudo curl -LO https://download.oracle.com/otn_software/linux/instantclient/instantclient-basic-linuxx64.zip
  sudo curl -LO https://download.oracle.com/otn_software/linux/instantclient/instantclient-sdk-linuxx64.zip
  sudo unzip -o instantclient-basic-linuxx64.zip
  sudo unzip -o instantclient-sdk-linuxx64.zip
  sudo ln -sfn /opt/oracle/instantclient_* /opt/oracle/instantclient
  echo /opt/oracle/instantclient | sudo tee /etc/ld.so.conf.d/oracle-instantclient.conf
  sudo ldconfig
  echo 'export ORACLE_HOME=/opt/oracle/instantclient' >> ~/.bashrc
  echo 'export LD_LIBRARY_PATH=$ORACLE_HOME' >> ~/.bashrc
  source ~/.bashrc

  # Perl driver for Oracle
  sudo -E cpan DBD::Oracle

  # ora2pg
  mkdir -p ~/src && cd ~/src
  curl -L -o ora2pg-25.0.tar.gz https://github.com/darold/ora2pg/archive/refs/tags/v25.0.tar.gz
  tar xzf ora2pg-25.0.tar.gz
  cd ora2pg-25.0
  perl Makefile.PL
  make
  sudo make install
  ora2pg --version
  ```
- **ora2pg project.** Create it once:
  ```bash
  mkdir -p ~/farmsprod/ora2pg
  ora2pg --project_base ~/farmsprod/ora2pg --init_project migprod
  cp <repo>/farms-liquibase/oracle-migration/ora2pg.conf.example ~/farmsprod/ora2pg/migprod/config/ora2pg.conf
  ```
  Then change these two lines in the generated `export_schema.sh`, so that only what the load needs is exported
  (packages and other code come from Liquibase). `import_all.sh` is used as generated.
  ```bash
  EXPORT_TYPE="SEQUENCE SEQUENCE_VALUES TABLE TYPE"
  SOURCE_TYPE=""
  ```
- **`ora2pg.conf`** (`~/farmsprod/ora2pg/migprod/config/ora2pg.conf`, not in git): fill in the placeholders in the
  copy of `ora2pg.conf.example`. Keep **`DROP_FKEY 0`**. Never print the file whole. Use
  `grep -vi pwd config/ora2pg.conf`.
- **Python, in both Windows and WSL.** They are separate installations.
  - Windows: install Python, then `python -m pip install psycopg2-binary`. It runs `gen_drop_script.py`,
    `check_empty.py`, `validate_structure.py` and `validate_sequences.py`, from PowerShell.
  - WSL: the `python3` that comes with Ubuntu is enough. Only `validate_counts.sh` uses it, and it needs no extra
    packages.
- **pgAdmin 4** on Windows. Step 3 uses the `psql` that comes with it. If you don't want pgAdmin, install `psql`
  another way and use its path in step 3.
  - Register the PROD server in pgAdmin, connecting through the port-forward: host `localhost`, port `5436`,
    maintenance database `farmsprod`, username `app_farms`. The connection works only while the port-forward is
    running.
- **All MAL_FARM releases up to and including 2.44.5 are in Oracle PROD** before the export. Release 2.44.7 was
  cancelled for Oracle PROD. Its package changes are in `farms-liquibase`, and step 7 runs its data scripts.
- **2.44.7 data scripts:** download them from the SVN tag, ready to run against Oracle.
- **Backup space:** check that the backup volume (repo1) has room for the load. See
  [Backups and WAL after a load](#backups-and-wal-after-a-load)
- **Set `max_wal_size` to 1GB** if it isn't already, so that the load produces less WAL. Step 13 sets it back.
  In WSL, logged in with `oc`:
  ```bash
  oc patch postgrescluster crunchy-postgres-prod -n e980f4-prod --type=merge \
    -p '{"spec":{"patroni":{"dynamicConfiguration":{"postgresql":{"parameters":{"max_wal_size":"1GB"}}}}}}'
  ```
  No restart is needed. After a few minutes, confirm that this shows `1GB`:
  `oc exec -n e980f4-prod <primary pod> -c database -- psql -tAc "show max_wal_size"`
- **Deploy the `farms-api` and `farms-legacy` versions you intend to go live with** (GitHub Actions >
  **openshift deploy**, `ENVIRONMENT_NAME=prod`), and check that they start. Do this before step 2: a deploy sets
  the app back to 1 replica.
- **Check the database changes on `main`.** farmsprod is wiped, so step 11 applies everything on `main`. Make sure
  the changes you want in PROD are merged, and that nothing there is not ready for PROD. Changes made since the
  last dry run haven't been tried against migrated data. To list them:
  `git fetch && git log --oneline --since=<last dry run date> origin/main -- farms-liquibase/database farms-liquibase/releases farms-liquibase/changelog.json`
- **Restrict access to the temporary Oracle database schema**, before it receives any PROD data, so that other company
  staff can't reach that data. `oracle_set_passwords.sql` prompts for a password and sets it on `FARM` and
  `PROXY_FARM_ADMIN`. Run it with SQL*Plus from `pre-work/`:
  ```
  sqlplus system@<oracle-tns-name> @oracle_set_passwords.sql
  ```
  Use the new `FARM` password in the later steps.
- [Steps](#steps) 1 to 3 can also be done in advance of receiving the data dump.

### At the start of each session

These may not survive for days, so check them before starting, and again after any break.

- **Port-forward:** keep `oc port-forward` to pgbouncer on `localhost:5436` running in a WSL terminal for the
  whole run. If it drops, restart it. Re-run any ora2pg step that was interrupted.
- Run tmux in WSL (`tmux new -s ora2pg`) so long steps survive a dropped terminal.
- **`pgpass.conf`** (`%APPDATA%\postgresql\pgpass.conf`) has the `app_farms` password for `localhost:5436`.

## Steps

### 1. Clear the `FARM` schema on the temporary Oracle server
This can and should be done before the dump arrives. It must be done: the `impdp` command (step 6) sets no `TABLE_EXISTS_ACTION`, so it
skips any table that already exists and leaves its old rows. In PL/SQL Developer, browse each object type below,
select all the objects, right-click and drop them:

1. Packages
2. Types
3. Triggers
4. Procedures
5. Functions
6. Materialized views
7. Views
8. Tables
9. Sequences

### 2. Scale the PROD apps to 0
This can be done before the dump arrives. In WSL, logged in with `oc`:
```bash
oc scale deployment farms-api-deployment-prod farms-legacy-deployment-prod --replicas=0 -n e980f4-prod
```

```bash
oc get deploy farms-api-deployment-prod farms-legacy-deployment-prod -n e980f4-prod   # both 0/0
```
Do this before every wipe or load, dry runs included.

### 3. Wipe farmsprod, then check it
This can also be done before the dump arrives.
Run these three blocks one at a time in PowerShell, from any folder inside `<repo>`: each one starts by going to the repository root.

Generate the drop script. Do this right before using it: it reflects the catalog at generation time.

```powershell
cd (git rev-parse --show-toplevel)
python farms-liquibase\oracle-migration\pre-work\gen_drop_script.py 5436 farmsprod farms-liquibase\oracle-migration\pre-work\drop_farmsprod.sql
```

Review `pre-work\drop_farmsprod.sql` (compare it with the last version, if you kept one). Then run it. It runs in
a single transaction, is guarded, and rolls back unless clean.

```powershell
cd (git rev-parse --show-toplevel)
& "$env:LOCALAPPDATA\Programs\pgAdmin 4\runtime\psql.exe" -X "host=localhost port=5436 dbname=farmsprod user=app_farms" -f farms-liquibase\oracle-migration\pre-work\drop_farmsprod.sql
```

Check the result:

```powershell
cd (git rev-parse --show-toplevel)
python farms-liquibase\oracle-migration\validate\check_empty.py 5436 farmsprod
```
`check_empty.py` must say `RESULT: PASS` (only the bootstrap state is left). The wipe also drops
`databasechangelog*`, so Liquibase starts from nothing. Roles are cluster-wide and survive the wipe, which the
`proxy_farms_rest` changeset handles. Time: 6s for the drop script on a fully loaded database.

### 4. DBA exports Oracle PROD
Shut down the Oracle PROD app (MAL_FARM) first.

The DBA runs `expdp` as the schema owner or as `system` with `SCHEMAS=FARM`. The dry run used this on QA,
serial and without parallelism. Ask the DBA to add `EXCLUDE=STATISTICS`, which ora2pg doesn't need.

```
expdp farm@<oracle-tns-name> DIRECTORY=DATA_PUMP_DIR DUMPFILE=expdp.<who>.farm.<yyyymmdd>.<env>.dmp VERSION=12.2.0 LOGFILE=expdp.<who>.farm.<yyyymmdd>.<env>.log EXCLUDE=STATISTICS
```
Enter the password at the prompt, not on the command line. Check the log for "successfully completed" and
record the dump size and elapsed time. Time: 1m41s, 19.6 GB, serial on QA. For the real run allow at least 1 hour (a guess), because the DBA's turnaround
is unknown.

### 5. Copy the dump from the shared drive to the temporary server
The DBA provides the path of a Windows shared directory, which needs the government VPN. Copy the dump straight
from there to the server, without saving a copy on your machine first. The data still passes through your machine,
so keep it awake and connected until the copy finishes.

1. Connect to `<temporary-oracle-server>` with WinSCP, as an account that can write to `<dump-directory>`, and go to that
   directory.
2. Drag the dump file from the shared directory into `<dump-directory>`.
3. Check that the size matches the file on the shared drive (and the SHA256 hash, if the DBA provides one).
4. If the DBA zipped or gzipped the file, unzip it in `<dump-directory>` with an SSH client such as PuTTY:
   `unzip <file>.zip` or `gunzip <file>.gz`.

Time: a 1.04 GB test file took 2m30s (about 7 MB/s), so a 19.6 GB dump would take about 47 minutes at that rate.

### 6. `impdp` into the temporary database
Run `impdp` into `<temporary-oracle-server>`, SID `<oracle-sid>`, schema `FARM`. `<dump-file>` is the name of
the `.dmp` file without the extension.

```
impdp farm@<oracle-tns-name> DIRECTORY=DATA_PUMP_DIR DUMPFILE=<dump-file>.dmp SCHEMAS=FARM LOGFILE=<dump-file>.dmp.<yyyymmdd>.<env>.log
```
Enter the password at the prompt, not on the command line.

Time: about 7m in the dry run.

### 7. Run the 2.44.7 data scripts on the temporary Oracle server
Release 2.44.7 was cancelled for Oracle PROD, so run its data scripts against the imported `FARM` schema,
connected as `FARM`: `00/farm.dml.sql`, then `01/farm.dml.sql`. The data load then carries the changes into
farmsprod. Do this after `impdp` and before step 8, so that the export has the advanced sequence value.

- The `bceid_form_ind` script ends with a count of rows still null. It should be 0.
- The other scripts insert some placeholder settings. Set the real GUIDs and API keys in the app after go-live. Ticket AS-4104 in the next sprint covers this.
- Run only the data scripts, not the release's package update: the Postgres code comes from Liquibase.

Time: allow 10m (the scripts run in seconds).

### 8. Export the ora2pg schema from Oracle, and diff it
**Run this after `impdp` and the data scripts.** The export holds the sequence values as well as
the table definitions, so one made before `impdp` would load the old sequence values into farmsprod. The diff
shows whether anything else changed. In WSL:

```bash
cd ~/farmsprod/ora2pg/migprod
prev=schema_prev_$(date +%Y%m%d_%H%M)     # copy to diff against; delete it when done
cp -r schema "$prev" && ./export_schema.sh && diff -r "$prev" schema
```
`export_schema.sh` exports `SEQUENCE SEQUENCE_VALUES TABLE TYPE` and writes `tables/table.sql`,
`INDEXES_table.sql`, `CONSTRAINTS_table.sql`, `FKEYS_table.sql`, `sequences/sequence.sql`,
`sequence_values/sequence_value.sql` and `types/type.sql`. Time: about 1m30s, and the diff takes seconds. Ignore the leftover files in
`schema/packages/`: `import_all.sh` never reads them. Do not rename any of them to `package.sql`.

### 9. ora2pg load: data, indexes, constraints, FKs
In the WSL tmux session:

```bash
cd ~/farmsprod/ora2pg/migprod
export PGPASSWORD=$(awk '/^PG_PWD/{print $2}' config/ora2pg.conf)
time ./import_all.sh -y -f -h localhost -U app_farms -d farmsprod -p 5436 -o app_farms -x -j 4 -P 4 2>&1 | tee import_$(date +%Y%m%d_%H%M).log
```
Flags: `-y` answers yes, `-f` skips the drop and create of the database, `-x` loads indexes and constraints
after the data, `-j 4 -P 4` for parallel jobs.

- With `DROP_FKEY 0` this should run through data, indexes, constraints and FKs in one go. The first dry run
  with `DROP_FKEY 1` aborted at the end of the data step, because ora2pg tried to add FKs before any primary
  keys existed.
- **If it aborts after the data,** run the three post-load files by hand, in this order, each one finishing
  before the next starts:
  ```bash
  time ora2pg -j 4 -c config/ora2pg.conf -t LOAD -i schema/tables/INDEXES_table.sql     2>&1 | tee idx.log
  time ora2pg -j 4 -c config/ora2pg.conf -t LOAD -i schema/tables/CONSTRAINTS_table.sql 2>&1 | tee con.log
  time ora2pg -j 4 -c config/ora2pg.conf -t LOAD -i schema/tables/FKEYS_table.sql       2>&1 | tee fk.log
  ```
- **Monitor** with `tail` on the log. The progress line shows the percentage of total rows. The estimate can run
  a few percent low, so 103% is normal.
- **Check for errors:** `grep -iE "error|fatal" import_*.log | grep -vi pwd`.

Dry run 1 times (with `-j 4`): schema create and data copy 71m (data copy 4,182 s), indexes 16m40s, constraints
9m44s, FKs 6m14s. Dry run 2 was the single-command run above, with `DROP_FKEY 0`. It ran straight through with
no errors and no manual steps: about 1h43m in total. Schema create about 2m, data copy 69m, indexes 18m28s,
constraints 9m09s, FKs 4m24s.

Run 3 (2026-11-25, 18:18 to 18:51 Vancouver) was the first after the prod CPU limits were raised (database
200m to 1000m), with `max_wal_size` at 1GB. Same command, no errors, **about 33m in total**: schema create about
1m, data copy 25m49s, indexes 2m20s, constraints 2m29s, FKs 56s. Use these for the real run.

### 10. Validate (before Liquibase)
In PowerShell, from any folder inside `<repo>`. The second line finds the ora2pg `schema` folder in WSL.

```powershell
cd (git rev-parse --show-toplevel)
$schema = wsl bash -c 'wslpath -w ~/farmsprod/ora2pg/migprod/schema'
python farms-liquibase\oracle-migration\validate\validate_structure.py 5436 farmsprod "$schema\tables\table.sql"
python farms-liquibase\oracle-migration\validate\validate_sequences.py 5436 farmsprod "$schema\sequences\sequence.sql"
```
and in WSL (about 1m10s; change `/mnt/c/source/git/nr-brmb-ast` if your clone is somewhere else):

```bash
bash /mnt/c/source/git/nr-brmb-ast/farms-liquibase/oracle-migration/validate/validate_counts.sh
```

- `validate_structure.py`: PASS (135 tables, 183 indexes, 527 constraints, all validated).
- `validate_sequences.py`: **`farm_sga_seq` is expected to FAIL here** (next value 1, max key about 2076). Liquibase fixes it.
- `validate_counts.sh`: every table must match Oracle exactly.

### 11. Liquibase
GitHub Actions > **Liquibase DDL application** > Run workflow, from `main`: `ENVIRONMENT_NAME=prod`,
`TAG=main` (or a tag you choose; it builds and pushes `farms-liquibase:<TAG>`). It builds the image, applies the
Job, and waits for it. Dry run 2: the whole workflow took 5m01s and the Job 3m35s; it succeeded first time with
134 changesets recorded. Run 3: workflow 4m01s, Job 2m33s, also 134 changesets, and all validators passed.
(The 80 s Job in run 1 only applied the changesets left over from earlier failed runs.)

If it fails:
- **Job pod OOMKilled:** memory is now 512Mi request, 1Gi limit in `openshift/farms-liquibase-job.yaml`. If it
  still dies, raise it.
- **Lock left behind** (`databasechangeloglock.locked = true`, held by a dead pod): delete the Job, then in
  farmsprod run `UPDATE databasechangeloglock SET locked=false, lockgranted=NULL, lockedby=NULL WHERE id=1;`
  and rerun the workflow. It resumes after the changesets already recorded.
- **Retries:** the Job has `backoffLimit 6`. Delete a failed Job with `oc delete job <name> -n e980f4-prod`.
- **Do not** run this changelog on dev, test or dlvr yet. Their history is under the old `scripts/` paths.

### 12. Validate again
Same commands as step 10. This time everything must pass, including `farm_sga_seq`. Extras from Liquibase
(`farm_urls`, `url_id`, `file_location`, 3 `farm_url` FKs, `databasechangelog*`) are INFO lines. Row counts must
still match (the 02_dml scripts change nothing on PROD data).

### 13. Go live
1. **Run manual full backups, repo1 and then repo2** (required). Start repo1, carry on with the next steps,
   and start repo2 once repo1 has finished. Check later that both completed and that repo1 usage dropped. The
   commands and the reasons are in [Backups and WAL after a load](#backups-and-wal-after-a-load).
2. Scale the apps back up. In WSL, logged in with `oc`:
   ```bash
   oc scale deployment farms-api-deployment-prod farms-legacy-deployment-prod --replicas=1 -n e980f4-prod
   ```
3. Set the `pgpass.conf` password for `localhost:5436` back to `not_set`.
4. Smoke test both apps.
5. **After the final load, set `max_wal_size` back to 64MB.** It was set to 1GB directly on the cluster for
   the loads (see [Preparation](#preparation)). A DB deploy does **not** put it back, because the chart's value
   hasn't changed. In WSL, logged in with `oc`:
   ```bash
   oc patch postgrescluster crunchy-postgres-prod -n e980f4-prod --type=merge \
     -p '{"spec":{"patroni":{"dynamicConfiguration":{"postgresql":{"parameters":{"max_wal_size":"64MB"}}}}}}'
   ```
   No restart is needed. It can take a few minutes to apply (about 4 in test). Confirm that this shows `64MB`:
   `oc exec -n e980f4-prod <primary pod> -c database -- psql -tAc "show max_wal_size"`

### 14. Secure and remove the PROD data
The temporary Oracle server now holds PROD data. Do this once farmsprod is validated and live, and no reload from
Oracle will be needed. In this order:

1. **Store the PROD dump file in a secure location.** Check that the stored copy is complete (size, or SHA256
   hash) before deleting anything. Don't record the location in this repository.
2. **Wipe the `FARM` schema on `<temporary-oracle-server>`**, dropping the objects as in step 1.
   Then run `PURGE RECYCLEBIN;` connected as `FARM`, otherwise dropped tables and their data stay in the database.
3. **Delete the PROD dump file from `<dump-directory>`** on `<temporary-oracle-server>`, including any zipped
   copy.
4. **Revert the passwords of `FARM` and `PROXY_FARM_ADMIN`**, or remove the accounts if they are no longer
   needed.

## Backups and WAL after a load
Applies to every load into farmsprod, dry run or real. Figures are as of 2026-11-25 for the June dump
(21 GB database).

- A load puts about 20 GB of WAL into repo1 (the 80Gi backup volume); the WAL archive was 22 GB on 2026-11-26,
  the day after a load. Each full or differential backup taken
  after it is about 10 GB, because the load rewrites the data files.
- That WAL stays until a full that **started after the load** completes. That backup's own expiry then removes
  it, so no expiry needs to be run by hand (WAL retention is `full` / 1, deployed to prod 2026-11-25).
- Let a full complete between two loads. Two loads' WAL plus their backups could fill repo1, which stops
  WAL archiving.
- **After every load, dry run or real, run manual fulls, repo1 first and then repo2,** instead of waiting for
  Saturday. For repo1, each differential taken before the next full is another copy of about 10 GB. For repo2
  (object storage), the full gives a second full copy of the loaded data. In WSL, logged in with `oc`:

  1. repo1:
  ```bash
  oc exec -n e980f4-prod -c pgbackrest crunchy-postgres-prod-repo-host-0 -- sh -c \
    'setsid nohup pgbackrest backup --stanza=db --repo=1 --type=full --log-level-console=info > /tmp/full_repo1.out 2>&1 < /dev/null & sleep 10; tail -4 /tmp/full_repo1.out | cut -c1-200'
  ```

  2. repo2, **after repo1 has finished**:
  ```bash
  oc exec -n e980f4-prod -c pgbackrest crunchy-postgres-prod-repo-host-0 -- sh -c \
    'setsid nohup pgbackrest backup --stanza=db --repo=2 --type=full --log-level-console=info > /tmp/full_repo2.out 2>&1 < /dev/null & sleep 10; tail -4 /tmp/full_repo2.out | cut -c1-200'
  ```
  Each runs in the background on the repo host, so closing the terminal doesn't stop it. A restart of that pod
  would. First check that no other backup is running. Scheduled backups run in their own job pods, so check for
  those too (no output means none is running):
  ```bash
  oc get job -n e980f4-prod | grep -E 'repo[12]-.*Running'
  ```
  Both commands were run on 2026-11-26. Start early enough for repo1 to finish before the 17:00 Vancouver
  differential, and repo2 before its 19:00 differential. The first scheduled differential after a load takes
  about as long as a full (39 minutes for repo2 on 2026-11-27), so after 17:00 (repo1) or 19:00 (repo2), check
  that it has finished before starting a manual full.
- **Other scheduled backups** (Vancouver time): repo1 incrementals run at 01:00, 05:00, 09:00 and 13:00, and
  repo2 incrementals at 03:00, 07:00, 11:00 and 15:00. The 11:00 repo2 incremental succeeded on 2026-11-26
  while a manual repo1 full was running.
- **Check whether they're done:**
  ```bash
  oc exec -n e980f4-prod -c pgbackrest crunchy-postgres-prod-repo-host-0 -- sh -c \
    'grep -E "ERROR|backup command end|expire command end" /tmp/full_repo[12].out; pgrep -f "[p]gbackrest backup" >/dev/null && echo STILL RUNNING || echo NOT RUNNING'
  ```
  To check repo2 only:
  ```bash
  oc exec -n e980f4-prod -c pgbackrest crunchy-postgres-prod-repo-host-0 -- sh -c \
    'grep -E "ERROR|backup command end|expire command end" /tmp/full_repo2.out | cut -c1-200; pgrep -f "[p]gbackrest backup .*--repo=2" >/dev/null && echo STILL RUNNING || echo NOT RUNNING'
  ```
  Success, for each repo's log, is both `backup command end: completed successfully` and `expire command end:
  completed successfully`. An `ERROR` line, or `NOT RUNNING` without both lines, means it failed. Log times are
  UTC (subtract 7 hours). The logs are lost if the repo host restarts; `pgbackrest info` still shows what
  completed: `oc exec -n e980f4-prod crunchy-postgres-prod-repo-host-0 -c pgbackrest -- pgbackrest info --stanza=db`
- The scheduled repo1 full runs Saturdays at 17:00 Vancouver time. A manual repo1 full took 39 minutes on 2026-11-26
  (4h19m on 2026-11-25, before the backup CPU limits were raised). It left repo1 at 11G of 80G.
- The scheduled repo2 full runs Saturdays at 19:00 Vancouver time. A manual repo2 full took 41 minutes on 2026-11-26.
- Check free space (WSL, logged in with `oc`):
  `oc exec -n e980f4-prod crunchy-postgres-prod-repo-host-0 -c pgbackrest -- df -h /pgbackrest/repo1`

## Gotchas learned in the dry runs
- Roles are cluster-wide. A wipe doesn't remove them, so any changeset that creates a role must be rerunnable.
- Never print `ora2pg.conf` whole or paste passwords into chat.
- The `proxy_farms_rest` role is what farms-api and farms-legacy connect as. `app_farms` is for migration
  tooling and has a different password in each environment.
- Dev, test and dlvr cannot take this changelog with `liquibase update` until their history is reconciled.
- Keep `DROP_FKEY 0` in `ora2pg.conf`. With `1`, the first load aborted after the data step.
- A Liquibase pod that dies can leave the lock held. Release it before rerunning (step 11 has the SQL).
- Don't deploy the database or restart the repo host while a manual backup is running.
- `farm_sga_seq` failing in the validation before Liquibase is expected (step 10).

## Reusing this for another project

The order of the steps and the gotchas should carry over. The scripts have FARMS names built into them, so change:
- **Schemas** `farms` and `farms_*_pkg`: `gen_drop_script.py`, `check_empty.py`, `validate_structure.py`,
  `validate_sequences.py`, `_db.py` and `schema_settings_check.py`.
- **Migration role** `app_farms`: `_db.py`, `gen_drop_script.py`, `check_empty.py` and `schema_settings_check.py`.
- **Sequence naming** (`farm_<x>_seq` goes with primary key `farm_<x>_pk`) and the exceptions list in
  `validate_sequences.py`.
- **Database, schema and server names** in `ora2pg.conf.example` and in the commands above.
- **The expected counts and timings** here are from FARMS data (21 GB loaded database).
