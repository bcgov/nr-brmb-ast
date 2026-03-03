create or replace procedure farms_codes_write_pkg.delete_market_rate_premium(
   in in_market_rate_premium_id farms.farm_market_rate_premium.market_rate_premium_id%type,
   in in_revision_count farms.farm_market_rate_premium.revision_count%type
)
language plpgsql
as $$
declare
    v_rows_affected  bigint := null;
begin
    delete from farms.farm_market_rate_premium m
    where m.market_rate_premium_id = in_market_rate_premium_id
    and m.revision_count = in_revision_count;

    get diagnostics v_rows_affected = row_count;
    if v_rows_affected = 0 then
        raise exception 'Invalid revision count';
    end if;
end;
$$;
