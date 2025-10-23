GRANT EXECUTE ON PROCEDURE farms_calculator_pkg.update_scenario_state TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_calculator_pkg.update_scenario_category TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_calculator_pkg.update_scenario_participnt_data_src_code TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_calculator_pkg.read_operations_for_puc_import TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_calculator_pkg.read_operations_for_inventory_import TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_calculator_pkg.read_operations_for_accrual_import TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_calculator_pkg.update_all_sc_rev TO "app_farms_rest_proxy";

GRANT EXECUTE ON FUNCTION farms_types_pkg.data_not_current_code TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_types_pkg.data_not_current_msg TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_types_pkg.revision_count_increment TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_types_pkg.invalid_revision_count_code TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_types_pkg.invalid_revision_count_msg TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_types_pkg.scenario_not_found_num TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_types_pkg.scenario_not_found_msg TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_types_pkg.invalid_ref_scenario_count_num TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_types_pkg.invalid_ref_scenario_count_msg TO "app_farms_rest_proxy";

GRANT USAGE ON SCHEMA farms_calculator_pkg TO "app_farms_rest_proxy";
GRANT USAGE ON SCHEMA farms_types_pkg TO "app_farms_rest_proxy";
