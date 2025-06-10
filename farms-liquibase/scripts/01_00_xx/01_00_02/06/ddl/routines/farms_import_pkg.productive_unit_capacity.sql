create or replace function farms_import_pkg.productive_unit_capacity(
    in in_program_year_version_id farms.program_year_version.program_year_version_id%type,
    in in_user varchar
)
returns varchar
language plpgsql
as $$
declare
    Unknown constant varchar := '-1';
    puc_insert_cursor cursor for
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
                   iic.inventory_item_code inventory_item_code,
                   op.farming_operation_id
            from farms.z21_participant_supplementary z21
            join farms.agristability_client ac on z21.participant_pin = ac.participant_pin
            join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
                                       and py.year = z21.program_year
            join farms.program_year_version pyv on py.program_year_id = pyv.program_year_id
            join farms.farming_operation op on pyv.program_year_version_id = op.program_year_version_id
                                            and op.operation_number = z21.operation_number
            left outer join farms.inventory_item_code iic on iic.inventory_item_code = to_char(z21.inventory_code)
            where z21.inventory_type_code = '1'
            and (z21.crop_on_farm_acres is not null or z21.crop_unseedable_acres is not null)
            and pyv.program_year_version_id = in_program_year_version_id
        ), file_23 as (
            -- current livestock productive units
            select z23.productive_capacity_amount,
                   (case
                       when sgc.structure_group_code is not null then null
                       else sgc.structure_group_code
                   end) import_comment,
                   sgc.structure_group_code structure_group_code,
                   (case
                       when sgc.structure_group_code is not null then null
                       else Unknown
                   end) inventory_item_code,
                   op.farming_operation_id
            from farms.z23_livestock_production_capacity z23
            join farms.agristability_client ac on z23.participant_pin = ac.participant_pin
            join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
                                       and py.year = z23.program_year
            join farms.program_year_version pyv on py.program_year_id = pyv.program_year_id
            join farms.farming_operation op on pyv.program_year_version_id = op.program_year_version_id
                                            and op.operation_number = z23.operation_number
            left outer join farms.structure_group_code sgc on sgc.structure_group_code = to_char(z23.inventory_code)
            where pyv.program_year_version_id = in_program_year_version_id
        ), file_42 as (
            -- mixed historical productive units
            select coalesce(z42.productive_capacity_units, 0) productive_capacity_amount,
                   (case
                       when z42.productive_type_code = 2 then
                           coalesce(sgc.structure_group_code, Unknown)
                       else null
                   end) structure_group_code,
                   (case
                       when z42.productive_type_code = 1 then
                           coalesce(iic.inventory_item_code, Unknown)
                       else null
                   end) inventory_item_code,
                   (case
                       when z42.productive_type_code = 2 then
                           (case when sgc.structure_group_code is not null then null else to_char(z42.productive_code) end)
                       when z42.productive_type_code = 1 then
                           (case when iic.inventory_item_code is not null then null else to_char(z42.productive_code) end)
                       else null
                   end) import_comment,
                   op.farming_operation_id
            from farms.z42_participant_reference_year z42
            join farms.agristability_client ac on z42.participant_pin = ac.participant_pin
            join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
                                       and py.year = z42.program_year
            join farms.program_year_version pyv on py.program_year_id = pyv.program_year_id
            join farms.farming_operation op on pyv.program_year_version_id = op.program_year_version_id
                                            and op.operation_number = z42.operation_number
            left outer join farms.structure_group_code sgc on sgc.structure_group_code = to_char(z42.productive_code)
            left outer join farms.inventory_item_code iic on iic.inventory_item_code = to_char(z42.productive_code)
            where pyv.program_year_version_id = in_program_year_version_id
        ), file_40_aarm as (
            -- historical AARM crops productive capacity
            select (case
                       when max(z40.prior_year_supplemental_key) is not null and aarm.inventory_type_code in ('1') then
                           sum(z40.crop_on_farm_acres)
                       else null
                   end) productive_capacity_amount,
                   null structure_group_code,
                   to_char(aarm.inventory_code) inventory_item_code,
                   max(op.farming_operation_id) farming_operation_id,
                   (case
                       when max(x.agristabilty_commodity_xref_id) is not null then null
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
            left outer join farms.agristabilty_commodity_xref x on to_char(aarm.inventory_code) = x.inventory_item_code
                                                                and to_char(aarm.inventory_type_code) = x.inventory_class_code
            left outer join farms.agristabilty_commodity_xref x2 on x2.inventory_class_code = to_char(aarm.inventory_type_code)
                                                                and x2.inventory_item_code = Unknown
            join farms.agristability_client ac on aarm.participant_pin = ac.participant_pin
            join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
                                       and aarm.program_year = py.year
            join farms.program_year_version pyv on py.program_year_id = pyv.program_year_id
            join farms.farming_operation op on pyv.program_year_version_id = op.program_year_version_id
                                            and op.operation_number = aarm.operation_number
            where z40.crop_on_farm_acres is not null
            and pyv.program_year_version_id = in_program_year_version_id
            group by -- can be simplified with a parent id + inv code/type
                     aarm.aarm_margin_id, -- included to ensure every row is included
                     aarm.participant_pin,
                     aarm.program_year,
                     aarm.operation_number,
                     aarm.inventory_type_code,
                     aarm.inventory_code,
                     aarm.production_unit
        ), file_40_raw as (
            -- historical Non-AARM crops productive capacity
            select zz.crop_on_farm_acres productive_capacity_amount,
                   null structure_group_code,
                   coalesce(iic.inventory_item_code, Unknown) inventory_item_code,
                   (case
                       when iic.inventory_item_code is not null then null
                       else to_char(zz.inventory_code)
                   end) import_comment,
                   op.farming_operation_id
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
                                       and py.year = zz.program_year
            join farms.program_year_version pyv on py.program_year_id = pyv.program_year_id
            join farms.farming_operation op on pyv.program_year_version_id = op.program_year_version_id
                                            and op.operation_number = zz.operation_number
            left outer join farms.inventory_item_code iic on iic.inventory_item_code = to_char(zz.inventory_code)
            where zz.crop_on_farm_acres is not null
            and pyv.program_year_version_id = in_program_year_version_id
        )
        select productive_capacity_amount,
               import_comment,
               structure_group_code,
               inventory_item_code,
               farming_operation_id,
               row_number() over (
                   partition by productive_capacity_amount, import_comment, structure_group_code, inventory_item_code, farming_operation_id
                   order by 1
               ) rn
        from (
            select sum(productive_capacity_amount) productive_capacity_amount,
                   string_agg(import_comment, ' ' order by import_comment) import_comment,
                   structure_group_code,
                   inventory_item_code,
                   farming_operation_id
            from file_21
            group by farming_operation_id,
                     structure_group_code,
                     inventory_item_code
            union all
            select sum(productive_capacity_amount) productive_capacity_amount,
                   string_agg(import_comment, ' ' order by import_comment) import_comment,
                   structure_group_code,
                   inventory_item_code,
                   farming_operation_id
            from file_23
            group by farming_operation_id,
                     structure_group_code,
                     inventory_item_code
            union all
            select sum(case
                       when a.farming_operation_id is not null then a.productive_capacity_amount
                       when b.farming_operation_id is not null then b.productive_capacity_amount
                       else c.productive_capacity_amount
                   end) productive_capacity_amount,
                   string_agg(case
                       when a.farming_operation_id is not null then a.import_comment
                       when b.farming_operation_id is not null then b.import_comment
                       else c.import_comment
                   end, ' ' order by case
                       when a.farming_operation_id is not null then a.import_comment
                       when b.farming_operation_id is not null then b.import_comment
                       else c.import_comment
                   end) import_comment,
                   (case
                       when a.farming_operation_id is not null then a.structure_group_code
                       when b.farming_operation_id is not null then b.structure_group_code
                       else c.structure_group_code
                   end) structure_group_code,
                   (case
                       when a.farming_operation_id is not null then a.inventory_item_code
                       when b.farming_operation_id is not null then b.inventory_item_code
                       else c.inventory_item_code
                   end) inventory_item_code,
                   coalesce(a.farming_operation_id, b.farming_operation_id, c.farming_operation_id) farming_operation_id
            from file_42 a
            full join file_40_aarm b on a.inventory_item_code = b.inventory_item_code
            full join file_40_raw c on a.inventory_item_code = c.inventory_item_code
            group by coalesce(a.farming_operation_id, b.farming_operation_id, c.farming_operation_id),
                     (case
                         when a.farming_operation_id is not null then a.structure_group_code
                         when b.farming_operation_id is not null then b.structure_group_code
                         else c.structure_group_code
                     end),
                     (case
                         when a.farming_operation_id is not null then a.inventory_item_code
                         when b.farming_operation_id is not null then b.inventory_item_code
                         else c.inventory_item_code
                     end)
        ) t;
    puc_insert_val record;

    puc_id farms.productive_unit_capacity.productve_unit_capacity_id%type;

    v_farming_operation_id farms.productive_unit_capacity.farming_operation_id%type := null;
    v_structure_group_code farms.productive_unit_capacity.structure_group_code%type := null;
    v_inventory_item_code farms.productive_unit_capacity.inventory_item_code%type := null;

begin

    for puc_insert_val in puc_insert_cursor
    loop

        v_farming_operation_id := puc_insert_val.farming_operation_id;
        v_structure_group_code := puc_insert_val.structure_group_code;
        v_inventory_item_code := puc_insert_val.inventory_item_code;

        select nextval('farms.seq_puc')
        into puc_id;

        insert into farms.productive_unit_capacity (
            productive_unit_capacity_id,
            productive_capacity_amount,
            import_comment,
            farming_operation_id,
            structure_group_code,
            agristability_scenario_id,
            inventory_item_code,
            revision_count,
            create_user,
            create_date,
            update_user,
            update_date
        ) values (
            puc_id,
            puc_insert_val.productive_capacity_amount,
            puc_insert_val.import_comment,
            puc_insert_val.farming_operation_id,
            puc_insert_val.structure_group_code,
            null,
            puc_insert_val.inventory_item_code,
            1,
            in_user,
            current_timestamp,
            in_user,
            current_timestamp
        );
    end loop;

    return null;
exception
    when others then
        return farms_import_pkg.scrub(farms_error_pkg.codify_productive_unit_capacity(
            sqlerrm,
            v_farming_operation_id,
            v_structure_group_code,
            v_inventory_item_code
        ));
end;
$$;
