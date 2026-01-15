CREATE TABLE farms.farm_rsn_rev_hog_inventories (
	rsn_rev_hog_inventory_id bigint NOT NULL,
	quantity_start decimal(19,3),
	quantity_end decimal(19,3),
	fmv_price decimal(13,2),
	inventory_item_code varchar(10) NOT NULL,
	rsn_rev_hog_result_id bigint NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_rsn_rev_hog_inventories IS E'RSN REV HOG INVENTORY contains the calculated amounts for the Reasonability Test: Hogs Revenue Test run against the scenario, by INVENTORY ITEM CODE.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_inventories.fmv_price IS E'FMV PRICE is the average market price obtained for a livestock for a given month.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_inventories.inventory_item_code IS E'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_inventories.quantity_end IS E'QUANTITY END is the closing quantity at the beginning of the year from REPORTED INVENTORY in CROP UNIT CODE.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_inventories.quantity_start IS E'QUANTITY START is the opening quantity at the beginning of the year from REPORTED INVENTORY in CROP UNIT CODE.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_inventories.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_inventories.rsn_rev_hog_inventory_id IS E'RSN REV HOG INVENTORY ID is a surrogate unique identifier for RSN REV HOG INVENTORY.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_inventories.rsn_rev_hog_result_id IS E'RSN REV HOG RESULT ID is a surrogate unique identifier for RSN REV HOG RESULT.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_inventories.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_inventories.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_inventories.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_rsn_rev_hog_inventories.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_rrhi_farm_ic_fk_i ON farms.farm_rsn_rev_hog_inventories (inventory_item_code);
ALTER TABLE farms.farm_rsn_rev_hog_inventories ADD CONSTRAINT farm_rrhi_pk PRIMARY KEY (rsn_rev_hog_inventory_id);
ALTER TABLE farms.farm_rsn_rev_hog_inventories ADD CONSTRAINT farm_rrhi_uk UNIQUE (rsn_rev_hog_result_id,inventory_item_code);
ALTER TABLE farms.farm_rsn_rev_hog_inventories ALTER COLUMN rsn_rev_hog_inventory_id SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_inventories ALTER COLUMN inventory_item_code SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_inventories ALTER COLUMN rsn_rev_hog_result_id SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_inventories ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_inventories ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_rsn_rev_hog_inventories ALTER COLUMN when_created SET NOT NULL;
