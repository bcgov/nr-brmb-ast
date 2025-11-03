create or replace function farms_reasonability_read_pkg.read_farm_rsnblty_rev_fruit_veg_inventory(
    in in_reasonability_test_id farms.farm_rsn_rev_fruit_veg_invntrs.reasonability_test_result_id%type,
    in in_program_year farms.farm_line_items.program_year%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        select rrfvi.inventory_item_code,
               iic.description inventory_item_code_description,
               rrfvi.quantity_produced,
               rrfvi.expected_price,
               rrfvi.expected_revenue,
               fvtc.fruit_veg_type_code,
               fvtc.description fruit_veg_type_code_description,
               cuc.crop_unit_code,
               cuc.description crop_unit_code_description
        from farms.farm_rsn_rev_fruit_veg_invntrs rrfvi
        join farms.farm_inventory_item_codes iic on iic.inventory_item_code = rrfvi.inventory_item_code
        join farms.farm_inventory_item_details iid on iid.inventory_item_code = iic.inventory_item_code
        left outer join farms.farm_fruit_veg_type_codes fvtc on fvtc.fruit_veg_type_code = iid.fruit_veg_type_code
        left outer join farms.farm_crop_unit_codes cuc on cuc.crop_unit_code = rrfvi.crop_unit_code
        where rrfvi.reasonability_test_result_id = in_reasonability_test_id
        and iid.program_year = in_program_year
        order by fvtc.fruit_veg_type_code, iic.inventory_item_code;

    return cur;

end;
$$;
