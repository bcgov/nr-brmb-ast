CREATE TABLE farms.farm_program_year_versions (
	program_year_version_id bigint NOT NULL,
	program_year_version_number bigint NOT NULL,
	form_version_number smallint NOT NULL,
	form_version_effective_date timestamp(0) NOT NULL,
	common_share_total integer,
	farm_years smallint,
	accrual_worksheet_ind varchar(1) NOT NULL,
	completed_prod_cycle_ind varchar(1) NOT NULL,
	cwb_worksheet_ind varchar(1) NOT NULL,
	perishable_commodities_ind varchar(1) NOT NULL,
	receipts_ind varchar(1) NOT NULL,
	accrual_cash_conversion_ind varchar(1) NOT NULL,
	combined_farm_ind varchar(1) NOT NULL,
	coop_member_ind varchar(1) NOT NULL,
	corporate_shareholder_ind varchar(1) NOT NULL,
	disaster_ind varchar(1) NOT NULL,
	partnership_member_ind varchar(1) NOT NULL,
	sole_proprietor_ind varchar(1) NOT NULL,
	other_text varchar(100),
	post_mark_date timestamp(0),
	province_of_residence varchar(2),
	received_date timestamp(0),
	last_year_farming_ind varchar(1) NOT NULL,
	can_send_cob_to_rep_ind varchar(1) NOT NULL,
	province_of_main_farmstead varchar(2),
	locally_updated_ind varchar(1) NOT NULL DEFAULT 'N',
	program_year_id bigint NOT NULL,
	participant_profile_code varchar(10) NOT NULL,
	municipality_code varchar(10),
	import_version_id bigint,
	federal_status_code varchar(10) NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_program_year_versions IS E'PROGRAM YEAR VERSION is an instance of a tax form submission made for a given FARMING OPERATION''s PROGRAM YEAR. A PROGRAM YEAR VERSION may have associated PROGRAM FEE and CLAIM SETTLEMENT PAYMENT data.';
COMMENT ON COLUMN farms.farm_program_year_versions.accrual_cash_conversion_ind IS E'ACCRUAL CASH CONVERSION IND denotes if the Accrual to Cash / Cash to Accrual Conversions box is checked.';
COMMENT ON COLUMN farms.farm_program_year_versions.accrual_worksheet_ind IS E'ACCRUAL WORKSHEET IND denotes if the "Accrual Reference Margin Worksheet" box is checked.';
COMMENT ON COLUMN farms.farm_program_year_versions.can_send_cob_to_rep_ind IS E'CAN SEND COB TO REP IND Indicates that a copy of the Calculation of Benefits (COB) statement should be sent to the contact person.a';
COMMENT ON COLUMN farms.farm_program_year_versions.combined_farm_ind IS E'COMBINED FARM IND as indicated by the participant.';
COMMENT ON COLUMN farms.farm_program_year_versions.common_share_total IS E'COMMON SHARE TOTAL is the Outstanding Common SharesIndividual: zero; Entity: >= zero.a';
COMMENT ON COLUMN farms.farm_program_year_versions.completed_prod_cycle_ind IS E'COMPLETED PROD CYCLE IND denotes if the "Have you completed a production cycle on at least one of the commodities you produced?" box is checked.';
COMMENT ON COLUMN farms.farm_program_year_versions.coop_member_ind IS E'COOP MEMBER IND is "Y" if carried on farming business as a member of a co-operative; otherwise "N".';
COMMENT ON COLUMN farms.farm_program_year_versions.corporate_shareholder_ind IS E'CORPORATE SHAREHOLDER IND is "Y"  if carried on farming business as a shareholder of a corporation; otherwise "N".';
COMMENT ON COLUMN farms.farm_program_year_versions.cwb_worksheet_ind IS E'CWB WORKSHEET IND denotes if the "CWB Adjustment Worksheet" box is checked.';
COMMENT ON COLUMN farms.farm_program_year_versions.disaster_ind IS E'DISASTER IND denotes if the "were you unable to complete a production cycle due to disaster circumstances?" box is checked.';
COMMENT ON COLUMN farms.farm_program_year_versions.farm_years IS E'FARM YEARS is the number of years the farm has been in operation.';
COMMENT ON COLUMN farms.farm_program_year_versions.federal_status_code IS E'FEDERAL STATUS CODE identifies the federal status code of the application. Possible Values: 1 - Waiting for Data, 2 - In Progress, 3 - Complete - Ineligible, 4 - Complete - Zero Payment, 5 - Complete - Payment.';
COMMENT ON COLUMN farms.farm_program_year_versions.form_version_effective_date IS E'FORM VERSION EFFECTIVE DATE is the date the form version information was last updated.';
COMMENT ON COLUMN farms.farm_program_year_versions.form_version_number IS E'FORM VERSION NUMBER distinguishes between different versions of the AgriStability application from the producer. Both the producer and the administration can initiate adjustments that create a new form version in a specific program year.';
COMMENT ON COLUMN farms.farm_program_year_versions.import_version_id IS E'IMPORT VERSION ID is a surrogate unique identifier for IMPORT VERSIONs.';
COMMENT ON COLUMN farms.farm_program_year_versions.last_year_farming_ind IS E'LAST YEAR FARMING IND indicates if the current PROGRAM YEAR was the last year of farming for the client.';
COMMENT ON COLUMN farms.farm_program_year_versions.locally_updated_ind IS E'LOCALLY UPDATED IND identifies if the record was updated after being imported into the system. If this value is ''Y'', the client has updated the PROGRAM YEAR VERSION information for a given year; as a result, the FARM system will not allow the overwriting of that particular  PROGRAM YEAR VERSION data by subsequent imports for the same year.';
COMMENT ON COLUMN farms.farm_program_year_versions.municipality_code IS E'MUNICIPALITY CODE denotes the municipality of the FARMSTEAD.';
COMMENT ON COLUMN farms.farm_program_year_versions.other_text IS E'OTHER TEXT is any additional justification or supporting details provided by participant or administration.';
COMMENT ON COLUMN farms.farm_program_year_versions.participant_profile_code IS E'PARTICIPANT PROFILE CODE is a unique code for the object PARTICIPANT PROFILE CODE described as a numeric code used to uniquely identify which programs the participant is applying for.  Examples of codes and descriptions are 1 - Agri-Stability Only, 2 - Agri-Invest Only, 3 - Agri-Stability and Agri-Invest. Default = 3.';
COMMENT ON COLUMN farms.farm_program_year_versions.partnership_member_ind IS E'PARTNERSHIP MEMBER IND is "Y" if carried on farming business partner of a partnership; otherwise "N".';
COMMENT ON COLUMN farms.farm_program_year_versions.perishable_commodities_ind IS E'PERISHABLE COMMODITIES IND denotes if the Perishable Commodities Worksheet box is checked.';
COMMENT ON COLUMN farms.farm_program_year_versions.post_mark_date IS E'POST MARK DATE is the Date the Form was Postmarked. Will be received date if received before filing deadline.';
COMMENT ON COLUMN farms.farm_program_year_versions.program_year_id IS E'PROGRAM YEAR ID is a surrogate unique identifier for PROGRAM YEARS.';
COMMENT ON COLUMN farms.farm_program_year_versions.program_year_version_id IS E'PROGRAM YEAR VERSION ID is a surrogate unique identifier for PROGRAM YEAR VERSIONS.';
COMMENT ON COLUMN farms.farm_program_year_versions.program_year_version_number IS E'PROGRAM YEAR VERSION NUMBER identifies the unique version of the PROGRAM YEAR VERSION for the given PROGRAM YEAR.';
COMMENT ON COLUMN farms.farm_program_year_versions.province_of_main_farmstead IS E'PROVINCE OF MAIN FARMSTEAD is the main farmstead''s Province in the legal land description.';
COMMENT ON COLUMN farms.farm_program_year_versions.province_of_residence IS E'PROVINCE OF RESIDENCE denotes the province for the operation making the tax submission.';
COMMENT ON COLUMN farms.farm_program_year_versions.receipts_ind IS E'RECEIPTS IND denotes if receipts are available and included with report.';
COMMENT ON COLUMN farms.farm_program_year_versions.received_date IS E'RECEIVED DATE is the date the form was received by RCT.';
COMMENT ON COLUMN farms.farm_program_year_versions.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_program_year_versions.sole_proprietor_ind IS E'SOLE PROPRIETOR IND is "Y" if carried on farming business as a sole proprietor; otherwise "N".';
COMMENT ON COLUMN farms.farm_program_year_versions.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_program_year_versions.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_program_year_versions.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_program_year_versions.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_pyv_farm_fsc_fk_i ON farms.farm_program_year_versions (federal_status_code);
CREATE INDEX farm_pyv_farm_iv_fk_i ON farms.farm_program_year_versions (import_version_id);
CREATE INDEX farm_pyv_farm_mc_fk_i ON farms.farm_program_year_versions (municipality_code);
CREATE INDEX farm_pyv_farm_ppc_fk_i ON farms.farm_program_year_versions (participant_profile_code);
CREATE INDEX farm_pyv_farm_py_fk_i ON farms.farm_program_year_versions (program_year_id);
CREATE INDEX farm_pyv_ms_i ON farms.farm_program_year_versions (program_year_id, municipality_code);
CREATE INDEX farm_pyv_muni_pyid_i ON farms.farm_program_year_versions (municipality_code, program_year_id);
ALTER TABLE farms.farm_program_year_versions ADD CONSTRAINT farm_pyv_pk PRIMARY KEY (program_year_version_id);
ALTER TABLE farms.farm_program_year_versions ADD CONSTRAINT avcon_1299283225_accru_000 CHECK (accrual_worksheet_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_program_year_versions ADD CONSTRAINT avcon_1299283225_accru_001 CHECK (accrual_cash_conversion_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_program_year_versions ADD CONSTRAINT avcon_1299283225_can_s_000 CHECK (can_send_cob_to_rep_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_program_year_versions ADD CONSTRAINT avcon_1299283225_combi_000 CHECK (combined_farm_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_program_year_versions ADD CONSTRAINT avcon_1299283225_compl_000 CHECK (completed_prod_cycle_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_program_year_versions ADD CONSTRAINT avcon_1299283225_coop__000 CHECK (coop_member_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_program_year_versions ADD CONSTRAINT avcon_1299283225_corpo_000 CHECK (corporate_shareholder_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_program_year_versions ADD CONSTRAINT avcon_1299283225_cwb_w_000 CHECK (cwb_worksheet_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_program_year_versions ADD CONSTRAINT avcon_1299283225_disas_000 CHECK (disaster_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_program_year_versions ADD CONSTRAINT avcon_1299283225_last__000 CHECK (last_year_farming_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_program_year_versions ADD CONSTRAINT avcon_1299283225_local_004 CHECK (locally_updated_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_program_year_versions ADD CONSTRAINT avcon_1299283225_partn_000 CHECK (partnership_member_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_program_year_versions ADD CONSTRAINT avcon_1299283225_peris_000 CHECK (perishable_commodities_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_program_year_versions ADD CONSTRAINT avcon_1299283225_recei_000 CHECK (receipts_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_program_year_versions ADD CONSTRAINT avcon_1299283225_sole__000 CHECK (sole_proprietor_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_program_year_versions ALTER COLUMN program_year_version_id SET NOT NULL;
ALTER TABLE farms.farm_program_year_versions ALTER COLUMN program_year_version_number SET NOT NULL;
ALTER TABLE farms.farm_program_year_versions ALTER COLUMN form_version_number SET NOT NULL;
ALTER TABLE farms.farm_program_year_versions ALTER COLUMN form_version_effective_date SET NOT NULL;
ALTER TABLE farms.farm_program_year_versions ALTER COLUMN accrual_worksheet_ind SET NOT NULL;
ALTER TABLE farms.farm_program_year_versions ALTER COLUMN completed_prod_cycle_ind SET NOT NULL;
ALTER TABLE farms.farm_program_year_versions ALTER COLUMN cwb_worksheet_ind SET NOT NULL;
ALTER TABLE farms.farm_program_year_versions ALTER COLUMN perishable_commodities_ind SET NOT NULL;
ALTER TABLE farms.farm_program_year_versions ALTER COLUMN receipts_ind SET NOT NULL;
ALTER TABLE farms.farm_program_year_versions ALTER COLUMN accrual_cash_conversion_ind SET NOT NULL;
ALTER TABLE farms.farm_program_year_versions ALTER COLUMN combined_farm_ind SET NOT NULL;
ALTER TABLE farms.farm_program_year_versions ALTER COLUMN coop_member_ind SET NOT NULL;
ALTER TABLE farms.farm_program_year_versions ALTER COLUMN corporate_shareholder_ind SET NOT NULL;
ALTER TABLE farms.farm_program_year_versions ALTER COLUMN disaster_ind SET NOT NULL;
ALTER TABLE farms.farm_program_year_versions ALTER COLUMN partnership_member_ind SET NOT NULL;
ALTER TABLE farms.farm_program_year_versions ALTER COLUMN sole_proprietor_ind SET NOT NULL;
ALTER TABLE farms.farm_program_year_versions ALTER COLUMN last_year_farming_ind SET NOT NULL;
ALTER TABLE farms.farm_program_year_versions ALTER COLUMN can_send_cob_to_rep_ind SET NOT NULL;
ALTER TABLE farms.farm_program_year_versions ALTER COLUMN locally_updated_ind SET NOT NULL;
ALTER TABLE farms.farm_program_year_versions ALTER COLUMN program_year_id SET NOT NULL;
ALTER TABLE farms.farm_program_year_versions ALTER COLUMN participant_profile_code SET NOT NULL;
ALTER TABLE farms.farm_program_year_versions ALTER COLUMN federal_status_code SET NOT NULL;
ALTER TABLE farms.farm_program_year_versions ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_program_year_versions ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_program_year_versions ALTER COLUMN when_created SET NOT NULL;
