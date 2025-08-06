create or replace procedure farms_codes_write_pkg.create_farm_type_3(
   inout in_farm_type_3_id tip_farm_type_3_lookup.tip_farm_type_3_lookup_id%type,
   in in_farm_type_3_name tip_farm_type_3_lookup.farm_type_3_name%type,
   in in_farm_user tip_farm_type_3_lookup.who_updated%type
)
language plpgsql
as $$
begin

    insert into farms.farm_tip_farm_type_3_lookups (
        tip_farm_type_3_lookup_id,
        farm_type_3_name,
        who_created,
        when_created,
        who_updated,
        when_updated,
        revision_count
    ) values (
        nextval('farms.seq_tft3l'),
        in_farm_type_3_name,
        in_farm_user,
        current_timestamp,
        in_farm_user,
        current_timestamp,
        1
    ) returning tip_farm_type_3_lookup_id into in_farm_type_3_id;
end;
$$;
