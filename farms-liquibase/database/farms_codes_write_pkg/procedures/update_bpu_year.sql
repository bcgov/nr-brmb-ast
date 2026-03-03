create or replace procedure farms_codes_write_pkg.update_bpu_year(
   in in_benchmark_per_unit_id farms.farm_benchmark_years.benchmark_per_unit_id%type,
   in in_year farms.farm_benchmark_years.benchmark_year%type,
   in in_margin farms.farm_benchmark_years.average_margin%type,
   in in_expense farms.farm_benchmark_years.average_expense%type,
   in in_revision_count farms.farm_benchmark_years.revision_count%type,
   in in_user farms.farm_benchmark_years.who_updated%type
)
language plpgsql
as $$
declare
    v_rows_affected  bigint := null;
begin
    update farms.farm_benchmark_years
    set average_margin = in_margin,
        average_expense = in_expense,
        when_updated = current_timestamp,
        who_updated = in_user,
        revision_count = (in_revision_count + 1)
    where benchmark_per_unit_id = in_benchmark_per_unit_id
    and benchmark_year = in_year
    and revision_count = in_revision_count;

    get diagnostics v_rows_affected = row_count;
    if v_rows_affected = 0 then
        raise exception 'Invalid revision count';
    end if;
end;
$$;
