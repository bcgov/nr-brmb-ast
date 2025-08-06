CREATE TABLE farms.farm_z22_production_insurances(
    production_insurance_key       numeric(10, 0)    NOT NULL,
    participant_pin                numeric(9, 0)     NOT NULL,
    program_year                   numeric(4, 0)     NOT NULL,
    operation_number               numeric(4, 0)     NOT NULL,
    production_insurance_number    varchar(12)       NOT NULL,
    revision_count                 numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                    varchar(30)       NOT NULL,
    when_created                    timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                    varchar(30),
    when_updated                    timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_z22_production_insurances.production_insurance_key IS 'PRODUCTION INSURANCE KEY is th primary key for the file. Provides each row with a unique identifier over the whole file.'
;
COMMENT ON COLUMN farms.farm_z22_production_insurances.participant_pin IS 'PARTICIPANT PIN is the unique AgriStability/AgriInvest pin for this producer. Was previous CAIS Pin and NISA Pin.'
;
COMMENT ON COLUMN farms.farm_z22_production_insurances.program_year IS 'PROGRAM YEAR is the stabilization year for this record.'
;
COMMENT ON COLUMN farms.farm_z22_production_insurances.operation_number IS 'OPERATION NUMBER identifies each operation a participant reports for a given stab year. Operations may have different statement numbers in different program years.'
;
COMMENT ON COLUMN farms.farm_z22_production_insurances.production_insurance_number IS 'PRODUCTION INSURANCE NUMBER is the PRODUCTION INSURANCE NUMBER.'
;
COMMENT ON COLUMN farms.farm_z22_production_insurances.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_z22_production_insurances.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_z22_production_insurances.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_z22_production_insurances.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_z22_production_insurances.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_z22_production_insurances IS 'Z22 PRODUCTION INSURANCE identifies the Insurance contract numbers provided by the participant on the supplemental page of the AgriStability application. This file will have 0 to 4 rows per participant and farming operation, for the current year. This file is created by FIPD. This is a staging object used to load temporary data set before being merged into the operational data.'
;


CREATE INDEX ix_zpi2_pp_py_on ON farms.farm_z22_production_insurances(participant_pin, program_year, operation_number)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_z22_production_insurances ADD 
    CONSTRAINT pk_zpi2 PRIMARY KEY (production_insurance_key) USING INDEX TABLESPACE pg_default 
;
