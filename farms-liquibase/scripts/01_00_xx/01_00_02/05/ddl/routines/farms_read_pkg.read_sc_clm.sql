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
               prod_insur_deemed_benefit,
               program_year_payments_received,
               allocated_reference_margin,
               repayment_of_cash_advances,
               total_payment,
               supply_managed_commodities_adj,
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
               ref_marg_limit_for_bnft_calc,
               clm.structural_change_code,
               scc.description structural_change_desc,
               clm.expense_structural_change_code,
               escc.description expense_structural_change_desc,
               agristability_scenario_id,
               tier2_trigger,
               tier3_trigger,
               benefit_before_deductions,
               benefit_after_prod_insur_ded,
               applied_benefit_percent,
               benefit_after_appl_benefit_pct,
               interim_benefit_percent,
               benefit_after_interim_ded,
               payment_cap,
               benefit_after_payment_cap,
               late_enrolment_penalty,
               bnft_after_late_enrol_penalty,
               late_enr_pnlty_aft_apl_ben_pct,
               standard_benefit,
               enh_margin_decline,
               enh_ref_margin_for_beneft_calc,
               enh_positive_margin_decline,
               enh_positive_margin_benefit,
               enh_negative_margin_decline,
               enh_negative_margin_benefit,
               enh_benefit_before_deductions,
               enh_benefit_after_prodins_ded,
               enh_benefit_after_interim_ded,
               enh_benefit_after_payment_cap,
               enh_total_benefit,
               enh_additional_benefit,
               enh_late_enrolment_penalty,
               enh_bnft_aftr_late_enr_penalty,
               enh_bnft_aftr_applied_bnft_pct,
               enh_lat_en_pnlty_af_apl_bn_pct,
               ratio_adj_reference_margin,
               additive_adj_reference_margin,
               clm.revision_count,
               clm.pi_deemed_bnft_manual_calc_ind
        from farms.farm_agristability_claims clm
        left join farms.farm_structural_change_codes scc on scc.structural_change_code = clm.structural_change_code
        left join farms.farm_structural_change_codes escc on escc.structural_change_code = clm.expense_structural_change_code
        where clm.agristability_scenario_id = any(sc_ids);

    return cur;
end;
$$;
