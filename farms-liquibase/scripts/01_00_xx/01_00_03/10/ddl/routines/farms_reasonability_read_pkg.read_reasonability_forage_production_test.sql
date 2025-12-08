create or replace function farms_reasonability_read_pkg.read_reasonability_forage_production_test(
    in in_reasonability_test_id farms.farm_rsn_prdctn_forage_results.reasonability_test_result_id%type,
    in in_program_year farms.farm_inventory_item_details.program_year%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        select rpfr.productive_capacity_amount,
               rpfr.expected_production_per_unit,
               rpfr.reported_quantity_produced,
               rpfr.expected_quantity_produced,
               rpfr.quantity_produced_variance,
               rpfr.quantity_produced_wthn_lmt_ind,
               rpfr.inventory_item_code,
               iic.description inventory_item_code_description,
               iid.commodity_type_code,
               ctc.description commodity_type_code_description
        from farms.farm_rsn_prdctn_forage_results rpfr
        join farms.farm_inventory_item_codes iic on iic.inventory_item_code = rpfr.inventory_item_code
        join farms.farm_inventory_item_details iid on iid.program_year = in_program_year
                                                   and iid.inventory_item_code = rpfr.inventory_item_code
        join farms.farm_commodity_type_codes ctc on ctc.commodity_type_code = iid.commodity_type_code
        where rpfr.reasonability_test_result_id = in_reasonability_test_id
        order by (rpfr.inventory_item_code)::numeric;

    return cur;

end;
$$;
