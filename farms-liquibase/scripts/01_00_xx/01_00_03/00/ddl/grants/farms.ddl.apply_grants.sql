GRANT EXECUTE ON PROCEDURE farms_version_pkg.uploaded_version TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_version_pkg.import_failure TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_version_pkg.import_complete TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_webapp_pkg.insert_import_version TO "app_farms_rest_proxy";

GRANT USAGE ON SCHEMA farms_webapp_pkg TO "app_farms_rest_proxy";
