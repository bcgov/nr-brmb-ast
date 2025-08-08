create or replace procedure farms_codes_write_pkg.create_crop_unit_default(
   in in_inventory_item_code farms.farm_crop_unit_defaults.inventory_item_code%type,
   in in_crop_unit_code farms.farm_crop_unit_defaults.crop_unit_code%type,
   in in_user farms.farm_crop_unit_defaults.who_updated%type
)
language plpgsql
as $$
begin

    insert into farms.farm_crop_unit_defaults (
        crop_unit_default_id,
        inventory_item_code,
        crop_unit_code,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        nextval('farms.seq_cud'),
        in_inventory_item_code,
        in_crop_unit_code,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );

end;
$$;
