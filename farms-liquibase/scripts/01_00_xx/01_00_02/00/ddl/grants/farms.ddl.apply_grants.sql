GRANT EXECUTE ON FUNCTION farms_bpu_pkg.is_group_code(VARCHAR) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.insert_error(NUMERIC, NUMERIC, VARCHAR) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.validate_program_year(NUMERIC) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.validate_municipality(NUMERIC) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.validate_expense(NUMERIC) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.validate_inventory(NUMERIC) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.clear_staging() TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.insert_staging_row(
    farms.zbpu_benchmark_per_unit.line_number%TYPE,
    farms.zbpu_benchmark_per_unit.program_year%TYPE,
    farms.zbpu_benchmark_per_unit.municipality_code%TYPE,
    farms.zbpu_benchmark_per_unit.inventory_item_code%TYPE,
    farms.zbpu_benchmark_per_unit.unit_comment%TYPE,
    farms.zbpu_benchmark_per_unit.year_minus_6_margin%TYPE,
    farms.zbpu_benchmark_per_unit.year_minus_5_margin%TYPE,
    farms.zbpu_benchmark_per_unit.year_minus_4_margin%TYPE,
    farms.zbpu_benchmark_per_unit.year_minus_3_margin%TYPE,
    farms.zbpu_benchmark_per_unit.year_minus_2_margin%TYPE,
    farms.zbpu_benchmark_per_unit.year_minus_1_margin%TYPE,
    farms.zbpu_benchmark_per_unit.year_minus_6_expense%TYPE,
    farms.zbpu_benchmark_per_unit.year_minus_5_expense%TYPE,
    farms.zbpu_benchmark_per_unit.year_minus_4_expense%TYPE,
    farms.zbpu_benchmark_per_unit.year_minus_3_expense%TYPE,
    farms.zbpu_benchmark_per_unit.year_minus_2_expense%TYPE,
    farms.zbpu_benchmark_per_unit.year_minus_1_expense%TYPE,
    VARCHAR
) TO "app_farms_rest_proxy";
