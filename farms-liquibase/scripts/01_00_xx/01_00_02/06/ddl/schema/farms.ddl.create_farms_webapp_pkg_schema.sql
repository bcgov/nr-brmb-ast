-- SCHEMA: farms_webapp_pkg

-- DROP SCHEMA "farms_webapp_pkg" ;

CREATE SCHEMA "farms_webapp_pkg"
    AUTHORIZATION "app_farms";

GRANT ALL ON SCHEMA "farms_webapp_pkg" TO "app_farms";

GRANT ALL ON SCHEMA "farms_webapp_pkg" TO postgres;

ALTER SCHEMA "farms_webapp_pkg" OWNER TO "app_farms";