-- SCHEMA: farms_codes_read_pkg

-- DROP SCHEMA "farms_codes_read_pkg" ;

CREATE SCHEMA "farms_codes_read_pkg"
    AUTHORIZATION "app_farms";

GRANT ALL ON SCHEMA "farms_codes_read_pkg" TO "app_farms";

GRANT ALL ON SCHEMA "farms_codes_read_pkg" TO postgres;

ALTER SCHEMA "farms_codes_read_pkg" OWNER TO "app_farms";