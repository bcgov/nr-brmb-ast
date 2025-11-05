create or replace procedure farms_reasonability_write_pkg.update_farm_rsnblty_rev_nrsry_results(
    in in_reasonability_test_reslt_id farms.farm_rsn_rev_nursery_results.reasonability_test_result_id%type,
    in in_nursery_pass_ind farms.farm_rsn_rev_nursery_results.nursery_pass_ind%type,
    in in_reported_revenue farms.farm_rsn_rev_nursery_results.reported_revenue%type,
    in in_expected_revenue farms.farm_rsn_rev_nursery_results.expected_revenue%type,
    in in_revenue_variance farms.farm_rsn_rev_nursery_results.revenue_variance%type,
    in in_revenue_variance_limit farms.farm_rsn_rev_nursery_results.revenue_variance_limit%type,
    in in_user farms.farm_rsn_rev_nursery_results.who_created%type
)
language plpgsql
as
$$
declare

    v_rowcount bigint;

begin
    select count(*)
    into v_rowcount
    from farms.farm_rsn_rev_nursery_results
    where reasonability_test_result_id = in_reasonability_test_reslt_id;

    if v_rowcount = 0 then

        insert into farms.farm_rsn_rev_nursery_results (
            rsn_rev_nursery_result_id,
            reasonability_test_result_id,
            nursery_pass_ind,
            reported_revenue,
            expected_revenue,
            revenue_variance,
            revenue_variance_limit,
            revision_count,
            who_created,
            when_created,
            who_updated,
            when_updated
        ) values (
            nextval('farms.farm_rrnr_seq'),
            in_reasonability_test_reslt_id,
            in_nursery_pass_ind,
            in_reported_revenue,
            in_expected_revenue,
            in_revenue_variance,
            in_revenue_variance_limit,
            1,
            in_user,
            current_timestamp,
            in_user,
            current_timestamp
        );

    else
        update farms.farm_rsn_rev_nursery_results
        set nursery_pass_ind = in_nursery_pass_ind,
            reported_revenue = in_reported_revenue,
            expected_revenue = in_expected_revenue,
            revenue_variance = in_revenue_variance,
            revenue_variance_limit = in_revenue_variance_limit,
            revision_count = revision_count + 1,
            who_updated = in_user,
            when_updated = current_timestamp
        where reasonability_test_result_id = in_reasonability_test_reslt_id;

    end if;

end;
$$;
