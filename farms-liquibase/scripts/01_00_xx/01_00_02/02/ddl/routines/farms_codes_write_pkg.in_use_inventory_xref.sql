create or replace function farms_codes_write_pkg.in_use_inventory_xref(
    in in_agristabilty_commodity_xref_id farms.farm_agristabilty_cmmdty_xref.agristabilty_cmmdty_xref_id%type
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
            from farms.farm_reported_inventories t
            where t.agristabilty_cmmdty_xref_id = in_agristabilty_commodity_xref_id
        )
        or exists(
            -- Inventory codes can only be used by productive units if they are
            -- a crop or livestock. If so, and the inventory code is used
            -- by a productive unit then do not allow the xref to be deleted.
            select null
            from farms.farm_agristabilty_cmmdty_xref x
            where x.agristabilty_cmmdty_xref_id = in_agristabilty_commodity_xref_id
            and x.inventory_class_code in ('1', '2')
            and exists (
                select null
                from farms.farm_productve_unit_capacities puc
                where puc.inventory_item_code = x.inventory_item_code
            )
            and not exists (
                -- There should only be a crop or a livestock, not both.
                -- But this will make sure you can still delete one of them
                -- if both are created by accident.
                select null
                from farms.farm_agristabilty_cmmdty_xref x2
                where x2.inventory_item_code = x.inventory_item_code
                and x2.inventory_class_code in ('1', '2')
                and x2.inventory_class_code != x.inventory_class_code
            )
        )
        then 1
        else 0
    end) into v_in_use;

    return v_in_use;
end;
$$;
