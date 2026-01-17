-- schema
GRANT ALL ON SCHEMA "farms_types_pkg" TO "app_farms";
GRANT ALL ON SCHEMA "farms_types_pkg" TO postgres;
GRANT USAGE ON SCHEMA "farms_types_pkg" TO "app_farms_rest_proxy";

-- functions
GRANT EXECUTE ON FUNCTION farms_types_pkg.data_not_current_code TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_types_pkg.data_not_current_msg TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_types_pkg.in_prog_scenario_not_exist_msg TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_types_pkg.in_prog_scenario_not_exist_num TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_types_pkg.invalid_ref_scenario_count_msg TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_types_pkg.invalid_ref_scenario_count_num TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_types_pkg.invalid_revision_count_code TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_types_pkg.invalid_revision_count_msg TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_types_pkg.line_item_not_found_msg TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_types_pkg.line_item_not_found_num TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_types_pkg.pin_already_in_comb_farm_msg TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_types_pkg.pin_already_in_comb_farm_num TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_types_pkg.pin_does_not_exist_msg TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_types_pkg.pin_does_not_exist_num TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_types_pkg.revision_count_increment TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_types_pkg.scenario_not_found_msg TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_types_pkg.scenario_not_found_num TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_types_pkg.user_scenario_only_code TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_types_pkg.user_scenario_only_msg TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_types_pkg.xref_more_than_one_found_msg TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_types_pkg.xref_more_than_one_found_num TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_types_pkg.xref_not_found_msg TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_types_pkg.xref_not_found_num TO "app_farms_rest_proxy";
