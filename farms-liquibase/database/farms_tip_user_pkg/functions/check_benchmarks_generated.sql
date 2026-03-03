create or replace function farms_tip_user_pkg.check_benchmarks_generated(
   in in_year farms.farm_program_years.year%type
)
returns bigint
language plpgsql
as $$
declare

    v_benchmark_count bigint;

begin

    select count(*)
    into v_benchmark_count
    from farms.farm_tip_benchmark_years tby
    where tby.program_year = in_year;

    return v_benchmark_count;

end;
$$;
