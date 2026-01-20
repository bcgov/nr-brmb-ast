CREATE TABLE farms.farm_z42_participant_ref_years (
	productive_capacity_key bigint NOT NULL,
	participant_pin integer NOT NULL,
	program_year smallint NOT NULL,
	operation_number smallint NOT NULL,
	productive_type_code smallint NOT NULL,
	productive_code integer NOT NULL,
	productive_capacity_units decimal(13,2),
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_z42_participant_ref_years IS E'Z42 PARTICIPANT REF YEAR identifies reference year data by for productive units by both inventory codes and structure groups.  The productive capacity for each code (inventory or structure group) is displayed in column 7. This file will have multiple rows per participant and farming operation.This file is created by FIPD. This is a staging object used to load temporary data set before being merged into the operational data.';
COMMENT ON COLUMN farms.farm_z42_participant_ref_years.operation_number IS E'OPERATION NUMBER identifies each operation a participant reports for a given stab year. Operations may have different statement numbers in different program years.';
COMMENT ON COLUMN farms.farm_z42_participant_ref_years.participant_pin IS E'PARTICIPANT PIN is the unique AgriStability/AgriInvest pin for this prodcuer. Was previous CAIS Pin and NISA Pin.';
COMMENT ON COLUMN farms.farm_z42_participant_ref_years.productive_capacity_key IS E'PRODUCTIVE CAPACITY KEY is the primary key for the file. Provides each row with a unique identifier over the whole file.';
COMMENT ON COLUMN farms.farm_z42_participant_ref_years.productive_capacity_units IS E'PRODUCTIVE CAPACITY UNITS is the number of units of production FIPD has used when processing future year payments. # of head for livestock, the amount of acres/square meters for crop production.';
COMMENT ON COLUMN farms.farm_z42_participant_ref_years.productive_code IS E'PRODUCTIVE CODE is the structure group or inventory code.';
COMMENT ON COLUMN farms.farm_z42_participant_ref_years.productive_type_code IS E'PRODUCTIVE TYPE CODE iIndicates if this is an Inventory Code 1, or a STRUCTURE GROUP CODE - 2.';
COMMENT ON COLUMN farms.farm_z42_participant_ref_years.program_year IS E'PROGRAM YEAR is the stabilization year for this record.';
COMMENT ON COLUMN farms.farm_z42_participant_ref_years.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_z42_participant_ref_years.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_z42_participant_ref_years.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_z42_participant_ref_years.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_z42_participant_ref_years.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_z42_farm_z03_fk_i ON farms.farm_z42_participant_ref_years (participant_pin, program_year, operation_number);
ALTER TABLE farms.farm_z42_participant_ref_years ADD CONSTRAINT farm_z42_pk PRIMARY KEY (productive_capacity_key);
ALTER TABLE farms.farm_z42_participant_ref_years ALTER COLUMN productive_capacity_key SET NOT NULL;
ALTER TABLE farms.farm_z42_participant_ref_years ALTER COLUMN participant_pin SET NOT NULL;
ALTER TABLE farms.farm_z42_participant_ref_years ALTER COLUMN program_year SET NOT NULL;
ALTER TABLE farms.farm_z42_participant_ref_years ALTER COLUMN operation_number SET NOT NULL;
ALTER TABLE farms.farm_z42_participant_ref_years ALTER COLUMN productive_type_code SET NOT NULL;
ALTER TABLE farms.farm_z42_participant_ref_years ALTER COLUMN productive_code SET NOT NULL;
ALTER TABLE farms.farm_z42_participant_ref_years ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_z42_participant_ref_years ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_z42_participant_ref_years ALTER COLUMN when_created SET NOT NULL;
