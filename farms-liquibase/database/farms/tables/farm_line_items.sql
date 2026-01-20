CREATE TABLE farms.farm_line_items (
	line_item_id bigint NOT NULL,
	program_year smallint NOT NULL,
	line_item smallint NOT NULL,
	description varchar(256) NOT NULL,
	province varchar(2),
	eligibility_ind varchar(1) NOT NULL,
	eligibility_for_ref_years_ind varchar(1) NOT NULL DEFAULT 'N',
	yardage_ind varchar(1) NOT NULL DEFAULT 'N',
	program_payment_ind varchar(1) NOT NULL DEFAULT 'N',
	contract_work_ind varchar(1) NOT NULL DEFAULT 'N',
	supply_managed_commodity_ind varchar(1) NOT NULL DEFAULT 'N',
	manual_expense_ind varchar(1) NOT NULL DEFAULT 'N',
	exclude_from_revenue_calc_ind varchar(1) NOT NULL DEFAULT 'N',
	industry_average_expense_ind varchar(1) NOT NULL DEFAULT 'N',
	established_date timestamp(0) NOT NULL,
	expiry_date timestamp(0) NOT NULL,
	commodity_type_code varchar(10),
	fruit_veg_type_code varchar(10),
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_line_items IS E'LINE ITEM describes valid incomes and expensed used by Agristability.';
COMMENT ON COLUMN farms.farm_line_items.commodity_type_code IS E'COMMODITY TYPE CODE is a unique code for the object COMMODITY TYPE CODE. Examples of codes and descriptions are GRAIN - Grain, FORAGE - Forage, FORAGESEED - Forage Seed, CATTLE - Cattle.';
COMMENT ON COLUMN farms.farm_line_items.contract_work_ind IS E'CONTRACT WORK IND identifies if the LINE ITEM is associated with contract work.';
COMMENT ON COLUMN farms.farm_line_items.description IS E'DESCRIPTION describes the LINE ITEM.';
COMMENT ON COLUMN farms.farm_line_items.eligibility_for_ref_years_ind IS E'ELIGIBILITY FOR REF YEARS IND identifies if the LINE ITEM is eligible for the specified province in the reference year. Used only for 2013 and forward.';
COMMENT ON COLUMN farms.farm_line_items.eligibility_ind IS E'ELIGIBILITY IND identifies if the LINE ITEM is eligible for the specified province in the program year. For 2012 and past scenarios this affects eligibility for reference years. For 2013 and forward it does not affect reference years.';
COMMENT ON COLUMN farms.farm_line_items.established_date IS E'ESTABLISHED DATE identifies the effective date of the record.';
COMMENT ON COLUMN farms.farm_line_items.exclude_from_revenue_calc_ind IS E'EXCLUDE FROM REVENUE CALC IND identifies if the LINE ITEM should be excluded when calculating the revenue for an AGRISTABILITY SCENARIO.';
COMMENT ON COLUMN farms.farm_line_items.expiry_date IS E'EXPIRY DATE identifies the date this record is no longer valid.';
COMMENT ON COLUMN farms.farm_line_items.fruit_veg_type_code IS E'FRUIT VEG TYPE CODE is a unique code for the object FRUIT VEG TYPE CODE. Examples of codes and descriptions are APPLE - Apples, BEAN - Beans,  POTATO - Potatoes.';
COMMENT ON COLUMN farms.farm_line_items.industry_average_expense_ind IS E'INDUSTRY AVERAGE EXPENSE IND identifies if the LINE ITEM is associated with a common expense used to calculate an industry average. This is used by reasonability tests.';
COMMENT ON COLUMN farms.farm_line_items.line_item IS E'LINE ITEM is income or expense item for Agristability.';
COMMENT ON COLUMN farms.farm_line_items.line_item_id IS E'LINE ITEM ID is a surrogate unique identifier for LINE ITEM IDs.';
COMMENT ON COLUMN farms.farm_line_items.manual_expense_ind IS E'MANUAL EXPENSE IND identifies if the LINE ITEM is associated with a manual expense.';
COMMENT ON COLUMN farms.farm_line_items.program_payment_ind IS E'PROGRAM PAYMENT IND identifies if the LINE ITEM is associated with income or expenses from government programs.';
COMMENT ON COLUMN farms.farm_line_items.program_year IS E'PROGRAM YEAR is the applicable PROGRAM YEAR  for this record.';
COMMENT ON COLUMN farms.farm_line_items.province IS E'PROVINCE identifies the province the code is eligible or not eligible in.';
COMMENT ON COLUMN farms.farm_line_items.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_line_items.supply_managed_commodity_ind IS E'SUPPLY MANAGED COMMODITY IND identifies if the LINE ITEM is associated with ''supply managed'' items such as milk or eggs.';
COMMENT ON COLUMN farms.farm_line_items.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_line_items.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_line_items.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_line_items.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
COMMENT ON COLUMN farms.farm_line_items.yardage_ind IS E'YARDAGE IND identifies if the LINE ITEM is associated with yardage.';
CREATE INDEX farm_li_efrc_ind_i ON farms.farm_line_items (program_year, exclude_from_revenue_calc_ind);
CREATE INDEX farm_li_efry_ind_i ON farms.farm_line_items (program_year, eligibility_for_ref_years_ind);
CREATE INDEX farm_li_eli_ind_i ON farms.farm_line_items (program_year, eligibility_ind);
CREATE INDEX farm_li_expiry_i ON farms.farm_line_items (expiry_date);
CREATE INDEX farm_li_farm_ctc_fk_i ON farms.farm_line_items (commodity_type_code);
CREATE INDEX farm_li_farm_fvtc_fk_i ON farms.farm_line_items (fruit_veg_type_code);
CREATE INDEX farm_li_iae_ind_i ON farms.farm_line_items (program_year, industry_average_expense_ind);
CREATE INDEX farm_li_yr_cw_i ON farms.farm_line_items (program_year, contract_work_ind);
CREATE INDEX farm_li_yr_li_i ON farms.farm_line_items (program_year, line_item);
CREATE INDEX farm_li_yr_me_i ON farms.farm_line_items (program_year, manual_expense_ind);
CREATE INDEX farm_li_yr_pp_i ON farms.farm_line_items (program_year, program_payment_ind);
CREATE INDEX farm_li_yr_smc_i ON farms.farm_line_items (program_year, supply_managed_commodity_ind);
CREATE INDEX farm_li_yr_yd_i ON farms.farm_line_items (program_year, yardage_ind);
ALTER TABLE farms.farm_line_items ADD CONSTRAINT farm_li_pk PRIMARY KEY (line_item_id);
ALTER TABLE farms.farm_line_items ADD CONSTRAINT farm_li_avg_exp_chk CHECK (industry_average_expense_ind in ('N', 'Y'));
ALTER TABLE farms.farm_line_items ADD CONSTRAINT farm_li_contract_work_chk CHECK (contract_work_ind in ('N', 'Y'));
ALTER TABLE farms.farm_line_items ADD CONSTRAINT farm_li_eligibility_chk CHECK (eligibility_ind in ('N', 'Y'));
ALTER TABLE farms.farm_line_items ADD CONSTRAINT farm_li_elig_ref_chk CHECK (eligibility_for_ref_years_ind in ('N', 'Y'));
ALTER TABLE farms.farm_line_items ADD CONSTRAINT farm_li_excl_rev_calc_chk CHECK (exclude_from_revenue_calc_ind in ('N', 'Y'));
ALTER TABLE farms.farm_line_items ADD CONSTRAINT farm_li_manual_expense_chk CHECK (manual_expense_ind in ('N', 'Y'));
ALTER TABLE farms.farm_line_items ADD CONSTRAINT farm_li_program_payment_chk CHECK (program_payment_ind in ('N', 'Y'));
ALTER TABLE farms.farm_line_items ADD CONSTRAINT farm_li_supply_mngd_com_chk CHECK (supply_managed_commodity_ind in ('N', 'Y'));
ALTER TABLE farms.farm_line_items ADD CONSTRAINT farm_li_yardage_chk CHECK (yardage_ind in ('N', 'Y'));
ALTER TABLE farms.farm_line_items ALTER COLUMN line_item_id SET NOT NULL;
ALTER TABLE farms.farm_line_items ALTER COLUMN program_year SET NOT NULL;
ALTER TABLE farms.farm_line_items ALTER COLUMN line_item SET NOT NULL;
ALTER TABLE farms.farm_line_items ALTER COLUMN description SET NOT NULL;
ALTER TABLE farms.farm_line_items ALTER COLUMN eligibility_ind SET NOT NULL;
ALTER TABLE farms.farm_line_items ALTER COLUMN eligibility_for_ref_years_ind SET NOT NULL;
ALTER TABLE farms.farm_line_items ALTER COLUMN yardage_ind SET NOT NULL;
ALTER TABLE farms.farm_line_items ALTER COLUMN program_payment_ind SET NOT NULL;
ALTER TABLE farms.farm_line_items ALTER COLUMN contract_work_ind SET NOT NULL;
ALTER TABLE farms.farm_line_items ALTER COLUMN supply_managed_commodity_ind SET NOT NULL;
ALTER TABLE farms.farm_line_items ALTER COLUMN manual_expense_ind SET NOT NULL;
ALTER TABLE farms.farm_line_items ALTER COLUMN exclude_from_revenue_calc_ind SET NOT NULL;
ALTER TABLE farms.farm_line_items ALTER COLUMN industry_average_expense_ind SET NOT NULL;
ALTER TABLE farms.farm_line_items ALTER COLUMN established_date SET NOT NULL;
ALTER TABLE farms.farm_line_items ALTER COLUMN expiry_date SET NOT NULL;
ALTER TABLE farms.farm_line_items ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_line_items ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_line_items ALTER COLUMN when_created SET NOT NULL;
