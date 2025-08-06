create or replace function farms_codes_read_pkg.read_crop_unit_conversions(
    in in_inventory_item_code farms.farm_fair_market_values.inventory_item_code%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select iic.inventory_item_code,
               dcu.crop_unit_code default_crop_unit_code,
               iic.description inventory_item_description,
               dcu.description default_crop_unit_description,
               cud.revision_count default_crop_revision_count,
               tcu.crop_unit_code target_crop_unit_code,
               tcu.description target_crop_unit_description,
               cf.conversion_factor
        from farms.farm_crop_unit_defaults cud
        join farms.farm_inventory_item_codes iic on iic.inventory_item_code = cud.inventory_item_code
        left outer join farms.farm_crop_unit_codes dcu on dcu.crop_unit_code = cud.crop_unit_code
        left outer join farms.farm_crop_unit_conversn_fctrs cf on cf.inventory_item_code = cud.inventory_item_code
        left outer join farms.farm_crop_unit_codes tcu on tcu.crop_unit_code = cf.target_crop_unit_code
        where (in_inventory_item_code is null or cud.inventory_item_code = in_inventory_item_code)
        /* Read relies on this ordering to construct FMV Conversion objects. */
        order by lower(iic.description),
                 iic.inventory_item_code,
                 lower(tcu.description),
                 tcu.crop_unit_code;
    return cur;
end;
$$;
