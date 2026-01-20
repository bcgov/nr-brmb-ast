create or replace function farms_codes_read_pkg.read_farm_type_1_codes()
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        select t.farm_type_1_name,
               t.when_created,
               t.revision_count,
               t.tip_farm_type_1_lookup_id,
               t.tip_farm_type_2_lookup_id,
               tft2l.farm_type_2_name,
               tft3l.tip_farm_type_3_lookup_id,
               tft3l.farm_type_3_name
        from farms.farm_tip_farm_type_1_lookups t
        join farms.farm_tip_farm_type_2_lookups tft2l on tft2l.tip_farm_type_2_lookup_id = t.tip_farm_type_2_lookup_id
        join farms.farm_tip_farm_type_3_lookups tft3l on tft3l.tip_farm_type_3_lookup_id = t.tip_farm_type_3_lookup_id
        order by lower(t.farm_type_1_name);
    return cur;

end;
$$;
