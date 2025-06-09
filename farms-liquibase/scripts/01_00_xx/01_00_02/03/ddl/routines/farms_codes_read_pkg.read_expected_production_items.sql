create or replace function farms_codes_read_pkg.read_expected_production_items(
    in in_expected_production_id farms.expected_production.expected_production_id%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        select ep.expected_production_id,
               ep.expected_production_per_productive_unit,
               ep.inventory_item_code,
               ep.crop_unit_code,
               ep.revision_count,
               ep.create_user,
               ep.create_date,
               ep.update_user,
               ep.update_date,
               iic.description as inventory_description,
               cuc.description as crop_description
        from farms.expected_production ep
        join farms.inventory_item_code iic on ep.inventory_item_code = iic.inventory_item_code
        join farms.crop_unit_code cuc on ep.crop_unit_code = cuc.crop_unit_code
        where (in_expected_production_id is null or ep.expected_production_id = in_expected_production_id)
        order by lower(iic.description),
                 ep.inventory_item_code,
                 lower(cuc.description),
                 ep.crop_unit_code;
    return cur;

end;
$$;
