create or replace function farms_reasonability_read_pkg.read_farm_rsnblty_rev_pltry_egg_rslts(
    in in_reasonability_test_reslt_id farms.farm_rsn_rev_poultry_egg_rslts.reasonability_test_result_id%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        select rrper.poultry_eggs_pass_ind,
               rrper.consumption_pass_ind,
               rrper.has_poultry_eggs_ind,
               rrper.consumption_layers,
               rrper.consumption_avg_eggs_per_layer,
               rrper.consumption_eggs_total,
               rrper.consumption_eggs_dozen_sold,
               rrper.consumption_price_per_dozen,
               rrper.consumption_expected_revenue,
               rrper.consumption_reported_revenue,
               rrper.consumption_revenue_variance,
               rrper.consumption_revenue_varinc_lmt,
               rrper.hatching_pass_ind,
               rrper.hatching_layers,
               rrper.hatching_avg_eggs_per_layer,
               rrper.hatching_eggs_total,
               rrper.hatching_eggs_dozen_sold,
               rrper.hatching_price_per_dozen,
               rrper.hatching_expected_revenue,
               rrper.hatching_reported_revenue,
               rrper.hatching_revenue_variance,
               rrper.hatching_revenue_variance_limt
        from farms.farm_rsn_rev_poultry_egg_rslts rrper
        where rrper.reasonability_test_result_id = in_reasonability_test_reslt_id;

    return cur;

end;
$$;
