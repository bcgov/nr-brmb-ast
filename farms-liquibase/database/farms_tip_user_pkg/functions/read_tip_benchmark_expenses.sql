create or replace function farms_tip_user_pkg.read_tip_benchmark_expenses(
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
             tby.generated_date,
             tbe.line_item,
             tbe.description,
             tbe.eligibility_ind,
             tbe.amount,
             tbe.amount_per_100,
             tbe.amount_per_100_high_25_pct,
             tbe.amount_per_100_5_year_average,
             tby.reference_year_count
      from farms.farm_tip_benchmark_years tby
      join farms.farm_tip_benchmark_expenses tbe on tbe.tip_benchmark_year_id = tby.tip_benchmark_year_id
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
               tby.income_range_low,
               tbe.eligibility_ind desc,
               tbe.line_item;

    return cur;

end;
$$;
