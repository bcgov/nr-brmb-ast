create or replace procedure farms_codes_write_pkg.update_farm_type_3(
   in in_farm_type_3_id tip_farm_type_3_lookup.tip_farm_type_3_lookup_id%type,
   in in_farm_type_name tip_farm_type_3_lookup.farm_type_3_name%type,
   in in_farm_user tip_farm_type_3_lookup.update_user%type
)
language plpgsql
as $$
begin

    delete from farms.tip_income_range tir
    where tir.tip_farm_type_3_lookup_id = in_farm_type_3_id;

    update farms.tip_farm_type_3_lookup
    set farm_type_3_name = in_farm_type_name,
        update_user = in_farm_user,
        update_date = current_timestamp,
        revision_count = revision_count + 1
    where tip_farm_type_3_lookup_id = in_farm_type_3_id;

end;
$$;
