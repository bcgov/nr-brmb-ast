create or replace function farms_read_pkg.read_sc_mgn(
    in sc_ids bigint[]
)
returns table(
    benefit_calc_margin_id          farms.farm_benefit_calc_margins.benefit_calc_margin_id%type,
    accrual_adjs_crop_inventory     farms.farm_benefit_calc_margins.accrual_adjs_crop_inventory%type,
    accrual_adjs_lvstck_inventory   farms.farm_benefit_calc_margins.accrual_adjs_lvstck_inventory%type,
    accrual_adjs_payables           farms.farm_benefit_calc_margins.accrual_adjs_payables%type,
    accrual_adjs_purchased_inputs   farms.farm_benefit_calc_margins.accrual_adjs_purchased_inputs%type,
    accrual_adjs_receivables        farms.farm_benefit_calc_margins.accrual_adjs_receivables%type,
    total_allowable_expenses        farms.farm_benefit_calc_margins.total_allowable_expenses%type,
    total_allowable_income          farms.farm_benefit_calc_margins.total_allowable_income%type,
    unadjusted_production_margin    farms.farm_benefit_calc_margins.unadjusted_production_margin%type,
    production_marg_accr_adjs       farms.farm_benefit_calc_margins.production_marg_accr_adjs%type,
    farming_operation_id            farms.farm_benefit_calc_margins.farming_operation_id%type,
    agristability_scenario_id       farms.farm_benefit_calc_margins.agristability_scenario_id%type,

    supply_mngd_commodity_income    farms.farm_benefit_calc_margins.supply_mngd_commodity_income%type,
    unadjusted_allowable_income     farms.farm_benefit_calc_margins.unadjusted_allowable_income%type,
    yardage_income                  farms.farm_benefit_calc_margins.yardage_income%type,
    program_payment_income          farms.farm_benefit_calc_margins.program_payment_income%type,
    total_unallowable_income        farms.farm_benefit_calc_margins.total_unallowable_income%type,
    unadjusted_allowable_expenses   farms.farm_benefit_calc_margins.unadjusted_allowable_expenses%type,
    yardage_expenses                farms.farm_benefit_calc_margins.yardage_expenses%type,
    contract_work_expenses          farms.farm_benefit_calc_margins.contract_work_expenses%type,
    manual_expenses                 farms.farm_benefit_calc_margins.manual_expenses%type,
    total_unallowable_expenses      farms.farm_benefit_calc_margins.total_unallowable_expenses%type,
    deferred_program_payments       farms.farm_benefit_calc_margins.deferred_program_payments%type,
    expense_accr_adjs               farms.farm_benefit_calc_margins.expense_accr_adjs%type,
    revision_count                  farms.farm_benefit_calc_margins.revision_count%type,
    prod_insur_deemed_subtotal      farms.farm_benefit_calc_margins.prod_insur_deemed_subtotal%type,
    prod_insur_deemed_total         farms.farm_benefit_calc_margins.prod_insur_deemed_total%type
)
language sql
as $$
    select benefit_calc_margin_id,
           accrual_adjs_crop_inventory,
           accrual_adjs_lvstck_inventory,
           accrual_adjs_payables,
           accrual_adjs_purchased_inputs,
           accrual_adjs_receivables,
           total_allowable_expenses,
           total_allowable_income,
           unadjusted_production_margin,
           production_marg_accr_adjs,
           farming_operation_id,
           agristability_scenario_id,

           supply_mngd_commodity_income,
           unadjusted_allowable_income,
           yardage_income,
           program_payment_income,
           total_unallowable_income,
           unadjusted_allowable_expenses,
           yardage_expenses,
           contract_work_expenses,
           manual_expenses,
           total_unallowable_expenses,
           deferred_program_payments,
           expense_accr_adjs,
           revision_count,
           prod_insur_deemed_subtotal,
           prod_insur_deemed_total
    from farms.farm_benefit_calc_margins clm
    where clm.agristability_scenario_id = any(sc_ids);
$$;
