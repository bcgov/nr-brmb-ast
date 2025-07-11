create or replace function farms_codes_read_pkg.read_market_rate_premiums(
    in in_id farms.market_rate_premium.market_rate_premium_id%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select m.market_rate_premium_id,
               m.minimum_total_premium_amount,
               m.maximum_total_premium_amount,
               m.risk_charge_flat_amount,
               m.risk_charge_percent_premium,
               m.adjust_charge_flat_amount,
               m.revision_count
        from farms.market_rate_premium m
        where (in_id is null or m.market_rate_premium_id = in_id)
        order by m.minimum_total_premium_amount;
    return cur;
end;
$$;
