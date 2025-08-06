CREATE TABLE farms.farm_z04_income_exps_dtls(
    income_expense_key          numeric(10, 0)    NOT NULL,
    participant_pin             numeric(9, 0)     NOT NULL,
    program_year                numeric(4, 0)     NOT NULL,
    operation_number            numeric(4, 0)     NOT NULL,
    program_year_codified_by    numeric(4, 0)     NOT NULL,
    line_code                   numeric(4, 0)     NOT NULL,
    ie_ind                varchar(1)        NOT NULL,
    amount                      numeric(13, 2)    NOT NULL,
    revision_count              numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                 varchar(30)       NOT NULL,
    when_created                 timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                 varchar(30),
    when_updated                 timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_z04_income_exps_dtls.income_expense_key IS 'INCOME EXPENSE KEY is th Primary key for the file. Provides each row with a unique identifier over the whole file.'
;
COMMENT ON COLUMN farms.farm_z04_income_exps_dtls.participant_pin IS 'PARTICIPANT PIN is the unique AgriStability/AgriInvest pin for this producer. Was previous CAIS Pin and NISA Pin.'
;
COMMENT ON COLUMN farms.farm_z04_income_exps_dtls.program_year IS 'PROGRAM YEAR is the stabilization year for this record.'
;
COMMENT ON COLUMN farms.farm_z04_income_exps_dtls.operation_number IS 'OPERATION NUMBER identifies each operation a participant reports for a given stab year. Operations may have different statement numbers in different program years.'
;
COMMENT ON COLUMN farms.farm_z04_income_exps_dtls.program_year_codified_by IS 'PROGRAM YEAR CODIFIED BY is the PROGRAM YEAR for this record.'
;
COMMENT ON COLUMN farms.farm_z04_income_exps_dtls.line_code IS 'LINE CODE is the Income/Expense code.'
;
COMMENT ON COLUMN farms.farm_z04_income_exps_dtls.ie_ind IS 'IE INDICATOR is the Income / Expense indicator. Allowable values are I - Income, E - Expense.'
;
COMMENT ON COLUMN farms.farm_z04_income_exps_dtls.amount IS 'AMOUNT is the Income / Expense Amount (not adjusted for partnership percent).'
;
COMMENT ON COLUMN farms.farm_z04_income_exps_dtls.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_z04_income_exps_dtls.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_z04_income_exps_dtls.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_z04_income_exps_dtls.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_z04_income_exps_dtls.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_z04_income_exps_dtls IS 'Z04 INCOME EXPENSES LIST identifies all the income(sales) and expense(purchases) listed by the producer on each statement A/B form. There can be multiple rows for a single income or expense code, if this is how the producer filled out the form. This file is sent to the provinces by FIPD. This is a staging object used to load temporary data set before being merged into the operational data.'
;


CREATE INDEX ix_zied_pp_py_on ON farms.farm_z04_income_exps_dtls(participant_pin, program_year, operation_number)
 TABLESPACE pg_default
;
CREATE INDEX ix_zied_pycb_lc ON farms.farm_z04_income_exps_dtls(program_year_codified_by, line_code)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_z04_income_exps_dtls ADD 
    CONSTRAINT pk_zied PRIMARY KEY (income_expense_key) USING INDEX TABLESPACE pg_default 
;
