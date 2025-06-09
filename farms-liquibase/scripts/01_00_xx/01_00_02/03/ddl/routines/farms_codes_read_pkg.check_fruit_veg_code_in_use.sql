create or replace function farms_codes_read_pkg.check_fruit_veg_code_in_use(
    in in_fruit_vegetable_type_code farms.fruit_vegetable_type_code.fruit_vegetable_type_code%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        select t.fruit_vegetable_type_code,
               c.fruit_vegetable_type_code
        from farms.line_item t
        full outer join farms.inventory_item_detail c on t.fruit_vegetable_type_code = c.fruit_vegetable_type_code
        where t.fruit_vegetable_type_code = in_fruit_vegetable_type_code
        or c.fruit_vegetable_type_code = in_fruit_vegetable_type_code;
    return cur;

end;
$$;
