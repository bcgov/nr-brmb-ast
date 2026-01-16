create or replace procedure farms_reasonability_write_pkg.update_farm_rsnblty_prdctn_grain_rslts(
    in in_reasonability_test_reslt_id farms.farm_rsn_prdctn_grain_results.reasonability_test_result_id%type,
    in in_productive_capacity_amount farms.farm_rsn_prdctn_grain_results.productive_capacity_amount%type,
    in in_reported_quantity_produced farms.farm_rsn_prdctn_grain_results.reported_quantity_produced%type,
    in in_expected_production_per_unit farms.farm_rsn_prdctn_grain_results.expected_production_per_unit%type,
    in in_expected_quantity_produced farms.farm_rsn_prdctn_grain_results.expected_quantity_produced%type,
    in in_quantity_produced_variance farms.farm_rsn_prdctn_grain_results.quantity_produced_variance%type,
    in in_quantity_produced_wthn_lmt_ind farms.farm_rsn_prdctn_grain_results.quantity_produced_wthn_lmt_ind%type,
    in in_inventory_item_code farms.farm_rsn_prdctn_grain_results.inventory_item_code%type,
    in in_qty_produced_crop_unit_code farms.farm_rsn_prdctn_grain_results.qty_produced_crop_unit_code%type,
    in in_user farms.farm_rsn_prdctn_grain_results.who_created%type
)
language plpgsql
as
$$
begin

    insert into farms.farm_rsn_prdctn_grain_results (
        rsn_prdctn_grain_result_id,
        reasonability_test_result_id,
        productive_capacity_amount,
        reported_quantity_produced,
        expected_production_per_unit,
        expected_quantity_produced,
        quantity_produced_variance,
        quantity_produced_wthn_lmt_ind,
        inventory_item_code,
        qty_produced_crop_unit_code,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        nextval('farms.farm_rpgr_seq'),
        in_reasonability_test_reslt_id,
        in_productive_capacity_amount,
        in_reported_quantity_produced,
        in_expected_production_per_unit,
        in_expected_quantity_produced,
        in_quantity_produced_variance,
        in_quantity_produced_wthn_lmt_ind,
        in_inventory_item_code,
        in_qty_produced_crop_unit_code,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );

end;
$$;
