create or replace function farms_codes_write_pkg.create_inventory_xref(
    in in_inventory_class_code farms.farm_agristabilty_cmmdty_xref.inventory_class_code%type,
    in in_inventory_item_code farms.farm_agristabilty_cmmdty_xref.inventory_item_code%type,
    in in_inventory_group_code farms.farm_agristabilty_cmmdty_xref.inventory_group_code%type,
    in in_market_commodity_indicator farms.farm_agristabilty_cmmdty_xref.market_commodity_ind%type,
    in in_user farms.farm_agristabilty_cmmdty_xref.who_updated%type
)
returns farms.farm_agristabilty_cmmdty_xref.agristabilty_cmmdty_xref_id%type
language plpgsql
as $$
declare
    v_xref_id farms.farm_agristabilty_cmmdty_xref.agristabilty_cmmdty_xref_id%type;
begin

    select nextval('farms.farm_acx_seq')
    into v_xref_id;

    insert into farms.farm_agristabilty_cmmdty_xref (
        agristabilty_cmmdty_xref_id,
        market_commodity_ind,
        inventory_item_code,
        inventory_group_code,
        inventory_class_code,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
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
