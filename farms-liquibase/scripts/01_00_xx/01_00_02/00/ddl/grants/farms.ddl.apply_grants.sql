GRANT EXECUTE ON FUNCTION farms_bpu_pkg.is_group_code(VARCHAR) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.insert_error(NUMERIC, NUMERIC, VARCHAR) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.validate_program_year(NUMERIC) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.validate_municipality(NUMERIC) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_bpu_pkg.validate_expense(NUMERIC) TO "app_farms_rest_proxy";
