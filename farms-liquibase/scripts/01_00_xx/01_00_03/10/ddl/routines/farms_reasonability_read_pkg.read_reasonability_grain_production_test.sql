create or replace function farms_reasonability_read_pkg.read_reasonability_grain_production_test(
    in in_reasonability_test_id farms.farm_rsn_prdctn_grain_results.reasonability_test_result_id%type,
    in in_program_year farms.farm_inventory_item_details.program_year%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        select rpgr.productive_capacity_amount,
               rpgr.expected_production_per_unit,
               rpgr.reported_quantity_produced,
               rpgr.expected_quantity_produced,
               rpgr.quantity_produced_variance,
               rpgr.quantity_produced_wthn_lmt_ind,
               rpgr.inventory_item_code,
               iic.description,
               iid.commodity_type_code
        from farms.farm_rsn_prdctn_grain_results rpgr
        join farms.farm_inventory_item_codes iic on iic.inventory_item_code = rpgr.inventory_item_code
        join farms.farm_inventory_item_details iid on iid.program_year = in_program_year
                                                   and iid.inventory_item_code = rpgr.inventory_item_code
        where rpgr.reasonability_test_result_id = in_reasonability_test_id
        order by (rpgr.inventory_item_code)::numeric;

    return cur;

end;
$$;
