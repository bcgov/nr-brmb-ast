create or replace function farms_codes_write_pkg.in_use_inventory_item_code(
    in in_inventory_item_code farms.inventory_item_code.inventory_item_code%type
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
            from farms.agristabilty_commodity_xref t
            where t.inventory_item_code = in_inventory_item_code
        )
        or exists(
            select null
            from farms.productive_unit_capacity t
            where t.inventory_item_code = in_inventory_item_code
        )
        or exists(
            select null
            from farms.fair_market_value t
            where t.inventory_item_code = in_inventory_item_code
        )
        or exists(
            select null
            from farms.benchmark_per_unit t
            where t.inventory_item_code = in_inventory_item_code
        )
        then 1
        else 0
    end) into v_in_use;

    return v_in_use;
end;
$$;
