-- SCHEMA: farms_codes_write_pkg

-- DROP SCHEMA "farms_codes_write_pkg" ;

CREATE SCHEMA "farms_codes_write_pkg"
    AUTHORIZATION "app_farms";

GRANT ALL ON SCHEMA "farms_codes_write_pkg" TO "app_farms";

GRANT ALL ON SCHEMA "farms_codes_write_pkg" TO postgres;

ALTER SCHEMA "farms_codes_write_pkg" OWNER TO "app_farms";