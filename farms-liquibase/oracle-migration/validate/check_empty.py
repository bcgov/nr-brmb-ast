"""Post-wipe check: farmsprod must hold nothing but the bootstrap state.

Usage: python check_empty.py <port> <dbname>
Run right after drop_farmsprod.sql (and before ora2pg). Read-only.
"""
import sys
from _db import connect, Report, PKG_LIKE

port, dbname = sys.argv[1], sys.argv[2]
cur = connect(port, dbname).cursor()
r = Report(f"check_empty: {dbname}")

NOT_EXT = ("not exists (select 1 from pg_depend d where d.classid = '{cls}'::regclass "
           "and d.objid = {oid} and d.deptype = 'e')")

cur.execute("select current_database(), current_user")
db, user = cur.fetchone()
r.check(db == dbname and user == "app_farms", "connected as app_farms to the expected database", f"{user}@{db}")

cur.execute("select pg_get_userbyid(nspowner) from pg_namespace where nspname = 'farms'")
row = cur.fetchone()
r.check(row is not None and row[0] == "app_farms", "schema farms exists and is owned by app_farms", str(row))

cur.execute(f"""select c.relkind::text, count(*) from pg_class c join pg_namespace n on n.oid = c.relnamespace
  where n.nspname = 'farms' and c.relkind in ('r','p','v','m','f','S','c')
    and {NOT_EXT.format(cls='pg_class', oid='c.oid')} and {NOT_EXT.format(cls='pg_type', oid='c.reltype')}
  group by 1""")
left = cur.fetchall()
r.check(not left, "no tables/views/sequences/composite types left in farms", str(left) if left else "")

cur.execute(f"""select count(*) from pg_proc p join pg_namespace n on n.oid = p.pronamespace
  where n.nspname = 'farms' and {NOT_EXT.format(cls='pg_proc', oid='p.oid')}""")
n = cur.fetchone()[0]
r.check(n == 0, "no non-extension routines left in farms", f"{n} found")

cur.execute("select nspname from pg_namespace where nspname like %s order by 1", (PKG_LIKE,))
pkgs = [x[0] for x in cur.fetchall()]
r.check(not pkgs, "no farms_*_pkg schemas left", f"{len(pkgs)} found" if pkgs else "")

cur.execute("select extname from pg_extension order by 1")
exts = [x[0] for x in cur.fetchall()]
r.check(exts == ["pgaudit", "plpgsql", "tablefunc"], "extensions are exactly pgaudit, plpgsql, tablefunc", str(exts))

cur.execute("select count(*) from pg_proc p join pg_depend d on d.objid = p.oid and d.deptype = 'e' "
            "where p.pronamespace = 'farms'::regnamespace")
r.info("tablefunc routines kept in farms", str(cur.fetchone()[0]))
r.finish()
