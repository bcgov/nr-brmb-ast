-- SCHEMA: farms_chefs_pkg

-- DROP SCHEMA "farms_chefs_pkg" ;

CREATE SCHEMA "farms_chefs_pkg"
    AUTHORIZATION "app_farms";

GRANT ALL ON SCHEMA "farms_chefs_pkg" TO "app_farms";

GRANT ALL ON SCHEMA "farms_chefs_pkg" TO postgres;

ALTER SCHEMA "farms_chefs_pkg" OWNER TO "app_farms";