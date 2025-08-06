create or replace function farms_import_pkg.is_inv_changed(
    in in_program_year_id farms.farm_program_years.program_year_id%type,
    in in_program_year_vrsn_prev_id farms.farm_program_year_versions.program_year_version_id%type
)
returns boolean
language plpgsql
as $$
declare
    Unknown constant varchar := '-1';
    cnt numeric := null;
begin
    -- check Reported Inventory
    with cur as (
        select x.inventory_item_code,
               x.inventory_class_code,
               inv.crop_unit_code,
               inv.quantity_produced,
               inv.start_of_year_amount,
               inv.end_of_year_amount,
               inv.quantity_start,
               inv.quantity_end,
               inv.price_end,
               inv.price_start,
               inv.end_year_producer_price,
               inv.accept_producer_price_ind,
               inv.aarm_reference_p1_price,
               inv.aarm_reference_p2_price,
               inv.on_farm_acres,
               inv.unseedable_acres,
               inv.import_comment,
               op.operation_number,
               row_number() over (partition by inventory_item_code, inventory_class_code,
               crop_unit_code, quantity_produced, start_of_year_amount, end_of_year_amount,
               quantity_start, quantity_end, price_end, price_start, end_year_producer_price,
               accept_producer_price_ind, aarm_reference_p1_price, aarm_reference_p2_price,
               on_farm_acres, unseedable_acres, import_comment, operation_number order by 1) rn
        from farms.farm_agristability_clients ac
        join farms.farm_program_years py on ac.agristability_client_id = py.agristability_client_id
        join farms.farm_program_year_versions pyv on pyv.program_year_id = py.program_year_id
        join farms.farm_farming_operations op on op.program_year_version_id = pyv.program_year_version_id
        join farms.farm_reported_inventories inv on inv.farming_operation_id = op.farming_operation_id
        join farms.farm_agristabilty_cmmdty_xref x on inv.agristabilty_cmmdty_xref_id = x.agristabilty_cmmdty_xref_id
        where pyv.program_year_version_id = in_program_year_vrsn_prev_id
        and op.locally_generated_ind = 'N'
        and inv.agristability_scenario_id is null
    ), file_21 as (
        select (case
                   when x.agristabilty_cmmdty_xref_id is not null then to_char(z21.inventory_code)
                   else x2.inventory_item_code
               end) inventory_item_code,
               (case
                   when x.agristabilty_cmmdty_xref_id is not null then to_char(z21.inventory_type_code)
                   else x2.inventory_class_code
               end) inventory_class_code,
               to_char(z21.crop_unit_type) crop_unit_code,
               z21.crop_qty_produced quantity_produced,
               null start_of_year_amount,
               z21.end_of_year_amount,
               null quantity_start,
               z21.quantity_end,
               null price_end,
               null price_start,
               z21.end_of_year_price end_year_producer_price,
               'N' accept_producer_price_ind,
               null aarm_reference_p1_price,
               null aarm_reference_p2_price,
               z21.crop_on_farm_acres on_farm_acres,
               z21.crop_unseedable_acres unseedable_acres,
               null import_comment,
               z21.operation_number
        from farms.farm_z21_participant_suppls z21
        join farms.farm_agristability_clients ac on z21.participant_pin = ac.participant_pin
        join farms.farm_program_years py on ac.agristability_client_id = py.agristability_client_id
                                   and z21.program_year = py.year
        left outer join farms.farm_agristabilty_cmmdty_xref x on to_char(z21.inventory_code) = x.inventory_item_code
                                                            and to_char(z21.inventory_type_code) = x.inventory_class_code
        left outer join farms.farm_agristabilty_cmmdty_xref x2 on to_char(z21.inventory_type_code) = x2.inventory_class_code
                                                             and x2.inventory_item_code = Unknown
        where py.program_year_id = in_program_year_id
    ), aarm as (
        select (case
                   when x.agristabilty_cmmdty_xref_id is not null then to_char(aarm.inventory_code)
                   else x2.inventory_item_code
               end) inventory_item_code,
               to_char(aarm.inventory_type_code) inventory_class_code,
               to_char(aarm.production_unit) crop_unit_code,
               (case
                   when max(z40.prior_year_supplemental_key) is not null and aarm.inventory_type_code in ('1', '2') then
                       sum(z40.crop_qty_produced)
                   else
                       null
               end) quantity_produced,
               coalesce(max(aarm.quantity_start), max(z40.quantity_start)) start_of_year_amount,
               coalesce(max(aarm.quantity_end), max(z40.quantity_end)) end_of_year_amount,
               coalesce(max(aarm.quantity_start), max(z40.quantity_start)) quantity_start,
               coalesce(max(aarm.quantity_end), max(z40.quantity_end)) quantity_end,
               coalesce(max(aarm.aarm_reference_p1_price), max(z40.end_year_price)) price_end,
               coalesce(max(aarm.aarm_reference_p2_price), max(z40.starting_price)) price_start,
               -- this was changed after the initial implmenetation to force the values to be
               -- returned to the client spreadsheet. When P1 or P2 is null, the CRA data is used.
               max(z40.end_year_producer_price) end_year_producer_price,
               max(z40.accept_producer_price_ind) accept_producer_price_ind,
               coalesce(max(aarm.aarm_reference_p1_price), max(z40.aarm_reference_p1_price)) aarm_reference_p1_price,
               coalesce(max(aarm.aarm_reference_p2_price), max(z40.aarm_reference_p2_price)) aarm_reference_p2_price,
               (case
                   when max(z40.prior_year_supplemental_key) is not null and aarm.inventory_type_code in ('1') then
                       sum(z40.crop_on_farm_acres)
                   else
                       null
               end) on_farm_acres,
               null unseedable_acres,
               (case
                   when max(x.agristabilty_cmmdty_xref_id) is not null then null
                   else aarm.inventory_code || ' ' || aarm.inventory_type_code
               end) import_comment,
               max(aarm.operation_number) operation_number
        from farms.farm_aarm_margins aarm
        left outer join farms.farm_z40_prtcpnt_ref_supl_dtls z40 on aarm.participant_pin = z40.participant_pin
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
        left outer join farms.farm_agristabilty_cmmdty_xref x on to_char(aarm.inventory_code) = x.inventory_item_code
                                                            and to_char(aarm.inventory_type_code) = x.inventory_class_code
        left outer join farms.farm_agristabilty_cmmdty_xref x2 on to_char(aarm.inventory_type_code) = x2.inventory_class_code
                                                             and x2.inventory_item_code = Unknown
        join farms.farm_agristability_clients ac on aarm.participant_pin = ac.participant_pin
        join farms.farm_program_years py on ac.agristability_client_id = py.agristability_client_id
                                   and aarm.program_year = py.year
        where aarm.program_year = in_program_year_id
        group by -- can be simplified with a parent id + inv code/type
                 aarm.aarm_margin_id, -- included to ensure every row is included
                 aarm.participant_pin,
                 aarm.program_year,
                 aarm.operation_number,
                 aarm.inventory_type_code,
                 aarm.inventory_code,
                 aarm.production_unit,
                 x.agristabilty_cmmdty_xref_id,
                 x2.inventory_item_code,
                 x2.inventory_class_code
    ), file_40 as (
        select (case
                   when x.agristabilty_cmmdty_xref_id is not null then to_char(zz.inventory_code)
                   else x2.inventory_item_code
               end) inventory_item_code,
               (case
                   when x.agristabilty_cmmdty_xref_id is not null then to_char(zz.inventory_type_code)
                   else x2.inventory_class_code
               end) inventory_class_code,
               to_char(zz.production_unit) crop_unit_code,
               zz.crop_qty_produced quantity_produced,
               zz.quantity_start start_of_year_amount,
               zz.quantity_end end_of_year_amount,
               zz.quantity_start,
               zz.quantity_end,
               zz.end_year_price price_end,
               zz.starting_price price_start,
               zz.end_year_producer_price,
               zz.accept_producer_price_ind,
               zz.aarm_reference_p1_price,
               zz.aarm_reference_p2_price,
               zz.crop_on_farm_acres on_farm_acres,
               null unseedable_acres,
               (case
                   when x.agristabilty_cmmdty_xref_id is not null then null
                   else zz.inventory_code || ' ' || zz.inventory_type_code
               end) import_comment,
               zz.operation_number
        from (
            select z40.*
            from farms.farm_z40_prtcpnt_ref_supl_dtls z40
            left outer join farms.farm_aarm_margins aarm on aarm.participant_pin = z40.participant_pin
                                                    and aarm.program_year = z40.program_year
                                                    and aarm.operation_number = z40.operation_number
                                                    and aarm.inventory_type_code = z40.inventory_type_code
                                                    and aarm.inventory_code = z40.inventory_code
                                                    and (aarm.production_unit = z40.production_unit
                                                         or (aarm.production_unit is null and z40.production_unit is null))
            where aarm.aarm_margin_id is null
        ) zz
        left outer join farms.farm_agristabilty_cmmdty_xref x on to_char(zz.inventory_code) = x.inventory_item_code
                                                            and to_char(zz.inventory_type_code) = x.inventory_class_code
        left outer join farms.farm_agristabilty_cmmdty_xref x2 on to_char(zz.inventory_type_code) = x2.inventory_class_code
                                                             and x2.inventory_item_code = Unknown
        join farms.farm_agristability_clients ac on zz.participant_pin = ac.participant_pin
        join farms.farm_program_years py on ac.agristability_client_id = py.agristability_client_id
                                   and zz.program_year = py.year
        where py.program_year_id = in_program_year_id
    ), staging_area as (
        select inventory_item_code,
               inventory_class_code,
               crop_unit_code,
               quantity_produced,
               start_of_year_amount,
               end_of_year_amount,
               quantity_start,
               quantity_end,
               price_end,
               price_start,
               end_year_producer_price,
               accept_producer_price_ind,
               aarm_reference_p1_price,
               aarm_reference_p2_price,
               on_farm_acres,
               unseedable_acres,
               import_comment,
               operation_number,
               row_number() over (partition by inventory_item_code, inventory_class_code,
               crop_unit_code, quantity_produced, start_of_year_amount, end_of_year_amount,
               quantity_start, quantity_end, price_end, price_start, end_year_producer_price,
               accept_producer_price_ind, aarm_reference_p1_price, aarm_reference_p2_price,
               on_farm_acres, unseedable_acres, import_comment, operation_number order by 1) rn
        from (
            select *
            from file_21
            union all
            select *
            from aarm
            union all
            select *
            from file_40
        ) t1
    )
    select count(*)
    into cnt
    from (
        select *
        from (
            select *
            from staging_area
            except
            select *
            from cur
        ) t2
        union all
        (
            select *
            from cur
            except
            select *
            from staging_area
        )
    ) t3;

    if cnt > 0 then
        return true;
    end if;

    return false;
end;
$$;
