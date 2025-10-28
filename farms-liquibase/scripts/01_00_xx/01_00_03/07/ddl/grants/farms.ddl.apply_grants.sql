GRANT EXECUTE ON FUNCTION farms_webapp_pkg.get_encryption_key TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_webapp_pkg.encrypt(text) TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_webapp_pkg.decrypt(text) TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_webapp_pkg.get_inventory_class_codes TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_webapp_pkg.get_inventory_group_codes TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_webapp_pkg.get_export_class_codes TO "app_farms_rest_proxy";
