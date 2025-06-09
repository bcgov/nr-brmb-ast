create or replace function farms_codes_read_pkg.read_inventory_xrefs(
    in in_inventory_class_code farms.agristabilty_commodity_xref.inventory_class_code%type,
    in in_inventory_item_code farms.inventory_item_code.inventory_item_code%type,
    in in_xref_id farms.agristabilty_commodity_xref.agristabilty_commodity_xref_id%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select x.agristabilty_commodity_xref_id,
               x.inventory_item_code,
               iic.description inventory_item_code_desc,
               x.market_commodity_indicator,
               x.inventory_class_code,
               icc.description inventory_class_code_desc,
               x.inventory_group_code,
               igc.description inventory_group_code_desc,
               x.revision_count
        from farms.agristabilty_commodity_xref x
        join farms.inventory_item_code iic on iic.inventory_item_code = x.inventory_item_code
        join farms.inventory_class_code icc on icc.inventory_class_code = x.inventory_class_code
        left outer join farms.inventory_group_code igc on igc.inventory_group_code = x.inventory_group_code
        where (in_inventory_class_code is null or x.inventory_class_code = in_inventory_class_code)
        and (in_inventory_item_code is null or x.inventory_item_code = in_inventory_item_code)
        and (in_xref_id is null or x.agristabilty_commodity_xref_id = in_xref_id)
        order by lower(iic.description),
                 x.agristabilty_commodity_xref_id;
    return cur;
end;
$$;
