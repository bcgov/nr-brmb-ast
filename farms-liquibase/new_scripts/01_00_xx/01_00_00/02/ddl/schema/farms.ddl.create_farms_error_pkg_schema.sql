-- SCHEMA: farms_error_pkg

-- DROP SCHEMA "farms_error_pkg" ;

CREATE SCHEMA "farms_error_pkg"
    AUTHORIZATION "app_farms";

GRANT ALL ON SCHEMA "farms_error_pkg" TO "app_farms";

GRANT ALL ON SCHEMA "farms_error_pkg" TO postgres;

ALTER SCHEMA "farms_error_pkg" OWNER TO "app_farms";