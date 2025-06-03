create or replace procedure farms_codes_write_pkg.create_income_range(
   in in_farm_type_3_id farms.tip_income_range.tip_farm_type_3_lookup_id%type,
   in in_farm_type_2_id farms.tip_income_range.tip_farm_type_2_lookup_id%type,
   in in_farm_type_1_id farms.tip_income_range.tip_farm_type_1_lookup_id%type,
   in in_range_high farms.tip_income_range.range_high%type,
   in in_range_low farms.tip_income_range.range_low%type,
   in in_mimimum_group_count farms.tip_income_range.minimum_group_count%type,
   in in_farm_user farms.tip_income_range.create_user%type
)
language plpgsql
as $$
begin

    insert into farms.tip_income_range (
        tip_income_range_id,
        tip_farm_type_3_lookup_id,
        tip_farm_type_2_lookup_id,
        tip_farm_type_1_lookup_id,
        range_high,
        range_low,
        minimum_group_count,
        create_user,
        create_date,
        update_user,
        update_date,
        revision_count
    ) values (
        nextval('farms.seq_tir'),
        in_farm_type_3_id,
        in_farm_type_2_id,
        in_farm_type_1_id,
        in_range_high,
        in_range_low,
        in_mimimum_group_count,
        in_farm_user,
        current_timestamp,
        in_farm_user,
        current_timestamp,
        1
    );

end;
$$;
