create or replace procedure farms_codes_write_pkg.update_fruit_veg_code(
   in in_fruit_veg_code farms.farm_fruit_veg_type_codes.fruit_veg_type_code%type,
   in in_fruit_veg_code_description farms.farm_fruit_veg_type_codes.description%type,
   in in_fruit_veg_code_variance_limit farms.farm_fruit_veg_type_details.revenue_variance_limit%type,
   in in_farm_user farms.farm_fruit_veg_type_codes.who_updated%type
)
language plpgsql
as $$
begin

    update farms.farm_fruit_veg_type_codes c
    set c.description = in_fruit_veg_code_description,
        c.who_updated = in_farm_user
    where c.fruit_veg_type_code = in_fruit_veg_code;

    update farms.farm_fruit_veg_type_details c
    set c.revenue_variance_limit = in_fruit_veg_code_variance_limit,
        c.who_updated = in_farm_user,
        c.revision_count = c.revision_count + 1
    where c.fruit_veg_type_code = in_fruit_veg_code;

end;
$$;
