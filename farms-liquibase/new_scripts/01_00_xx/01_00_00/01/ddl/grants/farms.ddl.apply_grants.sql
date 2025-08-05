GRANT TRUNCATE ON TABLE farms.farm_zbpu_benchmark_per_units TO "app_farms_rest_proxy";
GRANT TRUNCATE ON TABLE farms.farm_import_logs TO "app_farms_rest_proxy";

GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.clear_staging TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.delete_staging_errors TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_bpu_pkg.expense_data_differences TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_bpu_pkg.get_staging_errors TO "app_farms_rest_proxy";
