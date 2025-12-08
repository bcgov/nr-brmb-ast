create or replace procedure farms_calculator_pkg.update_adj_pyv(
    in in_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
    in in_old_pyv_id farms.farm_program_year_versions.program_year_version_id%type,
    in in_new_pyv_id farms.farm_program_year_versions.program_year_version_id%type,
    in in_user text
)
language plpgsql
as
$$
declare

    op_curs cursor for
        select old_op.farming_operation_id old_op_id,
               new_op.farming_operation_id new_op_id
        from farms.farm_farming_operations old_op
        left outer join farms.farm_farming_operations new_op on old_op.operation_number = new_op.operation_number
                                                             and new_op.program_year_version_id = in_new_pyv_id
                                                             and new_op.locally_generated_ind = 'N'
        where old_op.locally_generated_ind = 'N'
        and old_op.program_year_version_id = in_old_pyv_id;

    inv_curs cursor(old_op_id farms.farm_farming_operations.farming_operation_id%type) for
        select ri.reported_inventory_id,
               ri.agristabilty_cmmdty_xref_id,
               ri.cra_reported_inventory_id
        from farms.farm_reported_inventories ri
        where ri.agristability_scenario_id = in_scenario_id
        and ri.farming_operation_id = old_op_id;

    v_old_op_id farms.farm_farming_operations.farming_operation_id%type;
    v_new_op_id farms.farm_farming_operations.farming_operation_id%type;

begin

    for op_rec in op_curs
    loop
        v_old_op_id := op_rec.old_op_id;
        v_new_op_id := op_rec.new_op_id;

        -- if no matching operation is found or created in the new version
        -- then delete the adjustments.
        if coalesce(v_new_op_id::text, '') = ''
        then

            delete from farms.farm_reported_inventories ri
            where ri.agristability_scenario_id = in_scenario_id
            and ri.farming_operation_id = v_old_op_id;

            delete from farms.farm_reported_income_expenses rie
            where rie.agristability_scenario_id = in_scenario_id
            and rie.farming_operation_id = v_old_op_id;

            delete from farms.farm_productve_unit_capacities puc
            where puc.agristability_scenario_id = in_scenario_id
            and puc.farming_operation_id = v_old_op_id;

        else

            for inv_rec in inv_curs(v_old_op_id)
            loop

                update farms.farm_reported_inventories
                set farming_operation_id = v_new_op_id,
                    who_updated = in_user,
                    when_updated = current_timestamp,
                    cra_reported_inventory_id = (
                        select max(new_cra.reported_inventory_id)
                        from farms.farm_reported_inventories old_cra
                        join farms.farm_reported_inventories new_cra on old_cra.agristabilty_cmmdty_xref_id = new_cra.agristabilty_cmmdty_xref_id
                                                                     and ((old_cra.crop_unit_code = null and new_cra.crop_unit_code = null) or (old_cra.crop_unit_code = new_cra.crop_unit_code))
                                                                     and coalesce(old_cra.price_start, 0) = coalesce(new_cra.price_start, 0)
                                                                     and coalesce(old_cra.price_end, 0) = coalesce(new_cra.price_end, 0)
                                                                     and coalesce(old_cra.start_of_year_amount, 0) = coalesce(new_cra.start_of_year_amount, 0)
                                                                     and coalesce(old_cra.end_of_year_amount, 0) = coalesce(new_cra.end_of_year_amount, 0)
                                                                     and coalesce(old_cra.quantity_start, 0) = coalesce(new_cra.quantity_start, 0)
                                                                     and coalesce(old_cra.quantity_end, 0) = coalesce(new_cra.quantity_end, 0)
                                                                     and coalesce(old_cra.quantity_produced, 0) = coalesce(new_cra.quantity_produced, 0)
                        where old_cra.reported_inventory_id = ri.cra_reported_inventory_id
                        and new_cra.farming_operation_id = v_new_op_id
                        and coalesce(new_cra.agristability_scenario_id::text, '') = ''
                        and not exists (
                            select null
                            from farms.farm_reported_inventories ri2
                            where ri2.agristability_scenario_id = in_scenario_id
                            and ri2.cra_reported_inventory_id = new_cra.reported_inventory_id
                        )
                    )
                where reported_inventory_id = inv_rec.reported_inventory_id;

            end loop;


            update farms.farm_productve_unit_capacities puc
            set farming_operation_id = v_new_op_id,
                who_updated = in_user,
                when_updated = current_timestamp,
                cra_productve_unit_capacity_id = (
                    select max(new_cra.productve_unit_capacity_id)
                    from farms.farm_productve_unit_capacities new_cra
                    where new_cra.farming_operation_id = v_new_op_id
                    and ((coalesce(puc.inventory_item_code::text, '') = '' and coalesce(new_cra.inventory_item_code::text, '') = '') or (puc.inventory_item_code = new_cra.inventory_item_code))
                    and ((coalesce(puc.structure_group_code::text, '') = '' and coalesce(new_cra.structure_group_code::text, '') = '') or (puc.structure_group_code = new_cra.structure_group_code))
                    and coalesce(new_cra.agristability_scenario_id::text, '') = ''
                )
            where puc.agristability_scenario_id = in_scenario_id
            and puc.farming_operation_id = v_old_op_id;


            update farms.farm_reported_income_expenses rie
            set farming_operation_id = v_new_op_id,
                who_updated = in_user,
                when_updated = current_timestamp,
                cra_reported_income_expense_id = (
                    select max(new_cra.reported_income_expense_id)
                    from farms.farm_reported_income_expenses new_cra
                    where new_cra.farming_operation_id = v_new_op_id
                    and ((coalesce(rie.line_item::text, '') = '' and coalesce(new_cra.line_item::text, '') = '') or (rie.line_item = new_cra.line_item))
                    and ((coalesce(rie.expense_ind::text, '') = '' and coalesce(new_cra.expense_ind::text, '') = '') or (rie.expense_ind = new_cra.expense_ind))
                    and coalesce(new_cra.agristability_scenario_id::text, '') = ''
                )
            where rie.agristability_scenario_id = in_scenario_id
            and rie.farming_operation_id = v_old_op_id;

        end if;

    end loop;

end;
$$;
