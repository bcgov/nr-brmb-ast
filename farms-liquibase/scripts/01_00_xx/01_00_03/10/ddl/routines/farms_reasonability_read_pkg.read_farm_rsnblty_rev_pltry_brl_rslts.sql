create or replace function farms_reasonability_read_pkg.read_farm_rsnblty_rev_pltry_brl_rslts(
    in in_reasonability_test_reslt_id farms.farm_rsn_rev_poultry_brl_rslts.reasonability_test_result_id%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        select rrpbr.has_poultry_broilers_ind,
               rrpbr.poultry_broiler_pass_ind,
               rrpbr.has_chickens_ind,
               rrpbr.chicken_pass_ind,
               rrpbr.chicken_avg_weight_kg,
               rrpbr.chicken_expected_birds_sold,
               rrpbr.chicken_price_per_bird,
               rrpbr.chicken_expected_revenue,
               rrpbr.chicken_reported_revenue,
               rrpbr.chicken_kg_produced,
               rrpbr.chicken_revenue_variance,
               rrpbr.chicken_revenue_variance_limit,
               rrpbr.has_turkeys_ind,
               rrpbr.turkey_pass_ind,
               rrpbr.turkey_avg_weight_kg,
               rrpbr.turkey_expected_birds_sold,
               rrpbr.turkey_price_per_bird,
               rrpbr.turkey_expected_revenue,
               rrpbr.turkey_reported_revenue,
               rrpbr.turkey_kg_produced,
               rrpbr.turkey_revenue_variance,
               rrpbr.turkey_revenue_variance_limit
        from farms.farm_rsn_rev_poultry_brl_rslts rrpbr
        where rrpbr.reasonability_test_result_id = in_reasonability_test_reslt_id;

    return cur;

end;
$$;
