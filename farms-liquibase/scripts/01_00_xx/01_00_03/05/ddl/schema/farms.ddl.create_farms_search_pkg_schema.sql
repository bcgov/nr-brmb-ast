-- SCHEMA: farms_search_pkg

-- DROP SCHEMA "farms_search_pkg" ;

CREATE SCHEMA "farms_search_pkg"
    AUTHORIZATION "app_farms";

GRANT ALL ON SCHEMA "farms_search_pkg" TO "app_farms";

GRANT ALL ON SCHEMA "farms_search_pkg" TO postgres;

ALTER SCHEMA "farms_search_pkg" OWNER TO "app_farms";