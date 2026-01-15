CREATE TABLE farms.farm_reported_inventories (
	reported_inventory_id bigint NOT NULL,
	price_start decimal(13,2),
	price_end decimal(13,2),
	end_year_producer_price decimal(13,2),
	start_of_year_amount decimal(14,3),
	end_of_year_amount decimal(14,3),
	quantity_start decimal(14,3),
	quantity_end decimal(14,3),
	quantity_produced decimal(14,3),
	accept_producer_price_ind varchar(1),
	aarm_reference_p1_price decimal(13,2),
	aarm_reference_p2_price decimal(13,2),
	on_farm_acres decimal(13,3),
	unseedable_acres decimal(13,3),
	import_comment varchar(128),
	crop_unit_code varchar(10),
	agristabilty_cmmdty_xref_id bigint NOT NULL,
	farming_operation_id bigint NOT NULL,
	agristability_scenario_id bigint,
	cra_reported_inventory_id bigint,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_reported_inventories IS E'REPORTED INVENTORY information contains production details for agricultural commodities, including Production Unit (i.e. quantity and values of livestock and crops) as well as accrual data. REPORTED INVENTORY data originates from the federal data imports (collected via "harmonized forms"). REPORTED INVENTORY is associated with each FARMING OPERATION however REPORTED INCOME EXPENSE data must also exist for each FARMING OPERATION (see Requirements). REPORTED INVENTORY data may have associated Form Data Adjustments.';
COMMENT ON COLUMN farms.farm_reported_inventories.aarm_reference_p1_price IS E'AARM REFERENCE P1 PRICE identifies the start of year prices for 2007 payment processing. When processing 2007 payments, the start of year prices for each reference year could be manipulated for AARM purposes, to adjust the calculated margin for that year. This would affect processing of the PROGRAM YEAR (2007), but not affect processing of the year being adjusted. This field will only be populated if the start of year price has been over-ridden. Overides the start year price when using this year as a reference margin.';
COMMENT ON COLUMN farms.farm_reported_inventories.aarm_reference_p2_price IS E'AARM REFERENCE P2 PRICE identifies the start of year prices for 2007 payment processing. When processing 2007 payments, the start of year prices for each reference year could be manipulated for AARM purposes, to adjust the calculated margin for that year. This would affect processing of the PROGRAM YEAR (2007), but not affect processing of the year being adjusted. This field will only be populated if the start of year price has been over-ridden. Overides the end year price when using this year as a reference margin.';
COMMENT ON COLUMN farms.farm_reported_inventories.accept_producer_price_ind IS E'ACCEPT PRODUCER PRICE IND indicates if the P2 Producer price was used, even if it was outside FMV bands for the reference year.';
COMMENT ON COLUMN farms.farm_reported_inventories.agristability_scenario_id IS E'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.';
COMMENT ON COLUMN farms.farm_reported_inventories.agristabilty_cmmdty_xref_id IS E'AGRISTABILTY COMMDTY XREF ID is a surrogate unique identifier for an AGRISTABILTY COMMDTY XREF.';
COMMENT ON COLUMN farms.farm_reported_inventories.cra_reported_inventory_id IS E'REPORTED INVENTORY ID is a surrogate unique identifier for REPORTED INVENTORY.';
COMMENT ON COLUMN farms.farm_reported_inventories.crop_unit_code IS E'CROP UNIT CODE is a unique code for the object UNIT CODE described as a numeric code used to uniquely identify the unit of measure for the crop inventory .  Examples of codes and descriptions are 1-Pounds, 2-Tonnes, 3-Dozen, 4-Bushels.';
COMMENT ON COLUMN farms.farm_reported_inventories.end_of_year_amount IS E'END OF YEAR AMOUNT is the end of year dollar amount for Purchased Inputs, Deferred Income/receivables or Accounts Payable.';
COMMENT ON COLUMN farms.farm_reported_inventories.end_year_producer_price IS E'END YEAR PRODUCER PRICE is the price supplied by the participant for the end of the year.';
COMMENT ON COLUMN farms.farm_reported_inventories.farming_operation_id IS E'FARMING OPERATION ID is a surrogate unique identifier for FARMING OPERATIONs.';
COMMENT ON COLUMN farms.farm_reported_inventories.import_comment IS E'IMPORT COMMENT is a system generated comment about how the original data might have been modified to enable the data to be inserted into the operational tables. Usually this involves changing an invalid code.';
COMMENT ON COLUMN farms.farm_reported_inventories.on_farm_acres IS E'ON FARM ACRES is the number of acres grown of the crop.';
COMMENT ON COLUMN farms.farm_reported_inventories.price_end IS E'PRICE END is the End of Year (P2) inventory price reported by the participant.';
COMMENT ON COLUMN farms.farm_reported_inventories.price_start IS E'PRICE START is the opening price used when calculating the benefit for the reference year.';
COMMENT ON COLUMN farms.farm_reported_inventories.quantity_end IS E'QUANTITY END is the end of year quantity of inventory.';
COMMENT ON COLUMN farms.farm_reported_inventories.quantity_produced IS E'QUANTITY PRODUCED is the quantity of a crop produced - section 8 column e.';
COMMENT ON COLUMN farms.farm_reported_inventories.quantity_start IS E'QUANTITY START is the start of year quantity of inventory.';
COMMENT ON COLUMN farms.farm_reported_inventories.reported_inventory_id IS E'REPORTED INVENTORY ID is a surrogate unique identifier for REPORTED INVENTORY.';
COMMENT ON COLUMN farms.farm_reported_inventories.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_reported_inventories.start_of_year_amount IS E'START OF YEAR AMOUNT is the start of year dollar amount for Purchased Inputs, Deferred Income/receivables or Accounts Payable.';
COMMENT ON COLUMN farms.farm_reported_inventories.unseedable_acres IS E'UNSEEDABLE ACRES is the number of unseedable acres of the crop - section 8 column e.';
COMMENT ON COLUMN farms.farm_reported_inventories.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_reported_inventories.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_reported_inventories.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_reported_inventories.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_ri_farm_acx_fk_i ON farms.farm_reported_inventories (agristabilty_cmmdty_xref_id);
CREATE INDEX farm_ri_farm_as_fk_i ON farms.farm_reported_inventories (agristability_scenario_id);
CREATE INDEX farm_ri_farm_cuc_fk_i ON farms.farm_reported_inventories (crop_unit_code);
CREATE INDEX farm_ri_farm_fo_fk_i ON farms.farm_reported_inventories (farming_operation_id);
CREATE INDEX farm_ri_farm_ri_fk_i ON farms.farm_reported_inventories (cra_reported_inventory_id);
ALTER TABLE farms.farm_reported_inventories ADD CONSTRAINT farm_ri_pk PRIMARY KEY (reported_inventory_id);
ALTER TABLE farms.farm_reported_inventories ALTER COLUMN reported_inventory_id SET NOT NULL;
ALTER TABLE farms.farm_reported_inventories ALTER COLUMN agristabilty_cmmdty_xref_id SET NOT NULL;
ALTER TABLE farms.farm_reported_inventories ALTER COLUMN farming_operation_id SET NOT NULL;
ALTER TABLE farms.farm_reported_inventories ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_reported_inventories ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_reported_inventories ALTER COLUMN when_created SET NOT NULL;
