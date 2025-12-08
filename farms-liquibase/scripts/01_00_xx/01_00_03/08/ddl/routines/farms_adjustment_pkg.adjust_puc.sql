create or replace procedure farms_adjustment_pkg.adjust_puc(
    in in_action text,
    in in_adj_id farms.farm_productve_unit_capacities.productve_unit_capacity_id%type,
    in in_reported_id farms.farm_productve_unit_capacities.cra_productve_unit_capacity_id%type,
    in in_adj_amount farms.farm_productve_unit_capacities.productive_capacity_amount%type,
    in in_farming_operation_id farms.farm_productve_unit_capacities.farming_operation_id%type,
    in in_scenario_id farms.farm_productve_unit_capacities.agristability_scenario_id%type,
    in in_parent_scenario_id farms.farm_productve_unit_capacities.agristability_scenario_id%type,
    in in_structure_group_code farms.farm_productve_unit_capacities.structure_group_code%type,
    in in_inventory_item_code farms.farm_productve_unit_capacities.inventory_item_code%type,
    in in_participnt_data_src_code farms.farm_productve_unit_capacities.participnt_data_src_code%type,
    in in_revision_count farms.farm_productve_unit_capacities.revision_count%type,
    in in_user farms.farm_productve_unit_capacities.who_updated%type
)
language plpgsql
as
$$
begin

    if in_action = 'ADD' then
        insert into farms.farm_productve_unit_capacities (
            productve_unit_capacity_id,
            productive_capacity_amount,
            farming_operation_id,
            structure_group_code,
            agristability_scenario_id,
            inventory_item_code,
            participnt_data_src_code,
            who_created,
            who_updated,
            when_updated,
            cra_productve_unit_capacity_id
        ) values (
            nextval('farms.farm_puc_seq'),
            in_adj_amount,
            in_farming_operation_id,
            in_structure_group_code,
            in_scenario_id,
            in_inventory_item_code,
            in_participnt_data_src_code,
            in_user,
            in_user,
            current_timestamp,
            in_reported_id
        );
    elsif in_action = 'DELETE' then
        delete from farms.farm_productve_unit_capacities puc
        where puc.productve_unit_capacity_id = in_adj_id;
    elsif in_action = 'UPDATE' then
        update farms.farm_productve_unit_capacities
        set productive_capacity_amount = in_adj_amount,
            who_updated = in_user,
            when_updated = current_timestamp,
            revision_count = revision_count + 1
        where productve_unit_capacity_id = in_adj_id
        and revision_count = in_revision_count;
    end if;

    if (in_scenario_id is not null and in_scenario_id::text <> '') then
        insert into farms.farm_scenario_logs (
           scenario_log_id,
           log_message,
           agristability_scenario_id,
           who_created,
           who_updated)
        select nextval('farms.farm_sl_seq'),
                 '[Productive Capacity Adjustment] '
                 || (case
                        when (in_structure_group_code is not null and in_structure_group_code::text <> '') then 'Structure Group'
                        else 'Inventory Item'
                    end)
                 || ' Code: "'
                 || (case
                        when (in_structure_group_code is not null and in_structure_group_code::text <> '') then in_structure_group_code
                        else in_inventory_item_code
                    end)
                 || ' - '
                 || (case
                        when (in_structure_group_code is not null and in_structure_group_code::text <> '') then sgc.description
                        else iic.description
                    end)
                 || '", Year: '
                 || m.year
                 || case
                        when in_action = 'DELETE' then ', Adjustment Deleted'
                        else ', Adjustment Amount: ' || round((in_adj_amount)::numeric, 3)
                    end
               log_message,
               in_parent_scenario_id,
               in_user,
               in_user
        from farms.farm_scenarios_vw m
        left outer join farms.farm_inventory_item_codes iic on iic.inventory_item_code = in_inventory_item_code
        left outer join farms.farm_structure_group_codes sgc on sgc.structure_group_code = in_structure_group_code
        where m.agristability_scenario_id = in_scenario_id;
    end if;

end;
$$;
