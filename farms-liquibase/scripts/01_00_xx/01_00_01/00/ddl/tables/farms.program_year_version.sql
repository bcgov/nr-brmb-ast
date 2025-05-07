CREATE TABLE program_year_version(
    program_year_version_id                     numeric(10, 0)    NOT NULL,
    program_year_version_number                 numeric(10, 0)    NOT NULL,
    form_version_number                         numeric(4, 0)     NOT NULL,
    form_version_effective_date                 date              NOT NULL,
    common_share_total                          numeric(8, 0),
    farm_years                                  numeric(2, 0),
    accrual_worksheet_indicator                 varchar(1)        NOT NULL,
    completed_production_cycle_indicator        varchar(1)        NOT NULL,
    cwb_worksheet_indicator                     varchar(1)        NOT NULL,
    perishable_commodities_indicator            varchar(1)        NOT NULL,
    receipts_indicator                          varchar(1)        NOT NULL,
    accrual_cash_conversion_indicator           varchar(1)        NOT NULL,
    combined_farm_indicator                     varchar(1)        NOT NULL,
    coop_member_indicator                       varchar(1)        NOT NULL,
    corporate_shareholder_indicator             varchar(1)        NOT NULL,
    disaster_indicator                          varchar(1)        NOT NULL,
    partnership_member_indicator                varchar(1)        NOT NULL,
    sole_proprietor_indicator                   varchar(1)        NOT NULL,
    other_text                                  varchar(100),
    post_mark_date                              date,
    province_of_residence                       varchar(2),
    received_date                               date,
    last_year_farming_indicator                 varchar(1)        NOT NULL,
    can_send_cob_to_representative_indicator    varchar(1)        NOT NULL,
    province_of_main_farmstead                  varchar(2),
    locally_updated_indicator                   varchar(1)        DEFAULT 'N' NOT NULL,
    program_year_id                             numeric(10, 0)    NOT NULL,
    participant_profile_code                    varchar(10)       NOT NULL,
    municipality_code                           varchar(10),
    import_version_id                           numeric(10, 0),
    federal_status_code                         varchar(10)       NOT NULL,
    revision_count                              numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                                 varchar(30)       NOT NULL,
    create_date                                 timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                                 varchar(30),
    update_date                                 timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN program_year_version.program_year_version_id IS 'PROGRAM YEAR VERSION ID is a surrogate unique identifier for PROGRAM YEAR VERSIONS.'
;
COMMENT ON COLUMN program_year_version.program_year_version_number IS 'PROGRAM YEAR VERSION NUMBER identifies the unique version of the PROGRAM YEAR VERSION for the given PROGRAM YEAR.'
;
COMMENT ON COLUMN program_year_version.form_version_number IS 'FORM VERSION NUMBER distinguishes between different versions of the AgriStability application from the producer. Both the producer and the administration can initiate adjustments that create a new form version in a specific program year.'
;
COMMENT ON COLUMN program_year_version.form_version_effective_date IS 'FORM VERSION EFFECTIVE DATE is the date the form version information was last updated.'
;
COMMENT ON COLUMN program_year_version.common_share_total IS 'COMMON SHARE TOTAL is the Outstanding Common SharesIndividual: zero; Entity: >= zero.a'
;
COMMENT ON COLUMN program_year_version.farm_years IS 'FARM YEARS is the number of years the farm has been in operation.'
;
COMMENT ON COLUMN program_year_version.accrual_worksheet_indicator IS 'ACCRUAL WORKSHEET INDICATOR denotes if the "Accrual Reference Margin Worksheet" box is checked.'
;
COMMENT ON COLUMN program_year_version.completed_production_cycle_indicator IS 'COMPLETED PRODUCTION CYCLE INDICATOR denotes if the "Have you completed a production cycle on at least one of the commodities you produced?" box is checked.'
;
COMMENT ON COLUMN program_year_version.cwb_worksheet_indicator IS 'CWB WORKSHEET INDICATOR denotes if the "CWB Adjustment Worksheet" box is checked.'
;
COMMENT ON COLUMN program_year_version.perishable_commodities_indicator IS 'PERISHABLE COMMODITIES INDICATOR denotes if the Perishable Commodities Worksheet box is checked.'
;
COMMENT ON COLUMN program_year_version.receipts_indicator IS 'RECEIPTS INDICATOR denotes if receipts are available and included with report.'
;
COMMENT ON COLUMN program_year_version.accrual_cash_conversion_indicator IS 'ACCRUAL CASH CONVERSION INDICATOR denotes if the Accrual to Cash / Cash to Accrual Conversions box is checked.'
;
COMMENT ON COLUMN program_year_version.combined_farm_indicator IS 'COMBINED FARM INDICATOR as indicated by the participant.'
;
COMMENT ON COLUMN program_year_version.coop_member_indicator IS 'COOP MEMBER INDICATOR is "Y" if carried on farming business as a member of a co-operative; otherwise "N".'
;
COMMENT ON COLUMN program_year_version.corporate_shareholder_indicator IS 'CORPORATE SHAREHOLDER INDICATOR is "Y"  if carried on farming business as a shareholder of a corporation; otherwise "N".'
;
COMMENT ON COLUMN program_year_version.disaster_indicator IS 'DISASTER INDICATOR denotes if the "were you unable to complete a production cycle due to disaster circumstances?" box is checked.'
;
COMMENT ON COLUMN program_year_version.partnership_member_indicator IS 'PARTNERSHIP MEMBER INDICATOR is "Y" if carried on farming business partner of a partnership; otherwise "N".'
;
COMMENT ON COLUMN program_year_version.sole_proprietor_indicator IS 'SOLE PROPRIETOR INDICATOR is "Y" if carried on farming business as a sole proprietor; otherwise "N".'
;
COMMENT ON COLUMN program_year_version.other_text IS 'OTHER TEXT is any additional justification or supporting details provided by participant or administration.'
;
COMMENT ON COLUMN program_year_version.post_mark_date IS 'POST MARK DATE is the Date the Form was Postmarked. Will be received date if received before filing deadline.'
;
COMMENT ON COLUMN program_year_version.province_of_residence IS 'PROVINCE OF RESIDENCE denotes the province for the operation making the tax submission.'
;
COMMENT ON COLUMN program_year_version.received_date IS 'RECEIVED DATE is the date the form was received by RCT.'
;
COMMENT ON COLUMN program_year_version.last_year_farming_indicator IS 'LAST YEAR FARMING INDICATOR indicates if the current PROGRAM YEAR was the last year of farming for the client.'
;
COMMENT ON COLUMN program_year_version.can_send_cob_to_representative_indicator IS 'CAN SEND COB TO REPRESENTATIVE INDICATOR Indicates that a copy of the Calculation of Benefits (COB) statement should be sent to the contact person.a'
;
COMMENT ON COLUMN program_year_version.province_of_main_farmstead IS 'PROVINCE OF MAIN FARMSTEAD is the main farmstead''s Province in the legal land description.'
;
COMMENT ON COLUMN program_year_version.locally_updated_indicator IS 'LOCALLY UPDATED INDICATOR identifies if the record was updated after being imported into the system. If this value is ''Y'', the client has updated the PROGRAM YEAR VERSION information for a given year; as a result, the FARM system will not allow the overwriting of that particular  PROGRAM YEAR VERSION data by subsequent imports for the same year.'
;
COMMENT ON COLUMN program_year_version.program_year_id IS 'PROGRAM YEAR ID is a surrogate unique identifier for PROGRAM YEARS.'
;
COMMENT ON COLUMN program_year_version.participant_profile_code IS 'PARTICIPANT PROFILE CODE is a unique code for the object PARTICIPANT PROFILE CODE described as a numeric code used to uniquely identify which programs the participant is applying for.  Examples of codes and descriptions are 1 - Agri-Stability Only, 2 - Agri-Invest Only, 3 - Agri-Stability and Agri-Invest. Default = 3.'
;
COMMENT ON COLUMN program_year_version.municipality_code IS 'MUNICIPALITY CODE denotes the municipality of the FARMSTEAD.'
;
COMMENT ON COLUMN program_year_version.import_version_id IS 'IMPORT VERSION ID is a surrogate unique identifier for IMPORT VERSIONs.'
;
COMMENT ON COLUMN program_year_version.federal_status_code IS 'FEDERAL STATUS CODE identifies the federal status code of the application. Possible Values: 1 - Waiting for Data, 2 - In Progress, 3 - Complete - Ineligible, 4 - Complete - Zero Payment, 5 - Complete - Payment.'
;
COMMENT ON COLUMN program_year_version.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN program_year_version.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN program_year_version.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN program_year_version.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN program_year_version.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE program_year_version IS 'PROGRAM YEAR VERSION is an instance of a tax form submission made for a given FARMING OPERATION''s PROGRAM YEAR. A PROGRAM YEAR VERSION may have associated PROGRAM FEE and CLAIM SETTLEMENT PAYMENT data.'
;

