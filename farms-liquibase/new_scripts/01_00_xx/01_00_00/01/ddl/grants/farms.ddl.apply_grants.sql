GRANT USAGE ON SCHEMA farms_bpu_pkg TO "app_farms_rest_proxy";

GRANT TRUNCATE ON TABLE farms.farm_zbpu_benchmark_per_units TO "app_farms_rest_proxy";
GRANT TRUNCATE ON TABLE farms.farm_import_logs TO "app_farms_rest_proxy";

GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.clear_staging TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.delete_staging_errors TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_bpu_pkg.expense_data_differences TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_bpu_pkg.get_staging_errors TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.insert_error TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.insert_staging_row TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_bpu_pkg.is_group_code TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_bpu_pkg.margin_data_differences TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.staging_to_operational TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.validate_expense TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.validate_inventory TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.validate_municipality TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.validate_program_year TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.validate_staging TO "app_farms_rest_proxy";
