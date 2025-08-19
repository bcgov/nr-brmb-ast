-- SCHEMA: farms_ivpr_pkg

-- DROP SCHEMA "farms_ivpr_pkg" ;

CREATE SCHEMA "farms_ivpr_pkg"
    AUTHORIZATION "app_farms";

GRANT ALL ON SCHEMA "farms_ivpr_pkg" TO "app_farms";

GRANT ALL ON SCHEMA "farms_ivpr_pkg" TO postgres;

ALTER SCHEMA "farms_ivpr_pkg" OWNER TO "app_farms";