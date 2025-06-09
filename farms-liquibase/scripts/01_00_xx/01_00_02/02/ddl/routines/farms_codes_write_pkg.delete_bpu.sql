create or replace procedure farms_codes_write_pkg.delete_bpu(
   in in_benchmark_per_unit_id farms.benchmark_per_unit.benchmark_per_unit_id%type
)
language plpgsql
as $$
begin
    --
    -- You can't update the farm_benchmark_per_units table so there's no point in passing
    -- in the revision count.
    --
    delete from farms.benchmark_year
    where benchmark_per_unit_id = in_benchmark_per_unit_id;
    delete from farms.benchmark_per_unit
    where benchmark_per_unit_id = in_benchmark_per_unit_id;
end;
$$;
