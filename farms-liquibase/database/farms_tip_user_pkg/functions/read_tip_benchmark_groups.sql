create or replace function farms_tip_user_pkg.read_tip_benchmark_groups()
returns refcursor
language plpgsql
as $$
declare

    cur refcursor;

begin

    open cur for

      select tby.program_year,
             tby.farm_type_3_name,
             tby.farm_type_2_name,
             tby.farm_type_1_name,
             tby.income_range_low,
             tby.income_range_high
       from farms.farm_tip_benchmark_years tby
       where tby.program_year = tby.reference_year
       order by tby.program_year,
             tby.farm_type_3_name,
             tby.farm_type_2_name,
             tby.farm_type_1_name,
             tby.income_range_low;

    return cur;

end;
$$;
