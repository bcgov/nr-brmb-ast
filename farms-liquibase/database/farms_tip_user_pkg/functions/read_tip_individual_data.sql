create or replace function farms_tip_user_pkg.read_tip_individual_data(
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

      select ac.participant_pin,
             coalesce(psn.corp_name, psn.last_name||', '||psn.first_name) client_name,
             trr.tip_report_result_id,
             trr.reference_year_ind is_reference_year,
             trr.parent_tip_report_result_id parent_id,
             trr.year,
             trr.alignment_key,
             trr.operation_number,
             trr.partnership_pin,
             trr.partnership_name,
             trr.partnership_percent,
             trr.farm_type_3_name,
             trr.farm_type_2_name,
             trr.farm_type_1_name,
             trr.farm_type_3_percent,
             trr.farm_type_2_percent,
             trr.farm_type_1_percent,
             trr.farm_type_3_threshold_met_ind,
             trr.farm_type_2_threshold_met_ind,
             trr.farm_type_1_threshold_met_ind,
             trr.farm_type_level,
             trr.income_range_low,
             trr.income_range_high,
             trr.group_farming_operation_count,
             trr.tip_benchmark_year_id_type_3 type_3_tip_benchmark_year_id,
             trr.tip_benchmark_year_id_type_2 type_2_tip_benchmark_year_id,
             trr.tip_benchmark_year_id_type_1 type_1_tip_benchmark_year_id,
             tby3.income_range_low type_3_income_range_low,
             tby3.income_range_high type_3_income_range_high,
             tby2.income_range_low type_2_income_range_low,
             tby2.income_range_high type_2_income_range_high,
             tby1.income_range_low type_1_income_range_low,
             tby1.income_range_high type_1_income_range_high,
             tby3.minimum_group_count type_3_minimum_group_count,
             tby2.minimum_group_count type_2_minimum_group_count,
             tby1.minimum_group_count type_1_minimum_group_count,
             trr.reference_year_count,
             trr.total_income,
             trr.total_expenses,
             trr.allowable_income,
             trr.allowable_expenses,
             trr.non_allowable_income,
             trr.non_allowable_expenses,
             trr.commodity_income,
             trr.commodity_purchases,
             trr.allowable_repaymnt_prgm_benfts,
             trr.non_allowable_repay_prgm_bnfts,
             trr.commdity_purchases_repay_bnfts,
             trr.total_income_benchmark,
             trr.allowable_income_per_100,
             trr.allowable_income_per_100_5year,
             trr.allowable_income_per_100_bench,
             trr.other_farm_income_per100,
             trr.other_farm_income_per100_5year,
             trr.other_farm_income_per100_bench,
             trr.allowable_expenses_per_100,
             trr.allowable_expens_per_100_5year,
             trr.allowable_expens_per_100_bench,
             trr.non_allowable_expenses_per_100,
             trr.non_allowabl_expns_per_100_5yr,
             trr.non_allowabl_expns_per_100_bch,
             trr.production_margin_per_100,
             trr.production_margin_per_100_5yr,
             trr.production_margin_per_100_bch,
             trr.total_expenses_per_100,
             trr.total_expenses_per_100_5year,
             trr.total_expenses_per_100_bench,
             trr.cmmdty_prchs_rpay_bnft_100,
             trr.cmmdty_prchs_rpay_bnft_100_5yr,
             trr.cmmdty_prchs_rpay_bnft_100_bch,
             trr.cmmdty_prchs_per100_high_25pct,
             trr.non_allowbl_repay_pgm_bnft_100,
             trr.non_allw_rpay_pgm_bnft_100_5yr,
             trr.non_allw_rpay_pgm_bnft_100_bch,
             trr.non_allw_rpay_pgm_bnf_100_h25p,
             trr.production_margin,
             trr.production_margin_ratio,
             trr.production_margin_ratio_5year,
             trr.production_margin_ratio_bench,
             trr.prodtn_margin_ratio_low_25pct,
             trr.net_margin,
             trr.net_margin_per_100,
             trr.net_margin_per_100_5yr,
             trr.net_margin_per_100_bench,
             trr.operating_cost,
             trr.operating_cost_ratio,
             trr.operating_cost_ratio_5year,
             trr.operating_cost_ratio_bench,
             trr.operatng_cost_ratio_high_25pct,
             trr.direct_expenses,
             trr.direct_expenses_ratio,
             trr.direct_expenses_ratio_5year,
             trr.direct_expenses_ratio_bench,
             trr.direct_expens_ratio_high_25pct,
             trr.machinery_expenses,
             trr.machinery_expenses_ratio,
             trr.machinery_expenses_ratio_5year,
             trr.machinery_expenses_ratio_bench,
             trr.machnry_expns_ratio_high_25pct,
             trr.land_build_expenses,
             trr.land_build_expenses_ratio,
             trr.land_build_expens_ratio_5year,
             trr.land_build_expens_ratio_bench,
             trr.land_bld_expn_ratio_high_25pct,
             trr.overhead_expenses,
             trr.overhead_expenses_ratio,
             trr.overhead_expenses_ratio_5year,
             trr.overhead_expenses_ratio_bench,
             trr.overhead_expn_ratio_high_25pct,
             trr.use_for_benchmarks_ind,
             cmmdtyr.description cmmdty_prchs_rpay_b_100_indctr,
             pmrr.description production_margin_ratio_indctr,
             ocrr.description operating_cost_ratio_indicator,
             derr.description direct_expenses_ratio_indicatr,
             merr.description machinery_expenses_ratio_ratng,
             lberr.description land_build_expens_ratio_indctr,
             oerr.description overhead_expenses_ratio_indctr,
             trr.generated_date
      from farms.farm_tip_report_results trr
      join farms.farm_program_years py on py.program_year_id = trr.program_year_id
      join farms.farm_agristability_clients ac on ac.agristability_client_id = py.agristability_client_id
      join farms.farm_persons psn on psn.person_id = ac.person_id
      left outer join farms.farm_tip_report_results parent_trr on parent_trr.tip_report_result_id = trr.parent_tip_report_result_id
      left outer join farms.farm_tip_rating_codes cmmdtyr on cmmdtyr.tip_rating_code = trr.cmmdty_prchs_rpay_b_100_rating
      left outer join farms.farm_tip_rating_codes pmrr on pmrr.tip_rating_code = trr.production_margin_ratio_rating
      left outer join farms.farm_tip_rating_codes ocrr on ocrr.tip_rating_code = trr.operating_cost_ratio_rating
      left outer join farms.farm_tip_rating_codes derr on derr.tip_rating_code = trr.direct_expenses_ratio_rating
      left outer join farms.farm_tip_rating_codes merr on merr.tip_rating_code = trr.machinery_expenses_ratio_ratng
      left outer join farms.farm_tip_rating_codes lberr on lberr.tip_rating_code = trr.land_build_expens_ratio_rating
      left outer join farms.farm_tip_rating_codes oerr on oerr.tip_rating_code = trr.overhead_expenses_ratio_rating
      left outer join farms.farm_tip_benchmark_years tby3 on tby3.tip_benchmark_year_id = trr.tip_benchmark_year_id_type_3
      left outer join farms.farm_tip_benchmark_years tby2 on tby2.tip_benchmark_year_id = trr.tip_benchmark_year_id_type_2
      left outer join farms.farm_tip_benchmark_years tby1 on tby1.tip_benchmark_year_id = trr.tip_benchmark_year_id_type_1
      where ((trr.year = in_program_year and coalesce(trr.parent_tip_report_result_id::text, '') = '')
             or parent_trr.year = in_program_year)
        and (coalesce(in_farm_type_3_name::text, '') = '' or (trr.farm_type_3_name = in_farm_type_3_name and (coalesce(in_income_range_low::text, '') = '' or tby3.income_range_low >= in_income_range_low)))
        and (coalesce(in_farm_type_2_name::text, '') = '' or (trr.farm_type_2_name = in_farm_type_2_name and (coalesce(in_income_range_low::text, '') = '' or tby2.income_range_low >= in_income_range_low)))
        and (coalesce(in_farm_type_1_name::text, '') = '' or (trr.farm_type_1_name = in_farm_type_1_name and (coalesce(in_income_range_low::text, '') = '' or tby1.income_range_low >= in_income_range_low)))
        and (coalesce(in_farm_type_3_name::text, '') = '' or (trr.farm_type_3_name = in_farm_type_3_name and (coalesce(in_income_range_high::text, '') = '' or tby3.income_range_high <= in_income_range_high)))
        and (coalesce(in_farm_type_2_name::text, '') = '' or (trr.farm_type_2_name = in_farm_type_2_name and (coalesce(in_income_range_high::text, '') = '' or tby2.income_range_high <= in_income_range_high)))
        and (coalesce(in_farm_type_1_name::text, '') = '' or (trr.farm_type_1_name = in_farm_type_1_name and (coalesce(in_income_range_high::text, '') = '' or tby1.income_range_high <= in_income_range_high))) 
      order by trr.farm_type_3_name,
               trr.farm_type_2_name,
               trr.farm_type_1_name,
               trr.participant_pin,
               trr.alignment_key,
               trr.year desc;

    return cur;

end;
$$;
