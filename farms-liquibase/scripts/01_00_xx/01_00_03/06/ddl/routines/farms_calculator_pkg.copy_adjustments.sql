create or replace procedure farms_calculator_pkg.copy_adjustments(
    in in_from_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
    in in_to_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type
)
language plpgsql
as
$$
begin

    insert into farms.farm_reported_income_expenses (
        reported_income_expense_id,
        amount,
        expense_ind,
        line_item,
        farming_operation_id,
        agristability_scenario_id,
        who_created,
        when_created,
        who_updated,
        when_updated,
        cra_reported_income_expense_id)
    select nextval('farms.farm_rie_seq'),
           rie.amount,
           rie.expense_ind,
           rie.line_item,
           rie.farming_operation_id,
           in_to_scenario_id,
           who_created,
           when_created,
           who_updated,
           current_timestamp,
           rie.cra_reported_income_expense_id
    from farms.farm_reported_income_expenses rie
    where rie.agristability_scenario_id = in_from_scenario_id;


    insert into farms.farm_productve_unit_capacities (
        productve_unit_capacity_id,
        productive_capacity_amount,
        farming_operation_id,
        structure_group_code,
        agristability_scenario_id,
        inventory_item_code,
        participnt_data_src_code,
        who_created,
        when_created,
        who_updated,
        when_updated,
        cra_productve_unit_capacity_id)
    select nextval('farms.farm_puc_seq'),
           puc.productive_capacity_amount,
           puc.farming_operation_id,
           puc.structure_group_code,
           in_to_scenario_id,
           puc.inventory_item_code,
           puc.participnt_data_src_code,
           who_created,
           when_created,
           who_updated,
           current_timestamp,
           puc.cra_productve_unit_capacity_id
    from farms.farm_productve_unit_capacities puc
    where puc.agristability_scenario_id = in_from_scenario_id;


    insert into farms.farm_reported_inventories (
        reported_inventory_id,
        price_start,
        price_end,
        end_year_producer_price,
        quantity_start,
        quantity_end,
        quantity_produced,
        start_of_year_amount,
        end_of_year_amount,
        accept_producer_price_ind,
        aarm_reference_p1_price,
        aarm_reference_p2_price,
        agristabilty_cmmdty_xref_id,
        crop_unit_code,
        farming_operation_id,
        agristability_scenario_id,
        who_created,
        when_created,
        who_updated,
        when_updated,
        cra_reported_inventory_id)
    select nextval('farms.farm_ri_seq'),
           ri.price_start,
           ri.price_end,
           ri.end_year_producer_price,
           ri.quantity_start,
           ri.quantity_end,
           ri.quantity_produced,
           ri.start_of_year_amount,
           ri.end_of_year_amount,
           ri.accept_producer_price_ind,
           ri.aarm_reference_p1_price,
           ri.aarm_reference_p2_price,
           ri.agristabilty_cmmdty_xref_id,
           ri.crop_unit_code,
           ri.farming_operation_id,
           in_to_scenario_id,
           who_created,
           when_created,
           who_updated,
           current_timestamp,
           ri.cra_reported_inventory_id
    from farms.farm_reported_inventories ri
    where ri.agristability_scenario_id = in_from_scenario_id;

end;
$$;
