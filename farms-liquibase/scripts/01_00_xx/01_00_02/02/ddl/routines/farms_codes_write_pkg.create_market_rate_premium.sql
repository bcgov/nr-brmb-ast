create or replace procedure farms_codes_write_pkg.create_market_rate_premium(
   in in_minimum_total_premium_amount farms.farm_market_rate_premium.min_total_premium_amount%type,
   in in_maximum_total_premium_amount farms.farm_market_rate_premium.max_total_premium_amount%type,
   in in_risk_charge_flat_amount farms.farm_market_rate_premium.risk_charge_flat_amount%type,
   in in_risk_charge_percent_premium farms.farm_market_rate_premium.risk_charge_pct_premium%type,
   in in_adjust_charge_flat_amount farms.farm_market_rate_premium.adjust_charge_flat_amount%type,
   in in_user farms.farm_market_rate_premium.who_created%type
)
language plpgsql
as $$
begin
    insert into farms.farm_market_rate_premium (
        market_rate_premium_id,
        min_total_premium_amount,
        max_total_premium_amount,
        risk_charge_flat_amount,
        risk_charge_pct_premium,
        adjust_charge_flat_amount,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        nextval('farms.seq_mrp'),
        in_minimum_total_premium_amount,
        in_maximum_total_premium_amount,
        in_risk_charge_flat_amount,
        in_risk_charge_percent_premium,
        in_adjust_charge_flat_amount,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );
end;
$$;
