create or replace function farms_codes_read_pkg.read_expected_production_items(
    in in_expected_production_id farms.farm_expected_productions.expected_production_id%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        select ep.expected_production_id,
               ep.expected_prodctn_per_prod_unit,
               ep.inventory_item_code,
               ep.crop_unit_code,
               ep.revision_count,
               ep.who_created,
               ep.when_created,
               ep.who_updated,
               ep.when_updated,
               iic.description as inventory_desc,
               cuc.description as crop_description
        from farms.farm_expected_productions ep
        join farms.farm_inventory_item_codes iic on ep.inventory_item_code = iic.inventory_item_code
        join farms.farm_crop_unit_codes cuc on ep.crop_unit_code = cuc.crop_unit_code
        where (in_expected_production_id is null or ep.expected_production_id = in_expected_production_id)
        order by lower(iic.description),
                 ep.inventory_item_code,
                 lower(cuc.description),
                 ep.crop_unit_code;
    return cur;

end;
$$;
