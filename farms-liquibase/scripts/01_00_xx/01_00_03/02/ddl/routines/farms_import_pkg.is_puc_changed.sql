create or replace function farms_import_pkg.is_puc_changed(
    in in_program_year_id farms.program_year.program_year_id%type,
    in in_program_year_vrsn_prev_id farms.program_year_version.program_year_version_id%type
)
returns boolean
language plpgsql
as $$
declare
    Unknown constant varchar := '-1';
    cnt numeric := null;
begin
    with file_21 as (
        -- current crop productive units
        select coalesce(z21.crop_on_farm_acres, 0) + coalesce(z21.crop_unseedable_acres, 0) as productive_capacity_amount,
               (case
                   when iic.inventory_item_code is not null then null
                   else iic.inventory_item_code
               end) import_comment,
               (case
                   when iic.inventory_item_code is not null then null
                   else Unknown
               end) structure_group_code,
               iic.inventory_item_code,
               z21.operation_number
        from farms.z21_participant_supplementary z21
        join farms.agristability_client ac on z21.participant_pin = ac.participant_pin
        join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
                                    and z21.program_year = py.year
        left outer join farms.inventory_item_code iic on iic.inventory_item_code = z21.inventory_code::varchar
        where z21.inventory_type_code = '1'
        and (z21.crop_on_farm_acres is not null or z21.crop_unseedable_acres is not null)
        and py.program_year_id = in_program_year_id
    ), file_23 as (
        -- current livestock productive units
        select z23.productive_capacity_amount,
               (case
                   when sgc.structure_group_code is not null then null
                   else sgc.structure_group_code
               end) import_comment,
               sgc.structure_group_code,
               (case
                   when sgc.structure_group_code is not null then null
                   else Unknown
               end) inventory_item_code,
               z23.operation_number
        from farms.z23_livestock_production_capacity z23
        join farms.agristability_client ac on z23.participant_pin = ac.participant_pin
        join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
                                    and z23.program_year = py.year
        left outer join farms.inventory_item_code iic on iic.inventory_item_code = z23.inventory_code::varchar
        left outer join farms.structure_group_code sgc on sgc.structure_group_code = z23.inventory_code::varchar
        where py.program_year_id = in_program_year_id
    ), file_42 as (
        -- mixed historical productive units
        select coalesce(z42.productive_capacity_units, 0) as productive_capacity_amount,
               (case
                   when z42.productive_type_code = 2 then coalesce(sgc.structure_group_code, Unknown)
                   else null
               end) structure_group_code,
               (case
                   when z42.productive_type_code = 1 then coalesce(iic.inventory_item_code, Unknown)
                   else null
               end) inventory_item_code,
               (case
                   when z42.productive_type_code = 2 then
                       (case when sgc.structure_group_code is not null then null else z42.productive_code::varchar end)
                   when z42.productive_type_code = 1 then
                       (case when iic.inventory_item_code is not null then null else z42.productive_code::varchar end)
                   else null
               end) import_comment,
               z42.operation_number
        from farms.z42_participant_reference_year z42
        join farms.agristability_client ac on z42.participant_pin = ac.participant_pin
        join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
                                    and z42.program_year = py.year
        left outer join farms.structure_group_code sgc on sgc.structure_group_code = z42.productive_code::varchar
        left outer join farms.inventory_item_code iic on iic.inventory_item_code = z42.productive_code::varchar
        where py.program_year_id = in_program_year_id
    ), file_40_aarm as (
        -- historical AARM crops productive capacity
        select (case
                   when max(z40.prior_year_supplemental_key) is not null and aarm.inventory_type_code in ('1') then
                       sum(z40.crop_on_farm_acres)
                   else
                       null
               end) as productive_capacity_amount,
               null structure_group_code,
               aarm.inventory_code::varchar as inventory_item_code,
               aarm.operation_number,
               (case
                   when max(x.prior_year_supplemental_key) is not null then null
                   else aarm.inventory_code
               end) import_comment
        from farms.aarm_margin aarm
        left outer join farms.z40_participant_reference_supplemental_detail z40 on aarm.participant_pin = z40.participant_pin
                                                                                and aarm.program_year = z40.program_year
                                                                                and aarm.operation_number = z40.operation_number
                                                                                and aarm.inventory_type_code = z40.inventory_type_code
                                                                                and aarm.inventory_code = z40.inventory_code
                                                                                and (aarm.production_unit = z40.production_unit
                                                                                    or (aarm.production_unit is null and z40.production_unit is null))
                                                                                and (z40.prior_year_supplemental_key is null
                                                                                    or aarm.inventory_type_code in ('1', '2')
                                                                                    or (aarm.inventory_type_code in ('3', '4', '5')
                                                                                        and (aarm.quantity_start > 0 or aarm.quantity_end > 0)))
        left outer join farms.agristabilty_commodity_xref x on aarm.inventory_code::varchar = x.inventory_item_code
                                                            and aarm.inventory_type_code::varchar = x.inventory_class_code
        left outer join farms.agristabilty_commodity_xref x2 on x2.inventory_class_code = aarm.inventory_type_code::varchar
                                                             and x2.inventory_item_code = Unknown
        join farms.agristability_client ac on aarm.participant_pin = ac.participant_pin
        join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
                                    and aarm.program_year = py.year
        where z40.crop_on_farm_acres is not null
        and py.program_year_id = in_program_year_id
        group by -- can be simplified with a parent id + inv code/type
                 aarm.aarm_margin_id, -- included to ensure every row is included
                 aarm.participant_pin,
                 aarm.program_year,
                 aarm.operation_number,
                 aarm.inventory_type_code,
                 aarm.inventory_code,
                 aarm.production_unit
    ), file_40_raw as (
        -- historical AARM crops productive capacity
        select zz.crop_on_farm_acres productive_capacity_amount,
               null structure_group_code,
               coalesce(iic.inventory_item_code, Unknown) as inventory_item_code,
               (case
                   when iic.inventory_item_code is not null then null
                   else zz.inventory_code::varchar
               end) import_comment,
               zz.operation_number
        from (
            select z40.*
            from farms.z40_participant_reference_supplemental_detail z40
            left outer join farms.aarm_margin aarm on aarm.participant_pin = z40.participant_pin
                                                  and aarm.program_year = z40.program_year
                                                  and aarm.operation_number = z40.operation_number
                                                  and aarm.inventory_type_code = z40.inventory_type_code
                                                  and aarm.inventory_code = z40.inventory_code
                                                  and (aarm.production_unit = z40.production_unit
                                                      or (aarm.production_unit is null and z40.production_unit is null))
            where aarm.aarm_margin_id is null
        ) zz
        join farms.agristability_client ac on zz.participant_pin = ac.participant_pin
        join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
                                    and zz.program_year = py.year
        left outer join farms.inventory_item_code iic on iic.inventory_item_code = zz.inventory_code::varchar
        where zz.crop_on_farm_acres is not null
        and py.program_year_id = in_program_year_id
    ), new_values as (
        select productive_capacity_amount,
               import_comment,
               structure_group_code,
               inventory_item_code,
               operation_number,
               row_number() over (partition by productive_capacity_amount, import_comment,
               structure_group_code, inventory_item_code, operation_number order by 1) rn
        from (
            select sum(productive_capacity_amount) productive_capacity_amount,
                   import_comment,
                   structure_group_code,
                   inventory_item_code,
                   operation_number
            from file_21
            group by operation_number,
                     structure_group_code,
                     inventory_item_code,
                     import_comment
            union all
            select sum(productive_capacity_amount) productive_capacity_amount,
                   import_comment,
                   structure_group_code,
                   inventory_item_code,
                   operation_number
            from file_23
            group by operation_number,
                     structure_group_code,
                     inventory_item_code,
                     import_comment
            union all
            select sum(case
                       when a.operation_number is not null then a.productive_capacity_amount
                       when b.operation_number is not null then b.productive_capacity_amount
                       else c.productive_capacity_amount
                   end) productive_capacity_amount,
                   string_agg(case
                       when a.operation_number is not null then a.import_comment
                       when b.operation_number is not null then b.import_comment
                       else c.import_comment
                   end, ' ' order by case
                       when a.operation_number is not null then a.operation_number
                       when b.operation_number is not null then b.operation_number
                       else c.operation_number
                   end) import_comment,
                   (case
                       when a.operation_number is not null then a.structure_group_code
                       when b.operation_number is not null then b.structure_group_code
                       else c.structure_group_code
                   end) structure_group_code,
                   (case
                       when a.operation_number is not null then a.inventory_item_code
                       when b.operation_number is not null then b.inventory_item_code
                       else c.inventory_item_code
                   end) inventory_item_code,
                   coalesce(a.operation_number, b.operation_number, c.operation_number) operation_number
            from file_42 a
            full join file_40_aarm b on a.inventory_item_code = b.inventory_item_code
            full join file_40_raw c on a.inventory_item_code = c.inventory_item_code
            group by coalesce(a.operation_number, b.operation_number, c.operation_number),
                     (case
                         when a.operation_number is not null then a.structure_group_code
                         when b.operation_number is not null then b.structure_group_code
                         else c.structure_group_code
                     end),
                     (case
                         when a.operation_number is not null then a.inventory_item_code
                         when b.operation_number is not null then b.inventory_item_code
                         else c.inventory_item_code
                     end)
        ) t1
    ), cur_values as (
        select puc.productive_capacity_amount,
               puc.import_comment,
               puc.structure_group_code,
               puc.inventory_item_code,
               m.operation_number,
               row_number() over (partition by productive_capacity_amount, import_comment,
               structure_group_code, inventory_item_code, m.operation_number order by 1) rn
        from farms.operations_vw m
        join farms.farming_operation op on op.farming_operation_id = m.farming_operation_id
        join farms.productive_unit_capacity puc on puc.farming_operation_id = m.farming_operation_id
        where m.program_year_version_id = in_program_year_vrsn_prev_id
        and op.locally_generated_indicator = 'N'
        and puc.agristability_scenario_id is null
    )
    select count(*)
    into cnt
    from (
        (
            select *
            from new_values
            except
            select *
            from cur_values
        )
        union all
        (
            select *
            from cur_values
            except
            select *
            from new_values
        )
    ) t2;

    if cnt > 0 then
        return true;
    end if;

    return false;
end;
$$;
