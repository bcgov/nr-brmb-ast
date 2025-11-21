GRANT EXECUTE ON PROCEDURE farms_chefs_pkg.create_submission TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_chefs_pkg.update_submission TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_chefs_pkg.delete_submission TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_chefs_pkg.read_submissions_by_form_type TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_chefs_pkg.read_submissions_by_guid TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_chefs_pkg.update_scenario_submission_id TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_chefs_pkg.add_puc TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_chefs_pkg.add_income_expense TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_chefs_pkg.add_inv TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_chefs_pkg.create_crm_entity TO "app_farms_rest_proxy";

GRANT USAGE ON SCHEMA farms_chefs_pkg TO "app_farms_rest_proxy";
