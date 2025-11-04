create or replace procedure farms_reasonability_write_pkg.update_rsn_prdctn_frut_invntries(
    in in_reasonability_test_reslt_id farms.farm_rsn_prdctn_frut_invntries.reasonability_test_result_id%type,
    in in_productive_capacity_amount farms.farm_rsn_prdctn_frut_invntries.productive_capacity_amount%type,
    in in_reported_quantity_produced farms.farm_rsn_prdctn_frut_invntries.reported_quantity_produced%type,
    in in_expected_production_per_unit farms.farm_rsn_prdctn_frut_invntries.expected_production_per_unit%type,
    in in_expected_quantity_produced farms.farm_rsn_prdctn_frut_invntries.expected_quantity_produced%type,
    in in_inventory_item_code farms.farm_rsn_prdctn_frut_invntries.inventory_item_code%type,
    in in_qty_produced_crop_unit_code farms.farm_rsn_prdctn_frut_invntries.qty_produced_crop_unit_code%type,
    in in_user farms.farm_rsn_prdctn_frut_invntries.who_created%type
)
language plpgsql
as
$$
begin

    insert into farms.farm_rsn_prdctn_frut_invntries (
        rsn_prdctn_frut_invntry_id,
        productive_capacity_amount,
        reported_quantity_produced,
        expected_production_per_unit,
        expected_quantity_produced,
        inventory_item_code,
        qty_produced_crop_unit_code,
        reasonability_test_result_id,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        nextval('farms.farm_rpfi_seq'),
        in_productive_capacity_amount,
        in_reported_quantity_produced,
        in_expected_production_per_unit,
        in_expected_quantity_produced,
        in_inventory_item_code,
        in_qty_produced_crop_unit_code,
        in_reasonability_test_reslt_id,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );

end;
$$;
