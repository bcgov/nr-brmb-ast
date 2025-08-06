CREATE TABLE farms.farm_benefit_calc_margins(
    benefit_calc_margin_id              numeric(10, 0)    NOT NULL,
    total_allowable_expenses                   numeric(13, 2)    NOT NULL,
    total_allowable_income                     numeric(13, 2)    NOT NULL,
    unadjusted_production_margin               numeric(13, 2)    NOT NULL,
    production_marg_accr_adjs      numeric(13, 2),
    accrual_adjs_crop_inventory         numeric(13, 2),
    accrual_adjs_lvstck_inventory    numeric(13, 2),
    accrual_adjs_payables               numeric(13, 2),
    accrual_adjs_purchased_inputs       numeric(13, 2),
    accrual_adjs_receivables            numeric(13, 2),
    supply_mngd_commodity_income            numeric(13, 2),
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
    expense_accr_adjs                      numeric(13, 2),
    prod_insur_deemed_subtotal       numeric(13, 2),
    prod_insur_deemed_total          numeric(13, 2),
    farming_operation_id                       numeric(10, 0)    NOT NULL,
    agristability_scenario_id                  numeric(10, 0)    NOT NULL,
    revision_count                             numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                                varchar(30)       NOT NULL,
    when_created                                timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                                varchar(30),
    when_updated                                timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_benefit_calc_margins.benefit_calc_margin_id IS 'BENEFIT CALCULATION MARGIN ID is a surrogate unique identifier for BENEFIT CALCULATION MARGINs.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_margins.total_allowable_expenses IS 'TOTAL ALLOWABLE EXPENSES are the Calculated expenses for a program year.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_margins.total_allowable_income IS 'TOTAL ALLOWABLE INCOME is the Calculated income for a PROGRAM YEAR.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_margins.unadjusted_production_margin IS 'UNADJUSTED PRODUCTION MARGIN is the Calculated unadjusted production for a PROGRAM YEAR.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_margins.production_marg_accr_adjs IS 'PRODUCTION MARGIN ACCRUAL ADJUSTMENTS identifies the production margin associated with accrual adjustments.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_margins.accrual_adjs_crop_inventory IS 'ACCRUAL ADJUSTMENTS CROP INVENTORY IS the Calculated Accrual Adjustments for crop inventory for a PROGRAM YEAR.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_margins.accrual_adjs_lvstck_inventory IS 'ACCRUAL ADJUSTMENTS LIVESTOCK INVENTORY is the Calculated Accrual Adjustments for livestock for a PROGRAM YEAR.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_margins.accrual_adjs_payables IS 'ACCRUAL ADJUSTMENTS PAYABLES are Calculated Accrual Adjustments for payables for a PROGRAM YEAR.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_margins.accrual_adjs_purchased_inputs IS 'ACCRUAL ADJUSTMENTS PURCHASED INPUTS is the Calculated Accrual Adjustments for purchases for a PROGRAM YEAR.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_margins.accrual_adjs_receivables IS 'ACCRUAL ADJUSTMENTS RECEIVABLES is the Calculated Accrual Adjustments for receivables for a PROGRAM YEAR.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_margins.supply_mngd_commodity_income IS 'SUPPLY MANAGED COMMODITY INCOME is the income generated from ''supply managed commodities'' (which are things such as milk or eggs that have a limit on how much the FARMING OPERATION can produce).'
;
COMMENT ON COLUMN farms.farm_benefit_calc_margins.unadjusted_allowable_income IS 'UNADJUSTED ALLOWABLE INCOME is the FARMING OPERATION income before adjustments.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_margins.yardage_income IS 'YARDAGE INCOME is the income for the FARMING OPERATION associated with producing custom feed for livestock.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_margins.program_payment_income IS 'PROGRAM PAYMENT INCOME is the income for the FARMING OPERATION generated though program payments.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_margins.total_unallowable_income IS 'TOTAL UNALLOWABLE INCOME is the unallowable income for the FARMING OPERATION.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_margins.unadjusted_allowable_expenses IS 'UNADJUSTED ALLOWABLE EXPENSES are the expenses for the FARMING OPERATION before adjustments.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_margins.yardage_expenses IS 'YARDAGE EXPENSES are the expenses for the FARMING OPERATION associated with producing custom feed for livestock.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_margins.contract_work_expenses IS 'CONTRACT WORK EXPENSES are the expenses for the FARMING OPERATION incurred through  contract work.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_margins.manual_expenses IS 'MANUAL EXPENSES are the manual expenses for the FARMING OPERATION.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_margins.total_unallowable_expenses IS 'TOTAL UNALLOWABLE EXPENSES are the unallowable expenses for the FARMING OPERATION .'
;
COMMENT ON COLUMN farms.farm_benefit_calc_margins.deferred_program_payments IS 'DEFERRED PROGRAM PAYMENTS are payments from government programs which the FARMING OPERATION  is owed for the year.  As such they are considered to be receivables rather than actualized income.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_margins.expense_accr_adjs IS 'EXPENSE ACCRUAL ADJUSTED identifies the expenses adjusted by accruals. Used only for 2013 scenarios and forward.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_margins.prod_insur_deemed_subtotal IS 'PRODUCTION INSURANCE DEEMED SUBTOTAL the total deemed Production Insurance before the Negative Margin Payment Percentage is applied.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_margins.prod_insur_deemed_total IS 'PRODUCTION INSURANCE DEEMED TOTAL the total deemed Production Insurance after the Negative Margin Payment Percentage is applied.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_margins.farming_operation_id IS 'FARMING OPERATION ID is a surrogate unique identifier for FARMING OPERATIONs.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_margins.agristability_scenario_id IS 'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_margins.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_margins.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_margins.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_margins.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_margins.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_benefit_calc_margins IS 'BENEFIT CALCULATION MARGIN refer to the difference between revenues and expenses. BENEFIT CALCULATION MARGIN is used for calculating claims - note that claim values will not be calculated within the system, rather they will imported via the federal data imports. There may be many BENEFIT CALCULATION MARGIN associated with a client. There are several types of BENEFIT CALCULATION MARGIN including.'
;


CREATE INDEX ix_bcm_asi ON farms.farm_benefit_calc_margins(agristability_scenario_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_bcm_foi ON farms.farm_benefit_calc_margins(farming_operation_id)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_bcm_asi_foi ON farms.farm_benefit_calc_margins(agristability_scenario_id, farming_operation_id)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_benefit_calc_margins ADD 
    CONSTRAINT pk_bcm PRIMARY KEY (benefit_calc_margin_id) USING INDEX TABLESPACE pg_default 
;
