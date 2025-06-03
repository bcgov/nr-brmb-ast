create or replace procedure farms_codes_write_pkg.update_fruit_veg_code(
   in in_fruit_veg_code farms.fruit_vegetable_type_code.fruit_vegetable_type_code%type,
   in in_fruit_veg_code_description farms.fruit_vegetable_type_code.description%type,
   in in_fruit_veg_code_variance_limit farms.fruit_vegetable_type_detail.revenue_variance_limit%type,
   in in_farm_user farms.fruit_vegetable_type_code.update_user%type
)
language plpgsql
as $$
begin

    update farms.fruit_vegetable_type_code c
    set c.description = in_fruit_veg_code_description,
        c.update_user = in_farm_user
    where c.fruit_vegetable_type_code = in_fruit_veg_code;

    update farms.fruit_vegetable_type_detail c
    set c.revenue_variance_limit = in_fruit_veg_code_variance_limit,
        c.update_user = in_farm_user,
        c.revision_count = c.revision_count + 1
    where c.fruit_vegetable_type_code = in_fruit_veg_code;

end;
$$;
