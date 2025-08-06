create or replace procedure farms_codes_write_pkg.update_farm_type_3(
   in in_farm_type_3_id tip_farm_type_3_lookup.tip_farm_type_3_lookup_id%type,
   in in_farm_type_name tip_farm_type_3_lookup.farm_type_3_name%type,
   in in_farm_user tip_farm_type_3_lookup.who_updated%type
)
language plpgsql
as $$
begin

    delete from farms.farm_tip_income_ranges tir
    where tir.tip_farm_type_3_lookup_id = in_farm_type_3_id;

    update farms.farm_tip_farm_type_3_lookups
    set farm_type_3_name = in_farm_type_name,
        who_updated = in_farm_user,
        when_updated = current_timestamp,
        revision_count = revision_count + 1
    where tip_farm_type_3_lookup_id = in_farm_type_3_id;

end;
$$;
