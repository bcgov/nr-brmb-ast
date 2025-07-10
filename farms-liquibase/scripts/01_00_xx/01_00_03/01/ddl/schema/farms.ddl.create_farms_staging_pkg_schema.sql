-- SCHEMA: farms_staging_pkg

-- DROP SCHEMA "farms_staging_pkg" ;

CREATE SCHEMA "farms_staging_pkg"
    AUTHORIZATION "app_farms";

GRANT ALL ON SCHEMA "farms_staging_pkg" TO "app_farms";

GRANT ALL ON SCHEMA "farms_staging_pkg" TO postgres;

ALTER SCHEMA "farms_staging_pkg" OWNER TO "app_farms";