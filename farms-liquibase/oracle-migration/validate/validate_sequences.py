"""Sequence check: every sequence must hand out values above the data it feeds.

Usage: python validate_sequences.py <port> <dbname> [path to sequence.sql from ora2pg]
For each sequence farm_<x>_seq the table is found by its primary key constraint farm_<x>_pk
(single-column keys). For each pair: FAIL if the next value the sequence would return is
<= MAX(key column), i.e. the next insert would collide.
If sequence.sql is given, also FAIL when a sequence's START differs from the Oracle value
ora2pg exported. Read-only.
"""
import re
import sys
from decimal import Decimal
import psycopg2.sql as sql
from _db import connect, Report

port, dbname = sys.argv[1], sys.argv[2]
seq_sql = sys.argv[3] if len(sys.argv) > 3 else None

# sequences that do not follow the farm_<x>_seq / farm_<x>_pk convention: seq -> (table, column) or None (unused)
OVERRIDES = {
    "farm_as_cfn_seq": ("farm_agristability_scenarios", "combined_farm_number"),  # farms_calculator_pkg.combined_farm_add
    "farm_pyc_seq": None,  # not referenced by any code
}

conn = connect(port, dbname)
cur = conn.cursor()
r = Report(f"validate_sequences: {dbname}")

cur.execute("""select sequencename, start_value, increment_by, last_value from pg_sequences
               where schemaname = 'farms' order by 1""")
seqs = cur.fetchall()
cur.execute("""select con.conname, c.relname, array_agg(a.attname) from pg_constraint con
               join pg_class c on c.oid = con.conrelid join pg_attribute a on a.attrelid = c.oid and a.attnum = any(con.conkey)
               where con.connamespace = 'farms'::regnamespace and con.contype = 'p' group by 1, 2""")
pks = {name: (tbl, cols) for name, tbl, cols in cur.fetchall()}

if seq_sql:
    starts = {m[0].lower(): int(m[1]) for m in re.findall(r"CREATE SEQUENCE (?:IF NOT EXISTS )?(\w+)[^;]*?START (\d+)",
                                                         open(seq_sql, encoding="utf-8").read())}
    bad = [(s, st, starts[s]) for s, st, _, _ in seqs if s in starts and st != starts[s]]
    r.check(not bad, f"sequence START values match ora2pg's {len(starts)} exported values", str(bad[:10]) if bad else "")
    r.info("sequences in database but not in sequence.sql (e.g. farm_url_seq)",
           str(sorted({s for s, *_ in seqs} - set(starts))))

checked = 0
fixes = []
for seq, start, inc, last in seqs:
    if seq in OVERRIDES:
        target = OVERRIDES[seq]
    else:
        pk = pks.get(re.sub(r"_seq$", "_pk", seq))
        target = (pk[0], pk[1][0]) if pk and len(pk[1]) == 1 else "unmapped"
    if target is None:
        r.info(f"{seq}: unused, skipped")
        continue
    if target == "unmapped":
        r.check(False, f"{seq}: could not map to a table key", "add it to OVERRIDES")
        continue
    table, col = target
    nxt = start if last is None else last + inc
    cur.execute(sql.SQL("select max({c}) from {t}").format(c=sql.Identifier(col), t=sql.Identifier("farms", table)))
    mx = cur.fetchone()[0]
    if mx is not None and not isinstance(mx, (int, Decimal)):
        r.info(f"{seq} -> {table}.{col}: key is not numeric, skipped", repr(mx))
        continue
    checked += 1
    ok = mx is None or nxt > mx
    if not ok:
        r.check(False, f"{seq} -> {table}.{col}", f"next value {nxt} <= max {mx}")
        fixes.append(f"SELECT setval('farms.{seq}', {int(mx)}, true);  -- {table}.{col}")
r.check(True, f"{checked} sequences checked: next value is above MAX(key) for all except any failures listed above") \
    if r.failures == 0 else None
if fixes:
    print("\nSuggested fixes (NOT executed; review, then run as app_farms):")
    for f in fixes:
        print("  " + f)
r.finish()
