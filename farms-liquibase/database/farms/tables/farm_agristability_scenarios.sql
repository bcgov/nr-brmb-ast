CREATE TABLE farms.farm_agristability_scenarios (
	agristability_scenario_id bigint NOT NULL,
	scenario_number bigint NOT NULL,
	benefits_calculator_version varchar(20),
	scenario_created_by varchar(30) NOT NULL,
	default_ind varchar(1) NOT NULL,
	scenario_creation_date timestamp(0) NOT NULL,
	description varchar(2000),
	combined_farm_number bigint,
	cra_income_expns_received_date timestamp(0),
	cra_supplemental_received_date timestamp(0),
	program_year_version_id bigint NOT NULL,
	scenario_class_code varchar(10) NOT NULL,
	scenario_state_code varchar(10) NOT NULL,
	scenario_category_code varchar(10) NOT NULL,
	triage_queue_code varchar(10),
	participnt_data_src_code varchar(10) NOT NULL,
	chef_submission_id bigint,
	verifier_user_id bigint,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_agristability_scenarios IS E'AGRISTABILITY SCENARIO refers to a unique instance of the associated data. AGRISTABILITY SCENARIO will be enforced on Operation, Margin, Claim and all Adjustments data according to specific business rules (i.e. tied to State changes). Many instances of an AGRISTABILITY SCENARIO may exist for the above listed entities.';
COMMENT ON COLUMN farms.farm_agristability_scenarios.agristability_scenario_id IS E'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.';
COMMENT ON COLUMN farms.farm_agristability_scenarios.benefits_calculator_version IS E'BENEFITS CALCULATOR VERSION identifies the version from the benefits calculator spreadsheet used to calculate the claim.';
COMMENT ON COLUMN farms.farm_agristability_scenarios.chef_submission_id IS E'CHEF SUBMISSION ID is a surrogate unique identifier for an CHEF SUBMISSION.';
COMMENT ON COLUMN farms.farm_agristability_scenarios.combined_farm_number IS E'COMBINED FARM NUMBER links together related AGRISTABILITY SCENARIOS for which a benefit is calculated as a whole.';
COMMENT ON COLUMN farms.farm_agristability_scenarios.cra_income_expns_received_date IS E'CRA INCOME EXPNS RECEIVED DATE is the date when income/expense data was post marked and mailed to the CRA. This refers to data in REPORTED INCOME EXPENSE. Income/expense data is expected to always be included in a CRA import. This date is populated on import using the POST MARK DATE from program year version number 1 of PROGRAM YEAR VERSION.';
COMMENT ON COLUMN farms.farm_agristability_scenarios.cra_supplemental_received_date IS E'CRA SUPPLEMENTAL RECEIVED DATE is the date when supplemental data was post marked and mailed to the CRA. This refers to inventory or accruals in REPORTED INVENTORY. This date is populated on import using the POST MARK DATE of the PROGRAM YEAR VERSION.';
COMMENT ON COLUMN farms.farm_agristability_scenarios.default_ind IS E'DEFAULT IND is used to determine which AGRISTABILITY SCENARIO is the currently active record.';
COMMENT ON COLUMN farms.farm_agristability_scenarios.description IS E'DESCRIPTION identifies the user defined name of a save version (or scenario).';
COMMENT ON COLUMN farms.farm_agristability_scenarios.participnt_data_src_code IS E'PARTICIPNT DATA SRC CODE is a unique code for the object PARTICIPNT DATA SRC CODE. Examples of codes and descriptions are CRA - CRA, LOCAL - Local. This indicates which data will be used to calcualte the benefit. Currently this applies only to PRODUCTIVE UNIT CAPACITY data.';
COMMENT ON COLUMN farms.farm_agristability_scenarios.program_year_version_id IS E'PROGRAM YEAR VERSION ID is a surrogate unique identifier for PROGRAM YEAR VERSIONS.';
COMMENT ON COLUMN farms.farm_agristability_scenarios.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_agristability_scenarios.scenario_category_code IS E'SCENARIO CATEGORY CODE is a unique code for the object SCENARIO CATEGORY CODE described as a character code used to uniquely identify the category of the AGRIBILITY SCENARIO. Examples of codes and descriptions are CRAR - "CRA Received", NCRA - "New CRA Data", INT - "Interim", and FIN - "Final".';
COMMENT ON COLUMN farms.farm_agristability_scenarios.scenario_class_code IS E'SCENARIO CLASS CODE is a unique code for the SCENARIO CLASS CODE. Examples of codes and descriptions are CRA-Canadian Revenue Agency, USER-User Created, and REF-Reference.';
COMMENT ON COLUMN farms.farm_agristability_scenarios.scenario_created_by IS E'SCENARIO CREATED BY is the user who created the AGRISTABILITY SCENARIO.';
COMMENT ON COLUMN farms.farm_agristability_scenarios.scenario_creation_date IS E'SCENARIO CREATION DATE is the date that the scenario was created.';
COMMENT ON COLUMN farms.farm_agristability_scenarios.scenario_number IS E'SCENARIO NUMBER is the user unique identifier for a AGRISTABILITY SCENARIO.  A SCENARIO NAME must be unique across the same PROGRAM YEAR VERSION.';
COMMENT ON COLUMN farms.farm_agristability_scenarios.scenario_state_code IS E'SCENARIO STATE CODE is a unique code for the object SCENARIO STATE CODE described as a character code used to uniquely identify the state of the AGRIBILITY SCENARIO. Examples of codes and descriptions are : INP - IN PROGRESS,  APP - APPROVED, CLD - CLOSED.';
COMMENT ON COLUMN farms.farm_agristability_scenarios.triage_queue_code IS E'TRIAGE QUEUE CODE is a unique code for the object TRIAGE QUEUE CODE. Examples of codes and descriptions are DA_ZPP - Data Assessment: Zero Payment - Pass, DA_AZF - Data Assessment: Abbotsford - Zero - Fail.';
COMMENT ON COLUMN farms.farm_agristability_scenarios.verifier_user_id IS E'VERIFIER USER ID is a surrogate unique identifier for a USER. This indicates the primary Verifier who worked on this scenario.';
COMMENT ON COLUMN farms.farm_agristability_scenarios.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_agristability_scenarios.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_agristability_scenarios.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_agristability_scenarios.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_as_cfn_i ON farms.farm_agristability_scenarios (combined_farm_number);
CREATE INDEX farm_as_def_ind_i ON farms.farm_agristability_scenarios (default_ind);
CREATE INDEX farm_as_farm_cs_fk_i ON farms.farm_agristability_scenarios (chef_submission_id);
CREATE INDEX farm_as_farm_pdsc_fk_i ON farms.farm_agristability_scenarios (participnt_data_src_code);
CREATE INDEX farm_as_farm_pyv_fk_i ON farms.farm_agristability_scenarios (program_year_version_id);
CREATE INDEX farm_as_farm_scc_fk_i ON farms.farm_agristability_scenarios (scenario_category_code);
CREATE INDEX farm_as_farm_ssc_fk_i ON farms.farm_agristability_scenarios (scenario_state_code);
CREATE INDEX farm_as_farm_stc_fk_i ON farms.farm_agristability_scenarios (scenario_class_code);
CREATE INDEX farm_as_farm_tqc_fk_i ON farms.farm_agristability_scenarios (triage_queue_code);
CREATE INDEX farm_as_farm_vu_fk_i ON farms.farm_agristability_scenarios (verifier_user_id);
ALTER TABLE farms.farm_agristability_scenarios ADD CONSTRAINT farm_as_pk PRIMARY KEY (agristability_scenario_id);
ALTER TABLE farms.farm_agristability_scenarios ADD CONSTRAINT farm_as_number_uk UNIQUE (program_year_version_id,scenario_number);
ALTER TABLE farms.farm_agristability_scenarios ADD CONSTRAINT farm_as_default_chk CHECK (default_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_agristability_scenarios ALTER COLUMN agristability_scenario_id SET NOT NULL;
ALTER TABLE farms.farm_agristability_scenarios ALTER COLUMN scenario_number SET NOT NULL;
ALTER TABLE farms.farm_agristability_scenarios ALTER COLUMN scenario_created_by SET NOT NULL;
ALTER TABLE farms.farm_agristability_scenarios ALTER COLUMN default_ind SET NOT NULL;
ALTER TABLE farms.farm_agristability_scenarios ALTER COLUMN scenario_creation_date SET NOT NULL;
ALTER TABLE farms.farm_agristability_scenarios ALTER COLUMN program_year_version_id SET NOT NULL;
ALTER TABLE farms.farm_agristability_scenarios ALTER COLUMN scenario_class_code SET NOT NULL;
ALTER TABLE farms.farm_agristability_scenarios ALTER COLUMN scenario_state_code SET NOT NULL;
ALTER TABLE farms.farm_agristability_scenarios ALTER COLUMN scenario_category_code SET NOT NULL;
ALTER TABLE farms.farm_agristability_scenarios ALTER COLUMN participnt_data_src_code SET NOT NULL;
ALTER TABLE farms.farm_agristability_scenarios ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_agristability_scenarios ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_agristability_scenarios ALTER COLUMN when_created SET NOT NULL;
