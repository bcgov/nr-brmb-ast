create or replace procedure farms_bpu_pkg.clear_staging()
language plpgsql
as $$
begin
    truncate table farms.farm_zbpu_benchmark_per_units;
    truncate table farms.farm_import_logs;
end;
$$;
