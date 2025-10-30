-- SCHEMA: farms_adjustment_pkg

-- DROP SCHEMA "farms_adjustment_pkg" ;

CREATE SCHEMA "farms_adjustment_pkg"
    AUTHORIZATION "app_farms";

GRANT ALL ON SCHEMA "farms_adjustment_pkg" TO "app_farms";

GRANT ALL ON SCHEMA "farms_adjustment_pkg" TO postgres;

ALTER SCHEMA "farms_adjustment_pkg" OWNER TO "app_farms";