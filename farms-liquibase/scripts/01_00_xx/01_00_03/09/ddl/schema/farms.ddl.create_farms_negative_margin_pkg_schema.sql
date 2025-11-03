-- SCHEMA: farms_negative_margin_pkg

-- DROP SCHEMA "farms_negative_margin_pkg" ;

CREATE SCHEMA "farms_negative_margin_pkg"
    AUTHORIZATION "app_farms";

GRANT ALL ON SCHEMA "farms_negative_margin_pkg" TO "app_farms";

GRANT ALL ON SCHEMA "farms_negative_margin_pkg" TO postgres;

ALTER SCHEMA "farms_negative_margin_pkg" OWNER TO "app_farms";