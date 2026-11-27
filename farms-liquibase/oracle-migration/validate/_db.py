"""Shared helpers for the migration validation scripts (read-only).

Connects as app_farms via localhost (the pgbouncer port-forward); the password comes from pgpass.conf.
"""
import sys
import psycopg2

PKG_LIKE = r"farms\_%\_pkg"


def connect(port, dbname):
    conn = psycopg2.connect(host="localhost", port=port, dbname=dbname, user="app_farms",
                            sslmode="prefer", connect_timeout=10)
    conn.set_session(readonly=True, autocommit=True)
    return conn


class Report:
    """Collects PASS/FAIL/INFO lines and turns them into an exit code."""

    def __init__(self, title):
        self.failures = 0
        print(f"=== {title}")

    def check(self, ok, label, detail=""):
        if not ok:
            self.failures += 1
        print(f"  [{'PASS' if ok else 'FAIL'}] {label}" + (f" - {detail}" if detail else ""))

    def info(self, label, detail=""):
        print(f"  [INFO] {label}" + (f" - {detail}" if detail else ""))

    def finish(self):
        print(("RESULT: PASS" if self.failures == 0 else f"RESULT: FAIL ({self.failures} failed check(s))"))
        sys.exit(1 if self.failures else 0)
