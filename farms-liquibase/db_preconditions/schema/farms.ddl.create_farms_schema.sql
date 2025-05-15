-- SCHEMA: farms

-- DROP SCHEMA "farms" ;

\connect "farms${ENV}"

CREATE SCHEMA "farms"
    AUTHORIZATION postgres;

GRANT ALL ON SCHEMA "farms" TO "app_farms";

GRANT ALL ON SCHEMA "farms" TO postgres;

ALTER SCHEMA "farms" OWNER TO "app_farms";