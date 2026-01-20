-- schema
GRANT ALL ON SCHEMA "farms_report_pkg" TO "app_farms";
GRANT ALL ON SCHEMA "farms_report_pkg" TO postgres;
GRANT USAGE ON SCHEMA "farms_report_pkg" TO "app_farms_rest_proxy";

-- functions
GRANT EXECUTE ON FUNCTION farms_report_pkg.analytical_surveillance_strategy TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_report_pkg.get_farm_type_detailed_codes TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_report_pkg.get_latest_status TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_report_pkg.get_sector_and_detail TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_report_pkg.get_sector_detail_code TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_report_pkg.national_surveillance_strategy TO "app_farms_rest_proxy";
