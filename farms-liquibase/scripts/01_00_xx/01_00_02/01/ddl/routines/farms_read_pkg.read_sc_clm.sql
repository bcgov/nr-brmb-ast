create or replace function farms_read_pkg.read_sc_clm(
    in sc_ids numeric[]
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select agristability_claim_id,
               administrative_cost_share,
               late_application_penalty,
               maximum_contribution,
               outstanding_fees,
               program_year_margin,
               production_insurance_deemed_benefit,
               program_year_payments_received,
               allocated_reference_margin,
               repayment_of_cash_advances,
               total_payment,
               supply_managed_commodities_adjusted,
               producer_share,
               federal_contributions,
               provincial_contributions,
               interim_contributions,
               whole_farm_allocation,
               tier2_margin_decline,
               tier3_margin_decline,
               tier2_benefit,
               tier3_benefit,
               margin_decline,
               negative_margin_decline,
               negative_margin_benefit,
               total_benefit,
               adjusted_reference_margin,
               unadjusted_reference_margin,
               reference_margin_limit,
               reference_margin_limit_cap,
               reference_margin_limit_for_benefit_calculation,
               clm.structural_change_code,
               scc.description structural_change_desc,
               clm.expense_structural_change_code,
               escc.description expense_structural_change_desc,
               agristability_scenario_id,
               tier2_trigger,
               tier3_trigger,
               benefit_before_deductions,
               benefit_after_production_insurance_deduction,
               applied_benefit_percent,
               benefit_after_applied_benefit_percent,
               interim_benefit_percent,
               benefit_after_interim_deduction,
               payment_cap,
               benefit_after_payment_cap,
               late_enrolment_penalty,
               benefit_after_late_enrolment_penalty,
               late_enrolment_penalty_after_applied_benefit_percent,
               standard_benefit,
               enhanced_margin_decline,
               enhanced_reference_margin_for_benefit_calculation,
               enhanced_positive_margin_decline,
               enhanced_positive_margin_benefit,
               enhanced_negative_margin_decline,
               enhanced_negative_margin_benefit,
               enhanced_benefit_before_deductions,
               enhanced_benefit_after_production_insurance_deduction,
               enhanced_benefit_after_interim_deduction,
               enhanced_benefit_after_payment_cap,
               enhanced_total_benefit,
               enhanced_additional_benefit,
               enhanced_late_enrolment_penalty,
               enhanced_benefit_after_late_enrolment_penalty,
               enhanced_benefit_after_applied_benefit_percent,
               enhanced_late_enrolment_penalty_after_applied_benefit_percent,
               ratio_adjusted_reference_margin,
               additive_adjusted_reference_margin,
               clm.revision_count,
               clm.pi_deemed_benefit_manual_calculation_indicator
        from farms.agristability_claim clm
        left join farms.structural_change_code scc on scc.structural_change_code = clm.structural_change_code
        left join farms.structural_change_code escc on escc.structural_change_code = clm.expense_structural_change_code
        where clm.agristability_scenario_id = any(sc_ids);

    return cur;
end;
$$;
