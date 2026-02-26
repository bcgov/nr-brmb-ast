-- schema
GRANT ALL ON SCHEMA "farms_aarm_pkg" TO "app_farms";
GRANT ALL ON SCHEMA "farms_aarm_pkg" TO postgres;
GRANT USAGE ON SCHEMA "farms_aarm_pkg" TO "app_farms_rest_proxy";

-- procedures
GRANT EXECUTE ON PROCEDURE farms_aarm_pkg.clear_staging TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_aarm_pkg.insert_staging_row TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_aarm_pkg.staging_to_operational TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_aarm_pkg.update_prices TO "app_farms_rest_proxy";
