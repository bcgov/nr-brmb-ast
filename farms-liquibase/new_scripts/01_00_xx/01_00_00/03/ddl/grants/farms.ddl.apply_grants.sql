GRANT SELECT ON farms.agri_scenarios_vw TO "app_farms_rest_proxy";

GRANT EXECUTE ON FUNCTION farms_codes_write_pkg.copy_forward_year_config TO "app_farms_rest_proxy";
