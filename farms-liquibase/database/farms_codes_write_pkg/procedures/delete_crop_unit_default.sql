create or replace procedure farms_codes_write_pkg.delete_crop_unit_default(
   in in_inventory_item_code farms.farm_crop_unit_defaults.inventory_item_code%type
)
language plpgsql
as $$
begin

    delete from farms.farm_crop_unit_defaults cud
    where cud.inventory_item_code = in_inventory_item_code;

end;
$$;
