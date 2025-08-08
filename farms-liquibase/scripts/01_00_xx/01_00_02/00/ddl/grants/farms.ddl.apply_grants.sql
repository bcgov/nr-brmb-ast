GRANT EXECUTE ON FUNCTION farms_bpu_pkg.is_group_code(VARCHAR) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.insert_error(NUMERIC, NUMERIC, VARCHAR) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.validate_program_year(NUMERIC) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.validate_municipality(NUMERIC) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.validate_expense(NUMERIC) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.validate_inventory(NUMERIC) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.clear_staging() TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.insert_staging_row(
    farms.farm_zbpu_benchmark_per_units.line_number%TYPE,
    farms.farm_zbpu_benchmark_per_units.program_year%TYPE,
    farms.farm_zbpu_benchmark_per_units.municipality_code%TYPE,
    farms.farm_zbpu_benchmark_per_units.inventory_item_code%TYPE,
    farms.farm_zbpu_benchmark_per_units.unit_comment%TYPE,
    farms.farm_zbpu_benchmark_per_units.year_minus_6_margin%TYPE,
    farms.farm_zbpu_benchmark_per_units.year_minus_5_margin%TYPE,
    farms.farm_zbpu_benchmark_per_units.year_minus_4_margin%TYPE,
    farms.farm_zbpu_benchmark_per_units.year_minus_3_margin%TYPE,
    farms.farm_zbpu_benchmark_per_units.year_minus_2_margin%TYPE,
    farms.farm_zbpu_benchmark_per_units.year_minus_1_margin%TYPE,
    farms.farm_zbpu_benchmark_per_units.year_minus_6_expense%TYPE,
    farms.farm_zbpu_benchmark_per_units.year_minus_5_expense%TYPE,
    farms.farm_zbpu_benchmark_per_units.year_minus_4_expense%TYPE,
    farms.farm_zbpu_benchmark_per_units.year_minus_3_expense%TYPE,
    farms.farm_zbpu_benchmark_per_units.year_minus_2_expense%TYPE,
    farms.farm_zbpu_benchmark_per_units.year_minus_1_expense%TYPE,
    VARCHAR
) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.validate_staging(NUMERIC) TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_bpu_pkg.get_staging_errors(NUMERIC) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.delete_staging_errors(NUMERIC) TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_bpu_pkg.margin_data_differences(NUMERIC[], NUMERIC, NUMERIC) TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_bpu_pkg.expense_data_differences(NUMERIC[], NUMERIC, NUMERIC) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.staging_to_operational(NUMERIC, VARCHAR) TO "app_farms_rest_proxy";
