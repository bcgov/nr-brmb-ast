create or replace function farms_tip_user_pkg.benchmarks_match_config(
   in in_year farms.farm_program_years.year%type
)
returns varchar
language plpgsql
as $$
declare

    v_config_updated varchar(1);

begin

    select case when count(*) > 0 then 'N' else 'Y' end
    into v_config_updated
    from farms.farm_tip_benchmark_years tby
    where not exists (select * from farms.farm_tip_income_ranges_vw tir
                      where tir.range_low = tby.income_range_low
                        and tir.range_high = tby.income_range_high
                        and tir.minimum_group_count = tby.minimum_group_count
                        and (tir.farm_type_3_name = tby.farm_type_3_name
                             or tir.farm_type_2_name = tby.farm_type_2_name
                             or tir.farm_type_1_name = tby.farm_type_1_name
                            ) 
                     )
    and tby.program_year = in_year;

    return v_config_updated;
end;
$$;
