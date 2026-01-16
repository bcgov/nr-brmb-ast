create or replace procedure farms_reasonability_write_pkg.update_farm_rsnblty_rev_g_f_fs_invn_rslts(
    in in_reasonability_test_reslt_id farms.farm_rsn_rev_g_f_fs_invntories.reasonability_test_result_id%type,
    in in_quantity_produced farms.farm_rsn_rev_g_f_fs_invntories.quantity_produced%type,
    in in_quantity_start farms.farm_rsn_rev_g_f_fs_invntories.quantity_start%type,
    in in_quantity_end farms.farm_rsn_rev_g_f_fs_invntories.quantity_end%type,
    in in_quantity_consumed farms.farm_rsn_rev_g_f_fs_invntories.quantity_consumed%type,
    in in_quantity_sold farms.farm_rsn_rev_g_f_fs_invntories.quantity_sold%type,
    in in_expected_revenue farms.farm_rsn_rev_g_f_fs_invntories.expected_revenue%type,
    in in_reported_price farms.farm_rsn_rev_g_f_fs_invntories.reported_price%type,
    in in_inventory_item_code farms.farm_rsn_rev_g_f_fs_invntories.inventory_item_code%type,
    in in_crop_unit_code farms.farm_rsn_rev_g_f_fs_invntories.crop_unit_code%type,
    in in_user farms.farm_rsn_rev_g_f_fs_invntories.who_created%type
)
language plpgsql
as
$$
begin

    insert into farms.farm_rsn_rev_g_f_fs_invntories (
        rsn_rev_g_f_fs_invntories_id,
        reasonability_test_result_id,
        quantity_produced,
        quantity_start,
        quantity_end,
        quantity_consumed,
        quantity_sold,
        expected_revenue,
        reported_price,
        inventory_item_code,
        crop_unit_code,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        nextval('farms.farm_rrgi_seq'),
        in_reasonability_test_reslt_id,
        in_quantity_produced,
        in_quantity_start,
        in_quantity_end,
        in_quantity_consumed,
        in_quantity_sold,
        in_expected_revenue,
        in_reported_price,
        in_inventory_item_code,
        in_crop_unit_code,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );

end;
$$;
