create or replace function farms_reasonability_read_pkg.read_farm_rsnblty_rev_nrsry_incm_rslts(
    in in_reasonability_test_reslt_id farms.farm_rsn_rev_nursery_results.reasonability_test_result_id%type,
    in in_program_year farms.farm_inventory_item_details.program_year%type,
    in in_verified_date timestamp(0) default null
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        select rrni.line_item,
               rrni.reported_revenue,
               li.description
        from farms.farm_rsn_rev_nursery_incomes rrni
        join farms.farm_line_items li on li.line_item = rrni.line_item
                                      and in_program_year = li.program_year
                                      and li.expiry_date >= coalesce(in_verified_date, current_timestamp)
                                      and li.established_date < coalesce(in_verified_date, current_timestamp)
        where rrni.rsn_rev_nursery_result_id = (
            select rsn_rev_nursery_result_id
            from farms.farm_rsn_rev_nursery_results
            where reasonability_test_result_id = in_reasonability_test_reslt_id
        )
        order by rrni.line_item;

    return cur;

end;
$$;
