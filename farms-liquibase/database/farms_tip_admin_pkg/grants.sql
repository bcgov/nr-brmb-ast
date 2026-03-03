-- schema
GRANT ALL ON SCHEMA "farms_tip_admin_pkg" TO "app_farms";
GRANT ALL ON SCHEMA "farms_tip_admin_pkg" TO postgres;
GRANT USAGE ON SCHEMA "farms_tip_admin_pkg" TO "app_farms_rest_proxy";

-- procedures
GRANT EXECUTE ON PROCEDURE farms_tip_admin_pkg.benchmark_5year_averages TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_tip_admin_pkg.benchmark_years TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_tip_admin_pkg.copy_expenses_benchmarks TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_tip_admin_pkg.delete_benchmarks TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_tip_admin_pkg.expenses_benchmarks_5yr_avg TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_tip_admin_pkg.expenses_benchmarks_ref_years TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_tip_admin_pkg.expenses_benchmarks TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_tip_admin_pkg.expenses_five_year_averages TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_tip_admin_pkg.expenses TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_tip_admin_pkg.five_year_averages TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_tip_admin_pkg.generate_benchmarks TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_tip_admin_pkg.group_assignment TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_tip_admin_pkg.program_year_report_results TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_tip_admin_pkg.reference_year_report_results TO "app_farms_rest_proxy";
