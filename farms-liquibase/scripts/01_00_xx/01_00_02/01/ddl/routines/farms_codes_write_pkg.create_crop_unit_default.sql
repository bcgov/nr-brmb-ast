create or replace procedure farms_codes_write_pkg.create_crop_unit_default(
   in in_inventory_item_code farms.crop_unit_default.inventory_item_code%type,
   in in_crop_unit_code farms.crop_unit_default.crop_unit_code%type,
   in in_user farms.crop_unit_default.update_user%type
)
language plpgsql
as $$
begin

    insert into farms.crop_unit_default (
        crop_unit_default_id,
        inventory_item_code,
        crop_unit_code,
        revision_count,
        create_user,
        create_date,
        update_user,
        update_date
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
