create or replace procedure farms_calculator_pkg.delete_adj_for_generated_ops(
    in in_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type
)
language plpgsql
as
$$
begin

    -- Delete Income and Expenses
    delete from farms.farm_reported_income_expenses rie
    where rie.reported_income_expense_id in (
        select rie.reported_income_expense_id
        from farms.farm_agristability_scenarios sc
        join farms.farm_farming_operations fo on fo.program_year_version_id = sc.program_year_version_id
        join farms.farm_reported_income_expenses rie on rie.farming_operation_id = fo.farming_operation_id
                                                     and rie.agristability_scenario_id = sc.agristability_scenario_id
        where fo.locally_generated_ind = 'Y'
        and sc.agristability_scenario_id = in_scenario_id
    );

    -- Delete Inventory and Accruals
    delete from farms.farm_reported_inventories ri
    where ri.reported_inventory_id in (
        select ri.reported_inventory_id
        from farms.farm_agristability_scenarios sc
        join farms.farm_farming_operations fo on fo.program_year_version_id = sc.program_year_version_id
        join farms.farm_reported_inventories ri on ri.farming_operation_id = fo.farming_operation_id
                                                and ri.agristability_scenario_id = sc.agristability_scenario_id
        where fo.locally_generated_ind = 'Y'
        and sc.agristability_scenario_id = in_scenario_id
    );

    -- Delete Productive Units
    delete from farms.farm_productve_unit_capacities puc
    where puc.productve_unit_capacity_id in (
        select puc.productve_unit_capacity_id
        from farms.farm_agristability_scenarios sc
        join farms.farm_farming_operations fo on fo.program_year_version_id = sc.program_year_version_id
        join farms.farm_productve_unit_capacities puc on puc.farming_operation_id = fo.farming_operation_id
                                                      and puc.agristability_scenario_id = sc.agristability_scenario_id
        where fo.locally_generated_ind = 'Y'
        and sc.agristability_scenario_id = in_scenario_id
    );

end;
$$;
