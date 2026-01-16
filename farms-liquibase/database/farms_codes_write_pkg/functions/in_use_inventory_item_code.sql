create or replace function farms_codes_write_pkg.in_use_inventory_item_code(
    in in_inventory_item_code farms.farm_inventory_item_codes.inventory_item_code%type
)
returns numeric
language plpgsql
as $$
declare
    v_in_use numeric;
begin

    select (case
        when exists(
            select null
            from farms.farm_agristabilty_cmmdty_xref t
            where t.inventory_item_code = in_inventory_item_code
        )
        or exists(
            select null
            from farms.farm_productve_unit_capacities t
            where t.inventory_item_code = in_inventory_item_code
        )
        or exists(
            select null
            from farms.farm_fair_market_values t
            where t.inventory_item_code = in_inventory_item_code
        )
        or exists(
            select null
            from farms.farm_benchmark_per_units t
            where t.inventory_item_code = in_inventory_item_code
        )
        then 1
        else 0
    end) into v_in_use;

    return v_in_use;
end;
$$;
