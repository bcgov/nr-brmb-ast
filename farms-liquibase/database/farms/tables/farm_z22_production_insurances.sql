CREATE TABLE farms.farm_z22_production_insurances (
	production_insurance_key bigint NOT NULL,
	participant_pin integer NOT NULL,
	program_year smallint NOT NULL,
	operation_number smallint NOT NULL,
	production_insurance_number varchar(12) NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_z22_production_insurances IS E'Z22 PRODUCTION INSURANCE identifies the Insurance contract numbers provided by the participant on the supplemental page of the AgriStability application. This file will have 0 to 4 rows per participant and farming operation, for the current year. This file is created by FIPD. This is a staging object used to load temporary data set before being merged into the operational data.';
COMMENT ON COLUMN farms.farm_z22_production_insurances.operation_number IS E'OPERATION NUMBER identifies each operation a participant reports for a given stab year. Operations may have different statement numbers in different program years.';
COMMENT ON COLUMN farms.farm_z22_production_insurances.participant_pin IS E'PARTICIPANT PIN is the unique AgriStability/AgriInvest pin for this prodcuer. Was previous CAIS Pin and NISA Pin.';
COMMENT ON COLUMN farms.farm_z22_production_insurances.production_insurance_key IS E'PRODUCTION INSURANCE KEY is th primary key for the file. Provides each row with a unique identifier over the whole file.';
COMMENT ON COLUMN farms.farm_z22_production_insurances.production_insurance_number IS E'PRODUCTION INSURANCE NUMBER is the PRODUCTION INSURANCE NUMBER.';
COMMENT ON COLUMN farms.farm_z22_production_insurances.program_year IS E'PROGRAM YEAR is the stabilization year for this record.';
COMMENT ON COLUMN farms.farm_z22_production_insurances.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_z22_production_insurances.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_z22_production_insurances.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_z22_production_insurances.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_z22_production_insurances.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_z22_farm_z03_fk_i ON farms.farm_z22_production_insurances (participant_pin, program_year, operation_number);
ALTER TABLE farms.farm_z22_production_insurances ADD CONSTRAINT farm_z22_pk PRIMARY KEY (production_insurance_key);
ALTER TABLE farms.farm_z22_production_insurances ALTER COLUMN production_insurance_key SET NOT NULL;
ALTER TABLE farms.farm_z22_production_insurances ALTER COLUMN participant_pin SET NOT NULL;
ALTER TABLE farms.farm_z22_production_insurances ALTER COLUMN program_year SET NOT NULL;
ALTER TABLE farms.farm_z22_production_insurances ALTER COLUMN operation_number SET NOT NULL;
ALTER TABLE farms.farm_z22_production_insurances ALTER COLUMN production_insurance_number SET NOT NULL;
ALTER TABLE farms.farm_z22_production_insurances ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_z22_production_insurances ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_z22_production_insurances ALTER COLUMN when_created SET NOT NULL;
