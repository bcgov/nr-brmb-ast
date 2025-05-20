-- SCHEMA: farms_bpu_pkg

-- DROP SCHEMA "farms_bpu_pkg" ;

CREATE SCHEMA "farms_bpu_pkg"
    AUTHORIZATION postgres;

GRANT ALL ON SCHEMA "farms_bpu_pkg" TO "app_farms";

GRANT ALL ON SCHEMA "farms_bpu_pkg" TO postgres;

ALTER SCHEMA "farms_bpu_pkg" OWNER TO "app_farms";