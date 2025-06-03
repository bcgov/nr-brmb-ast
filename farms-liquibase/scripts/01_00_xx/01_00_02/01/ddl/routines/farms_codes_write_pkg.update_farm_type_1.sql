create or replace procedure farms_codes_write_pkg.update_farm_type_1(
   in in_farm_type_name farms.tip_farm_type_1_lookup.farm_type_1_name%type,
   in in_farm_type_2_id farms.tip_farm_type_1_lookup.tip_farm_type_2_lookup_id%type,
   in in_farm_user farms.tip_farm_type_1_lookup.update_user%type,
   in in_farm_type_id farms.tip_farm_type_1_lookup.tip_farm_type_1_lookup_id%type
)
language plpgsql
as $$
begin

    delete from farms.tip_income_range tir
    where tir.tip_farm_type_1_lookup_id = in_farm_type_id;

    update farms.tip_farm_type_1_lookup
    set farm_type_1_name = in_farm_type_name,
        tip_farm_type_2_lookup_id = in_farm_type_2_id,
        update_user = in_farm_user,
        update_date = current_timestamp,
        revision_count = revision_count + 1
    where tip_farm_type_1_lookup_id = in_farm_type_id;

end;
$$;
