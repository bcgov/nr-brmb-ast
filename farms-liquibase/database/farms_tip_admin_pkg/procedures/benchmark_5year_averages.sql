create or replace procedure farms_tip_admin_pkg.benchmark_5year_averages(
   in in_year farms.farm_program_years.year%type,
   in in_user farms.farm_tip_benchmark_years.who_updated%type
)
language plpgsql
as $$
begin

    merge into farms.farm_tip_benchmark_years o
    using(
      select tby.tip_benchmark_year_id,
             count(rtby.tip_benchmark_year_id) reference_year_count,
             coalesce(round((avg(rtby.allowable_income_per_100))::numeric, 2), 0) allowable_income_per_100_5year,
             coalesce(round((avg(rtby.other_farm_income_per100))::numeric, 2), 0) other_farm_income_per100_5year,
             coalesce(round((avg(rtby.allowable_expenses_per_100))::numeric, 2), 0) allowable_expenses_per_100_5yr,
             coalesce(round((avg(rtby.non_allowable_expenses_per_100))::numeric, 2), 0) non_allowable_expn_per_100_5yr,
             coalesce(round((avg(rtby.total_expenses_per_100))::numeric, 2), 0) total_expenses_per_100_5year,
             coalesce(round((avg(rtby.cmmdty_prchs_rpay_bnft_per_100))::numeric, 2), 0) cmmdty_prchs_rpay_per_100_5yr,
             coalesce(round((avg(rtby.non_allowbl_repay_pgm_bnft_100))::numeric, 2), 0) non_allw_rpay_pgm_bnft_100_5yr,
             coalesce(round((avg(rtby.net_margin_per_100))::numeric, 2), 0) net_margin_per_100_5year,
             coalesce(round((avg(rtby.production_margin_per_100))::numeric, 2), 0) production_margin_per_100_5yr
      from farms.farm_tip_benchmark_years tby
      left outer join farms.farm_tip_benchmark_years rtby on rtby.parent_tip_benchmark_year_id = tby.tip_benchmark_year_id
      where tby.program_year = tby.reference_year
        and tby.program_year = in_year
      group by tby.tip_benchmark_year_id
    ) n
    on (o.tip_benchmark_year_id = n.tip_benchmark_year_id)
    when matched then
      update set
       reference_year_count           = n.reference_year_count,
       allowable_income_per_100_5year = n.allowable_income_per_100_5year,
       other_farm_income_per100_5year = n.other_farm_income_per100_5year,
       allowable_expenses_per_100_5yr = n.allowable_expenses_per_100_5yr,
       non_allowable_expn_per_100_5yr = n.non_allowable_expn_per_100_5yr,
       total_expenses_per_100_5year = n.total_expenses_per_100_5year,
       cmmdty_prchs_rpay_per_100_5yr = n.cmmdty_prchs_rpay_per_100_5yr,
       non_allw_rpay_pgm_bnft_100_5yr = n.non_allw_rpay_pgm_bnft_100_5yr,
       net_margin_per_100_5year = n.net_margin_per_100_5year,
       production_margin_per_100_5yr = n.production_margin_per_100_5yr,
       revision_count = revision_count + 1,
       who_updated = in_user,
       when_updated = current_timestamp;

end;
$$;
