-- schema
GRANT ALL ON SCHEMA "farms_ivpr_pkg" TO "app_farms";
GRANT ALL ON SCHEMA "farms_ivpr_pkg" TO postgres;
GRANT USAGE ON SCHEMA "farms_ivpr_pkg" TO "app_farms_rest_proxy";

-- functions
GRANT EXECUTE ON FUNCTION farms_ivpr_pkg.get_staging_errors TO "app_farms_rest_proxy";

-- procedures
GRANT EXECUTE ON PROCEDURE farms_ivpr_pkg.clear_staging TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_ivpr_pkg.delete_staging_errors TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_ivpr_pkg.insert_error TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_ivpr_pkg.insert_staging_row TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_ivpr_pkg.staging_to_operational TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_ivpr_pkg.validate_inventory TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_ivpr_pkg.validate_staging TO "app_farms_rest_proxy";
