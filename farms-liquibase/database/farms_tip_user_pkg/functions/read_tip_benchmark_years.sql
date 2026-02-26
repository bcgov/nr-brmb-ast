create or replace function farms_tip_user_pkg.read_tip_benchmark_years(
   in in_program_year farms.farm_tip_benchmark_years.program_year%type,
   in in_farm_type_3_name farms.farm_tip_benchmark_years.farm_type_3_name%type,
   in in_farm_type_2_name farms.farm_tip_benchmark_years.farm_type_2_name%type,
   in in_farm_type_1_name farms.farm_tip_benchmark_years.farm_type_1_name%type,
   in in_income_range_low farms.farm_tip_benchmark_years.income_range_low%type,
   in in_income_range_high farms.farm_tip_benchmark_years.income_range_high%type
)
returns refcursor
language plpgsql
as $$
declare

    cur refcursor;

begin

    open cur for

      select tby.program_year,
             tby.reference_year,
             tby.farm_type_3_name,
             tby.farm_type_2_name,
             tby.farm_type_1_name,
             tby.income_range_low,
             tby.income_range_high,
             tby.tip_benchmark_year_id,
             tby.minimum_group_count,
             tby.farming_operation_count,
             tby.reference_year_count,
             tby.total_income,
             tby.allowable_income,
             tby.allowable_expenses,
             tby.allowable_expenses_per_100,
             tby.allowable_expenses_per_100_5yr,
             tby.non_allowable_income,
             tby.non_allowable_expenses,
             tby.non_allowable_expenses_per_100,
             tby.non_allowable_expn_per_100_5yr,
             tby.allowable_income_per_100,
             tby.allowable_income_per_100_5year,
             tby.other_farm_income_per100,
             tby.other_farm_income_per100_5year,
             tby.total_expenses_per_100,
             tby.total_expenses_per_100_5year,
             tby.cmmdty_prchs_rpay_bnft_per_100,
             tby.cmmdty_prchs_rpay_per_100_5yr,
             tby.cmmdty_prchs_per100_high_25pct,
             tby.non_allowbl_repay_pgm_bnft_100,
             tby.non_allw_rpay_pgm_bnft_100_5yr,
             tby.non_allw_rpay_pgm_bnf_100_h25p,
             tby.net_margin,
             tby.net_margin_per_100,
             tby.net_margin_per_100_5year,
             tby.production_margin,
             tby.production_margin_per_100,
             tby.production_margin_per_100_5yr,
             tby.production_margin_ratio,
             tby.prodtn_margin_ratio_low_25pct,
             tby.operating_cost,
             tby.operating_cost_ratio,
             tby.operatng_cost_ratio_high_25pct,
             tby.direct_expenses,
             tby.direct_expenses_ratio,
             tby.direct_expens_ratio_high_25pct,
             tby.machinery_expenses,
             tby.machinery_expenses_ratio,
             tby.machnry_expns_ratio_high_25pct,
             tby.land_build_expenses,
             tby.land_build_expenses_ratio,
             tby.land_bld_expn_ratio_high_25pct,
             tby.overhead_expenses,
             tby.overhead_expenses_ratio,
             tby.overhead_expn_ratio_high_25pct,
             tby.generated_date
      from farms.farm_tip_benchmark_years tby
      where tby.program_year = in_program_year
        and (coalesce(in_farm_type_3_name::text, '') = '' or tby.farm_type_3_name = in_farm_type_3_name)
        and (coalesce(in_farm_type_2_name::text, '') = '' or tby.farm_type_2_name = in_farm_type_2_name)
        and (coalesce(in_farm_type_1_name::text, '') = '' or tby.farm_type_1_name = in_farm_type_1_name)
        and (coalesce(in_income_range_low::text, '') = '' or tby.income_range_low >= in_income_range_low)
        and (coalesce(in_income_range_high::text, '') = '' or tby.income_range_high <= in_income_range_high) 
      order by tby.reference_year desc,
               tby.farm_type_1_name,
               tby.farm_type_2_name,
               tby.farm_type_3_name,
               tby.income_range_low;

    return cur;

end;
$$;
