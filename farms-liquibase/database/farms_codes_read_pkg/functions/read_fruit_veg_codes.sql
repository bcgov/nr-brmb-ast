create or replace function farms_codes_read_pkg.read_fruit_veg_codes()
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        select t.fruit_veg_type_code,
               t.description,
               t.established_date,
               t.expiry_date,
               t.revision_count,
               c.revenue_variance_limit
        from farms.farm_fruit_veg_type_codes t
        join farms.farm_fruit_veg_type_details c on t.fruit_veg_type_code = c.fruit_veg_type_code
        order by lower(t.fruit_veg_type_code);
    return cur;

end;
$$;
