create or replace procedure farms_reasonability_write_pkg.update_rsnblty_prdctn_fruit_veg_rslts(
    in in_reasonability_test_reslt_id farms.farm_rsn_prdctn_frut_veg_rslts.reasonability_test_result_id%type,
    in in_productive_capacity_amount farms.farm_rsn_prdctn_frut_veg_rslts.productive_capacity_amount%type,
    in in_reported_quantity_produced farms.farm_rsn_prdctn_frut_veg_rslts.reported_quantity_produced%type,
    in in_expected_quantity_produced farms.farm_rsn_prdctn_frut_veg_rslts.expected_quantity_produced%type,
    in in_quantity_produced_variance farms.farm_rsn_prdctn_frut_veg_rslts.quantity_produced_variance%type,
    in in_quantity_produced_wthn_lmt_ind farms.farm_rsn_prdctn_frut_veg_rslts.quantity_produced_wthn_lmt_ind%type,
    in in_fruit_veg_code farms.farm_rsn_prdctn_frut_veg_rslts.fruit_veg_type_code%type,
    in in_qty_produced_crop_unit_code farms.farm_rsn_prdctn_frut_veg_rslts.qty_produced_crop_unit_code%type,
    in in_user farms.farm_rsn_prdctn_frut_veg_rslts.who_created%type
)
language plpgsql
as
$$
begin

    insert into farms.farm_rsn_prdctn_frut_veg_rslts(
        rsn_prdctn_frut_veg_rslt_id,
        reasonability_test_result_id,
        productive_capacity_amount,
        reported_quantity_produced,
        expected_quantity_produced,
        quantity_produced_variance,
        quantity_produced_wthn_lmt_ind,
        fruit_veg_type_code,
        qty_produced_crop_unit_code,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        nextval('farms.farm_rpfvr_seq'),
        in_reasonability_test_reslt_id,
        in_productive_capacity_amount,
        in_reported_quantity_produced,
        in_expected_quantity_produced,
        in_quantity_produced_variance,
        in_quantity_produced_wthn_lmt_ind,
        in_fruit_veg_code,
        in_qty_produced_crop_unit_code,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );

end;
$$;
