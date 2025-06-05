create or replace function farms_codes_read_pkg.read_farm_type_3_codes()
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        select t.tip_farm_type_3_lookup_id,
               t.farm_type_3_name code,
               t.create_date,
               t.revision_count
        from farms.tip_farm_type_3_lookup t
        order by lower(t.farm_type_3_name);
    return cur;

end;
$$;
