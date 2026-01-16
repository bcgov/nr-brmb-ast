create or replace function farms_reasonability_read_pkg.read_farm_rsnblty_rev_g_f_fs_invn_rslts(
    in in_reasonability_test_id farms.farm_rsn_rev_g_f_fs_invntories.reasonability_test_result_id%type,
    in in_program_year farms.farm_inventory_item_details.program_year%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        select rrgi.quantity_produced,
               rrgi.quantity_start,
               rrgi.quantity_end,
               rrgi.quantity_consumed,
               rrgi.quantity_sold,
               rrgi.expected_revenue,
               rrgi.reported_price,
               rrgi.inventory_item_code,
               iic.description inventory_item_desc,
               rrgi.crop_unit_code,
               cuc.description crop_unit_desc,
               iid.commodity_type_code,
               ctc.description commodity_type_code_description
        from farms.farm_rsn_rev_g_f_fs_invntories rrgi
        join farms.farm_inventory_item_codes iic on iic.inventory_item_code = rrgi.inventory_item_code
        join farms.farm_inventory_item_details iid on iid.program_year = in_program_year
                                                   and iid.inventory_item_code = rrgi.inventory_item_code
        join farms.farm_crop_unit_codes cuc on cuc.crop_unit_code = rrgi.crop_unit_code
        left outer join farms.farm_commodity_type_codes ctc on ctc.commodity_type_code = iid.commodity_type_code
        where rrgi.reasonability_test_result_id = in_reasonability_test_id
        order by (rrgi.inventory_item_code)::numeric;

    return cur;

end;
$$;
