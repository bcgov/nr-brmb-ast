create or replace function farms_tip_user_pkg.read_tip_individual_expenses(
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

      select trr.participant_pin,
             coalesce(psn.corp_name, psn.last_name||', '||psn.first_name) client_name,
             trr.year,
             trr.reference_year_ind is_reference_year,
             trr.alignment_key,
             trr.partnership_pin,
             trr.farm_type_3_name,
             trr.farm_type_2_name,
             trr.farm_type_1_name,
             trr.farm_type_2_threshold_met_ind,
             trr.farm_type_1_threshold_met_ind,
             trr.farm_type_level,
             trr.income_range_low,
             trr.income_range_high,
             tre.line_item,
             tre.description,
             tre.eligibility_ind,
             tre.amount,
             tre.amount_per_100,
             tre.amount_per_100_5_year,
             tre.amount_per_100_benchmark,
             tre.amount_per_100_high_25_pct,
             trc.description expense_indicator,
             trr.use_for_benchmarks_ind,
             trr.tip_report_result_id,
             trr.parent_tip_report_result_id parent_id,
             tre.tip_report_expense_id,
             trr.generated_date,
             trr.reference_year_count
      from farms.farm_tip_report_results trr
      join farms.farm_program_years py on py.program_year_id = trr.program_year_id
      join farms.farm_agristability_clients ac on ac.agristability_client_id = py.agristability_client_id
      join farms.farm_persons psn on psn.person_id = ac.person_id
      left outer join farms.farm_tip_report_results parent_trr on parent_trr.tip_report_result_id = trr.parent_tip_report_result_id
      join farms.farm_tip_report_expenses tre on tre.tip_report_result_id = trr.tip_report_result_id
      left outer join farms.farm_tip_rating_codes trc on trc.tip_rating_code = tre.tip_rating_code
      where ((trr.year = in_program_year and coalesce(trr.parent_tip_report_result_id::text, '') = '')
             or parent_trr.year = in_program_year)
        and (coalesce(in_farm_type_3_name::text, '') = '' or trr.farm_type_3_name = in_farm_type_3_name)
        and (coalesce(in_farm_type_2_name::text, '') = '' or trr.farm_type_2_name = in_farm_type_2_name)
        and (coalesce(in_farm_type_1_name::text, '') = '' or trr.farm_type_1_name = in_farm_type_1_name)
        and (coalesce(in_income_range_low::text, '') = '' or trr.income_range_low >= in_income_range_low)
        and (coalesce(in_income_range_high::text, '') = '' or trr.income_range_high <= in_income_range_high) 
      order by trr.farm_type_3_name,
               trr.farm_type_2_name,
               trr.farm_type_1_name,
               trr.participant_pin,
               trr.alignment_key,
               trr.year desc,
               tre.eligibility_ind desc,
               tre.line_item;

    return cur;

end;
$$;
