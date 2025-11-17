create or replace function farms_export_pkg.f25()
returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        select x.inventory_item_code inventory_code,
               iic.description inventory_desc,
               x.inventory_class_code inventory_type,
               icc.description inventory_type_desc,
               x.inventory_group_code,
               igc.description inventory_group_desc
        from farms.farm_agristabilty_cmmdty_xref x
        join farms.farm_inventory_item_codes iic on iic.inventory_item_code = x.inventory_item_code
        join farms.farm_inventory_class_codes icc on icc.inventory_class_code = x.inventory_class_code
        left outer join farms.farm_inventory_group_codes igc on igc.inventory_group_code = x.inventory_group_code
        where (iic.expiry_date > current_timestamp or coalesce(iic.expiry_date::text, '') = '')
        and (icc.expiry_date > current_timestamp or coalesce(icc.expiry_date::text, '') = '')
        and (igc.expiry_date > current_timestamp or coalesce(igc.expiry_date::text, '') = '');

    return cur;
end;
$$;
