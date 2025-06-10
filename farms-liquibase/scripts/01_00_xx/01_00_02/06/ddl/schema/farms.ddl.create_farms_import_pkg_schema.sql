-- SCHEMA: farms_import_pkg

-- DROP SCHEMA "farms_import_pkg" ;

CREATE SCHEMA "farms_import_pkg"
    AUTHORIZATION "app_farms";

GRANT ALL ON SCHEMA "farms_import_pkg" TO "app_farms";

GRANT ALL ON SCHEMA "farms_import_pkg" TO postgres;

ALTER SCHEMA "farms_import_pkg" OWNER TO "app_farms";