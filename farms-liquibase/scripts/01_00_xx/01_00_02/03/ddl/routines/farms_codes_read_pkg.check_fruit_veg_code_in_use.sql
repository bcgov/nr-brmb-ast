create or replace function farms_codes_read_pkg.check_fruit_veg_code_in_use(
    in in_fruit_vegetable_type_code farms.farm_fruit_veg_type_codes.fruit_veg_type_code%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        select t.fruit_veg_type_code,
               c.fruit_veg_type_code
        from farms.farm_line_items t
        full outer join farms.farm_inventory_item_details c on t.fruit_veg_type_code = c.fruit_veg_type_code
        where t.fruit_veg_type_code = in_fruit_vegetable_type_code
        or c.fruit_veg_type_code = in_fruit_vegetable_type_code;
    return cur;

end;
$$;
