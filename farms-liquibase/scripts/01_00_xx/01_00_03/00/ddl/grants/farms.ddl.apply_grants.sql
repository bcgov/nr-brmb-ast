GRANT EXECUTE ON PROCEDURE farms_version_pkg.uploaded_version TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_version_pkg.import_failure TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_version_pkg.import_complete TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_webapp_pkg.insert_import_version TO "app_farms_rest_proxy";

GRANT USAGE ON SCHEMA farms_bpu_pkg TO "app_farms_rest_proxy";
GRANT USAGE ON SCHEMA farms_codes_read_pkg TO "app_farms_rest_proxy";
GRANT USAGE ON SCHEMA farms_codes_write_pkg TO "app_farms_rest_proxy";
GRANT USAGE ON SCHEMA farms_error_pkg TO "app_farms_rest_proxy";
GRANT USAGE ON SCHEMA farms_import_pkg TO "app_farms_rest_proxy";
GRANT USAGE ON SCHEMA farms_read_pkg TO "app_farms_rest_proxy";
GRANT USAGE ON SCHEMA farms_report_pkg TO "app_farms_rest_proxy";
GRANT USAGE ON SCHEMA farms_version_pkg TO "app_farms_rest_proxy";
GRANT USAGE ON SCHEMA farms_webapp_pkg TO "app_farms_rest_proxy";

GRANT TRUNCATE ON TABLE farms.zbpu_benchmark_per_unit TO "app_farms_rest_proxy";
GRANT TRUNCATE ON TABLE farms.import_log TO "app_farms_rest_proxy";
