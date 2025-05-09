CREATE TABLE benefit_calculation_total(
    benefit_calculation_total_id                          numeric(10, 0)    NOT NULL,
    total_allowable_expenses                              numeric(16, 2),
    total_allowable_income                                numeric(16, 2),
    unadjusted_production_margin                          numeric(16, 2),
    production_margin_accrual_adjustments                 numeric(16, 2),
    production_margin_after_structure_changes             numeric(16, 2),
    accrual_adjustments_crop_inventory                    numeric(16, 2),
    accrual_adjustments_livestock_inventory               numeric(16, 2),
    accrual_adjustments_payables                          numeric(16, 2),
    accrual_adjustments_purchased_inputs                  numeric(16, 2),
    accrual_adjustments_receivables                       numeric(16, 2),
    structural_change_adjustments                         numeric(16, 2),
    fiscal_year_pro_rate_adjustments                      numeric(16, 2),
    supply_managed_commodity_income                       numeric(16, 2),
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
    structural_change_notable_indicator                   varchar(1),
    bpu_lead_indicator                                    varchar(1),
    expense_accrual_adjustments                           numeric(16, 2),
    expense_structural_change_adjustments                 numeric(16, 2),
    expenses_after_structure_changes                      numeric(16, 2),
    ratio_structural_change_adjustments                   numeric(16, 2),
    additive_structural_change_adjustments                numeric(16, 2),
    ratio_production_margin_after_structure_changes       numeric(16, 2),
    additive_production_margin_after_structural_change    numeric(16, 2),
    ratio_structural_change_notable_indicator             varchar(1),
    additive_structural_change_notable_indicator          varchar(1),
    agristability_scenario_id                             numeric(10, 0)    NOT NULL,
    revision_count                                        numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                                           varchar(30)       NOT NULL,
    create_date                                           timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_user                                           varchar(30),
    update_date                                           timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN benefit_calculation_total.benefit_calculation_total_id IS 'BENEFIT CALCULATION TOTAL ID is a surrogate unique identifier for BENEFIT CALCULATION TOTALs.'
;
COMMENT ON COLUMN benefit_calculation_total.total_allowable_expenses IS 'TOTAL ALLOWABLE EXPENSES are the Calculated expenses for a program year.'
;
COMMENT ON COLUMN benefit_calculation_total.total_allowable_income IS 'TOTAL ALLOWABLE INCOME is the Calculated income for a PROGRAM YEAR.'
;
COMMENT ON COLUMN benefit_calculation_total.unadjusted_production_margin IS 'UNADJUSTED PRODUCTION MARGIN is the Calculated unadjusted production for a PROGRAM YEAR.'
;
COMMENT ON COLUMN benefit_calculation_total.production_margin_accrual_adjustments IS 'PRODUCTION MARGIN ACCRUAL ADJUSTMENTS identifies the production margin associated with accrual adjustments.'
;
COMMENT ON COLUMN benefit_calculation_total.production_margin_after_structure_changes IS 'PRODUCTION MARGIN AFTER STRUCTURE CHANGES is the production margin after structure changes.'
;
COMMENT ON COLUMN benefit_calculation_total.accrual_adjustments_crop_inventory IS 'ACCRUAL ADJUSTMENTS CROP INVENTORY IS the Calculated Accrual Adjustments for crop inventory for a PROGRAM YEAR.'
;
COMMENT ON COLUMN benefit_calculation_total.accrual_adjustments_livestock_inventory IS 'ACCRUAL ADJUSTMENTS LIVESTOCK INVENTORY is the Calculated Accrual Adjustments for livestock for a PROGRAM YEAR.'
;
COMMENT ON COLUMN benefit_calculation_total.accrual_adjustments_payables IS 'ACCRUAL ADJUSTMENTS PAYABLES are Calculated Accrual Adjustments for payables for a PROGRAM YEAR.'
;
COMMENT ON COLUMN benefit_calculation_total.accrual_adjustments_purchased_inputs IS 'ACCRUAL ADJUSTMENTS PURCHASED INPUTS is the Calculated Accrual Adjustments for purchases for a PROGRAM YEAR.'
;
COMMENT ON COLUMN benefit_calculation_total.accrual_adjustments_receivables IS 'ACCRUAL ADJUSTMENTS RECEIVABLES is the Calculated Accrual Adjustments for receivables for a PROGRAM YEAR.'
;
COMMENT ON COLUMN benefit_calculation_total.structural_change_adjustments IS 'STRUCTURAL CHANGE ADJUSTMENTS is the Calculated Structural Change Adjustments for a PROGRAM YEAR.'
;
COMMENT ON COLUMN benefit_calculation_total.fiscal_year_pro_rate_adjustments IS 'FISCAL YEAR PRO RATE ADJUSTMENTS is an adjustment used when financial data for a year is for more or less than 12 months.'
;
COMMENT ON COLUMN benefit_calculation_total.supply_managed_commodity_income IS 'SUPPLY MANAGED COMMODITY INCOME is the income generated from ''supply managed commodities'' for a PROGRAM YEAR.'
;
COMMENT ON COLUMN benefit_calculation_total.unadjusted_allowable_income IS 'UNADJUSTED ALLOWABLE EXPENSES are the expenses for a PROGRAM YEAR before adjustments.'
;
COMMENT ON COLUMN benefit_calculation_total.yardage_income IS 'YARDAGE INCOME is the income associated with producing custom feed for livestock for a PROGRAM YEAR.'
;
COMMENT ON COLUMN benefit_calculation_total.program_payment_income IS 'PROGRAM PAYMENT INCOME is the income generated though program payments for a PROGRAM YEAR.'
;
COMMENT ON COLUMN benefit_calculation_total.total_unallowable_income IS 'TOTAL UNALLOWABLE INCOME is the unallowable income for a PROGRAM YEAR.'
;
COMMENT ON COLUMN benefit_calculation_total.unadjusted_allowable_expenses IS 'UNADJUSTED ALLOWABLE EXPENSES are the expenses for a PROGRAM YEAR before adjustments.'
;
COMMENT ON COLUMN benefit_calculation_total.yardage_expenses IS 'YARDAGE EXPENSES are the expenses associated with producing custom feed for livestock for a PROGRAM YEAR.'
;
COMMENT ON COLUMN benefit_calculation_total.contract_work_expenses IS 'CONTRACT WORK EXPENSES are the expenses for a PROGRAM YEAR incurred through  contract work.'
;
COMMENT ON COLUMN benefit_calculation_total.manual_expenses IS 'MANUAL EXPENSES are the manual expenses for a PROGRAM YEAR.'
;
COMMENT ON COLUMN benefit_calculation_total.total_unallowable_expenses IS 'TOTAL UNALLOWABLE EXPENSES are the unallowable expenses for a PROGRAM YEAR.'
;
COMMENT ON COLUMN benefit_calculation_total.deferred_program_payments IS 'DEFERRED PROGRAM PAYMENTS are payments from government programs owed for the PROGRAM YEAR. As such they are considered to be receivables rather than actualized income.'
;
COMMENT ON COLUMN benefit_calculation_total.farm_size_ratio IS 'FARM SIZE RATIO is used as an indication of how much the productive capacity of a farm has changed between the reference year and the current PROGRAM YEAR.'
;
COMMENT ON COLUMN benefit_calculation_total.expense_farm_size_ratio IS 'EXPENSE FARM SIZE RATIO is used as an indication of how much the cost of production of a farm has changed between the reference year and the current PROGRAM YEAR.'
;
COMMENT ON COLUMN benefit_calculation_total.structural_change_notable_indicator IS 'STRUCTURAL CHANGE NOTABLE INDICATOR identifies whether the structural change is large enough to be used to adjust the production margin.'
;
COMMENT ON COLUMN benefit_calculation_total.bpu_lead_indicator IS 'BPU LEAD INDICATOR indicates whether to use BPU values for the reference year, or the previous reference year.'
;
COMMENT ON COLUMN benefit_calculation_total.expense_accrual_adjustments IS 'EXPENSE ACCRUAL ADJUSTMENTS identifies the expenses adjusted by accruals. Used only for 2013 scenarios and forward.'
;
COMMENT ON COLUMN benefit_calculation_total.expense_structural_change_adjustments IS 'EXPENSE STRUCTURAL CHANGE ADJUSTMENTS identifies the amount to adjust the expenses based on structural change. Used only for 2013 scenarios and forward.'
;
COMMENT ON COLUMN benefit_calculation_total.expenses_after_structure_changes IS 'EXPENSES AFTER STRUCTURE CHANGES is the expenses after structure changes. Used only for 2013 scenarios and forward.'
;
COMMENT ON COLUMN benefit_calculation_total.ratio_structural_change_adjustments IS 'RATIO STRUCTURAL CHANGE ADJUSTMENTS is the Calculated Structural Change Adjustments for a PROGRAM YEAR using the Ratio method.'
;
COMMENT ON COLUMN benefit_calculation_total.additive_structural_change_adjustments IS 'ADDITIVE STRUCTURAL CHANGE ADJUSTMENTS is the Calculated Structural Change Adjustments for a PROGRAM YEAR using the Additive method.'
;
COMMENT ON COLUMN benefit_calculation_total.ratio_production_margin_after_structure_changes IS 'RATIO PRODUCTION MARGIN AFTEr STRUCTURE CHANGES is the production margin after the Ratio method structure changes.'
;
COMMENT ON COLUMN benefit_calculation_total.additive_production_margin_after_structural_change IS 'ADDITIVE PRODUCTION MARGIN AFTER STRUCTURAL CHANGE is the production margin after the Additive method structure changes.'
;
COMMENT ON COLUMN benefit_calculation_total.ratio_structural_change_notable_indicator IS 'RATIO STRUCTURAL CHANGE NOTABLE INDICATOR identifies whether the ratio structural change is large enough to be used to adjust the production margin.'
;
COMMENT ON COLUMN benefit_calculation_total.additive_structural_change_notable_indicator IS 'ADDITIVE STRUCTURAL CHANGE NOTABLE INDICATOR identifies whether the additive structural change is large enough to be used to adjust the production margin.'
;
COMMENT ON COLUMN benefit_calculation_total.agristability_scenario_id IS 'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.'
;
COMMENT ON COLUMN benefit_calculation_total.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN benefit_calculation_total.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN benefit_calculation_total.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN benefit_calculation_total.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN benefit_calculation_total.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE benefit_calculation_total IS 'BENEFIT CALCULATION TOTAL Is the summation of the BENEFIT CALCULATION MARGINs for an AGRISTABILITY SCENARIO.'
;


CREATE UNIQUE INDEX uk_bct_asi ON benefit_calculation_total(agristability_scenario_id)
 TABLESPACE pg_default
;

ALTER TABLE benefit_calculation_total ADD 
    CONSTRAINT pk_bct PRIMARY KEY (benefit_calculation_total_id) USING INDEX TABLESPACE pg_default 
;
