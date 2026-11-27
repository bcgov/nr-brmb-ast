import sys
import psycopg2

port, dbname = sys.argv[1], sys.argv[2]

QUERIES = [
    ("extensions (name, schema, owner)",
     "select extname, extnamespace::regnamespace::text, pg_get_userbyid(extowner) from pg_extension order by 1"),
    ("schemas (owner, ACL, comment)",
     "select n.nspname, pg_get_userbyid(n.nspowner), n.nspacl::text, obj_description(n.oid,'pg_namespace') "
     "from pg_namespace n where n.nspname not like 'pg\\_%' and n.nspname <> 'information_schema' order by 1"),
    ("default ACLs",
     "select pg_get_userbyid(defaclrole), defaclnamespace::regnamespace::text, defaclobjtype::text, defaclacl::text "
     "from pg_default_acl order by 1,2,3"),
    ("relations not owned by app_farms",
     "select n.nspname, c.relkind::text, pg_get_userbyid(c.relowner), count(*) "
     "from pg_class c join pg_namespace n on n.oid=c.relnamespace "
     "where n.nspname like 'farms%' and pg_get_userbyid(c.relowner) <> 'app_farms' group by 1,2,3 order by 1,2,3"),
    ("routines not owned by app_farms",
     "select n.nspname, pg_get_userbyid(p.proowner), count(*) "
     "from pg_proc p join pg_namespace n on n.oid=p.pronamespace "
     "where n.nspname like 'farms%' and pg_get_userbyid(p.proowner) <> 'app_farms' group by 1,2 order by 1,2"),
    ("enum/domain types not owned by app_farms",
     "select n.nspname, t.typname, pg_get_userbyid(t.typowner) "
     "from pg_type t join pg_namespace n on n.oid=t.typnamespace "
     "where n.nspname like 'farms%' and t.typtype in ('e','d') and pg_get_userbyid(t.typowner) <> 'app_farms' order by 1,2"),
    ("database/role settings",
     "select coalesce(d.datname,'-'), coalesce(r.rolname,'-'), s.setconfig::text "
     "from pg_db_role_setting s left join pg_database d on d.oid=s.setdatabase "
     "left join pg_roles r on r.oid=s.setrole order by 1,2"),
    ("database ACL",
     "select pg_get_userbyid(datdba), datacl::text from pg_database where datname = current_database()"),
]

conn = psycopg2.connect(host="localhost", port=port, dbname=dbname, user="app_farms", sslmode="prefer")
conn.set_session(readonly=True, autocommit=True)
cur = conn.cursor()
for label, sql in QUERIES:
    print("== " + label)
    cur.execute(sql)
    for row in cur.fetchall():
        print("  " + " | ".join(str(c) for c in row))
conn.close()
