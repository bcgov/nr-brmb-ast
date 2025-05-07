CREATE TABLE benefit_calculation_margin(
    benefit_calculation_margin_id              numeric(10, 0)    NOT NULL,
    total_allowable_expenses                   numeric(13, 2)    NOT NULL,
    total_allowable_income                     numeric(13, 2)    NOT NULL,
    unadjusted_production_margin               numeric(13, 2)    NOT NULL,
    production_margin_accrual_adjustments      numeric(13, 2),
    accrual_adjustments_crop_inventory         numeric(13, 2),
    accrual_adjustments_livestock_inventory    numeric(13, 2),
    accrual_adjustments_payables               numeric(13, 2),
    accrual_adjustments_purchased_inputs       numeric(13, 2),
    accrual_adjustments_receivables            numeric(13, 2),
    supply_managed_commodity_income            numeric(13, 2),
    unadjusted_allowable_income                numeric(13, 2),
    yardage_income                             numeric(13, 2),
    program_payment_income                     numeric(13, 2),
    total_unallowable_income                   numeric(13, 2),
    unadjusted_allowable_expenses              numeric(13, 2),
    yardage_expenses                           numeric(13, 2),
    contract_work_expenses                     numeric(13, 2),
    manual_expenses                            numeric(13, 2),
    total_unallowable_expenses                 numeric(13, 2),
    deferred_program_payments                  numeric(13, 2),
    expense_accr_adjusted                      numeric(13, 2),
    production_insurance_deemed_subtotal       numeric(13, 2),
    production_insurance_deemed_total          numeric(13, 2),
    farming_operation_id                       numeric(10, 0)    NOT NULL,
    agristability_scenario_id                  numeric(10, 0)    NOT NULL,
    revision_count                             numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                                varchar(30)       NOT NULL,
    create_date                                timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                                varchar(30),
    update_date                                timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN benefit_calculation_margin.benefit_calculation_margin_id IS 'BENEFIT CALCULATION MARGIN ID is a surrogate unique identifier for BENEFIT CALCULATION MARGINs.'
;
COMMENT ON COLUMN benefit_calculation_margin.total_allowable_expenses IS 'TOTAL ALLOWABLE EXPENSES are the Calculated expenses for a program year.'
;
COMMENT ON COLUMN benefit_calculation_margin.total_allowable_income IS 'TOTAL ALLOWABLE INCOME is the Calculated income for a PROGRAM YEAR.'
;
COMMENT ON COLUMN benefit_calculation_margin.unadjusted_production_margin IS 'UNADJUSTED PRODUCTION MARGIN is the Calculated unadjusted production for a PROGRAM YEAR.'
;
COMMENT ON COLUMN benefit_calculation_margin.production_margin_accrual_adjustments IS 'PRODUCTION MARGIN ACCRUAL ADJUSTMENTS identifies the production margin associated with accrual adjustments.'
;
COMMENT ON COLUMN benefit_calculation_margin.accrual_adjustments_crop_inventory IS 'ACCRUAL ADJUSTMENTS CROP INVENTORY IS the Calculated Accrual Adjustments for crop inventory for a PROGRAM YEAR.'
;
COMMENT ON COLUMN benefit_calculation_margin.accrual_adjustments_livestock_inventory IS 'ACCRUAL ADJUSTMENTS LIVESTOCK INVENTORY is the Calculated Accrual Adjustments for livestock for a PROGRAM YEAR.'
;
COMMENT ON COLUMN benefit_calculation_margin.accrual_adjustments_payables IS 'ACCRUAL ADJUSTMENTS PAYABLES are Calculated Accrual Adjustments for payables for a PROGRAM YEAR.'
;
COMMENT ON COLUMN benefit_calculation_margin.accrual_adjustments_purchased_inputs IS 'ACCRUAL ADJUSTMENTS PURCHASED INPUTS is the Calculated Accrual Adjustments for purchases for a PROGRAM YEAR.'
;
COMMENT ON COLUMN benefit_calculation_margin.accrual_adjustments_receivables IS 'ACCRUAL ADJUSTMENTS RECEIVABLES is the Calculated Accrual Adjustments for receivables for a PROGRAM YEAR.'
;
COMMENT ON COLUMN benefit_calculation_margin.supply_managed_commodity_income IS 'SUPPLY MANAGED COMMODITY INCOME is the income generated from ''supply managed commodities'' (which are things such as milk or eggs that have a limit on how much the FARMING OPERATION can produce).'
;
COMMENT ON COLUMN benefit_calculation_margin.unadjusted_allowable_income IS 'UNADJUSTED ALLOWABLE INCOME is the FARMING OPERATION income before adjustments.'
;
COMMENT ON COLUMN benefit_calculation_margin.yardage_income IS 'YARDAGE INCOME is the income for the FARMING OPERATION associated with producing custom feed for livestock.'
;
COMMENT ON COLUMN benefit_calculation_margin.program_payment_income IS 'PROGRAM PAYMENT INCOME is the income for the FARMING OPERATION generated though program payments.'
;
COMMENT ON COLUMN benefit_calculation_margin.total_unallowable_income IS 'TOTAL UNALLOWABLE INCOME is the unallowable income for the FARMING OPERATION.'
;
COMMENT ON COLUMN benefit_calculation_margin.unadjusted_allowable_expenses IS 'UNADJUSTED ALLOWABLE EXPENSES are the expenses for the FARMING OPERATION before adjustments.'
;
COMMENT ON COLUMN benefit_calculation_margin.yardage_expenses IS 'YARDAGE EXPENSES are the expenses for the FARMING OPERATION associated with producing custom feed for livestock.'
;
COMMENT ON COLUMN benefit_calculation_margin.contract_work_expenses IS 'CONTRACT WORK EXPENSES are the expenses for the FARMING OPERATION incurred through  contract work.'
;
COMMENT ON COLUMN benefit_calculation_margin.manual_expenses IS 'MANUAL EXPENSES are the manual expenses for the FARMING OPERATION.'
;
COMMENT ON COLUMN benefit_calculation_margin.total_unallowable_expenses IS 'TOTAL UNALLOWABLE EXPENSES are the unallowable expenses for the FARMING OPERATION .'
;
COMMENT ON COLUMN benefit_calculation_margin.deferred_program_payments IS 'DEFERRED PROGRAM PAYMENTS are payments from government programs which the FARMING OPERATION  is owed for the year.  As such they are considered to be receivables rather than actualized income.'
;
COMMENT ON COLUMN benefit_calculation_margin.expense_accr_adjusted IS 'EXPENSE ACCRUAL ADJUSTED identifies the expenses adjusted by accruals. Used only for 2013 scenarios and forward.'
;
COMMENT ON COLUMN benefit_calculation_margin.production_insurance_deemed_subtotal IS 'PRODUCTION INSURANCE DEEMED SUBTOTAL the total deemed Production Insurance before the Negative Margin Payment Percentage is applied.'
;
COMMENT ON COLUMN benefit_calculation_margin.production_insurance_deemed_total IS 'PRODUCTION INSURANCE DEEMED TOTAL the total deemed Production Insurance after the Negative Margin Payment Percentage is applied.'
;
COMMENT ON COLUMN benefit_calculation_margin.farming_operation_id IS 'FARMING OPERATION ID is a surrogate unique identifier for FARMING OPERATIONs.'
;
COMMENT ON COLUMN benefit_calculation_margin.agristability_scenario_id IS 'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.'
;
COMMENT ON COLUMN benefit_calculation_margin.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN benefit_calculation_margin.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN benefit_calculation_margin.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN benefit_calculation_margin.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN benefit_calculation_margin.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE benefit_calculation_margin IS 'BENEFIT CALCULATION MARGIN refer to the difference between revenues and expenses. BENEFIT CALCULATION MARGIN is used for calculating claims - note that claim values will not be calculated within the system, rather they will imported via the federal data imports. There may be many BENEFIT CALCULATION MARGIN associated with a client. There are several types of BENEFIT CALCULATION MARGIN including.'
;


CREATE INDEX ix_bcm_asi ON benefit_calculation_margin(agristability_scenario_id)
;
CREATE INDEX ix_bcm_foi ON benefit_calculation_margin(farming_operation_id)
;
CREATE UNIQUE INDEX uk_bcm_asi_foi ON benefit_calculation_margin(agristability_scenario_id, farming_operation_id)
;
