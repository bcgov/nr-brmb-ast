-- SCHEMA: farms_fifo_pkg

-- DROP SCHEMA "farms_fifo_pkg" ;

CREATE SCHEMA "farms_fifo_pkg"
    AUTHORIZATION "app_farms";

GRANT ALL ON SCHEMA "farms_fifo_pkg" TO "app_farms";

GRANT ALL ON SCHEMA "farms_fifo_pkg" TO postgres;

ALTER SCHEMA "farms_fifo_pkg" OWNER TO "app_farms";