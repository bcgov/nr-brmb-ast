create or replace procedure farms_codes_write_pkg.delete_crop_unit_convrsn_factr(
   in in_inventory_item_code farms.crop_unit_conversion_factor.inventory_item_code%type,
   in in_target_crop_unit_code farms.crop_unit_conversion_factor.target_crop_unit_code%type
)
language plpgsql
as $$
begin

    delete from farms.crop_unit_conversion_factor cf
    where cf.inventory_item_code = in_inventory_item_code
    and (in_target_crop_unit_code is null
        or cf.target_crop_unit_code = in_target_crop_unit_code);

end;
$$;
