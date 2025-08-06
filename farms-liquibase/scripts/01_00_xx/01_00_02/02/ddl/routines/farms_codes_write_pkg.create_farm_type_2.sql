create or replace procedure farms_codes_write_pkg.create_farm_type_2(
   inout in_farm_type_2_id farms.farm_tip_farm_type_2_lookups.tip_farm_type_2_lookup_id%type,
   in in_farm_type_3_id farms.farm_tip_farm_type_3_lookups.tip_farm_type_3_lookup_id%type,
   in in_farm_type_name farms.farm_tip_farm_type_2_lookups.farm_type_2_name%type,
   in in_farm_user farms.farm_tip_farm_type_2_lookups.who_updated%type
)
language plpgsql
as $$
begin

    insert into farms.farm_tip_farm_type_2_lookups (
        tip_farm_type_2_lookup_id,
        farm_type_2_name,
        tip_farm_type_3_lookup_id,
        who_created,
        when_created,
        who_updated,
        when_updated,
        revision_count
    ) values (
        nextval('farms.seq_tft2l'),
        in_farm_type_name,
        in_farm_type_3_id,
        in_farm_user,
        current_timestamp,
        in_farm_user,
        current_timestamp,
        1
    ) returning tip_farm_type_2_lookup_id into in_farm_type_2_id;

end;
$$;
