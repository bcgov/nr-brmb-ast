create or replace function farms_codes_read_pkg.read_farm_type_default_income_range()
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        select tir.range_low,
               tir.range_high,
               tir.minimum_group_count,
               tir.tip_farm_type_3_lookup_id,
               tir.tip_farm_type_2_lookup_id,
               tir.tip_farm_type_1_lookup_id
        from farms.farm_tip_income_ranges tir
        where tir.tip_farm_type_3_lookup_id is null
        and tir.tip_farm_type_2_lookup_id is null
        and tir.tip_farm_type_1_lookup_id is null
        order by tir.range_low;
    return cur;

end;
$$;
