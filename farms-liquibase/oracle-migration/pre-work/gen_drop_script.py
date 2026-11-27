"""Generate a reviewed DROP script for the farms and farms_*_pkg schemas.

Read-only: it only queries the catalog. It never executes any DROP.
Extension member objects (tablefunc, pgcrypto, ...) are excluded via pg_depend.
Usage: python gen_drop_script.py <port> <dbname> <output.sql>
"""
import sys
import psycopg2

port, dbname, outfile = sys.argv[1], sys.argv[2], sys.argv[3]

SCHEMA_FILTER = "(n.nspname = 'farms' or n.nspname like 'farms\\_%\\_pkg')"

# Not an extension member: no pg_depend row with deptype 'e' for this object.
def not_ext(classname, oid_expr):
    return (f"not exists (select 1 from pg_depend d where d.classid = '{classname}'::regclass "
            f"and d.objid = {oid_expr} and d.deptype = 'e')")

Q_RELS = f"""
select c.relkind::text, format('%I.%I', n.nspname, c.relname)
from pg_class c join pg_namespace n on n.oid = c.relnamespace
where {SCHEMA_FILTER}
  and c.relkind in ('v','m','r','p','f','c')
  and not c.relispartition
  and {not_ext('pg_class', 'c.oid')}
  and {not_ext('pg_type', 'c.reltype')}
order by n.nspname, c.relname
"""

Q_SEQS = f"""
select format('%I.%I', n.nspname, c.relname)
from pg_class c join pg_namespace n on n.oid = c.relnamespace
where {SCHEMA_FILTER}
  and c.relkind = 'S'
  and not exists (select 1 from pg_depend d where d.classid = 'pg_class'::regclass
                  and d.objid = c.oid and d.deptype in ('a','i'))
  and {not_ext('pg_class', 'c.oid')}
order by n.nspname, c.relname
"""

Q_PROCS = f"""
select p.prokind::text, format('%I.%I(%s)', n.nspname, p.proname, pg_get_function_identity_arguments(p.oid))
from pg_proc p join pg_namespace n on n.oid = p.pronamespace
where {SCHEMA_FILTER}
  and {not_ext('pg_proc', 'p.oid')}
order by n.nspname, p.proname, p.oid
"""

Q_TYPES = f"""
select t.typtype::text, format('%I.%I', n.nspname, t.typname)
from pg_type t join pg_namespace n on n.oid = t.typnamespace
where {SCHEMA_FILTER}
  and t.typtype in ('e','d')
  and {not_ext('pg_type', 't.oid')}
order by n.nspname, t.typname
"""

# Visible-but-excluded objects, so the reviewer sees what is being kept.
Q_KEPT = f"""
select 'relation' as kind, n.nspname, c.relkind::text, count(*)
from pg_class c join pg_namespace n on n.oid = c.relnamespace
where {SCHEMA_FILTER}
  and (not ({not_ext('pg_class', 'c.oid')}) or not ({not_ext('pg_type', 'c.reltype')}))
group by 2,3
union all
select 'routine', n.nspname, p.prokind::text, count(*)
from pg_proc p join pg_namespace n on n.oid = p.pronamespace
where {SCHEMA_FILTER} and not ({not_ext('pg_proc', 'p.oid')})
group by 2,3
order by 1,2,3
"""

# Anything left over that the generator has no DROP rule for.
Q_UNHANDLED = f"""
select n.nspname, c.relkind::text, count(*)
from pg_class c join pg_namespace n on n.oid = c.relnamespace
where {SCHEMA_FILTER}
  and c.relkind not in ('v','m','r','p','f','c','S','i','I','t')
group by 1,2
"""

# The 'farms_*_pkg' schemas are dropped whole (their routines go with them); so is any extension living in one.
Q_PKG_SCHEMAS = r"""
select n.nspname from pg_namespace n
where n.nspname like 'farms\_%\_pkg'
order by 1
"""

Q_PKG_EXTENSIONS = r"""
select e.extname, n.nspname from pg_extension e join pg_namespace n on n.oid = e.extnamespace
where n.nspname like 'farms\_%\_pkg'
order by 1
"""

conn = psycopg2.connect(host="localhost", port=port, dbname=dbname, user="app_farms", sslmode="prefer")
conn.set_session(readonly=True, autocommit=True)
cur = conn.cursor()

cur.execute(Q_RELS); rels = cur.fetchall()
cur.execute(Q_SEQS); seqs = [r[0] for r in cur.fetchall()]
cur.execute(Q_PROCS); procs = cur.fetchall()
cur.execute(Q_TYPES); types = cur.fetchall()
cur.execute(Q_KEPT); kept = cur.fetchall()
cur.execute(Q_UNHANDLED); unhandled = cur.fetchall()
cur.execute(Q_PKG_SCHEMAS); pkg_schemas = [r[0] for r in cur.fetchall()]
cur.execute(Q_PKG_EXTENSIONS); pkg_exts = cur.fetchall()
conn.close()

views = [n for k, n in rels if k == 'v']
matviews = [n for k, n in rels if k == 'm']
tables = [n for k, n in rels if k in ('r', 'p')]
foreign = [n for k, n in rels if k == 'f']
comp_types = [n for k, n in rels if k == 'c']

