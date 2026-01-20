CREATE TABLE farms.farm_benefit_calc_totals (
	benefit_calc_total_id bigint NOT NULL,
	total_allowable_expenses decimal(16,2),
	total_allowable_income decimal(16,2),
	unadjusted_production_margin decimal(16,2),
	production_marg_accr_adjs decimal(16,2),
	production_marg_aft_str_changs decimal(16,2),
	accrual_adjs_crop_inventory decimal(16,2),
	accrual_adjs_lvstck_inventory decimal(16,2),
	accrual_adjs_payables decimal(16,2),
	accrual_adjs_purchased_inputs decimal(16,2),
	accrual_adjs_receivables decimal(16,2),
	structural_change_adjs decimal(16,2),
	fiscal_year_pro_rate_adjs decimal(16,2),
	supply_mngd_commodity_income decimal(16,2),
	unadjusted_allowable_income decimal(16,2),
	yardage_income decimal(16,2),
	program_payment_income decimal(16,2),
	total_unallowable_income decimal(16,2),
	unadjusted_allowable_expenses decimal(16,2),
	yardage_expenses decimal(16,2),
	contract_work_expenses decimal(16,2),
	manual_expenses decimal(16,2),
	total_unallowable_expenses decimal(16,2),
	deferred_program_payments decimal(16,2),
	farm_size_ratio decimal(8,3),
	expense_farm_size_ratio decimal(8,3),
	structural_change_notable_ind varchar(1),
	bpu_lead_ind varchar(1),
	expense_accr_adjs decimal(16,2),
	expense_structural_chg_adjs decimal(16,2),
	expenses_aft_str_chg decimal(16,2),
	ratio_structural_change_adjs decimal(16,2),
	additive_structural_change_adj decimal(16,2),
	ratio_prod_marg_aft_str_changs decimal(16,2),
	additive_prod_marg_aft_str_chg decimal(16,2),
	ratio_struc_chg_notable_ind varchar(1),
	additive_struc_chg_notable_ind varchar(1),
	agristability_scenario_id bigint NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_benefit_calc_totals IS E'BENEFIT CALC TOTAL Is the summation of the BENEFIT CALC MARGINs for an AGRISTABILITY SCENARIO.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.accrual_adjs_crop_inventory IS E'ACCRUAL ADJS CROP INVENTORY IS the Calculated Accrual Adjustments for crop inventory for a PROGRAM YEAR.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.accrual_adjs_lvstck_inventory IS E'ACCRUAL ADJS LVSTCK INVENTORY is the Calculated Accrual Adjustments for livestock for a PROGRAM YEAR.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.accrual_adjs_payables IS E'ACCRUAL ADJS PAYABLES are Calculated Accrual Adjustments for payables for a PROGRAM YEAR.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.accrual_adjs_purchased_inputs IS E'ACCRUAL ADJS PURCHASED INPUTS is the Calculated Accrual Adjustments for purchases for a PROGRAM YEAR.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.accrual_adjs_receivables IS E'ACCRUAL ADJS RECEIVABLES is the Calculated Accrual Adjustments for receivables for a PROGRAM YEAR.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.additive_prod_marg_aft_str_chg IS E'ADDITIVE PROD MARG AFT STR CHG is the production margin after the Additive method structure changes.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.additive_struc_chg_notable_ind IS E'ADDITIVE STRUC CHG NOTABLE IND identifies whether the additive structural change is large enough to be used to adjust the production margin.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.additive_structural_change_adj IS E'ADDITIVE STRUCTURAL CHANGE ADJ is the Calculated Structural Change Adjusments for a PROGRAM YEAR using the Additive method.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.agristability_scenario_id IS E'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.benefit_calc_total_id IS E'BENEFIT CALC TOTAL ID is a surrogate unique identifier for BENEFIT CALC TOTALs.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.bpu_lead_ind IS E'BPU LEAD IND indicates whether to use BPU values for the reference year, or the previous reference year.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.contract_work_expenses IS E'CONTRACT WORK EXPENSES are the expenses for a PROGRAM YEAR incurred through  contract work.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.deferred_program_payments IS E'DEFERRED PROGRAM PAYMENTS are payments from government programs owed for the PROGRAM YEAR. As such they are considered to be receivables rather than actualized income.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.expense_accr_adjs IS E'EXPENSE ACCR ADJS identifies the expenses adjusted by accruals. Used only for 2013 scenarios and forward.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.expense_farm_size_ratio IS E'EXPENSE FARM SIZE RATIO is used as an indication of how much the cost of production of a farm has changed between the reference year and the current PROGRAM YEAR.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.expense_structural_chg_adjs IS E'EXPENSE STRUCTURAL CHANGE ADJS identifies the amount to adjust the expenses based on structural change. Used only for 2013 scenarios and forward.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.expenses_aft_str_chg IS E'EXPENSES AFT STR CHG is the expenses after structure changes. Used only for 2013 scenarios and forward.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.farm_size_ratio IS E'FARM SIZE RATIO is used as an indication of how much the productive capacity of a farm has changed between the reference year and the current PROGRAM YEAR.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.fiscal_year_pro_rate_adjs IS E'FISCAL YEAR PRO RATE ADJS is an adjustment used when financial data for a year is for more or less than 12 months.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.manual_expenses IS E'MANUAL EXPENSES are the manual expenses for a PROGRAM YEAR.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.production_marg_accr_adjs IS E'PRODUCTION MARG ACCR ADJS identifies the production margin associated with accrual adjustments.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.production_marg_aft_str_changs IS E'PRODUCTION MARG AFT STR CHANGS is the production margin after structure changes.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.program_payment_income IS E'PROGRAM PAYMENT INCOME is the income generated though program payments for a PROGRAM YEAR.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.ratio_prod_marg_aft_str_changs IS E'RATIO PROD MARG AFT STR CHANGS is the production margin after the Ratio method structure changes.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.ratio_struc_chg_notable_ind IS E'RATIO STRUC CHG NOTABLE IND identifies whether the ratio structural change is large enough to be used to adjust the production margin.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.ratio_structural_change_adjs IS E'RATIO STRUCTURAL CHANGE ADJS is the Calculated Structural Change Adjusments for a PROGRAM YEAR using the Ratio method.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.structural_change_adjs IS E'STRUCTURAL CHANGE ADJS is the Calculated Structural Change Adjustments for a PROGRAM YEAR.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.structural_change_notable_ind IS E'STRUCTURAL CHANGE NOTABLE IND identifies whether the structural change is large enough to be used to adjust the production margin.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.supply_mngd_commodity_income IS E'SUPPLY MNGD COMMODITY INCOME is the income generated from ''supply managed commodites'' for a PROGRAM YEAR.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.total_allowable_expenses IS E'TOTAL ALLOWABLE EXPENSES are the Calculated expenses for a program year.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.total_allowable_income IS E'TOTAL ALLOWABLE INCOME is the Calculated income for a PROGRAM YEAR.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.total_unallowable_expenses IS E'TOTAL UNALLOWABLE EXPENSES are the unallowable expenses for a PROGRAM YEAR.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.total_unallowable_income IS E'TOTAL UNALLOWABLE INCOME is the unallowable income for a PROGRAM YEAR.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.unadjusted_allowable_expenses IS E'UNADJUSTED ALLOWABLE EXPENSES are the expenses for a PROGRAM YEAR before adjustments.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.unadjusted_allowable_income IS E'UNADJUSTED ALLOWABLE INCOME is the rollup of all UNADJUSTED ALLOWABLE INCOME for a PROGRAM YEAR.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.unadjusted_production_margin IS E'UNADJUSTED PRODUCTION MARGIN is the Calculated unadjusted production for a PROGRAM YEAR.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.yardage_expenses IS E'YARDAGE EXPENSES are the expenses associated with producing custom feed for livestock for a PROGRAM YEAR.';
COMMENT ON COLUMN farms.farm_benefit_calc_totals.yardage_income IS E'YARDAGE INCOME is the income associated with producing custom feed for livestock for a PROGRAM YEAR.';
ALTER TABLE farms.farm_benefit_calc_totals ADD CONSTRAINT farm_bcmt_as_uk UNIQUE (agristability_scenario_id);
ALTER TABLE farms.farm_benefit_calc_totals ADD CONSTRAINT farm_bcmt_pk PRIMARY KEY (benefit_calc_total_id);
ALTER TABLE farms.farm_benefit_calc_totals ADD CONSTRAINT farm_bcmt_ascn_chk CHECK (additive_struc_chg_notable_ind in ('N', 'Y'));
ALTER TABLE farms.farm_benefit_calc_totals ADD CONSTRAINT farm_bcmt_bpul_chk CHECK (bpu_lead_ind in ('N', 'Y'));
ALTER TABLE farms.farm_benefit_calc_totals ADD CONSTRAINT farm_bcmt_rscn_chk CHECK (ratio_struc_chg_notable_ind in ('N', 'Y'));
ALTER TABLE farms.farm_benefit_calc_totals ADD CONSTRAINT farm_bcmt_scn_chk CHECK (structural_change_notable_ind in ('N', 'Y'));
ALTER TABLE farms.farm_benefit_calc_totals ALTER COLUMN benefit_calc_total_id SET NOT NULL;
ALTER TABLE farms.farm_benefit_calc_totals ALTER COLUMN agristability_scenario_id SET NOT NULL;
ALTER TABLE farms.farm_benefit_calc_totals ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_benefit_calc_totals ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_benefit_calc_totals ALTER COLUMN when_created SET NOT NULL;
