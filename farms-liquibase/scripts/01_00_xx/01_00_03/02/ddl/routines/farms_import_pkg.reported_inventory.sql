create or replace function farms_import_pkg.reported_inventory(
    in in_program_year_version_id farms.program_year_version.program_year_version_id%type,
    in in_user varchar
)
returns varchar
language plpgsql
as $$
declare
    Unknown constant varchar := '-1';
    inv_insert_cursor cursor for
        select z21.inventory_code::varchar inventory_item_code,
               z21.inventory_type_code::varchar inventory_class_code,
               z21.crop_unit_type::varchar crop_unit_code,
               z21.crop_quantity_produced quantity_produced,
               z21.crop_on_farm_acres on_farm_acres,
               z21.crop_unseedable_acres unseedable_acres,
               z21.quantity_end quantity_end,
               null price_end,
               null price_start,
               null start_of_year_amount,
               z21.end_of_year_amount end_of_year_amount,
               null quantity_start,
               z21.end_of_year_price end_year_producer_price,
               'N' accept_producer_price_indicator,
               null aarm_reference_p1_price,
               null aarm_reference_p2_price,
               op.farming_operation_id,
               (case
                   when x.agristabilty_commodity_xref_id is not null then null
                   else z21.inventory_code || ' ' || z21.inventory_type_code
               end) import_comment,
               coalesce(x.agristabilty_commodity_xref_id, x2.agristabilty_commodity_xref_id) agristabilty_commodity_xref_id
        from farms.z21_participant_supplementary z21
        left outer join farms.agristabilty_commodity_xref x on z21.inventory_code::varchar = x.inventory_item_code
                                                            and z21.inventory_type_code::varchar = x.inventory_class_code
        left outer join farms.agristabilty_commodity_xref x2 on x2.inventory_class_code = z21.inventory_type_code::varchar
                                                            and x2.inventory_item_code = Unknown
        join farms.agristability_client ac on z21.participant_pin = ac.participant_pin
        join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
                                   and z21.program_year = py.year
        join farms.program_year_version pyv on py.program_year_id = pyv.program_year_id
        join farms.farming_operation op on pyv.program_year_version_id = op.program_year_version_id
                                        and op.operation_number = z21.operation_number
        where pyv.program_year_version_id = in_program_year_version_id
        union all
        select aarm.inventory_code::varchar inventory_item_code,
               aarm.inventory_type_code::varchar inventory_class_code,
               aarm.production_unit::varchar crop_unit_code,
               (case
                   when max(z40.prior_year_supplemental_key) is not null and aarm.inventory_type_code in ('1', '2') then
                       sum(z40.crop_quantity_produced)
                   else null
               end) quantity_produced,
               (case
                   when max(z40.prior_year_supplemental_key) is not null and aarm.inventory_type_code in ('1') then
                       sum(z40.crop_on_farm_acres)
                   else null
               end) on_farm_acres,
               null unseedable_acres,
               coalesce(max(aarm.quantity_end), max(z40.quantity_end)) quantity_end,

               coalesce(max(aarm.aarm_reference_p2_price), max(z40.end_year_price)) price_end,
               coalesce(max(aarm.aarm_reference_p1_price), max(z40.starting_price)) price_start,

               coalesce(max(aarm.quantity_start), max(z40.quantity_start)) start_of_year_amount,
               coalesce(max(aarm.quantity_end), max(z40.quantity_end)) end_of_year_amount,

               coalesce(max(aarm.quantity_start), max(z40.quantity_start)) quantity_start,

               -- this was changed after the initial implmenetation to force the values to be
               -- returned to the client spreadsheet. When P1 or P2 is null, the CRA data is used.
               max(z40.end_year_producer_price) end_year_producer_price,
               max(z40.accept_producer_price_indicator) accept_producer_price_indicator,
               coalesce(max(aarm.aarm_reference_p1_price), max(z40.aarm_reference_p1_price)) aarm_reference_p1_price,
               coalesce(max(aarm.aarm_reference_p2_price), max(z40.aarm_reference_p2_price)) aarm_reference_p2_price,
               max(op.farming_operation_id) farming_operation_id,
               (case
                   when max(x.agristabilty_commodity_xref_id) is not null then null
                   else aarm.inventory_code || ' ' || aarm.inventory_type_code
               end) import_comment,
               coalesce(max(x.agristabilty_commodity_xref_id), max(x2.agristabilty_commodity_xref_id)) agristabilty_commodity_xref_id
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
                                                                                         and (z40.quantity_start > 0 or z40.quantity_end > 0)))
        left outer join farms.agristabilty_commodity_xref x on aarm.inventory_code::varchar = x.inventory_item_code
                                                            and aarm.inventory_type_code::varchar = x.inventory_class_code
        left outer join farms.agristabilty_commodity_xref x2 on x2.inventory_class_code = aarm.inventory_type_code::varchar
                                                            and x2.inventory_item_code = Unknown
        join farms.agristability_client ac on aarm.participant_pin = ac.participant_pin
        join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
                                   and aarm.program_year = py.year
        join farms.program_year_version pyv on py.program_year_id = pyv.program_year_id
        join farms.farming_operation op on pyv.program_year_version_id = op.program_year_version_id
                                        and op.operation_number = aarm.operation_number
        where pyv.program_year_version_id = in_program_year_version_id
        group by -- can be simplified with a parent id + inv code/type
                 aarm.aarm_margin_id, -- included to ensure every row is included
                 aarm.participant_pin,
                 aarm.program_year,
                 aarm.operation_number,
                 aarm.inventory_type_code,
                 aarm.inventory_code,
                 aarm.production_unit
        union all
        select zz.inventory_code::varchar inventory_item_code,
               zz.inventory_type_code::varchar inventory_class_code,
               zz.production_unit::varchar crop_unit_code,
               zz.crop_quantity_produced quantity_produced,
               zz.crop_on_farm_acres on_farm_acres,
               null unseedable_acres,
               zz.quantity_end,
               zz.end_year_price price_end,
               zz.starting_price price_start,
               zz.quantity_start start_of_year_amount,
               zz.quantity_end end_of_year_amount,
               zz.quantity_start,
               zz.end_year_producer_price,
               zz.accept_producer_price_indicator,
               zz.aarm_reference_p1_price,
               zz.aarm_reference_p2_price,
               op.farming_operation_id,
               (case
                   when x.agristabilty_commodity_xref_id is not null then null
                   else zz.inventory_code || ' ' || zz.inventory_type_code
               end) import_comment,
               coalesce(x.agristabilty_commodity_xref_id, x2.agristabilty_commodity_xref_id) agristabilty_commodity_xref_id
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
        left outer join farms.agristabilty_commodity_xref x on zz.inventory_code::varchar = x.inventory_item_code
                                                            and zz.inventory_type_code::varchar = x.inventory_class_code
        left outer join farms.agristabilty_commodity_xref x2 on x2.inventory_class_code = zz.inventory_type_code::varchar
                                                             and x2.inventory_item_code = Unknown
        join farms.agristability_client ac on zz.participant_pin = ac.participant_pin
        join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
                                   and zz.program_year = py.year
        join farms.program_year_version pyv on py.program_year_id = pyv.program_year_id
        join farms.farming_operation op on pyv.program_year_version_id = op.program_year_version_id
                                        and op.operation_number = zz.operation_number
        where pyv.program_year_version_id = in_program_year_version_id;
    inv_insert_val record;

    ri_id farms.reported_inventory.reported_inventory_id%type;

    v_inventory_item_code farms.agristabilty_commodity_xref.inventory_item_code%type := null;
    v_inventory_class_code farms.agristabilty_commodity_xref.inventory_class_code%type := null;
    v_crop_unit_code farms.reported_inventory.crop_unit_code%type := null;
    v_farming_operation_id farms.reported_inventory.farming_operation_id%type := null;
