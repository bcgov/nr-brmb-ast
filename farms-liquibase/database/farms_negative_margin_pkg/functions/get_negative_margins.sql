create or replace function farms_negative_margin_pkg.get_negative_margins(
    in in_farming_operation_id farms.farm_farming_operations.farming_operation_id%type,
    in in_agristability_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type
) returns refcursor
language plpgsql
as
$$
declare

    v_cursor refcursor;

begin
    open v_cursor for
        select nm.negative_margin_id,
               nm.farming_operation_id,
               nm.inventory_item_code,
               nm.agristability_scenario_id,
               nm.revision_count,
               iic.description as inventory_desc,
               nm.deductible_percentage,
               nm.required_insurable_value,
               nm.insurable_value_purchased,
               nm.reported_quantity as reported,
               nm.guaranteed_prod_value,
               nm.premiums_paid,
               nm.required_premium,
               nm.market_rate_premium,
               nm.claims_received,
               nm.claims_calculation,
               nm.deemed_premium,
               nm.premium_rate,
               nm.deemed_received,
               nm.deemed_pi_value
        from farms.farm_negative_margins nm
        join farms.farm_inventory_item_codes iic on nm.inventory_item_code = iic.inventory_item_code
        where nm.farming_operation_id = in_farming_operation_id
        and nm.agristability_scenario_id = in_agristability_scenario_id
        order by nm.inventory_item_code;
    return v_cursor;
end;
$$;
