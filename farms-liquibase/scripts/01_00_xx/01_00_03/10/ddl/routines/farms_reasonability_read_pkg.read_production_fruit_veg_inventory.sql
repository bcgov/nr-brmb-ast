create or replace function farms_reasonability_read_pkg.read_production_fruit_veg_inventory(
    in in_reasonability_test_id farms.farm_rsn_prdctn_frut_veg_rslts.reasonability_test_result_id%type,
    in in_program_year farms.farm_line_items.program_year%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        select rpfi.productive_capacity_amount,
               rpfi.expected_production_per_unit,
               rpfi.reported_quantity_produced,
               rpfi.expected_quantity_produced,
               rpfi.inventory_item_code,
               iic.description inventory_item_code_description,
               rpfi.qty_produced_crop_unit_code,
               fvtc.fruit_veg_type_code,
               fvtc.description fruit_veg_type_code_description
        from farms.farm_rsn_prdctn_frut_invntries rpfi
        join farms.farm_inventory_item_codes iic on iic.inventory_item_code = rpfi.inventory_item_code
        left outer join farms.farm_inventory_item_details iid on iid.inventory_item_code = rpfi.inventory_item_code
                                                              and iid.program_year = in_program_year
        left outer join farms.farm_fruit_veg_type_codes fvtc on fvtc.fruit_veg_type_code = iid.fruit_veg_type_code
        where rpfi.reasonability_test_result_id = in_reasonability_test_id
        order by rpfi.inventory_item_code;

    return cur;

end;
$$;
