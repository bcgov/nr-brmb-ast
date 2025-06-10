-- SCHEMA: farms_version_pkg

-- DROP SCHEMA "farms_version_pkg" ;

CREATE SCHEMA "farms_version_pkg"
    AUTHORIZATION "app_farms";

GRANT ALL ON SCHEMA "farms_version_pkg" TO "app_farms";

GRANT ALL ON SCHEMA "farms_version_pkg" TO postgres;

ALTER SCHEMA "farms_version_pkg" OWNER TO "app_farms";