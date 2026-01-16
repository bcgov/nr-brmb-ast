create or replace procedure farms_reasonability_write_pkg.update_farm_rsnblty_rev_fruit_veg_inventory(
    in in_reasonability_test_result_id farms.farm_rsn_rev_fruit_veg_invntrs.reasonability_test_result_id%type,
    in in_quantity_produced farms.farm_rsn_rev_fruit_veg_invntrs.quantity_produced%type,
    in in_expected_price farms.farm_rsn_rev_fruit_veg_invntrs.expected_price%type,
    in in_expected_revenue farms.farm_rsn_rev_fruit_veg_invntrs.expected_revenue%type,
    in in_inventory_item_code farms.farm_rsn_rev_fruit_veg_invntrs.inventory_item_code%type,
    in in_crop_unit_code farms.farm_rsn_rev_fruit_veg_invntrs.crop_unit_code%type,
    in in_user farms.farm_rsn_rev_fruit_veg_invntrs.who_created%type
)
language plpgsql
as
$$
begin

    insert into farms.farm_rsn_rev_fruit_veg_invntrs (
        rsn_rev_fruit_veg_invntry_id,
        quantity_produced,
        expected_price,
        expected_revenue,
        inventory_item_code,
        crop_unit_code,
        reasonability_test_result_id,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        nextval('farms.farm_rrfvi_seq'),
        in_quantity_produced,
        in_expected_price,
        in_expected_revenue,
        in_inventory_item_code,
        in_crop_unit_code,
        in_reasonability_test_result_id,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );

end;
$$;
