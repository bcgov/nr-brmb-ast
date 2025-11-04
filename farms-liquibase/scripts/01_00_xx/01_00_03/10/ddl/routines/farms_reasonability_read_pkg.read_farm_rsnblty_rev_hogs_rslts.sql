create or replace function farms_reasonability_read_pkg.read_farm_rsnblty_rev_hogs_rslts(
    in in_reasonability_test_reslt_id farms.farm_rsn_rev_hog_results.reasonability_test_result_id%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        select rrhr.has_hogs_ind,
               rrhr.hogs_pass_ind,
               rrhr.farrow_to_finish_operation_ind,
               rrhr.feeder_operation_ind,
               rrhr.reported_expenses,
               rrhr.total_quantity_start,
               rrhr.total_quantity_end,
               rrhr.farrow_sows_breeding,
               rrhr.farrow_births_per_cycle,
               rrhr.farrow_birth_cycles_per_year,
               rrhr.farrow_total_births_per_cycle,
               rrhr.farrow_births_all_cycles,
               rrhr.farrow_death_rate,
               rrhr.farrow_deaths,
               rrhr.farrow_boar_purchase_count,
               rrhr.farrow_boar_purchase_price,
               rrhr.farrow_boar_purchase_expense,
               rrhr.farrow_sow_purchase_expense,
               rrhr.farrow_sow_purchase_count,
               rrhr.farrow_sow_purchase_price,
               rrhr.feeder_productive_units,
               rrhr.feeder_weanling_purchase_expns,
               rrhr.feeder_weanling_purchase_price,
               rrhr.feeder_weanling_purchase_count,
               rrhr.total_purchase_count,
               rrhr.expected_sold,
               rrhr.heaviest_hog_price,
               rrhr.reported_revenue,
               rrhr.expected_revenue,
               rrhr.revenue_variance,
               rrhr.revenue_variance_limit
        from farms.farm_rsn_rev_hog_results rrhr
        where rrhr.reasonability_test_result_id = in_reasonability_test_reslt_id;

    return cur;

end;
$$;
