CREATE TABLE farms.farm_agristability_scenarios(
    agristability_scenario_id           numeric(10, 0)    NOT NULL,
    scenario_number                     numeric(10, 0)    NOT NULL,
    benefits_calculator_version         varchar(20),
    scenario_created_by                 varchar(30)       NOT NULL,
    default_ind                   varchar(1)        NOT NULL,
    scenario_creation_date              date              NOT NULL,
    description                         varchar(2000),
    combined_farm_number                numeric(10, 0),
    cra_income_expns_received_date    date,
    cra_supplemental_received_date      date,
    program_year_version_id             numeric(10, 0)    NOT NULL,
    scenario_class_code                 varchar(10)       NOT NULL,
    scenario_state_code                 varchar(10)       NOT NULL,
    scenario_category_code              varchar(10)       NOT NULL,
    triage_queue_code                   varchar(10),
    participnt_data_src_code        varchar(10)       NOT NULL,
    chef_submission_id                  numeric(10, 0),
    verifier_user_id                    numeric(10, 0),
    revision_count                      numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                         varchar(30)       NOT NULL,
    when_created                         timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                         varchar(30),
    when_updated                         timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_agristability_scenarios.agristability_scenario_id IS 'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.'
;
COMMENT ON COLUMN farms.farm_agristability_scenarios.scenario_number IS 'SCENARIO NUMBER is the user unique identifier for a AGRISTABILITY SCENARIO.  A SCENARIO NAME must be unique across the same PROGRAM YEAR VERSION.'
;
COMMENT ON COLUMN farms.farm_agristability_scenarios.benefits_calculator_version IS 'BENEFITS CALCULATOR VERSION identifies the version from the benefits calculator spreadsheet used to calculate the claim.'
;
COMMENT ON COLUMN farms.farm_agristability_scenarios.scenario_created_by IS 'SCENARIO CREATED BY is the user who created the AGRISTABILITY SCENARIO.'
;
COMMENT ON COLUMN farms.farm_agristability_scenarios.default_ind IS 'DEFAULT INDICATOR is used to determine which AGRISTABILITY SCENARIO is the currently active record.'
;
COMMENT ON COLUMN farms.farm_agristability_scenarios.scenario_creation_date IS 'SCENARIO CREATION DATE is the date that the scenario was created.'
;
COMMENT ON COLUMN farms.farm_agristability_scenarios.description IS 'DESCRIPTION identifies the user defined name of a save version (or scenario).'
;
COMMENT ON COLUMN farms.farm_agristability_scenarios.combined_farm_number IS 'COMBINED FARM NUMBER links together related AGRISTABILITY SCENARIOS for which a benefit is calculated as a whole.'
;
COMMENT ON COLUMN farms.farm_agristability_scenarios.cra_income_expns_received_date IS 'CRA INCOME EXPENSE RECEIVED DATE is the date when income/expense data was post marked and mailed to the CRA. This refers to data in REPORTED INCOME EXPENSE. Income/expense data is expected to always be included in a CRA import. This date is populated on import using the POST MARK DATE from program year version number 1 of PROGRAM YEAR VERSION.'
;
COMMENT ON COLUMN farms.farm_agristability_scenarios.cra_supplemental_received_date IS 'CRA SUPPLEMENTAL RECEIVED DATE is the date when supplemental data was post marked and mailed to the CRA. This refers to inventory or accruals in REPORTED INVENTORY. This date is populated on import using the POST MARK DATE of the PROGRAM YEAR VERSION.'
;
COMMENT ON COLUMN farms.farm_agristability_scenarios.program_year_version_id IS 'PROGRAM YEAR VERSION ID is a surrogate unique identifier for PROGRAM YEAR VERSIONS.'
;
COMMENT ON COLUMN farms.farm_agristability_scenarios.scenario_class_code IS 'SCENARIO CLASS CODE is a unique code for the SCENARIO CLASS CODE. Examples of codes and descriptions are CRA-Canadian Revenue Agency, USER-User Created, and REF-Reference.'
;
COMMENT ON COLUMN farms.farm_agristability_scenarios.scenario_state_code IS 'AGRISTABILITY STATE CODE is a unique code for the object AGRISTABILITY STATE CODE described as a character code used to uniquely identify the state of the PROGRAM YEAR VERSION. Examples of codes and descriptions are : INP - IN PROGRESS,  APP - APPROVED, PD - PAID.'
;
COMMENT ON COLUMN farms.farm_agristability_scenarios.scenario_category_code IS 'SCENARIO CATEGORY CODE is a unique code for the object SCENARIO CATEGORY CODE described as a character code used to uniquely identify the category of the AGRIBILITY SCENARIO. Examples of codes and descriptions are CRAR - "CRA Received", NCRA - "New CRA Data", INT - "Interim", and FIN - "Final".'
;
COMMENT ON COLUMN farms.farm_agristability_scenarios.triage_queue_code IS 'TRIAGE QUEUE CODE is a unique code for the object TRIAGE QUEUE CODE. Examples of codes and descriptions are DA_ZPP - Data Assessment: Zero Payment - Pass, DA_AZF - Data Assessment: Abbotsford - Zero - Fail.'
;
COMMENT ON COLUMN farms.farm_agristability_scenarios.participnt_data_src_code IS 'PARTICIPANT DATA SOURCE CODE is a unique code for the object PARTICIPANT DATA SOURCE CODE. Examples of codes and descriptions are CRA - CRA, LOCAL - Local.'
;
COMMENT ON COLUMN farms.farm_agristability_scenarios.chef_submission_id IS 'CHEF SUBMISSION ID is a surrogate unique identifier for an CHEF SUBMISSION.'
;
COMMENT ON COLUMN farms.farm_agristability_scenarios.verifier_user_id IS 'USER ID is a surrogate unique identifier for a USER.'
;
COMMENT ON COLUMN farms.farm_agristability_scenarios.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_agristability_scenarios.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_agristability_scenarios.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_agristability_scenarios.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_agristability_scenarios.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_agristability_scenarios IS 'AGRISTABILITY SCENARIO refers to a unique instance of the associated data. AGRISTABILITY SCENARIO will be enforced on Operation, Margin, Claim and all Adjustments data according to specific business rules (i.e. tied to State changes). Many instances of an AGRISTABILITY SCENARIO may exist for the above listed entities.'
;


CREATE INDEX ix_as_cfn ON farms.farm_agristability_scenarios(combined_farm_number)
 TABLESPACE pg_default
;
CREATE INDEX ix_as_di ON farms.farm_agristability_scenarios(default_ind)
 TABLESPACE pg_default
;
CREATE INDEX ix_as_csi ON farms.farm_agristability_scenarios(chef_submission_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_as_pdsc ON farms.farm_agristability_scenarios(participnt_data_src_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_as_pyvi ON farms.farm_agristability_scenarios(program_year_version_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_as_scc ON farms.farm_agristability_scenarios(scenario_category_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_as_ssc ON farms.farm_agristability_scenarios(scenario_state_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_as_scc1 ON farms.farm_agristability_scenarios(scenario_class_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_as_tqc ON farms.farm_agristability_scenarios(triage_queue_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_as_vui ON farms.farm_agristability_scenarios(verifier_user_id)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_as_pyvi_sn ON farms.farm_agristability_scenarios(program_year_version_id, scenario_number)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_agristability_scenarios ADD 
    CONSTRAINT pk_as PRIMARY KEY (agristability_scenario_id) USING INDEX TABLESPACE pg_default 
;
