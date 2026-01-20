create or replace procedure farms_calculator_pkg.log_import_comments(
    in in_program_year_version_id farms.farm_agristability_scenarios.program_year_version_id%type,
    in in_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
    in in_user farms.farm_agristability_scenarios.who_created%type
)
language plpgsql
as
$$
begin

    insert into farms.farm_scenario_logs (
        scenario_log_id,
        log_message,
        agristability_scenario_id,
        who_created,
        who_updated)
    select nextval('farms.farm_sl_seq'),
           '[Productive Capacity Import Comment] '
           || (case when (puc.structure_group_code is not null and puc.structure_group_code::text <> '') then 'Structure Group' else 'Inventory' end)
           || ' Code: '
           || coalesce(puc.structure_group_code, puc.inventory_item_code)
           || ' Comment: '
           || puc.import_comment,
           in_scenario_id,
           in_user,
           in_user
    from farms.farm_farming_operations fo
    join farms.farm_productve_unit_capacities puc on puc.farming_operation_id = fo.farming_operation_id
    where fo.program_year_version_id = in_program_year_version_id
    and (puc.import_comment is not null and puc.import_comment::text <> '');

    insert into farms.farm_scenario_logs (
       scenario_log_id,
       log_message,
       agristability_scenario_id,
       who_created,
       who_updated)
    select nextval('farms.farm_sl_seq'),
           '[' || case when rie.expense_ind = 'Y' then 'Expense' else 'Income' end
           || ' Import Comment] Code: '
           || rie.line_item
           || ' Comment: '
           || rie.import_comment,
           in_scenario_id,
           in_user,
           in_user
    from farms.farm_farming_operations fo
    join farms.farm_reported_income_expenses rie on rie.farming_operation_id = fo.farming_operation_id
    where fo.program_year_version_id = in_program_year_version_id
    and (rie.import_comment is not null and rie.import_comment::text <> '');

    insert into farms.farm_scenario_logs(
        scenario_log_id,
        log_message,
        agristability_scenario_id,
        who_created,
        who_updated)
    select nextval('farms.farm_sl_seq'),
           '[Inventory/Accrual Import Comment] Code: '
           || iic.inventory_item_code
           || ' Comment: '
           || ri.import_comment,
           in_scenario_id,
           in_user,
           in_user
    from farms.farm_farming_operations fo
    join farms.farm_reported_inventories ri on ri.farming_operation_id = fo.farming_operation_id
    join farms.farm_agristabilty_cmmdty_xref x on x.agristabilty_cmmdty_xref_id = ri.agristabilty_cmmdty_xref_id
    join farms.farm_inventory_item_codes iic on iic.inventory_item_code = x.inventory_item_code
    where fo.program_year_version_id = in_program_year_version_id
    and (ri.import_comment is not null and ri.import_comment::text <> '');

end;
$$;
