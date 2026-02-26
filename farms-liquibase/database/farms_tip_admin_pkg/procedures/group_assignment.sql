create or replace procedure farms_tip_admin_pkg.group_assignment(
   in in_year farms.farm_program_years.year%type,
   in in_mode text,
   in in_user farms.farm_tip_report_results.who_updated%type
)
language plpgsql
as $$
begin

    merge into farms.farm_tip_report_results o
    using(
    with trr as (
      select parent_trr.year program_year,
             parent_trr.year,
             parent_trr.tip_report_result_id,
             parent_trr.farm_type_3_name,
             parent_trr.farm_type_2_name,
             parent_trr.farm_type_1_name,
             parent_trr.farm_type_3_threshold_met_ind,
             parent_trr.farm_type_2_threshold_met_ind,
             parent_trr.farm_type_1_threshold_met_ind,
             parent_trr.allowable_income program_year_allowable_income,
             parent_trr.cmmdty_prchs_rpay_bnft_100,
             parent_trr.non_allowbl_repay_pgm_bnft_100,
             parent_trr.production_margin_ratio,
             parent_trr.operating_cost_ratio,
             parent_trr.direct_expenses_ratio,
             parent_trr.machinery_expenses_ratio,
             parent_trr.land_build_expenses_ratio,
             parent_trr.overhead_expenses_ratio
      from farms.farm_tip_report_results parent_trr
      where coalesce(parent_trr.parent_tip_report_result_id::text, '') = ''
        and parent_trr.year = in_year
        and in_mode = 'PROGRAM_YEAR'
      
union all

      select parent_trr.year program_year,
             rtrr.year,
             rtrr.tip_report_result_id,
             parent_trr.farm_type_3_name,
             parent_trr.farm_type_2_name,
             parent_trr.farm_type_1_name,
             parent_trr.farm_type_3_threshold_met_ind,
             parent_trr.farm_type_2_threshold_met_ind,
             parent_trr.farm_type_1_threshold_met_ind,
             parent_trr.allowable_income program_year_allowable_income,
             rtrr.cmmdty_prchs_rpay_bnft_100,
             rtrr.non_allowbl_repay_pgm_bnft_100,
             rtrr.production_margin_ratio,
             rtrr.operating_cost_ratio,
             rtrr.direct_expenses_ratio,
             rtrr.machinery_expenses_ratio,
             rtrr.land_build_expenses_ratio,
             rtrr.overhead_expenses_ratio
      from farms.farm_tip_report_results parent_trr
      join farms.farm_tip_report_results rtrr on rtrr.parent_tip_report_result_id = parent_trr.tip_report_result_id
      where coalesce(parent_trr.parent_tip_report_result_id::text, '') = ''
        and parent_trr.year = in_year
        and in_mode = 'REFERENCE_YEAR' 
    ),
    b1 as (
      select trr.tip_report_result_id,
             case when tby1.farming_operation_count >= tby1.minimum_group_count then 1
                  when tby2.farming_operation_count >= tby2.minimum_group_count then 2
                  when tby3.farming_operation_count >= tby3.minimum_group_count then 3
             end farm_type_level,
             trr.farm_type_3_name,
             trr.farm_type_2_name,
             trr.farm_type_1_name,
             trr.cmmdty_prchs_rpay_bnft_100,
             trr.non_allowbl_repay_pgm_bnft_100,
             trr.production_margin_ratio,
             trr.operating_cost_ratio,
             trr.direct_expenses_ratio,
             trr.machinery_expenses_ratio,
             trr.land_build_expenses_ratio,
             trr.overhead_expenses_ratio,
             tby3.tip_benchmark_year_id tip_benchmark_year_id_type_3,
             tby2.tip_benchmark_year_id tip_benchmark_year_id_type_2,
             tby1.tip_benchmark_year_id tip_benchmark_year_id_type_1
      from trr
      left outer join farms.farm_tip_benchmark_years tby1 on tby1.program_year = trr.program_year
                                                   and tby1.reference_year = trr.year
                                                   and tby1.farm_type_1_name = trr.farm_type_1_name
                                                   and trr.program_year_allowable_income between tby1.income_range_low and tby1.income_range_high
                                                   and trr.farm_type_1_threshold_met_ind = 'Y'
      left outer join farms.farm_tip_benchmark_years tby2 on tby2.program_year = trr.program_year
                                                   and tby2.reference_year = trr.year
                                                   and tby2.farm_type_2_name = trr.farm_type_2_name
                                                   and trr.program_year_allowable_income between tby2.income_range_low and tby2.income_range_high
                                                   and trr.farm_type_2_threshold_met_ind = 'Y'
      left outer join farms.farm_tip_benchmark_years tby3 on tby3.program_year = trr.program_year
                                                   and tby3.reference_year = trr.year
                                                   and tby3.farm_type_3_name = trr.farm_type_3_name
                                                   and trr.program_year_allowable_income between tby3.income_range_low and tby3.income_range_high
    ),
    b2 as (
      select b1.tip_report_result_id,
             b1.farm_type_level,
             b1.farm_type_3_name,
             b1.farm_type_2_name,
             b1.farm_type_1_name,
             b1.cmmdty_prchs_rpay_bnft_100,
             b1.non_allowbl_repay_pgm_bnft_100,
             b1.production_margin_ratio,
             b1.operating_cost_ratio,
             b1.direct_expenses_ratio,
             b1.machinery_expenses_ratio,
             b1.land_build_expenses_ratio,
             b1.overhead_expenses_ratio,

               case b1.farm_type_level
                 when 1 then b1.tip_benchmark_year_id_type_1
                 when 2 then b1.tip_benchmark_year_id_type_2
                 when 3 then b1.tip_benchmark_year_id_type_3
               end
             tip_benchmark_year_id,
             b1.tip_benchmark_year_id_type_3,
             b1.tip_benchmark_year_id_type_2,
             b1.tip_benchmark_year_id_type_1
      from b1
    ),
    b3 as (
      select b2.*,

             tby.income_range_low,
             tby.income_range_high,
             tby.farming_operation_count group_farming_operation_count,
             tby.total_income total_income_benchmark,
             tby.allowable_income_per_100 allowable_income_per_100_bench,
             tby.other_farm_income_per100 other_farm_income_per100_bench,
             tby.allowable_expenses_per_100 allowable_expens_per_100_bench,
             tby.non_allowable_expenses_per_100 non_allowabl_expns_per_100_bch,
             tby.cmmdty_prchs_rpay_bnft_per_100 cmmdty_prchs_rpay_bnft_100_bch,
             tby.cmmdty_prchs_per100_high_25pct,
             tby.non_allowbl_repay_pgm_bnft_100 non_allw_rpay_pgm_bnft_100_bch,
             tby.non_allw_rpay_pgm_bnf_100_h25p,
             tby.total_expenses_per_100 total_expenses_per_100_bench,
             tby.production_margin_per_100 production_margin_per_100_bch,
             tby.net_margin net_margin_bench,
             tby.net_margin_per_100 net_margin_per_100_bench,

             tby.production_margin_ratio production_margin_ratio_bench,
             tby.operating_cost_ratio operating_cost_ratio_bench,
             tby.direct_expenses_ratio direct_expenses_ratio_bench,
             tby.machinery_expenses_ratio machinery_expenses_ratio_bench,
             tby.land_build_expenses_ratio land_build_expenses_ratio_bench,
             tby.overhead_expenses_ratio overhead_expenses_ratio_bench,

             tby.prodtn_margin_ratio_low_25pct,
             tby.operatng_cost_ratio_high_25pct,
             tby.direct_expens_ratio_high_25pct,
             tby.machnry_expns_ratio_high_25pct,
             tby.land_bld_expn_ratio_high_25pct,
             tby.overhead_expn_ratio_high_25pct
      from b2
      left outer join farms.farm_tip_benchmark_years tby on tby.tip_benchmark_year_id = b2.tip_benchmark_year_id
    )
    select b3.*,
           case
              when b3.production_margin_ratio >= b3.production_margin_ratio_bench then 'GOOD'
              when b3.production_margin_ratio >  b3.prodtn_margin_ratio_low_25pct then 'CAUTION'
              else 'CONCERN'
           end production_margin_ratio_rating,
           case
              when b3.operating_cost_ratio <= b3.operating_cost_ratio_bench then 'GOOD'
              when b3.operating_cost_ratio <  b3.operatng_cost_ratio_high_25pct then 'CAUTION'
              else 'CONCERN'
           end operating_cost_ratio_rating,
           case
              when b3.direct_expenses_ratio <= b3.direct_expenses_ratio_bench then 'GOOD'
              when b3.direct_expenses_ratio <  b3.direct_expens_ratio_high_25pct then 'CAUTION'
              else 'CONCERN'
           end direct_expenses_ratio_rating,
           case
              when b3.machinery_expenses_ratio <= b3.machinery_expenses_ratio_bench then 'GOOD'
              when b3.machinery_expenses_ratio <  b3.machnry_expns_ratio_high_25pct then 'CAUTION'
              else 'CONCERN'
           end machinery_expenses_ratio_ratng,
           case
              when b3.land_build_expenses_ratio <= b3.land_build_expenses_ratio_bench then 'GOOD'
              when b3.land_build_expenses_ratio <  b3.land_bld_expn_ratio_high_25pct then 'CAUTION'
              else 'CONCERN'
           end land_build_expens_ratio_rating,
           case
              when b3.overhead_expenses_ratio <= b3.overhead_expenses_ratio_bench then 'GOOD'
              when b3.overhead_expenses_ratio <  b3.overhead_expn_ratio_high_25pct then 'CAUTION'
              else 'CONCERN'
           end overhead_expenses_ratio_rating,
           case
             when abs(b3.cmmdty_prchs_rpay_bnft_100 - b3.cmmdty_prchs_rpay_bnft_100_bch) > 5 then
               case
                  when b3.cmmdty_prchs_rpay_bnft_100 <= b3.cmmdty_prchs_rpay_bnft_100_bch then 'GOOD'
                  when b3.cmmdty_prchs_rpay_bnft_100 <  b3.cmmdty_prchs_per100_high_25pct then 'CAUTION'
                  else 'CONCERN'
               end
           end cmmdty_prchs_rpay_b_100_rating
    from b3
    ) n
    on (o.tip_report_result_id = n.tip_report_result_id)
    when matched then
      update set
        farm_type_level = n.farm_type_level,
        income_range_low = n.income_range_low,
        income_range_high = n.income_range_high,
        group_farming_operation_count = n.group_farming_operation_count,
        total_income_benchmark = n.total_income_benchmark,
        allowable_income_per_100_bench = n.allowable_income_per_100_bench,
        other_farm_income_per100_bench = n.other_farm_income_per100_bench,
        allowable_expens_per_100_bench = n.allowable_expens_per_100_bench,
        non_allowabl_expns_per_100_bch = n.non_allowabl_expns_per_100_bch,
        cmmdty_prchs_rpay_bnft_100_bch = n.cmmdty_prchs_rpay_bnft_100_bch,
        cmmdty_prchs_per100_high_25pct = n.cmmdty_prchs_per100_high_25pct,
        non_allw_rpay_pgm_bnft_100_bch = n.non_allw_rpay_pgm_bnft_100_bch,
        non_allw_rpay_pgm_bnf_100_h25p = n.non_allw_rpay_pgm_bnf_100_h25p,
        total_expenses_per_100_bench = n.total_expenses_per_100_bench,
        production_margin_per_100_bch = n.production_margin_per_100_bch,
        net_margin_per_100_bench = n.net_margin_per_100_bench,
        production_margin_ratio_bench = n.production_margin_ratio_bench,
        operating_cost_ratio_bench = n.operating_cost_ratio_bench,
        direct_expenses_ratio_bench = n.direct_expenses_ratio_bench,
        machinery_expenses_ratio_bench = n.machinery_expenses_ratio_bench,
        land_build_expens_ratio_bench = n.land_build_expenses_ratio_bench,
        overhead_expenses_ratio_bench = n.overhead_expenses_ratio_bench,
        prodtn_margin_ratio_low_25pct = n.prodtn_margin_ratio_low_25pct,
        operatng_cost_ratio_high_25pct = n.operatng_cost_ratio_high_25pct,
        direct_expens_ratio_high_25pct = n.direct_expens_ratio_high_25pct,
        machnry_expns_ratio_high_25pct = n.machnry_expns_ratio_high_25pct,
        land_bld_expn_ratio_high_25pct = n.land_bld_expn_ratio_high_25pct,
        overhead_expn_ratio_high_25pct = n.overhead_expn_ratio_high_25pct,
        cmmdty_prchs_rpay_b_100_rating = n.cmmdty_prchs_rpay_b_100_rating,
        production_margin_ratio_rating = n.production_margin_ratio_rating,
        operating_cost_ratio_rating = n.operating_cost_ratio_rating,
        direct_expenses_ratio_rating = n.direct_expenses_ratio_rating,
        machinery_expenses_ratio_ratng = n.machinery_expenses_ratio_ratng,
        land_build_expens_ratio_rating = n.land_build_expens_ratio_rating,
        overhead_expenses_ratio_rating = n.overhead_expenses_ratio_rating,
        tip_benchmark_year_id = n.tip_benchmark_year_id,
        tip_benchmark_year_id_type_3 = n.tip_benchmark_year_id_type_3,
        tip_benchmark_year_id_type_2 = n.tip_benchmark_year_id_type_2,
        tip_benchmark_year_id_type_1 = n.tip_benchmark_year_id_type_1,
        generated_date = current_timestamp,
        revision_count = revision_count + 1,
        who_updated = in_user,
        when_updated = current_timestamp;

end;
$$;
