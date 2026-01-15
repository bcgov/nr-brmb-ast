CREATE TABLE farms.farm_benefit_calc_margins (
	benefit_calc_margin_id bigint NOT NULL,
	total_allowable_expenses decimal(13,2) NOT NULL,
	total_allowable_income decimal(13,2) NOT NULL,
	unadjusted_production_margin decimal(13,2) NOT NULL,
	production_marg_accr_adjs decimal(13,2),
	accrual_adjs_crop_inventory decimal(13,2),
	accrual_adjs_lvstck_inventory decimal(13,2),
	accrual_adjs_payables decimal(13,2),
	accrual_adjs_purchased_inputs decimal(13,2),
	accrual_adjs_receivables decimal(13,2),
	supply_mngd_commodity_income decimal(13,2),
	unadjusted_allowable_income decimal(13,2),
	yardage_income decimal(13,2),
	program_payment_income decimal(13,2),
	total_unallowable_income decimal(13,2),
	unadjusted_allowable_expenses decimal(13,2),
	yardage_expenses decimal(13,2),
	contract_work_expenses decimal(13,2),
	manual_expenses decimal(13,2),
	total_unallowable_expenses decimal(13,2),
	deferred_program_payments decimal(13,2),
	expense_accr_adjs decimal(13,2),
	prod_insur_deemed_subtotal decimal(13,2),
	prod_insur_deemed_total decimal(13,2),
	farming_operation_id bigint NOT NULL,
	agristability_scenario_id bigint NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_benefit_calc_margins IS E'BENEFIT CALC MARGIN refer to the difference between revenues and expenses. BENEFIT CALC MARGIN is used for calculating claims - note that claim values will not be calculated within the system, rather they will imported via the federal data imports. There may be many BENEFIT CALC MARGIN associated with a client. There are several types of BENEFIT CALC MARGIN including.';
COMMENT ON COLUMN farms.farm_benefit_calc_margins.accrual_adjs_crop_inventory IS E'ACCRUAL ADJS CROP INVENTORY IS the Calculated Accrual Adjustments for crop inventory for a PROGRAM YEAR.';
COMMENT ON COLUMN farms.farm_benefit_calc_margins.accrual_adjs_lvstck_inventory IS E'ACCRUAL ADJS LVSTCK INVENTORY is the Calculated Accrual Adjustments for livestock for a PROGRAM YEAR.';
COMMENT ON COLUMN farms.farm_benefit_calc_margins.accrual_adjs_payables IS E'ACCRUAL ADJS PAYABLES are Calculated Accrual Adjustments for payables for a PROGRAM YEAR.';
COMMENT ON COLUMN farms.farm_benefit_calc_margins.accrual_adjs_purchased_inputs IS E'ACCRUAL ADJS PURCHASED INPUTS is the Calculated Accrual Adjustments for purchases for a PROGRAM YEAR.';
COMMENT ON COLUMN farms.farm_benefit_calc_margins.accrual_adjs_receivables IS E'ACCRUAL ADJS RECEIVABLES is the Calculated Accrual Adjustments for receivables for a PROGRAM YEAR.';
COMMENT ON COLUMN farms.farm_benefit_calc_margins.agristability_scenario_id IS E'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.';
COMMENT ON COLUMN farms.farm_benefit_calc_margins.benefit_calc_margin_id IS E'BENEFIT CALC MARGIN ID is a surrogate unique identifier for BENEFIT CALC MARGINs.';
COMMENT ON COLUMN farms.farm_benefit_calc_margins.contract_work_expenses IS E'CONTRACT WORK EXPENSES are the expenses for the FARMING OPERATION incurred through  contract work.';
COMMENT ON COLUMN farms.farm_benefit_calc_margins.deferred_program_payments IS E'DEFERRED PROGRAM PAYMENTS are payments from government programs which the FARMING OPERATION  is owed for the year.  As such they are considered to be receivables rather than actualized income.';
COMMENT ON COLUMN farms.farm_benefit_calc_margins.expense_accr_adjs IS E'EXPENSE ACCR ADJS identifies the expenses adjusted by accruals. Used only for 2013 scenarios and forward.';
COMMENT ON COLUMN farms.farm_benefit_calc_margins.farming_operation_id IS E'FARMING OPERATION ID is a surrogate unique identifier for FARMING OPERATIONs.';
COMMENT ON COLUMN farms.farm_benefit_calc_margins.manual_expenses IS E'MANUAL EXPENSES are the manual expenses for the FARMING OPERATION.';
COMMENT ON COLUMN farms.farm_benefit_calc_margins.prod_insur_deemed_subtotal IS E'PROD INSUR DEEMED SUBTOTAL the total deemed Production Insurance before the Negative Margin Payment Percentage is applied.';
COMMENT ON COLUMN farms.farm_benefit_calc_margins.prod_insur_deemed_total IS E'PROD INSUR DEEMED TOTAL the total deemed Production Insurance after the Negative Margin Payment Percentage is applied.';
COMMENT ON COLUMN farms.farm_benefit_calc_margins.production_marg_accr_adjs IS E'PRODUCTION MARG ACCR ADJS identifies the production margin associated with accrual adjustments.';
COMMENT ON COLUMN farms.farm_benefit_calc_margins.program_payment_income IS E'PROGRAM PAYMENT INCOME is the income for the FARMING OPERATION generated though program payments.';
COMMENT ON COLUMN farms.farm_benefit_calc_margins.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_benefit_calc_margins.supply_mngd_commodity_income IS E'SUPPLY MNGD COMMODITY INCOME is the income generated from ''supply managed commodites'' (which are things such as milk or eggs that have a limit on how much the FARMING OPERATION can produce).';
COMMENT ON COLUMN farms.farm_benefit_calc_margins.total_allowable_expenses IS E'TOTAL ALLOWABLE EXPENSES are the Calculated expenses for a program year.';
COMMENT ON COLUMN farms.farm_benefit_calc_margins.total_allowable_income IS E'TOTAL ALLOWABLE INCOME is the Calculated income for a PROGRAM YEAR.';
COMMENT ON COLUMN farms.farm_benefit_calc_margins.total_unallowable_expenses IS E'TOTAL UNALLOWABLE EXPENSES are the unallowable expenses for the FARMING OPERATION .';
COMMENT ON COLUMN farms.farm_benefit_calc_margins.total_unallowable_income IS E'TOTAL UNALLOWABLE INCOME is the unallowable income for the FARMING OPERATION.';
COMMENT ON COLUMN farms.farm_benefit_calc_margins.unadjusted_allowable_expenses IS E'UNADJUSTED ALLOWABLE EXPENSES are the expenses for the FARMING OPERATION before adjustments.';
COMMENT ON COLUMN farms.farm_benefit_calc_margins.unadjusted_allowable_income IS E'UNADJUSTED ALLOWABLE INCOME is the FARMING OPERATION income before adjustments.';
COMMENT ON COLUMN farms.farm_benefit_calc_margins.unadjusted_production_margin IS E'UNADJUSTED PRODUCTION MARGIN is the Calculated unadjusted production for a PROGRAM YEAR.';
COMMENT ON COLUMN farms.farm_benefit_calc_margins.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_benefit_calc_margins.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_benefit_calc_margins.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_benefit_calc_margins.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
COMMENT ON COLUMN farms.farm_benefit_calc_margins.yardage_expenses IS E'YARDAGE EXPENSES are the expenses for the FARMING OPERATION associated with producing custom feed for livestock.';
COMMENT ON COLUMN farms.farm_benefit_calc_margins.yardage_income IS E'YARDAGE INCOME is the income for the FARMING OPERATION associated with producing custom feed for livestock.';
CREATE INDEX farm_bcm_farm_as_fk_i ON farms.farm_benefit_calc_margins (agristability_scenario_id);
CREATE INDEX farm_bcm_farm_fo_fk_i ON farms.farm_benefit_calc_margins (farming_operation_id);
ALTER TABLE farms.farm_benefit_calc_margins ADD CONSTRAINT farm_bcm_as_fo_uk UNIQUE (agristability_scenario_id,farming_operation_id);
ALTER TABLE farms.farm_benefit_calc_margins ADD CONSTRAINT farm_bcm_pk PRIMARY KEY (benefit_calc_margin_id);
ALTER TABLE farms.farm_benefit_calc_margins ALTER COLUMN benefit_calc_margin_id SET NOT NULL;
ALTER TABLE farms.farm_benefit_calc_margins ALTER COLUMN total_allowable_expenses SET NOT NULL;
ALTER TABLE farms.farm_benefit_calc_margins ALTER COLUMN total_allowable_income SET NOT NULL;
ALTER TABLE farms.farm_benefit_calc_margins ALTER COLUMN unadjusted_production_margin SET NOT NULL;
ALTER TABLE farms.farm_benefit_calc_margins ALTER COLUMN farming_operation_id SET NOT NULL;
ALTER TABLE farms.farm_benefit_calc_margins ALTER COLUMN agristability_scenario_id SET NOT NULL;
ALTER TABLE farms.farm_benefit_calc_margins ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_benefit_calc_margins ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_benefit_calc_margins ALTER COLUMN when_created SET NOT NULL;
