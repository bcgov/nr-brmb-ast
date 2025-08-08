CREATE TABLE farms.farm_z23_livestock_prod_cpcts(
    productive_capacity_key       numeric(10, 0)    NOT NULL,
    participant_pin               numeric(9, 0)     NOT NULL,
    program_year                  numeric(4, 0)     NOT NULL,
    operation_number              numeric(4, 0)     NOT NULL,
    inventory_code                numeric(5, 0)     NOT NULL,
    productive_capacity_amount    numeric(14, 3)    NOT NULL,
    revision_count                numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                   varchar(30)       NOT NULL,
    when_created                   timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                   varchar(30),
    when_updated                   timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_z23_livestock_prod_cpcts.productive_capacity_key IS 'PRODUCTIVE CAPACITY KEY is the primary key for the file. Provides each row with a unique identifier over the whole file.'
;
COMMENT ON COLUMN farms.farm_z23_livestock_prod_cpcts.participant_pin IS 'PARTICIPANT PIN is the unique AgriStability/AgriInvest pin for this producer. Was previous CAIS Pin and NISA Pin.'
;
COMMENT ON COLUMN farms.farm_z23_livestock_prod_cpcts.program_year IS 'PROGRAM YEAR is the stabilization year for this record.'
;
COMMENT ON COLUMN farms.farm_z23_livestock_prod_cpcts.operation_number IS 'OPERATION NUMBER identifies each operation a participant reports for a given stab year. Operations may have different statement numbers in different program years.'
;
COMMENT ON COLUMN farms.farm_z23_livestock_prod_cpcts.inventory_code IS 'INVENTORY CODE is a numeric code used to uniquely identify an inventory item.'
;
COMMENT ON COLUMN farms.farm_z23_livestock_prod_cpcts.productive_capacity_amount IS 'PRODUCTIVE CAPACITY AMOUNT is the quantity entered in section 9 for this inventory/bpu code.'
;
COMMENT ON COLUMN farms.farm_z23_livestock_prod_cpcts.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_z23_livestock_prod_cpcts.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_z23_livestock_prod_cpcts.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_z23_livestock_prod_cpcts.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_z23_livestock_prod_cpcts.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_z23_livestock_prod_cpcts IS 'Z23 LIVESTOCK PRODUCTION CAPACITY identifies the Livestock production capacity information provided by the participant in section 9 of the supplemental page of the AgriStability application. This file will have 0 to many rows per participant and farming operation.This file is created by FIPD. This is a staging object used to load temporary data set before being merged into the operational data.'
;


CREATE INDEX ix_zlpc_pp_py_on ON farms.farm_z23_livestock_prod_cpcts(participant_pin, program_year, operation_number)
 TABLESPACE pg_default
;
CREATE INDEX ix_zlpc_pp_py ON farms.farm_z23_livestock_prod_cpcts(participant_pin, program_year)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_z23_livestock_prod_cpcts ADD 
    CONSTRAINT pk_zlpc PRIMARY KEY (productive_capacity_key) USING INDEX TABLESPACE pg_default 
;
