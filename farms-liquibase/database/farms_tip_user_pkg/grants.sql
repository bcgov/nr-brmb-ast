-- schema
GRANT ALL ON SCHEMA "farms_tip_user_pkg" TO "app_farms";
GRANT ALL ON SCHEMA "farms_tip_user_pkg" TO postgres;
GRANT USAGE ON SCHEMA "farms_tip_user_pkg" TO "app_farms_rest_proxy";

-- functions
GRANT EXECUTE ON FUNCTION farms_tip_user_pkg.benchmarks_match_config TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_tip_user_pkg.check_benchmarks_generated TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_tip_user_pkg.get_tip_report_blob_upd TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_tip_user_pkg.get_tip_report_blob TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_tip_user_pkg.get_tip_report_document_id TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_tip_user_pkg.read_tip_benchmark_expenses TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_tip_user_pkg.read_tip_benchmark_groups TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_tip_user_pkg.read_tip_benchmark_summary_report_expenses TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_tip_user_pkg.read_tip_benchmark_summary_report TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_tip_user_pkg.read_tip_benchmark_years TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_tip_user_pkg.read_tip_farm_report_pins TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_tip_user_pkg.read_tip_grouping_config TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_tip_user_pkg.read_tip_individual_data TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_tip_user_pkg.read_tip_individual_expenses TO "app_farms_rest_proxy";

-- procedures
GRANT EXECUTE ON PROCEDURE farms_tip_user_pkg.update_tip_participant_flag TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_tip_user_pkg.update_tip_report TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_tip_user_pkg.upsert_tip_report_document TO "app_farms_rest_proxy";
