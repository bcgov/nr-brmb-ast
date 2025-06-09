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
        select benefit_calculation_margin_id,
               accrual_adjustments_crop_inventory,
               accrual_adjustments_livestock_inventory,
               accrual_adjustments_payables,
               accrual_adjustments_purchased_inputs,
               accrual_adjustments_receivables,
               total_allowable_expenses,
               total_allowable_income,
               unadjusted_production_margin,
               production_margin_accrual_adjustments,
               farming_operation_id,
               agristability_scenario_id,

               supply_managed_commodity_income,
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
               expense_accrual_adjustments,
               revision_count,
               production_insurance_deemed_subtotal,
               production_insurance_deemed_total
        from farms.benefit_calculation_margin clm
        where clm.agristability_scenario_id = any(sc_ids);
    return cur;
end;
$$;
