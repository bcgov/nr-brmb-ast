-- schema
GRANT ALL ON SCHEMA "farms_staging_pkg" TO "app_farms";
GRANT ALL ON SCHEMA "farms_staging_pkg" TO postgres;
GRANT USAGE ON SCHEMA "farms_staging_pkg" TO "app_farms_rest_proxy";

-- functions
GRANT EXECUTE ON PROCEDURE farms_staging_pkg.clear TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_staging_pkg.insert_z01 TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_staging_pkg.insert_z02 TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_staging_pkg.insert_z03 TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_staging_pkg.insert_z04 TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_staging_pkg.insert_z05 TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_staging_pkg.insert_z21 TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_staging_pkg.insert_z22 TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_staging_pkg.insert_z23 TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_staging_pkg.insert_z28 TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_staging_pkg.insert_z29 TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_staging_pkg.insert_z40 TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_staging_pkg.insert_z42 TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_staging_pkg.insert_z50 TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_staging_pkg.insert_z51 TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_staging_pkg.insert_z99 TO "app_farms_rest_proxy";
