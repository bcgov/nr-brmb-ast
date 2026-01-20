CREATE TABLE farms.farm_z21_participant_suppls (
	inventory_key bigint NOT NULL,
	participant_pin integer NOT NULL,
	program_year smallint NOT NULL,
	operation_number smallint NOT NULL,
	inventory_type_code smallint NOT NULL,
	inventory_code integer NOT NULL,
	crop_unit_type smallint,
	crop_on_farm_acres decimal(13,3),
	crop_qty_produced decimal(14,3),
	quantity_end decimal(14,3),
	end_of_year_price decimal(13,2),
	end_of_year_amount decimal(13,2),
	crop_unseedable_acres decimal(13,3),
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_z21_participant_suppls IS E'Z21 PARTICIPANT SUPPL identifies Inventory information from parts 7, 8, 10, 11, 12 of the AgriStability Supplementary Form. Totals are not included, but can be calculated. The Inventory Type code describes which part of the form each row came from. This file will have multiple rows per participant and farming operation, for the current (latest) program year. This file is created by FIPD.  This is a staging object used to load temporary data set before being merged into the operational data.';
COMMENT ON COLUMN farms.farm_z21_participant_suppls.crop_on_farm_acres IS E'CROP ON FARM ACRES is the number of acres grown of the crop - section 8 column d.';
COMMENT ON COLUMN farms.farm_z21_participant_suppls.crop_qty_produced IS E'CROP QTY PRODUCED is the quantity  of a crop produced  - section 8 column e.';
COMMENT ON COLUMN farms.farm_z21_participant_suppls.crop_unit_type IS E'CROP UNIT TYPE is a numeric value assigned to a unit type for the crop inventory, section 8 column c. Foreign key to file 28.';
COMMENT ON COLUMN farms.farm_z21_participant_suppls.crop_unseedable_acres IS E'CROP UNSEEDABLE ACRES is the number of unseedable acres of the crop - section 8 column e.';
COMMENT ON COLUMN farms.farm_z21_participant_suppls.end_of_year_amount IS E'END OF YEAR AMOUNT is the the end of year dollar amount for Purchased Inputs, Defferred income/receivables or Accounts Payable.';
COMMENT ON COLUMN farms.farm_z21_participant_suppls.end_of_year_price IS E'END OF YEAR PRICE is the END OF YEAR PRICE for crop or livestock inventory entries.';
COMMENT ON COLUMN farms.farm_z21_participant_suppls.inventory_code IS E'INVENTORY CODE is a numeric code used to uniquely identify an inventory item.';
COMMENT ON COLUMN farms.farm_z21_participant_suppls.inventory_key IS E'INVENTORY KEY is the primary key for the file. Provides each row with a unique identifier over the whole file.';
COMMENT ON COLUMN farms.farm_z21_participant_suppls.inventory_type_code IS E'INVENTORY TYPE CODE is a numeric code indicating an inventory type. Valid values are 1 - Crops Inventory, 2 - Livestock  Inventory, 3 - Purchased Inputs, 4 - Deferred Income & Receivables, 5 - Accounts Payable.';
COMMENT ON COLUMN farms.farm_z21_participant_suppls.operation_number IS E'OPERATION NUMBER identifies each operation a participant reports for a given stab year. Operations may have different statement numbers in different program years.';
COMMENT ON COLUMN farms.farm_z21_participant_suppls.participant_pin IS E'PARTICIPANT PIN is the unique AgriStability/AgriInvest pin for this prodcuer. Was previous CAIS Pin and NISA Pin.';
COMMENT ON COLUMN farms.farm_z21_participant_suppls.program_year IS E'PROGRAM YEAR is the stabilization year for this record.';
COMMENT ON COLUMN farms.farm_z21_participant_suppls.quantity_end IS E'QUANTITY END is the ending inventory for livestock (section 7 column c) or crop (section 8 column f).';
COMMENT ON COLUMN farms.farm_z21_participant_suppls.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_z21_participant_suppls.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_z21_participant_suppls.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_z21_participant_suppls.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_z21_participant_suppls.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_z21_farm_z03_fk_i ON farms.farm_z21_participant_suppls (participant_pin, program_year, operation_number);
CREATE INDEX farm_z21_farm_z29_fk_i ON farms.farm_z21_participant_suppls (inventory_code, inventory_type_code);
ALTER TABLE farms.farm_z21_participant_suppls ADD CONSTRAINT farm_z21_pk PRIMARY KEY (inventory_key,participant_pin,program_year,operation_number);
ALTER TABLE farms.farm_z21_participant_suppls ALTER COLUMN inventory_key SET NOT NULL;
ALTER TABLE farms.farm_z21_participant_suppls ALTER COLUMN participant_pin SET NOT NULL;
ALTER TABLE farms.farm_z21_participant_suppls ALTER COLUMN program_year SET NOT NULL;
ALTER TABLE farms.farm_z21_participant_suppls ALTER COLUMN operation_number SET NOT NULL;
ALTER TABLE farms.farm_z21_participant_suppls ALTER COLUMN inventory_type_code SET NOT NULL;
ALTER TABLE farms.farm_z21_participant_suppls ALTER COLUMN inventory_code SET NOT NULL;
ALTER TABLE farms.farm_z21_participant_suppls ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_z21_participant_suppls ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_z21_participant_suppls ALTER COLUMN when_created SET NOT NULL;
