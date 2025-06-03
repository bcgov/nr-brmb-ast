create or replace procedure farms_codes_write_pkg.create_farm_type_3(
   inout in_farm_type_3_id tip_farm_type_3_lookup.tip_farm_type_3_lookup_id%type,
   in in_farm_type_3_name tip_farm_type_3_lookup.farm_type_3_name%type,
   in in_farm_user tip_farm_type_3_lookup.update_user%type
)
language plpgsql
as $$
begin

    insert into farms.tip_farm_type_3_lookup (
        tip_farm_type_3_lookup_id,
        farm_type_3_name,
        create_user,
        create_date,
        update_user,
        update_date,
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
