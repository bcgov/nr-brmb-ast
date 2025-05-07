CREATE TABLE z22_production_insurance(
    production_insurance_key       numeric(10, 0)    NOT NULL,
    participant_pin                numeric(9, 0)     NOT NULL,
    program_year                   numeric(4, 0)     NOT NULL,
    operation_number               numeric(4, 0)     NOT NULL,
    production_insurance_number    varchar(12)       NOT NULL,
    revision_count                 numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                    varchar(30)       NOT NULL,
    create_date                    timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                    varchar(30),
    update_date                    timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN z22_production_insurance.production_insurance_key IS 'PRODUCTION INSURANCE KEY is th primary key for the file. Provides each row with a unique identifier over the whole file.'
;
COMMENT ON COLUMN z22_production_insurance.participant_pin IS 'PARTICIPANT PIN is the unique AgriStability/AgriInvest pin for this producer. Was previous CAIS Pin and NISA Pin.'
;
COMMENT ON COLUMN z22_production_insurance.program_year IS 'PROGRAM YEAR is the stabilization year for this record.'
;
COMMENT ON COLUMN z22_production_insurance.operation_number IS 'OPERATION NUMBER identifies each operation a participant reports for a given stab year. Operations may have different statement numbers in different program years.'
;
COMMENT ON COLUMN z22_production_insurance.production_insurance_number IS 'PRODUCTION INSURANCE NUMBER is the PRODUCTION INSURANCE NUMBER.'
;
COMMENT ON COLUMN z22_production_insurance.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN z22_production_insurance.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN z22_production_insurance.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN z22_production_insurance.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN z22_production_insurance.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE z22_production_insurance IS 'Z22 PRODUCTION INSURANCE identifies the Insurance contract numbers provided by the participant on the supplemental page of the AgriStability application. This file will have 0 to 4 rows per participant and farming operation, for the current year. This file is created by FIPD. This is a staging object used to load temporary data set before being merged into the operational data.'
;


CREATE INDEX ix_zpi2_pp_py_on ON z22_production_insurance(participant_pin, program_year, operation_number)
;
