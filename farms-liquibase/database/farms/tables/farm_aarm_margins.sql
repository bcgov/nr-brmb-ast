CREATE TABLE farms.farm_aarm_margins (
	aarm_margin_id bigint NOT NULL,
	participant_pin integer NOT NULL,
	program_year smallint NOT NULL,
	operation_number smallint NOT NULL,
	partner_percent decimal(7,4),
	inventory_type_code smallint NOT NULL,
	inventory_code integer NOT NULL,
	inventory_description varchar(256),
	production_unit smallint,
	aarm_reference_p1_price decimal(13,2),
	aarm_reference_p2_price decimal(13,2),
	quantity_start decimal(14,3),
	quantity_end decimal(14,3),
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON COLUMN farms.farm_aarm_margins.aarm_margin_id IS E'AARM MARGIN ID is a sequential numbergiven in the first column of the AARM CSV file.';
COMMENT ON COLUMN farms.farm_aarm_margins.aarm_reference_p1_price IS E'AARM REFERENCE P1 PRICE identifies that when processing 2007 payments, the start of year prices for each reference year could be manipulated for AARM purposes, to adjust the calculated margin for that year. This would affect processing of the PROGRAM YEAR (2007), but not affect processing of the year being adjusted. This field will only be populated if the start of year price has been over-ridden.';
COMMENT ON COLUMN farms.farm_aarm_margins.aarm_reference_p2_price IS E'AARM REFERENCE P2 PRICE identifies that when processing 2007 payments, the end of year prices for each reference year could be manipulated for AARM purposes, to adjust the calculated margin for that year. This would affect processing of the PROGRAM YEAR (2007), but not affect processing of the year being adjusted. This field will only be populated if the end of year price has been over-ridden.';
COMMENT ON COLUMN farms.farm_aarm_margins.inventory_code IS E'INVENTORY CODE is a numeric code used to uniquely identify an inventory item.';
COMMENT ON COLUMN farms.farm_aarm_margins.inventory_description IS E'INVENTORY DESCRIPTION is the english description of the Inventory. An English text description of an inventory item.';
COMMENT ON COLUMN farms.farm_aarm_margins.inventory_type_code IS E'INVENTORY TYPE CODE is a numeric code indicating an inventory type. Valid values are 1 - Crops Inventory, 2 - Livestock  Inventory, 3 - Purchased Inputs, 4 - Deferred Income & Receivables, 5 - Accounts Payable.';
COMMENT ON COLUMN farms.farm_aarm_margins.operation_number IS E'OPERATION NUMBER identifies each operation a participant reports for a given stab year. Operations may have different statement numbers in different program years.';
COMMENT ON COLUMN farms.farm_aarm_margins.participant_pin IS E'PARTICIPANT PIN is the unique AgriStability/AgriInvest pin for this prodcuer. Was previous CAIS Pin and NISA Pin.';
COMMENT ON COLUMN farms.farm_aarm_margins.partner_percent IS E'PARTNER PERCENT is the partners percentage share.';
COMMENT ON COLUMN farms.farm_aarm_margins.production_unit IS E'PRODUCTION UNIT is the unit of measure code.';
COMMENT ON COLUMN farms.farm_aarm_margins.program_year IS E'PROGRAM YEAR is the year this data pertains to.';
COMMENT ON COLUMN farms.farm_aarm_margins.quantity_end IS E'QUANTITY END is the ending inventory for livestock (section 7 column c) or crop (section 8 column f).';
COMMENT ON COLUMN farms.farm_aarm_margins.quantity_start IS E'QUANTITY START is the Start of year quantity of inventory. For livestock this will always be # of Head.';
COMMENT ON COLUMN farms.farm_aarm_margins.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_aarm_margins.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_aarm_margins.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_aarm_margins.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_aarm_margins.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_aarm_asc_py_fo_i ON farms.farm_aarm_margins (participant_pin, program_year, operation_number);
CREATE INDEX farm_aarm_pyo_inv_i ON farms.farm_aarm_margins (participant_pin, program_year, operation_number, inventory_type_code, inventory_code);
ALTER TABLE farms.farm_aarm_margins ADD CONSTRAINT farm_aarm_pk PRIMARY KEY (aarm_margin_id);
ALTER TABLE farms.farm_aarm_margins ALTER COLUMN aarm_margin_id SET NOT NULL;
ALTER TABLE farms.farm_aarm_margins ALTER COLUMN participant_pin SET NOT NULL;
ALTER TABLE farms.farm_aarm_margins ALTER COLUMN program_year SET NOT NULL;
ALTER TABLE farms.farm_aarm_margins ALTER COLUMN operation_number SET NOT NULL;
ALTER TABLE farms.farm_aarm_margins ALTER COLUMN inventory_type_code SET NOT NULL;
ALTER TABLE farms.farm_aarm_margins ALTER COLUMN inventory_code SET NOT NULL;
ALTER TABLE farms.farm_aarm_margins ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_aarm_margins ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_aarm_margins ALTER COLUMN when_created SET NOT NULL;
