CREATE TABLE farms.farm_program_years (
	program_year_id bigint NOT NULL,
	year smallint NOT NULL,
	interim_verification_notes text,
	final_verification_notes text,
	adjustment_verification_notes text,
	assigned_to_user_guid varchar(32),
	assigned_to_userid varchar(64),
	non_participant_ind varchar(1) NOT NULL,
	late_participant_ind varchar(1) NOT NULL DEFAULT 'N',
	cash_margins_ind varchar(1) NOT NULL DEFAULT 'N',
	cash_margins_opt_in_date timestamp(0),
	local_statement_a_receivd_date timestamp(0),
	local_supplemntl_received_date timestamp(0),
	agristability_client_id bigint NOT NULL,
	farm_type_code varchar(10),
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_program_years IS E'PROGRAM YEAR is the taxation year for the given FARMING OPERATION. PROGRAM YEAR will have at least one FARMING OPERATION  associated with it. PROGRAM YEAR will have at least one PROGRAM STATE associated with it.';
COMMENT ON COLUMN farms.farm_program_years.adjustment_verification_notes IS E'ADJUSTMENT VERIFICATION NOTES, also know as job comments, are notes that may be optionally provided by the user about the Administrative Adjustment or Producer Adjustment benefit for the PROGRAM YEAR for the given client. AADJ and PADJ are the SCENARIO CATEGORY CODE for Administrative Adjustment and Producer Adjustment AGRISTABILITY SCECNARIOs.';
COMMENT ON COLUMN farms.farm_program_years.agristability_client_id IS E'AGRISTABILITY CLIENT ID is a surrogate unique identifier for AGRI STABILITY CLIENTs.';
COMMENT ON COLUMN farms.farm_program_years.assigned_to_user_guid IS E'ASSIGNED TO USER GUID describes the name of the user to which the PROGRAM YEAR is assigned. USER GUID describes each user or group in a directory using a unique identifer called a GUID or Globally Unique Identifier. The value is a 128 bit number but is displayed and passed around the netowork as a 32 character hex string. There is no guarantee that a user ID is unique.';
COMMENT ON COLUMN farms.farm_program_years.assigned_to_userid IS E'ASSIGNED TO USERID is to which the PROGRAM YEAR has been assigned. USERID is the user ID associated with the GUID that a user would use to log on with. For this purpose only the username is stored in the record.';
COMMENT ON COLUMN farms.farm_program_years.cash_margins_ind IS E'CASH MARGINS IND is "Y" if the client has opted for their benefit for this program year to be calculated using the Optional Simplified Reference Margin method (also known as Cash Margins) which, among other things, ignores accruals; otherwise "N".';
COMMENT ON COLUMN farms.farm_program_years.cash_margins_opt_in_date IS E'CASH MARGINS OPT IN DATE is the date that the participant submitted a Simplified Reference Margin Consent Form to indicate they want to opt in to Cash Margins.';
COMMENT ON COLUMN farms.farm_program_years.final_verification_notes IS E'FINAL VERIFICATION NOTES, also know as job comments, are notes that may be optionally provided by the user about the Final benefit for the PROGRAM YEAR for the given client. FIN is the SCENARIO CATEGORY CODE for Final AGRISTABILITY SCECNARIOs.';
COMMENT ON COLUMN farms.farm_program_years.interim_verification_notes IS E'INTERIM VERIFICATION NOTES, also known as job comments, are notes that may be optionally provided by the user about the Interim benefit for the PROGRAM YEAR for the given client. INT is the SCENARIO CATEGORY CODE for Interim AGRISTABILITY SCECNARIOs.';
COMMENT ON COLUMN farms.farm_program_years.late_participant_ind IS E'LATE PARTICIPANT IND is "Y" if the client has never been in the program or has previously opted out of the program and applies after the New Participant Deadline Date; otherwise "N".';
COMMENT ON COLUMN farms.farm_program_years.local_statement_a_receivd_date IS E'LOCAL STATEMENT A RECEIVD DATE is the date when Statement A data (includes income and expenses) was received by the province directly from the client rather than from the CRA.';
COMMENT ON COLUMN farms.farm_program_years.local_supplemntl_received_date IS E'LOCAL SUPPLEMNTL RECEIVED DATE is the date when supplemental data was received by the province directly from the client rather than from the CRA.';
COMMENT ON COLUMN farms.farm_program_years.non_participant_ind IS E'NON PARTICIPANT IND is "Y" if the client is not participating in the Agristability Program for this program year; otherwise "N".';
COMMENT ON COLUMN farms.farm_program_years.program_year_id IS E'PROGRAM YEAR ID is a surrogate unique identifier for PROGRAM YEARS.';
COMMENT ON COLUMN farms.farm_program_years.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_program_years.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_program_years.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_program_years.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_program_years.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
COMMENT ON COLUMN farms.farm_program_years.year IS E'YEAR is the PROGRAM YEAR this data pertains to.';
CREATE INDEX farm_py_farm_asc_fk_i ON farms.farm_program_years (agristability_client_id);
CREATE INDEX farm_py_farm_ftc_fk_i ON farms.farm_program_years (farm_type_code);
CREATE INDEX farm_py_year_i ON farms.farm_program_years (year);
ALTER TABLE farms.farm_program_years ADD CONSTRAINT farm_py_pk PRIMARY KEY (program_year_id);
ALTER TABLE farms.farm_program_years ADD CONSTRAINT farm_py_client_year_uk UNIQUE (agristability_client_id,year);
ALTER TABLE farms.farm_program_years ADD CONSTRAINT farm_py_cash_margins_chk CHECK (cash_margins_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_program_years ADD CONSTRAINT farm_py_late_participant_chk CHECK (late_participant_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_program_years ADD CONSTRAINT farm_py_non_participant_chk CHECK (non_participant_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_program_years ALTER COLUMN program_year_id SET NOT NULL;
ALTER TABLE farms.farm_program_years ALTER COLUMN year SET NOT NULL;
ALTER TABLE farms.farm_program_years ALTER COLUMN non_participant_ind SET NOT NULL;
ALTER TABLE farms.farm_program_years ALTER COLUMN late_participant_ind SET NOT NULL;
ALTER TABLE farms.farm_program_years ALTER COLUMN cash_margins_ind SET NOT NULL;
ALTER TABLE farms.farm_program_years ALTER COLUMN agristability_client_id SET NOT NULL;
ALTER TABLE farms.farm_program_years ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_program_years ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_program_years ALTER COLUMN when_created SET NOT NULL;
