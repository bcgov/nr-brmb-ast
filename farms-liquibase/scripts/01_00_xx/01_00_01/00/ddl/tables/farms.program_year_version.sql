CREATE TABLE farms.farm_program_year_versions(
    program_year_version_id                     numeric(10, 0)    NOT NULL,
    program_year_version_number                 numeric(10, 0)    NOT NULL,
    form_version_number                         numeric(4, 0)     NOT NULL,
    form_version_effective_date                 date              NOT NULL,
    common_share_total                          numeric(8, 0),
    farm_years                                  numeric(2, 0),
    accrual_worksheet_ind                 varchar(1)        NOT NULL,
    completed_prod_cycle_ind        varchar(1)        NOT NULL,
    cwb_worksheet_ind                     varchar(1)        NOT NULL,
    perishable_commodities_ind            varchar(1)        NOT NULL,
    receipts_ind                          varchar(1)        NOT NULL,
    accrual_cash_conversion_ind           varchar(1)        NOT NULL,
    combined_farm_ind                     varchar(1)        NOT NULL,
    coop_member_ind                       varchar(1)        NOT NULL,
    corporate_shareholder_ind             varchar(1)        NOT NULL,
    disaster_ind                          varchar(1)        NOT NULL,
    partnership_member_ind                varchar(1)        NOT NULL,
    sole_proprietor_ind                   varchar(1)        NOT NULL,
    other_text                                  varchar(100),
    post_mark_date                              date,
    province_of_residence                       varchar(2),
    received_date                               date,
    last_year_farming_ind                 varchar(1)        NOT NULL,
    can_send_cob_to_rep_ind    varchar(1)        NOT NULL,
    province_of_main_farmstead                  varchar(2),
    locally_updated_ind                   varchar(1)        DEFAULT 'N' NOT NULL,
    program_year_id                             numeric(10, 0)    NOT NULL,
    participant_profile_code                    varchar(10)       NOT NULL,
    municipality_code                           varchar(10),
    import_version_id                           numeric(10, 0),
    federal_status_code                         varchar(10)       NOT NULL,
    revision_count                              numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                                 varchar(30)       NOT NULL,
    when_created                                 timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                                 varchar(30),
    when_updated                                 timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_program_year_versions.program_year_version_id IS 'PROGRAM YEAR VERSION ID is a surrogate unique identifier for PROGRAM YEAR VERSIONS.'
;
COMMENT ON COLUMN farms.farm_program_year_versions.program_year_version_number IS 'PROGRAM YEAR VERSION NUMBER identifies the unique version of the PROGRAM YEAR VERSION for the given PROGRAM YEAR.'
;
COMMENT ON COLUMN farms.farm_program_year_versions.form_version_number IS 'FORM VERSION NUMBER distinguishes between different versions of the AgriStability application from the producer. Both the producer and the administration can initiate adjustments that create a new form version in a specific program year.'
;
COMMENT ON COLUMN farms.farm_program_year_versions.form_version_effective_date IS 'FORM VERSION EFFECTIVE DATE is the date the form version information was last updated.'
;
COMMENT ON COLUMN farms.farm_program_year_versions.common_share_total IS 'COMMON SHARE TOTAL is the Outstanding Common SharesIndividual: zero; Entity: >= zero.a'
;
COMMENT ON COLUMN farms.farm_program_year_versions.farm_years IS 'FARM YEARS is the number of years the farm has been in operation.'
;
COMMENT ON COLUMN farms.farm_program_year_versions.accrual_worksheet_ind IS 'ACCRUAL WORKSHEET INDICATOR denotes if the "Accrual Reference Margin Worksheet" box is checked.'
;
COMMENT ON COLUMN farms.farm_program_year_versions.completed_prod_cycle_ind IS 'COMPLETED PRODUCTION CYCLE INDICATOR denotes if the "Have you completed a production cycle on at least one of the commodities you produced?" box is checked.'
;
COMMENT ON COLUMN farms.farm_program_year_versions.cwb_worksheet_ind IS 'CWB WORKSHEET INDICATOR denotes if the "CWB Adjustment Worksheet" box is checked.'
;
COMMENT ON COLUMN farms.farm_program_year_versions.perishable_commodities_ind IS 'PERISHABLE COMMODITIES INDICATOR denotes if the Perishable Commodities Worksheet box is checked.'
;
COMMENT ON COLUMN farms.farm_program_year_versions.receipts_ind IS 'RECEIPTS INDICATOR denotes if receipts are available and included with report.'
;
COMMENT ON COLUMN farms.farm_program_year_versions.accrual_cash_conversion_ind IS 'ACCRUAL CASH CONVERSION INDICATOR denotes if the Accrual to Cash / Cash to Accrual Conversions box is checked.'
;
COMMENT ON COLUMN farms.farm_program_year_versions.combined_farm_ind IS 'COMBINED FARM INDICATOR as indicated by the participant.'
;
COMMENT ON COLUMN farms.farm_program_year_versions.coop_member_ind IS 'COOP MEMBER INDICATOR is "Y" if carried on farming business as a member of a co-operative; otherwise "N".'
;
COMMENT ON COLUMN farms.farm_program_year_versions.corporate_shareholder_ind IS 'CORPORATE SHAREHOLDER INDICATOR is "Y"  if carried on farming business as a shareholder of a corporation; otherwise "N".'
;
COMMENT ON COLUMN farms.farm_program_year_versions.disaster_ind IS 'DISASTER INDICATOR denotes if the "were you unable to complete a production cycle due to disaster circumstances?" box is checked.'
;
COMMENT ON COLUMN farms.farm_program_year_versions.partnership_member_ind IS 'PARTNERSHIP MEMBER INDICATOR is "Y" if carried on farming business partner of a partnership; otherwise "N".'
;
COMMENT ON COLUMN farms.farm_program_year_versions.sole_proprietor_ind IS 'SOLE PROPRIETOR INDICATOR is "Y" if carried on farming business as a sole proprietor; otherwise "N".'
;
COMMENT ON COLUMN farms.farm_program_year_versions.other_text IS 'OTHER TEXT is any additional justification or supporting details provided by participant or administration.'
;
COMMENT ON COLUMN farms.farm_program_year_versions.post_mark_date IS 'POST MARK DATE is the Date the Form was Postmarked. Will be received date if received before filing deadline.'
;
COMMENT ON COLUMN farms.farm_program_year_versions.province_of_residence IS 'PROVINCE OF RESIDENCE denotes the province for the operation making the tax submission.'
;
COMMENT ON COLUMN farms.farm_program_year_versions.received_date IS 'RECEIVED DATE is the date the form was received by RCT.'
;
COMMENT ON COLUMN farms.farm_program_year_versions.last_year_farming_ind IS 'LAST YEAR FARMING INDICATOR indicates if the current PROGRAM YEAR was the last year of farming for the client.'
;
COMMENT ON COLUMN farms.farm_program_year_versions.can_send_cob_to_rep_ind IS 'CAN SEND COB TO REPRESENTATIVE INDICATOR Indicates that a copy of the Calculation of Benefits (COB) statement should be sent to the contact person.a'
;
COMMENT ON COLUMN farms.farm_program_year_versions.province_of_main_farmstead IS 'PROVINCE OF MAIN FARMSTEAD is the main farmstead''s Province in the legal land description.'
;
COMMENT ON COLUMN farms.farm_program_year_versions.locally_updated_ind IS 'LOCALLY UPDATED INDICATOR identifies if the record was updated after being imported into the system. If this value is ''Y'', the client has updated the PROGRAM YEAR VERSION information for a given year; as a result, the FARM system will not allow the overwriting of that particular  PROGRAM YEAR VERSION data by subsequent imports for the same year.'
;
COMMENT ON COLUMN farms.farm_program_year_versions.program_year_id IS 'PROGRAM YEAR ID is a surrogate unique identifier for PROGRAM YEARS.'
;
COMMENT ON COLUMN farms.farm_program_year_versions.participant_profile_code IS 'PARTICIPANT PROFILE CODE is a unique code for the object PARTICIPANT PROFILE CODE described as a numeric code used to uniquely identify which programs the participant is applying for.  Examples of codes and descriptions are 1 - Agri-Stability Only, 2 - Agri-Invest Only, 3 - Agri-Stability and Agri-Invest. Default = 3.'
;
COMMENT ON COLUMN farms.farm_program_year_versions.municipality_code IS 'MUNICIPALITY CODE denotes the municipality of the FARMSTEAD.'
;
COMMENT ON COLUMN farms.farm_program_year_versions.import_version_id IS 'IMPORT VERSION ID is a surrogate unique identifier for IMPORT VERSIONs.'
;
COMMENT ON COLUMN farms.farm_program_year_versions.federal_status_code IS 'FEDERAL STATUS CODE identifies the federal status code of the application. Possible Values: 1 - Waiting for Data, 2 - In Progress, 3 - Complete - Ineligible, 4 - Complete - Zero Payment, 5 - Complete - Payment.'
;
COMMENT ON COLUMN farms.farm_program_year_versions.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_program_year_versions.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_program_year_versions.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_program_year_versions.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_program_year_versions.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_program_year_versions IS 'PROGRAM YEAR VERSION is an instance of a tax form submission made for a given FARMING OPERATION''s PROGRAM YEAR. A PROGRAM YEAR VERSION may have associated PROGRAM FEE and CLAIM SETTLEMENT PAYMENT data.'
;


CREATE INDEX ix_pyv_fsc ON farms.farm_program_year_versions(federal_status_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_pyv_ivi ON farms.farm_program_year_versions(import_version_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_pyv_mc ON farms.farm_program_year_versions(municipality_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_pyv_ppc ON farms.farm_program_year_versions(participant_profile_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_pyv_pyi ON farms.farm_program_year_versions(program_year_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_pyv_pyi_mc ON farms.farm_program_year_versions(program_year_id, municipality_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_pyv_mc_pyi ON farms.farm_program_year_versions(municipality_code, program_year_id)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_program_year_versions ADD 
    CONSTRAINT pk_pyv PRIMARY KEY (program_year_version_id) USING INDEX TABLESPACE pg_default 
;
