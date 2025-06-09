create or replace procedure farms_codes_write_pkg.create_crop_unit_convrsn_factr(
   in in_inventory_item_code farms.crop_unit_conversion_factor.inventory_item_code%type,
   in in_target_crop_unit_code farms.crop_unit_conversion_factor.target_crop_unit_code%type,
   in in_conversion_factor farms.crop_unit_conversion_factor.conversion_factor%type,
   in in_user farms.crop_unit_conversion_factor.update_user%type
)
language plpgsql
as $$
begin

    insert into farms.crop_unit_conversion_factor (
        crop_unit_conversion_factor_id,
        conversion_factor,
        inventory_item_code,
        target_crop_unit_code,
        revision_count,
        create_user,
        create_date,
        update_user,
        update_date
    ) values (
        nextval('farms.seq_cucf'),
        in_conversion_factor,
        in_inventory_item_code,
        in_target_crop_unit_code,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );

end;
$$;
