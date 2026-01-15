CREATE TABLE farms.farm_agristability_claims (
	agristability_claim_id bigint NOT NULL,
	administrative_cost_share decimal(13,2),
	late_application_penalty decimal(13,2),
	maximum_contribution decimal(13,2),
	outstanding_fees decimal(13,2),
	program_year_margin decimal(13,2) NOT NULL,
	prod_insur_deemed_benefit decimal(13,2),
	pi_deemed_bnft_manual_calc_ind varchar(1) NOT NULL,
	program_year_payments_received decimal(13,2),
	allocated_reference_margin decimal(13,2),
	repayment_of_cash_advances decimal(13,2),
	total_payment decimal(13,2),
	supply_managed_commodities_adj decimal(13,2),
	producer_share decimal(13,2),
	federal_contributions decimal(13,2),
	provincial_contributions decimal(13,2),
	interim_contributions decimal(13,2),
	whole_farm_allocation decimal(4,3),
	margin_decline decimal(13,2),
	negative_margin_decline decimal(13,2),
	negative_margin_benefit decimal(13,2),
	total_benefit decimal(13,2) NOT NULL,
	adjusted_reference_margin decimal(13,2),
	unadjusted_reference_margin decimal(13,2),
	reference_margin_limit decimal(13,2),
	reference_margin_limit_cap decimal(13,2),
	ref_marg_limit_for_bnft_calc decimal(13,2),
	tier2_trigger decimal(13,2),
	tier2_margin_decline decimal(13,2),
	tier2_benefit decimal(13,2),
	tier3_trigger decimal(13,2),
	tier3_margin_decline decimal(13,2),
	tier3_benefit decimal(13,2),
	applied_benefit_percent decimal(4,3),
	benefit_before_deductions decimal(13,2),
	benefit_after_prod_insur_ded decimal(13,2),
	benefit_after_appl_benefit_pct decimal(13,2),
	interim_benefit_percent decimal(4,3),
	benefit_after_interim_ded decimal(13,2),
	payment_cap decimal(13,2),
	benefit_after_payment_cap decimal(13,2),
	late_enrolment_penalty decimal(13,2),
	bnft_after_late_enrol_penalty decimal(13,2),
	late_enr_pnlty_aft_apl_ben_pct decimal(13,2),
	standard_benefit decimal(13,2),
	enh_ref_margin_for_beneft_calc decimal(13,2),
	enh_margin_decline decimal(13,2),
	enh_positive_margin_decline decimal(13,2),
	enh_positive_margin_benefit decimal(13,2),
	enh_negative_margin_decline decimal(13,2),
	enh_negative_margin_benefit decimal(13,2),
	enh_benefit_before_deductions decimal(13,2),
	enh_benefit_after_prodins_ded decimal(13,2),
	enh_benefit_after_interim_ded decimal(13,2),
	enh_total_benefit decimal(16,2),
	enh_additional_benefit decimal(16,2),
	enh_late_enrolment_penalty decimal(13,2),
	enh_bnft_aftr_late_enr_penalty decimal(13,2),
	enh_bnft_aftr_applied_bnft_pct decimal(13,2),
	enh_lat_en_pnlty_af_apl_bn_pct decimal(13,2),
	enh_benefit_after_payment_cap decimal(13,2),
	ratio_adj_reference_margin decimal(13,2),
	additive_adj_reference_margin decimal(13,2),
	structural_change_code varchar(10),
	expense_structural_change_code varchar(10),
	agristability_scenario_id bigint NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_agristability_claims IS E'AGRISTABILITY CLAIM is the calculated program benefit for an AgriStabilityClient.  AGRISTABILITY CLAIM data will originate from provincial calculations (note that historical data will come from federal sources). AGRISTABILITY CLAIM is associated with a unique AGRISTABILITY CLIENT for a given Program Year. An AGRISTABILITY CLAIM includes the monies paid, broken down by various funding sources, specifically:';
COMMENT ON COLUMN farms.farm_agristability_claims.additive_adj_reference_margin IS E'ADDITIVE ADJ REFERENCE MARGIN is the REFERENCE MARGIN using the Additive structural change method.';
COMMENT ON COLUMN farms.farm_agristability_claims.adjusted_reference_margin IS E'ADJUSTED REFERENCE MARGIN is the ADJUSTED REFERENCE MARGIN.';
COMMENT ON COLUMN farms.farm_agristability_claims.administrative_cost_share IS E'ADMINISTRATIVE COST SHARE are costs incurred by the client for administrative purposes.';
COMMENT ON COLUMN farms.farm_agristability_claims.agristability_claim_id IS E'AGRISTABILITY CLAIM ID is a surrogate unique identifier for AGRISTABILITY CLAIMs.';
COMMENT ON COLUMN farms.farm_agristability_claims.agristability_scenario_id IS E'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.';
COMMENT ON COLUMN farms.farm_agristability_claims.allocated_reference_margin IS E'ALLOCATED REFERENCE MARGIN is the reference margin allocated to the BENEFIT CALC MARGIN.';
COMMENT ON COLUMN farms.farm_agristability_claims.applied_benefit_percent IS E'APPLIED BENEFIT PERCENT is a percentage used to reduce the benefit.';
COMMENT ON COLUMN farms.farm_agristability_claims.benefit_after_appl_benefit_pct IS E'BENEFIT AFTER APPL BENEFIT PCT is the benefit after the APPLIED BENEFIT PERCENT is applied.';
COMMENT ON COLUMN farms.farm_agristability_claims.benefit_after_interim_ded IS E'BENEFIT AFTER INTERIM DED is the benefit after the "Interim" deduction is made.';
COMMENT ON COLUMN farms.farm_agristability_claims.benefit_after_payment_cap IS E'BENEFIT AFTER PAYMENT CAP is the benefit after the PAYMENT CAP is applied.';
COMMENT ON COLUMN farms.farm_agristability_claims.benefit_after_prod_insur_ded IS E'BENEFIT AFTER PROD INSUR DED is the benefit after the "Production Insurance" deduction is made.';
COMMENT ON COLUMN farms.farm_agristability_claims.benefit_before_deductions IS E'BENEFIT BEFORE DEDUCTIONS is the benefit before the "Production Insurance", "Whole Farm Percentage", and "Interim" deductions are made.';
COMMENT ON COLUMN farms.farm_agristability_claims.bnft_after_late_enrol_penalty IS E'BNFT AFTER LATE ENROL PENALTY is the benefit after the LATE ENROLMENT PENALTY deduction is applied.';
COMMENT ON COLUMN farms.farm_agristability_claims.enh_additional_benefit IS E'ENH ADDITIONAL BENEFIT is the benefit amount to be paid from BC Funding in addition to the PROGRAM YEAR BENEFIT. This is calculated as the ENH TOTAL BENEFIT minus the PROGRAM YEAR BENEFIT.';
COMMENT ON COLUMN farms.farm_agristability_claims.enh_benefit_after_interim_ded IS E'ENH BENEFIT AFTER INTERIM DED is the BC Enhanced Benefit after the "Interim" deduction is made.';
COMMENT ON COLUMN farms.farm_agristability_claims.enh_benefit_after_payment_cap IS E'ENH BENEFIT AFTER PAYMENT CAP is the BC Enhanced benefit after the PAYMENT CAP is applied.';
COMMENT ON COLUMN farms.farm_agristability_claims.enh_benefit_after_prodins_ded IS E'ENH BENEFIT AFTER PRODINS DED is the BC Enhanced Benefit after the "Production Insurance" deduction is made.';
COMMENT ON COLUMN farms.farm_agristability_claims.enh_benefit_before_deductions IS E'ENH BENEFIT BEFORE DEDUCTIONS is the BC Enhanced Benefit before the "Production Insurance", "Whole Farm Percentage", and "Interim" deductions are made.';
COMMENT ON COLUMN farms.farm_agristability_claims.enh_bnft_aftr_applied_bnft_pct IS E'ENH BNFT AFTR APPLIED BNFT PCT is the BC Enhanced benefit after the APPLIED BENEFIT PERCENT is applied.';
COMMENT ON COLUMN farms.farm_agristability_claims.enh_bnft_aftr_late_enr_penalty IS E'ENH BNFT AFTR LATE ENR PENALTY is the BC Enhanced benefit after the ENH LATE ENROLMENT PENALTY deduction is applied.';
COMMENT ON COLUMN farms.farm_agristability_claims.enh_lat_en_pnlty_af_apl_bn_pct IS E'ENH LAT EN PNLTY AF APL BN PCT is the BC Enhanced ENH LATE ENROLMENT PENALTY after the APPLIED BENEFIT PERCENT (combined farm percent) is applied.';
COMMENT ON COLUMN farms.farm_agristability_claims.enh_late_enrolment_penalty IS E'ENH LATE ENROLMENT PENALTY is the deduction made to the BC Enhanced Benefit as a penalty for not paying the enrolment fee until after the final enrolment deadline date for a given program year.';
COMMENT ON COLUMN farms.farm_agristability_claims.enh_margin_decline IS E'ENH MARGIN DECLINE is the amout of decline in the margin in the BC Enhanced Benefit calculation.';
COMMENT ON COLUMN farms.farm_agristability_claims.enh_negative_margin_benefit IS E'ENH NEGATIVE MARGIN BENEFIT is the benefit for the negative margin for the BC Enhanced Benefit.';
COMMENT ON COLUMN farms.farm_agristability_claims.enh_negative_margin_decline IS E'ENH NEGATIVE MARGIN DECLINE is the margin decline for the negative margin for the BC Enhanced Benefit.';
COMMENT ON COLUMN farms.farm_agristability_claims.enh_positive_margin_benefit IS E'ENH POSITIVE MARGIN BENEFIT is the benefit for the positive margin for the BC Enhanced Benefit.';
COMMENT ON COLUMN farms.farm_agristability_claims.enh_positive_margin_decline IS E'ENH POSITIVE MARGIN DECLINE is the margin decline for the positive margin for the BC Enhanced Benefit.';
COMMENT ON COLUMN farms.farm_agristability_claims.enh_ref_margin_for_beneft_calc IS E'ENH REF MARGIN FOR BENEFT CALC  is the reference margin used to calculate the BC Enhanced Benefit.';
COMMENT ON COLUMN farms.farm_agristability_claims.enh_total_benefit IS E'ENH TOTAL BENEFIT is the total calculated BC Enhanced Benefit after deductions.';
COMMENT ON COLUMN farms.farm_agristability_claims.expense_structural_change_code IS E'STRUCTURAL CHANGE CODE denotes the method used to create the structural change.';
COMMENT ON COLUMN farms.farm_agristability_claims.federal_contributions IS E'FEDERAL CONTRIBUTIONS identifies the amount of contributions from the federal government that the participant has received.';
COMMENT ON COLUMN farms.farm_agristability_claims.interim_benefit_percent IS E'INTERIM BENEFIT PERCENT is the percentage of the benefit to be paid for an interim benefit.';
COMMENT ON COLUMN farms.farm_agristability_claims.interim_contributions IS E'INTERIM CONTRIBUTIONS is the amount of contributions have been allocated for the participant if a final calculation has not yet been made. If a final calculation has been made this amount will be zero.';
COMMENT ON COLUMN farms.farm_agristability_claims.late_application_penalty IS E'LATE APPLICATION PENALTY is the penalty applied due to a late application to Agri Stability program.';
COMMENT ON COLUMN farms.farm_agristability_claims.late_enr_pnlty_aft_apl_ben_pct IS E'LATE ENR PNLTY AFT APL BEN PCT is the LATE ENROLMENT PENALTY after the APPLIED BENEFIT PERCENT is applied.';
COMMENT ON COLUMN farms.farm_agristability_claims.late_enrolment_penalty IS E'LATE ENROLMENT PENALTY is the deduction made to the BC Enhanced Benefit as a penalty for not paying the enrolment fee until after the final enrolment deadline date for a given program year.';
COMMENT ON COLUMN farms.farm_agristability_claims.margin_decline IS E'MARGIN DECLINE is the amout of margin decline.';
COMMENT ON COLUMN farms.farm_agristability_claims.maximum_contribution IS E'MAXIMUM CONTRIBUTION is the maximum amount the client is eligible for in this AGRISTABILITY CLAIM.';
COMMENT ON COLUMN farms.farm_agristability_claims.negative_margin_benefit IS E'NEGATIVE MARGIN BENEFIT is the negative amout of margin benefit.';
COMMENT ON COLUMN farms.farm_agristability_claims.negative_margin_decline IS E'NEGATIVE MARGIN DECLINE is the negative amout of margin decline.';
COMMENT ON COLUMN farms.farm_agristability_claims.outstanding_fees IS E'OUTSTANDING FEES are fees which have not yet been paid by the AGRISTABILITY CLIENT.';
COMMENT ON COLUMN farms.farm_agristability_claims.payment_cap IS E'PAYMENT CAP is the maximum payment, calculated as a percentage of MARGIN DECLINE (70% for 2022).';
COMMENT ON COLUMN farms.farm_agristability_claims.pi_deemed_bnft_manual_calc_ind IS E'PI DEEMED BNFT MANUAL CALC IND identifies whether PROD INSUR DEEMED BENEFIT was calculated manually or using the Negative Margins screen.';
COMMENT ON COLUMN farms.farm_agristability_claims.prod_insur_deemed_benefit IS E'PROD INSUR DEEMED BENEFIT must not be blank if there is a negative margin amount.';
COMMENT ON COLUMN farms.farm_agristability_claims.producer_share IS E'PRODUCER SHARE identifies the amount of AgriStability withdrawals that are the producers share.';
COMMENT ON COLUMN farms.farm_agristability_claims.program_year_margin IS E'PROGRAM YEAR MARGIN is determined by the Income less allowable expenses.';
COMMENT ON COLUMN farms.farm_agristability_claims.program_year_payments_received IS E'PROGRAM YEAR PAYMENTS RECEIVED are payments received by the AGRISTABILITY CLIENT to date.';
COMMENT ON COLUMN farms.farm_agristability_claims.provincial_contributions IS E'PROVINCIAL CONTRIBUTIONS identifies the contributions from the provincial government the participant has received.';
COMMENT ON COLUMN farms.farm_agristability_claims.ratio_adj_reference_margin IS E'RATIO ADJ REFERENCE MARGIN is the REFERENCE MARGIN using the Ratio structural change method.';
COMMENT ON COLUMN farms.farm_agristability_claims.ref_marg_limit_for_bnft_calc IS E'REF MARG LIMIT FOR BNFT CALC is the maximum amount for the ALLOCATED REFERENCE MARGIN. For 2018 forward, the lesser of the ADJUSTED REFERNCE MARGIN or the REF MARG LIMIT FOR BNFT CALC is used as the ALLOCATED REFERENCE MARGIN. Used only for 2018 scenarios and forward. For 2018 forward, see REFERENCE MARGIN LIMIT and REF MARG LIMIT FOR BNFT CALC.';
COMMENT ON COLUMN farms.farm_agristability_claims.reference_margin_limit IS E'REFERENCE MARGIN LIMIT is the maximum amount for the ALLOCATED REFERENCE MARGIN. Either the ADJUSTED REFERNCE MARGIN or the REFERENCE MARGIN LIMIT is used as the ALLOCATED REFERENCE MARGIN. Used only for 2013 scenarios and forward.';
COMMENT ON COLUMN farms.farm_agristability_claims.reference_margin_limit_cap IS E'REFERENCE MARGIN LIMIT CAP is the minimum to which the ALLOCATED REFERENCE MARGIN can be reduced. Used only for 2018 scenarios and forward.';
COMMENT ON COLUMN farms.farm_agristability_claims.repayment_of_cash_advances IS E'REPAYMENT OF CASH ADVANCES is the amount received by the AGRISTABILITY CLIENT as cash advances to date.';
COMMENT ON COLUMN farms.farm_agristability_claims.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_agristability_claims.standard_benefit IS E'STANDARD BENEFIT is the total benefit for the Agristability Program. This is populated only for 2017 forward.';
COMMENT ON COLUMN farms.farm_agristability_claims.structural_change_code IS E'STRUCTURAL CHANGE CODE denotes the method used to create the structural change.';
COMMENT ON COLUMN farms.farm_agristability_claims.supply_managed_commodities_adj IS E'SUPPLY MANAGED COMMODITIES ADJ is the supply managed adjusted commodities.';
COMMENT ON COLUMN farms.farm_agristability_claims.tier2_benefit IS E'TIER2 BENEFIT is the benefit for tier2.';
COMMENT ON COLUMN farms.farm_agristability_claims.tier2_margin_decline IS E'TIER2 MARGIN DECLINE is the margin decline for tier2.';
COMMENT ON COLUMN farms.farm_agristability_claims.tier2_trigger IS E'TIER2 TRIGGER is the allocated reference margin multiplied by a constant. It is used to calculate the TIER2 MARGIN DECLINE.';
COMMENT ON COLUMN farms.farm_agristability_claims.tier3_benefit IS E'TIER3 BENEFIT is the benefit for tier3.';
COMMENT ON COLUMN farms.farm_agristability_claims.tier3_margin_decline IS E'TIER3 MARGIN DECLINE is the margin decline for tier3.';
COMMENT ON COLUMN farms.farm_agristability_claims.tier3_trigger IS E'TIER3 TRIGGER is the ALLOCATED REFERENCE MARGIN multiplied by a constant. It is used to calculate the TIER3 MARGIN DECLINE.';
COMMENT ON COLUMN farms.farm_agristability_claims.total_benefit IS E'TOTAL BENEFIT is the total calculated benefit prior to deductions.';
COMMENT ON COLUMN farms.farm_agristability_claims.total_payment IS E'TOTAL PAYMENT is the total amount of payment due to the AGRISTABILITY CLIENT.';
COMMENT ON COLUMN farms.farm_agristability_claims.unadjusted_reference_margin IS E'UNADJUSTED REFERENCE MARGIN is the adjusted reference margin.';
COMMENT ON COLUMN farms.farm_agristability_claims.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_agristability_claims.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_agristability_claims.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_agristability_claims.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
COMMENT ON COLUMN farms.farm_agristability_claims.whole_farm_allocation IS E'WHOLE FARM ALLOCATION is the percentage of the farm associated with the current PROGRAM YEAR.';
CREATE INDEX farm_acl_farm_scmc_exp_fk_i ON farms.farm_agristability_claims (expense_structural_change_code);
CREATE INDEX farm_acl_farm_scmc_fk_i ON farms.farm_agristability_claims (structural_change_code);
ALTER TABLE farms.farm_agristability_claims ADD CONSTRAINT farm_acl_pk PRIMARY KEY (agristability_claim_id);
ALTER TABLE farms.farm_agristability_claims ADD CONSTRAINT farm_ac_as_uk UNIQUE (agristability_scenario_id);
ALTER TABLE farms.farm_agristability_claims ADD CONSTRAINT farm_acl_pidbmc_chk CHECK (pi_deemed_bnft_manual_calc_ind in ('N', 'Y'));
ALTER TABLE farms.farm_agristability_claims ALTER COLUMN agristability_claim_id SET NOT NULL;
ALTER TABLE farms.farm_agristability_claims ALTER COLUMN program_year_margin SET NOT NULL;
ALTER TABLE farms.farm_agristability_claims ALTER COLUMN pi_deemed_bnft_manual_calc_ind SET NOT NULL;
ALTER TABLE farms.farm_agristability_claims ALTER COLUMN total_benefit SET NOT NULL;
ALTER TABLE farms.farm_agristability_claims ALTER COLUMN agristability_scenario_id SET NOT NULL;
ALTER TABLE farms.farm_agristability_claims ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_agristability_claims ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_agristability_claims ALTER COLUMN when_created SET NOT NULL;
