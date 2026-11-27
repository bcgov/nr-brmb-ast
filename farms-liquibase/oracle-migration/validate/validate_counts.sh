#!/bin/bash
# Row-count comparison, Oracle (the FARM schema loaded by impdp) vs Postgres, per table.
# Run in WSL, with the pgbouncer port-forward up (the PG_DSN in ora2pg.conf points at it).
# Usage: ./validate_counts.sh [ora2pg project dir]     (default ~/farmsprod/ora2pg/migprod)
# Exit status 0 only if every Oracle table has exactly the same count in Postgres.
# Takes a couple of minutes: it runs a real SELECT COUNT(*) on both sides.
PROJ="${1:-$HOME/farmsprod/ora2pg/migprod}"
LOG="$PROJ/test_count_$(date +%Y%m%d_%H%M%S).log"
cd "$PROJ" || exit 2
ora2pg -c config/ora2pg.conf -t TEST_COUNT --count_rows --no_header > "$LOG" 2>&1
python3 - "$LOG" <<'PY'
import sys
ora, pg, order = {}, {}, []
for line in open(sys.argv[1]):
    line = line.strip()
    if line.startswith("ORACLEDB:"):
        _, t, n = line.split(":"); ora[t.lower()] = int(n); order.append(t.lower())
    elif line.startswith("POSTGRES:"):
        _, t, n = line.split(":"); pg[t.lower()] = int(n)
if not ora:
    print("No counts found in", sys.argv[1], "- ora2pg failed?"); sys.exit(2)
bad = [(t, ora[t], pg.get(t)) for t in order if pg.get(t) != ora[t]]
print(f"=== validate_counts: {len(ora)} Oracle tables, {sum(ora.values()):,} rows")
for t, o, p in bad:
    print(f"  [FAIL] {t}: oracle={o:,} postgres={'MISSING' if p is None else f'{p:,}'}")
print(f"  [{'PASS' if not bad else 'FAIL'}] {len(ora)-len(bad)} of {len(ora)} tables match exactly")
extra = sorted(set(pg) - set(ora))
if extra: print("  [INFO] Postgres-only tables:", extra)
print("RESULT:", "PASS" if not bad else f"FAIL ({len(bad)} table(s) differ)")
sys.exit(1 if bad else 0)
PY
