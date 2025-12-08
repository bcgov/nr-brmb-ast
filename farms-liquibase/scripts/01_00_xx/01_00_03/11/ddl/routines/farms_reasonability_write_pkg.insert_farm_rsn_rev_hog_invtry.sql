create or replace procedure farms_reasonability_write_pkg.insert_farm_rsn_rev_hog_invtry(
    in in_reasonability_test_reslt_id farms.farm_rsn_rev_hog_results.reasonability_test_result_id%type,
    in in_quantity_start farms.farm_rsn_rev_hog_inventories.quantity_start%type,
    in in_quantity_end farms.farm_rsn_rev_hog_inventories.quantity_end%type,
    in in_fmv_price farms.farm_rsn_rev_hog_inventories.fmv_price%type,
    in in_inventory_item_code farms.farm_rsn_rev_hog_inventories.inventory_item_code%type,
    in in_user farms.farm_rsn_rev_g_f_fs_invntories.who_created%type
)
language plpgsql
as
$$
begin

    insert into farms.farm_rsn_rev_hog_inventories (
        rsn_rev_hog_inventory_id,
        quantity_start,
        quantity_end,
        fmv_price,
        inventory_item_code,
        rsn_rev_hog_result_id,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        nextval('farms.farm_rrhi_seq'),
        in_quantity_start,
        in_quantity_end,
        in_fmv_price,
        in_inventory_item_code,
        (
            select rsn_rev_hog_result_id
            from farms.farm_rsn_rev_hog_results
            where reasonability_test_result_id = in_reasonability_test_reslt_id
        ),
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );

end;
$$;
