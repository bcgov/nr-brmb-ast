create or replace procedure farms_codes_write_pkg.delete_fruit_veg_code(
   in in_fruit_veg_code farms.fruit_vegetable_type_code.fruit_vegetable_type_code%type
)
language plpgsql
as $$
begin

    delete from farms.fruit_vegetable_type_detail c
    where c.fruit_vegetable_type_code = in_fruit_veg_code;

    delete from farms.fruit_vegetable_type_code c
    where c.fruit_vegetable_type_code = in_fruit_veg_code;

end;
$$;
