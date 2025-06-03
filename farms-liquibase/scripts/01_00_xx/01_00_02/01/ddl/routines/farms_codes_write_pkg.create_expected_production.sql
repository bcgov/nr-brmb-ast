create or replace procedure farms_codes_write_pkg.create_expected_production(
   in in_crop_unit_code farms.expected_production.crop_unit_code%type,
   in in_inventory_code farms.expected_production.inventory_item_code%type,
   in in_expected_production_value farms.expected_production.expected_production_per_productive_unit%type,
   in in_farm_user farms.expected_production.update_user%type
)
language plpgsql
as $$
begin

    insert into farms.expected_production (
        expected_production_id,
        expected_production_per_productive_unit,
        inventory_item_code,
        crop_unit_code,
        revision_count,
        create_user,
        create_date,
        update_user,
        update_date
    ) values (
        nextval('farms.seq_ep'),
        in_expected_production_value,
        in_inventory_code,
        in_crop_unit_code,
        1,
        in_farm_user,
        current_timestamp,
        in_farm_user,
        current_timestamp
    );

end;
$$;
