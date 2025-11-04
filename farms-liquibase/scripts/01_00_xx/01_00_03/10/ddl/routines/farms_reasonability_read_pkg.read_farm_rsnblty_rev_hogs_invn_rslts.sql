create or replace function farms_reasonability_read_pkg.read_farm_rsnblty_rev_hogs_invn_rslts(
    in in_reasonability_test_reslt_id farms.farm_reasonabilty_test_results.reasonability_test_result_id%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        select rrhi.quantity_start,
               rrhi.quantity_end,
               rrhi.fmv_price,
               rrhi.inventory_item_code,
               iic.description inventory_item_desc
        from farms.farm_rsn_rev_hog_results rrhr
        join farms.farm_rsn_rev_hog_inventories rrhi on rrhi.rsn_rev_hog_result_id = rrhr.rsn_rev_hog_result_id
        join farms.farm_inventory_item_codes iic on iic.inventory_item_code = rrhi.inventory_item_code
        where rrhr.reasonability_test_result_id = in_reasonability_test_reslt_id
        order by (iic.inventory_item_code)::numeric;

    return cur;

end;
$$;
