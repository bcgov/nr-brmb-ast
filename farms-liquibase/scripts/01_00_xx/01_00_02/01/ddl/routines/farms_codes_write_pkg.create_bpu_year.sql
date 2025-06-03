create or replace procedure farms_codes_write_pkg.create_bpu_year(
   in in_benchmark_per_unit_id farms.benchmark_year.benchmark_per_unit_id%type,
   in in_year farms.benchmark_year.benchmark_year%type,
   in in_margin farms.benchmark_year.average_margin%type,
   in in_expense farms.benchmark_year.average_expense%type,
   in in_user farms.benchmark_year.create_user%type
)
language plpgsql
as $$
begin
    insert into farms.benchmark_year (
        benchmark_year,
        average_margin,
        average_expense,
        benchmark_per_unit_id,
        revision_count,
        create_user,
        create_date
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
