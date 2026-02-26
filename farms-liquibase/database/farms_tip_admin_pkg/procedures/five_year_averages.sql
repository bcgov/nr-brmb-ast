create or replace procedure farms_tip_admin_pkg.five_year_averages(
   in in_year farms.farm_program_years.year%type,
   in in_user farms.farm_tip_report_results.who_updated%type
)
language plpgsql
as $$
begin

    merge into farms.farm_tip_report_results o
    using(
      select parent_trr.tip_report_result_id,
             count(rtrr.tip_report_result_id) reference_year_count,
             coalesce(round((avg(rtrr.allowable_income_per_100))::numeric, 2), 0) allowable_income_per_100_5year,
             coalesce(round((avg(rtrr.other_farm_income_per100))::numeric, 2), 0) other_farm_income_per100_5year,
             coalesce(round((avg(rtrr.allowable_expenses_per_100))::numeric, 2), 0) allowable_expens_per_100_5year,
             coalesce(round((avg(rtrr.non_allowable_expenses_per_100))::numeric, 2), 0) non_allowabl_expns_per_100_5yr,
             coalesce(round((avg(rtrr.production_margin_per_100))::numeric, 2), 0) production_margin_per_100_5yr,
             coalesce(round((avg(rtrr.net_margin_per_100))::numeric, 2), 0) net_margin_per_100_5yr,
             coalesce(round((avg(rtrr.total_expenses_per_100))::numeric, 2), 0) total_expenses_per_100_5year,
             coalesce(round((avg(rtrr.cmmdty_prchs_rpay_bnft_100))::numeric, 2), 0) cmmdty_prchs_rpay_bnft_100_5yr,
             coalesce(round((avg(rtrr.non_allowbl_repay_pgm_bnft_100))::numeric, 2), 0) non_allw_rpay_pgm_bnft_100_5yr,
             coalesce(round((avg(rtrr.production_margin_ratio))::numeric, 3), 0) production_margin_ratio_5year,
             coalesce(round((avg(rtrr.operating_cost_ratio))::numeric, 3), 0) operating_cost_ratio_5year,
             coalesce(round((avg(rtrr.direct_expenses_ratio))::numeric, 3), 0) direct_expenses_ratio_5year,
             coalesce(round((avg(rtrr.machinery_expenses_ratio))::numeric, 3), 0) machinery_expenses_ratio_5year,
             coalesce(round((avg(rtrr.land_build_expenses_ratio))::numeric, 3), 0) land_build_expens_ratio_5year,
             coalesce(round((avg(rtrr.overhead_expenses_ratio))::numeric, 3), 0) overhead_expenses_ratio_5year
      from farms.farm_tip_report_results parent_trr
      left outer join farms.farm_tip_report_results rtrr on rtrr.parent_tip_report_result_id = parent_trr.tip_report_result_id
      where parent_trr.year = in_year
        and coalesce(parent_trr.parent_tip_report_result_id::text, '') = ''
      group by parent_trr.tip_report_result_id
    ) n
    on (o.tip_report_result_id = n.tip_report_result_id)
    when matched then
      update set
       reference_year_count = n.reference_year_count,
       allowable_income_per_100_5year = n.allowable_income_per_100_5year,
       other_farm_income_per100_5year = n.other_farm_income_per100_5year,
       allowable_expens_per_100_5year = n.allowable_expens_per_100_5year,
       non_allowabl_expns_per_100_5yr = n.non_allowabl_expns_per_100_5yr,
       production_margin_per_100_5yr = n.production_margin_per_100_5yr,
       net_margin_per_100_5yr = n.net_margin_per_100_5yr,
       total_expenses_per_100_5year = n.total_expenses_per_100_5year,
       cmmdty_prchs_rpay_bnft_100_5yr = n.cmmdty_prchs_rpay_bnft_100_5yr,
       non_allw_rpay_pgm_bnft_100_5yr = n.non_allw_rpay_pgm_bnft_100_5yr,
       production_margin_ratio_5year = n.production_margin_ratio_5year,
       operating_cost_ratio_5year = n.operating_cost_ratio_5year,
       direct_expenses_ratio_5year = n.direct_expenses_ratio_5year,
       machinery_expenses_ratio_5year = n.machinery_expenses_ratio_5year,
       land_build_expens_ratio_5year = n.land_build_expens_ratio_5year,
       overhead_expenses_ratio_5year = n.overhead_expenses_ratio_5year,
       revision_count = revision_count + 1,
       who_updated = in_user,
       when_updated = current_timestamp;

end;
$$;
