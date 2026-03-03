create or replace procedure farms_tip_admin_pkg.benchmark_years(
   in in_year farms.farm_program_years.year%type,
   in in_mode text,
   in in_user farms.farm_tip_report_results.who_updated%type
)
language plpgsql
as $$
begin

    merge into farms.farm_tip_benchmark_years o
    using(
    with
     yr as (
      select parent_trr.year program_year,
             parent_trr.farm_type_3_name,
             parent_trr.farm_type_2_name,
             parent_trr.farm_type_1_name,
             parent_trr.farm_type_3_threshold_met_ind,
             parent_trr.farm_type_2_threshold_met_ind,
             parent_trr.farm_type_1_threshold_met_ind,
             null parent_tip_benchmark_year_id_type_3,
             null parent_tip_benchmark_year_id_type_2,
             null parent_tip_benchmark_year_id_type_1,
             parent_trr.allowable_income,
             parent_trr.tip_report_result_id,
             parent_trr.year reference_year
      from farms.farm_tip_report_results parent_trr
      where coalesce(parent_trr.parent_tip_report_result_id::text, '') = ''
        and parent_trr.year = in_year
        and parent_trr.use_for_benchmarks_ind = 'Y'
        and in_mode = 'PROGRAM_YEAR'
      
union all

      select parent_trr.year program_year,
             parent_trr.farm_type_3_name,
             parent_trr.farm_type_2_name,
             parent_trr.farm_type_1_name,
             parent_trr.farm_type_3_threshold_met_ind,
             parent_trr.farm_type_2_threshold_met_ind,
             parent_trr.farm_type_1_threshold_met_ind,
             parent_trr.tip_benchmark_year_id_type_3 parent_tip_benchmark_year_id_type_3,
             parent_trr.tip_benchmark_year_id_type_2 parent_tip_benchmark_year_id_type_2,
             parent_trr.tip_benchmark_year_id_type_1 parent_tip_benchmark_year_id_type_1,
             parent_trr.allowable_income,
             rtrr.tip_report_result_id,
             rtrr.year reference_year
      from farms.farm_tip_report_results parent_trr
      join farms.farm_tip_report_results rtrr on rtrr.parent_tip_report_result_id = parent_trr.tip_report_result_id
      where coalesce(parent_trr.parent_tip_report_result_id::text, '') = ''
        and parent_trr.year = in_year
        and parent_trr.use_for_benchmarks_ind = 'Y'
        and in_mode = 'REFERENCE_YEAR' 
    ),
    g as (
      select yr.program_year,
             yr.reference_year,
             yr.farm_type_3_name farm_type_3_name_group,
             null farm_type_2_name_group,
             null farm_type_1_name_group,
             irv.range_low range_low_group,
             irv.range_high range_high_group,
             irv.minimum_group_count,
             parent_tip_benchmark_year_id_type_3 parent_tip_benchmark_year_id,
             trr.*
      from farms.farm_tip_income_ranges_vw irv
      join yr on irv.farm_type_3_name = yr.farm_type_3_name
              and yr.allowable_income between irv.range_low and irv.range_high
              and yr.farm_type_3_threshold_met_ind = 'Y'
      join farms.farm_tip_report_results trr on trr.tip_report_result_id = yr.tip_report_result_id
      where (irv.farm_type_3_name is not null and irv.farm_type_3_name::text <> '')
        and coalesce(irv.farm_type_2_name::text, '') = ''
        and coalesce(irv.farm_type_1_name::text, '') = '' 
      
union all

      select yr.program_year,
             yr.reference_year,
             null farm_type_3_name_group,
             yr.farm_type_2_name farm_type_2_name_group,
             null farm_type_1_name_group,
             irv.range_low range_low_group,
             irv.range_high range_high_group,
             irv.minimum_group_count,
             parent_tip_benchmark_year_id_type_2 parent_tip_benchmark_year_id,
             trr.*
      from farms.farm_tip_income_ranges_vw irv
      join yr on irv.farm_type_2_name = yr.farm_type_2_name
              and yr.allowable_income between irv.range_low and irv.range_high
              and yr.farm_type_2_threshold_met_ind = 'Y'
      join farms.farm_tip_report_results trr on trr.tip_report_result_id = yr.tip_report_result_id
      where (irv.farm_type_3_name is not null and irv.farm_type_3_name::text <> '')
        and (irv.farm_type_2_name is not null and irv.farm_type_2_name::text <> '')
        and coalesce(irv.farm_type_1_name::text, '') = '' 
      
union all

      select yr.program_year,
             yr.reference_year,
             null farm_type_3_name_group,
             null farm_type_2_name_group,
             yr.farm_type_1_name farm_type_1_name_group,
             irv.range_low range_low_group,
             irv.range_high range_high_group,
             irv.minimum_group_count,
             parent_tip_benchmark_year_id_type_1 parent_tip_benchmark_year_id,
             trr.*
      from farms.farm_tip_income_ranges_vw irv
      join yr on irv.farm_type_1_name = yr.farm_type_1_name
              and yr.allowable_income between irv.range_low and irv.range_high
              and yr.farm_type_1_threshold_met_ind = 'Y'
      join farms.farm_tip_report_results trr on trr.tip_report_result_id = yr.tip_report_result_id
      where (irv.farm_type_3_name is not null and irv.farm_type_3_name::text <> '')
        and (irv.farm_type_2_name is not null and irv.farm_type_2_name::text <> '')
        and (irv.farm_type_1_name is not null and irv.farm_type_1_name::text <> '') 
    )
    select program_year,
           reference_year,
           farm_type_3_name_group,
           farm_type_2_name_group,
           farm_type_1_name_group,
           range_low_group,
           range_high_group,
           minimum_group_count,
           count(*) farming_operation_count,
           round((median(total_income))::numeric, 2) total_income,
           round((median(allowable_income))::numeric, 2) allowable_income,
           round((median(allowable_expenses))::numeric, 2) allowable_expenses,
           round((median(non_allowable_income))::numeric, 2) non_allowable_income,
           round((median(non_allowable_expenses))::numeric, 2) non_allowable_expenses,
           round((median(allowable_income_per_100))::numeric, 2) allowable_income_per_100,
           round((median(other_farm_income_per100))::numeric, 2) other_farm_income_per100,
           round((median(allowable_expenses_per_100))::numeric, 2) allowable_expenses_per_100,
           round((median(non_allowable_expenses_per_100))::numeric, 2) non_allowable_expenses_per_100,
           round((median(production_margin_per_100))::numeric, 2) production_margin_per_100,
           round((median(total_expenses_per_100))::numeric, 2) total_expenses_per_100,
           round((median(cmmdty_prchs_rpay_bnft_100))::numeric, 2) cmmdty_prchs_rpay_bnft_per_100,
           round((median(non_allowbl_repay_pgm_bnft_100))::numeric, 2) non_allowbl_repay_pgm_bnft_100,
           round((median(net_margin))::numeric, 2) net_margin,
           round((median(net_margin_per_100))::numeric, 2) net_margin_per_100,
           round((median(production_margin))::numeric, 2) production_margin,
           round((median(production_margin_ratio))::numeric, 4) production_margin_ratio,
           round((median(operating_cost))::numeric, 2) operating_cost,
           round((median(operating_cost_ratio))::numeric, 4) operating_cost_ratio,
           round((median(direct_expenses))::numeric, 2) direct_expenses,
           round((median(direct_expenses_ratio))::numeric, 4) direct_expenses_ratio,
           round((median(machinery_expenses))::numeric, 2) machinery_expenses,
           round((median(machinery_expenses_ratio))::numeric, 4) machinery_expenses_ratio,
           round((median(land_build_expenses))::numeric, 2) land_build_expenses,
           round((median(land_build_expenses_ratio))::numeric, 4) land_build_expenses_ratio,
           round((median(overhead_expenses))::numeric, 2) overhead_expenses,
           round((median(overhead_expenses_ratio))::numeric, 4) overhead_expenses_ratio,
           percentile_cont(0.25) within group(order by production_margin_ratio) prodtn_margin_ratio_low_25pct, -- ascending sort to get the low 25th
           percentile_cont(0.25) within group(order by operating_cost_ratio desc) operatng_cost_ratio_high_25pct,
           percentile_cont(0.25) within group(order by direct_expenses_ratio desc) direct_expens_ratio_high_25pct,
           percentile_cont(0.25) within group(order by machinery_expenses_ratio desc) machnry_expns_ratio_high_25pct,
           percentile_cont(0.25) within group(order by land_build_expenses_ratio desc) land_bld_expn_ratio_high_25pct,
           percentile_cont(0.25) within group(order by overhead_expenses_ratio desc) overhead_expn_ratio_high_25pct,
           percentile_cont(0.25) within group(order by cmmdty_prchs_rpay_bnft_100 desc) cmmdty_prchs_per100_high_25pct,
           percentile_cont(0.25) within group(order by non_allowbl_repay_pgm_bnft_100 desc) non_allw_rpay_pgm_bnf_100_h25p,
           case when program_year = reference_year then 'n' else 'Y' end reference_year_ind,
           max(parent_tip_benchmark_year_id) parent_tip_benchmark_year_id
    from g
    group by program_year,
             reference_year,
             farm_type_3_name_group,
             farm_type_2_name_group,
             farm_type_1_name_group,
             range_low_group,
             range_high_group,
             minimum_group_count
    order by farm_type_3_name_group,
             farm_type_2_name_group,
             farm_type_1_name_group,
             range_low_group,
             range_high_group
    ) n
    on (    ( (coalesce(o.farm_type_3_name::text, '') = '' and coalesce(n.farm_type_3_name_group::text, '') = '') or o.farm_type_3_name = n.farm_type_3_name_group)
        and ( (coalesce(o.farm_type_2_name::text, '') = '' and coalesce(n.farm_type_2_name_group::text, '') = '') or o.farm_type_2_name = n.farm_type_2_name_group)
        and ( (coalesce(o.farm_type_1_name::text, '') = '' and coalesce(n.farm_type_1_name_group::text, '') = '') or o.farm_type_1_name = n.farm_type_1_name_group)
        and o.program_year = n.program_year
        and o.reference_year = n.reference_year
        and o.income_range_low = n.range_low_group
        and o.income_range_high = n.range_high_group
       )
    when not matched then
      insert(tip_benchmark_year_id,
       program_year,
       reference_year,
       farm_type_3_name,
       farm_type_2_name,
       farm_type_1_name,
       income_range_low,
       income_range_high,
       minimum_group_count,
       farming_operation_count,
       total_income,
       allowable_income,
       allowable_expenses,
       non_allowable_income,
       non_allowable_expenses,
       allowable_income_per_100,
       other_farm_income_per100,
       allowable_expenses_per_100,
       non_allowable_expenses_per_100,
       production_margin_per_100,
       total_expenses_per_100,
       cmmdty_prchs_rpay_bnft_per_100,
       non_allowbl_repay_pgm_bnft_100,
       net_margin,
       net_margin_per_100,
       production_margin,
       production_margin_ratio,
       operating_cost,
       operating_cost_ratio,
       direct_expenses,
       direct_expenses_ratio,
       machinery_expenses,
       machinery_expenses_ratio,
       land_build_expenses,
       land_build_expenses_ratio,
       overhead_expenses,
       overhead_expenses_ratio,
       cmmdty_prchs_per100_high_25pct,
       non_allw_rpay_pgm_bnf_100_h25p,
       prodtn_margin_ratio_low_25pct,
       operatng_cost_ratio_high_25pct,
       direct_expens_ratio_high_25pct,
       machnry_expns_ratio_high_25pct,
       land_bld_expn_ratio_high_25pct,
       overhead_expn_ratio_high_25pct,
       reference_year_ind,
       parent_tip_benchmark_year_id,
       generated_date,
       revision_count,
       who_created,
       when_created,
       who_updated,
       when_updated)
      values (nextval('farms.farm_tby_seq'),
       n.program_year,
       n.reference_year,
       n.farm_type_3_name_group,
       n.farm_type_2_name_group,
       n.farm_type_1_name_group,
       n.range_low_group,
       n.range_high_group,
       n.minimum_group_count,
       n.farming_operation_count,
       n.total_income,
       n.allowable_income,
       n.allowable_expenses,
       n.non_allowable_income,
       n.non_allowable_expenses,
       n.allowable_income_per_100,
       n.other_farm_income_per100,
       n.allowable_expenses_per_100,
       n.non_allowable_expenses_per_100,
       n.production_margin_per_100,
       n.total_expenses_per_100,
       n.cmmdty_prchs_rpay_bnft_per_100,
       n.non_allowbl_repay_pgm_bnft_100,
       n.net_margin,
       n.net_margin_per_100,
       n.production_margin,
       n.production_margin_ratio,
       n.operating_cost,
       n.operating_cost_ratio,
       n.direct_expenses,
       n.direct_expenses_ratio,
       n.machinery_expenses,
       n.machinery_expenses_ratio,
       n.land_build_expenses,
       n.land_build_expenses_ratio,
       n.overhead_expenses,
       n.overhead_expenses_ratio,
       n.cmmdty_prchs_per100_high_25pct,
       n.non_allw_rpay_pgm_bnf_100_h25p,
       n.prodtn_margin_ratio_low_25pct,
       n.operatng_cost_ratio_high_25pct,
       n.direct_expens_ratio_high_25pct,
       n.machnry_expns_ratio_high_25pct,
       n.land_bld_expn_ratio_high_25pct,
       n.overhead_expn_ratio_high_25pct,
       n.reference_year_ind,
       n.parent_tip_benchmark_year_id,
       current_timestamp,
       1,
       in_user,
       current_timestamp,
       in_user,
       current_timestamp);

end;
$$;
