create or replace procedure farms_bpu_pkg.clear_staging()
language plpgsql
as $$
begin
    truncate table farms.zbpu_benchmark_per_unit;
    truncate table farms.import_log;
end;
$$;
