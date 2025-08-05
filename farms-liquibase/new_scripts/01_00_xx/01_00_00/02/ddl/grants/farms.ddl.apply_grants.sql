GRANT USAGE ON SCHEMA farms_error_pkg TO "app_farms_rest_proxy";

GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_benefit_margins TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_claim TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_income_expense TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_inventory_code TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_mun_code TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_operation TO "app_farms_rest_proxy";
