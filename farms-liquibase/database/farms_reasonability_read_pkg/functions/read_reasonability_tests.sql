create or replace function farms_reasonability_read_pkg.read_reasonability_tests(
    in in_agristability_scenario_id farms.farm_reasonabilty_test_results.agristability_scenario_id%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        select rtr.reasonability_test_result_id,
               rtr.fresh_ind,
               rtr.generated_date,
               rtr.forage_consumer_capacity,

               rtr.benefit_risk_pass_ind,
               rtr.program_year_margin,
               rtr.total_benefit,
               rtr.benchmark_margin,
               rtr.benchmark_margin_deductible,
               rtr.benchmark_margin_less_deductbl,
               rtr.benchmark_margin_less_py_marg,
               rtr.benchmark_margin_payout_level,
               rtr.benefit_benchmark_befor_combnd,
               rtr.combined_benefit_percent,
               rtr.benefit_benchmark,
               rtr.benefit_risk_variance,
               rtr.benefit_risk_variance_limit,

               rtr.margin_pass_ind,
               rtr.adjusted_reference_margin,
               rtr.adj_reference_margin_variance,
               rtr.adj_reference_margin_vrinc_lmt,
               rtr.within_lmt_prct_ref_margin_ind,
               rtr.margin_industry_average,
               rtr.margin_industry_avg_variance,
               rtr.margin_industry_average_limit,
               rtr.margin_wthn_lmt_indtry_avg_ind,

               rtr.structural_change_pass_ind,
               rtr.production_marg_accr_adjs,
               rtr.ratio_adj_reference_margin,
               rtr.additive_adj_reference_margin,
               rtr.ratio_additive_ref_marg_diff,
               rtr.ratio_add_diff_dollar_limit,
               rtr.within_dlr_limit_ratio_add_ind,
               rtr.additive_division_ratio,
               rtr.additive_division_ratio_limit,
               rtr.within_limit_addtiv_divisn_ind,
               rtr.structual_change_method_to_use,
               rtr.farm_size_ratio_limit,
               rtr.farm_size_rtios_within_lmt_ind,

               rtr.expense_indtry_av_cmp_pass_ind,
               rtr.expense_accr_adjs_indtry_av_cm,
               rtr.expense_industry_average,
               rtr.expense_industry_avg_variance,
               rtr.expense_industry_average_limit,

               rtr.production_pass_ind,
               rtr.production_forage_fs_pass_ind,
               rtr.production_fruit_veg_pass_ind,
               rtr.prod_forage_qty_produced_limit,
               rtr.prod_fruit_veg_qty_prodcd_limt,
               rtr.prod_grain_qty_produced_limit,

               rtr.expense_ref_year_comp_pass_ind,
               rtr.expense_accr_adjs,
               rtr.expenses_aft_str_ch_yr_minus_1,
               rtr.expenses_aft_str_ch_yr_minus_2,
               rtr.expenses_aft_str_ch_yr_minus_3,
               rtr.expenses_aft_str_ch_yr_minus_4,
               rtr.expenses_aft_str_ch_yr_minus_5,
               rtr.expenses_aft_str_ch_ref_yr_avg,
               rtr.expenses_aft_str_ch_variance,
               rtr.expenses_aft_str_ch_varian_lmt,

               rtr.revenue_risk_pass_ind,
               rtr.revenue_rsk_grain_frg_pass_ind,
               rtr.revenue_rsk_grain_forage_limit,
               rtr.revenue_risk_fruit_vg_pass_ind

        from farms.farm_reasonabilty_test_results rtr
        where rtr.agristability_scenario_id = in_agristability_scenario_id;

    return cur;

end;
$$;
