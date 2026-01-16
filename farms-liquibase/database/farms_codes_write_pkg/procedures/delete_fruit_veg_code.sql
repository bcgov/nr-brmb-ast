create or replace procedure farms_codes_write_pkg.delete_fruit_veg_code(
   in in_fruit_veg_code farms.farm_fruit_veg_type_codes.fruit_veg_type_code%type
)
language plpgsql
as $$
begin

    delete from farms.farm_fruit_veg_type_details c
    where c.fruit_veg_type_code = in_fruit_veg_code;

    delete from farms.farm_fruit_veg_type_codes c
    where c.fruit_veg_type_code = in_fruit_veg_code;

end;
$$;
