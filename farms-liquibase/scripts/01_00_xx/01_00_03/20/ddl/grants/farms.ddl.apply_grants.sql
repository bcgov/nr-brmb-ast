GRANT EXECUTE ON FUNCTION farms_read_pkg.read_py_id TO "app_farms_rest_proxy";

GRANT EXECUTE ON FUNCTION farms_enrolment_write_pkg.get_scenario_id TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_enrolment_write_pkg.generate_non_enw_enrolment TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_enrolment_write_pkg.generate_enrolments TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_enrolment_write_pkg.update_staging TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_enrolment_write_pkg.copy_staging_to_operational TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_enrolment_write_pkg.upsert_operational TO "app_farms_rest_proxy";

GRANT USAGE ON SCHEMA farms_enrolment_write_pkg TO "app_farms_rest_proxy";
