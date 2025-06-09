create or replace procedure farms_codes_write_pkg.update_inventory_xref(
   in in_agristabilty_commodity_xref_id farms.agristabilty_commodity_xref.agristabilty_commodity_xref_id%type,
   in in_inventory_group_code farms.agristabilty_commodity_xref.inventory_group_code%type,
   in in_market_commodity_indicator farms.agristabilty_commodity_xref.market_commodity_indicator%type,
   in in_revision_count farms.agristabilty_commodity_xref.revision_count%type,
   in in_user farms.agristabilty_commodity_xref.update_user%type
)
language plpgsql
as $$
begin
    update farms.agristabilty_commodity_xref x
    set x.inventory_group_code = in_inventory_group_code,
        x.market_commodity_indicator = in_market_commodity_indicator,
        x.revision_count = x.revision_count + 1,
        x.update_user = in_user,
        x.update_date = current_timestamp
    where x.agristabilty_commodity_xref_id = in_agristabilty_commodity_xref_id
    and x.revision_count = in_revision_count;

    if sql%rowcount <> 1 then
        raise exception 'Invalid revision count';
    end if;
end;
$$;
