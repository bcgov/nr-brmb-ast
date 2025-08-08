CREATE TABLE farms.farm_benefit_calc_totals(
    benefit_calc_total_id                          numeric(10, 0)    NOT NULL,
    total_allowable_expenses                              numeric(16, 2),
    total_allowable_income                                numeric(16, 2),
    unadjusted_production_margin                          numeric(16, 2),
    production_marg_accr_adjs                 numeric(16, 2),
    production_marg_aft_str_changs             numeric(16, 2),
    accrual_adjs_crop_inventory                    numeric(16, 2),
    accrual_adjs_lvstck_inventory               numeric(16, 2),
    accrual_adjs_payables                          numeric(16, 2),
    accrual_adjs_purchased_inputs                  numeric(16, 2),
    accrual_adjs_receivables                       numeric(16, 2),
    structural_change_adjs                         numeric(16, 2),
    fiscal_year_pro_rate_adjs                      numeric(16, 2),
    supply_mngd_commodity_income                       numeric(16, 2),
    unadjusted_allowable_income                           numeric(16, 2),
    yardage_income                                        numeric(16, 2),
    program_payment_income                                numeric(16, 2),
    total_unallowable_income                              numeric(16, 2),
    unadjusted_allowable_expenses                         numeric(16, 2),
    yardage_expenses                                      numeric(16, 2),
    contract_work_expenses                                numeric(16, 2),
    manual_expenses                                       numeric(16, 2),
    total_unallowable_expenses                            numeric(16, 2),
    deferred_program_payments                             numeric(16, 2),
    farm_size_ratio                                       numeric(8, 3),
    expense_farm_size_ratio                               numeric(8, 3),
    structural_change_notable_ind                   varchar(1),
    bpu_lead_ind                                    varchar(1),
    expense_accr_adjs                           numeric(16, 2),
    expense_structural_chg_adjs                 numeric(16, 2),
    expenses_aft_str_chg                      numeric(16, 2),
    ratio_structural_change_adjs                   numeric(16, 2),
    additive_structural_change_adj                numeric(16, 2),
    ratio_prod_marg_aft_str_changs       numeric(16, 2),
    additive_prod_marg_aft_str_chg    numeric(16, 2),
    ratio_struc_chg_notable_ind             varchar(1),
    additive_struc_chg_notable_ind          varchar(1),
    agristability_scenario_id                             numeric(10, 0)    NOT NULL,
    revision_count                                        numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                                           varchar(30)       NOT NULL,
    when_created                                           timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                                           varchar(30),
    when_updated                                           timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_benefit_calc_totals.benefit_calc_total_id IS 'BENEFIT CALCULATION TOTAL ID is a surrogate unique identifier for BENEFIT CALCULATION TOTALs.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.total_allowable_expenses IS 'TOTAL ALLOWABLE EXPENSES are the Calculated expenses for a program year.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.total_allowable_income IS 'TOTAL ALLOWABLE INCOME is the Calculated income for a PROGRAM YEAR.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.unadjusted_production_margin IS 'UNADJUSTED PRODUCTION MARGIN is the Calculated unadjusted production for a PROGRAM YEAR.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.production_marg_accr_adjs IS 'PRODUCTION MARGIN ACCRUAL ADJUSTMENTS identifies the production margin associated with accrual adjustments.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.production_marg_aft_str_changs IS 'PRODUCTION MARGIN AFTER STRUCTURE CHANGES is the production margin after structure changes.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.accrual_adjs_crop_inventory IS 'ACCRUAL ADJUSTMENTS CROP INVENTORY IS the Calculated Accrual Adjustments for crop inventory for a PROGRAM YEAR.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.accrual_adjs_lvstck_inventory IS 'ACCRUAL ADJUSTMENTS LIVESTOCK INVENTORY is the Calculated Accrual Adjustments for livestock for a PROGRAM YEAR.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.accrual_adjs_payables IS 'ACCRUAL ADJUSTMENTS PAYABLES are Calculated Accrual Adjustments for payables for a PROGRAM YEAR.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.accrual_adjs_purchased_inputs IS 'ACCRUAL ADJUSTMENTS PURCHASED INPUTS is the Calculated Accrual Adjustments for purchases for a PROGRAM YEAR.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.accrual_adjs_receivables IS 'ACCRUAL ADJUSTMENTS RECEIVABLES is the Calculated Accrual Adjustments for receivables for a PROGRAM YEAR.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.structural_change_adjs IS 'STRUCTURAL CHANGE ADJUSTMENTS is the Calculated Structural Change Adjustments for a PROGRAM YEAR.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.fiscal_year_pro_rate_adjs IS 'FISCAL YEAR PRO RATE ADJUSTMENTS is an adjustment used when financial data for a year is for more or less than 12 months.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.supply_mngd_commodity_income IS 'SUPPLY MANAGED COMMODITY INCOME is the income generated from ''supply managed commodities'' for a PROGRAM YEAR.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.unadjusted_allowable_income IS 'UNADJUSTED ALLOWABLE EXPENSES are the expenses for a PROGRAM YEAR before adjustments.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.yardage_income IS 'YARDAGE INCOME is the income associated with producing custom feed for livestock for a PROGRAM YEAR.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.program_payment_income IS 'PROGRAM PAYMENT INCOME is the income generated though program payments for a PROGRAM YEAR.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.total_unallowable_income IS 'TOTAL UNALLOWABLE INCOME is the unallowable income for a PROGRAM YEAR.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.unadjusted_allowable_expenses IS 'UNADJUSTED ALLOWABLE EXPENSES are the expenses for a PROGRAM YEAR before adjustments.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.yardage_expenses IS 'YARDAGE EXPENSES are the expenses associated with producing custom feed for livestock for a PROGRAM YEAR.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.contract_work_expenses IS 'CONTRACT WORK EXPENSES are the expenses for a PROGRAM YEAR incurred through  contract work.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.manual_expenses IS 'MANUAL EXPENSES are the manual expenses for a PROGRAM YEAR.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.total_unallowable_expenses IS 'TOTAL UNALLOWABLE EXPENSES are the unallowable expenses for a PROGRAM YEAR.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.deferred_program_payments IS 'DEFERRED PROGRAM PAYMENTS are payments from government programs owed for the PROGRAM YEAR. As such they are considered to be receivables rather than actualized income.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.farm_size_ratio IS 'FARM SIZE RATIO is used as an indication of how much the productive capacity of a farm has changed between the reference year and the current PROGRAM YEAR.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.expense_farm_size_ratio IS 'EXPENSE FARM SIZE RATIO is used as an indication of how much the cost of production of a farm has changed between the reference year and the current PROGRAM YEAR.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.structural_change_notable_ind IS 'STRUCTURAL CHANGE NOTABLE INDICATOR identifies whether the structural change is large enough to be used to adjust the production margin.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.bpu_lead_ind IS 'BPU LEAD INDICATOR indicates whether to use BPU values for the reference year, or the previous reference year.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.expense_accr_adjs IS 'EXPENSE ACCRUAL ADJUSTMENTS identifies the expenses adjusted by accruals. Used only for 2013 scenarios and forward.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.expense_structural_chg_adjs IS 'EXPENSE STRUCTURAL CHANGE ADJUSTMENTS identifies the amount to adjust the expenses based on structural change. Used only for 2013 scenarios and forward.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.expenses_aft_str_chg IS 'EXPENSES AFTER STRUCTURE CHANGES is the expenses after structure changes. Used only for 2013 scenarios and forward.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.ratio_structural_change_adjs IS 'RATIO STRUCTURAL CHANGE ADJUSTMENTS is the Calculated Structural Change Adjustments for a PROGRAM YEAR using the Ratio method.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.additive_structural_change_adj IS 'ADDITIVE STRUCTURAL CHANGE ADJUSTMENTS is the Calculated Structural Change Adjustments for a PROGRAM YEAR using the Additive method.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.ratio_prod_marg_aft_str_changs IS 'RATIO PRODUCTION MARGIN AFTEr STRUCTURE CHANGES is the production margin after the Ratio method structure changes.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.additive_prod_marg_aft_str_chg IS 'ADDITIVE PRODUCTION MARGIN AFTER STRUCTURAL CHANGE is the production margin after the Additive method structure changes.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.ratio_struc_chg_notable_ind IS 'RATIO STRUCTURAL CHANGE NOTABLE INDICATOR identifies whether the ratio structural change is large enough to be used to adjust the production margin.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.additive_struc_chg_notable_ind IS 'ADDITIVE STRUCTURAL CHANGE NOTABLE INDICATOR identifies whether the additive structural change is large enough to be used to adjust the production margin.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.agristability_scenario_id IS 'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_totals.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_benefit_calc_totals IS 'BENEFIT CALCULATION TOTAL Is the summation of the BENEFIT CALCULATION MARGINs for an AGRISTABILITY SCENARIO.'
;


CREATE UNIQUE INDEX uk_bct_asi ON farms.farm_benefit_calc_totals(agristability_scenario_id)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_benefit_calc_totals ADD 
    CONSTRAINT pk_bct PRIMARY KEY (benefit_calc_total_id) USING INDEX TABLESPACE pg_default 
;
