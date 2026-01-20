-- schema
GRANT ALL ON SCHEMA "farms_fifo_pkg" TO "app_farms";
GRANT ALL ON SCHEMA "farms_fifo_pkg" TO postgres;
GRANT USAGE ON SCHEMA "farms_fifo_pkg" TO "app_farms_rest_proxy";

-- functions
GRANT EXECUTE ON FUNCTION farms_fifo_pkg.read_fifo_calculation_items TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_fifo_pkg.read_fifo_status_by_year TO "app_farms_rest_proxy";
