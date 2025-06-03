create or replace function farms_codes_write_pkg.create_inventory_xref(
    in in_inventory_class_code farms.agristabilty_commodity_xref.inventory_class_code%type,
    in in_inventory_item_code farms.agristabilty_commodity_xref.inventory_item_code%type,
    in in_inventory_group_code farms.agristabilty_commodity_xref.inventory_group_code%type,
    in in_market_commodity_indicator farms.agristabilty_commodity_xref.market_commodity_indicator%type,
    in in_user farms.agristabilty_commodity_xref.update_user%type
)
returns farms.agristabilty_commodity_xref.agristabilty_commodity_xref_id%type
language plpgsql
as $$
declare
    v_xref_id farms.agristabilty_commodity_xref.agristabilty_commodity_xref_id%type;
begin

    select nextval('farms.seq_acx')
    into v_xref_id;

    insert into farms.agristabilty_commodity_xref (
        agristabilty_commodity_xref_id,
        market_commodity_indicator,
        inventory_item_code,
        inventory_group_code,
        inventory_class_code,
        revision_count,
        create_user,
        create_date,
        update_user,
        update_date
    ) values (
        v_xref_id,
        in_market_commodity_indicator,
        in_inventory_item_code,
        in_inventory_group_code,
        in_inventory_class_code,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );

    return v_xref_id;
end;
$$;
