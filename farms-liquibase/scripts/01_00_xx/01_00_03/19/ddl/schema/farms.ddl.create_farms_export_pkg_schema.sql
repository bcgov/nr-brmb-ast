-- SCHEMA: farms_export_pkg

-- DROP SCHEMA "farms_export_pkg" ;

CREATE SCHEMA "farms_export_pkg"
    AUTHORIZATION "app_farms";

GRANT ALL ON SCHEMA "farms_export_pkg" TO "app_farms";

GRANT ALL ON SCHEMA "farms_export_pkg" TO postgres;

ALTER SCHEMA "farms_export_pkg" OWNER TO "app_farms";