create or replace procedure farms_codes_write_pkg.update_farm_type_2(
   in in_farm_type_name farms.tip_farm_type_2_lookup.farm_type_2_name%type,
   in in_farm_type_id farms.tip_farm_type_2_lookup.tip_farm_type_2_lookup_id%type,
   in in_farm_user farms.tip_farm_type_2_lookup.update_user%type,
   in in_farm_type_3_id farms.tip_farm_type_2_lookup.tip_farm_type_3_lookup_id%type
)
language plpgsql
as $$
begin

    delete from farms.tip_income_range tir
    where tir.tip_farm_type_2_lookup_id = in_farm_type_id;

    update farms.tip_farm_type_2_lookup
    set farm_type_2_name = in_farm_type_name,
        tip_farm_type_3_lookup_id = in_farm_type_3_id,
        update_user = in_farm_user,
        update_date = current_timestamp,
        revision_count = revision_count + 1
    where tip_farm_type_2_lookup_id = in_farm_type_id;

end;
$$;
