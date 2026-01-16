-- schema
GRANT ALL ON SCHEMA "farms_error_pkg" TO "app_farms";
GRANT ALL ON SCHEMA "farms_error_pkg" TO postgres;
GRANT USAGE ON SCHEMA "farms_error_pkg" TO "app_farms_rest_proxy";

-- functions
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_benefit_margins TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_claim TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_income_expense TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_inventory_code TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_mun_code TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_operation TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_operational_partner TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_participant TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_production_insurance TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_production_unit TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_productive_unit_capacity TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_program_year TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_reported_inventory TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_scenario TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify TO "app_farms_rest_proxy";
