create or replace function farms_tip_user_pkg.read_tip_grouping_config()
returns refcursor
language plpgsql
as $$
declare

    cur refcursor;

begin

    open cur for

      select ranges.farm_type_3_name,
             ranges.farm_type_2_name,
             ranges.farm_type_1_name,
             ranges.range_low,
             ranges.range_high,
             ranges.minimum_group_count,
             ranges.ranges_inherited_from
      from (
        select 'Default' farm_type_3_name,
               'Default' farm_type_2_name,
               'Default' farm_type_1_name,
               tir.range_low,
               tir.range_high,
               tir.minimum_group_count,
               null ranges_inherited_from,
               0 defaults_first
        from farms.farm_tip_income_ranges tir
        where coalesce(tir.tip_farm_type_3_lookup_id::text, '') = ''
        and coalesce(tir.tip_farm_type_2_lookup_id::text, '') = ''
        and coalesce(tir.tip_farm_type_1_lookup_id::text, '') = '' 
        
union all

        select tirv.farm_type_3_name,
               tirv.farm_type_2_name,
               tirv.farm_type_1_name,
               tirv.range_low,
               tirv.range_high,
               tirv.minimum_group_count,
               tirv.inherited_from ranges_inherited_from,
               1 defaults_first
        from farms.farm_tip_income_ranges_vw tirv
      ) ranges 
      order by defaults_first,
               farm_type_3_name,
               farm_type_2_name nulls first,
               farm_type_1_name nulls first,
               range_low;

    return cur;

end;
$$;
