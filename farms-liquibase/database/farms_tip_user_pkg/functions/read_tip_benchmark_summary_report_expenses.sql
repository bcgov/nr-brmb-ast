create or replace function farms_tip_user_pkg.read_tip_benchmark_summary_report_expenses(
   in in_program_year farms.farm_tip_benchmark_years.program_year%type,
   in in_farm_type_3_name farms.farm_tip_benchmark_years.farm_type_3_name%type,
   in in_farm_type_2_name farms.farm_tip_benchmark_years.farm_type_2_name%type,
   in in_farm_type_1_name farms.farm_tip_benchmark_years.farm_type_1_name%type,
   in in_income_range_low farms.farm_tip_benchmark_years.income_range_low%type,
   in in_eligibility_ind farms.farm_tip_benchmark_expenses.eligibility_ind%type
)
returns refcursor
language plpgsql
as $$
declare

    cur refcursor;

begin

    open cur for

      select tbe.description,
             tbe.line_item,
             tbe.amount_per_100_5_year_average,
             tbe.amount_per_100
      from farms.farm_tip_benchmark_years tby
      join farms.farm_tip_benchmark_expenses tbe on tbe.tip_benchmark_year_id = tby.tip_benchmark_year_id
      where tby.reference_year = tby.program_year
        and tby.program_year = in_program_year
        and (coalesce(in_farm_type_3_name::text, '') = '' or tby.farm_type_3_name = in_farm_type_3_name)
        and (coalesce(in_farm_type_2_name::text, '') = '' or tby.farm_type_2_name = in_farm_type_2_name)
        and (coalesce(in_farm_type_1_name::text, '') = '' or tby.farm_type_1_name = in_farm_type_1_name)
        and (coalesce(in_income_range_low::text, '') = '' or tby.income_range_low = in_income_range_low)
        and (coalesce(in_eligibility_ind::text, '') = '' or tbe.eligibility_ind = in_eligibility_ind) 
      order by tby.reference_year desc,
               tby.farm_type_1_name,
               tby.farm_type_2_name,
               tby.farm_type_3_name,
               tby.income_range_low,
               tbe.eligibility_ind desc,
               tbe.line_item;

    return cur;

end;
$$;
