create or replace function farms_reasonability_read_pkg.read_farm_rsnblty_rev_nrsry_invn_rslts(
    in in_reasonability_test_reslt_id farms.farm_rsn_rev_nursery_results.reasonability_test_result_id%type,
    in in_program_year farms.farm_inventory_item_details.program_year%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        select rrni.quantity_produced,
               rrni.quantity_start,
               rrni.quantity_end,
               rrni.quantity_sold,
               rrni.expected_revenue,
               rrni.fmv_price,
               rrni.inventory_item_code,
               iic.description inventory_item_desc
        from farms.farm_rsn_rev_nursery_invntries rrni
        join farms.farm_inventory_item_codes iic on iic.inventory_item_code = rrni.inventory_item_code
        join farms.farm_inventory_item_details iid on iid.program_year = in_program_year
                                                   and iid.inventory_item_code = rrni.inventory_item_code
        where rrni.rsn_rev_nursery_result_id = (
            select rsn_rev_nursery_result_id
            from farms.farm_rsn_rev_nursery_results
            where reasonability_test_result_id = in_reasonability_test_reslt_id
        )
        order by (rrni.inventory_item_code)::numeric;

    return cur;

end;
$$;
