create or replace procedure farms_codes_write_pkg.create_farm_type_2(
   inout in_farm_type_2_id farms.tip_farm_type_2_lookup.tip_farm_type_2_lookup_id%type,
   in in_farm_type_3_id farms.tip_farm_type_3_lookup.tip_farm_type_3_lookup_id%type,
   in in_farm_type_name farms.tip_farm_type_2_lookup.farm_type_2_name%type,
   in in_farm_user farms.tip_farm_type_2_lookup.update_user%type
)
language plpgsql
as $$
begin

    insert into farms.tip_farm_type_2_lookup (
        tip_farm_type_2_lookup_id,
        farm_type_2_name,
        tip_farm_type_3_lookup_id,
        create_user,
        create_date,
        update_user,
        update_date,
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
