CREATE TABLE agristability_claim(
    agristability_claim_id                                           numeric(10, 0)    NOT NULL,
    administrative_cost_share                                        numeric(13, 2),
    late_application_penalty                                         numeric(13, 2),
    maximum_contribution                                             numeric(13, 2),
    outstanding_fees                                                 numeric(13, 2),
    program_year_margin                                              numeric(13, 2)    NOT NULL,
    production_insurance_deemed_benefit                              numeric(13, 2),
    pi_deemed_benefit_manual_calculation_indicator                   varchar(1)        NOT NULL,
    program_year_payments_received                                   numeric(13, 2),
    allocated_reference_margin                                       numeric(13, 2),
    repayment_of_cash_advances                                       numeric(13, 2),
    total_payment                                                    numeric(13, 2),
    supply_managed_commodities_adjusted                              numeric(13, 2),
    producer_share                                                   numeric(13, 2),
    federal_contributions                                            numeric(13, 2),
    provincial_contributions                                         numeric(13, 2),
    interim_contributions                                            numeric(13, 2),
    whole_farm_allocation                                            numeric(4, 3),
    margin_decline                                                   numeric(13, 2),
    negative_margin_decline                                          numeric(13, 2),
    negative_margin_benefit                                          numeric(13, 2),
    total_benefit                                                    numeric(13, 2)    NOT NULL,
    adjusted_reference_margin                                        numeric(13, 2),
    unadjusted_reference_margin                                      numeric(13, 2),
    reference_margin_limit                                           numeric(13, 2),
    reference_margin_limit_cap                                       numeric(13, 2),
    reference_margin_limit_for_benefit_calculation                   numeric(13, 2),
    tier2_trigger                                                    numeric(13, 2),
    tier2_margin_decline                                             numeric(13, 2),
    tier2_benefit                                                    numeric(13, 2),
    tier3_trigger                                                    numeric(13, 2),
    tier3_margin_decline                                             numeric(13, 2),
    tier3_benefit                                                    numeric(13, 2),
    applied_benefit_percent                                          numeric(4, 3),
    benefit_before_deductions                                        numeric(13, 2),
    benefit_after_production_insurance_deduction                     numeric(13, 2),
    benefit_after_applied_benefit_percent                            numeric(13, 2),
    interim_benefit_percent                                          numeric(4, 3),
    benefit_after_interim_deduction                                  numeric(13, 2),
    payment_cap                                                      numeric(13, 2),
    benefit_after_payment_cap                                        numeric(13, 2),
    late_enrolment_penalty                                           numeric(13, 2),
    benefit_after_late_enrolment_penalty                             numeric(13, 2),
    late_enrolment_penalty_after_applied_benefit_percent             numeric(13, 2),
    standard_benefit                                                 numeric(13, 2),
    enhanced_reference_margin_for_benefit_calculation                numeric(13, 2),
    enhanced_margin_decline                                          numeric(13, 2),
    enhanced_positive_margin_decline                                 numeric(13, 2),
    enhanced_positive_margin_benefit                                 numeric(13, 2),
    enhanced_negative_margin_decline                                 numeric(13, 2),
    enhanced_negative_margin_benefit                                 numeric(13, 2),
    enhanced_benefit_before_deductions                               numeric(13, 2),
    enhanced_benefit_after_production_insurance_deduction            numeric(13, 2),
    enhanced_benefit_after_interim_deduction                         numeric(13, 2),
    enhanced_total_benefit                                           numeric(16, 2),
    enhanced_additional_benefit                                      numeric(16, 2),
    enhanced_late_enrolment_penalty                                  numeric(13, 2),
    enhanced_benefit_after_late_enrolment_penalty                    numeric(13, 2),
    enhanced_benefit_after_applied_benefit_percent                   numeric(13, 2),
    enhanced_late_enrolment_penalty_after_applied_benefit_percent    numeric(13, 2),
    enhanced_benefit_after_payment_cap                               numeric(13, 2),
    ratio_adjusted_reference_margin                                  numeric(13, 2),
    additive_adjusted_reference_margin                               numeric(13, 2),
    structural_change_code                                           varchar(10),
    expense_structural_change_code                                   varchar(10),
    agristability_scenario_id                                        numeric(10, 0)    NOT NULL,
    revision_count                                                   numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                                                      varchar(30)       NOT NULL,
    create_date                                                      timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                                                      varchar(30),
    update_date                                                      timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN agristability_claim.agristability_claim_id IS 'AGRISTABILITY CLAIM ID is a surrogate unique identifier for AGRISTABILITY CLAIMs.'
;
COMMENT ON COLUMN agristability_claim.administrative_cost_share IS 'ADMINISTRATIVE COST SHARE are costs incurred by the client for administrative purposes.'
;
COMMENT ON COLUMN agristability_claim.late_application_penalty IS 'LATE APPLICATION PENALTY is the penalty applied due to a late application to Agri Stability program.'
;
COMMENT ON COLUMN agristability_claim.maximum_contribution IS 'MAXIMUM CONTRIBUTION is the maximum amount the client is eligible for in this AGRISTABILITY CLAIM.'
;
COMMENT ON COLUMN agristability_claim.outstanding_fees IS 'OUTSTANDING FEES are fees which have not yet been paid by the AGRISTABILITY CLIENT.'
;
COMMENT ON COLUMN agristability_claim.program_year_margin IS 'PROGRAM YEAR MARGIN is determined by the Income less allowable expenses.'
;
COMMENT ON COLUMN agristability_claim.production_insurance_deemed_benefit IS 'PRODUCTION INSURANCE DEEMED BENEFIT must not be blank if there is a negative margin amount.'
;
COMMENT ON COLUMN agristability_claim.pi_deemed_benefit_manual_calculation_indicator IS 'PI DEEMED BENEFIT MANUAL CALCULATION INDICATOR identifies whether PRODUCTION INSURANCE DEEMED BENEFIT was calculated manually or using the Negative Margins screen.'
;
COMMENT ON COLUMN agristability_claim.program_year_payments_received IS 'PROGRAM YEAR PAYMENTS RECEIVED are payments received by the AGRISTABILITY CLIENT to date.'
;
COMMENT ON COLUMN agristability_claim.allocated_reference_margin IS 'ALLOCATED REFERENCE MARGIN is the reference margin allocated to the BENEFIT CALCULATION MARGIN.'
;
COMMENT ON COLUMN agristability_claim.repayment_of_cash_advances IS 'REPAYMENT OF CASH ADVANCES is the amount received by the AGRISTABILITY CLIENT as cash advances to date.'
;
COMMENT ON COLUMN agristability_claim.total_payment IS 'TOTAL PAYMENT is the total amount of payment due to the AGRISTABILITY CLIENT.'
;
COMMENT ON COLUMN agristability_claim.supply_managed_commodities_adjusted IS 'SUPPLY MANAGED COMMODITIES ADJUSTED is the supply managed adjusted commodities.'
;
COMMENT ON COLUMN agristability_claim.producer_share IS 'PRODUCER SHARE identifies the amount of AgriStability withdrawals that are the producers share.'
;
COMMENT ON COLUMN agristability_claim.federal_contributions IS 'FEDERAL CONTRIBUTIONS identifies the amount of contributions from the federal government that the participant has received.'
;
COMMENT ON COLUMN agristability_claim.provincial_contributions IS 'PROVINCIAL CONTRIBUTIONS identifies the contributions from the provincial government the participant has received.'
;
COMMENT ON COLUMN agristability_claim.interim_contributions IS 'INTERIM CONTRIBUTIONS is the amount of contributions have been allocated for the participant if a final calculation has not yet been made. If a final calculation has been made this amount will be zero.'
;
COMMENT ON COLUMN agristability_claim.whole_farm_allocation IS 'WHOLE FARM ALLOCATION is the percentage of the farm associated with the current PROGRAM YEAR.'
;
COMMENT ON COLUMN agristability_claim.margin_decline IS 'MARGIN DECLINE is the amount of margin decline.'
;
COMMENT ON COLUMN agristability_claim.negative_margin_decline IS 'NEGATIVE MARGIN DECLINE is the negative amout of margin decline.'
;
COMMENT ON COLUMN agristability_claim.negative_margin_benefit IS 'NEGATIVE MARGIN BENEFIT is the negative amout of margin benefit.'
;
COMMENT ON COLUMN agristability_claim.total_benefit IS 'TOTAL BENEFIT is the total calculated benefit prior to deductions.'
;
COMMENT ON COLUMN agristability_claim.adjusted_reference_margin IS 'ADJUSTED REFERENCE MARGIN is the ADJUSTED REFERENCE MARGIN.'
;
COMMENT ON COLUMN agristability_claim.unadjusted_reference_margin IS 'UNADJUSTED REFERENCE MARGIN is the adjusted reference margin.'
;
COMMENT ON COLUMN agristability_claim.reference_margin_limit IS 'REFERENCE MARGIN LIMIT is the maximum amount for the ALLOCATED REFERENCE MARGIN. Either the ADJUSTED REFERENCE MARGIN or the REFERENCE MARGIN LIMIT is used as the ALLOCATED REFERENCE MARGIN. Used only for 2013 scenarios and forward.'
;
COMMENT ON COLUMN agristability_claim.reference_margin_limit_cap IS 'REFERENCE MARGIN LIMIT CAP is the minimum to which the ALLOCATED REFERENCE MARGIN can be reduced. Used only for 2018 scenarios and forward.'
;
COMMENT ON COLUMN agristability_claim.reference_margin_limit_for_benefit_calculation IS 'REFERENCE MARGIN LIMIT FOR BENEFIT CALCULATION is the maximum amount for the ALLOCATED REFERENCE MARGIN. For 2018 forward, the lesser of the ADJUSTED REFERENCE MARGIN or the REFERENCE MARGIN LIMIT FOR BENEFIT CALCULATION is used as the ALLOCATED REFERENCE MARGIN. Used only for 2018 scenarios and forward. For 2018 forward, see REFERENCE MARGIN LIMIT and REFERENCE MARGIN LIMIT FOR BENEFIT CALCULATION.'
;
COMMENT ON COLUMN agristability_claim.tier2_trigger IS 'TIER2 TRIGGER is the allocated reference margin multiplied by a constant. It is used to calculate the TIER2 MARGIN DECLINE.'
;
COMMENT ON COLUMN agristability_claim.tier2_margin_decline IS 'TIER2 MARGIN DECLINE is the margin decline for tier2.'
;
COMMENT ON COLUMN agristability_claim.tier2_benefit IS 'TIER2 BENEFIT is the benefit for tier2.'
;
COMMENT ON COLUMN agristability_claim.tier3_trigger IS 'TIER3 TRIGGER is the ALLOCATED REFERENCE MARGIN multiplied by a constant. It is used to calculate the TIER3 MARGIN DECLINE.'
;
COMMENT ON COLUMN agristability_claim.tier3_margin_decline IS 'TIER3 MARGIN DECLINE is the margin decline for tier3.'
;
COMMENT ON COLUMN agristability_claim.tier3_benefit IS 'TIER3 BENEFIT is the benefit for tier3.'
;
COMMENT ON COLUMN agristability_claim.applied_benefit_percent IS 'APPLIED BENEFIT PERCENT is a percentage used to reduce the benefit.'
;
COMMENT ON COLUMN agristability_claim.benefit_before_deductions IS 'BENEFIT BEFORE DEDUCTIONS is the benefit before the "Production Insurance", "Whole Farm Percentage", and "Interim" deductions are made.'
;
COMMENT ON COLUMN agristability_claim.benefit_after_production_insurance_deduction IS 'BENEFIT AFTER PRODUCTION INSURANCE DEDUCTION is the benefit after the "Production Insurance" deduction is made.'
;
COMMENT ON COLUMN agristability_claim.benefit_after_applied_benefit_percent IS 'BENEFIT AFTER APPLIED BENEFIT PERCENT is the benefit after the APPLIED BENEFIT PERCENT is applied.'
;
COMMENT ON COLUMN agristability_claim.interim_benefit_percent IS 'INTERIM BENEFIT PERCENT is the percentage of the benefit to be paid for an interim benefit.'
;
COMMENT ON COLUMN agristability_claim.benefit_after_interim_deduction IS 'BENEFIT AFTER INTERIM DEDUCTION is the benefit after the "Interim" deduction is made.'
;
COMMENT ON COLUMN agristability_claim.payment_cap IS 'PAYMENT CAP is the maximum payment, calculated as a percentage of MARGIN DECLINE (70% for 2022).'
;
COMMENT ON COLUMN agristability_claim.benefit_after_payment_cap IS 'BENEFIT AFTER PAYMENT CAP is the benefit after the PAYMENT CAP is applied.'
;
COMMENT ON COLUMN agristability_claim.late_enrolment_penalty IS 'LATE ENROLMENT PENALTY is the deduction made to the BC Enhanced Benefit as a penalty for not paying the enrolment fee until after the final enrolment deadline date for a given program year.'
;
COMMENT ON COLUMN agristability_claim.benefit_after_late_enrolment_penalty IS 'BENEFIT AFTER LATE ENROLMENT PENALTY is the benefit after the LATE ENROLMENT PENALTY deduction is applied.'
;
COMMENT ON COLUMN agristability_claim.late_enrolment_penalty_after_applied_benefit_percent IS 'LATE ENROLMENT PENALTY AFTER APPLIED BENEFIT PERCENT is the LATE ENROLMENT PENALTY after the APPLIED BENEFIT PERCENT is applied.'
;
COMMENT ON COLUMN agristability_claim.standard_benefit IS 'STANDARD BENEFIT is the total benefit for the Agristability Program. This is populated only for 2017 forward.'
;
COMMENT ON COLUMN agristability_claim.enhanced_reference_margin_for_benefit_calculation IS 'ENHANCED REFERENCE MARGIN FOR BENEFIT CALCULATION  is the reference margin used to calculate the BC Enhanced Benefit.'
;
COMMENT ON COLUMN agristability_claim.enhanced_margin_decline IS 'ENHANCED MARGIN DECLINE is the amount of decline in the margin in the BC Enhanced Benefit calculation.'
;
COMMENT ON COLUMN agristability_claim.enhanced_positive_margin_decline IS 'ENHANCED POSITIVE MARGIN DECLINE is the margin decline for the positive margin for the BC Enhanced Benefit.'
;
COMMENT ON COLUMN agristability_claim.enhanced_positive_margin_benefit IS 'ENHANCED POSITIVE MARGIN BENEFIT is the benefit for the positive margin for the BC Enhanced Benefit.'
;
COMMENT ON COLUMN agristability_claim.enhanced_negative_margin_decline IS 'ENHANCED NEGATIVE MARGIN DECLINE is the margin decline for the negative margin for the BC Enhanced Benefit.'
;
COMMENT ON COLUMN agristability_claim.enhanced_negative_margin_benefit IS 'ENHANCED NEGATIVE MARGIN BENEFIT is the benefit for the negative margin for the BC Enhanced Benefit.'
;
COMMENT ON COLUMN agristability_claim.enhanced_benefit_before_deductions IS 'ENHANCED BENEFIT BEFORE DEDUCTIONS is the BC Enhanced Benefit before the "Production Insurance", "Whole Farm Percentage", and "Interim" deductions are made.'
;
COMMENT ON COLUMN agristability_claim.enhanced_benefit_after_production_insurance_deduction IS 'ENHANCED BENEFIT AFTER PRODUCTION INSURANCE DEDUCTION is the BC Enhanced Benefit after the "Production Insurance" deduction is made.'
;
COMMENT ON COLUMN agristability_claim.enhanced_benefit_after_interim_deduction IS 'ENHANCED BENEFIT AFTER INTERIM DEDUCTION is the BC Enhanced Benefit after the "Interim" deduction is made.'
;
COMMENT ON COLUMN agristability_claim.enhanced_total_benefit IS 'ENHANCED TOTAL BENEFIT is the total calculated BC Enhanced Benefit after deductions.'
;
COMMENT ON COLUMN agristability_claim.enhanced_additional_benefit IS 'ENHANCED ADDITIONAL BENEFIT is the benefit amount to be paid from BC Funding in addition to the PROGRAM YEAR BENEFIT. This is calculated as the ENHANCED TOTAL BENEFIT minus the PROGRAM YEAR BENEFIT.'
;
COMMENT ON COLUMN agristability_claim.enhanced_late_enrolment_penalty IS 'ENHANCED LATE ENROLMENT PENALTY is the deduction made to the BC Enhanced Benefit as a penalty for not paying the enrolment fee until after the final enrolment deadline date for a given program year.'
;
COMMENT ON COLUMN agristability_claim.enhanced_benefit_after_late_enrolment_penalty IS 'ENHANCED BENEFIT AFTER LATE ENROLMENT PENALTY is the BC Enhanced benefit after the ENHANCED LATE ENROLMENT PENALTY deduction is applied.'
;
COMMENT ON COLUMN agristability_claim.enhanced_benefit_after_applied_benefit_percent IS 'ENHANCED BENEFIT AFTER APPLIED BENEFIT PERCENT is the BC Enhanced benefit after the APPLIED BENEFIT PERCENT is applied.'
;
COMMENT ON COLUMN agristability_claim.enhanced_late_enrolment_penalty_after_applied_benefit_percent IS 'ENHANCED LATE ENROLMENT PENALTY AFTER APPLIED BENEFIT PERCENT is the BC Enhanced ENHANCED LATE ENROLMENT PENALTY after the APPLIED BENEFIT PERCENT (combined farm percent) is applied.'
;
COMMENT ON COLUMN agristability_claim.enhanced_benefit_after_payment_cap IS 'ENHANCED BENEFIT AFTER PAYMENT CAP is the BC Enhanced benefit after the PAYMENT CAP is applied.'
;
COMMENT ON COLUMN agristability_claim.ratio_adjusted_reference_margin IS 'RATIO ADJUSTED REFERENCE MARGIN is the REFERENCE MARGIN using the Ratio structural change method.'
;
COMMENT ON COLUMN agristability_claim.additive_adjusted_reference_margin IS 'ADDITIVE ADJUSTED REFERENCE MARGIN is the REFERENCE MARGIN using the Additive structural change method.'
;
COMMENT ON COLUMN agristability_claim.structural_change_code IS 'STRUCTURAL CHANGE CODE denotes the method used to create the structural change.'
;
COMMENT ON COLUMN agristability_claim.expense_structural_change_code IS 'STRUCTURAL CHANGE CODE denotes the method used to create the structural change.'
;
COMMENT ON COLUMN agristability_claim.agristability_scenario_id IS 'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.'
;
COMMENT ON COLUMN agristability_claim.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN agristability_claim.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN agristability_claim.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN agristability_claim.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN agristability_claim.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE agristability_claim IS 'AGRISTABILITY CLAIM is the calculated program benefit for an AgriStabilityClient.  AGRISTABILITY CLAIM data will originate from provincial calculations (note that historical data will come from federal sources). AGRISTABILITY CLAIM is associated with a unique AGRISTABILITY CLIENT for a given Program Year. An AGRISTABILITY CLAIM includes the monies paid, broken down by various funding sources, specifically:'
;


CREATE INDEX ix_ac_escc ON agristability_claim(expense_structural_change_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_ac_scc ON agristability_claim(structural_change_code)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_ac_asi ON agristability_claim(agristability_scenario_id)
 TABLESPACE pg_default
;
