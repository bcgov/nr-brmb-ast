create or replace function farms_codes_read_pkg.get_livestock_unit_code(
    in in_inventory_item_code farms.fair_market_value.inventory_item_code%type
)
returns varchar
language plpgsql
as $$
declare
    inventory_item_code varchar;
begin
    case
        when in_inventory_item_code in ('7600','7603','7604','7602','7614') then inventory_item_code := '1';
        when in_inventory_item_code in ('7663','7664','7665') then inventory_item_code := '3';
        when in_inventory_item_code in ('7606','7608','7610','7612') then inventory_item_code := '99';
        else inventory_item_code := '32';
    end case;
    return inventory_item_code;
end;
$$;
