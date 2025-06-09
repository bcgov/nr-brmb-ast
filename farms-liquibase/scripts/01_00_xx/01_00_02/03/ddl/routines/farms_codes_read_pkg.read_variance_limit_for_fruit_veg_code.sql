create or replace function farms_codes_read_pkg.read_variance_limit_for_fruit_veg_code(
    in in_fruit_vegetable_type_code farms.fruit_vegetable_type_code.fruit_vegetable_type_code%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        select c.revenue_variance_limit
        from farms.fruit_vegetable_type_detail c
        where c.fruit_vegetable_type_code = in_fruit_vegetable_type_code;
    return cur;

end;
$$;
