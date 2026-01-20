-- schema
GRANT ALL ON SCHEMA "farms_adjustment_pkg" TO "app_farms";
GRANT ALL ON SCHEMA "farms_adjustment_pkg" TO postgres;
GRANT USAGE ON SCHEMA "farms_adjustment_pkg" TO "app_farms_rest_proxy";

-- procedures
GRANT EXECUTE ON PROCEDURE farms_adjustment_pkg.adjust_income_expense TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_adjustment_pkg.adjust_inv TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_adjustment_pkg.adjust_puc TO "app_farms_rest_proxy";
