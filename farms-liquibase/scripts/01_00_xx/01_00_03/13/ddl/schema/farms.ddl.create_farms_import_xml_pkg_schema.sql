-- SCHEMA: farms_import_xml_pkg

-- DROP SCHEMA "farms_import_xml_pkg" ;

CREATE SCHEMA "farms_import_xml_pkg"
    AUTHORIZATION "app_farms";

GRANT ALL ON SCHEMA "farms_import_xml_pkg" TO "app_farms";

GRANT ALL ON SCHEMA "farms_import_xml_pkg" TO postgres;

ALTER SCHEMA "farms_import_xml_pkg" OWNER TO "app_farms";