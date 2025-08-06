create or replace procedure farms_codes_write_pkg.create_income_range(
   in in_farm_type_3_id farms.farm_tip_income_ranges.tip_farm_type_3_lookup_id%type,
   in in_farm_type_2_id farms.farm_tip_income_ranges.tip_farm_type_2_lookup_id%type,
   in in_farm_type_1_id farms.farm_tip_income_ranges.tip_farm_type_1_lookup_id%type,
   in in_range_high farms.farm_tip_income_ranges.range_high%type,
   in in_range_low farms.farm_tip_income_ranges.range_low%type,
   in in_mimimum_group_count farms.farm_tip_income_ranges.minimum_group_count%type,
   in in_farm_user farms.farm_tip_income_ranges.who_created%type
)
language plpgsql
as $$
begin

    insert into farms.farm_tip_income_ranges (
        tip_income_range_id,
        tip_farm_type_3_lookup_id,
        tip_farm_type_2_lookup_id,
        tip_farm_type_1_lookup_id,
        range_high,
        range_low,
        minimum_group_count,
        who_created,
        when_created,
        who_updated,
        when_updated,
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
