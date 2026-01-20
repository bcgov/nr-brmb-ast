create or replace procedure farms_codes_write_pkg.delete_inventory_xref(
   in in_agristabilty_commodity_xref_id farms.farm_agristabilty_cmmdty_xref.agristabilty_cmmdty_xref_id%type,
   in in_revision_count farms.farm_agristabilty_cmmdty_xref.revision_count%type
)
language plpgsql
as $$
declare
    v_in_use numeric;
begin

    v_in_use := farms_codes_write_pkg.in_use_inventory_item_code(in_agristabilty_commodity_xref_id);

    if v_in_use = 0 then

        delete from farms.farm_agristabilty_cmmdty_xref x
        where x.agristabilty_cmmdty_xref_id = in_agristabilty_commodity_xref_id
        and x.revision_count = in_revision_count;

        if sql%rowcount <> 1 then
            raise exception 'Invalid revision count';
        end if;
    end if;

end;
$$;
