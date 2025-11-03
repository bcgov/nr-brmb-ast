GRANT EXECUTE ON FUNCTION farms_reasonability_read_pkg.read_reasonability_tests TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_reasonability_read_pkg.read_reasonability_benefit_risk_pu TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_reasonability_read_pkg.read_reasonability_forage_production_test TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_reasonability_read_pkg.read_production_fruit_veg_inventory TO "app_farms_rest_proxy";

GRANT USAGE ON SCHEMA farms_reasonability_read_pkg TO "app_farms_rest_proxy";
