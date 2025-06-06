create or replace function farms_read_pkg.read_sc_tot_mgn(
    in sc_ids numeric[]
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select benefit_calculation_total_id,
               accrual_adjustments_crop_inventory,
               accrual_adjustments_livestock_inventory,
               accrual_adjustments_payables,
               accrual_adjustments_purchased_inputs,
               accrual_adjustments_receivables,
               structural_change_adjustments,
               total_allowable_expenses,
               total_allowable_income,
               unadjusted_production_margin,
               production_margin_accrual_adjustments,
               production_margin_after_structure_changes,
               agristability_scenario_id,
               fiscal_year_pro_rate_adjustments,
               farm_size_ratio,
               expense_farm_size_ratio,
               structural_change_notable_indicator,
               bpu_lead_indicator,
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
               expense_structural_change_adjustments,
               expenses_after_structure_changes,
               ratio_structural_change_adjustments,
               additive_structural_change_adjustments,
               ratio_production_margin_after_structure_changes,
               additive_production_margin_after_structural_change,
               ratio_structural_change_notable_indicator,
               additive_structural_change_notable_indicator,
               revision_count
        from farms.benefit_calculation_total ttl
        where ttl.agristability_scenario_id = any(sc_ids);

    return cur;
end;
$$;
