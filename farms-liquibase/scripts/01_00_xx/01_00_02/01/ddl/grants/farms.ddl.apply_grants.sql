GRANT EXECUTE ON FUNCTION farms_error_pkg.codify(VARCHAR) TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_mun_code(VARCHAR) TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_production_unit(
    VARCHAR,
    farms.crop_unit_code.crop_unit_code%TYPE
) TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_inventory_code(VARCHAR, VARCHAR, DATE) TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_participant(
    VARCHAR,
    farms.agristability_client.participant_pin%TYPE,
    farms.agristability_client.participant_class_code%TYPE,
    farms.agristability_client.participant_language_code%TYPE
) TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_reported_inventory(
    VARCHAR,
    farms.agristabilty_commodity_xref.inventory_item_code%TYPE,
    farms.agristabilty_commodity_xref.inventory_class_code%TYPE,
    farms.reported_inventory.crop_unit_code%TYPE,
    farms.reported_inventory.farming_operation_id%TYPE
) TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_productive_unit_capacity(
    VARCHAR,
    farms.productive_unit_capacity.farming_operation_id%TYPE,
    farms.productive_unit_capacity.structure_group_code%TYPE,
    farms.productive_unit_capacity.inventory_item_code%TYPE
) TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_income_expense(
    VARCHAR,
    farms.line_item.line_item%TYPE,
    farms.reported_income_expenses.farming_operation_id%TYPE
) TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_claim(
    VARCHAR,
    farms.agristability_claim.agristability_scenario_id%TYPE
) TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_benefit_margins(
    VARCHAR,
    farms.benefit_calculation_total.agristability_scenario_id%TYPE
) TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_scenario(
    VARCHAR,
    farms.agristability_scenario.agristability_scenario_id%TYPE
) TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_operational_partner(
    VARCHAR,
    farms.farming_operatin_partner.farming_operation_id%TYPE
) TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_error_pkg.codify_production_insurance(
    VARCHAR,
    farms.farming_operatin_partner.farming_operation_id%TYPE
) TO "app_farms_rest_proxy";

GRANT EXECUTE ON FUNCTION farms_import_pkg.numbers_equal(NUMERIC, NUMERIC) TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_import_pkg.text_equal(VARCHAR, VARCHAR) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_import_pkg.update_status(NUMERIC, VARCHAR) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_import_pkg.update_status_non_autonomous(NUMERIC, VARCHAR) TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_import_pkg.append_imp(NUMERIC, VARCHAR) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_import_pkg.update_imp(NUMERIC, VARCHAR) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_import_pkg.append_imp1(NUMERIC, VARCHAR) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_import_pkg.clear_log(NUMERIC) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_import_pkg.close_global_log(NUMERIC) TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_import_pkg.scrub(VARCHAR) TO "app_farms_rest_proxy";

GRANT EXECUTE ON FUNCTION farms_version_pkg.create_version(
    farms.import_version.description%TYPE,
    farms.import_version.import_file_name%TYPE,
    VARCHAR
) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_version_pkg.start_upload(NUMERIC, VARCHAR) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_version_pkg.update_control_file_info_stg(NUMERIC, VARCHAR) TO "app_farms_rest_proxy";
GRANT EXECUTE ON FUNCTION farms_version_pkg.uploaded_version(NUMERIC, VARCHAR, VARCHAR) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_version_pkg.upload_failure(NUMERIC, VARCHAR, VARCHAR) TO "app_farms_rest_proxy";
GRANT EXECUTE ON PROCEDURE farms_version_pkg.start_import(NUMERIC, VARCHAR) TO "app_farms_rest_proxy";