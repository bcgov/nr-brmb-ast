GRANT EXECUTE ON PROCEDURE farms_calculator_pkg.update_scenario_state TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_calculator_pkg.update_scenario_category TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_calculator_pkg.update_scenario_participnt_data_src_code TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_calculator_pkg.read_operations_for_puc_import TO "app_farms_rest_proxy";

GRANT USAGE ON SCHEMA farms_calculator_pkg TO "app_farms_rest_proxy";