lines = []
w = lines.append
w(f"-- Generated DROP script for database '{dbname}'. REVIEW BEFORE RUNNING; nothing has been executed.")
w("-- Scope: schema 'farms' (emptied; the schema itself is kept) and every schema 'farms_*_pkg' (dropped whole, with its routines).")
w("-- Extensions living in a pkg schema are dropped first (pgcrypto in farms_webapp_pkg): recreate them after Liquibase has recreated the schema.")
w("-- Kept: extension members in 'farms' (tablefunc), the pgbouncer and public schemas.")
w("-- Run as app_farms. Regenerate it immediately before each use, since it reflects the catalog at generation time.")
w("-- Run with psql (it uses a psql meta-command), e.g.: psql -X \"host=... dbname=" + dbname + " user=app_farms\" -f <this file>")
w("-- It commits only if every statement succeeds AND nothing but extension members remains in scope.")
w("-- Any error stops the script and the transaction is rolled back.")
w("")
w("\\set ON_ERROR_STOP on")
w("")
w("BEGIN;")
w("")
w("DO $guard$ BEGIN")
w(f"    IF current_database() <> '{dbname}' THEN")
w(f"        RAISE EXCEPTION 'Refusing to run: connected to %, expected {dbname}', current_database();")
w("    END IF;")
w("END $guard$;")
w("")

def section(title, stmts):
    w(f"-- {title} ({len(stmts)})")
    for s in stmts:
        w(s)
    w("")

section("views", [f"DROP VIEW IF EXISTS {n};" for n in views])
section("materialized views", [f"DROP MATERIALIZED VIEW IF EXISTS {n};" for n in matviews])
section("foreign tables", [f"DROP FOREIGN TABLE IF EXISTS {n};" for n in foreign])
section("tables (CASCADE removes their FKs, triggers, indexes, owned sequences; includes databasechangelog*)",
        [f"DROP TABLE IF EXISTS {n} CASCADE;" for n in tables])

proc_stmts = []
for kind, sig in procs:
    if not sig.startswith('farms.'):
        continue  # pkg-schema routines are removed by DROP SCHEMA ... CASCADE below
    if kind == 'p':
        proc_stmts.append(f"DROP PROCEDURE IF EXISTS {sig};")
    elif kind == 'a':
        proc_stmts.append(f"DROP AGGREGATE IF EXISTS {sig};")
    else:
        proc_stmts.append(f"DROP FUNCTION IF EXISTS {sig};")
section("functions, procedures, aggregates in schema farms (pkg-schema routines go with their schemas)", proc_stmts)

section("standalone composite types", [f"DROP TYPE IF EXISTS {n};" for n in comp_types])
section("enum types", [f"DROP TYPE IF EXISTS {n};" for k, n in types if k == 'e'])
section("domains", [f"DROP DOMAIN IF EXISTS {n};" for k, n in types if k == 'd'])
section("standalone sequences", [f"DROP SEQUENCE IF EXISTS {n};" for n in seqs])

section("extensions living in a pkg schema (recreate after Liquibase)", [f"DROP EXTENSION IF EXISTS {e};" for e, _ in pkg_exts])
section("pkg schemas (CASCADE removes their routines; Liquibase recreates them)",
        [f'DROP SCHEMA IF EXISTS "{n}" CASCADE;' for n in pkg_schemas])

w("-- Verify nothing but extension members remains in scope; raising here rolls the whole transaction back.")
w(r"""DO $verify$
DECLARE
    leftover_rels int;
    leftover_routines int;
    leftover_schemas int;
BEGIN
    SELECT count(*) INTO leftover_rels
    FROM pg_class c JOIN pg_namespace n ON n.oid = c.relnamespace
    WHERE (n.nspname = 'farms' OR n.nspname LIKE 'farms\_%\_pkg')
      AND c.relkind IN ('r','p','v','m','f','S','c')
      AND NOT EXISTS (SELECT 1 FROM pg_depend d WHERE d.classid = 'pg_class'::regclass AND d.objid = c.oid AND d.deptype = 'e')
      AND NOT EXISTS (SELECT 1 FROM pg_depend d WHERE d.classid = 'pg_type'::regclass AND d.objid = c.reltype AND d.deptype = 'e');
    SELECT count(*) INTO leftover_routines
    FROM pg_proc p JOIN pg_namespace n ON n.oid = p.pronamespace
    WHERE n.nspname = 'farms'
      AND NOT EXISTS (SELECT 1 FROM pg_depend d WHERE d.classid = 'pg_proc'::regclass AND d.objid = p.oid AND d.deptype = 'e');
    SELECT count(*) INTO leftover_schemas FROM pg_namespace WHERE nspname LIKE 'farms\_%\_pkg';
    IF leftover_rels <> 0 OR leftover_routines <> 0 OR leftover_schemas <> 0 THEN
        RAISE EXCEPTION 'Not clean: % relations, % routines and % pkg schemas remain; rolling back', leftover_rels, leftover_routines, leftover_schemas;
    END IF;
END $verify$;""")
w("")
w("COMMIT;")

with open(outfile, "w", encoding="utf-8", newline="\n") as f:
    f.write("\n".join(lines) + "\n")

print(f"views={len(views)} matviews={len(matviews)} foreign={len(foreign)} tables={len(tables)} "
      f"routines_in_farms={len(proc_stmts)} pkg_schemas={len(pkg_schemas)} pkg_extensions={[e for e,_ in pkg_exts]} composite_types={len(comp_types)} enums={sum(1 for k,_ in types if k=='e')} "
      f"domains={sum(1 for k,_ in types if k=='d')} sequences={len(seqs)}")
print("KEPT (extension members, not dropped):")
for row in kept:
    print("  ", row)
print("UNHANDLED relation kinds (no DROP rule):", unhandled or "none")
