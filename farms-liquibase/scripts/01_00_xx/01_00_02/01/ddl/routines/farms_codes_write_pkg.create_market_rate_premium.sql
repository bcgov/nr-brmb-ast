create or replace procedure farms_codes_write_pkg.create_market_rate_premium(
   in in_minimum_total_premium_amount farms.market_rate_premium.minimum_total_premium_amount%type,
   in in_maximum_total_premium_amount farms.market_rate_premium.maximum_total_premium_amount%type,
   in in_risk_charge_flat_amount farms.market_rate_premium.risk_charge_flat_amount%type,
   in in_risk_charge_percent_premium farms.market_rate_premium.risk_charge_percent_premium%type,
   in in_adjust_charge_flat_amount farms.market_rate_premium.adjust_charge_flat_amount%type,
   in in_user farms.market_rate_premium.create_user%type
)
language plpgsql
as $$
begin
    insert into farms.market_rate_premium (
        market_rate_premium_id,
        minimum_total_premium_amount,
        maximum_total_premium_amount,
        risk_charge_flat_amount,
        risk_charge_percent_premium,
        adjust_charge_flat_amount,
        revision_count,
        create_user,
        create_date,
        update_user,
        update_date
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
