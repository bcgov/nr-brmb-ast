create or replace function farms_reasonability_read_pkg.read_farm_rsnblty_rev_fruit_veg_rslts(
    in in_reasonability_test_id farms.farm_rsn_rev_fruit_veg_results.reasonability_test_result_id%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        select rrfvr.quantity_produced,
               rrfvr.revenue_variance,
               rrfvr.revenue_variance_limit,
               rrfvr.reported_revenue,
               rrfvr.expected_price,
               rrfvr.expected_revenue,
               rrfvr.revenue_within_limit_ind,
               rrfvr.fruit_veg_type_code,
               rrfvr.crop_unit_code,
               fvtc.description
        from farms.farm_rsn_rev_fruit_veg_results rrfvr
        join farms.farm_fruit_veg_type_codes fvtc on fvtc.fruit_veg_type_code = rrfvr.fruit_veg_type_code
        where rrfvr.reasonability_test_result_id = in_reasonability_test_id
        order by rrfvr.fruit_veg_type_code;

    return cur;

end;
$$;
