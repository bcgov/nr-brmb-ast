GRANT EXECUTE ON FUNCTION farms_version_pkg.create_version TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_version_pkg.start_upload TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_version_pkg.update_control_file_info_stg TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_version_pkg.uploaded_version TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_version_pkg.upload_failure TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_version_pkg.start_import TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_version_pkg.perform_import TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_version_pkg.import_failure TO "app_farms_rest_proxy";
