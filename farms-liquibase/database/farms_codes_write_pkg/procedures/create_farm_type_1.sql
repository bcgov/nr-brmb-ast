create or replace procedure farms_codes_write_pkg.create_farm_type_1(
   inout in_farm_type_1_id farms.farm_tip_farm_type_1_lookups.tip_farm_type_1_lookup_id%type,
   in in_farm_type_name farms.farm_tip_farm_type_1_lookups.farm_type_1_name%type,
   in in_farm_type_2_id farms.farm_tip_farm_type_1_lookups.tip_farm_type_2_lookup_id%type,
   in in_farm_user farms.farm_tip_farm_type_1_lookups.who_updated%type
)
language plpgsql
as $$
begin

    insert into farms.farm_tip_farm_type_1_lookups(
        tip_farm_type_1_lookup_id,
        farm_type_1_name,
        tip_farm_type_2_lookup_id,
        who_created,
        when_created,
        who_updated,
        when_updated,
        revision_count
    ) values (
        nextval('farms.farm_tft1l_seq'),
        in_farm_type_name,
        in_farm_type_2_id,
        in_farm_user,
        current_timestamp,
        in_farm_user,
        current_timestamp,
        1
    ) returning tip_farm_type_1_lookup_id into in_farm_type_1_id;

end;
$$;
