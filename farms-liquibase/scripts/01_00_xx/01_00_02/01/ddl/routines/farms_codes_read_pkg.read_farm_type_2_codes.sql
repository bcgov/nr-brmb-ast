create or replace function farms_codes_read_pkg.read_farm_type_2_codes()
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select t.farm_type_2_name,
               t.create_date,
               t.revision_count,
               t.tip_farm_type_2_lookup_id,
               t.tip_farm_type_3_lookup_id,
               tft3l.farm_type_3_name
        from farms.tip_farm_type_2_lookup t
        join farms.tip_farm_type_3_lookup tft3l on tft3l.tip_farm_type_3_lookup_id = t.tip_farm_type_3_lookup_id
        order by lower(t.farm_type_2_name);
    return cur;

end;
$$;