begin

    call farms_import_pkg.setup_unknown_codes();

    for inv_insert_val in inv_insert_cursor
    loop
        v_inventory_item_code := inv_insert_val.inventory_item_code;
        v_inventory_class_code := inv_insert_val.inventory_class_code;
        v_crop_unit_code := inv_insert_val.crop_unit_code;
        v_farming_operation_id := inv_insert_val.farming_operation_id;

        if v_inventory_class_code is null or inv_insert_val.agristabilty_commodity_xref_id is null then
            return farms_import_pkg.scrub(farms_error_pkg.codify_reported_inventory(
                'INV_CLS_CD',
                v_inventory_item_code,
                v_inventory_class_code,
                v_crop_unit_code,
                v_farming_operation_id
            ));
        end if;

        select nextval('farms.seq_ri')
        into ri_id;

        insert into farms.reported_inventory (
            reported_inventory_id,
            price_start,
            price_end,
            end_year_producer_price,
            quantity_end,
            start_of_year_amount,
            end_of_year_amount,
            quantity_produced,
            on_farm_acres,
            unseedable_acres,
            quantity_start,
            accept_producer_price_indicator,
            aarm_reference_p1_price,
            aarm_reference_p2_price,
            import_comment,
            agristabilty_commodity_xref_id,
            crop_unit_code,
            farming_operation_id,
            agristability_scenario_id,
            revision_count,
            create_user,
            create_date,
            update_user,
            update_date
        ) values (
            ri_id,
            inv_insert_val.price_start,
            inv_insert_val.price_end,
            inv_insert_val.end_year_producer_price,
            inv_insert_val.quantity_end,
            inv_insert_val.start_of_year_amount,
            inv_insert_val.end_of_year_amount,
            inv_insert_val.quantity_produced,
            inv_insert_val.on_farm_acres,
            inv_insert_val.unseedable_acres,
            inv_insert_val.quantity_start,
            inv_insert_val.accept_producer_price_indicator,
            inv_insert_val.aarm_reference_p1_price,
            inv_insert_val.aarm_reference_p2_price,
            inv_insert_val.import_comment,
            inv_insert_val.agristabilty_commodity_xref_id,
            inv_insert_val.crop_unit_code,
            inv_insert_val.farming_operation_id,
            null,
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
        return farms_import_pkg.scrub(farms_error_pkg.codify_reported_inventory(
            sqlerrm,
            v_inventory_item_code,
            v_inventory_class_code,
            v_crop_unit_code,
            v_farming_operation_id
        ));
end;
$$;
