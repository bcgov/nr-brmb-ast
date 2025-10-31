GRANT EXECUTE ON PROCEDURE farms_negative_margin_pkg.calculate_negative_margins TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_negative_margin_pkg.update_negative_margin TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_negative_margin_pkg.get_negative_margins TO "app_farms_rest_proxy";

GRANT USAGE ON SCHEMA farms_negative_margin_pkg TO "app_farms_rest_proxy";
