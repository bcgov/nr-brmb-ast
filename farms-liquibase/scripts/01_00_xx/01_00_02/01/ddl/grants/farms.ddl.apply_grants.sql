GRANT EXECUTE ON FUNCTION farms_import_pkg.numbers_equal(NUMERIC, NUMERIC) TO "app_farms_rest_proxy";

GRANT EXECUTE ON FUNCTION farms_version_pkg.create_version(
    farms.import_version.description%TYPE,
    farms.import_version.import_file_name%TYPE,
    VARCHAR
) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_version_pkg.start_upload(NUMERIC, VARCHAR) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_version_pkg.update_control_file_info_stg(NUMERIC, VARCHAR) TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_version_pkg.uploaded_version(NUMERIC, VARCHAR, VARCHAR) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_version_pkg.upload_failure(NUMERIC, VARCHAR, VARCHAR) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_version_pkg.start_import(NUMERIC, VARCHAR) TO "app_farms_rest_proxy";