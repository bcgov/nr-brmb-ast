GRANT USAGE ON SCHEMA farms_ivpr_pkg TO "app_farms_rest_proxy";
GRANT TRUNCATE ON TABLE farms.farm_zivpr_iv_premium_rates TO "app_farms_rest_proxy";

GRANT EXECUTE ON PROCEDURE farms_ivpr_pkg.insert_error TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_ivpr_pkg.validate_inventory TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_ivpr_pkg.insert_staging_row TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_ivpr_pkg.clear_staging TO "app_farms_rest_proxy";
