create or replace procedure farms_codes_write_pkg.update_market_rate_premium(
   in in_market_rate_premium_id farms.market_rate_premium.market_rate_premium_id%type,
   in in_minimum_total_premium_amount farms.market_rate_premium.minimum_total_premium_amount%type,
   in in_maximum_total_premium_amount farms.market_rate_premium.maximum_total_premium_amount%type,
   in in_risk_charge_flat_amount farms.market_rate_premium.risk_charge_flat_amount%type,
   in in_risk_charge_percent_premium farms.market_rate_premium.risk_charge_percent_premium%type,
   in in_adjust_charge_flat_amount farms.market_rate_premium.adjust_charge_flat_amount%type,
   in in_revision_count farms.market_rate_premium.revision_count%type,
   in in_user farms.market_rate_premium.create_user%type
)
language plpgsql
as $$
begin
    update farms.market_rate_premium m
    set m.minimum_total_premium_amount = in_minimum_total_premium_amount,
        m.maximum_total_premium_amount = in_maximum_total_premium_amount,
        m.risk_charge_flat_amount = in_risk_charge_flat_amount,
        m.risk_charge_percent_premium = in_risk_charge_percent_premium,
        m.adjust_charge_flat_amount = in_adjust_charge_flat_amount,
        m.revision_count = m.revision_count + 1,
        m.update_user = in_user,
        m.update_date = current_timestamp
    where m.market_rate_premium_id = in_market_rate_premium_id
    and m.revision_count = in_revision_count;

    if sql%rowcount <> 1 then
        raise exception 'Invalid revision count';
    end if;
end;
$$;
