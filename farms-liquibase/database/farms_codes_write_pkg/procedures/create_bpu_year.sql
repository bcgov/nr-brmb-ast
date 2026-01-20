create or replace procedure farms_codes_write_pkg.create_bpu_year(
   in in_benchmark_per_unit_id farms.farm_benchmark_years.benchmark_per_unit_id%type,
   in in_year farms.farm_benchmark_years.benchmark_year%type,
   in in_margin farms.farm_benchmark_years.average_margin%type,
   in in_expense farms.farm_benchmark_years.average_expense%type,
   in in_user farms.farm_benchmark_years.who_created%type
)
language plpgsql
as $$
begin
    insert into farms.farm_benchmark_years (
        benchmark_year,
        average_margin,
        average_expense,
        benchmark_per_unit_id,
        revision_count,
        who_created,
        when_created
    ) values (
        in_year,
        in_margin,
        in_expense,
        in_benchmark_per_unit_id,
        1,
        in_user,
        current_timestamp
    );
end;
$$;
