create or replace procedure farms_negative_margin_pkg.update_negative_margin(
    in in_negative_margin_id farms.farm_negative_margins.negative_margin_id%type,
    in in_deductible_percentage farms.farm_negative_margins.deductible_percentage%type,
    in in_insurable_value_purchased farms.farm_negative_margins.insurable_value_purchased%type,
    in in_guaranteed_prod_value farms.farm_negative_margins.guaranteed_prod_value%type,
    in in_premiums_paid farms.farm_negative_margins.premiums_paid%type,
    in in_claims_received farms.farm_negative_margins.claims_received%type,
    in in_user farms.farm_negative_margins.who_created%type
)
language plpgsql
as
$$
begin
    update farms.farm_negative_margins
    set deductible_percentage = in_deductible_percentage,
        insurable_value_purchased = in_insurable_value_purchased,
        guaranteed_prod_value = in_guaranteed_prod_value,
        premiums_paid = in_premiums_paid,
        claims_received = in_claims_received,
        revision_count = revision_count + 1,
        who_updated = in_user,
        when_updated = current_timestamp
    where negative_margin_id = in_negative_margin_id;

end;
$$;
