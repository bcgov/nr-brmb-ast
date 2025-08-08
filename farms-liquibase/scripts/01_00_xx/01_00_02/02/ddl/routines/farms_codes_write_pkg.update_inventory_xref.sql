create or replace procedure farms_codes_write_pkg.update_inventory_xref(
   in in_agristabilty_commodity_xref_id farms.farm_agristabilty_cmmdty_xref.agristabilty_cmmdty_xref_id%type,
   in in_inventory_group_code farms.farm_agristabilty_cmmdty_xref.inventory_group_code%type,
   in in_market_commodity_indicator farms.farm_agristabilty_cmmdty_xref.market_commodity_ind%type,
   in in_revision_count farms.farm_agristabilty_cmmdty_xref.revision_count%type,
   in in_user farms.farm_agristabilty_cmmdty_xref.who_updated%type
)
language plpgsql
as $$
begin
    update farms.farm_agristabilty_cmmdty_xref x
    set x.inventory_group_code = in_inventory_group_code,
        x.market_commodity_ind = in_market_commodity_indicator,
        x.revision_count = x.revision_count + 1,
        x.who_updated = in_user,
        x.when_updated = current_timestamp
    where x.agristabilty_cmmdty_xref_id = in_agristabilty_commodity_xref_id
    and x.revision_count = in_revision_count;

    if sql%rowcount <> 1 then
        raise exception 'Invalid revision count';
    end if;
end;
$$;
