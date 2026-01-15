CREATE TABLE farms.farm_z23_livestock_prod_cpcts (
	productive_capacity_key bigint NOT NULL,
	participant_pin integer NOT NULL,
	program_year smallint NOT NULL,
	operation_number smallint NOT NULL,
	inventory_code integer NOT NULL,
	productive_capacity_amount decimal(14,3) NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_z23_livestock_prod_cpcts IS E'Z23 LIVESTOCK PROD CPCT identifies the Livestock production capacity information provided by the participant in section 9 of the supplemental page of the AgriStability application. This file will have 0 to many rows per participant and farming operation.This file is created by FIPD. This is a staging object used to load temporary data set before being merged into the operational data.';
COMMENT ON COLUMN farms.farm_z23_livestock_prod_cpcts.inventory_code IS E'INVENTORY CODE is a numeric code used to uniquely identify an inventory item.';
COMMENT ON COLUMN farms.farm_z23_livestock_prod_cpcts.operation_number IS E'OPERATION NUMBER identifies each operation a participant reports for a given stab year. Operations may have different statement numbers in different program years.';
COMMENT ON COLUMN farms.farm_z23_livestock_prod_cpcts.participant_pin IS E'PARTICIPANT PIN is the unique AgriStability/AgriInvest pin for this prodcuer. Was previous CAIS Pin and NISA Pin.';
COMMENT ON COLUMN farms.farm_z23_livestock_prod_cpcts.productive_capacity_amount IS E'PRODUCTIVE CAPACITY AMOUNT is the quantity entered in section 9 for this inventory/bpu code.';
COMMENT ON COLUMN farms.farm_z23_livestock_prod_cpcts.productive_capacity_key IS E'PRODUCTIVE CAPACITY KEY is the primary key for the file. Provides each row with a unique identifier over the whole file.';
COMMENT ON COLUMN farms.farm_z23_livestock_prod_cpcts.program_year IS E'PROGRAM YEAR is the stabilization year for this record.';
COMMENT ON COLUMN farms.farm_z23_livestock_prod_cpcts.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_z23_livestock_prod_cpcts.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_z23_livestock_prod_cpcts.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_z23_livestock_prod_cpcts.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_z23_livestock_prod_cpcts.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_z23_farm_z03_fk_i ON farms.farm_z23_livestock_prod_cpcts (participant_pin, program_year, operation_number);
CREATE INDEX farm_z23_pin_yr_i ON farms.farm_z23_livestock_prod_cpcts (participant_pin, program_year);
ALTER TABLE farms.farm_z23_livestock_prod_cpcts ADD CONSTRAINT farm_z23_pk PRIMARY KEY (productive_capacity_key);
ALTER TABLE farms.farm_z23_livestock_prod_cpcts ALTER COLUMN productive_capacity_key SET NOT NULL;
ALTER TABLE farms.farm_z23_livestock_prod_cpcts ALTER COLUMN participant_pin SET NOT NULL;
ALTER TABLE farms.farm_z23_livestock_prod_cpcts ALTER COLUMN program_year SET NOT NULL;
ALTER TABLE farms.farm_z23_livestock_prod_cpcts ALTER COLUMN operation_number SET NOT NULL;
ALTER TABLE farms.farm_z23_livestock_prod_cpcts ALTER COLUMN inventory_code SET NOT NULL;
ALTER TABLE farms.farm_z23_livestock_prod_cpcts ALTER COLUMN productive_capacity_amount SET NOT NULL;
ALTER TABLE farms.farm_z23_livestock_prod_cpcts ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_z23_livestock_prod_cpcts ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_z23_livestock_prod_cpcts ALTER COLUMN when_created SET NOT NULL;
