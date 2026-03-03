create or replace function farms_tip_user_pkg.read_tip_benchmark_summary_report(
   in in_program_year farms.farm_tip_benchmark_years.program_year%type,
   in in_farm_type_3_name farms.farm_tip_benchmark_years.farm_type_3_name%type,
   in in_farm_type_2_name farms.farm_tip_benchmark_years.farm_type_2_name%type,
   in in_farm_type_1_name farms.farm_tip_benchmark_years.farm_type_1_name%type,
   in in_income_range_low farms.farm_tip_benchmark_years.income_range_low%type
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
             tby.farming_operation_count,
             tby.allowable_income_per_100_5year,
             tby.allowable_income_per_100,
             tby.other_farm_income_per100_5year,
             tby.other_farm_income_per100,
             tby.cmmdty_prchs_rpay_per_100_5yr,
             tby.cmmdty_prchs_rpay_bnft_per_100,
             tby.allowable_expenses_per_100_5yr,
             tby.allowable_expenses_per_100,
             tby.production_margin_per_100_5yr,
             tby.production_margin_per_100,
             tby.non_allw_rpay_pgm_bnft_100_5yr,
             tby.non_allowbl_repay_pgm_bnft_100,
             tby.non_allowable_expn_per_100_5yr,
             tby.non_allowable_expenses_per_100,
             tby.total_expenses_per_100_5year,
             tby.total_expenses_per_100,
             tby.net_margin_per_100_5year,
             tby.net_margin_per_100,
             tby.generated_date
      from farms.farm_tip_benchmark_years tby
      where tby.reference_year = tby.program_year
        and tby.program_year = in_program_year
        and (coalesce(in_farm_type_3_name::text, '') = '' or tby.farm_type_3_name = in_farm_type_3_name)
        and (coalesce(in_farm_type_2_name::text, '') = '' or tby.farm_type_2_name = in_farm_type_2_name)
        and (coalesce(in_farm_type_1_name::text, '') = '' or tby.farm_type_1_name = in_farm_type_1_name)
        and (coalesce(in_income_range_low::text, '') = '' or tby.income_range_low = in_income_range_low) 
      order by tby.reference_year desc,
               tby.farm_type_1_name,
               tby.farm_type_2_name,
               tby.farm_type_3_name,
               tby.income_range_low;

    return cur;

end;
$$;
