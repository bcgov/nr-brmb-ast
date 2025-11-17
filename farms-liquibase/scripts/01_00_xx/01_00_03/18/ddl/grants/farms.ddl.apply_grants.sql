GRANT EXECUTE ON PROCEDURE farms_ivpr_pkg.staging_to_operational TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_ivpr_pkg.insert_staging_row TO "app_farms_rest_proxy";

GRANT TRUNCATE ON TABLE farms.farm_zivpr_iv_premium_rates TO "app_farms_rest_proxy";
