create or replace function farms_webapp_pkg.get_ref_year_claim(
    in in_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type
) returns refcursor
language plpgsql
as
$$
declare

    v_cursor refcursor;

begin
    open v_cursor for
        select
            clm.agristability_claim_id,
            clm.administrative_cost_share,
            clm.late_application_penalty,
            clm.maximum_contribution,
            clm.outstanding_fees,
            clm.program_year_margin,
            clm.prod_insur_deemed_benefit,
            clm.program_year_payments_received,
            clm.allocated_reference_margin,
            clm.repayment_of_cash_advances,
            clm.total_payment,
            clm.supply_managed_commodities_adj,
            clm.producer_share,
            clm.federal_contributions,
            clm.provincial_contributions,
            clm.interim_contributions,
            clm.whole_farm_allocation,
            clm.margin_decline,
            clm.negative_margin_decline,
            clm.negative_margin_benefit,
            clm.total_benefit,
            clm.adjusted_reference_margin,
            clm.unadjusted_reference_margin,
            clm.reference_margin_limit,
            clm.tier2_trigger,
            clm.tier2_margin_decline,
            clm.tier2_benefit,
            clm.tier3_trigger,
            clm.tier3_margin_decline,
            clm.tier3_benefit,
            clm.applied_benefit_percent,
            clm.interim_benefit_percent,
            clm.benefit_before_deductions,
            clm.benefit_after_prod_insur_ded,
            clm.benefit_after_appl_benefit_pct,
            clm.benefit_after_interim_ded,
            clm.structural_change_code,
            clm.expense_structural_change_code
        from farms.farm_agristability_scenarios fas1
        join farms.farm_agristability_scenarios fas2 on fas1.program_year_version_id = fas2.program_year_version_id
        join farms.farm_agristability_claims clm on clm.agristability_scenario_id = fas2.agristability_scenario_id
        where fas1.agristability_scenario_id = in_scenario_id
        and fas2.scenario_class_code in ('CRA', 'GEN')
        order by fas2.scenario_number desc;
    return v_cursor;
end;
$$;
