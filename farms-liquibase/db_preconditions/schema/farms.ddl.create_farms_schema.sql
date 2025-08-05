-- SCHEMA: farms

-- DROP SCHEMA "farms" ;

CREATE SCHEMA "farms"
    AUTHORIZATION postgres;

GRANT ALL ON SCHEMA "farms" TO "app_farms";

GRANT ALL ON SCHEMA "farms" TO postgres;

ALTER SCHEMA "farms" OWNER TO "app_farms";

GRANT CREATE ON DATABASE "farms${ENV}" TO "app_farms";
