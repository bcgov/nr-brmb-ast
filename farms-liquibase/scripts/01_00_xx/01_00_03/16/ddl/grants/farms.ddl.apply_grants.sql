GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.staging_to_operational TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.insert_staging_row TO "app_farms_rest_proxy";

GRANT TRUNCATE ON TABLE farms.farm_zbpu_benchmark_per_units TO "app_farms_rest_proxy";
