create or replace procedure farms_codes_write_pkg.update_bpu_year(
   in in_benchmark_per_unit_id farms.benchmark_year.benchmark_per_unit_id%type,
   in in_year farms.benchmark_year.benchmark_year%type,
   in in_margin farms.benchmark_year.average_margin%type,
   in in_expense farms.benchmark_year.average_expense%type,
   in in_revision_count farms.benchmark_year.revision_count%type,
   in in_user farms.benchmark_year.update_user%type
)
language plpgsql
as $$
begin
    update farms.benchmark_year
    set average_margin = in_margin,
        average_expense = in_expense,
        update_timestamp = current_timestamp,
        update_user = in_user,
        revision_count = (in_revision_count + 1)
    where benchmark_per_unit_id = in_benchmark_per_unit_id
    and benchmark_year = in_year
    and revision_count = in_revision_count;

    if sql%rowcount <> 1 then
        raise exception 'Invalid revision count';
    end if;
end;
$$;
