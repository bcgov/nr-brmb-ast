-- schema
GRANT ALL ON SCHEMA "farms_import_xml_pkg" TO "app_farms";
GRANT ALL ON SCHEMA "farms_import_xml_pkg" TO postgres;
GRANT USAGE ON SCHEMA "farms_import_xml_pkg" TO "app_farms_rest_proxy";

-- functions
GRANT EXECUTE ON FUNCTION farms_import_xml_pkg.get_import_log TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_import_xml_pkg.get_import_numbers TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_import_xml_pkg.get_import_top_level_errors TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_import_xml_pkg.get_staging_log TO "app_farms_rest_proxy";
