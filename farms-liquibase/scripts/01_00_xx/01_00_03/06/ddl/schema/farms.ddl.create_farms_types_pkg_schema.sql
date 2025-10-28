-- SCHEMA: farms_types_pkg

-- DROP SCHEMA "farms_types_pkg" ;

CREATE SCHEMA "farms_types_pkg"
    AUTHORIZATION "app_farms";

GRANT ALL ON SCHEMA "farms_types_pkg" TO "app_farms";

GRANT ALL ON SCHEMA "farms_types_pkg" TO postgres;

ALTER SCHEMA "farms_types_pkg" OWNER TO "app_farms";