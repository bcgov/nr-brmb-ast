"""Structure check: does Postgres contain everything ora2pg's generated table.sql defines?

Usage: python validate_structure.py <port> <dbname> <path to table.sql>
Compares table names, columns, index names and constraint names (PK/UK/FK/CHECK) from table.sql
and, when ora2pg was run with FILE_PER_INDEX/CONSTRAINT/FKEYS, the INDEXES_table.sql,
CONSTRAINTS_table.sql and FKEYS_table.sql files next to it, with the catalog, and requires every constraint to be validated.
Run after import_all.sh (before Liquibase) and again after Liquibase.
Anything MISSING is a failure. Extra objects are reported as INFO (expected after Liquibase:
farm_urls, url_id columns, farm_bpu_farm_url_fk etc.). Read-only.
"""
import os
import re
import sys
from _db import connect, Report

port, dbname, table_sql = sys.argv[1], sys.argv[2], sys.argv[3]
text = open(table_sql, encoding="utf-8").read()
# indexes/constraints/FKs live in sibling files when ora2pg uses FILE_PER_*; read them too if present
extra_files = [f for f in ("INDEXES_table.sql", "CONSTRAINTS_table.sql", "FKEYS_table.sql")
               if os.path.exists(os.path.join(os.path.dirname(table_sql), f))]
all_sql = text + "".join(open(os.path.join(os.path.dirname(table_sql), f), encoding="utf-8").read()
                         for f in extra_files)

# --- expected objects, from the ora2pg output
exp_tables, exp_cols = [], {}
for m in re.finditer(r"^CREATE TABLE (\w+) \(\n(.*?)\n\) ?;", text, re.S | re.M):
    t = m.group(1).lower()
    exp_tables.append(t)
    cols = set()
    for line in m.group(2).split("\n"):
        tok = line.strip().split(" ")[0].lower()
        if tok and tok not in ("constraint", "primary", "unique", "check", "foreign"):
            cols.add(tok)
    exp_cols[t] = cols
exp_idx = {m.lower() for m in re.findall(r"^CREATE (?:UNIQUE )?INDEX (\w+)", all_sql, re.M)}
exp_con = {m.lower() for m in re.findall(r"ADD CONSTRAINT (\w+)", all_sql)}

# --- actual objects
cur = connect(port, dbname).cursor()
r = Report(f"validate_structure: {dbname} vs {table_sql}")
r.info("ora2pg files read", ", ".join(["table.sql"] + extra_files))
cur.execute("select c.relname from pg_class c where c.relnamespace = 'farms'::regnamespace and c.relkind in ('r','p')")
act_tables = {x[0] for x in cur.fetchall()}
cur.execute("""select c.relname, a.attname from pg_attribute a join pg_class c on c.oid = a.attrelid
               where c.relnamespace = 'farms'::regnamespace and c.relkind in ('r','p') and a.attnum > 0 and not a.attisdropped""")
act_cols = {}
for t, col in cur.fetchall():
    act_cols.setdefault(t, set()).add(col)
cur.execute("select c.relname from pg_class c where c.relnamespace = 'farms'::regnamespace and c.relkind = 'i'")
act_idx = {x[0] for x in cur.fetchall()}
cur.execute("""select con.conname, con.convalidated, con.contype::text from pg_constraint con
               where con.connamespace = 'farms'::regnamespace and con.contype in ('p','u','f','c')""")
cons = cur.fetchall()
act_con = {c[0] for c in cons}


def diff(label, expected, actual):
    missing, extra = sorted(set(expected) - set(actual)), sorted(set(actual) - set(expected))
    r.check(not missing, f"{label}: all {len(set(expected))} expected objects present",
            f"missing {len(missing)}: {missing[:15]}" if missing else "")
    if extra:
        r.info(f"{label}: {len(extra)} extra beyond table.sql", str(extra[:15]))


diff("tables", exp_tables, act_tables)
# a table with no expected entry is excluded from the column check
miss_cols = {t: sorted(exp_cols[t] - act_cols.get(t, set())) for t in exp_cols if t in act_tables}
miss_cols = {t: c for t, c in miss_cols.items() if c}
r.check(not miss_cols, "columns: every expected column present", str(dict(list(miss_cols.items())[:8])) if miss_cols else "")
extra_cols = {t: sorted(act_cols[t] - exp_cols[t]) for t in exp_cols if t in act_cols and act_cols[t] - exp_cols[t]}
if extra_cols:
    r.info(f"columns: {len(extra_cols)} tables have extra columns (url_id/file_location expected after Liquibase)",
           str(extra_cols))
diff("indexes (named CREATE INDEX)", exp_idx, act_idx)
diff("constraints", exp_con, act_con)
bad = sorted(c[0] for c in cons if not c[1])
r.check(not bad, "all constraints validated", str(bad[:15]) if bad else "")
kinds = {}
for _, _, k in cons:
    kinds[k] = kinds.get(k, 0) + 1
r.info("constraint counts by type (p=pk u=unique f=fk c=check)", str(kinds))
r.finish()
