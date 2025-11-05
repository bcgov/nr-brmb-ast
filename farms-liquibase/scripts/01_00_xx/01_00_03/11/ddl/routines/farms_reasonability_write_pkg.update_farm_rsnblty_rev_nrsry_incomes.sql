create or replace procedure farms_reasonability_write_pkg.update_farm_rsnblty_rev_nrsry_incomes(
    in in_reasonability_test_reslt_id farms.farm_rsn_rev_nursery_results.reasonability_test_result_id%type,
    in in_line_item farms.farm_rsn_rev_nursery_incomes.line_item%type,
    in in_reported_revenue farms.farm_rsn_rev_nursery_incomes.reported_revenue%type,
    in in_user farms.farm_rsn_rev_nursery_results.who_created%type
)
language plpgsql
as
$$
begin

    insert into farms.farm_rsn_rev_nursery_incomes (
        rsn_rev_nursery_income_id,
        rsn_rev_nursery_result_id,
        line_item,
        reported_revenue,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        nextval('farms.farm_rrnir_seq'),
        (
            select rsn_rev_nursery_result_id
            from farms.farm_rsn_rev_nursery_results
            where reasonability_test_result_id = in_reasonability_test_reslt_id
        ),
        in_line_item,
        in_reported_revenue,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );

end;
$$;
