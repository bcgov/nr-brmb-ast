create or replace procedure farms_reasonability_write_pkg.update_farm_rsnblty_rev_g_f_fs_incm_rslts(
    in in_reasonability_test_reslt_id farms.farm_rsn_rev_g_f_fs_incm_rslts.reasonability_test_result_id%type,
    in in_reported_revenue farms.farm_rsn_rev_g_f_fs_incm_rslts.reported_revenue%type,
    in in_expected_revenue farms.farm_rsn_rev_g_f_fs_incm_rslts.expected_revenue%type,
    in in_revenue_variance farms.farm_rsn_rev_g_f_fs_incm_rslts.revenue_variance%type,
    in in_revenue_wthn_limit_ind farms.farm_rsn_rev_g_f_fs_incm_rslts.revenue_within_limit_ind%type,
    in in_line_item farms.farm_rsn_rev_g_f_fs_incm_rslts.line_item%type,
    in in_user farms.farm_rsn_rev_g_f_fs_invntories.who_created%type
)
language plpgsql
as
$$
begin

    insert into farms.farm_rsn_rev_g_f_fs_incm_rslts (
        rsn_rev_g_f_fs_incm_rslt_id,
        reasonability_test_result_id,
        reported_revenue,
        expected_revenue,
        revenue_variance,
        revenue_within_limit_ind,
        line_item,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        nextval('farms.farm_rrgir_seq'),
        in_reasonability_test_reslt_id,
        in_reported_revenue,
        in_expected_revenue,
        in_revenue_variance,
        in_revenue_wthn_limit_ind,
        in_line_item,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );

end;
$$;
