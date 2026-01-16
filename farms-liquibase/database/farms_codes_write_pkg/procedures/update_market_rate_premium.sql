create or replace procedure farms_codes_write_pkg.update_market_rate_premium(
   in in_market_rate_premium_id farms.farm_market_rate_premium.market_rate_premium_id%type,
   in in_minimum_total_premium_amount farms.farm_market_rate_premium.min_total_premium_amount%type,
   in in_maximum_total_premium_amount farms.farm_market_rate_premium.max_total_premium_amount%type,
   in in_risk_charge_flat_amount farms.farm_market_rate_premium.risk_charge_flat_amount%type,
   in in_risk_charge_percent_premium farms.farm_market_rate_premium.risk_charge_pct_premium%type,
   in in_adjust_charge_flat_amount farms.farm_market_rate_premium.adjust_charge_flat_amount%type,
   in in_revision_count farms.farm_market_rate_premium.revision_count%type,
   in in_user farms.farm_market_rate_premium.who_created%type
)
language plpgsql
as $$
begin
    update farms.farm_market_rate_premium
    set min_total_premium_amount = in_minimum_total_premium_amount,
        max_total_premium_amount = in_maximum_total_premium_amount,
        risk_charge_flat_amount = in_risk_charge_flat_amount,
        risk_charge_pct_premium = in_risk_charge_percent_premium,
        adjust_charge_flat_amount = in_adjust_charge_flat_amount,
        revision_count = revision_count + 1,
        who_updated = in_user,
        when_updated = current_timestamp
    where market_rate_premium_id = in_market_rate_premium_id
    and revision_count = in_revision_count;

    if sql%rowcount <> 1 then
        raise exception 'Invalid revision count';
    end if;
end;
$$;
