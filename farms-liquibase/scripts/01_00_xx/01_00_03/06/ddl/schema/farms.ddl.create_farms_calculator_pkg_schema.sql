-- SCHEMA: farms_calculator_pkg

-- DROP SCHEMA "farms_calculator_pkg" ;

CREATE SCHEMA "farms_calculator_pkg"
    AUTHORIZATION "app_farms";

GRANT ALL ON SCHEMA "farms_calculator_pkg" TO "app_farms";

GRANT ALL ON SCHEMA "farms_calculator_pkg" TO postgres;

ALTER SCHEMA "farms_calculator_pkg" OWNER TO "app_farms";