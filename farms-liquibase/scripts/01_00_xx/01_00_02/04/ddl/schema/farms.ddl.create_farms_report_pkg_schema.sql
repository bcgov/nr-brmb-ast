-- SCHEMA: farms_report_pkg

-- DROP SCHEMA "farms_report_pkg" ;

CREATE SCHEMA "farms_report_pkg"
    AUTHORIZATION "app_farms";

GRANT ALL ON SCHEMA "farms_report_pkg" TO "app_farms";

GRANT ALL ON SCHEMA "farms_report_pkg" TO postgres;

ALTER SCHEMA "farms_report_pkg" OWNER TO "app_farms";