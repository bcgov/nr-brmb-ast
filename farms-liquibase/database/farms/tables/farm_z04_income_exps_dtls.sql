CREATE TABLE farms.farm_z04_income_exps_dtls (
	income_expense_key bigint NOT NULL,
	participant_pin integer NOT NULL,
	program_year smallint NOT NULL,
	operation_number smallint NOT NULL,
	program_year_codified_by smallint NOT NULL,
	line_code smallint NOT NULL,
	ie_ind varchar(1) NOT NULL,
	amount decimal(13,2) NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_z04_income_exps_dtls IS E'Z04 INCOME EXPS DTL identifies all the income(sales) and expense(purchases) listed by the producer on each statement A/B form. There can be multiple rows for a single income or expense code, if this is how the producer filled out the form. This file is sent to the provinces by FIPD. This is a staging object used to load temporary data set before being merged into the operational data.';
COMMENT ON COLUMN farms.farm_z04_income_exps_dtls.amount IS E'AMOUNT is the Income / Expense Amount (not adjusted for prshp pct).';
COMMENT ON COLUMN farms.farm_z04_income_exps_dtls.ie_ind IS E'IE IND is the Income / Expense indicator. Allowable values are I - Income, E - Expense.';
COMMENT ON COLUMN farms.farm_z04_income_exps_dtls.income_expense_key IS E'INCOME EXPENSE KEY is th Primary key for the file. Provides each row with a unique identifier over the whole file.';
COMMENT ON COLUMN farms.farm_z04_income_exps_dtls.line_code IS E'LINE CODE is the Income/Expense code.';
COMMENT ON COLUMN farms.farm_z04_income_exps_dtls.operation_number IS E'OPERATION NUMBER identifies each operation a participant reports for a given stab year. Operations may have different statement numbers in different program years.';
COMMENT ON COLUMN farms.farm_z04_income_exps_dtls.participant_pin IS E'PARTICIPANT PIN is the unique AgriStability/AgriInvest pin for this prodcuer. Was previous CAIS Pin and NISA Pin.';
COMMENT ON COLUMN farms.farm_z04_income_exps_dtls.program_year IS E'PROGRAM YEAR is the stabilization year for this record.';
COMMENT ON COLUMN farms.farm_z04_income_exps_dtls.program_year_codified_by IS E'PROGRAM YEAR is the PROGRAM YEAR for this record.';
COMMENT ON COLUMN farms.farm_z04_income_exps_dtls.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_z04_income_exps_dtls.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_z04_income_exps_dtls.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_z04_income_exps_dtls.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_z04_income_exps_dtls.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_z04_farm_z03_fk_i ON farms.farm_z04_income_exps_dtls (participant_pin, program_year, operation_number);
CREATE INDEX farm_z04_farm_z07_fk_i ON farms.farm_z04_income_exps_dtls (program_year_codified_by, line_code);
ALTER TABLE farms.farm_z04_income_exps_dtls ADD CONSTRAINT farm_z04_pk PRIMARY KEY (income_expense_key);
ALTER TABLE farms.farm_z04_income_exps_dtls ADD CONSTRAINT farm_z04_yr_chk CHECK (program_year = program_year_codified_by);
ALTER TABLE farms.farm_z04_income_exps_dtls ALTER COLUMN income_expense_key SET NOT NULL;
ALTER TABLE farms.farm_z04_income_exps_dtls ALTER COLUMN participant_pin SET NOT NULL;
ALTER TABLE farms.farm_z04_income_exps_dtls ALTER COLUMN program_year SET NOT NULL;
ALTER TABLE farms.farm_z04_income_exps_dtls ALTER COLUMN operation_number SET NOT NULL;
ALTER TABLE farms.farm_z04_income_exps_dtls ALTER COLUMN program_year_codified_by SET NOT NULL;
ALTER TABLE farms.farm_z04_income_exps_dtls ALTER COLUMN line_code SET NOT NULL;
ALTER TABLE farms.farm_z04_income_exps_dtls ALTER COLUMN ie_ind SET NOT NULL;
ALTER TABLE farms.farm_z04_income_exps_dtls ALTER COLUMN amount SET NOT NULL;
ALTER TABLE farms.farm_z04_income_exps_dtls ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_z04_income_exps_dtls ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_z04_income_exps_dtls ALTER COLUMN when_created SET NOT NULL;
