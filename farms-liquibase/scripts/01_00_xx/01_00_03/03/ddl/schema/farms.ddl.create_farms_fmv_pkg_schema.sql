-- SCHEMA: farms_fmv_pkg

-- DROP SCHEMA "farms_fmv_pkg" ;

CREATE SCHEMA "farms_fmv_pkg"
    AUTHORIZATION "app_farms";

GRANT ALL ON SCHEMA "farms_fmv_pkg" TO "app_farms";

GRANT ALL ON SCHEMA "farms_fmv_pkg" TO postgres;

ALTER SCHEMA "farms_fmv_pkg" OWNER TO "app_farms";