CREATE TABLE farms.farm_z03_statement_infos (
	operation_number smallint NOT NULL,
	participant_pin integer NOT NULL,
	program_year smallint NOT NULL,
	partnership_pin integer NOT NULL,
	partnership_name varchar(42),
	partnership_percent decimal(6,4) NOT NULL,
	fiscal_year_start varchar(8),
	fiscal_year_end varchar(8),
	accounting_code smallint,
	landlord_ind varchar(1),
	crop_share_ind varchar(1),
	feeder_member_ind varchar(1),
	gross_income decimal(13,2),
	expenses decimal(13,2),
	net_income_before_adj decimal(13,2),
	other_deductions decimal(13,2),
	inventory_adjustments decimal(13,2),
	net_income_after_adj decimal(13,2),
	business_use_of_home_expenses decimal(13,2),
	net_farm_income decimal(13,2),
	crop_disaster_ind varchar(1),
	livestock_disaster_ind varchar(1),
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_z03_statement_infos IS E'Z03 STATEMENT INFO identifies the data from section 3 of the Statement A. Also included are the amounts from Section 6 - Summary of Income and Expense. There will be 1 row in this file per participant, per statement A/B filled out. This file is sent to the provinces by FIPD. This is a staging object used to load temporary data set before being merged into the operational data.';
COMMENT ON COLUMN farms.farm_z03_statement_infos.accounting_code IS E'ACCOUNTING CODE is the Method of Accounting to CRA and AAFC.';
COMMENT ON COLUMN farms.farm_z03_statement_infos.business_use_of_home_expenses IS E'BUSINESS USE OF HOME EXPENSES is the allowable portion of business-use-of-home expenses (9934).';
COMMENT ON COLUMN farms.farm_z03_statement_infos.crop_disaster_ind IS E'CROP DISASTER IND is the productive capacity decreased due to disaster circumstances indicator (from page 7 of the t1273). Allowable values are Y - Yes, N - No.';
COMMENT ON COLUMN farms.farm_z03_statement_infos.crop_share_ind IS E'CROP SHARE IND indicates if this Participant participated in a crop share as a tenant. Allowable values are Y - Yes, N - No.';
COMMENT ON COLUMN farms.farm_z03_statement_infos.expenses IS E'EXPENSES is the Total Farming Expenses (9968).';
COMMENT ON COLUMN farms.farm_z03_statement_infos.feeder_member_ind IS E'FEEDER MEMBER IND indicates if this participant was a member of a feeder association. Allowable values are Y - Yes, N - No.';
COMMENT ON COLUMN farms.farm_z03_statement_infos.fiscal_year_end IS E'FISCAL YEAR END is the Operation FISCAL YEAR END (yyyymmdd - may be blank).';
COMMENT ON COLUMN farms.farm_z03_statement_infos.fiscal_year_start IS E'FISCAL YEAR START is the Operation Fiscal year Start (yyyymmdd - may be blank)';
COMMENT ON COLUMN farms.farm_z03_statement_infos.gross_income IS E'GROSS INCOME is the GROSS INCOME (9959).';
COMMENT ON COLUMN farms.farm_z03_statement_infos.inventory_adjustments IS E'INVENTORY ADJUSTMENTS is the INVENTORY ADJUSTMENTS for the current year(9941 and 9942).';
COMMENT ON COLUMN farms.farm_z03_statement_infos.landlord_ind IS E'LANDLORD IND indicates if this Participant participated in a crop share as a landlord. Allowable values are Y - Yes, N - No.';
COMMENT ON COLUMN farms.farm_z03_statement_infos.livestock_disaster_ind IS E'LIVESTOCK DISASTER IND is the productive capacity decreased due to disaster circumstances indicator (from page 7 of the t1273). Allowable values are Y - Yes, N - No.';
COMMENT ON COLUMN farms.farm_z03_statement_infos.net_farm_income IS E'NET FARM INCOME is Net Farming Income after adjustments (9946).';
COMMENT ON COLUMN farms.farm_z03_statement_infos.net_income_after_adj IS E'NET INCOME AFTER ADJ is the NET INCOME AFTER ADJUSTMENTS amount (9944).';
COMMENT ON COLUMN farms.farm_z03_statement_infos.net_income_before_adj IS E'NET INCOME BEFORE ADJ is the Net Income before Adjustments (9969).';
COMMENT ON COLUMN farms.farm_z03_statement_infos.operation_number IS E'OPERATION NUMBER identifies each operation a participant reports for a given stab year. Operations may have different statement numbers in different program years.';
COMMENT ON COLUMN farms.farm_z03_statement_infos.other_deductions IS E'OTHER DEDUCTIONS is the OTHER DEDUCTIONS to the net income amount. (9940).';
COMMENT ON COLUMN farms.farm_z03_statement_infos.participant_pin IS E'PARTICIPANT PIN is the unique AgriStability/AgriInvest pin for this prodcuer. Was previous CAIS Pin and NISA Pin.';
COMMENT ON COLUMN farms.farm_z03_statement_infos.partnership_name IS E'PARTNERSHIP NAME is the name of the partnership.';
COMMENT ON COLUMN farms.farm_z03_statement_infos.partnership_percent IS E'PARTNERSHIP PERCENT is the percentage of the partnership. (100% will be stored as 1.0).';
COMMENT ON COLUMN farms.farm_z03_statement_infos.partnership_pin IS E'PARTNERSHIP PIN uniquely identifies the partnership. If both the partners in an operation file applications, the same PARTNERSHIP PIN will show up under both pins. PARTNERSHIP PIN will  represent the same operation if/when they are used in different program years.';
COMMENT ON COLUMN farms.farm_z03_statement_infos.program_year IS E'PROGRAM YEAR is the stabilization year for this record.';
COMMENT ON COLUMN farms.farm_z03_statement_infos.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_z03_statement_infos.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_z03_statement_infos.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_z03_statement_infos.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_z03_statement_infos.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_z03_farm_z02_fk_i ON farms.farm_z03_statement_infos (participant_pin, program_year);
ALTER TABLE farms.farm_z03_statement_infos ADD CONSTRAINT farm_z03_pk PRIMARY KEY (participant_pin,program_year,operation_number);
ALTER TABLE farms.farm_z03_statement_infos ALTER COLUMN operation_number SET NOT NULL;
ALTER TABLE farms.farm_z03_statement_infos ALTER COLUMN participant_pin SET NOT NULL;
ALTER TABLE farms.farm_z03_statement_infos ALTER COLUMN program_year SET NOT NULL;
ALTER TABLE farms.farm_z03_statement_infos ALTER COLUMN partnership_pin SET NOT NULL;
ALTER TABLE farms.farm_z03_statement_infos ALTER COLUMN partnership_percent SET NOT NULL;
ALTER TABLE farms.farm_z03_statement_infos ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_z03_statement_infos ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_z03_statement_infos ALTER COLUMN when_created SET NOT NULL;
