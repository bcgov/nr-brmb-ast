create or replace view farms.farm_tip_income_ranges_vw as
with type1 as (
  select ft3.farm_type_3_name,
         ft2.farm_type_2_name,
         ft1.farm_type_1_name,
         coalesce(ft1ir.range_low, coalesce(ft2ir.range_low, coalesce(ft3ir.range_low, ftdir.range_low)))::numeric range_low,
         coalesce(ft1ir.range_high, coalesce(ft2ir.range_high, coalesce(ft3ir.range_high, ftdir.range_high)))::numeric range_high,
         coalesce(ft1ir.minimum_group_count, coalesce(ft2ir.minimum_group_count, coalesce(ft3ir.minimum_group_count, ftdir.minimum_group_count)))::numeric minimum_group_count,
         case when ft1ir.tip_income_range_id is not null then null
              when ft2ir.tip_income_range_id is not null then 'type 2: '||ft2.farm_type_2_name
              when ft3ir.tip_income_range_id is not null then 'type 3: '||ft3.farm_type_3_name
              else 'default'
           end inherited_from
  from farm_tip_farm_type_1_lookups ft1
  join farm_tip_farm_type_2_lookups ft2 on ft2.tip_farm_type_2_lookup_id = ft1.tip_farm_type_2_lookup_id
  join farm_tip_farm_type_3_lookups ft3 on ft3.tip_farm_type_3_lookup_id = ft2.tip_farm_type_3_lookup_id
  left outer join farm_tip_income_ranges ft1ir on ft1ir.tip_farm_type_1_lookup_id = ft1.tip_farm_type_1_lookup_id
  left outer join farm_tip_income_ranges ft2ir on ft2ir.tip_farm_type_2_lookup_id = ft2.tip_farm_type_2_lookup_id
                                              and ft1ir.tip_farm_type_1_lookup_id is null
  left outer join farm_tip_income_ranges ft3ir on ft3ir.tip_farm_type_3_lookup_id = ft3.tip_farm_type_3_lookup_id
                                              and ft1ir.tip_farm_type_1_lookup_id is null
                                              and ft2ir.tip_farm_type_2_lookup_id is null
  left outer join farm_tip_income_ranges ftdir on ftdir.tip_farm_type_1_lookup_id is null
                                              and ftdir.tip_farm_type_2_lookup_id is null
                                              and ftdir.tip_farm_type_3_lookup_id is null
                                              and ft1ir.tip_farm_type_1_lookup_id is null
                                              and ft2ir.tip_farm_type_2_lookup_id is null
                                              and ft3ir.tip_farm_type_3_lookup_id is null
),
type2 as (
  select ft3.farm_type_3_name,
         ft2.farm_type_2_name,
         null farm_type_1_name,
         coalesce(ft2ir.range_low, coalesce(ft3ir.range_low, ftdir.range_low)) range_low,
         coalesce(ft2ir.range_high, coalesce(ft3ir.range_high, ftdir.range_high)) range_high,
         coalesce(ft2ir.minimum_group_count, coalesce(ft3ir.minimum_group_count, ftdir.minimum_group_count)) minimum_group_count,
         case when ft2ir.tip_income_range_id is not null then null
              when ft3ir.tip_income_range_id is not null then 'type 3: '||ft3.farm_type_3_name
              else 'default'
           end inherited_from
  from farm_tip_farm_type_2_lookups ft2
  join farm_tip_farm_type_3_lookups ft3 on ft3.tip_farm_type_3_lookup_id = ft2.tip_farm_type_3_lookup_id
  left outer join farm_tip_income_ranges ft2ir on ft2ir.tip_farm_type_2_lookup_id = ft2.tip_farm_type_2_lookup_id
  left outer join farm_tip_income_ranges ft3ir on ft3ir.tip_farm_type_3_lookup_id = ft3.tip_farm_type_3_lookup_id
                                              and ft2ir.tip_farm_type_2_lookup_id is null
  left outer join farm_tip_income_ranges ftdir on ftdir.tip_farm_type_1_lookup_id is null
                                              and ftdir.tip_farm_type_2_lookup_id is null
                                              and ftdir.tip_farm_type_3_lookup_id is null
                                              and ft2ir.tip_farm_type_2_lookup_id is null
                                              and ft3ir.tip_farm_type_3_lookup_id is null
),
type3 as (
  select ft3.farm_type_3_name,
         null farm_type_2_name,
         null farm_type_1_name,
         coalesce(ft3ir.range_low, ftdir.range_low) range_low,
         coalesce(ft3ir.range_high, ftdir.range_high) range_high,
         coalesce(ft3ir.minimum_group_count, ftdir.minimum_group_count) minimum_group_count,
         case when ft3ir.tip_income_range_id is not null then null
              else 'default'
           end inherited_from
  from farm_tip_farm_type_3_lookups ft3
  left outer join farm_tip_income_ranges ft3ir on ft3ir.tip_farm_type_3_lookup_id = ft3.tip_farm_type_3_lookup_id
  left outer join farm_tip_income_ranges ftdir on ftdir.tip_farm_type_1_lookup_id is null
                                              and ftdir.tip_farm_type_2_lookup_id is null
                                              and ftdir.tip_farm_type_3_lookup_id is null
                                              and ft3ir.tip_farm_type_3_lookup_id is null
)
select farm_type_3_name, farm_type_2_name, farm_type_1_name, range_low, range_high, minimum_group_count, inherited_from from type3
union all
select farm_type_3_name, farm_type_2_name, farm_type_1_name, range_low, range_high, minimum_group_count, inherited_from from type2
union all
select farm_type_3_name, farm_type_2_name, farm_type_1_name, range_low, range_high, minimum_group_count, inherited_from from type1
order by 1, 2, 3, 4;
