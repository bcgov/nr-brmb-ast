create or replace procedure farms_codes_write_pkg.delete_market_rate_premium(
   in in_market_rate_premium_id farms.farm_market_rate_premium.market_rate_premium_id%type,
   in in_revision_count farms.farm_market_rate_premium.revision_count%type
)
language plpgsql
as $$
begin
    delete from farms.farm_market_rate_premium m
    where m.market_rate_premium_id = in_market_rate_premium_id
    and m.revision_count = in_revision_count;

    if sql%rowcount <> 1 then
        raise exception 'Invalid revision count';
    end if;
end;
$$;
