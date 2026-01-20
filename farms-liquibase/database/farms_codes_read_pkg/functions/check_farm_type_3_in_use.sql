create or replace function farms_codes_read_pkg.check_farm_type_3_in_use(
    in in_farm_type_item_id farms.farm_tip_farm_type_3_lookups.tip_farm_type_3_lookup_id%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        select t.tip_farm_type_2_lookup_id
        from farms.farm_tip_farm_type_2_lookups t
        where t.tip_farm_type_3_lookup_id = in_farm_type_item_id;
    return cur;

end;
$$;
