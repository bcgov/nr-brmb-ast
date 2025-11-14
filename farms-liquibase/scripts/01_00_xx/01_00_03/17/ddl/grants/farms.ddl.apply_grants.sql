GRANT EXECUTE ON PROCEDURE farms_fmv_pkg.staging_to_operational TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_fmv_pkg.insert_staging_row TO "app_farms_rest_proxy";

GRANT TRUNCATE ON TABLE farms.farm_zfmv_fair_market_values TO "app_farms_rest_proxy";
