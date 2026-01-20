create or replace function farms_read_pkg.read_sc_mgn(
    in sc_ids numeric[]
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
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
    return cur;
end;
$$;
