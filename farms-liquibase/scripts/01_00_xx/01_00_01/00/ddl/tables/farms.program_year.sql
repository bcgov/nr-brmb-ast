CREATE TABLE farms.farm_program_years(
    program_year_id                     numeric(10, 0)    NOT NULL,
    year                                numeric(4, 0)     NOT NULL,
    interim_verification_notes          text,
    final_verification_notes            text,
    adjustment_verification_notes       text,
    assigned_to_user_guid               varchar(32),
    assigned_to_userid                  varchar(64),
    non_participant_ind           varchar(1)        NOT NULL,
    late_participant_ind          varchar(1)        DEFAULT 'N' NOT NULL,
    cash_margins_ind              varchar(1)        DEFAULT 'N' NOT NULL,
    cash_margins_opt_in_date            date,
    local_statement_a_received_date     date,
    local_supplemntl_received_date    date,
    agristability_client_id             numeric(10, 0)    NOT NULL,
    farm_type_code                      varchar(10),
    revision_count                      numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                         varchar(30)       NOT NULL,
    when_created                         timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                         varchar(30),
    when_updated                         timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_program_years.program_year_id IS 'PROGRAM YEAR ID is a surrogate unique identifier for PROGRAM YEARS.'
;
COMMENT ON COLUMN farms.farm_program_years.year IS 'YEAR is the PROGRAM YEAR this data pertains to.'
;
COMMENT ON COLUMN farms.farm_program_years.interim_verification_notes IS 'INTERIM VERIFICATION NOTES, also known as job comments, are notes that may be optionally provided by the user about the Interim benefit for the PROGRAM YEAR for the given client. INT is the SCENARIO CATEGORY CODE for Interim AGRISTABILITY SCECNARIOs.'
;
COMMENT ON COLUMN farms.farm_program_years.final_verification_notes IS 'FINAL VERIFICATION NOTES, also know as job comments, are notes that may be optionally provided by the user about the Final benefit for the PROGRAM YEAR for the given client. FIN is the SCENARIO CATEGORY CODE for Final AGRISTABILITY SCENARIOs.'
;
COMMENT ON COLUMN farms.farm_program_years.adjustment_verification_notes IS 'ADJUSTMENT VERIFICATION NOTES, also know as job comments, are notes that may be optionally provided by the user about the Administrative Adjustment or Producer Adjustment benefit for the PROGRAM YEAR for the given client. AADJ and PADJ are the SCENARIO CATEGORY CODE for Administrative Adjustment and Producer Adjustment AGRISTABILITY SCENARIOs.'
;
COMMENT ON COLUMN farms.farm_program_years.assigned_to_user_guid IS 'ASSIGNED TO USER GUID describes the name of the user to which the PROGRAM YEAR is assigned. USER GUID describes each user or group in a directory using a unique identifier called a GUID or Globally Unique Identifier. The value is a 128 bit number but is displayed and passed around the network as a 32 character hex string. There is no guarantee that a user ID is unique.'
;
COMMENT ON COLUMN farms.farm_program_years.assigned_to_userid IS 'ASSIGNED TO USERID is to which the PROGRAM YEAR has been assigned. USERID is the user ID associated with the GUID that a user would use to log on with. For this purpose only the username is stored in the record.'
;
COMMENT ON COLUMN farms.farm_program_years.non_participant_ind IS 'NON PARTICIPANT INDICATOR is "Y" if the client is not participating in the Agristability Program for this program year; otherwise "N".'
;
COMMENT ON COLUMN farms.farm_program_years.late_participant_ind IS 'LATE PARTICIPANT INDICATOR is "Y" if the client has never been in the program or has previously opted out of the program and applies after the New Participant Deadline Date; otherwise "N".'
;
COMMENT ON COLUMN farms.farm_program_years.cash_margins_ind IS 'CASH MARGINS INDICATOR is "Y" if the client has opted for their benefit for this program year to be calculated using the Optional Simplified Reference Margin method (also known as Cash Margins) which, among other things, ignores accruals; otherwise "N".'
;
COMMENT ON COLUMN farms.farm_program_years.cash_margins_opt_in_date IS 'CASH MARGINS OPT IN DATE is the date that the participant submitted a Simplified Reference Margin Consent Form to indicate they want to opt in to Cash Margins.'
;
COMMENT ON COLUMN farms.farm_program_years.local_statement_a_received_date IS 'LOCAL STATEMENT A RECEIVED DATE is the date when Statement A data (includes income and expenses) was received by the province directly from the client rather than from the CRA.'
;
COMMENT ON COLUMN farms.farm_program_years.local_supplemntl_received_date IS 'LOCAL SUPPLEMENTAL RECEIVED DATE is the date when supplemental data was received by the province directly from the client rather than from the CRA.'
;
COMMENT ON COLUMN farms.farm_program_years.agristability_client_id IS 'AGRISTABILITY CLIENT ID is a surrogate unique identifier for AGRI STABILITY CLIENTs.'
;
COMMENT ON COLUMN farms.farm_program_years.farm_type_code IS 'FARM TYPE CODE is a unique code for the object FARM TYPE CODE. Examples of codes and descriptions are BERRIES - Berries, FORAGE - Forage, GRAIN - Grain.'
;
COMMENT ON COLUMN farms.farm_program_years.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_program_years.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_program_years.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_program_years.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_program_years.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_program_years IS 'PROGRAM YEAR is the taxation year for the given FARMING OPERATION. PROGRAM YEAR will have at least one FARMING OPERATION  associated with it. PROGRAM YEAR will have at least one PROGRAM STATE associated with it.'
;


CREATE INDEX ix_py_aci ON farms.farm_program_years(agristability_client_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_py_ftc ON farms.farm_program_years(farm_type_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_py_y ON farms.farm_program_years(year)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_py_aci_y ON farms.farm_program_years(agristability_client_id, year)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_program_years ADD 
    CONSTRAINT pk_py PRIMARY KEY (program_year_id) USING INDEX TABLESPACE pg_default 
;
