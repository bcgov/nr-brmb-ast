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
        select benefit_calc_total_id,
               accrual_adjs_crop_inventory,
               accrual_adjs_lvstck_inventory,
               accrual_adjs_payables,
               accrual_adjs_purchased_inputs,
               accrual_adjs_receivables,
               structural_change_adjs,
               total_allowable_expenses,
               total_allowable_income,
               unadjusted_production_margin,
               production_marg_accr_adjs,
               production_marg_aft_str_changs,
               agristability_scenario_id,
               fiscal_year_pro_rate_adjs,
               farm_size_ratio,
               expense_farm_size_ratio,
               structural_change_notable_ind,
               bpu_lead_ind,
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
               expense_structural_chg_adjs,
               expenses_aft_str_chg,
               ratio_structural_change_adjs,
               additive_structural_change_adj,
               ratio_prod_marg_aft_str_changs,
               additive_prod_marg_aft_str_chg,
               ratio_struc_chg_notable_ind,
               additive_struc_chg_notable_ind,
               revision_count
        from farms.farm_benefit_calc_totals ttl
        where ttl.agristability_scenario_id = any(sc_ids);

    return cur;
end;
$$;
