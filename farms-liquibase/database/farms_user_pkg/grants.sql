-- schema
GRANT ALL ON SCHEMA "farms_user_pkg" TO "app_farms";
GRANT ALL ON SCHEMA "farms_user_pkg" TO postgres;
GRANT USAGE ON SCHEMA "farms_user_pkg" TO "app_farms_rest_proxy";

-- functions
GRANT EXECUTE ON FUNCTION farms_user_pkg.get_all_users TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_user_pkg.get_user_by_user_guid TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_user_pkg.get_user_by_user_id TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_user_pkg.get_users TO "app_farms_rest_proxy";

-- procedures
GRANT EXECUTE ON PROCEDURE farms_user_pkg.create_user TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_user_pkg.delete_user TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_user_pkg.update_user TO "app_farms_rest_proxy";
