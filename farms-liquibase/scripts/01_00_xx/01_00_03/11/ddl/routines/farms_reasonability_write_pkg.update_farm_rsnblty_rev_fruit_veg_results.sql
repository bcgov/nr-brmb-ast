create or replace procedure farms_reasonability_write_pkg.update_farm_rsnblty_rev_fruit_veg_results(
    in in_reasonability_test_reslt_id farms.farm_rsn_rev_fruit_veg_results.reasonability_test_result_id%type,
    in in_reported_revenue farms.farm_rsn_rev_fruit_veg_results.reported_revenue%type,
    in in_quantity_produced farms.farm_rsn_rev_fruit_veg_results.quantity_produced%type,
    in in_expected_price farms.farm_rsn_rev_fruit_veg_results.expected_price%type,
    in in_expected_revenue farms.farm_rsn_rev_fruit_veg_results.expected_revenue%type,
    in in_revenue_variance farms.farm_rsn_rev_fruit_veg_results.revenue_variance%type,
    in in_revenue_wthn_limit_ind farms.farm_rsn_rev_fruit_veg_results.revenue_within_limit_ind%type,
    in in_revenue_variance_limit farms.farm_rsn_rev_fruit_veg_results.revenue_variance_limit%type,
    in in_fruit_veg_type_code farms.farm_rsn_rev_fruit_veg_results.fruit_veg_type_code%type,
    in in_crop_unit_code farms.farm_rsn_rev_fruit_veg_results.crop_unit_code%type,
    in in_user farms.farm_rsn_rev_fruit_veg_results.who_created%type
)
language plpgsql
as
$$
begin

    insert into farms.farm_rsn_rev_fruit_veg_results (
        rsn_rev_fruit_veg_result_id,
        reasonability_test_result_id,
        quantity_produced,
        reported_revenue,
        expected_price,
        expected_revenue,
        revenue_variance,
        revenue_within_limit_ind,
        revenue_variance_limit,
        fruit_veg_type_code,
        crop_unit_code,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        nextval('farms.farm_rrfvr_seq'),
        in_reasonability_test_reslt_id,
        in_quantity_produced,
        in_reported_revenue,
        in_expected_price,
        in_expected_revenue,
        in_revenue_variance,
        in_revenue_wthn_limit_ind,
        in_revenue_variance_limit,
        in_fruit_veg_type_code,
        in_crop_unit_code,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );

end;
$$;
