create or replace procedure farms_codes_write_pkg.delete_crop_unit_default(
   in in_inventory_item_code farms.crop_unit_default.inventory_item_code%type
)
language plpgsql
as $$
begin

    delete from farms.crop_unit_default cud
    where cud.inventory_item_code = in_inventory_item_code;

end;
$$;
