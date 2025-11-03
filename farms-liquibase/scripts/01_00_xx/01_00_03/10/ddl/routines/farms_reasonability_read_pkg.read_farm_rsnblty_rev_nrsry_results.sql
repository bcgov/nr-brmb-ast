create or replace function farms_reasonability_read_pkg.read_farm_rsnblty_rev_nrsry_results(
    in_reasonability_test_reslt_id farms.farm_rsn_rev_nursery_results.reasonability_test_result_id%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        select rrnr.nursery_pass_ind,
               rrnr.revenue_variance,
               rrnr.revenue_variance_limit,
               rrnr.expected_revenue,
               rrnr.reported_revenue
        from farms.farm_rsn_rev_nursery_results rrnr
        where rrnr.reasonability_test_result_id = in_reasonability_test_reslt_id;

    return cur;

end;
$$;
